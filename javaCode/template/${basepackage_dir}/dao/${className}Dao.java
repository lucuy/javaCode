<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
package ${basepackage}.dao;

<#include "/java_imports.include">

import static com.u17.util.ObjectUtils.isNotEmpty;

import org.springframework.stereotype.Repository;

@Repository
public class ${className}Dao extends BaseHibernateDao<${className},${(table.pkColumns[0].simpleJavaType)!"Integer"}>{

	@Override
	public Class<${className}> getEntityClass() {
		return ${className}.class;
	}
	
	public Page<${className}> findPage(${className}Query query) {
		return pageQuery(baseHql(query),query);
	}
	
	public List<${className}> findByQuery(${className}Query query) {
		return findByQuery(baseHql(query),query);
	}
	
	public int findCountQuery(${className}Query query){
		return findCountQuery(baseHql(query),query);
	}
	
	private String baseHql(${className}Query query){
		StringBuilder hql = new StringBuilder("select t from ${className} t where 1=1 and flag >= 0");
		
        <#list table.columns as column>
        <#if !column.fk>
        <#if column.isDateTimeColumn>
        if(isNotEmpty(query.get${column.columnName}Begin())) {
        	hql.append(" and  t.${column.columnNameLower} >= :${column.columnNameLower}Begin ");
        }
        if(isNotEmpty(query.get${column.columnName}End())) {
        	hql.append(" and  t.${column.columnNameLower} <= :${column.columnNameLower}End ");
        }
        <#else>
        if(isNotEmpty(query.get${column.columnName}())) {
        	hql.append(" and  t.${column.columnNameLower} = :${column.columnNameLower} ");
        }
        </#if>
        </#if>
        </#list>
        
		<#list table.importedKeys as foreignKey>
	    if(isNotEmpty(query.get${foreignKey.table.className}()) && isNotEmpty(query.get${foreignKey.table.className}().getId())) {
		    hql.append(" and  t.${classNameLower} = :${classNameLower} ");
		}
	    </#list>
	         
        if(isNotEmpty(query.getSortColumns())) {
        	hql.append(" order by "+query.getSortColumns());
        }
        
        return hql.toString();
	}

}
