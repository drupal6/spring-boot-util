package com.springboot.zookeeper.springbootzookeeper.config;

import com.springboot.zookeeper.springbootzookeeper.entity.ServiceNode;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ConfigurationProperties("zk")
@Configuration
public class ZookeeperConf {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZookeeperConf.class);

    private final static String TYPE = "type";
    private final static String BPATH = "bpath";
    private final static String ADDRESS = "address";
    private final static String PORT = "port";

    @Value("${zk.url}")
    private String zkUrl;

    //节点配置
    private Map<String, Map<String, String>> service;

    //是否是领导
    private boolean isLeader = false;

    //服务节点信息
    private Map<String, Map<String, ServiceNode>> servicesMap = new ConcurrentHashMap<String, Map<String, ServiceNode>>();

    public Map<String, Map<String, String>> getService() {
        return service;
    }

    public void setService(Map<String, Map<String, String>> service) {
        this.service = service;
    }

    public boolean isLeader() {
        return isLeader;
    }

    @Bean
    public CuratorFramework getZKCleint() throws Exception{
        //重连策略 初始重连间隔1000毫秒，尝试重连次数3
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(zkUrl, retryPolicy);
        //选举监听
//        leaderListener(zkClient);
        zkClient.start();
//        //初始服务
//        initServices(zkClient);
        return zkClient;
    }

    /**
     * 选举监听
     * @param zkClient
     */
    private void leaderListener(CuratorFramework zkClient) {
        LeaderSelectorListenerAdapter listener = new LeaderSelectorListenerAdapter() {
            public void takeLeadership(CuratorFramework client)  {
                isLeader = true;
                System.out.println(">>>>leader:" + isLeader);
            }
        };
        LeaderSelector selector = new LeaderSelector(zkClient, "/leader", listener);
        selector.autoRequeue();
        selector.start();
    }

    /**
     * 初始服务
     * @throws Exception
     */
    public void initServices(CuratorFramework zkClient) throws Exception{
        if(service != null && false == service.isEmpty()) {
            for(String serviceName : service.keySet()) {
                Map<String, String> conf = service.get(serviceName);
                if(conf.get(TYPE).equals("provider")) {
                    //注册服务
                    registerService(zkClient, conf.get(BPATH), conf.get(ADDRESS), Integer.parseInt(conf.get(PORT)), serviceName, null);
                } else if (conf.get(TYPE).equals("consumer")) {
                    //发现服务
                    discoverService(zkClient, conf.get(BPATH), serviceName);
                    //监听服务节点变化
                    treeAddListener(zkClient, conf.get(BPATH), serviceName);
                } else {
                    throw new IllegalArgumentException("zk service type not found. serviceName:" + serviceName);
                }
            }
        }
    }

    /**
     * 注册单个服务
     * @param basePath
     * @param address
     * @param port
     * @param serviceName
     * @param extData
     * @throws Exception
     */
    private void registerService(CuratorFramework zkClient, String basePath, String address
            , int port, String serviceName, Map<String, Object> extData) throws Exception {
        //构造服务描述
        ServiceInstanceBuilder<Map> service = ServiceInstance.builder();
        service.address(address);
        service.port(port);
        service.name(serviceName);
        //额外信息
        if(extData != null) {
            service.payload(extData);
        }
        ServiceInstance<Map> instance = service.build();
        ServiceDiscovery<Map> serviceDiscovery = ServiceDiscoveryBuilder.builder(Map.class)
                .client(zkClient)
                .serializer(new JsonInstanceSerializer<Map>(Map.class))
                .basePath(basePath)
                .build();
        serviceDiscovery.registerService(instance);
        serviceDiscovery.start();
        LOGGER.info("registerService is success! basePath:{}, address:{}, port:{}, serviceName:{}"
                , basePath, address, port, serviceName);
    }

    /**
     * 发现服务
     * @return
     */
    private void discoverService(CuratorFramework zkClient, String basePath, String serviceName) throws Exception{
        ServiceDiscovery<Map> serviceDiscovery = ServiceDiscoveryBuilder.builder(Map.class)
                .client(zkClient)
                .serializer(new JsonInstanceSerializer<Map>(Map.class))
                .basePath(basePath)
                .build();
        serviceDiscovery.start();
        Collection<ServiceInstance<Map>> all = serviceDiscovery.queryForInstances(serviceName);
        if(all.isEmpty()) {
            LOGGER.warn("not found zkservice. basePath:{}, serviceName:{}", basePath, serviceName);
            return;
        } else {
            for(ServiceInstance<Map> si : all) {
                addServiceNode(serviceName, si.getId(), si.getAddress(), si.getPort());
            }
        }
    }

    /**
     * 添加服务节点数据
     * @param id
     * @param address
     * @param port
     */
    private void addServiceNode(String serviceName, String id, String address, int port) {
        Map<String, ServiceNode> nodeMap = servicesMap.get(serviceName);
        if(nodeMap == null) {
            nodeMap = new ConcurrentHashMap<String, ServiceNode>();
            servicesMap.put(serviceName, nodeMap);
        }
        ServiceNode node = new ServiceNode(id, address, port);
        nodeMap.put(id, node);
        LOGGER.info("add serviceNode. serviceName:{}, id:{}, address:{}, port:{}", serviceName, id, address, port);
    }

    /**
     * 根据serviceName和id移除服务节点
     * @param serviceName
     * @param id
     */
    private void removeNode(String serviceName, String id) {
        Map<String, ServiceNode> nodeMap = servicesMap.get(serviceName);
        if(nodeMap == null) {
            return;
        }
        nodeMap.remove(id);
        if(nodeMap.isEmpty()) {
            servicesMap.remove(serviceName);
        }
        LOGGER.info("remove serviceNode. serviceName:{}, id:{}", serviceName, id);
    }

    /**
     * 根据serviceName和id获取服务节点
     * @param serviceName
     * @param id
     * @return
     */
    private ServiceNode getServiceNode(String serviceName, String id) {
        Map<String, ServiceNode> nodeMap = servicesMap.get(serviceName);
        if(nodeMap == null) {
            return null;
        }
        return nodeMap.get(id);
    }

    public Map<String, ServiceNode> getNodeMap(String serviceName) {
        return servicesMap.get(serviceName);
    }

    /**
     * 监听所有子节点(限定支持2层节点)
     * @param zkClient
     * @param basePath
     * @throws Exception
     */
    private void treeAddListener(CuratorFramework zkClient, String basePath, String serviceName) throws Exception {
        String path = basePath + "/" + serviceName;
        TreeCache cache = new TreeCache(zkClient, path);
        cache.start();
        ServiceNodeListener serviceNodeListener = new ServiceNodeListener(path, serviceName);
        cache.getListenable().addListener(serviceNodeListener);
    }

    class ServiceNodeListener implements TreeCacheListener {
        String path;
        String serviceName;

        public ServiceNodeListener(String path, String serviceName) {
            this.path = path;
            this.serviceName = serviceName;
        }

        @Override
        public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent event) throws Exception {
            if(event.getData() == null || event.getData().getPath() == null) {
                return;
            }
            LOGGER.info("node change event. type:{}, path:{}, data:{}", event.getType().name()
                    , event.getData().getPath(), new String(event.getData().getData()));
            if(path.equals(event.getData().getPath())) {
                //父节点变更 只处理移除事件
                switch (event.getType()) {
                    case NODE_REMOVED:
                        servicesMap.remove(serviceName);
                        break;
                    default:
                        break;
                }
            } else {
                //子节点变更
                switch (event.getType()) {
                    case NODE_ADDED:
                        ServiceInstance<Map> nodeData = new JsonInstanceSerializer<Map>(Map.class).deserialize(event.getData().getData());
                        addServiceNode(serviceName, nodeData.getId(), nodeData.getAddress(), nodeData.getPort());
                        break;
                    case NODE_UPDATED:
                        ServiceInstance<Map> nodeData1 = new JsonInstanceSerializer<Map>(Map.class).deserialize(event.getData().getData());
                        ServiceNode node = getServiceNode(serviceName, nodeData1.getId());
                        if(node == null) {
                            //新建
                            addServiceNode(serviceName, nodeData1.getId(), nodeData1.getAddress(), nodeData1.getPort());
                        } else {
                            //更新
                            node.setAddress(nodeData1.getAddress());
                            node.setPort(nodeData1.getPort());
                        }
                        break;
                    case NODE_REMOVED:
                        removeNode(serviceName, event.getData().getPath());
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
