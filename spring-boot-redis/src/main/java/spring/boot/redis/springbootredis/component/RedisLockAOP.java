package spring.boot.redis.springbootredis.component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.boot.redis.springbootredis.annotation.RedisLock;
import spring.boot.redis.springbootredis.constant.RedisConstant;
import spring.boot.redis.springbootredis.exception.LockException;
import spring.boot.redis.springbootredis.service.RedisLockService;
import spring.boot.redis.springbootredis.util.ParamUtil;


@Component
@Aspect
public class RedisLockAOP {
	
	private final Logger LOGGER = LoggerFactory.getLogger(RedisLockAOP.class);

    @Autowired
    RedisLockService redisService;

    @Pointcut("@annotation(spring.boot.redis.springbootredis.annotation.RedisLock)")
    public void annotationPoinCut(){
    }

    @Around("annotationPoinCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
    	 Signature s = pjp.getSignature();
         MethodSignature ms = (MethodSignature) s;
         Method m = ms.getMethod();
         RedisLock redisLock = m.getAnnotation(RedisLock.class);
         if(redisLock.lockName() == null || redisLock.lockName().isEmpty()) {
             throw new Throwable("lockName is null or is empty.");
         }
         Object[] args = pjp.getArgs();
         String[] argNames = ms.getParameterNames();
         String lockKey = null;
         Map<String, Object> paramM = ParamUtil.megerNameAndArgs(argNames, args);
         if(false == redisLock.lockKey().isEmpty()) {
             lockKey = ParamUtil.getParam(paramM, redisLock.lockKey());
         }
         StringBuilder sb = new StringBuilder();
         sb.append(redisLock.lockName());
         if(lockKey != null && false == lockKey.isEmpty()) {
             sb.append(RedisConstant.FIRST_SPLIT).append(lockKey);
         }
         
         String lKey = sb.toString();
         String comsumerKey = ParamUtil.getParam(paramM, redisLock.comsumerKey());;
         int lockExpire = redisLock.expire();
         long starT = System.currentTimeMillis();
         long end = starT + lockExpire;
         Object result = null;
         boolean lock = false;
         while(System.currentTimeMillis() < end) {
        	 if(redisService.lock(lKey, comsumerKey, lockExpire)) {
        		 lock = true;
        		 try{
        			 result = pjp.proceed();
        		 } finally {
        			 if(lock) {
        				 redisService.releaseLock(lKey, comsumerKey);
        				 long endT = System.currentTimeMillis();
        				 if(endT - starT > lockExpire) {
        					 LOGGER.error("execute time is too long. executeTime:{} expireTime:{}",  endT - starT, lockExpire);
        				 }
        			 }
        		 }
        		 return result;
        	 } else {
        		 if(redisLock.nio()) {
        			 try{
        				 Thread.sleep(redisLock.margin());
        			 } catch(Exception e) {
        				 Thread.currentThread().interrupt();
        				 e.printStackTrace();
        			 }
        		 } else {
        			 throw new LockException("no get lock. lockkey" + lKey);
        		 }
        	 }
         }
         throw new TimeoutException("wait lock time out. lockkey:" + lKey);
    }
}
