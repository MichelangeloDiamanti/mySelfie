<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    
    <jsp:attribute name="header">
		<link href="${pageContext.request.contextPath}/resources/css/navbar.css" media="all" rel="stylesheet" type="text/css" />
       	<link href="${pageContext.request.contextPath}/resources/css/leftMenu.css" media="all" rel="stylesheet" type="text/css" />
    </jsp:attribute>
    
    <jsp:attribute name="javascripts">	

		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/navbar.js"></script>
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/leftMenu.js"></script>
			
	</jsp:attribute>
    
    <jsp:body>

		<jsp:include page="/WEB-INF/pages/navbar.jsp" />
		<jsp:include page="/WEB-INF/pages/leftMenu.jsp" /> 
		
		
		<h1>Users:</h1>
		
		<c:forEach var="user" items="${requestScope.userMatches}">
			    <c:out value="${user.id_user}"/> 
			    <c:out value="${user.username}"/> 
			    <c:out value="${user.email}"/> 
			    <c:out value="${user.phone}"/> 
			    <c:out value="${user.name}"/> 
			    <c:out value="${user.surname}"/> 
			    <c:out value="${user.gender}"/> 
			    <c:out value="${user.notes}"/> 
			    <c:out value="${user.city}"/> 
			    <c:out value="${user.profilepic}"/> 
			    <c:out value="${user.birthdate}"/>  
			    <br>
		</c:forEach>
		
		<h1>Selfies:</h1>
		
		<c:forEach var="selfie" items="${requestScope.selfieMatches}">
		    <c:out value="${selfie.id_selfie}"/> 
		    <c:out value="${selfie.uploader}"/> 
		    <c:out value="${selfie.description}"/> 
		    <c:out value="${selfie.location}"/> 
		    <c:out value="${selfie.date}"/> 
		    <c:out value="${selfie.picture}"/> 
		    <br>
		</c:forEach>
		
		<h1>HashTags:</h1>
		
		<c:forEach var="hashtag" items="${requestScope.hashTagMatches}">
		    <c:out value="${hashtag.id_hashtag}"/> 
		    <c:out value="${hashtag.name}"/> 
		    <br>
		</c:forEach>
		
    </jsp:body>
        
</t:template>



