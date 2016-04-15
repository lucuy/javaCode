<#include "/macro.include"/> <#assign className = table.className>
<#assign classNameLower = className?uncap_first> <#assign column =
table.generalColumns>

<%@ page contentType="text/html;charset=UTF-8" import="com.u17.bean.*"%>
<%@ include file="../commons/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<%@ include file="../commons/meta.jsp"%>
<script type="text/javascript">
	KindEditor.ready(function(K) {
		window.editor = K.create('#editor_id',{
			resizeType : 1,
			allowPreviewEmoticons : false,
			items : [
				'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
				'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
				'insertunorderedlist']
		});
	});
	
	$(document).ready(function() {
		if($("#hide_up_img").val().length!=0){
			$("#photoCover").css({"display":"none"});
		}
		$("#up_img").AjaxFileUpload({
			action: "file/fileUpload",
			onComplete: function(filename, response) {
				console.log(response);
				response = $.parseJSON(response);
				
				if(response.result == "success"){
					$("#up_img_res").html("文件上传成功，文件地址:<a href='"+response.data+"' target='_blank'>"+response.data+"</a>");
					$("#hide_up_img").val(response.data);
					$("#photoCover").css({"display":"none"});
				}else {
					$("#up_img_res").text("文件上传失败");
				}	
			}
		});
	});

