package javaCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import javaCode.util.BeanUtil;

/**
 * 解析xml文件的类
 */
public class GeneratorProperties {
	private static final String PROPERTIES_FILE_NAME = "generator.xml";
	private static Properties props = new Properties();
	
	static{
		reload();
	}
	
	public static void reload() {
		try {
			File file = new File(ClassLoader.getSystemResource(PROPERTIES_FILE_NAME).toURI());
			InputStream is = new FileInputStream(file);
			
			props.loadFromXML(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key){
		return props.getProperty(key);
	}
	
	public static String getProperty(String key,String defaultValue){
		return props.getProperty(key, defaultValue);
	}
	
	/**
	 * 根据key先在配置文件中找值，如果不存在则在第二个参数中找
	 */
	public static String getProperty(String key,Object o){
		String value = getProperty(key);
		
		if(value == null){
			Map<String,Object> map = BeanUtil.describe(o);
			
			return (String)map.get(key);
		}
		
		return value;
	}
}
