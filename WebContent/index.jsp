<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    
    <jsp:attribute name="header">
		<link href="${pageContext.request.contextPath}/resources/css/formSignUp.css" media="all" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/resources/css/index.css" media="all" rel="stylesheet" type="text/css" />            
        
        <!-- se si proviene dalla registrazione viene incluso il css dei toast message -->

        <link href="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css" rel="stylesheet" />

    
    </jsp:attribute>
    
    <jsp:attribute name="javascripts">
		
		<script src="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
		<!-- se si proviene dalla registrazione viene visualizzato un toast message -->
		<c:if test="${param.status == 'success' }">
			<script>toastr.success('welcome aboard!', 'Registration successful');</script>
		</c:if>

		<c:if test="${param.status == 'fail' }">
		    <script src="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
		    <c:if test="${param.reason == 'nickNameInUseException' }">
				<script>toastr.error('nickname already in use', 'Registration failed');</script>
		    </c:if>
		    <c:if test="${param.reason == 'badInput' }">
				<script>toastr.error('invalid data', 'Registration failed');</script>
		    </c:if>
		</c:if>
		
		<!-- plugin input file -->
		<script src="${pageContext.request.contextPath}/resources/javascript/fileInput/fileinput.js"></script>
		<script type="text/javascript">
			// with plugin options
			$("#input-id").fileinput({
				allowedFileTypes: ["image"],
				'showUpload':false,
				'previewFileType':'any'
				});
		</script>
		
		<!-- sezione custom scripts -->
		<script type="text/javascript">
			/* cambia immagini di sfondo */
			$(window).load(function blurbgchange()
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
			
			/* apre sezione registrazione onclick */
			var InOut=true;
			function showsignupform()
			{
				if(InOut)
				{
					document.getElementById("signup_form").style.display= "block"; 
					$("#signup_form").animate({left: "75%",}, 350 );			
				}
		
				if(!InOut)
					$("#signup_form").animate({left: "100%",}, 250 , function(){ document.getElementById("signup_form").style.display= "none"; });					 
		
				InOut=!InOut;
			}	
			
		</script>	
		
		<script type="text/javascript">
		
			$("#nickname").on({
				'change':function () 
				{ 
					var my_txt = $(this).val();
					var len = my_txt.length;
					if(len > 0)
					{
					 	$.ajax({
							url : 'homepage/checkNickname',
							data : {
								nickName : $('#nickname').val(),
								reqType : "checkNick"
							},
							success : function(responseText) {
								$('#nickname').css({ borderBottomLeftRadius: 0, borderBottomRightRadius: 0});
								if(responseText==="true")
									$('#nicknameAlert').html(
											"<div class=\"alert alert-success\" role=\"alert\">" +
											"  <span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\"></span>" +
											"  <span class=\"sr-only\">Error:</span>" +
											"  username available" +
											"</div>"			
									);
								else 
									$('#nicknameAlert').html(
											"<div class=\"alert alert-danger\" role=\"alert\">" +
											"  <span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\"></span>" +
											"  <span class=\"sr-only\">Error:</span>" +
											"  username not available" +
											"</div>"		
									);
							}
						});
					}else{
						$('#nicknameAlert').html("");
					}
			}
		});
			
		</script>
		
		
		<script type="text/javascript">
			
			function checkPwd()
			{
				var p = document.getElementById('supassword').value;
				var cp = document.getElementById('suchkpassword').value;
				if(p === cp)
				{
				    return true;
				}
				else
				{
					toastr.error('Passwords don\'t match', 'Registration failed');
					var d = document.getElementById('supassword').parentNode;
					var cd = document.getElementById('suchkpassword').parentNode;
					
					var errtxt = d.innerHTML;
					errtxt = errtxt + "<span class=\"glyphicon glyphicon-remove form-control-feedback\" style=\"padding-right:50px;\"></span>";					
					d.innerHTML = errtxt;
					
					errtxt = cd.innerHTML;
					errtxt = errtxt + "<span class=\"glyphicon glyphicon-remove form-control-feedback\" style=\"padding-right:50px;\"></span>";					
					cd.innerHTML = errtxt;
					
					d.className += " has-error";
					cd.className += " has-error";
					
					document.getElementById('supassword').value = p;
					document.getElementById('suchkpassword').value = cp;
					/* alert("ciao"); */
	  				return false;
				}
				
				
			}
			
		</script>	
		
		<script type="text/javascript">
	
	    	$(document).ready(function() 
	    	{
				$('#formlogin').submit(function() 
				{
					$.ajax(
					{
						method: "POST",
						url : '/mySelfie/userValidator',
						data : 
						{ 
							username: $('#username').val(), 
							password: $('#password').val(), //le password non dovrebbero viaggiare in chiaro 
							redURL: $('#redURL').val()
						},
						success : function(responseText) 
						{
							if(responseText === "loginFAIL")
							{
								toastr.error('credentials are not valid', 'Error on LogIn');
								document.getElementById("usernameContainer").className += " has-error";
								document.getElementById("passwordContainer").className += " has-error";
							}
							else
							{
								window.location = responseText;
							}
						}
					});
				});
			});
		</script>

	</jsp:attribute>
    
    <jsp:body>
		
		<!-- contiene le immagini di sfondo -->
		<div id="blur_img_container">
			<div id="blur_img_1" class="blur_img"></div>
			<div id="blur_img_2" class="blur_img"></div>
			<div id="blur_img_3" class="blur_img"></div>
			<div id="blur_img_4" class="blur_img"></div>
			<div id="blur_img_5" class="blur_img"></div>
			<div id="blur_img_6" class="blur_img"></div>
			<div id="blur_img_7" class="blur_img"></div>
			<div id="blur_img_8" class="blur_img"></div>
			<div id="black"></div>
			<!-- titolo -->
			<img id="title" src="${pageContext.request.contextPath}/resources/images/bigtitle.png">
		</div>
		<!-- logo -->
		<img id="logo" src="${pageContext.request.contextPath}/resources/images/my.png"></img>
		<!--p id="slogan"> Where you can share your best selfies with the world </p-->
		
		<!-- form di login -->
		<div id="login_form">
			<jsp:include page="/WEB-INF/pages/formLogIn.jsp" />
		</div>
		
		<!-- form di registrazione -->
		<div id="signup_form">
			<div id="signup_form_container">
				<jsp:include page="/WEB-INF/pages/formSignUp.jsp" />
			</div>
		</div>    
    
	
    </jsp:body>
        
</t:template>



