<#include "/custom.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>   
<#assign classNameLowerCase = className?lower_case>   

package ${basepackage}.controller;

<#include "/java_imports.include">

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.log4j.Logger;

import com.u17.bean.Banner;
import com.u17.bean.Movie;
import com.u17.common.DateUtils;
import com.u17.util.ObjectUtils;
import com.u17.vo.BannerQuery;

@Controller
@RequestMapping("/${classNameFirstLower}")
public class ${className}Controller extends BaseController{
	private static Logger logger = Logger.getLogger(${className}Controller.class);
	protected static final String DEFAULT_SORT_COLUMNS = "orderNum,updateTime desc";
	@Autowired
	private ${className}Service ${classNameFirstLower}Service;
	<#list table.importedKeys as foreignKey>
	<#assign fkPojoClass = foreignKey.table.className>
	<#assign fkPojoClassVar = fkPojoClass?uncap_first>
	@Autowired
	private ${fkPojoClass}Service ${fkPojoClassVar}Service;
	</#list>
	
	@RequestMapping("/create")
	public ModelAndView create(${className} ${classNameFirstLower}) {
		ModelAndView mv = new ModelAndView("/${classNameFirstLower}/create");
		<#-- 关联属性 -->
		<#list table.importedKeys as foreignKey>
			<#assign fkPojoClass = foreignKey.table.className>
			<#assign fkPojoClassVar = fkPojoClass?uncap_first>
	
			List<${fkPojoClass}> list${fkPojoClass} = ${fkPojoClassVar}Service.getAll();
			mv.addObject("list${fkPojoClass}", list${fkPojoClass});
		</#list>
		return mv;
	}
	
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public ModelAndView save(${className} ${classNameFirstLower},RedirectAttributes attr) {
		ModelAndView mv = new ModelAndView("redirect:/${classNameFirstLower}/list");
		
		if(${classNameFirstLower}.getId() == null){
			${classNameFirstLower}.setCreateTime(DateUtils.currentTimeSeconds());
			${classNameFirstLower}.setUpdateTime(DateUtils.currentTimeSeconds());
//			TODO 通过session获取操作员ID
			${classNameFirstLower}.setOpUserId(1);
			${classNameFirstLower}Service.save(${classNameFirstLower});
			attr.addFlashAttribute("success", "添加成功!");
		}else{
			${classNameFirstLower}.setUpdateTime(DateUtils.currentTimeSeconds());
			${classNameFirstLower}Service.dynamicUpdate(${classNameFirstLower});
			attr.addFlashAttribute("success", "修改成功!");
		}
		logger.debug("创建成功");
		return mv;
	}
	
	@RequestMapping(value="/delete")
	public ModelAndView delete(${className}Query query,RedirectAttributes attr) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IntrospectionException {
		ModelAndView mv = new ModelAndView("redirect:/${classNameFirstLower}/list");
		${classNameFirstLower}Service.deleteById(query.getId());
		query.setId(null);
		attr.addFlashAttribute("success", "删除成功");
		attr.addAllAttributes(ObjectUtils.convertBean(query));
		
		return mv;
	}
	
	@RequestMapping(value="/batchDelete")
	public ModelAndView batchDelete(@RequestParam("items") Integer[] items,RedirectAttributes attr) {
		ModelAndView mv = new ModelAndView("redirect:/${classNameFirstLower}/list");
		for(int i = 0; i < items.length; i++) {
			${classNameFirstLower}Service.deleteById(items[i]);
		}
		attr.addFlashAttribute("success", "批量删除成功");
		
		return mv;
	}
	
	@RequestMapping(value="/edit/{id}")
	public ModelAndView edit(@PathVariable int id) {
		ModelAndView mv = new ModelAndView("/${classNameFirstLower}/create");
		${className} ${classNameFirstLower} = ${classNameFirstLower}Service.getById(id);	
		mv.addObject("${classNameFirstLower}", ${classNameFirstLower});	
		<#-- 关联属性 -->
		<#list table.importedKeys as foreignKey>
			<#assign fkPojoClass = foreignKey.table.className>
			<#assign fkPojoClassVar = fkPojoClass?uncap_first>
			List<${fkPojoClass}> list${fkPojoClass} = ${fkPojoClassVar}Service.getAll();
			mv.addObject("list${fkPojoClass}", list${fkPojoClass});
		</#list>
		
		return mv;
	}
	
	@RequestMapping(value="/view/{id}")
	public ModelAndView view(@PathVariable int id) {
		ModelAndView mv = new ModelAndView("/${classNameFirstLower}/view");	
		${className} ${classNameFirstLower} = ${classNameFirstLower}Service.getById(id);
		mv.addObject("${classNameFirstLower}", ${classNameFirstLower});
		<#-- 关联属性 -->
		<#list table.importedKeys as foreignKey>
			<#assign fkPojoClass = foreignKey.table.className>
			<#assign fkPojoClassVar = fkPojoClass?uncap_first>
	
			List<${fkPojoClass}> list${fkPojoClass} = ${fkPojoClassVar}Service.getAll();
			mv.addObject("list${fkPojoClass}", list${fkPojoClass});
		</#list>
	
		return mv;
	}
	
	@RequestMapping("/list")
	public ModelAndView list(${className}Query query) {
		ModelAndView mv = new ModelAndView("/${classNameFirstLower}/list");	
		query.setSortColumns(DEFAULT_SORT_COLUMNS);	
		Page<${className}> page = ${classNameFirstLower}Service.findPage(query);	
		mv.addObject("page", page);
		mv.addObject("query", query);
		
		return mv;
	}
	
	@RequestMapping("/jsonView")
	@ResponseBody 
	public Object jsonView(${className}Query query){
		Map<String, Object> modelMap = new HashMap<String, Object>(); 
		modelMap.put("key", "value");
		
		return modelMap;
	}
	
}