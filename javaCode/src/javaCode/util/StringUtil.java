package javaCode.util;

import java.sql.Types;
import java.util.HashMap;
import javaCode.GeneratorProperties;

public class StringUtil {
	
	/**
	 * 删除数据库表名的前缀
	 * @param sqlName 数据库表名
	 * */
	public static String removeTableSqlNamePrefix(String sqlName) {
		String prefixs =GeneratorProperties.getProperty("tableRemovePrefixes");
		
		for(String prefix : prefixs.split(",")) {
			String removedPrefixSqlName = removePrefix(sqlName, prefix,true);
			
			if(!removedPrefixSqlName.equals(sqlName)) {
				return removedPrefixSqlName;
			}
		}
		
		return sqlName;
	}
	
	/**
	 * 删除字符串前缀
	 * @param str 需要删除前缀的字符串
	 * @param prefix 前缀
	 * @param ignoreCase 是否区分大小写（true表示不区分大小写）
	 * */
	public static String removePrefix(String str,String prefix,boolean ignoreCase) {
		if(str == null) 
			return null;
		
		if(prefix == null) 
			return str;
		
		if(ignoreCase) {
			if(str.toLowerCase().startsWith(prefix.toLowerCase())) {
				return str.substring(prefix.length());
			}
		}else {
			if(str.startsWith(prefix)) {
				return str.substring(prefix.length());
			}
		}
		
		return str;
	}
	
	/**
	 * 转换带下划线的首字母小写字符串为大写
	 * */
	public static String makeAllWordFirstLetterUpperCase(String sqlName) {
		String[] strs = sqlName.toLowerCase().split("_");
		String result = "";
		String preStr = "";
		
		for(int i = 0; i < strs.length; i++) {
			if(preStr.length() == 1) {
				result += strs[i];
			}else {
				result += capitalize(strs[i]);
			}
			preStr = strs[i];
		}
		
		return result;
	}
	
