<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@attribute name="header" fragment="true" %>
<%@attribute name="javascripts" fragment="true" %>

<html>
  
  <head>
  	
  	<title>mySelfie</title>
  	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/my.ico" />
  	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/bootstrap/bootstrap.css" />
  	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common.css" />
  	<jsp:invoke fragment="header"/>
  
  </head>
  
  <body>
    
	<div id="bcontainer" class="container">
      <jsp:doBody/>
  	</div>
  </body>

  <script type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/jquery-1.11.3.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/bootstrap/bootstrap.js"></script>
  <jsp:invoke fragment="javascripts"/>
    
</html>