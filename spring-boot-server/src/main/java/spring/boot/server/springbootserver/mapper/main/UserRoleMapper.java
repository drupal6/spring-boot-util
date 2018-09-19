package spring.boot.server.springbootserver.mapper.main;

import spring.boot.server.springbootserver.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import spring.boot.server.springbootserver.mapper.BaseMapper;
import java.util.List;

/**
 * 
 * @date 2018-09-19 11:12:39
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

	List<UserRole> queryByUserId(Long userId);

}