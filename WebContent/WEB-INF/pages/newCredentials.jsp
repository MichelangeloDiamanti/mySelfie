<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form id="formResetPassword" action="/mySelfie/resetCredentials" method="post" >
	<div id="usernameContainer">
		<input type="password" name="password" id="username" class="textbox form-control" placeholder="Password">	
	</div>
	<div id="passwordContainer">
		<input type="password" name="confirmPassword" id="password" class="textbox form-control" placeholder="Confirm Password">
	</div>
	<button type="submit" name="logIn" id="loginbtn"> LogIn </button>
	
	<!-- riceve come attributo l'URL a cui si deve essere reindirizzati dopo il corretto login -->
	<input type="hidden" id="redURL" name="redURL" value="<c:out value="${redURL}"/>"/>
	<input type="hidden" name="secret" value="<c:out value="${requestScope.secret}"/>"/>
	<input type="hidden" name="action" value="newCredentials"/>
</form>