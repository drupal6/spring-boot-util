package spring.boot.server.springbootserver.plyload.res;

public class UserResponst {

	private int ret;
	private String msg;
	private Object obj;
	
	public UserResponst(int ret, String msg){
		this.ret = ret;
		this.msg = msg;
	}
	
	public UserResponst(Object obj) {
		this.obj = obj;
	}
	
	public int getRet() {
		return ret;
	}
	
	public void setRet(int ret) {
		this.ret = ret;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
