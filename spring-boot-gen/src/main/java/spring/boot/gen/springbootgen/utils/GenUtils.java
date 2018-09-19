package spring.boot.gen.springbootgen.utils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.core.env.Environment;
import spring.boot.gen.springbootgen.entity.ColumnEntity;
import spring.boot.gen.springbootgen.entity.TableEntity;
import spring.boot.gen.springbootgen.exception.RRException;


/**
 * 代码生成器   工具类
 * 
 * @date 2016年12月19日 下午11:40:24
 */
public class GenUtils {
	
	/**
	 * 生成代码
	 */
	public static void generatorCode(Map<String, String> table,
			List<Map<String, String>> columns, List<Map<String, String>> indexs, Environment env){
		//配置信息
		Configuration config = getConfig();
		
		Map<String,  String> indexMap = new HashMap<String,  String>();
		if(indexs != null) {
			for(Map<String, String> map : indexs) {
				if(map.get("Key_name") != null && false == map.get("Key_name").isEmpty()) {
					indexMap.put(map.get("Column_name"), map.get("Key_name"));
				}
			}
		}
		
		//表信息
		TableEntity tableEntity = new TableEntity();
		tableEntity.setTableName(table.get("tableName"));
		tableEntity.setComments(table.get("tableComment"));
		//表名转换成Java类名
		String className = tableToJava(tableEntity.getTableName(), config.getString("tablePrefix"));
		tableEntity.setClassName(className);
		tableEntity.setClassname(StringUtils.uncapitalize(className));
		
		//列信息
		List<ColumnEntity> columsList = new ArrayList<>();
		boolean hasDate = false;
		for(Map<String, String> column : columns){
			ColumnEntity columnEntity = new ColumnEntity();
			columnEntity.setColumnName(column.get("columnName"));
			columnEntity.setDataType(column.get("dataType"));
			columnEntity.setComments(column.get("columnComment"));
			columnEntity.setExtra(column.get("extra"));
			columnEntity.setColumnKey(column.get("columnKey"));
			
			//列名转换成Java属性名
			String attrName = columnToJava(columnEntity.getColumnName());
			if(false == hasDate && attrName.equals("Date")) {
				hasDate = true;
			}
			columnEntity.setAttrName(attrName);
			columnEntity.setAttrname(StringUtils.uncapitalize(attrName));
			
			//列的数据类型，转换成Java类型
			String attrType = config.getString(columnEntity.getDataType(), "unknowType");
			columnEntity.setAttrType(attrType);
			
//			//是否主键
			if("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null){
				tableEntity.setPk(columnEntity);
			} else {
				if(indexMap.containsKey(columnEntity.getColumnName())) {
					tableEntity.addIndex(indexMap.get(columnEntity.getColumnName()), columnEntity);
				}
			}
			columsList.add(columnEntity);
		}
		tableEntity.setColumns(columsList);
		
		//没主键，则第一个字段为主键
		if(tableEntity.getPk() == null){
			tableEntity.setPk(tableEntity.getColumns().get(0));
		}
		
		//设置velocity资源加载器
		Properties prop = new Properties();  
		prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");  
		Velocity.init(prop);
		
		String projectpath = env.getProperty("project.path");
		String entitypackage = env.getProperty("entity.package");
		String mapperpackage = env.getProperty("mapper.package");
		String xmlpackage = env.getProperty("xml.package");
		String controllerpackage = env.getProperty("controller.package");
		String servicepackage = env.getProperty("service.package");
		
		//封装模板数据
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", tableEntity.getTableName());
		map.put("comments", tableEntity.getComments());
		map.put("pk", tableEntity.getPk());
		map.put("className", tableEntity.getClassName());
		map.put("classname", tableEntity.getClassname());
		map.put("pathName", tableEntity.getClassname().toLowerCase());
		map.put("columns", tableEntity.getColumns());
		map.put("clomunMap", tableEntity.getClomunMap());
		map.put("entitypackage", entitypackage);
		map.put("mapperpackage", mapperpackage);
		map.put("controllerpackage", controllerpackage);
		map.put("servicepackage", servicepackage);
		map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
		map.put("hasDate", hasDate);
		map.put("hasList", tableEntity.hasList());
        VelocityContext context = new VelocityContext(map);
        generatorEntity(context, tableEntity, projectpath, entitypackage);
        generatorMapper(context, tableEntity, projectpath, mapperpackage);
        generatorMapperXML(context, tableEntity, projectpath, xmlpackage);
        generatorController(context, tableEntity, projectpath, controllerpackage);
        generatorService(context, tableEntity, projectpath, servicepackage);
	}
	
