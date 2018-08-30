package spring.boot.redis.springbootredis.component;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.boot.redis.springbootredis.annotation.RedisCache;
import spring.boot.redis.springbootredis.util.ParamUtil;
import spring.boot.redis.springbootredis.service.RedisService;

import java.lang.reflect.Method;
import java.util.Map;

@Component
@Aspect
public class RedisAOP {

    @Autowired
    RedisService redisService;

    @Pointcut("@annotation(spring.boot.redis.springbootredis.annotation.RedisCache)")
    public void annotationPoinCut(){
    }

    @Around("annotationPoinCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Signature s = pjp.getSignature();
        MethodSignature ms = (MethodSignature) s;
        Method m = ms.getMethod();
        RedisCache redisCache = m.getAnnotation(RedisCache.class);
        if(redisCache.name() == null || redisCache.name().isEmpty()) {
            throw new Throwable("name is null or is empty.");
        }
        Object[] args = pjp.getArgs();
        String[] argNames = ms.getParameterNames();
        String keyValue = null;
        if(false == redisCache.key().isEmpty()) {
            Map<String, Object> paramM = ParamUtil.megerNameAndArgs(argNames, args);
            if(paramM == null) {
                throw new Throwable("key is error.");
            }
            keyValue = ParamUtil.getParam(paramM, redisCache.key());
        }
        Class returnClass = ms.getReturnType();
        StringBuilder sb = new StringBuilder();
        sb.append(redisCache.name());
        if(keyValue != null && false == keyValue.isEmpty()) {
            sb.append("#").append(keyValue);
        }
        String key = sb.toString();
        byte[] returnBytes = null;
        if(returnClass.getName().equalsIgnoreCase(String.class.getName())) {
            returnBytes = redisService.get(key);
        }
        if(returnBytes != null) {
            return byteToObj(returnClass, returnBytes);
        }
        Object returnObj = pjp.proceed();
        if(returnObj != null) {
            returnBytes = objToByte(returnObj);
            redisService.set(key, returnBytes);
            if(redisCache.expire() > 0) {
                redisService.expire(key, redisCache.expire());
            }
        }
        return returnObj;
    }

    private Object byteToObj(Class returnClass, byte[] returnBytes) throws Throwable{
        if(returnClass.getName().equalsIgnoreCase(String.class.getName())) {
            return new String(returnBytes);
        } else {
            throw new Throwable("type not suport. class:" + returnClass.getClass().getName());
        }
    }


    private byte[] objToByte(Object returnObj) throws Throwable {
        if(returnObj instanceof String) {
            return ((String) returnObj).getBytes();
        } else {
            throw new Throwable("type not suport. class:" + returnObj.getClass().getName());
        }
    }
}
