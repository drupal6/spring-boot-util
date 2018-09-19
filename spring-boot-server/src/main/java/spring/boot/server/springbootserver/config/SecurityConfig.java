package spring.boot.server.springbootserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring.boot.server.springbootserver.service.security.JwtAuthenticationEntryPoint;
import spring.boot.server.springbootserver.service.security.JwtAuthenticationFilter;
import spring.boot.server.springbootserver.service.security.SecurityUserService;


/**
 * 设置安全机制
 */

@Configuration
@EnableWebSecurity //使当前工程支持web安全机制
@EnableGlobalMethodSecurity(
        securedEnabled = true, //针对controller/service 方法进行安全角色控制  @Secured("ROLE_ADMIN") 确定 Spring Security 安全注释 [@Secured] 是否应该启用
        jsr250Enabled = true,  //确定 JSR-250注释 [@RolesAllowed..] 是否应该启用
        prePostEnabled = true  //能使用基于复杂的表达式访问控制 确定 Spring Security 前置注释 [@PreAuthorize,@PostAuthorize,..] 是否应该启用
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SecurityUserService securityUserService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * AuthenticationManagerBuilder 能告知进行是内存授权 、LDAP鉴权、JDBC授权、还是自定义授权
     * 此处通过customUserDetailsService自定义 和 passwordEncoder 方式授权
     */
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(securityUserService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 授权用户登录
     */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 在不同情况 用于增加角色和保护资源
     * 1.静态资源访问控制
     * 2.公用API 所有人使用
     * 3.授权用户访问API 
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	  http
          .csrf()
              .disable()
          .exceptionHandling()
              .authenticationEntryPoint(unauthorizedHandler)
              .and()
          .sessionManagement()
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              .and()
              .authorizeRequests()
              .antMatchers("/server/auth/**").permitAll()
              .antMatchers("/server/user/**").permitAll()
              .antMatchers("/monitor/**").permitAll()
              .anyRequest().authenticated();
    	  

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}