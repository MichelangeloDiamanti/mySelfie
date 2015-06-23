<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:template>

	<jsp:attribute name="header">
		<link
			href="${pageContext.request.contextPath}/resources/css/formSignUp.css"
			media="all" rel="stylesheet" type="text/css" />
		<link
			href="${pageContext.request.contextPath}/resources/css/index.css"
			media="all" rel="stylesheet" type="text/css" />            
        
        <!-- se si proviene dalla registrazione viene incluso il css dei toast message -->

        <link
			href="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css"
			rel="stylesheet" />

    
    </jsp:attribute>

	<jsp:attribute name="javascripts">
		
		<script
			src="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
		<!-- se si proviene dalla registrazione viene visualizzato un toast message -->
		<c:if test="${param.status == 'success' }">
			<script>
				toastr.success('welcome aboard!', 'Registration successful');
			</script>
		</c:if>

		<c:if test="${param.status == 'fail' }">
		    <script
				src="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
		    <c:if test="${param.reason == 'nickNameInUseException' }">
				<script>
					toastr.error('nickname already in use',
							'Registration failed');
				</script>
		    </c:if>
		    <c:if test="${param.reason == 'badInput' }">
				<script>
					toastr.error('invalid data', 'Registration failed');
				</script>
		    </c:if>
		</c:if>
		
		<!-- plugin input file -->
		<script
			src="${pageContext.request.contextPath}/resources/javascript/fileInput/fileinput.js"></script>
		
		<script
			src="${pageContext.request.contextPath}/resources/javascript/scripts/index.js"></script>
		

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
				<jsp:include page="/WEB-INF/pages/formLogIn.jsp" />
			</div>
			
		</div>
		
		<!-- form di registrazione -->
		<div id="signup_form">
			<div id="signup_form_container">
				<jsp:include page="/WEB-INF/pages/formSignUp.jsp" />
			</div>
		</div>    
    
	
    </jsp:body>

</t:template>



