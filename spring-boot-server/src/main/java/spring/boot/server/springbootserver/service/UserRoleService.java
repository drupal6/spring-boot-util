package spring.boot.server.springbootserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import spring.boot.server.springbootserver.mapper.main.UserRoleMapper;
import spring.boot.server.springbootserver.entity.UserRole;

@Service("userRoleService")
public class UserRoleService {
	@Autowired
	private UserRoleMapper userRoleMapper;
	
	
	
	
	public UserRole queryObject(Integer id){
		return userRoleMapper.queryObject(id);
	}
	
	public List<UserRole> queryList(){
		return userRoleMapper.queryList();
	}
	
	public void save(UserRole userRole){
		userRoleMapper.save(userRole);
	}
	
	public void update(UserRole userRole){
		userRoleMapper.update(userRole);
	}
	
	public void delete(Integer id){
		userRoleMapper.delete(id);
	}
	
	public List<UserRole> queryByUserId(Long userId){
		return userRoleMapper.queryByUserId(userId);
	}
	
}
