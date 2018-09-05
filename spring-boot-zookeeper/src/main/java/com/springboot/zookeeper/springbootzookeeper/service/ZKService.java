package com.springboot.zookeeper.springbootzookeeper.service;

import com.springboot.zookeeper.springbootzookeeper.config.ZookeeperConf;
import com.springboot.zookeeper.springbootzookeeper.entity.ServiceNode;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.ProviderNotFoundException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class ZKService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZKService.class);

    @Autowired
    private CuratorFramework zkClient;
    @Autowired
    private ZookeeperConf zkConfig;

    /**
     * 初始化服务
     */
    public void init() throws Exception {
        zkConfig.initServices(zkClient);
    }

    /**
     * 停止服务
     */
    public void stop() {
        zkClient.close();
    }

    public boolean isLeader() {
        return zkConfig.isLeader();
    }

    /**
     * 随机负载均衡
     * @param serviceName
     * @return
     * @throws ProviderNotFoundException
     */
    public ServiceNode choosNode(String serviceName) throws ProviderNotFoundException{
        Map<String, ServiceNode> nodeMap = zkConfig.getNodeMap(serviceName);
        if(nodeMap == null || nodeMap.isEmpty()) {
            throw new ProviderNotFoundException("service name:" + serviceName);
        }
        Integer[] keys = nodeMap.keySet().toArray(new Integer[0]);
        Random random = new Random();
        Integer randomKey = keys[random.nextInt(keys.length)];
        return nodeMap.get(randomKey);
    }

    /**
     * 分布式锁
     * @param key
     * @param lockHandle
     */
    public void lock(String key, ILockHandle lockHandle) {
        String path = "/lock/" + key;
        try{
            InterProcessMutex lock = new InterProcessMutex(zkClient, path);
            if(lock.acquire(10, TimeUnit.HOURS)) {
                try {
                    lockHandle.lockHandle();
                } finally {
                    lock.release();
                }
            } else {
                LOGGER.info("locked. keyd:{}", key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
