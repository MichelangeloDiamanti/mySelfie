<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form id="formResetPassword" action="/mySelfie/resetCredentials" method="post" >
	<div id="usernameContainer">
		<input type="email" name="email" id="username" class="textbox form-control" placeholder="Insert your email address">	
	</div>
	<button type="submit" name="logIn" id="loginbtn"> LogIn </button>
	
	<!-- riceve come attributo l'URL a cui si deve essere reindirizzati dopo il corretto login -->
	<input type="hidden" id="redURL" name="redURL" value="<c:out value="${redURL}"/>"/>
	<input type="hidden" name="action" value="sendMail"/>
</form>