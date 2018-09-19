package spring.boot.server.springbootserver.mapper.main;

import spring.boot.server.springbootserver.entity.User;
import org.apache.ibatis.annotations.Mapper;
import spring.boot.server.springbootserver.mapper.BaseMapper;

/**
 * 
 * @date 2018-09-19 11:12:39
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

	User queryByUserName(String userName);

}