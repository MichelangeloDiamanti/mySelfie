<form id="formlogin" method="post" onSubmit="return false;">
	<div id="usernameContainer">
		<input type="text" name="username" id="username" class="textbox form-control" placeholder="Username">	
	</div>
	<div id="passwordContainer">
		<input type="password" name="password" id="password" class="textbox form-control" placeholder="Password">
	</div>
	<button type="submit" name="logIn" id="loginbtn"> LogIn </button>
	<input type="checkbox" name="rememberme" id="rm">
	<label id="rmlbl" for="rm">Remember me</label>
	<label id="signup" onClick="showsignupform()"> SignUp </label>
</form>