package spring.boot.gen.springbootgen.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import spring.boot.gen.springbootgen.mapper.SysGeneratorMapper;
import spring.boot.gen.springbootgen.service.SysGeneratorService;
import spring.boot.gen.springbootgen.utils.GenUtils;

@Service("sysGeneratorService")
public class SysGeneratorServiceImpl implements SysGeneratorService {
	@Autowired
	private SysGeneratorMapper sysGeneratorMapper;
	@Autowired
	private Environment env;

	@Override
	public List<Map<String, Object>> queryList(Map<String, Object> map) {
		return sysGeneratorMapper.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysGeneratorMapper.queryTotal(map);
	}

	@Override
	public Map<String, String> queryTable(String tableName) {
		return sysGeneratorMapper.queryTable(tableName);
	}

	@Override
	public List<Map<String, String>> queryColumns(String tableName) {
		return sysGeneratorMapper.queryColumns(tableName);
	}

	@Override
	public void generatorCode() {
		String[] tableNames = env.getProperty("tables").trim().split(",");
		for(String tableName : tableNames){
			//索引信息
			List<Map<String, String>> indexs = sysGeneratorMapper.queryIndex(tableName);
			//查询表信息
			Map<String, String> table = queryTable(tableName);
			
			//查询列信息
			List<Map<String, String>> columns = queryColumns(tableName);
			//生成代码
			GenUtils.generatorCode(table, columns, indexs, env);
		}
	}
}
