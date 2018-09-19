package spring.boot.server.springbootserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import spring.boot.server.springbootserver.mapper.main.UserMapper;
import spring.boot.server.springbootserver.entity.User;

@Service("userService")
public class UserService {
	@Autowired
	private UserMapper userMapper;
	
	
	
	
	public User queryObject(Long id){
		return userMapper.queryObject(id);
	}
	
	public List<User> queryList(){
		return userMapper.queryList();
	}
	
	public void save(User user){
		userMapper.save(user);
	}
	
	public void update(User user){
		userMapper.update(user);
	}
	
	public void delete(Long id){
		userMapper.delete(id);
	}
	
	public User queryByUserName(String userName){
		return userMapper.queryByUserName(userName);
	}
	
}
