<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="custom-bootstrap-menu" class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/homepage.jsp">
        		<img id="logo" alt="Brand" src="${pageContext.request.contextPath}/resources/images/extendedLogo.png">
        	</a>
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-menubuilder"><span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
            </button>
        </div>
        <div class="collapse navbar-collapse navbar-menubuilder">
            <ul class="nav navbar-nav navbar-left">
                <li>
	                <form action="search/" method="post" class="form-inline" role="form">
	                	<div id="searchdiv">
							<input type="text" id="search"/>
							<button id="searchbtn">
								<img id="magnifierGlass" src="${pageContext.request.contextPath}/resources/images/search.png"></img>
							</button>
							<div id="separator"></div>
						</div>
					</form>
                </li>
            </ul>
      			<div id="buttons">
        <ul class="nav navbar-nav navbar-right">
                <li>
                	<div id="updiv" class="iconsdiv">
						<div class="circle"></div>
						<a  href="${pageContext.request.contextPath}/uploadselfie.jsp" >
							<img id="upload" class="icon" src="${pageContext.request.contextPath}/resources/images/upload.png"></img>
						</a>
					</div>
                </li>
                <li>
                	<div id="homediv" class="iconsdiv">
						<div class="circle"></div>
						<a  href="${pageContext.request.contextPath}/homepage.jsp" >
							<img id="home" class="icon" src="${pageContext.request.contextPath}/resources/images/home.png"></img>
						</a>
					</div>
                </li>       
                <li>
                	<a href="${pageContext.request.contextPath}/profile.jsp">
						<span id="profilepicdiv" style="background-image: url('${pageContext.request.contextPath}/protected/resources/images/profilePics/<c:out value="${sessionScope.user.profilepic}" />')" ></span>
                	</a>
                </li>                           
            </ul>
            </div>
        </div>
    </div>
</div>
