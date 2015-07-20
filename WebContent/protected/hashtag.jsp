<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    
    <jsp:attribute name="header">
		<link href="${pageContext.request.contextPath}/resources/css/navbar.css" media="all" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/resources/css/postTable.css" media="all" rel="stylesheet" type="text/css" />    
    	<link href="${pageContext.request.contextPath}/resources/css/leftMenu.css" media="all" rel="stylesheet" type="text/css" />
    	<link href="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css" rel="stylesheet" />
    </jsp:attribute>
    
    <jsp:attribute name="javascripts">	

		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/navbar.js"></script>
		<script src="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>

		<script type="text/javascript">
			<jsp:include page="/resources/javascript/scripts/hashtag.js" />
			<jsp:include page="/resources/javascript/scripts/comments.js" />
			<jsp:include page="/resources/javascript/scripts/leftMenu.js" />
		</script>
		
	</jsp:attribute>
    
    <jsp:body>

		<jsp:include page="/WEB-INF/pages/navbar.jsp" />

		<jsp:include page="/WEB-INF/pages/leftMenu.jsp" /> 
		
		<div id="postsContainer">
	  		${requestScope.hashtagPosts}
	    </div>

    </jsp:body>
        
</t:template>



