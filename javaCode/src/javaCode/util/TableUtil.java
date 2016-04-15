package javaCode.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javaCode.db.TableFactory;
import javaCode.db.model.Table;

public class TableUtil {
	private static final Map<String,Table> SQL_TABLE_MAP = new HashMap<String,Table>();
	private static final Map<String,Table> CLASS_TABLE_MAP = new HashMap<String,Table>();
	private static final List<Table> TABLES = TableFactory.getInstance().getAllTables();
	
	public static void putSqlTable(String sqlName,Table table){
		SQL_TABLE_MAP.put(sqlName, table);
	}
	
	public static Table getSqlTable(String sqlName){
		return SQL_TABLE_MAP.get(sqlName);
	}
	
	public static void putClassTable(String className,Table table){
		CLASS_TABLE_MAP.put(className, table);
	}
	
	public static Table getClassTable(String className){
		return CLASS_TABLE_MAP.get(className);
	}
	
	public static List<Table> getAllTable(){
		return TABLES;
	}
}
