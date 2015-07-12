<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:template>
	
	<jsp:attribute name="header">
	
		<link href="${pageContext.request.contextPath}/resources/css/navbar.css" media="all" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/resources/css/formModifyProfile.css" media="all" rel="stylesheet" type="text/css" />
		<link href="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css" rel="stylesheet" />
		
	</jsp:attribute>
	

	<jsp:attribute name="javascripts">
	
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/navbar.js"></script>
	
		<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=places"></script>
		<script type="text/javascript">
			var city=document.getElementById('city');
			var autocompleteCity = new google.maps.places.Autocomplete(city);
		</script>

		<script src="${pageContext.request.contextPath}/resources/javascript/fileInput/fileinput.js"></script>
		<script type="text/javascript">
			// initialize with defaults
			$("#input-id").fileinput();
			// with plugin options
			$("#input-id").fileinput({'showUpload':false, 'previewFileType':'any'});
		</script>
		
		<script src="${pageContext.request.contextPath}/resources/javascript/datePicker/bootstrap-datepicker.js"></script>
		<script type="text/javascript">
			$('#my-datepicker').datepicker({
			orientation: 'auto top',
			autoclose: true,
			format: 'yyyy/mm/dd'
			});
		</script>
	
		<script	src="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
		
		<!-- se si proviene dalla registrazione viene visualizzato un toast message -->
		<c:if test="${param.status == 'success' }">
			<script>
				toastr.success('', 'Update successful');
			</script>
		</c:if>

		<c:if test="${param.status == 'fail' }">
		    <script src="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
		    <c:if test="${param.reason == 'incorrect_password' }">
				<script>
					toastr.error('incorrect password', 'Update failed');
				</script>
		    </c:if>
		    <c:if test="${param.reason == 'passwords_dont_match' }">
				<script>
					toastr.error("Passwords don't match", "Update failed");
				</script>
		    </c:if>
		    <c:if test="${param.reason == 'invalid_username' }">
				<script>
					toastr.error("invalid username", "Update failed");
				</script>
		    </c:if>
		</c:if>

	
	
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/modifyProfile.js"></script>
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/selfieAnimation.js"></script>
		
	</jsp:attribute>
	
	
	<jsp:body>
		
		<jsp:include page="/WEB-INF/pages/navbar.jsp" />
		
			<label id="mpTitle">
				<span id="wrench" class="glyphicon glyphicon-wrench"></span>
				Modify your informations:
			</label>
			<div id="modifyProfile">
				<jsp:include page="/WEB-INF/pages/formModifyProfile.jsp" />
			</div>
			
			<div>
				<img src="${pageContext.request.contextPath}/resources/images/floor.png" id="floor" >
				<img src="${pageContext.request.contextPath}/resources/images/group.png" id="group" >
				<img src="${pageContext.request.contextPath}/resources/images/you.png"   id="you"   >
				<img src="${pageContext.request.contextPath}/resources/images/flash.png" id="flash" >
			</div>
		
		</jsp:body>
	
</t:template>