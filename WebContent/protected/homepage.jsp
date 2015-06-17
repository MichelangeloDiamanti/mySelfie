<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    
    <jsp:attribute name="header">
		<link href="${pageContext.request.contextPath}/resources/css/navbar.css" media="all" rel="stylesheet" type="text/css" />       
    </jsp:attribute>
    
    <jsp:attribute name="javascripts">	
		
		
	</jsp:attribute>
    
    <jsp:body>

		<jsp:include page="/WEB-INF/pages/navbar.jsp" />
		
		<c:out value="${sessionScope.user.id_user}" />
		<c:out value="${sessionScope.user.nickname}" />
		<c:out value="${sessionScope.user.email}" />
		<c:out value="${sessionScope.user.profilepic}" />
		<img src="${pageContext.request.contextPath}/protected/resources/profilepics/<c:out value="${sessionScope.user.profilepic}" />" >
    </jsp:body>
        
</t:template>



