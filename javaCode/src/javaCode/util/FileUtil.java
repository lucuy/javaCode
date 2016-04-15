package javaCode.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javaCode.GeneratorProperties;

public class FileUtil {
	
	public static List<String> getAllTemplate(File file,String exclusion){
		List<String> templates = new ArrayList<String>();
		
		recursionFile(file,templates,exclusion);
		
		return templates;
	}
	
	public static void recursionFile(File file,List<String> templates,String exclusion){
		File[] files = file.listFiles();
		
		for(File f:files){
			if(f.isDirectory()){
				recursionFile(f,templates,exclusion);
			}else{
				if(!f.getName().endsWith(exclusion))
					templates.add(f.getPath().replaceAll("\\\\","/"));
			}
		}
	}
	
	public static List<String> convert(String str){
		char[] chars = str.toCharArray();
		
		List<String> strs = new ArrayList<String>();
		String temp = "";
		
		boolean flag = false;
		
		for (int i = 0; i < chars.length; i++) {
			if(flag && chars[i] == '}'){
				flag = false;
				
				if(!temp.trim().equals("")){
					strs.add(temp);
					temp = "";
				}
			} else if(flag && (chars[i] == '$' || chars[i] == '{')){
				temp = "";
				flag = false;
			} else if(flag){
				temp += chars[i];
			}
			
			if(chars[i] == '{' && i > 0 && chars[i-1] == '$')
				flag = true;
		}
		
		return strs;
	}
	
	public static String convertPath(String path,Object o){
		String separator = "/";
		String[] paths = path.replaceAll("\\\\","/").split("/");
		
		StringBuffer sb = new StringBuffer();
		
		for(String p:paths){
			List<String> strs = convert(p);
			
			if(strs != null && strs.size() > 0){
				String temp = p;
				
				for (String string : strs) {
					String value = GeneratorProperties.getProperty(string, o);
					temp = temp.replace("${"+string+"}", value);//替换变量
				}
				
				sb.append(temp+separator);
			}else{
				sb.append(p+separator);
			}
		}
		
		if(sb.toString().endsWith(separator))
			sb.deleteCharAt(sb.length()-1);
		
		return sb.toString();
	}
	
	public static String convertDir(String path){
		if(path.lastIndexOf("/") == -1)
			return path;
		else
			return path.substring(0,path.lastIndexOf("/"));
	}
	
	public static String subTemplateDir(String str){
		if(str.startsWith("template"))	
			return str.substring(str.indexOf("/"));
		else 
			return null;
	}
}
