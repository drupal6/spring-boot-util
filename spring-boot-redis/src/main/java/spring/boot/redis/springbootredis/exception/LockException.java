package spring.boot.redis.springbootredis.exception;

/**
 * 没有获得redis锁异常
 * @author Administrator
 *
 */
public class LockException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 490073346077897761L;
	
	public LockException(String message) {
		super(message);
	}

}
