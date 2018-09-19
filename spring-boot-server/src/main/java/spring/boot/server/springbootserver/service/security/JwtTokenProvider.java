package spring.boot.server.springbootserver.service.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import spring.boot.server.springbootserver.entity.UserPrincipal;

/**
 * 1. 用户登录之后 生成JWT ，同时验证认证JWT 头部 
 * 2. 读取properties中的 jwtSecret jwtExpirationInMs 
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        JSONArray roles = new JSONArray();
        Collection<? extends GrantedAuthority> auts = userPrincipal.getAuthorities();
        if(auts != null && false == auts.isEmpty()) {
        	for(GrantedAuthority ga : auts) {
        		roles.add(ga.getAuthority());
        	}
        }
        userPrincipal.getAuthorities();
        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("n", userPrincipal.getUsername())
                .claim("r", roles)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    @SuppressWarnings("unchecked")
    public UserPrincipal pareToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        long id = Long.parseLong(claims.getSubject());
        String userName = claims.get("n", String.class);
		List<String> roles = claims.get("r", List.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
    	if(roles != null) {
    		for(String role : roles) {
    			authorities.add(new SimpleGrantedAuthority(role));
    		}
    	}
        UserPrincipal userPrincipal = new UserPrincipal(id, userName, null, authorities);
        return userPrincipal;
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
    
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
    
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
            return Jwts.builder()
            		.setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }


}
