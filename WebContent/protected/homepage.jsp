<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    
    <jsp:attribute name="header">
		<link href="${pageContext.request.contextPath}/resources/css/navbar.css" media="all" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/resources/css/postTable.css" media="all" rel="stylesheet" type="text/css" />    
    </jsp:attribute>
    
    <jsp:attribute name="javascripts">	
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/navbar.js"></script>
		<script type="text/javascript">
			<jsp:include page="/resources/javascript/scripts/getPosts.js" />
		</script>
		
	</jsp:attribute>
    
    <jsp:body>

		<jsp:include page="/WEB-INF/pages/navbar.jsp" />
		
		<!-- c:out value="${sessionScope.user.id_user}" /-->

    </jsp:body>
        
</t:template>



