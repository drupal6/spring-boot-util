package spring.boot.gen.springbootgen.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysGeneratorMapper {

    List<Map<String, Object>> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    Map<String, String> queryTable(String tableName);

    @Select("show index from ${tableName}")
    List<Map<String, String>> queryIndex(@Param("tableName") String tableName);

    List<Map<String, String>> queryColumns(String tableName);
}
