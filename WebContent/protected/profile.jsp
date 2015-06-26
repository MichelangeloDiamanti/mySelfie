<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    
    <jsp:attribute name="header">
		<link href="${pageContext.request.contextPath}/resources/css/navbar.css" media="all" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/resources/css/profile.css" media="all" rel="stylesheet" type="text/css" />            
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
		    	autoclose: true
		    });
		</script>		
		
		
	</jsp:attribute>
    
    <jsp:body>

		<jsp:include page="/WEB-INF/pages/navbar.jsp" />
    
	    <div id="postsContainer">
	  		${requestScope.profilePosts}
	    </div>
		
		
    
    	<!-- div id="modifyProfile">
    		<jsp:include page="/WEB-INF/pages/formModifyProfile.jsp" />
    	</div-->

		
		
    </jsp:body>
        
</t:template>



