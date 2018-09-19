package spring.boot.server.springbootserver.entity;




/**
 * 
 * @date 2018-09-19 11:12:39
 */
public class User {
	
	//uid
	private Long id;
	//账号
	private String userName;
	//密码
	private String password;

	/**
	 * 设置：uid
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：uid
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：账号
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取：账号
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置：密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：密码
	 */
	public String getPassword() {
		return password;
	}
}
