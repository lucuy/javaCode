package javaCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javaCode.db.model.Table;
import javaCode.util.FileUtil;
import javaCode.util.TableUtil;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class GeneratorFacade {
	private static File getTemplateDir(){
		File file = new File(GeneratorProperties.getProperty("template_dir","template"));
		
		if(file.exists())
			return file;
		else
			System.err.println("模板文件目录不存在");
		
		return null;
	}
	
	public static void generateByTable(String sqlTable) throws IOException, TemplateException{
		File template_dir = getTemplateDir();
		
		List<String> templates = FileUtil.getAllTemplate(template_dir,".include");
		
		Configuration cfg = getConfiguration(template_dir);
		
		Table table = TableUtil.getSqlTable(sqlTable);
		
		System.err.println(table.getSqlName()+"开始自动生成代码");
		for (String string : templates) {
			Template template = cfg.getTemplate(FileUtil.subTemplateDir(string));
			
			Map<String,Object> root = new HashMap<String,Object>();
			root.put("table", table);
			
			String outRoot = GeneratorProperties.getProperty("outRoot").replaceAll("\\\\","/");
			
			String filename = outRoot+FileUtil.subTemplateDir(FileUtil.convertPath(string,table));
			String dirs = FileUtil.convertDir(filename);
			
			File dir = new File(dirs);
			dir.mkdirs();
			
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(new File(filename)),"UTF-8");
			
			template.process(root, out);
			
			out.close();
		}
		
		System.err.println("自动生成代码结束");
	}
	
	public static void generateByAllTable() throws IOException, TemplateException{
		File template_dir = getTemplateDir();
		
		List<String> templates = FileUtil.getAllTemplate(template_dir,".include");
		
		Configuration cfg = getConfiguration(template_dir);
		
		List<Table> tables = TableUtil.getAllTable();
		
		for(Table table:tables){
			for (String string : templates) {
				Template template = cfg.getTemplate(FileUtil.subTemplateDir(string));
				
				Map<String,Object> root = new HashMap<String,Object>();
				root.put("table", table);
				
				String outRoot = GeneratorProperties.getProperty("outRoot").replaceAll("\\\\","/");
				
				String filename = outRoot+FileUtil.subTemplateDir(FileUtil.convertPath(string,table));
				String dirs = FileUtil.convertDir(filename);
				
				File dir = new File(dirs);
				dir.mkdirs();
				
				OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(new File(filename)),"UTF-8");
				
				template.process(root, out);
				
				out.close();
			}
		}
		
		System.err.println("自动生成代码结束");
	}
	
	private static Configuration getConfiguration(File file){
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
		
		try {
			FileTemplateLoader loder = new FileTemplateLoader(file);
			
			cfg.setTemplateLoader(loder);
			cfg.setSharedVariable("basepackage", GeneratorProperties.getProperty("basepackage"));
			cfg.setSharedVariable("namespace", GeneratorProperties.getProperty("namespace"));
			cfg.setSharedVariable("schema", GeneratorProperties.getProperty("jdbc.schema"));
			cfg.setDefaultEncoding("UTF-8");
			
			return cfg;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
