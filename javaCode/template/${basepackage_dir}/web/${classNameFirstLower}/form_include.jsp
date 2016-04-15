<#include "/macro.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<%@ page contentType="text/html;charset=UTF-8" import="${basepackage}.bean.*" %>
<%@ include file="/commons/taglibs.jsp" %>

<#list table.columns as column>
<#if column.pk>
	<input type="hidden" id="${column.columnNameLower}" name="${column.columnNameLower}" value="<@jspEl classNameLower+"."+column.columnNameLower/>"/>
</#if>
</#list>

<#list table.generalColumns as column>
	<#if !column.isDateTimeColumn>
	<tr>	
		<td class="tdLabel">
			<#if !column.nullable><span class="required">*</span></#if><%=${className}.ALIAS_${column.constantName}%>:
		</td>		
		<td>
		<#if column.size gte 500>
			<form:textarea path="${classNameLower}.${column.columnNameLower}" id="${column.columnNameLower}" class="elm" rows="12" style="width:80%;"></form:textarea>
		<#else>
			<form:input path="${classNameLower}.${column.columnNameLower}" id="${column.columnNameLower}" />
		</#if>
			<font color='red'><form:errors path="${column.columnNameLower}"/></font>
		</td>
	</tr>
	</#if>
</#list> 

<@generateJavaManyToOne/>

<#macro generateJavaManyToOne>
	<#list table.importedKeys as foreignKey>
	<#assign fkTable = foreignKey.table>
	<#assign fkPojoClass = fkTable.className>
	<#assign fkPojoClassVar = fkPojoClass?uncap_first>
	<#assign listPojoClassVar = 'list'+fkPojoClass>
	<#assign pojoClassVar = classNameLower+'.'+fkPojoClassVar+".id == item.id ? 'selected' : ''">
	
	<tr>	
		<td class="tdLabel"><%=${fkPojoClass}.TABLE_ALIAS%></td>
		<td>
			<select name="${fkPojoClassVar}.id">
				<c:forEach items="<@jspEl listPojoClassVar />" var="item">
					<option value="<@jspEl "item.id" />" <@jspEl pojoClassVar />><@jspEl "item.id" /></option>
				</c:forEach>
			</select>
		</td>
	</tr>
	</#list>
</#macro>		


