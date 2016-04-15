<#include "/macro.include"/>
 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>

<gk:override name="head">
	<title><%=${className}.TABLE_ALIAS%><@jspEl classNameLower+".id == null ? '新增':'修改'" /></title>
	
	<script type="text/javascript" src="js/xheditor/xheditor.js"></script>
	<script type="text/javascript" src="js/xheditor/xheditor_lang/zh-cn.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$(".elm").xheditor({
				upImgUrl:function(){
					var cb = "http://192.168.2.108:8080/University/iframe.html";
					return "http://192.168.2.208:80/iframe_upload?X-Progress-ID="+uuid()+"&cb="+cb;
				},
				upImgExt:"jpg,jpeg,gif,png"
			});
			
			/* $("ul.tabs").tabs("div.panes > div");
			
			$("#test").click(function(){
				$(".table").children("tbody").load("pages/table.jsp",function(){
					
				});
			}); */
		});
		
	</script>
</gk:override>

<gk:override name="content">
	<form action="${classNameLower}/save" method="post">
		<table class="formTable">
			<%@ include file="form_include.jsp" %>
			<tr>
				<td colspan="100" style="text-align:center;">
					<input id="submitButton" name="submitButton" type="submit" value="提交" />
					<input type="button" value="返回列表" onclick="window.location='${classNameLower}/list'"/>
					<input type="button" value="后退" onclick="history.back();"/>
				</td>
			</tr>
		</table>
	</form>
</gk:override>

<%@ include file="../base.jsp" %>