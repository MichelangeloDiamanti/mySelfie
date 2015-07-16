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
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/openPost.js"></script>
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/comments.js"></script>
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/follow.js"></script>
			
	</jsp:attribute>
    
    <jsp:body>

		<jsp:include page="/WEB-INF/pages/navbar.jsp" />



		<div id="infoContainer">
		
			<span id="profilepicbig" style="background-image: url('${pageContext.request.contextPath}/protected/resources/profilepics/<c:out value="${requestScope.profilePic}" />')" ></span>

			<label id="profileUsername">${requestScope.profileOwner}</label>

			<!-- se si tratta del profilo dell'utente stesso non fa vedere il bottone follow
				 ... seguire se stessi è patetico!
			 -->
			<c:if test="${!requestScope.myProfile}">
				<c:choose> 
				  <%-- se l'utente segue gia il profilo viene mostrato unfollow --%>
				  <c:when test="${requestScope.follow}">
				  	<form id="unfollow_form" class="form-inline" onsubmit="return false">
				    	<button id="unfollow_btn" class="btn btn-xs" >
				    		<span class="glyphicon glyphicon-eye-close un-followbtn"></span>
				    		unfollow
				    	</button>
				    	<input type="hidden" name="profile_id" value="${requestScope.profileId}">
				    </form>
				  </c:when>
				  <%-- se l'utente NON segue il profilo viene mostrato follow --%>
				  <c:otherwise>
				  	<form id="follow_form" class="form-inline" onsubmit="return false">
				    	<button id="follow_btn" class="btn btn-xs">
				    		<span class="glyphicon glyphicon-eye-open un-followbtn"></span>
							follow
				    	</button>
				    	<input type="hidden" name="profile_id" value="${requestScope.profileId}">
				    </form>
				  </c:otherwise>
				</c:choose>
			</c:if>
			<c:if test="${requestScope.myProfile}">
				<br><br>
			</c:if>
		
					
			<table id="infolbl">
				<tr>
					<td>
						<label id="followerslbl">Followers:</label>
					</td>
					<td>
						<label class="lblvalue">${requestScope.followers}</label>
					</td>
				</tr>	
				<tr>
					<td>
						<label id="followinglbl">Following:</label>
					</td>
					<td>
						<label class="lblvalue">${requestScope.following}</label>
					</td>
				</tr>
				<tr>
					<td>
						<label id="postslbl">Posts:</label>
					</td>
					<td>
						<label class="lblvalue">${requestScope.nposts}</label>
					</td>
				</tr>			
			</table>				
		
		</div>

		<c:if test="${requestScope.profileNotes != NULL && requestScope.profileNotes != ''}">
			<label id="userNotesLbl">Notes:</label>
			<div id="userNotes">
		  		${requestScope.profileNotes}
			</div>
		</c:if>

	    <div id="postsContainer">
	  		${requestScope.profilePosts}
	    </div>
		
		<!-- Modal -->
		<div class="modal fade" id="modalTable" tabindex="-1" role="dialog" aria-labelledby="">
			<div class="modal-dialog" id="modalTableContent" role="document">
			</div>
		</div>

    </jsp:body>
        
</t:template>



