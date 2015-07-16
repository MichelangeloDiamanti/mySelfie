<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


	<div id="leftMenuBG"></div>
	
   	<ul id="leftMenu">
   		
   		<hr class="lmhr">
   		
   		<a class="lma" href="${pageContext.request.contextPath}/protected/profile/${sessionScope.user.username}">
   			<li class="tab"><span class="glyphicon glyphicon-user"></span> Profile </li>
   		</a>
   		
   		<hr class="lmhr">
   		
   		<li id="tabH" class="tab"><span class="glyphicon glyphicon-home"></span> Home </li>
   		
   		
   		<hr class="lmhr">
   		
   		<li id="tabE" class="tab"><span class="glyphicon glyphicon-globe"></span> Explore </li>
   		
   		<hr class="lmhr">

   		<a class="lma" href="${pageContext.request.contextPath}/protected/modifyProfile.jsp">
   			<li class="tab"><span class="glyphicon glyphicon-wrench"></span> Settings </li>
   		</a>
   		
   		<hr class="lmhr">

   	</ul>
	
	