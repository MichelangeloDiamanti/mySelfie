<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:template>
    
    <jsp:attribute name="header">
		<link href="${pageContext.request.contextPath}/resources/css/navbar.css" media="all" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/resources/css/profile.css" media="all" rel="stylesheet" type="text/css" />            
		<link href="${pageContext.request.contextPath}/resources/css/postTable.css" media="all" rel="stylesheet" type="text/css" />            
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
		
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/openPost.js"></script>
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/comments.js"></script>
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/follow.js"></script>
		
	</jsp:attribute>
    
    <jsp:body>

		<jsp:include page="/WEB-INF/pages/navbar.jsp" />

		<!-- se si tratta del profilo dell'utente stesso non fa vedere il bottone follow
			 ... seguire se stessi è patetico!
		 -->
		<c:if test="${!requestScope.myProfile}">
			<c:choose> 
			  <%-- se l'utente segue gia il profilo viene mostrato unfollow --%>
			  <c:when test="${requestScope.following}">
			  	<form id="unfollow_form" class="form-inline" onsubmit="return false">
			    	<button id="unfollow_btn" style="position:relative; float:right;">unfollow!</button>
			    	<input type="hidden" name="profile_id" value="${requestScope.profileId}">
			    </form>
			  </c:when>
			  <%-- se l'utente NON segue il profilo viene mostrato follow --%>
			  <c:otherwise>
			  	<form id="follow_form" class="form-inline" onsubmit="return false">
			    	<button id="follow_btn" style="position:relative; float:right;">follow!</button>
			    	<input type="hidden" name="profile_id" value="${requestScope.profileId}">
			    </form>
			  </c:otherwise>
			</c:choose>
		</c:if>

	    <div id="postsContainer">
	  		${requestScope.profilePosts}
	    </div>
    
    	<!-- div id="modifyProfile">
    		<jsp:include page="/WEB-INF/pages/formModifyProfile.jsp" />
    	</div-->

		
		
    </jsp:body>
        
</t:template>



