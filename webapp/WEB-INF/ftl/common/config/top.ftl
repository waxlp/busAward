<#macro top index>
			<style>
#topMenu>li>a {
	padding: 10px 13px
}
</style>
<script baseUrl="${basePath}" src="${basePath}/js/user.login.js"></script>
<script type="text/javascript">
	function logout(){
		window.location.href="${basePath}/logout.shtml";
	}
</script>
<div class="navbar navbar-inverse navbar-fixed-top animated fadeInDown" style="z-index: 101; height: 41px;">

	<div class="container" style="padding-left: 0px; padding-right: 0px;">
		<div class="navbar-header">
			<button data-target=".navbar-collapse" data-toggle="collapse"
				type="button" class="navbar-toggle collapsed">
				<span class="sr-only">导航</span> <span class="icon-bar"></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
		</div>
		<div role="navigation" class="navbar-collapse collapse">
			<a id="_logo" href="${basePath}"
				style="color: #fff; font-size: 24px;" class="navbar-brand hidden-sm"></a>
			<ul class="nav navbar-nav" id="topMenu">
				<li class="dropdown ${(index==1)?string('active','')}"><a
					aria-expanded="false" aria-haspopup="true" role="button"
					data-toggle="dropdown" class="dropdown-toggle"
					href="${basePath}/user/index.shtml"> 校园快讯<span class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<li><a href="${basePath}/user/index.shtml">个人资料</a></li>
						<li><a href="${basePath}/user/resetPwd.shtml">密码重置</a></li>
						<li><a href="${basePath}/user/updatePswd.shtml">密码修改</a></li>
						<li><a href="${basePath}/news/index.shtml?category=">校园资讯</a></li>
					</ul></li> <#--拥有 角色 校长,政教处-->
				<@shiro.hasAnyRoles name='校长,政教处'>
				<li class="dropdown ${(index==2)?string('active','')}"><a
					aria-expanded="false" aria-haspopup="true" role="button"
					data-toggle="dropdown" class="dropdown-toggle"
					href="${basePath}/sys/index.shtml"> 系统管理<span class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<@shiro.hasPermission name="/sys/index.shtml">
						<li><a href="${basePath}/sys/index.shtml">学期设置</a></li>
						</@shiro.hasPermission>
						<@shiro.hasPermission name="/sys/award.shtml">
						<li><a href="${basePath}/sys/award.shtml">奖励系数</a></li>
						</@shiro.hasPermission>
						<@shiro.hasPermission name="/sys/tchUp.shtml">
						<li><a href="${basePath}/sys/tchUp.shtml">教师信息</a></li>
						</@shiro.hasPermission>
						<@shiro.hasPermission name="/classs/classUp.shtml">
						<li><a href="${basePath}/classs/classUp.shtml">班级升级</a></li>
						</@shiro.hasPermission>
						<@shiro.hasPermission name="/sys/alotSubject.shtml">
						<li><a href="${basePath}/sys/alotSubject.shtml">科目管理</a></li>
						</@shiro.hasPermission>
						<@shiro.hasPermission name="/news/manageNews.shtml">
						<li><a href="${basePath}/news/manageNews.shtml">资讯管理</a></li>
						</@shiro.hasPermission>
					</ul></li>
				</@shiro.hasAnyRoles> <#--拥有 角色 校长,政教处-->
				<@shiro.hasAnyRoles name='校长,政教处'>
				<li class="dropdown ${(index==3)?string('active','')}"><a
					aria-expanded="false" aria-haspopup="true" role="button"
					data-toggle="dropdown" class="dropdown-toggle"
					href="${basePath}/permission/index.shtml"> 权限管理<span
						class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<@shiro.hasPermission name="/role/index.shtml">
						<li><a href="${basePath}/role/index.shtml">角色列表</a></li>
						</@shiro.hasPermission>
						<@shiro.hasPermission name="/role/allotRole.shtml">
						<li><a href="${basePath}/role/allotRole.shtml">角色分配</a></li>
						</@shiro.hasPermission>
						<@shiro.hasPermission name="/permission/index.shtml">
						<li><a href="${basePath}/permission/index.shtml">权限列表</a></li>
						</@shiro.hasPermission>
						<@shiro.hasPermission name="/permission/allotPermission.shtml">
						<li><a href="${basePath}/permission/allotPermission.shtml">权限分配</a></li>
						</@shiro.hasPermission>
					</ul></li> </@shiro.hasAnyRoles> <#---->
				<@shiro.hasAnyRoles name='校长,政教处,班主任,任课教师'>
				<li class="dropdown ${(index==4)?string('active','')}"><a
					aria-expanded="false" aria-haspopup="true" role="button"
					data-toggle="dropdown" class="dropdown-toggle"
					href="${basePath}/permission/index.shtml"> 学生信息<span
						class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<@shiro.hasPermission name="/stu/index.shtml">
						<li><a href="${basePath}/student/stu.shtml">学生列表</a></li>
						</@shiro.hasPermission>
						<@shiro.hasPermission name="/stu/stuDay.shtml">
						<li><a href="${basePath}/studay/stuDay.shtml">学生日常</a></li>
						</@shiro.hasPermission>
						<@shiro.hasPermission name="/stu/chart.shtml">
						<li><a href="${basePath}/StuDayChart/returnStuDayChart.shtml">日常统计</a></li>
						</@shiro.hasPermission>
					</ul></li> </@shiro.hasAnyRoles> <#---->
				<@shiro.hasAnyRoles name='校长,政教处,班主任,学生'>
				<li class="dropdown ${(index==5)?string('active','')}"><a
					aria-expanded="false" aria-haspopup="true" role="button"
					data-toggle="dropdown" class="dropdown-toggle"
					href="${basePath}/class/index.shtml"> 班级信息<span class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<@shiro.hasPermission name="/class/classDay.shtml">
						<li><a href="${basePath}/classs/classsDay.shtml">班级日常</a></li>
						</@shiro.hasPermission><@shiro.hasPermission
						name="/class/classChart.shtml">
						<li><a href="${basePath}/classs/classChart.shtml">班级统计</a></li>
						</@shiro.hasPermission> <@shiro.hasPermission
						name="/class/myClass.shtml">
						<li><a href="${basePath}/classs/myClass.shtml">我的班级</a></li>
						</@shiro.hasPermission>
					</ul></li> </@shiro.hasAnyRoles> <#----> <@shiro.hasAnyRoles
				name='校长,政教处,教务处,学生,任课教师,班主任'>
				<li class="dropdown ${(index==6)?string('active','')}"><a
					aria-expanded="false" aria-haspopup="true" role="button"
					data-toggle="dropdown" class="dropdown-toggle"
					href="${basePath}/score/index.shtml"> 成绩信息<span class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<@shiro.hasPermission name="/score/index.shtml">
						<li><a href="${basePath}/score/index.shtml">成绩列表</a></li>
						</@shiro.hasPermission> <@shiro.hasPermission
						name="/score/showScore.shtml">
						
						<li><a href="${basePath}/score/showScore.shtml">成绩展示</a></li>
						</@shiro.hasPermission> <@shiro.hasPermission
						name="/score/myScore.shtml">
						<li><a href="${basePath}/score/myScore.shtml">我的成绩</a></li>
						</@shiro.hasPermission>
						<@shiro.hasPermission name="/score/StudentScore.shtml">
						<li><a href="${basePath}/score/StudentScore.shtml">个人成绩对比</a></li>
						</@shiro.hasPermission>
					</ul></li> </@shiro.hasAnyRoles> <#----> <@shiro.hasAnyRoles
				name='学生,班主任,政教处,教务处'>
				<li class="dropdown ${(index==7)?string('active','')}"><a
					aria-expanded="false" aria-haspopup="true" role="button"
					data-toggle="dropdown" class="dropdown-toggle"
					href="${basePath}/permission/index.shtml"> 巴士币<span
						class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<@shiro.hasPermission name="/coin/allotSCoin.shtml">
						<li><a href="${basePath}/coin/allotSCoin.shtml">巴士币(学生日常)</a></li>
						</@shiro.hasPermission> <@shiro.hasPermission name="/coin/allotCCoin.shtml">
						<li><a href="${basePath}/coin/allotCCoin.shtml">巴士币(班级)</a></li>
						</@shiro.hasPermission> <@shiro.hasPermission
						name="/student/myCoin.shtml">
						<li><a href="${basePath}/student/myCoin.shtml">我的巴士币</a></li>
						</@shiro.hasPermission>
					</ul></li> </@shiro.hasAnyRoles>
					
					<li class="dropdown ${(index==7)?string('active','')}"><a
						aria-expanded="false" aria-haspopup="true" role="button"
						data-toggle="dropdown" class="dropdown-toggle"
						href="${basePath}/user/index.shtml"> 个人资料<span
							class="caret"></span>
					</a>
						<ul class="dropdown-menu">
						
							<li class="dropdown ${(index==10)?string('active','')}" style="color:#000;">
					<a aria-expanded="false" aria-haspopup="true"  role="button" data-toggle="dropdown"  />
						<@shiro.user>  
							<@shiro.principal property="userName"/><span class="caret"></span></a>
							<ul class="dropdown-menu" userid="">
								<li><a href="${basePath}/user/index.shtml">个人资料</a></li>
								<li><a href="javascript:void(0);" onclick="logout();">退出登录</a></li>
							</ul>
						</@shiro.user>  
						<@shiro.guest>   
							 href="javascript:void(0);" onclick="location.href='${basePath}/u/login.shtml'" class="dropdown-toggle qqlogin" >
							<img src="${basePath}/images/du.png">&nbsp;登录</a>
						</@shiro.guest>  					
				</li>	
							
							
						</ul></li> 
					
			</ul>
		
		</div>
	</div>
</div>
</#macro>
