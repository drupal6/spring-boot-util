package spring.boot.server.springbootserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import spring.boot.server.springbootserver.mapper.main.RoleMapper;
import spring.boot.server.springbootserver.entity.Role;

@Service("roleService")
public class RoleService {
	@Autowired
	private RoleMapper roleMapper;
	
	
	
	
	public Role queryObject(Integer id){
		return roleMapper.queryObject(id);
	}
	
	public List<Role> queryList(){
		return roleMapper.queryList();
	}
	
	public void save(Role role){
		roleMapper.save(role);
	}
	
	public void update(Role role){
		roleMapper.update(role);
	}
	
	public void delete(Integer id){
		roleMapper.delete(id);
	}
	
}
