<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:template>
    
    <jsp:attribute name="header">
		<link href="${pageContext.request.contextPath}/resources/css/navbar.css" media="all" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/resources/css/pageNotFound.css" media="all" rel="stylesheet" type="text/css" />
       	<link href="${pageContext.request.contextPath}/resources/css/leftMenu.css" media="all" rel="stylesheet" type="text/css" />
 
    </jsp:attribute>
    
    <jsp:attribute name="javascripts">
    	<script src="${pageContext.request.contextPath}/resources/javascript/scripts/navbar.js"></script>
    	<script src="${pageContext.request.contextPath}/resources/javascript/scripts/leftMenu.js"></script>
    		
	</jsp:attribute>
    
    <jsp:body>
		<jsp:include page="/WEB-INF/pages/navbar.jsp" />
		<jsp:include page="/WEB-INF/pages/leftMenu.jsp" /> 


		<img id="pnfimg" src="${pageContext.request.contextPath}/resources/images/PnF.png" >
		<label id="qcq">404</label><br>
		<label id="pnf">Page not found</label>
    </jsp:body>
        
</t:template>



