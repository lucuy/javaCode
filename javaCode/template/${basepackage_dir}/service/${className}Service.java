<#include "/custom.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
package ${basepackage}.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<#include "/java_imports.include">

@Service
@Transactional
public class ${className}Service extends BaseService<${className},${(table.pkColumns[0].simpleJavaType)!"Integer"}>{
//	此表id对应的域名
	protected static final String ID_NAME = "id";
//	此表删除状态对应的域名
	protected static final String DELETE_FLAG_NAME = "flag";
//	此表逻辑删除删除对应的值
	protected static final int DELETE_FLAG = -1;
	
	@Autowired
	private ${className}Dao ${classNameLower}Dao;
	
	<#list table.exportedKeys as fkTable>
	<#assign fkClassName    = fkTable.className>
	<#assign fkClassNameLower = fkTable.className?uncap_first>
	@Autowired
	private ${fkClassName}Service ${fkClassNameLower}Service;
	</#list>
	
	public BaseDao<${className},${(table.pkColumns[0].javaType)!"Integer"}> getBaseDao() {
		return this.${classNameLower}Dao;
	}
	
	@Transactional(readOnly=true)
	public Page<${className}> findPage(${className}Query query) {
		return ${classNameLower}Dao.findPage(query);
	}
	
	@Transactional(readOnly=true)
	public List<${className}> findByQuery(${className}Query query) {
		return ${classNameLower}Dao.findByQuery(query);
	}
	
	@Transactional(readOnly = true)
	public int findCountQuery(${className}Query query) {
		return ${classNameLower}Dao.findCountQuery(query);
	}
	
	@Transactional
	public void deleteById(${(table.pkColumns[0].javaType)!"Integer"} id) {
//		删除此表数据，根据主键进行删除
		Map<String,Object> setCondition = new HashMap<String, Object>();
		setCondition.put(DELETE_FLAG_NAME,DELETE_FLAG);
		Map<String,Object> whereCondition = new HashMap<String, Object>();
		whereCondition.put(ID_NAME,id);
		this.updateByField(setCondition, whereCondition);
		this.caseDelete(id);
	}
	
	private void caseDelete(${(table.pkColumns[0].javaType)!"Integer"} id) {
		<#list table.exportedKeys as fkTable>
		<#assign fkClassName    = fkTable.className>
		<#assign fkClassNameLower = fkTable.className?uncap_first>
		
//		删除一对多关联表中数据，根据外键，进行逻辑删除
		Map<String,Object> ${fkClassNameLower}SetCondition = new HashMap<String, Object>();
//		逻辑删除,修改关联表删除状态
		${fkClassNameLower}SetCondition.put("flag", -1);
		Map<String,Object> ${fkClassNameLower}WhereCondition = new HashMap<String, Object>();
//		外键关联,通过外键确定关联表中对应记录
		${fkClassNameLower}WhereCondition.put("${classNameLower}_id",id);
		${fkClassNameLower}Service.updateByField(${fkClassNameLower}SetCondition,${fkClassNameLower}WhereCondition);
		
		</#list>
	}
	
}
