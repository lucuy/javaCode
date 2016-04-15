package javaCode.db.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javaCode.util.StringUtil;


/**
 * 将数据库中的表抽象为Java类，sqlName数据库表名，remarks数据库表注解，
 * className表名对应的类名，columns数据库表的所有字段，importedKeys
 * 数据库表引用的外键关联，exportedKeys数据库表被作为外键关联的表
 * */
public class Table implements Serializable{
	private static final long serialVersionUID = 5615234867019064252L;
	
	private String sqlName;		//数据库表名
	private String remarks;		//数据库表注解
	private String className;	//表名对应的类名
	private String tableType;   //数据库表类型("TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM")
	private List<Column> columns = new ArrayList<Column>();	//数据库表的所有字段
	
	private List<ForeignKey> importedKeys = new ArrayList<ForeignKey>();//数据库表的外键关联（相当于多对一多的一方）	
	private List<Table> exportedKeys = new ArrayList<Table>();	//数据库表被关联的表（相当于一对多一的一方）
	
	public Table() {}
	
	public Table(String sqlName, String remarks, String className) {
		super();
		this.sqlName = sqlName;
		this.remarks = remarks;
		this.className = className;
	}

	public String getSqlName() {
		return sqlName;
	}
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	public List<ForeignKey> getImportedKeys() {
		return importedKeys;
	}
	public void setImportedKeys(List<ForeignKey> importedKeys) {
		this.importedKeys = importedKeys;
	}
	/**
	 * 添加外键关系
	 */
	public void addForeignKey(ForeignKey foreignKey){
		importedKeys.add(foreignKey);
	}
	
	public List<Table> getExportedKeys() {
		return exportedKeys;
	}
	public void setExportedKeys(List<Table> exportedKeys) {
		this.exportedKeys = exportedKeys;
	}
	public void addExportedKeys(Table table){
		exportedKeys.add(table);
	}
	
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public void addColumns(Column column){
		columns.add(column);
	}
	
	public String getClassNameFirstLower() {
		return StringUtil.changeFirstCharacterLowerCase(className);
	}
	
	public List<Column> getPkColumns(){
		List<Column> pkColumns = new ArrayList<Column>();
		
		for (Column column : columns) {
			if(column.isPk())
				pkColumns.add(column);
		}
		
		return pkColumns;
	}
	
	public List<Column> getGeneralColumns(){
		List<Column> geColumns = new ArrayList<Column>();
		
		for (Column column : columns) {
			if(!column.isFk() && !column.isPk())
				geColumns.add(column);
		}
		
		return geColumns;
	}
	
	public boolean isCompositeId(){
		return getPkCount() > 1;
	}
	
	public int getPkCount() {
		int pkCount = 0;
		
		for(Column c : columns){
			if(c.isPk()) {
				pkCount ++;
			}
		}
		
		return pkCount;
	}
	
	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
		result = prime * result + ((sqlName == null) ? 0 : sqlName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (remarks == null) {
			if (other.remarks != null)
				return false;
		} else if (!remarks.equals(other.remarks))
			return false;
		if (sqlName == null) {
			if (other.sqlName != null)
				return false;
		} else if (!sqlName.equals(other.sqlName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Table [sqlName=" + sqlName + ", remarks=" + remarks + ", className=" + className + "]";
	}
	
}
