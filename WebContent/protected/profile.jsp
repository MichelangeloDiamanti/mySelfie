<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    
    <jsp:attribute name="header">
		<link href="${pageContext.request.contextPath}/resources/css/navbar.css" media="all" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/resources/css/profile.css" media="all" rel="stylesheet" type="text/css" />            
		<link href="${pageContext.request.contextPath}/resources/css/postTable.css" media="all" rel="stylesheet" type="text/css" />            
    </jsp:attribute>
    
    <jsp:attribute name="javascripts">
    
    	<script src="${pageContext.request.contextPath}/resources/javascript/scripts/navbar.js"></script>	
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/openPost.js"></script>
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/comments.js"></script>
		
	</jsp:attribute>
    
    <jsp:body>

		<jsp:include page="/WEB-INF/pages/navbar.jsp" />
    
    
    	<button id="follow_btn" class="btn btn-primary btn-lg">Follow</button>
    	
    	 
	    <div id="postsContainer">
	  		${requestScope.profilePosts}
	    </div>
		
		
    		
		
    </jsp:body>
        
</t:template>



