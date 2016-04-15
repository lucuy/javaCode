package javaCode.db.model;

import java.io.Serializable;


/**
 * 将数据库中的外键抽象为Java类，table表示外键所属的表，
 * relationShip关系名称
 */
public class ForeignKey implements Serializable{
	private static final long serialVersionUID = 7682264984294188908L;
	
	private Table table;				//外键对应的表
	private Column fk_column;		//外键所在表中的字段
	private Column pk_column;		//外键对应表中的字段
	
	public ForeignKey(){}
	
	public ForeignKey(Table table, Column fk_column, Column pk_column) {
		super();
		this.table = table;
		this.fk_column = fk_column;
		this.pk_column = pk_column;
	}

	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}

	public Column getFk_column() {
		return fk_column;
	}

	public void setFk_column(Column fk_column) {
		this.fk_column = fk_column;
	}

	public Column getPk_column() {
		return pk_column;
	}

	public void setPk_column(Column pk_column) {
		this.pk_column = pk_column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fk_column == null) ? 0 : fk_column.hashCode());
		result = prime * result + ((pk_column == null) ? 0 : pk_column.hashCode());
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
		ForeignKey other = (ForeignKey) obj;
		if (fk_column == null) {
			if (other.fk_column != null)
				return false;
		} else if (!fk_column.equals(other.fk_column))
			return false;
		if (pk_column == null) {
			if (other.pk_column != null)
				return false;
		} else if (!pk_column.equals(other.pk_column))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ForeignKey [table=" + table + ", fk_column=" + fk_column + ", pk_column=" + pk_column + "]";
	}
	
}
