package spring.boot.redis.springbootredis.component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.boot.redis.springbootredis.annotation.RedisSet;
import spring.boot.redis.springbootredis.constant.RedisConstant;
import spring.boot.redis.springbootredis.service.RedisService;
import spring.boot.redis.springbootredis.util.JSONUtil;
import spring.boot.redis.springbootredis.util.ParamUtil;


@Component
@Aspect
public class RedisSetAOP {

    @Autowired
    RedisService redisService;

    @Pointcut("@annotation(spring.boot.redis.springbootredis.annotation.RedisSet)")
    public void annotationPoinCut(){
    }

    @Around("annotationPoinCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Signature s = pjp.getSignature();
        MethodSignature ms = (MethodSignature) s;
        Method m = ms.getMethod();
        RedisSet redisSet;
        redisSet = m.getAnnotation(RedisSet.class);
        if(redisSet.name() == null || redisSet.name().isEmpty()) {
            throw new Throwable("name is null or is empty.");
        }
        Object[] args = pjp.getArgs();
        String[] argNames = ms.getParameterNames();
        String keyValue = null;
        if(false == redisSet.key().isEmpty()) {
            Map<String, Object> paramM = ParamUtil.megerNameAndArgs(argNames, args);
            keyValue = ParamUtil.getParam(paramM, redisSet.key());
        }
        Class<?> returnClass = ms.getReturnType();
        Type type = m.getGenericReturnType();
        StringBuilder sb = new StringBuilder();
        sb.append(redisSet.name());
        if(keyValue != null && false == keyValue.isEmpty()) {
            sb.append(RedisConstant.FIRST_SPLIT).append(keyValue);
        }
        String key = sb.toString();
        byte[] returnBytes =  redisService.get(key);
        if(returnBytes != null) {
        	System.out.println("从redis获取数据返回.");
            return byteToObj(returnClass, type, returnBytes);
        }
        Object returnObj = pjp.proceed();
        if(returnObj != null) {
            returnBytes = objToByte(returnObj);
            if(redisSet.expire() > 0) {
                redisService.setEx(key, returnBytes, redisSet.expire());
            } else {
            	redisService.set(key, returnBytes);
            }
        }
        System.out.println("获取数据返回.");
        return returnObj;
    }

    private Object byteToObj(Class<?> returnClass, Type type, byte[] returnBytes){
        if(returnClass.getName().equalsIgnoreCase(String.class.getName())) {
            return new String(returnBytes);
        } else {
        	return JSONUtil.byteToObject(returnBytes, type);
        }
    }


    private byte[] objToByte(Object returnObj) throws Throwable {
        if(returnObj instanceof String) {
            return ((String) returnObj).getBytes();
        } else {
           return JSONUtil.getBytes(returnObj);
        }
    }
}
