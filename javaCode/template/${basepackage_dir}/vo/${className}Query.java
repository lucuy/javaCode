<#include "/macro.include"/>
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

<#include "/java_imports.include">

public class ${className}Query extends BaseQuery implements Serializable {
    private static final long serialVersionUID = 3148176768559230877L;
    
	<@generateFields/>
	<@generateProperties/>
	<@generateJavaManyToOne/>
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
		<#list table.pkColumns as column>
			.append(get${column.columnName}())
		</#list>
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof ${className} == false) 
			return false;
		
		if(this == obj) 
			return true;
		
		${className} other = (${className})obj;
		
		return new EqualsBuilder()
			<#list table.pkColumns as column>
			.append(get${column.columnName}(),other.get${column.columnName}())
			</#list>
			.isEquals();
	}
	
}
<#macro generateFields>
	<#list table.columns as column>
	<#if !column.fk>
	<#if column.isDateTimeColumn>
	private ${column.simpleJavaType} ${column.columnNameLower}Begin;
	private ${column.simpleJavaType} ${column.columnNameLower}End;
	<#else>
	private ${column.simpleJavaType} ${column.columnNameLower};
	</#if>
	</#if>
	</#list>
</#macro>

<#macro generateProperties>
	<#list table.columns as column>
	<#if !column.fk>
	<#if column.isDateTimeColumn>
	public ${column.simpleJavaType} get${column.columnName}Begin() {
		return this.${column.columnNameLower}Begin;
	}
	
	public void set${column.columnName}Begin(${column.simpleJavaType} value) {
		this.${column.columnNameLower}Begin = value;
	}	
	
	public ${column.simpleJavaType} get${column.columnName}End() {
		return this.${column.columnNameLower}End;
	}
	
	public void set${column.columnName}End(${column.simpleJavaType} value) {
		this.${column.columnNameLower}End = value;
	}
	<#else>
	public ${column.simpleJavaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
	
	public void set${column.columnName}(${column.simpleJavaType} value) {
		this.${column.columnNameLower} = value;
	}
	</#if>	
	</#if>	
	</#list>
</#macro>

<#macro generateJavaManyToOne>
	<#list table.importedKeys as foreignKey>
	<#assign fkClass = foreignKey.table.className>
	<#assign fkClassVar = fkClass?uncap_first>
	
	private ${fkClass} ${fkClassVar};
	public void set${fkClass}(${fkClass} ${fkClassVar}){
		this.${fkClassVar} = ${fkClassVar};
	}
	
	public ${fkClass} get${fkClass}() {
		return ${fkClassVar};
	}
	</#list>
</#macro>


