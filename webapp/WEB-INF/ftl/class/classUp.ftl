<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8" />
		<title>升级操作 - 班级管理</title>
		<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
		<link   rel="icon" href="${basePath}/favicon.ico" type="image/x-icon" />
		<link   rel="shortcut icon" href="${basePath}/favicon.ico" />
		<link href="${basePath}/js/common/bootstrap/3.3.5/css/bootstrap.min.css?${_v}" rel="stylesheet"/>
		<link href="${basePath}/css/common/base.css?${_v}" rel="stylesheet"/>
		<script  src="${basePath}/js/common/jquery/jquery1.8.3.min.js"></script>
		<script  src="${basePath}/js/common/layer/layer.js"></script>
		<script  src="${basePath}/js/common/bootstrap/3.3.5/js/bootstrap.min.js"></script>
		<script  src="${basePath}/js/shiro.demo.js"></script>
	</head>
	
		
	<body data-target="#one" data-spy="scroll">
		<#--引入头部-->
		<@_top.top 2/>
		<div class="container" style="padding-bottom: 15px;min-height: 300px; margin-top: 40px;">
			<div class="row">
				<#--引入左侧菜单-->
				<@_left.sys 4/>
				<div class="col-md-10">
					<h2>班级列表</h2>
					<hr>
					<form method="post" action="" id="formId" class="form-inline">
						<div clss="well">
					      <div class="form-group">
					        <input type="text" class="form-control" style="width: 300px;" 
					        			name="gradeNameOrClassName" id="findContent" placeholder="输入班级 /年级名称">
					      </div>
					     <span class="">
				         	<button type="submit" class="btn btn-primary">查询</button>
				         		<a class="btn btn-success" onclick="$('#addClass').modal();">增加班级</a>
				         		<button type="button" onclick="$('#classUupTc').modal();"; class="btn btn-primary">升级</button> 
				         </span>    
				        </div>
					<hr>
					<table class="table table-bordered">
						<tr>
							<th><input type="checkbox" id="checkAll"/></th>
							<th>班级名称</th>
							<th>班级类别</th>
							<th>班主任</th>
							<th>班级标识</th>
							<th>备注</th>
							<th>操作升级</th>
						</tr>
						 	 <#--<#assign page= classUp["page"]/>-->
						 	 <#assign teacherList= classUp["teacherList"]/>
						 	  <#assign pageInfo= classUp["pageInfo"]/>
						<#if pageInfo?exists && pageInfo.list?size gt 0 >
							<#list pageInfo.list as it>
								<tr>
									<td><input value="${it.classId}" check='box' type="checkbox" /></td>
									<td><#if it.gradeName==1>
										七年级
										</#if>
										<#if it.gradeName==2>
										八年级
										</#if>
										<#if it.gradeName==3>
										九年级
										</#if>${it.className}</td>
									<td><#if it.classCategory=='A'>
									实验班
										</#if>
										<#if it.classCategory=='B'>
									重点班
										</#if>
										<#if it.classCategory=='C'>
									平行班
										</#if>
									</td>
									<td>
									${(it.teacher.tchName)!"暂无"}
									</td>
									<td>${it.stuClassMark}</td>
									<td>${it.remark}</td>
									 <td><!--<a class="btn btn-success" onclick="upUp(${it.gradeName}, '${it.className}','${it.stuClassMark}')">升级</a>-->&nbsp
									<a class="btn  btn-danger" onclick="subjectTeacherList(${it.gradeName}, '${it.className}',${it.classId})">高级</a>
									<a class="btn  btn-danger" onclick="selectTeacherByClassId(${it.classId},${it.gradeName},'${it.className}')">详情</a>
									</td>
								</tr>
							</#list>
						<#else>
							<tr>
								<td class="text-center danger" colspan="4">没有找班级信息</td>
							</tr>
						</#if>
					</table>
					</form>
					
				<div class="col-md-6" style="margin-top: 50px">
				    <span>当前显示第&nbsp;${pageInfo.getPageNum()}&nbsp;页,共&nbsp${pageInfo.getTotal()}&nbsp条记录</span>
				 <div class="pagination pagination-centered" >
				  <ul class="bottom" >
				  <#if  pageInfo.isFirstPage==false>
				  <a href="${basePath}/classs/classUp.shtml?pn=${pageInfo.prePage}&">上页</a>
				  </#if>
						<#list pageInfo.navigatepageNums as item>
						<a href="${basePath}/classs/classUp.shtml?pn=${item}&">${item}</a>
						</#list>
						
						  <#if  pageInfo.hasNextPage==true>
				  <a  href="${basePath}/classs/classUp.shtml?pn=${pageInfo.nextPage}&">下页</a>
				  </#if>
				  <a href="${basePath}/classs/classUp.shtml?pn=${pageInfo.lastPage}&">末页</a>
					  </ul>
					  </div>
					  </div>
					
								<#--添加班级弹框-->
			<div class="modal fade bs-example-modal-sm"  id="addClass" tabindex="-1" role="dialog" aria-labelledby="selectPermissionLabel">
			  <div class="modal-dialog modal-sm" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title">添加班级</h4>
			      </div>
			      <div class="modal-body">
			    <form action="${basePath}/student/insertClass.shtml" id="insertClass">
				<li>年级:&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<select name="gradeName" id="gradeName" >
				<option value="1">七年级</option>
				<option value="2">八年级</option>
				<option value="3">九年级</option>
				</select></li><br />
				<li>班级:&nbsp&nbsp&nbsp<input id="className" name="className" type="text"></li><br/>
				<li>班级类别：<select name="classCategory">
								<option value="A">实验班</option>
								<option value="B">重点班</option>
								<option value="C">平行班</option>
								</select></li><br>
				<li>标识:<input name="stuClassMark" id="stuClassMark" type="text"></li><br/>
				<li>备注:<input name="remark" id="remark" type="text"></li>
				<div>
				</div>
				</form>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			        <button type="button" onclick="addclasss()" class="btn btn-primary">Save</button>
			      </div>
			    </div>
			  </div>
			</div>
			<#--/弹框-->
											<#--班级升级-->
			<div class="modal fade bs-example-modal-sm"  id="updateClassMark" tabindex="-1" role="dialog" aria-labelledby="selectPermissionLabel">
			  <div class="modal-dialog modal-sm" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title">班级升级</h4>
			      </div>
			      <div class="modal-body">
			   <form action="${basePath}/classs/classUpUp.shtml" id="updateClassMark">
			    <input type="hidden" id="classCountext" />
			   	 升级后：<span id="classAndGradeName"></span></br></br>
			    <div id="classUp">
			
			    </div>
			   </from>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			        <button type="button" onclick="upSubmit()" class="btn btn-primary">Save</button>
			      </div>
			    </div>
			  </div>
			</div>
			<#--/弹框-->
			
			<#--班级高级弹框-->
			<div class="modal fade bs-example-modal-sm"  id="seniorClass" tabindex="-1" role="dialog" aria-labelledby="selectPermissionLabel">
			  <div class="modal-dialog modal-sm" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title">高级设置</h4>
			      </div>
			      <div class="modal-body">
			    <form action="${basePath}/student/insertClassTeacher.shtml" id="insertClassTeacer">
			    <input type="hidden" name="classId" id="classId" value="">
			     <input type="hidden" name="gradeNameId" id="gradeNameId" value="">
			    <li><span id="classGradeName"></span></li></br>
				<li>班主任: <select id="headTeacher" name="headTeacher">
				  <option value="0">--请选择--</option>
				<#list teacherList as item>
				<option value="${item.tchId}">${item.tchName}</option>
				</#list>
				</select></li><br />
				<div id="subjectList">
					
					
					</div>
				<div>
				</div>
				</form>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			        <button type="button" onclick="addTeacher()" class="btn btn-primary">Save</button>
			      </div>
			    </div>
			  </div>
			</div>
			<#--/弹框-->
			
			<#--班级详情弹框-->
			<div class="modal fade bs-example-modal-sm"  id="classContext" tabindex="-1" role="dialog" aria-labelledby="selectPermissionLabel">
			  <div class="modal-dialog modal-sm" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title">班级详情</h4>
			      </div>
			      <div class="modal-body">
			    <form action="${basePath}/student/insertClassTeacher.shtml" id="insertClassTeacer">
			    <input type="hidden" name="classId" id="classId" value="">
			    <li><span id="classNameGradeName"></span></li></br>
			    <li>班主任:<span id="headTch"></span></li></br>
				<div id="subjectTeacherList">
					
				</div>
				<div>
				</div>
				</form>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			      </div>
			    </div>
			  </div>
			</div>
			<#--/弹框-->
			
				<#--班级详情弹框-->
			<div class="modal fade bs-example-modal-sm"  id="classUupTc" tabindex="-1" role="dialog" aria-labelledby="selectPermissionLabel">
			  <div class="modal-dialog modal-sm" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title">班级升级</h4>
			      </div>
			      <div class="modal-body">
				  确定升级？？
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			         <button type="button" onclick="upupclass()" class="btn btn-primary">Save</button>
			      </div>
			    </div>
			  </div>
			</div>
			<#--/弹框-->
			
				</div>
			</div><#--/row-->
		</div>
		
		
		
	 <script>
   function addclasss(){
    var className=$("#className").val();
	var stuClassMark=$("#stuClassMark").val();
	if(className==""||stuClassMark==""){
	alert("请将数据补充完整");
	}else{
            $.getJSON('${basePath}/classs/insertClass.shtml',$('#insertClass').serialize(),function(data,textStatus,jqXHR){
    		console.log(data);
    		alert(data.result); 
    		 window.location.reload();
			});
	}
   }
   
	
   </script>
   
   <script>
		function selectTeacherByClassId(classId,gradeName,className){

		if(gradeName=="1"){
		gradeName="七年级";
		}else if(gradeName=="2"){
		gradeName="八年级";
		}else if(gradeName=="3"){
		gradeName="九年级";
		}
		
		$("#classNameGradeName").empty();
		$('#classNameGradeName').append(gradeName+" "+className);
		
		
$.getJSON("${basePath}/classs/selectTeacherByClassId.shtml",{classId,classId},function(data,textStatus,jqXHR){
	$.each(data,function(key,values){
    				if(key=="班主任"){
    				$('#headTch').empty();
    				$("#headTch").append(values);
    				}else{
    				$("#subjectTeacherList").append("<li>"+key+":"+values+"</li></br>");
    				}
    				});
			});
	
    		$('#subjectTeacherList').empty();
    		$('#classContext').modal();
    		
    		}
    		
		</script>
		
		<script>
	function addTeacher(){
	var headTeacher=$('#headTeacher').val();
	if(headTeacher=='0'){
	alert("班主任不能为空");
	}else{
		
		var classId=$("#classId").val();
		var data=classId;
		var geadeName=$('#gradeNameId').val();
		var headTeacher=$("#headTeacher").val();
		var ChineseTeacher=$("#语文").val();
		var MathematicsTeacher=$("#数学").val();
		var EnglishTeacher=$("#外语").val();
		var GeographyTeacher=$("#地理").val();
		var PhysicsTeacher=$("#物理").val();
		var BiologyTeacher=$("#生物").val();
		var HistoryTeacher=$("#历史").val();
		var ChemistryTeacher=$("#化学").val();
		var PoliticsTeacher=$("#政治").val();
		var SportsTeacher=$("#体育").val();8
		var musicTracher=$("#音乐").val();
		var fineArtsTeacher=$("#美术").val();
		var microComputerTeacher=$("#微机").val();
		var psychologyTeacher=$("#心理").val();
		if(geadeName=='1'){
		data=classId+","+headTeacher+","+ChineseTeacher+","+MathematicsTeacher+","+EnglishTeacher+","+GeographyTeacher+","+BiologyTeacher+","+HistoryTeacher+","+PoliticsTeacher+","+musicTracher+","+fineArtsTeacher+","+microComputerTeacher+","+psychologyTeacher+","+SportsTeacher;
		}else if(geadeName=='2'){
		data=classId+","+headTeacher+","+ChineseTeacher+","+MathematicsTeacher+","+EnglishTeacher+","+GeographyTeacher+","+BiologyTeacher+","+HistoryTeacher+","+PoliticsTeacher+","+PhysicsTeacher+","+musicTracher+","+fineArtsTeacher+","+microComputerTeacher+","+psychologyTeacher+","+SportsTeacher;
		}else if(geadeName=='3'){
		data=classId+","+headTeacher+","+ChineseTeacher+","+MathematicsTeacher+","+EnglishTeacher+","+HistoryTeacher+","+PoliticsTeacher+","+ChemistryTeacher+","+PhysicsTeacher+","+SportsTeacher;
		}
			$.getJSON("${basePath}/classs/insertClassTeacher.shtml",{data,data},function(data,textStatus,jqXHR){
    		alert(data.result); 
    		 window.location.reload();
			});
            }
	}
   </script>
   
   <script>
		function subjectTeacherList(str,className,classId){
		
		var gradeName="";
		if(str=="1"){
		gradeName="七年级";
		}else if(str=="2"){
		gradeName="八年级";
		}else if(str=="3"){
		gradeName="九年级";
		}
		$('#classId').val(classId);
		$('#gradeNameId').val(str);
		$("#classGradeName").empty();
		$('#classGradeName').append(gradeName+" "+className);
		var data=str+','+classId;
		
		$.getJSON("${basePath}/classs/divChange.shtml",{data:data},function(data,textStatus,jqXHR){
		$("#subjectList").empty(); 
    			$.each(data.teacherMapByGrade,function(key,values){
    			console.log(key); 
    			$("#subjectList").append("<li>"+key+"：<select id="+key+"><option>--请选择--</option></select></li></br>"); 
				    console.log(key); 
				     $(values).each(function(){    
				     switch (key){
				    case "语文":
				     $("#语文").append("<option value="+this.tchId+">"+this.tchName+"</option>"); 
				    break;
				     case "数学":
				     $("#数学").append("<option value="+this.tchId+">"+this.tchName+"</option>"); 
				    break;
				     case "外语":
				     $("#外语").append("<option value="+this.tchId+">"+this.tchName+"</option>"); 
				    break;
				     case "政治":
				     $("#政治").append("<option value="+this.tchId+">"+this.tchName+"</option>"); 
				    break;
				     case "历史":
				     $("#历史").append("<option value="+this.tchId+">"+this.tchName+"</option>"); 
				    break;
				     case "地理":
				     $("#地理").append("<option value="+this.tchId+">"+this.tchName+"</option>"); 
				    break;
				     case "物理":
				     $("#物理").append("<option value="+this.tchId+"> "+this.tchName+"</option>"); 
				    break;
				      case "化学":
				     $("#化学").append("<option value="+this.tchId+">"+this.tchName+"</option>"); 
				    break;
				       case "生物":
				     $("#生物").append("<option value="+this.tchId+">"+this.tchName+"</option>"); 
				    break;
				    case "音乐":
				     $("#音乐").append("<option value="+this.tchId+">"+this.tchName+"</option>"); 
				    break;
				    case "体育":
				     $("#体育").append("<option value="+this.tchId+">"+this.tchName+"</option>"); 
				    break;
				    case "美术":
				     $("#美术").append("<option value="+this.tchId+">"+this.tchName+"</option>"); 
				    break;
				    case "微机":
				     $("#微机").append("<option value="+this.tchId+">"+this.tchName+"</option>"); 
				    break;
				      case "心理":
				     $("#心理").append("<option value="+this.tchId+">"+this.tchName+"</option>"); 
				    break;
				    }  
				    });
				 	}); 
				
			$.each(data.classDownTeacherMap,function(key,values){
			 $("#"+key).val(values);
			});
		});
    
		$('#seniorClass').modal();
		}
		</script>
		
   	<script>
   	function upUp(gradeName,className,classMark){
   
   		var gradeId=gradeName;
		if(gradeName=="1"){
		gradeId="八年级";
		}else if(gradeName=="2"){
		gradeId="九年级";
		}
		if(gradeName=="1"||gradeName=="2"){
		$("#classCountext").empty();
		$("#classCountext").val(gradeName+","+className+","+classMark);
		$("#classAndGradeName").empty();
		$('#classAndGradeName').append(gradeId+" "+className);
		$("#classUp").empty();
		$('#classUp').append("班级：<select id='classType'><option value='A'>实验班</option><option value='B'>重点班</option><option value='C'>平行班</option></select>");
		}else{
		$("#classCountext").empty();
		$("#classCountext").val(gradeName+","+className+","+classMark+",毕业");
		$("#classUp").empty();
		$("#classAndGradeName").empty();
		$('#classAndGradeName').append("毕业了");
		}
		$('#updateClassMark').modal();
		}
   	</script>
   	<script>
   	function upSubmit(){
   	var data=$("#classCountext").val();
   	var classType=$("#classType").val();
   	if(classType!=null){
   	data+=","+classType;
   	}
   		
$.getJSON("${basePath}/classs/classUpUp.shtml",{data,data},function(data,textStatus,jqXHR){
    		alert(data.result); 
    		 window.location.reload();
			});
   	}
   	</script>
   	
   	<script>
   	function upupclass(){
  	$.getJSON('${basePath}/classs/upupclass.shtml',{data:""},function(data,textStatus,jqXHR){
    		alert(data.result); 
    		 window.location.reload();
			});
   	}
   	</script>
	</body>
</html>