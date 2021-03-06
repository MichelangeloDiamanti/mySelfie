/* Popup stile bootstrap navigatore file    */
$("#input-id").fileinput({
	allowedFileTypes: ["image"],
	'showUpload':false,
	'previewFileType':'any'
});

		
/* Effetto immagini in background */
$(document).ready(function() 
{
	var photo=2, prevPhoto=1;
	setInterval(function() 
	{ 
		$("#blur_img_" + photo).fadeTo( 2000 , 1);
		$("#blur_img_" + prevPhoto).fadeTo( 2000 , 0);
		photo++;
		prevPhoto++;
		if(photo>=9) photo=1;
		if(prevPhoto>=9) prevPhoto=1;
	}, 10000);
});


/* Apre il form di registrazione (SignUp) */
var InOut=true;
function showsignupform() {
	if(InOut) {
		document.getElementById("signup_form").style.display= "block"; 
		$("#signup_form").animate({right: "-150px",}, 350 );			
	}
	if(!InOut) $("#signup_form").animate({right: "-500px",}, 250 , function(){ 
		document.getElementById("signup_form").style.display= "none"; 
		});					 
		
	InOut=!InOut;
}	


/* Controllo username già in uso */		
$("#suusername").on({
	'change':function () { 
		var my_txt = $(this).val();
		var len = my_txt.length;
		if(len > 0) {
			$.ajax({
				url : '/mySelfie/index/checkUsername',
				data : {
					username : $('#suusername').val(),
					reqType : "checkUsr"
				},
				success : function(responseText) {
					$('#suusername').css({ borderBottomLeftRadius: 0, borderBottomRightRadius: 0});
					if(responseText==="true")
						$('#usernameAlert').html(
								"<div class=\"alert alert-success\" role=\"alert\">" +
								"  <span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\"></span>" +
								"  <span class=\"sr-only\">Error:</span>" +
								"  username available" +
								"</div>"			
						);
					else 
						$('#usernameAlert').html(
								"<div class=\"alert alert-danger\" role=\"alert\">" +
								"  <span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\"></span>" +
								"  <span class=\"sr-only\">Error:</span>" +
								"  username not available" +
								"</div>"		
						);
				}
			});
		}
		else {
			$('#usernameAlert').html("");
		}
	}
});

/* Controllo email già in uso */		
$("#email").on({
	'change':function () { 
		var my_txt = $(this).val();
		var len = my_txt.length;
		if(len > 0) {
			$.ajax({
				url : '/mySelfie/index/checkEmail',
				data : {
					email : $('#email').val(),
					reqType : "checkEmail"
				},
				success : function(responseText) {
					$('#email').css({ borderBottomLeftRadius: 0, borderBottomRightRadius: 0});
					if(responseText==="true")
						$('#emailAlert').html(
								"<div class=\"alert alert-success\" role=\"alert\">" +
								"  <span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\"></span>" +
								"  <span class=\"sr-only\">Error:</span>" +
								"  email available" +
								"</div>"			
						);
					else 
						$('#emailAlert').html(
								"<div class=\"alert alert-danger\" role=\"alert\">" +
								"  <span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\"></span>" +
								"  <span class=\"sr-only\">Error:</span>" +
								"  email not available" +
								"</div>"		
						);
				}
			});
		}
		else {
			$('#emailAlert').html("");
		}
	}
});
	

/* Controlli input */
function checkInput() {
	
	document.getElementById('suusername').parentNode.className = "col-md-8";
	document.getElementById('supassword').parentNode.className = "col-md-8";
	document.getElementById('suchkpassword').parentNode.className = "col-md-8";
	
	var chkS = false;
	var chkP = false;
	
	//controlla se ci sono spazi nello username
	var u = document.getElementById('suusername').value;
	if(u.indexOf(' ') != -1)
	{
		document.getElementById('suusername').parentNode.className += " has-error";
		document.getElementById('suusername').value = u;
		toastr.error('Blank spaces are not permitted', 'Registration failed');
		
		chkS = false;
	}
	else
		chkS = true;
	
	//controlla se le password sono uguali
	var p = document.getElementById('supassword').value;
	var cp = document.getElementById('suchkpassword').value;
	if(p === cp)
	{
		chkP = true;
	}
	else 
	{
		toastr.error('Passwords don\'t match', 'Registration failed');
		var d = document.getElementById('supassword').parentNode;
		var cd = document.getElementById('suchkpassword').parentNode;
		d.className += " has-error";
		cd.className += " has-error";		
		document.getElementById('supassword').value = p;
		document.getElementById('suchkpassword').value = cp;
	  	chkP = false;
	}
	
	if(chkS && chkP)
		return true;
	else 
		return false;
}

/* Controlli input per la form reset password*/
function checkInputRP() {

	//controlla se le password sono uguali
	var p = document.getElementById('rpassword').value;
	var cp = document.getElementById('rchkpassword').value;
	if(p === cp)
	{
		return true;
	}
	else 
	{
		toastr.error('Passwords don\'t match', 'Reset failed');
		document.getElementById('rpassword').value = p;
		document.getElementById('rchkpassword').value = cp;
		return false;
	}
}


/* Funzione login dinamico      */
$(document).ready(function() {
	$('#formlogin').submit(function() {
		$.ajax( {
			method: "POST",
			url : '/mySelfie/userValidator',
			data : { 
				action: "login",
				username: $('#username').val(), 
				password: $('#password').val(), 
				redURL: $('#redURL').val(),
				remMe : $("#rm:checked").val(),
			},
			success : function(responseText) {
				
				if(responseText === "loginFAIL") {
					toastr.error('credentials are not valid', 'Error on LogIn');
					document.getElementById("usernameContainer").className += " has-error";
					document.getElementById("passwordContainer").className += " has-error";
				}
				else {
					window.location = responseText;
				}
			}
		});
	});
});




//funzione che mostra la form per resettare le credenziali
function showNewCredentialsForm()
{
	var resetCredentialsForm = "<form id=\"formResetPassword\" action=\"/mySelfie/resetCredentials\" method=\"post\" >"
							 + "<div id=\"emailContainer\">"
							 + "<label>If you can't remember your password, you only have to tell us your e-mail address. You will receive a mail with a link to a page where you can reset you password.</label>"
							 + "</div>"
							 + "<input type=\"email\" required=\"required\" name=\"email\" id=\"username\" class=\"textbox form-control\" placeholder=\"Insert your email address\">"	
							 + "<button type=\"submit\" name=\"logIn\" id=\"loginbtn\"> Send </button>"
							 + "<label id=\"rttlp\" onClick=\"window.location='/mySelfie/'\">Back</label>"
							 + "<input type=\"hidden\" name=\"action\" value=\"sendMail\" />"
							 + "</form>";
									 
	$('#login_form').html(resetCredentialsForm);
}


