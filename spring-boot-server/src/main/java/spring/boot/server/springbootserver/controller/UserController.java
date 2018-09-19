package spring.boot.server.springbootserver.controller;


import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.boot.server.springbootserver.entity.User;
import spring.boot.server.springbootserver.plyload.req.UserRequest;
import spring.boot.server.springbootserver.plyload.res.UserResponst;
import spring.boot.server.springbootserver.service.UserService;


/**
 * 
 * 
 * @date 2018-08-28 18:07:55
 */
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/queryObject")
	public UserResponst queryObject(@Valid @RequestBody UserRequest req) {
		User user = userService.queryObject(req.getId());
		if(user == null) {
			return new UserResponst(1, "user not exit.");
		} else {
			return new UserResponst(user);
		}
	}
	
	@PostMapping("/queryList")
	public UserResponst queryList() {
		List<User> users = userService.queryList();
		if(users == null) {
			return new UserResponst(1, "user list is null");
		} else {
			return new UserResponst(users);
		}
	}
	
	@PostMapping("/save")
	public UserResponst save(@Valid @RequestBody UserRequest req) {
		User user = userService.queryByUserName(req.getUsername());
		if(user != null) {
			return new UserResponst(2, "user is exit.");
		}
		user = new User();
		user.setUserName(req.getUsername());
		user.setPassword(req.getPassword());
		userService.save(user);
		return new UserResponst(user);
	}
	
	@PostMapping("/update")
	public UserResponst update(@Valid @RequestBody UserRequest req) {
		User user = userService.queryObject(req.getId());
		if(user == null) {
			return new UserResponst(1, "user not exit.");
		}
		user.setUserName(req.getUsername());
		user.setPassword(req.getPassword());
		userService.update(user);
		return new UserResponst(user);
	}
	
	@PostMapping("/delete")
	public UserResponst delete(@Valid @RequestBody UserRequest req) {
		userService.delete(req.getId());
		return new UserResponst(2, "delete user.");
	}
	
	@PostMapping("/queryByUserName")
	public UserResponst queryByUserName(@Valid @RequestBody UserRequest req) {
		User user = userService.queryByUserName(req.getUsername());
		if(user != null) {
			return new UserResponst(user);
		}
		return new UserResponst(2, "user is exit.");
	}
}
