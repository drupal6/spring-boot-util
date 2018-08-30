package spring.boot.redis.springbootredis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {
    String name();
    String key() default "";
    //过期时间 < 0 没有过期时间 单位：秒
    int expire() default 0;
}
