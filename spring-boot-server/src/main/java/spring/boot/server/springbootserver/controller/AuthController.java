package spring.boot.server.springbootserver.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.boot.server.springbootserver.entity.User;
import spring.boot.server.springbootserver.entity.UserRole;
import spring.boot.server.springbootserver.mapper.main.UserMapper;
import spring.boot.server.springbootserver.mapper.main.UserRoleMapper;
import spring.boot.server.springbootserver.plyload.req.AuthRequest;
import spring.boot.server.springbootserver.plyload.res.AuthResponst;
import spring.boot.server.springbootserver.service.security.JwtTokenProvider;


@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	UserMapper userDao;
	@Autowired
	UserRoleMapper userRoleDao;
	
	@Autowired
	PasswordEncoder passwordEncoder;
 
	@Autowired
	AuthenticationManager authenticationManager;
	 
	@Autowired
	JwtTokenProvider tokenProvider;
	
	@PostMapping("/signup")
	public AuthResponst signup(@Valid @RequestBody AuthRequest req) {
		AuthResponst res = new AuthResponst();
		if(userDao.queryByUserName(req.getUsername()) != null) {
			res.setRet(1);
			res.setMsg("username is exist!");
			return res;
		}
		User user = new User();
		user.setUserName(req.getUsername());
		user.setPassword(passwordEncoder.encode(req.getPassword().trim()));
		userDao.save(user);
		UserRole userRole = new UserRole();
		userRole.setUserId(user.getId());
		userRole.setRoleId(2);
		userRoleDao.save(userRole);
		res.setRet(0);
		res.setMsg("create user is success!");
		return res;
	}
	
	@PostMapping("/signin")
	public AuthResponst signin(@Valid @RequestBody AuthRequest req) {
		Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
            		req.getUsername(),
            		req.getPassword()
            )
        );
		SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        AuthResponst res = new AuthResponst();
        res.setRet(0);
		res.setMsg("login user is success!");
		res.setToken(jwt); 
        return res;
	}
}
