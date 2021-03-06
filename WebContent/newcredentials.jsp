<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:template>

	<jsp:attribute name="header">
		<link
			href="${pageContext.request.contextPath}/resources/css/index.css"
			media="all" rel="stylesheet" type="text/css" />            
        
        <!-- se si proviene dalla registrazione viene incluso il css dei toast message -->

        <link href="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css" rel="stylesheet" />


    
    </jsp:attribute>

	<jsp:attribute name="javascripts">
				
		<!-- se c'è un messaggio toast da visualizzare -->
		<c:if test="${not empty requestScope.toastMessage}">
			<script src="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
			<c:choose> 
			  <c:when test="${requestScope.toastMessage.type == 'success'}">
			  	<script>
			    	toastr.success("${requestScope.toastMessage.body}", "${requestScope.toastMessage.title}");
			   	</script>
			   </c:when>
			  <c:when test="${requestScope.toastMessage.type == 'fail'}">
			  	<script>
			    	toastr.error("${requestScope.toastMessage.body}", "${requestScope.toastMessage.title}");
			   	</script>
			  </c:when>
			  <c:when test="${requestScope.toastMessage.type == 'info'}">
			  	<script>
			    	toastr.info("${requestScope.toastMessage.body}", "${requestScope.toastMessage.title}");
			   	</script>
			  </c:when>
			</c:choose>
		</c:if>

		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/scripts/newCredentials.js"></script>
		
	</jsp:attribute>

	<jsp:body>
		
		<!-- contiene le immagini di sfondo -->
		<div id="blur_img_container">
			<div id="blur_img_1" class="blur_img"></div>
			<div id="blur_img_2" class="blur_img"></div>
			<div id="blur_img_3" class="blur_img"></div>
			<div id="blur_img_4" class="blur_img"></div>
			<div id="blur_img_5" class="blur_img"></div>
			<div id="blur_img_6" class="blur_img"></div>
			<div id="blur_img_7" class="blur_img"></div>
			<div id="blur_img_8" class="blur_img"></div>
			<div id="black"></div>
		</div>
		
		<div id="globalContainer">
		
			<!-- titolo -->
			<img id="title"
				src="${pageContext.request.contextPath}/resources/images/bigtitle.png">
	
			<!-- logo -->
			<img id="logo"
				src="${pageContext.request.contextPath}/resources/images/my.png"></img>
			
			<!-- form di login -->
			<div id="login_form">
				<jsp:include page="/WEB-INF/pages/newCredentialsForm.jsp" />
			</div>
			
		</div> 
	
    </jsp:body>

</t:template>



