package spring.boot.server.springbootserver.mapper;

import java.util.List;
import java.util.Map;

/**
 * 基础Dao(还需在XML文件里，有对应的SQL语句)
 */
public interface BaseMapper<T> {
	
	void save(T t);
	
	int update(T t);
	
	int update(Map<String, Object> map);
	
	int delete(Object id);

	T queryObject(Object id);
	
	List<T> queryList();
	
}
