package spring.boot.redis.springbootredis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 同步阻塞锁
 * @author Administrator
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BIOLock {

	//锁名字
	String lockName();
	//锁名字后缀
	String lockKey() default "";
	//使用端的关键字
	String comsumerKey();
	//超时时间 默认1000毫秒 (单位:毫秒)
	int expire() default 1000;
	//循环周期 默认10毫秒 (单位:毫秒)
	int margin() default 10;
	
}
