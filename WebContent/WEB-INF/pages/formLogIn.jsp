<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form id="formlogin" method="post" onSubmit="return false;">
	<label id="usernamelbl" >Username:</label>
	<div id="usernameContainer">
		<input type="text" name="username" id="username" class="textbox form-control" placeholder="Username">	
	</div>
	<label id="passwordlbl" >Password:</label>
	<div id="passwordContainer">
		<input type="password" name="password" id="password" class="textbox form-control" placeholder="Password">
	</div>
	<button type="submit" name="logIn" id="loginbtn"> LogIn </button>
	
	<input type="checkbox" name="rm" id="rm">
	<label id="rmlbl" for="rm">Remember me</label>
	
	<label id="signup" onClick="showsignupform()"> SignUp </label>
	<label id="forgot" onClick="showNewCredentialsForm()"> Forgot password? </label>
	<!-- riceve come attributo l'URL a cui si deve essere reindirizzati dopo il corretto login -->
	<input type="hidden" id="redURL" name="redURL" value="<c:out value="${redURL}"/>">
</form>