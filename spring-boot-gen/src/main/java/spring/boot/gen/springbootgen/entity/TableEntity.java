package spring.boot.gen.springbootgen.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表数据
 */
public class TableEntity {
	//表的名称
	private String tableName;
	//表的备注
	private String comments;
	//表的主键
	private ColumnEntity pk;
	//表的列名(不包含主键)
	private List<ColumnEntity> columns;
	
	private Map<String, IndexEntity> clomunMap;
	
	//类名(第一个字母大写)，如：sys_user => SysUser
	private String className;
	//类名(第一个字母小写)，如：sys_user => sysUser
	private String classname;
	
	private boolean hasList;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public ColumnEntity getPk() {
		return pk;
	}
	public void setPk(ColumnEntity pk) {
		this.pk = pk;
	}
	public List<ColumnEntity> getColumns() {
		return columns;
	}
	public void setColumns(List<ColumnEntity> columns) {
		this.columns = columns;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	
	public Map<String, IndexEntity> getClomunMap() {
		return clomunMap;
	}
	public void setClomunMap(Map<String, IndexEntity> clomunMap) {
		this.clomunMap = clomunMap;
	}
	
	public boolean isHasList() {
		return hasList;
	}
	public void setHasList(boolean hasList) {
		this.hasList = hasList;
	}
	public void addIndex(String indexName, ColumnEntity ce) {
		if(clomunMap == null) {
			clomunMap = new HashMap<>();
		}
		IndexEntity index = clomunMap.get(indexName);
		String where = ce.getColumnName() + " = #{" + ce.getAttrname() + "}";
		if(index == null) {
			index = new IndexEntity(indexName);
			index.setIdStr(ce.getAttrName());
			index.setWhere(where);
			index.setParamStr(ce.getAttrType() + " " + ce.getAttrname());
			index.setParamName(ce.getAttrname());
			clomunMap.put(indexName, index);
		} else {
			index.setIdStr(index.getIdStr() + "And" + ce.getAttrName());
			index.setWhere(index.getWhere() + " and " + where);
			index.setParamStr(index.getParamStr() + ", " + ce.getAttrType() + " " + ce.getAttrname());
			index.setParamName(index.getParamName() + ", " + ce.getAttrname());
		}
		boolean isList = ce.getColumnKey().equals("UNI") ? false : true;
		if(false == isList) {
			index.setReturnList(false);
		}
	}
	
	public boolean hasList() {
		if(clomunMap == null) {
			return false;
		}
		for(IndexEntity index : clomunMap.values()) {
			if(index.isReturnList()) {
				return true;
			}
		}
		return false;
	}
}
