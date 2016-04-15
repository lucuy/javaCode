<#include "/macro.include"/> 
<#include "/custom.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<%@ page contentType="text/html;charset=UTF-8" import="${basepackage}.bean.*"%>
<%@ include file="/commons/taglibs.jsp" %>

<gk:override name="head">
	<title><%=${className}.TABLE_ALIAS%> 维护</title>
	
	<script type="text/javascript" >
		$(document).ready(function() {
			window.simpleTable = new SimpleTable('queryForm','<@jspEl 'p:page(page)'/>','<@jspEl 'p:pageSize(page)'/>','<@jspEl 'pageRequest.sortColumns'/>');
		});
	</script>
</gk:override>

<gk:override name="content">
	<form id="queryForm" name="queryForm" action="${classNameLower}/list" method="post" style="display:inline;">
	<fieldset class="fieldset">
		<legend>搜索</legend>
		<#list table.generalColumns as column>
		<#if !column.isDateTimeColumn>
		<label><%=${className}.ALIAS_${column.constantName}%>：</label>
		<input id="${column.columnNameLower}" name="${column.columnNameLower}" class="md-input" type="text" value="<@jspEl "query."+column.columnNameLower/>"/>
		</#if>
		</#list>	
		<input type="submit" class="std-button" value="查询" onclick="getReferenceForm(this).action='${classNameLower}/list'" />		
	</fieldset>
	
	<div class="handleControl">
		<input type="submit" class="std-button" value="新增" onclick="getReferenceForm(this).action='${classNameLower}/create'"/>
		<input type="button" class="std-button" value="删除" onclick="batchDelete('${classNameLower}/batchDelete','items',document.forms.queryForm)"/>
	</div>
	
	<table class="table">
		<thead>
			<tr>
				<th>序号</th>
				<th style="width: 50px;"><input type="checkbox" onclick="setAllCheckboxState('items',this.checked)"></th>
				<#list table.generalColumns as column>
				<th><%=${className}.ALIAS_${column.constantName}%></th>
				</#list>
				<#list table.importedKeys as foreignKey>
				<th><%=${foreignKey.table.className}.TABLE_ALIAS%></th>
				</#list>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="<@jspEl 'page'/>" var="item" varStatus="status">
				<tr>
					<td><@jspEl 'p:firstElementNumber(page) + status.index'/></td>
					<td><input type="checkbox" name="items" value="<@jspEl 'item.id'/>"></td>
					<#list table.generalColumns as column>
					<#if column.isDateTimeColumn>
					<td><fmt:formatDate value="<@jspEl "item."+column.columnNameLower/>" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<#else>
					<td><@jspEl "item."+column.columnNameLower/></td>
					</#if>
					</#list>
					<#list table.importedKeys as foreignKey>
					<td><@jspEl "item."+foreignKey.table.className?uncap_first+".id"/></td>
					</#list>
					<td>
						<a href="${classNameLower}/edit/<@jspEl 'item.id'/>">修改</a>、
						<a href="${classNameLower}/delete/<@jspEl 'item.id'/>">删除</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td colspan=100><simpletable:pageToolbar page="<@jspEl 'page'/>"></simpletable:pageToolbar></td>
			</tr>
		</tfoot>
	</table>
	</form>
</gk:override>

<%@ include file="../base.jsp" %>
