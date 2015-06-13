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
			<script>toastr.success('ora sei dentro!', 'registrazione effettuata');</script>
		</c:if>

		<c:if test="${param.status == 'fail' }">
		    <script src="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
		    <c:if test="${param.reason == 'nickNameInUseException' }">
				<script>toastr.error('il nickname scelto non è disponibile!', 'registrazione fallita');</script>
		    </c:if>
		    <c:if test="${param.reason == 'passwordDontMatch' }">
				<script>toastr.error('le password non coincidono!', 'registrazione fallita');</script>
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
					toastr.error('le password non coincidono!', 'registrazione fallita');
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
			<input type="text" id="username" class="textbox form-control" placeholder="Username">	
			<input type="password" id="password" class="textbox form-control" placeholder="Password">
			<button type="submit" id="loginbtn"> LogIn </button>
			<input type="checkbox" id="rm">
			<label id="rmlbl" for="rm">Remember me</label>
			<label id="signup" onClick="showsignupform()"> SignUp </label>
		</div>
		
		<!-- form di registrazione -->
		<div id="signup_form">
			<div id="signup_form_container">
				<jsp:include page="/WEB-INF/pages/formSignUp.jsp" />
			</div>
		</div>    
    
	
    </jsp:body>
        
</t:template>



