package spring.boot.server.springbootserver.entity;




/**
 * 
 * @date 2018-09-19 11:12:39
 */
public class UserRole {
	
	//
	private Integer id;
	//用户id
	private Long userId;
	//角色id
	private Integer roleId;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：用户id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户id
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：角色id
	 */
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	/**
	 * 获取：角色id
	 */
	public Integer getRoleId() {
		return roleId;
	}
}
