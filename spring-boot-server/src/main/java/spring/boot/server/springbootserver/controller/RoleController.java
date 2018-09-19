package spring.boot.server.springbootserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.boot.server.springbootserver.annotation.CurrentUser;
import spring.boot.server.springbootserver.entity.Role;
import spring.boot.server.springbootserver.service.RoleService;


/**
 * 
 * 
 * @date 2018-09-03 17:25:11
 */
@RestController
@RequestMapping("role")
public class RoleController {
	@Autowired
	private RoleService roleService;
	
	@GetMapping("/getRole")
	public Role getRole(@CurrentUser UserDetails userD, int id) {
		return roleService.queryObject(id);
	}
}
