<#include "/macro.include"/>
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.portal.bean;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

<#include "/java_imports.include">

@Entity
@Table(name = "${table.sqlName}",schema = "${schema}")
@DynamicInsert
@DynamicUpdate
public class ${className} extends BaseBean {
	private static final long serialVersionUID = -5349781903754068011L;
	
	public static final String TABLE_ALIAS = "${table.remarks!""}";
	<#list table.generalColumns as column>
	public static final String ALIAS_${column.constantName} = "${column.remarks!""}";
	</#list>
		
	<@generateFields/>
	<@generatePkProperties/>
	<@generateNotPkProperties/>
	<@generateJavaOneToMany/>
	<@generateJavaManyToOne/>

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
		<#list table.columns as column>
			<#if !column.fk>
			.append("${column.columnName}",get${column.columnName}())
			</#if>
		</#list>
			.toString();
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
	private ${column.simpleJavaType} ${column.columnNameLower};
		</#if>
	</#list>

</#macro>

<#macro generatePkProperties>
	<#list table.columns as column>
	<#if column.pk>
	public void set${column.columnName}(${column.simpleJavaType} id) {
		this.${column.columnNameLower} = id;
	}
	
	@Id @GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "native")
	public ${column.simpleJavaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
	</#if>
	</#list>
</#macro>

<#macro generateNotPkProperties>
	<#list table.generalColumns as column>
		@Column(name = "${column.sqlName}", nullable = ${column.nullable?string}, length = ${column.size?c})
		public ${column.simpleJavaType} get${column.columnName}() {
			return this.${column.columnNameLower};
		}
					
		public void set${column.columnName}(${column.simpleJavaType} value) {
			this.${column.columnNameLower} = value;
		}
	</#list>
</#macro>

<#macro generateJavaOneToMany>
	<#list table.exportedKeys as fkTable>
	<#assign fkClassName    = fkTable.className>
	<#assign fkClassNameLower = fkTable.className?uncap_first>
	
	private List<${fkClassName}> list${fkClassName} = new ArrayList<${fkClassName}>();
	public void setList${fkClassName}(List<${fkClassName}> list${fkClassName}){
		this.list${fkClassName} = list${fkClassName};
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "${classNameLower}")
	public	List<${fkClassName}> getList${fkClassName}() {
		return list${fkClassName};
	}
	</#list>
</#macro>

<#macro generateJavaManyToOne>
	<#list table.importedKeys as foreignKey>
	<#assign fkSqlTable = foreignKey.table>
	<#assign fkClassName    = fkSqlTable.className>
	<#assign fkClassNameLower = fkSqlTable.className?uncap_first>
	
	private ${fkClassName} ${fkClassNameLower};
	public void set${fkClassName}(${fkClassName} ${fkClassNameLower}){
		this.${fkClassNameLower} = ${fkClassNameLower};
	}
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "${foreignKey.fk_column.sqlName}")
	})
	public ${fkClassName} get${fkClassName}() {
		return ${fkClassNameLower};
	}
	</#list>
</#macro>

