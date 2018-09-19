package spring.boot.server.springbootserver.service.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.server.springbootserver.entity.Role;
import spring.boot.server.springbootserver.entity.User;
import spring.boot.server.springbootserver.entity.UserPrincipal;
import spring.boot.server.springbootserver.entity.UserRole;
import spring.boot.server.springbootserver.service.RoleService;
import spring.boot.server.springbootserver.service.UserRoleService;
import spring.boot.server.springbootserver.service.UserService;


/**
 * 授权一个用户能做什么事情 ， 包括拥有什么角色 ， 必须通过UserDetailsService做处理。
 * 通过用户名 、id 去加载用户信息。
 * 
 */

@Service
public class SecurityUserService implements UserDetailsService {

    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleService roleService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userService.queryByUserName(username);
        if(user == null) {
        	throw new UsernameNotFoundException("User not found with username : " + username);
        }
        return UserPrincipal.create(user, getUserRole(user));
    }
    
    public List<Role> getUserRole(User user) {
    	List<Role> roles = new ArrayList<>();
        List<UserRole> userRoles = userRoleService.queryByUserId(user.getId());
        if(userRoles != null) {
        	for(UserRole userRole : userRoles) {
        		Role role = roleService.queryObject(userRole.getRoleId());
        		if(role != null) {
        			roles.add(role);
        		}
        	}
        }
        return roles;
    }
}