	private static void generatorEntity(VelocityContext context, TableEntity tableEntity, String projectpath, String packagePath) {
		StringWriter sw = new StringWriter();
		String template = "template/Entity.java.vm";
		Template tpl = Velocity.getTemplate(template, "UTF-8");
		tpl.merge(context, sw);
		
		try {
			File directory = getDirectory(projectpath + "/src/main/java", packagePath);
			File targetFile = new File(directory, tableEntity.getClassName() + ".java");
			writeFile(targetFile, sw.toString(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void generatorMapper(VelocityContext context, TableEntity tableEntity, String projectpath, String packagePath) {
		StringWriter sw = new StringWriter();
		String template = "template/Mapper.java.vm";
		Template tpl = Velocity.getTemplate(template, "UTF-8");
		tpl.merge(context, sw);
		
		try {
			File directory = getDirectory(projectpath + "/src/main/java", packagePath);
			File targetFile = new File(directory, tableEntity.getClassName() + "Mapper.java");
			writeFile(targetFile, sw.toString(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void generatorMapperXML(VelocityContext context, TableEntity tableEntity, String projectpath, String packagePath) {
		StringWriter sw = new StringWriter();
		String template = "template/Mapper.xml.vm";
		Template tpl = Velocity.getTemplate(template, "UTF-8");
		tpl.merge(context, sw);
		
		try {
			File directory = getDirectory(projectpath + "/src/main/resources", packagePath);
			File targetFile = new File(directory, tableEntity.getClassName() + "Mapper.xml");
			writeFile(targetFile, sw.toString(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void generatorController(VelocityContext context, TableEntity tableEntity, String projectpath, String packagePath) {
		StringWriter sw = new StringWriter();
		String template = "template/controller.java.vm";
		Template tpl = Velocity.getTemplate(template, "UTF-8");
		tpl.merge(context, sw);
		
		try {
			File directory = getDirectory(projectpath + "/src/main/java", packagePath);
			File targetFile = new File(directory, tableEntity.getClassName() + "Controller.java");
			if(targetFile.exists()) {
				System.err.println(tableEntity.getClassName() + "Controller.java文件已经存在，不重新生成");
				return;
			}
			writeFile(targetFile, sw.toString(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void generatorService(VelocityContext context, TableEntity tableEntity, String projectpath, String packagePath) {
		StringWriter sw = new StringWriter();
		String template = "template/service.java.vm";
		Template tpl = Velocity.getTemplate(template, "UTF-8");
		tpl.merge(context, sw);
		try {
			File directory = getDirectory(projectpath + "/src/main/java", packagePath);
			File targetFile = new File(directory, tableEntity.getClassName() + "Service.java");
			if(targetFile.exists()) {
				System.err.println(tableEntity.getClassName() + "Service.java文件已经存在，不重新生成");
				return;
			}
			writeFile(targetFile, sw.toString(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    private static File getDirectory(String targetProject, String targetPackage) throws Exception {

        File project = new File(targetProject);
        if (!project.isDirectory()) {
            throw new Exception("no found targetPorject:" + targetProject);
        }

        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackage, ".");
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        File directory = new File(project, sb.toString());
        if (!directory.isDirectory()) {
            boolean rc = directory.mkdirs();
            if (!rc) {
                throw new Exception("not found targetPackage:" + sb.toString());
            }
        }
        return directory;
    }
	
	 private static void writeFile(File file, String content, String fileEncoding) throws IOException {
	        FileOutputStream fos = new FileOutputStream(file, false);
	        OutputStreamWriter osw;
	        if (fileEncoding == null) {
	            osw = new OutputStreamWriter(fos);
	        } else {
	            osw = new OutputStreamWriter(fos, fileEncoding);
	        }
	        
	        BufferedWriter bw = new BufferedWriter(osw);
	        bw.write(content);
	        bw.close();
	    }
	
	
	/**
	 * 列名转换成Java属性名
	 */
	public static String columnToJava(String columnName) {
		return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
	}
	
	/**
	 * 表名转换成Java类名
	 */
	public static String tableToJava(String tableName, String tablePrefix) {
		if(StringUtils.isNotBlank(tablePrefix)){
			tableName = tableName.replace(tablePrefix, "");
		}
		return columnToJava(tableName);
	}
	
	/**
	 * 获取配置信息
	 */
	public static Configuration getConfig(){
		try {
			return new PropertiesConfiguration("generator.properties");
		} catch (ConfigurationException e) {
			throw new RRException("获取配置文件失败，", e);
		}
	}
}
