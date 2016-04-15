package javaCode;

import java.io.IOException;

import freemarker.template.TemplateException;

public class GeneratorMain {

	public static void main(String[] args) {
		try {
			GeneratorFacade.generateByAllTable();
			//GeneratorFacade.generateByTable("wui_comic_chapter");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
}
