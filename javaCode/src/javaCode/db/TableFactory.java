package javaCode.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javaCode.GeneratorProperties;
import javaCode.db.model.Column;
import javaCode.db.model.ForeignKey;
import javaCode.db.model.Table;
import javaCode.util.StringUtil;
import javaCode.util.TableUtil;

/**
 * 单例模式，getAllTables()方法返回封装好的所有表
 * */
public class TableFactory {
	private static TableFactory instance = null;
	
	/**
	 * @return 单例模式得到当前类的对象
	 */
	public synchronized static TableFactory getInstance() {
		if(instance == null) 
			instance = new TableFactory();
		
		return instance;
	}
	
	/**
	 * @return 数据库链接
	 */
	private static Connection getConnection() {
		return DataSourceProvider.getConnection();
	}
	
	private static String getSchema(){
		return GeneratorProperties.getProperty("jdbc.schema");
	}
	
	/**
	 * 将数据库表转换为Java类Table，数据库字段转换为Java类Column，外键转换为
	 * Java类ForeignKey
	 * @return 所有转化过的表
	 */
	public List<Table> getAllTables(){
		Connection conn = getConnection();		//获得数据库链接
		
		try {
			DatabaseMetaData dbMetaData = conn.getMetaData();	//数据库元数据
			ResultSet rs = dbMetaData.getTables(null, getSchema(), null, null);//得到某个数据库下所有的表
			
			List<Table> tables = new ArrayList<Table>();	//存放所有转换过的表对象
			
			while(rs.next()) {
				Table table = createTable(rs);		//转化数据库表为Table类对象
				
				tables.add(table);				
				
				TableUtil.putSqlTable(table.getSqlName(), table);	//将Table对象与数据库表名关联，方便查询
				TableUtil.putClassTable(table.getClassName(), table);//将Table对象与类名关联，方便查询
			}
			
			for(Table table:tables){			//遍历所有转化过的Table对象，封装所有表的字段和外键关系
				createColumn(dbMetaData,table);	//封装表的字段
			}
			
			for(Table table:tables){			//遍历所有转化过的Table对象，封装所有表的字段和外键关系
				createImportedKeys(dbMetaData,table);	//封装外键关系
			}
			
			return tables;		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * 生成外键关联关系
	 * @param dbMetaData
	 * @param table
	 * @throws SQLException
	 */
	private void createImportedKeys(DatabaseMetaData dbMetaData,Table table) throws SQLException{
		ResultSet importedKeys = 
				dbMetaData.getImportedKeys(null,getSchema(), table.getSqlName());//所有导入的外键关系（相当于多对一多方）
		
		while(importedKeys.next()){		
			 String pk_table = importedKeys.getString("PKTABLE_NAME");		//外键对应的主表名称
			 String pkcol   = importedKeys.getString("PKCOLUMN_NAME");	//外键对应的主表中的字段
			 String fkcol   = importedKeys.getString("FKCOLUMN_NAME");	//表中外键字段名称
			 
			Table pkTable = TableUtil.getSqlTable(pk_table); 	//外键对应的主表的Table对象
			
			ForeignKey fk = new ForeignKey();		//外键所对应的Java类对象
			fk.setTable(pkTable);					//外键对应的主表的Table对象
			
			for(Column c:table.getColumns()){		//遍历所有字段
				if(c.getSqlName().equals(fkcol))		//找到当前字段所对应的Java类对象
					fk.setFk_column(c);	
			}
			
			for(Column c:pkTable.getColumns()){	//遍历外键主表所有字段
				if(c.getSqlName().equals(pkcol))	//找到主表字段所对应的Java类对象
					fk.setPk_column(c);
			}
			
//			System.err.println("表："+fk.getFk_column().getTable().getSqlName()+"的"+fk.getFk_column().getSqlName()+"字段"
//					+"是表"+fk.getTable().getClassName()+"中"+fk.getPk_column().getSqlName()+"字段的外键");
			
			table.addForeignKey(fk);				//表对象中添加外键关系
			pkTable.addExportedKeys(table);		//外键主表对象中添加输出关系
		}
		
		importedKeys.close();
	}
	
	/**
	 * 生成表的字段，将字段转换为Java类Column的对象，存入Table类中
	 * @param dbMetaData
	 * @param table
	 * @throws SQLException
	 */
	private void createColumn(DatabaseMetaData dbMetaData,Table table) throws SQLException{
		ResultSet  columnRs = dbMetaData.getColumns(null, getSchema(), table.getSqlName() , null);//表中所有字段
		ResultSet primaryKeyRs = dbMetaData.getPrimaryKeys(null,getSchema(), table.getSqlName());//表中所有主键
		ResultSet foreignKeysRs = dbMetaData.getImportedKeys(null,getSchema(), table.getSqlName());	//表中所有外键
		ResultSet indexRs = dbMetaData.getIndexInfo(null,getSchema(), table.getSqlName(),false,true);//表中所有索引列
		
		List<String> primaryKeys = new LinkedList<String>();	//存放主键
		List<String> foreignKeys = new LinkedList<String>();	//存放外键
		List<String> indexes = new LinkedList<String>();		//存放索引列
		List<String> uniqueIndexes =  new LinkedList<String>(); //唯一索引
		
		while(primaryKeyRs.next()){	//主键
			 String columnName = primaryKeyRs.getString("COLUMN_NAME");
			 primaryKeys.add(columnName);
		}
		
		while (foreignKeysRs.next()) {	//外键
			 String fkcol   = foreignKeysRs.getString("FKCOLUMN_NAME");
			 foreignKeys.add(fkcol);
		}
		
		while (indexRs.next()) {		//索引
			String columnName = indexRs.getString("COLUMN_NAME");
	            
			if (columnName != null) {
				indexes.add(columnName);
		         }
			
			boolean nonUnique = indexRs.getBoolean("NON_UNIQUE");	//唯一键索引
			
			if (!nonUnique) {	
				uniqueIndexes.add(columnName);
			}
		}
		
		while(columnRs.next()){	//遍历所有字段，并转换为Java类Column
			String sqlTypeName = columnRs.getString("TYPE_NAME");
		        String sqlColumnName = columnRs.getString("COLUMN_NAME");
		        String defaultValue = columnRs.getString("COLUMN_DEF");
		        String remarks = columnRs.getString("REMARKS");
		        int sqlType = columnRs.getInt("DATA_TYPE");
		        int size =  columnRs.getInt("COLUMN_SIZE");
		        int dcm = columnRs.getInt("DECIMAL_DIGITS");
		        
		        Column column = new Column();
		        column.setSqlName(sqlColumnName);		//数据库表字段名
		        column.setColumnName(getColumnName(sqlColumnName));
		        column.setSqlTypeName(sqlTypeName);	//字段类型名
		        column.setDefaultValue(defaultValue);	//默认值
		        column.setRemarks(remarks);			//注释
		        column.setSqlType(sqlType);	
		        column.setSize(size);					//长度
		        column.setNullable(DatabaseMetaData.columnNullable == columnRs.getInt("NULLABLE"));	//是否能为空
		        column.setPk(primaryKeys.contains(sqlColumnName));	//是否为主键
		        column.setFk(foreignKeys.contains(sqlColumnName));//是否为外键
		        
		        column.setJavaType(StringUtil.getPreferredJavaType(sqlType,size,dcm));//Java类型
		        
		        column.setIndexed(indexes.contains(sqlColumnName));	//是否索引
		        column.setUnique(uniqueIndexes.contains(sqlColumnName));//是否唯一
		        column.setTable(table);	//字段所属表
			         
		        table.getColumns().add(column);//表增加字段
		}
		
		columnRs.close();
		primaryKeyRs.close();
		indexRs.close();
		foreignKeysRs.close();
	}
	
	/**
	 * 将表转换为Table对象
	 */
	private Table createTable(ResultSet rs){
		try {
			Table table = new Table();
			
			String sqlName = rs.getString("TABLE_NAME");	//表名
			String remarks = getTableComments(getSchema(),sqlName);	//表注释
			String tableType = rs.getString("TABLE_TYPE");  //表类型
			
			table.setSqlName(sqlName);
			table.setRemarks(remarks);
			table.setTableType(tableType);
			table.setClassName(getClassName(sqlName));		//表对应类名称
			
			return table;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * 根据数据库表名生成Java类名
	 * @param sqlName
	 * @return java类名
	 */
	private  static String getClassName(String sqlName){
		return StringUtil.makeAllWordFirstLetterUpperCase(StringUtil.toUnderscoreName(StringUtil.removeTableSqlNamePrefix(sqlName)));
	}
	
	/**
	 * 根据数据库列名生成Java类列名
	 * @param sqlName
	 * @return java类名
	 */
	private  static String getColumnName(String sqlName){
		return StringUtil.makeAllWordFirstLetterUpperCase(StringUtil.toUnderscoreName(sqlName));
	}
	
	/**
	 * 返回数据库表注释
	 */
	private String getTableComments(String schema,String table)  {
		String sql = "select table_comment from information_schema.tables where table_schema='"+schema+"' and table_name='"+table+"'";
		System.err.println(sql);
		
		DbQuery dbQuery = new DbQuery();
		
		return dbQuery.queryForString(sql);
	}
	
	/**
	 * 内部类，用来执行sql查询数据库表的注释
	 */
	private class DbQuery {
		/**
		 * 查询数据库表注释
		 * @param sql
		 * @return
		 */
		public String queryForString(String sql) {
			Statement s = null;
			ResultSet rs = null;
			
			try {
				s =  getConnection().createStatement();
				rs = s.executeQuery(sql);
				
				if(rs.next()) {
					return rs.getString(1);
				}
				
				return null;
			}catch(SQLException e) {
				e.printStackTrace();
				return null;
			}finally {
				close(rs,null,s);
			}
		}	
		
		/**
		 * 关闭资源
		 * @param rs
		 * @param ps
		 * @param statements
		 */
		public void close(ResultSet rs,PreparedStatement ps,Statement... statements) {
			try {
				if(ps != null) ps.close();
				if(rs != null) rs.close();
				
				for(Statement s : statements) {s.close();}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
