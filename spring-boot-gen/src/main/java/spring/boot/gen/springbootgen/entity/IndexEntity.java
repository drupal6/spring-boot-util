package spring.boot.gen.springbootgen.entity;

public class IndexEntity {

	private final String name;
	
	private String idStr;
	
	private String where;
	
	private String paramStr;
	
	private String paramName;
	
	private boolean returnList = true;

	public IndexEntity(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public String getIdStr() {
		return idStr;
	}

	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public boolean isReturnList() {
		return returnList;
	}

	public void setReturnList(boolean returnList) {
		this.returnList = returnList;
	}

	public String getParamStr() {
		return paramStr;
	}

	public void setParamStr(String paramStr) {
		this.paramStr = paramStr;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
}
