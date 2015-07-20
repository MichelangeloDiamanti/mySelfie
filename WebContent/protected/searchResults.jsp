<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:template>
    
    <jsp:attribute name="header">
		<link href="${pageContext.request.contextPath}/resources/css/navbar.css" media="all" rel="stylesheet" type="text/css" />
		<link href="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css" rel="stylesheet" />
       	<link href="${pageContext.request.contextPath}/resources/css/leftMenu.css" media="all" rel="stylesheet" type="text/css" />
       	<link href="${pageContext.request.contextPath}/resources/css/searchResults.css" media="all" rel="stylesheet" type="text/css" />
       	<link href="${pageContext.request.contextPath}/resources/css/postTable.css" media="all" rel="stylesheet" type="text/css" />
    </jsp:attribute>
    
    <jsp:attribute name="javascripts">	

		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/navbar.js"></script>
		<script src="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/leftMenu.js"></script>
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/openPost.js"></script>
		<script src="${pageContext.request.contextPath}/resources/javascript/scripts/comments.js"></script>

	</jsp:attribute>
    
    <jsp:body>

		<jsp:include page="/WEB-INF/pages/navbar.jsp" />
		<jsp:include page="/WEB-INF/pages/leftMenu.jsp" /> 
		
		<c:if test="${empty requestScope.userMatches && empty requestScope.selfieMatches && empty requestScope.hashTagMatches}">
			<label class="search_empty_label">No results found</label></div>
		</c:if>
		
		<c:if test="${not empty requestScope.userMatches || not empty requestScope.selfieMatches || not empty requestScope.hashTagMatches}">
			<table id="searchResTable">
			
				<tr id="searchheader">
					<th class="searchHead searchLine">Users</th>
					<th class="searchHead searchLine">Selfies</th>
					<th class="searchHead">Hashtags</th>
				</tr>
				
				
				
				<tr>
					<td class="searchUser searchLine">	
						<ul id="searchUserList">
						
							<c:forEach var="user" items="${requestScope.userMatches}">
						    	<li class="searchUserItem">
							    	<a class="searchProfileA" href="${pageContext.request.contextPath}/protected/profile/${user.username}">
							    		<span class="searchProfilepic" style="background-image: url('${pageContext.request.contextPath}/protected/resources/profilepics/${user.profilepic}')" ></span>
							    		<label class="searchUsername"> ${user.username} </label>	
									</a>
						    		<div class="searchUserInfo">					    	
						    			<label class="searchUserInfos"> ${user.name} ${user.surname} 
						    				<c:if test="${user.city!=null && user.city!=''}">,</c:if> ${user.city} 
						    			</label>					    	
						    		</div>
						    	</li>
							</c:forEach>
	
						</ul>
					</td>	
	
					<td class="searchSelfie searchLine">	
						<c:forEach var="selfie" items="${requestScope.selfieMatches}">
					    	<span id="selfie-${selfie.id_selfie}" class="searchThumbnail" style="background-image: url('${pageContext.request.contextPath}/protected/resources/selfies/compressedSize/${selfie.picture}')" data-toggle="modal" data-target="#modalTable" onClick="openIMG(this)"></span>
						</c:forEach>
					</td>	
	
					<td class="searchHashtag">
						<ul id="searchHashtagList">					
							<c:forEach var="hashtag" items="${requestScope.hashTagMatches}">
								<li class="searchUserItem">
									<a class="hashtag_link" href="${pageContext.request.contextPath}/protected/hashtag/${fn:substringAfter(hashtag.name, '#')}">
										${hashtag.name}
									</a>
								</li>
							</c:forEach>
						</ul>
					</td>	
	
				</tr>
				
						
			</table>
			
			
			<!-- Modal -->
			<div class="modal fade" id="modalTable" tabindex="-1" role="dialog" aria-labelledby="">
				<div class="modal-dialog" id="modalTableContent" role="document">
				</div>
			</div>
		
		</c:if>
				
    </jsp:body>
        
</t:template>



