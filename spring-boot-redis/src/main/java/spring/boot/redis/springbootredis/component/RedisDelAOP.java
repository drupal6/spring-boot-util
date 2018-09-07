package spring.boot.redis.springbootredis.component;

import java.lang.reflect.Method;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.boot.redis.springbootredis.annotation.RedisDel;
import spring.boot.redis.springbootredis.constant.RedisConstant;
import spring.boot.redis.springbootredis.service.RedisService;
import spring.boot.redis.springbootredis.util.ParamUtil;

@Component
@Aspect
public class RedisDelAOP {

    @Autowired
    RedisService redisService;

    @Pointcut("@annotation(spring.boot.redis.springbootredis.annotation.RedisDel)")
    public void annotationPoinCut(){
    }

    @Around("annotationPoinCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Signature s = pjp.getSignature();
        MethodSignature ms = (MethodSignature) s;
        Method m = ms.getMethod();
        RedisDel redisDel = m.getAnnotation(RedisDel.class);
        if(redisDel.name() == null || redisDel.name().isEmpty()) {
            throw new Throwable("name is null or is empty.");
        }
        Object[] args = pjp.getArgs();
        String[] argNames = ms.getParameterNames();
        String keyValue = null;
        if(false == redisDel.key().isEmpty()) {
            Map<String, Object> paramM = ParamUtil.megerNameAndArgs(argNames, args);
            keyValue = ParamUtil.getParam(paramM, redisDel.key());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(redisDel.name());
        if(keyValue != null && false == keyValue.isEmpty()) {
            sb.append(RedisConstant.FIRST_SPLIT).append(keyValue);
        }
        String key = sb.toString();
        
        Object result = pjp.proceed();
        redisService.deleteKey(key);
        return result;
    }
}
