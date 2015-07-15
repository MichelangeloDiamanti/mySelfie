<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form id="formResetPassword" action="/mySelfie/resetCredentials" method="post" onSubmit="return checkInputRP()">

		<label id="npass">New password:</label>
		<input type="password" required="required" name="password" id="rpassword" class="textbox form-control" placeholder="New password">	
	
		<label id="cnpass">Check new password:</label>
		<input type="password" required="required" name="confirmPassword" id="rchkpassword" class="textbox form-control" placeholder="Check new password">

	<button type="submit" name="logIn" id="setpassbtn"> Set Password </button>
	
	<!-- riceve come attributo l'URL a cui si deve essere reindirizzati dopo il corretto login -->
	<input type="hidden" id="redURL" name="redURL" value="<c:out value="${redURL}"/>"/>
	<input type="hidden" name="secret" value="<c:out value="${requestScope.secret}"/>"/>
	<input type="hidden" name="action" value="newCredentials"/>
</form>