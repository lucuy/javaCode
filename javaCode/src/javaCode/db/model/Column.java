package javaCode.db.model;

import java.io.Serializable;
import javaCode.util.StringUtil;

/**
 * 将数据库中表的字段抽象为Java类
 * */
public class Column implements Serializable{
	private static final long serialVersionUID = -5354971787479595680L;
	
	private Table table;		//字段所属的表
	
	private int sqlType;		//数据库原生列类型
	private int size;		//字段长度
	private int decimalDigits;		//字段是否是DECIMAL类型
	
	private String sqlTypeName; //数据库原生列类型(如 varchar、int)
	private String sqlName;  	//数据库原生列名
	private String columnName; //java类中列名
	private String defaultValue;	//默认值
	private String remarks;		//注解
	private String javaType;		//对应的java类型
	
	private boolean isPk;   	//是否是主键
	private boolean isFk;	//是否是外键
	private boolean isNullable;	//字段是否能为空
	private boolean isIndexed;	//字段是否索引列
	private boolean isUnique;     //是否唯一
	
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
	public int getSqlType() {
		return sqlType;
	}
	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}
	public String getSqlTypeName() {
		return sqlTypeName;
	}
	public void setSqlTypeName(String sqlTypeName) {
		this.sqlTypeName = sqlTypeName;
	}
	public String getSqlName() {
		return sqlName;
	}
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}
	public boolean isPk() {
		return isPk;
	}
	public void setPk(boolean isPk) {
		this.isPk = isPk;
	}
	public boolean isFk() {
		return isFk;
	}
	public void setFk(boolean isFk) {
		this.isFk = isFk;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getDecimalDigits() {
		return decimalDigits;
	}
	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}
	public boolean isNullable() {
		return isNullable;
	}
	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
	public boolean isIndexed() {
		return isIndexed;
	}
	public void setIndexed(boolean isIndexed) {
		this.isIndexed = isIndexed;
	}
	public boolean isUnique() {
		return isUnique;
	}
	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getJavaType() {
		return javaType;
	}
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public String getSimpleJavaType(){
		String[] strs = javaType.split("\\.");
		
		if(strs.length > 1){
			return strs[strs.length - 1];
		}else
			return javaType;
	}
	
	/** 列是否是String类型 */
	public boolean getIsStringColumn() {
		return StringUtil.isString(getJavaType());
	}
	
	/** 列是否是日期类型 */
	public boolean getIsDateTimeColumn() {
		return StringUtil.isDate(getJavaType());
	}
	
	/** 列是否是Number类型 */
	public boolean getIsNumberColumn() {
		return StringUtil.isFloatNumber(getJavaType()) 
			|| StringUtil.isIntegerNumber(getJavaType());
	}
	
	/**
     * 列的常量名称
     */
	public String getConstantName() {
		return StringUtil.toUnderscoreName(getColumnName()).toUpperCase();
	}
	
	public String getColumnNameLower(){
		return StringUtil.changeFirstCharacterLowerCase(columnName);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqlName == null) ? 0 : sqlName.hashCode());
		result = prime * result + sqlType;
		result = prime * result + ((table == null) ? 0 : table.hashCode());
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
		Column other = (Column) obj;
		if (sqlName == null) {
			if (other.sqlName != null)
				return false;
		} else if (!sqlName.equals(other.sqlName))
			return false;
		if (sqlType != other.sqlType)
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Column [sqlType=" + sqlType + ", size=" + size + ", decimalDigits=" + decimalDigits + ", sqlTypeName=" + sqlTypeName + ", sqlName=" + sqlName + ", columnName=" + columnName
				+ ", defaultValue=" + defaultValue + ", remarks=" + remarks + ", javaType=" + javaType + ", isPk=" + isPk + ", isFk=" + isFk + ", isNullable=" + isNullable
				+ ", isIndexed=" + isIndexed + ", isUnique=" + isUnique + "]";
	}
	
}
