<#include "/macro.include"/> 
<#include "/custom.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<%@ page contentType="text/html;charset=UTF-8" import="com.u17.bean.*"%>
<%@ include file="../commons/taglibs.jsp"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<%@ include file="../commons/meta.jsp"%>
	<script type="text/javascript" >
		$(document).ready(function() {
			window.simpleTable = new SimpleTable('queryForm','<@jspEl 'p:page(page)'/>','<@jspEl 'p:pageSize(page)'/>');
		});
	</script>
</head>

<body>
	<%@ include file="../commons/top-nav.jsp"%>
	
	<div class="ch-container">
		<div class="row">	
			<jsp:include page="../commons/left-nav.jsp">
				<jsp:param name="left_menu" value="1"/>
			</jsp:include>

			<!-- 中间内容开始 -->
			<div id="content" class="col-lg-10 col-sm-10">
				<!-- 返回导航按钮 -->
				<div>
					<ul class="breadcrumb">
						<li><a href="#">Home</a></li>
						<li><a href="#">Tables</a></li>
					</ul>
				</div>

				<div class="row">
					<div class="box col-md-12">
						<div class="box-inner">
							<div class="box-header well" data-original-title="">
								<h2>
									<i class="glyphicon glyphicon-user"></i> 数 据 列 表
								</h2>

								<div class="box-icon">
									<a href="${classNameLower}/create" class="btn btn-round btn-default">
										<i class="glyphicon glyphicon-plus"></i>
									</a>
								</div>
							</div>
							<div class="box-content">
								<div class="alert alert-info">每页<@jspEl 'p:pageSize(page)'/>条数据，总共<@jspEl 'p:totalPages(page)'/>页，当前第<@jspEl 'p:page(page)'/>页</div>
								<#-- 查询条件 -->
								<form id="queryForm" name="queryForm" action="${classNameLower}/list" method="post" class="form-inline" style="margin-bottom: 8px;">
									<#list table.generalColumns as column>
									<#if !column.isDateTimeColumn>
									<div class="form-group has-success has-feedback">
										<#-- 对于 label标签 和 input标签中 name value 应该对应同一字段-->
										<label class="control-label"><%=${className}.ALIAS_${column.constantName}%>：</label>
										<input name="${column.columnNameLower }" value="<@jspEl "query."+column.columnNameLower/>" type="text" class="form-control" />
									</div>
									<#if column_index gte 2><#break></#if>
									</#if>
									</#list>
									
									<a class="btn btn-success" href="javascript:document.queryForm.submit();"> 
										<i class="glyphicon glyphicon-zoom-in icon-white"></i> 搜索
									</a>
								</form>
								
								<table
									class="table table-striped table-bordered bootstrap-datatable responsive">
									<thead>
										<tr>
											<th>序号</th>
											<#-- 关联属性 -->
											<#list table.importedKeys as foreignKey>
												<#assign fkPojoClass = foreignKey.table.className>
												<#assign fkPojoClassVar = fkPojoClass?uncap_first>
												<!-- many 关联的 one 的属性 -->
											<th><%=${fkPojoClass}.ALIAS_NAME%></th>
											</#list>
											<#list table.generalColumns as column>
											<th><%=${className}.ALIAS_${column.constantName}%></th>
											</#list>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="<@jspEl 'page'/>" var="item" varStatus="status">
											<tr>
												<td class="center" style="vertical-align:middle; text-align:center;"><@jspEl 'p:firstElementNumber(page) + status.index'/></td>					
												<#list table.importedKeys as foreignKey>
												<td class="center"><@jspEl "item."+foreignKey.table.className?uncap_first+".id"/></td>
												</#list>									
												<#list table.generalColumns as column>
												<#if column.columnNameLower == 'createTime' || column.columnNameLower == 'updateTime'>
												<td class="center"><@jspEl "d:format(item."+column.columnNameLower+",\"yyyy-MM-dd\")"/></td>
												<#elseif column.columnNameLower == 'img'>	
												<td class="center">
													<a href="<@jspEl "item."+column.columnNameLower/>" data-toggle="popover" rel="popover" data-img="<@jspEl "item."+column.columnNameLower/>" 
															data-width="100px" data-height="100px" target="_blank"><@jspEl "t:stringCutMiddle(item."+column.columnNameLower+",40)"/></a>
												</td>
												<#elseif column.size gt 200>
												<td class="center"><@jspEl "t:stringCut(item."+column.columnNameLower+",40)"/></td>
												<#else>
												<td class="center"><@jspEl "item."+column.columnNameLower/></td>
												</#if>								
												</#list>
												<td class="center">
													<a class="btn btn-success" href="${classNameLower}/view/<@jspEl 'item.id'/>"><i class="glyphicon glyphicon-zoom-in icon-white"></i> 查看</a> 
													<a class="btn btn-info" href="${classNameLower}/edit/<@jspEl 'item.id'/>"> <i class="glyphicon glyphicon-edit icon-white"></i> 编辑</a> 
													<a class="btn btn-danger" data-toggle="modal" data-target="#myModal<@jspEl 'item.id'/>"> <i class="glyphicon glyphicon-trash icon-white"></i> 删除</a>
													
													<div class="modal fade bs-example-modal-sm" id="myModal<@jspEl 'item.id'/>" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
													  <div class="modal-dialog modal-sm">
													    <div class="modal-content">
													      <div class="modal-body">
													      	<p style="text-align: center;font-family:arial;color:red;font-size:18px;"><img src="static/images/delornot.png" style="width: 50px;height: 50px;"/>是否确定删除记录?</p> </div>
													      <div class="modal-footer" style="text-align: center">
													        <button type="button" class="btn btn-warning" data-dismiss="modal" onclick="javascript:simpleTable.toggleDelPage('<@jspEl 'p:page(page)'/>','<@jspEl 'item.id'/>','${classNameLower}/delete');">确认</button>
													        <button type="button" class="btn btn-primary" data-dismiss="modal" aria-label="Close">取消</button>
													      </div>
													    </div>
													  </div>
													</div>
													
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<div class="col-md-12 center-block">
									<div class="paging_bootstrap pagination">
										<ul class="pagination">
											
											<c:choose>
												<c:when test="<@jspEl 'p:isFirstPage(page)'/>">
													<li class="prev disabled"><a href="#">← 上一页</a></li>
												</c:when>
												<c:otherwise>
													<li class="prev"><a href="javascript:simpleTable.togglePage(<@jspEl 'p:page(page) - 1'/>);">← 上一页</a></li>
												</c:otherwise>
											</c:choose>
											
											<c:forEach begin="<@jspEl 'p:beginPage(page,7)'/>" end="<@jspEl 'p:endPage(page,7)'/>" var="p">
												<c:choose>
													<c:when test="<@jspEl 'p == p:page(page)'/>">
														<li class="active"><a href="javascript:void(0);"><@jspEl 'p'/></a></li>
													</c:when>
													<c:otherwise>
														<li><a href="javascript:simpleTable.togglePage(<@jspEl 'p'/>);"><@jspEl 'p'/></a></li>
													</c:otherwise>
												</c:choose>
											</c:forEach>
											
											<c:choose>
												<c:when test="<@jspEl '!p:isLastPage(page)'/>">
													<li class="next"><a href="javascript:simpleTable.togglePage(<@jspEl 'p:page(page) + 1'/>);">下一页 → </a></li>
												</c:when>
												<c:otherwise>
													<li class="next disabled"><a href="#">下一页 → </a></li>
												</c:otherwise>
											</c:choose>
										</ul>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- 中间内容结束 -->
			</div>
		</div>
	</div>
	<!--主内容区包括左侧导航、中部内容区结束-->
</body>		
</html>