	/**
	 * 字符串首字母转为大写
	 * */
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}
	
	/**
	 * 字符串首字母转为小写
	 * */
	public static String changeFirstCharacterLowerCase(String str) {
		return changeFirstCharacterCase(str, false);
	}
	
	/**
	 * 字符串首字母大小写转换
	 * */
	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (str == null || str.length() == 0) {
			return str;
		}
		
		StringBuffer buf = new StringBuffer(str.length());
		
		if (capitalize) {
			buf.append(Character.toUpperCase(str.charAt(0)));
		}else {
			buf.append(Character.toLowerCase(str.charAt(0)));
		}
		
		buf.append(str.substring(1));
		
		return buf.toString();
	}
	
	/**
	 * 数据库表名设计采用26个英文字母(区分大小写)和0-9这十个自然数,加上下划线'_'组成,共63个字符
	 * 该方法是用来将驼峰命名转换为下划线命名，并转为小写
	 * */
	public static String toUnderscoreName(String name) {
		if(name == null) 
			return null;
		
		String filteredName = name;
		
		if(filteredName.equals(filteredName.toUpperCase())) {
			filteredName = filteredName.toLowerCase();
		}
		
		StringBuffer result = new StringBuffer();
		
		if (filteredName != null && filteredName.length() > 0) {
			result.append(filteredName.substring(0, 1).toLowerCase());
			
			for (int i = 1; i < filteredName.length(); i++) {
				String preChart = filteredName.substring(i - 1, i);
				String cur = filteredName.substring(i, i + 1);
				
				if(cur.equals("_")) {
					result.append("_");
					continue;
				}
				
				if(preChart.equals("_")){
					result.append(cur.toLowerCase());
					continue;
				}
				
				if(cur.matches("\\d")) {
					result.append(cur);
				}else if (cur.equals(cur.toUpperCase())) {
					result.append("_");
					result.append(cur.toLowerCase());
				}else {
					result.append(cur);
				}
			}
		}
		
		return result.toString();
	}
	
	private final static HashMap<Integer,String> _preferredJavaTypeForSqlType = new HashMap<Integer,String>();
	
	 static {
	      _preferredJavaTypeForSqlType.put(Types.TINYINT, "java.lang.Byte");
	      _preferredJavaTypeForSqlType.put(Types.SMALLINT, "java.lang.Short");
	      _preferredJavaTypeForSqlType.put(Types.INTEGER, "java.lang.Integer");
	      _preferredJavaTypeForSqlType.put(Types.BIGINT, "java.lang.Long");
	      _preferredJavaTypeForSqlType.put(Types.REAL, "java.lang.Float");
	      _preferredJavaTypeForSqlType.put(Types.FLOAT, "java.lang.Double");
	      _preferredJavaTypeForSqlType.put(Types.DOUBLE, "java.lang.Double");
	      _preferredJavaTypeForSqlType.put(Types.DECIMAL, "java.math.BigDecimal");
	      _preferredJavaTypeForSqlType.put(Types.NUMERIC, "java.math.BigDecimal");
	      _preferredJavaTypeForSqlType.put(Types.BIT, "java.lang.Boolean");
	      _preferredJavaTypeForSqlType.put(Types.BOOLEAN, "java.lang.Boolean");
	      _preferredJavaTypeForSqlType.put(Types.CHAR, "java.lang.String");
	      _preferredJavaTypeForSqlType.put(Types.VARCHAR, "java.lang.String");
	      // according to resultset.gif, we should use java.io.Reader, but String is more convenient for EJB
	      _preferredJavaTypeForSqlType.put(Types.LONGVARCHAR, "java.lang.String");
	      _preferredJavaTypeForSqlType.put(Types.BINARY, "byte[]");
	      _preferredJavaTypeForSqlType.put(Types.VARBINARY, "byte[]");
	      _preferredJavaTypeForSqlType.put(Types.LONGVARBINARY, "byte[]");
	      _preferredJavaTypeForSqlType.put(Types.DATE, "java.sql.Date");
	      _preferredJavaTypeForSqlType.put(Types.TIME, "java.sql.Time");
	      _preferredJavaTypeForSqlType.put(Types.TIMESTAMP, "java.sql.Timestamp");
	      _preferredJavaTypeForSqlType.put(Types.CLOB, "java.sql.Clob");
	      _preferredJavaTypeForSqlType.put(Types.BLOB, "java.sql.Blob");
	      _preferredJavaTypeForSqlType.put(Types.ARRAY, "java.sql.Array");
	      _preferredJavaTypeForSqlType.put(Types.REF, "java.sql.Ref");
	      _preferredJavaTypeForSqlType.put(Types.STRUCT, "java.lang.Object");
	      _preferredJavaTypeForSqlType.put(Types.JAVA_OBJECT, "java.lang.Object");
	}
	 
	/**
	 * 将数据库字段类型转为Java的数据类型
	 * */
	public static String getPreferredJavaType(int sqlType, int size,int decimalDigits) {
		
		if ((sqlType == Types.DECIMAL || sqlType == Types.NUMERIC) && decimalDigits == 0) {
			if (size < 10) {
				return "java.lang.Integer";
			} else if (size < 19) {
				return "java.lang.Long";
			} else {
				return "java.math.BigDecimal";
			}
		}
		
		String result = _preferredJavaTypeForSqlType.get(sqlType);
		
		String p_result = GeneratorProperties.getProperty("java_typemapping."+result);
		
		if(p_result != null)
			result = p_result;
		
		if (result == null) {
			result = "java.lang.Object";
		}
		
		return result;
	}
	
	public static boolean isFloatNumber(String javaType) {
		if(javaType.endsWith("Float") || javaType.endsWith("Double") || javaType.endsWith("BigDecimal") || javaType.endsWith("BigInteger")) {
			return true;
		}
		if(javaType.endsWith("float") || javaType.endsWith("double") || javaType.endsWith("BigDecimal") || javaType.endsWith("BigInteger")) {
            return true;
        }
		return false;
	}
	
	public static boolean isIntegerNumber(String javaType) {
		if(javaType.endsWith("Long") || javaType.endsWith("Integer") || javaType.endsWith("Short") || javaType.endsWith("Byte")) {
			return true;
		}
		if(javaType.endsWith("long") || javaType.endsWith("int") || javaType.endsWith("short") || javaType.endsWith("byte")) {
            return true;
        }
		return false;
	}

	public static boolean isDate(String javaType) {
		if(javaType.endsWith("Date") || javaType.endsWith("Timestamp") || javaType.endsWith("Time")) {
			return true;
		}
		return false;
	}
	
	public static boolean isString(String javaType) {
		if(javaType.endsWith("String")) {
			return true;
		}
		return false;
	}
}