<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    
    <jsp:attribute name="header">
		<link href="${pageContext.request.contextPath}/resources/css/navbar.css" media="all" rel="stylesheet" type="text/css" />
    	<link href="${pageContext.request.contextPath}/resources/css/leftMenu.css" media="all" rel="stylesheet" type="text/css" />
    	<link href="${pageContext.request.contextPath}/resources/css/notifications.css" media="all" rel="stylesheet" type="text/css" />
    	<link href="${pageContext.request.contextPath}/resources/css/postTable.css" media="all" rel="stylesheet" type="text/css" />
    	<link href="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css" rel="stylesheet" />
    </jsp:attribute>
    
    <jsp:attribute name="javascripts">	

		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/navbar.js"></script>
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/notifications.js"></script>
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/leftMenu.js"></script>
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/comments.js"></script>
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/openPost.js"></script>
		<script src="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
		
	</jsp:attribute>
    
    <jsp:body>

		<jsp:include page="/WEB-INF/pages/navbar.jsp" />

		<jsp:include page="/WEB-INF/pages/leftMenu.jsp" /> 
		
		<div id="oldAndNewNotifications">				
		</div>


		<!-- Modal -->
		<div class="modal fade" id="modalTable" tabindex="-1" role="dialog" aria-labelledby="">
			<div class="modal-dialog" id="modalTableContent" role="document">
			</div>
		</div>

    </jsp:body>
        
</t:template>