</script>
</head>
<body onLoad="setupZoom();">
	<%@ include file="../commons/top-nav.jsp"%>

	<!--主内容区包括左侧导航、中部内容区开始-->
	<div class="ch-container">
		<div class="row">
			<jsp:include page="../commons/left-nav.jsp">
				<jsp:param name="left_menu" value="1" />
			</jsp:include>

			<!-- 中间内容开始 -->
			<div id="content" class="col-lg-10 col-sm-10">
				<!-- 返回导航按钮 -->
				<div>
					<ul class="breadcrumb">
						<li><a href="#">Home</a></li>
						<li><a href="${classNameLower}/list">列表页</a></li>
					</ul>
				</div>

				<div class="row">
					<div class="box col-md-12">
						<div class="box-inner">
							<div class="box-header well" data-original-title="">
								<h2>
									<i class="glyphicon glyphicon-edit"></i> 新建首页轮播图
								</h2>

								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round btn-default"><i
										class="glyphicon glyphicon-cog"></i></a> <a href="#"
										class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a> <a href="#"
										class="btn btn-close btn-round btn-default"><i
										class="glyphicon glyphicon-remove"></i></a>
								</div>
							</div>

							<div class="box-content">
								<form id="reg-form" action="${classNameLower}/save" method="post" class="main form-horizontal">
									<c:if test="<@jspEl 'not empty ${classNameLower}.id'/>">
										<input type="hidden" name="id" value="<@jspEl '${classNameLower}.id'/>">
									</c:if>
									<!-- 不显示最后操作人员ID和时间类型的值 -->
									<!-- 判断不能为空的值前面标识红色* -->
									<#list table.generalColumns as column> 
									
									<#if column.constantName!="OP_USER_ID" && column.constantName!="UPDATE_TIME" && column.constantName!="CREATE_TIME">
									<div class="input-group input-group-sm" style="margin: 5px 0;">
										
										<#if !column.nullable>
										<span class="input-group-addon"><span style="font-size: 12px; color: red;">* </span> 
										<%=${className}.ALIAS_${column.constantName}%>：</span>
										
										<#if column.size gte 500> 
										<textarea path="${classNameLower}.${column.columnNameLower}" id="editor_id"  required class="autogrow"
											style="height: 100px; width: 100%;" name="${column.columnNameLower}">
										</textarea>
										<!-- 不能为空图片相关 --> 
										<#elseif column.constantName=="IMG" || column.constantName=="COMIC_COVER"> 
										 <div class="form-control">
											<label> 
											<input id="up_img" name="up_img" type="file" style="width: 70px;display: none;" /> 
											<div class="input-append">
													     <input type="button" onclick="$('input[id=up_img]').click();" value="选择文件" />
													     <input id="photoCover" type="text" placeholder="未选择任何图片" class="input-xlarge" required />
												        </div>
											<input id="hide_up_img" type="hidden" name="${column.columnNameLower}" value="<@jspEl '${classNameLower}.${column.columnNameLower}'/>" />
											</label>
											<c:choose>
												<c:when test="<@jspEl 'not empty ${classNameLower}.${column.columnNameLower}'/>">
													<label id="up_img_res">
													<a href="<@jspEl '${classNameLower}.${column.columnNameLower}'/>" target="_blank">
													<@jspEl '${classNameLower}.${column.columnNameLower}'/></a>
													</label>
												</c:when>
												<c:otherwise>
													<label id="up_img_res"></label>
												</c:otherwise>
											</c:choose>
										</div>
										<!-- 特殊字段处理 -->
										<#elseif column.constantName=="FLAG">
										<div class="form-control">
											<label style="margin: 0 5px;"> <input type="radio"
												name="flag" value="0"
												<c:if test="<@jspEl '${classNameLower}.flag eq 0'/>">
														checked="checked"
													</c:if> />
												显示
											</label> <label style="margin: 0 5px;"> <input type="radio"
												name="flag" value="2"
												<c:if test="<@jspEl '${classNameLower}.flag eq 2'/>">
														checked="checked"
													</c:if> />
												隐藏
											</label>
										</div>
										<!-- 特殊字段处理 -->
										<#elseif column.constantName=="TYPE">
										
										<select name="type" style="width:400px;height:30px;">
											<option value="1"
												<c:if test="<@jspEl '${classNameLower}.type eq 1'/>">
													selected="selected"	
												</c:if>
												>首页${classNameLower}</option>
											<option value="2"
												<c:if test="<@jspEl '${classNameLower}.type eq 2'/>">
													selected="selected"	
												</c:if>
												>动漫展示页</option>
										</select>
									    <!-- 特殊字段处理 -->
										<#elseif column.constantName=="LABEL">
										
										<div class="form-control">
											<label style="margin: 0 5px;"> <input name="label" value="8" type="checkbox"
												<c:if test="<@jspEl 'fn:contains(${classNameLower}.label,\'8\')'/>">
													checked="checked"
												</c:if>
												> vip </label> 
											<label style="margin: 0 5px;"> <input name="label" value="4" type="checkbox"
												<c:if test="<@jspEl 'fn:contains(${classNameLower}.label,\'4\')'/>">
													checked="checked"
												</c:if>
												> 独家  </label>
											<label style="margin: 0 5px;"> <input name="label" value="2" type="checkbox"
												<c:if test="<@jspEl 'fn:contains(${classNameLower}.label,\'2\')'/>">
													checked="checked"
												</c:if>
												> 连载  </label>
											<label style="margin: 0 5px;"> <input name="label" value="1" type="checkbox"
												<c:if test="<@jspEl 'fn:contains(${classNameLower}.label,\'1\')'/>">
													checked="checked"
												</c:if>
												> 完结  </label>
										</div>
										
										<#else>
										<input path="${classNameLower}.${column.columnNameLower}" id="${column.columnNameLower}" name="${column.columnNameLower}" type="text" value="<@jspEl '${classNameLower}.${column.columnNameLower}'/>" class="form-control"  required/>
										</#if> 
										<font color='red'><form:errors path="${column.columnNameLower}" /></font>
										</div>
										
										<#else>
										<span class="input-group-addon"><%=${className}.ALIAS_${column.constantName}%>：</span>
										
										<#if column.size gte 500> 
										<textarea path="${classNameLower}.${column.columnNameLower}"  id="editor_id" class="autogrow"
											style="height: 100px; width: 100%;" name="${column.columnNameLower}">
										</textarea> 
										<#elseif column.constantName=="IMG" || column.constantName=="COMIC_COVER"> 
										 <div class="form-control">
											<label> 
											<input id="up_img" name="up_img" type="file" style="width: 70px;display: none;" /> 
											<div class="input-append">
												<input type="button" onclick="$('input[id=up_img]').click();" value="选择文件" />
												<input id="photoCover" type="text" placeholder="未选择任何图片" />
											</div>
											<input id="hide_up_img" type="hidden" name="${column.columnNameLower}" value="<@jspEl '${classNameLower}.${column.columnNameLower}'/>" />
											</label>
											<c:choose>
												<c:when test="<@jspEl 'not empty ${classNameLower}.${column.columnNameLower}'/>">
													<label id="up_img_res">
													<a href="<@jspEl '${classNameLower}.${column.columnNameLower}'/>" target="_blank">
													<@jspEl '${classNameLower}.${column.columnNameLower}'/></a>
													</label>
												</c:when>
												<c:otherwise>
													<label id="up_img_res"></label>
												</c:otherwise>
											</c:choose>
										</div>
										<!-- 特殊字段处理 -->
										<#elseif column.constantName=="FLAG">
										<div class="form-control">
											<label style="margin: 0 5px;"> <input type="radio"
												name="flag" value="0"
												<c:if test="<@jspEl '${classNameLower}.flag eq 0'/>">
														checked="checked"
												</c:if> />
												显示
											</label> <label style="margin: 0 5px;"> <input type="radio"
												name="flag" value="2"
												<c:if test="<@jspEl '${classNameLower}.flag eq 2'/>">
														checked="checked"
												</c:if> />
												隐藏
											</label>
										</div>
										<!-- 特殊字段处理 -->
										<#elseif column.constantName=="TYPE">
										
										<select name="type" style="width:400px;height:30px;">
											<option value="1"
												<c:if test="<@jspEl '${classNameLower}.type eq 1'/>">
													selected="selected"	
												</c:if>
												>首页${classNameLower}</option>
											<option value="2"
												<c:if test="<@jspEl '${classNameLower}.type eq 2'/>">
													selected="selected"	
												</c:if>
												>动漫展示页</option>
										</select>
										<!-- 特殊字段处理 -->
										<#elseif column.constantName=="LABEL">
										
										<div class="form-control">
											<label style="margin: 0 5px;"> <input name="label" value="8" type="checkbox"
												<c:if test="<@jspEl 'fn:contains(${classNameLower}.label,\'8\')'/>">
													checked="checked"
												</c:if>
												> vip </label> 
											<label style="margin: 0 5px;"> <input name="label" value="4" type="checkbox"
												<c:if test="<@jspEl 'fn:contains(${classNameLower}.label,\'4\')'/>">
													checked="checked"
												</c:if>
												> 独家  </label>
											<label style="margin: 0 5px;"> <input name="label" value="2" type="checkbox"
												<c:if test="<@jspEl 'fn:contains(${classNameLower}.label,\'2\')'/>">
													checked="checked"
												</c:if>
												> 连载  </label>
											<label style="margin: 0 5px;"> <input name="label" value="1" type="checkbox"
												<c:if test="<@jspEl 'fn:contains(${classNameLower}.label,\'1\')'/>">
													checked="checked"
												</c:if>
												> 完结  </label>
										</div>
									
										<#else>
										<input path="${classNameLower}.${column.columnNameLower}" id="${column.columnNameLower}" name="${column.columnNameLower}" type="text" value="<@jspEl '${classNameLower}.${column.columnNameLower}'/>" class="form-control"/>
										</#if> 
										<font color='red'><form:errors path="${column.columnNameLower}" /></font>
										</div>
										
										</#if>
									</#if> 
									</#list>
									<!-- 外键相关字段处理 -->
									<#list table.importedKeys as foreignKey> <#assign fkPojoClass =
									foreignKey.table.className> <#assign fkPojoClassVar =
									fkPojoClass?uncap_first>
									<!-- many 关联的 one 的属性 -->
									<div class="input-group input-group-sm" style="margin: 5px 0;">

										<span class="input-group-addon"><%=${fkPojoClass}.ALIAS_NAME%>：</span>
										<select name="${fkPojoClassVar}.id"
											style="width: 400px; height: 30px;">
											<c:forEach items="<@jspEl 'list${fkPojoClass}'/>" var="mess">
												<option value="<@jspEl 'mess.id'/>"
													<c:if test="<@jspEl '${classNameLower}.${fkPojoClassVar}.id eq mess.id'/>">
													selected="selected"	
												</c:if>><@jspEl
													'mess.name'/></option>
											</c:forEach>
										</select>
									</div>
									</#list>
									<button id="ok" type="submit" class="btn btn-primary">提交</button>
									<button type="button" class="btn btn-warning" onClick="javascript:window.history.back();">返回</button>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- 中间内容结束 -->
		</div>
	</div>
	<!--主内容区包括左侧导航、中部内容区结束-->
	<script>
	
		$('input[id=up_img]').change(function() {
			$('#photoCover').val($(this).val());
		});
		
	</script>
</body>
</html>