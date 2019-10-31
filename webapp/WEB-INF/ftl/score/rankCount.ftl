<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8" http-equiv="Content-Type" content="multipart/form-data" ;/>
<title>成绩列表—成绩信息</title>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
<link rel="icon" href="${basePath}/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="${basePath}/favicon.ico" />
<link href="${basePath}/js/common/bootstrap/3.3.5/css/bootstrap.min.css?${_v}" rel="stylesheet" />
<link href="${basePath}/css/common/base.css?${_v}" rel="stylesheet" />
<script src="${basePath}/js/common/jquery/jquery1.8.3.min.js"></script>
<script src="${basePath}/js/common/layer/layer.js"></script>
<script src="${basePath}/js/common/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="${basePath}/js/shiro.demo.js"></script>
<script src="https://cdn.bootcss.com/echarts/4.2.1-rc1/echarts.min.js"></script>
<script type="text/javascript">
	$(function(){
		$("#classMark").click(function(){
			var classMark = $("#classMark").val();
			$.ajax({
		         type : "post",
		         async : true,           
		         url : "${basePath}/score/selecStutByClass.shtml",    
		         data : {classMark:classMark },
		         dataType : "json",        
		         success : function(result) {
		            $("#semesterInfo").empty();
				  	$.each(result.semesters,function(){
				  			var semOption = $("<option></option>").append(this.semesterName).attr("value",this.semesterId);
				  			semOption.appendTo("#semesterInfo");
				  		});
		         },
		         error : function(errorMsg) {
		         alert("暂无信息");
		         }
		    	})
		
		});
	});

</script>
</head>
<body data-target="#one" data-spy="scroll" >
<@_top.top 6/>
<div class="container" style="padding-bottom: 15px; min-height: 300px; margin-top: 40px;">
	<div class="row">
	<@_left.score 2/>
		<div class="col-md-10">
			<h3>成绩分析</h3>
			<hr />
			<h5>各班每次考试名次段人数波动图</h5>
			<div clss="well">
				
				<div class="form-group" id="subjectByClazz">
					<label class="control-label">班级:</label>
						<select class="" data-style="btn-primary"  id="classMark">
						<#if classMarks??>
						<#list classMarks as it>
							<option value="${it}">${it}</option>
						</#list>
						</#if>
						</select>
						<select class="" data-style="btn-primary"  id="semesterInfo">
							
						</select>
						
				</div>
			</div>
			
			<span class="">
				<button type="submit" class="btn btn-primary" onclick = "selectRankCount()">查看</button>
			</span>
			<div id="rankCount" style="width: 900px; height: 500px; margin: 0"></div>
				
		</div>
	</div>
</div>	
</body>
<script type="text/javascript">
	var rankCountChart = echarts.init(document.getElementById('rankCount'));
          // 显示标题，图例和空的坐标轴
     rankCountChart.setOption({
            tooltip: {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'cross',
		            crossStyle: {
		                color: '#999'
		            }
		        }
		    },
		    toolbox: {
		        feature: {
		            dataView: {show: true, readOnly: false},
		            magicType: {show: true, type: ['line', 'bar']},
		            restore: {show: true},
		            saveAsImage: {show: true}
		        }
		    },
		    legend: {
		        data:['前10','前20','前50','前100','前150','前200','前300']
		    },
		    xAxis: [
		        {
		            type: 'category',
		            data: [],
		            axisPointer: {
		                type: 'shadow'
		            }
		        }
		    ],
		    yAxis: [
		        {
		            type: 'value',
		            name: '人数',
		            min: 0,
		            max: 1200,
		            interval: 100,
		            axisLabel: {
		                formatter: '{value} '
		            }
		        }
		        
		    ],
		    series: [
		        {
		            name:'前10',
		            type:'bar',
		            data:[]
		        },
		        {
		            name:'前20',
		            type:'bar',
		            data:[]
		        },
		        {
		            name:'前50',
		            type:'bar',
		            data:[]
		        },
		        {
		            name:'前100',
		            type:'bar',
		            data:[]
		        },
		        {
		            name:'前150',
		            type:'bar',
		            data:[]
		        },
		        {
		            name:'前200',
		            type:'bar',
		            data:[]
		        },
		        {
		            name:'前300',
		            type:'bar',
		            data:[]
		        }
		    ]
          });

    	function selectRankCount(){
    		var examNames =[];
			var rank10s=[];    
		    var rank20s=[];    
		    var rank50s=[];    
			var rank100s = []; 
			var rank150s = []; 
			var rank200s = []; 
			var rank300s = []; 
			
			
			$.ajax({
		         type : "post",
		         async : true,           
		         url : "${basePath}/score/countRank.shtml",    
		         data : {classMark:$("#classMark").val(),semesterId:$("#semesterInfo").val()},
		         dataType : "json",        
		         success : function(result) {
		             
		             if (result) {
		                    for(var i=0;i<result.rankCounts.length;i++){  
		                       examNames.push(result.rankCounts[i].examName);  
		                       
		                    }
		                   
		                    for(var i=0;i<result.rankCounts.length;i++){ 
		                        rank10s.push(result.rankCounts[i].rank10Count);   
		                    }
		                    for(var i=0;i<result.rankCounts.length;i++){       
		                        rank20s.push(result.rankCounts[i].rank20Count);   
		                    }
		                    for(var i=0;i<result.rankCounts.length;i++){       
		                        rank50s.push(result.rankCounts[i].rank50Count);    
		                    }
		                     for(var i=0;i<result.rankCounts.length;i++){       
		                        rank100s.push(result.rankCounts[i].rank100Count);    
		                    }
		                     for(var i=0;i<result.rankCounts.length;i++){       
		                        rank150s.push(result.rankCounts[i].rank150Count);    
		                    }
		                     for(var i=0;i<result.rankCounts.length;i++){       
		                        rank200s.push(result.rankCounts[i].rank200Count);    
		                    }
		                    for(var i=0;i<result.rankCounts.length;i++){       
		                        rank300s.push(result.rankCounts[i].rank300Count);    
		                    }
		                    
		                    rankCountChart.hideLoading();  
		                    rankCountChart.setOption({ 
		                        xAxis: {
		                            data: examNames
		                        },
		                        series: [
		                         {
						            name:'前10',
						            data:rank10s
						        },
						        {
						            name:'前20',
						            data:rank20s
						        },
						        {
						            name:'前50',
						            data:rank50s
						        },
						        {
						            name:'前100',
						            data:rank100s
						        },
						        {
						            name:'前150',
						            data:rank150s
						        },
						        {
						            name:'前200',
						            data:rank200s
						        },
						        {
						            name:'前300',
						            data:rank300s
		        				}
		                        
		                        ]
		                    }); 
		                   
		             }
		         },
		         error : function(errorMsg) {
		         alert("图表请求数据失败!");
		         subScoreChart.hideLoading();
		         }
		    	})
		}
</script>
</html>
