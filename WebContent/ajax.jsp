<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<title>jQuery, Ajax and Servlet/JSP integration example</title>

<script src="http://code.jquery.com/jquery-1.10.2.js"
	type="text/javascript"></script>
<script type="text/javascript">

	$(document).on('input','#userName',function () { 
		var my_txt = $(this).val();
		var len = my_txt.length;
		if(len > 0)
		{
		 	$.ajax({
				url : 'homepage/ajax',
				data : {
					userName : $('#userName').val(),
					checkNick : "true"
				},
				success : function(responseText) {
					if(responseText==="true")
						$('#ajaxGetUserServletResponse').html(
								"<div class=\"alert alert-success\" role=\"alert\">" +
								"  <span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\"></span>" +
								"  <span class=\"sr-only\">Error:</span>" +
								"  username disponibile" +
								"</div>"			
						);
					else 
						$('#ajaxGetUserServletResponse').html(
								"<div class=\"alert alert-danger\" role=\"alert\">" +
								"  <span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\"></span>" +
								"  <span class=\"sr-only\">Error:</span>" +
								"  username non disponibile" +
								"</div>"		
						);
				}
			});
		}else{
			$('#ajaxGetUserServletResponse').html("");
		}
	});
</script>
</head>
<body>

	<form id="formid">
		Enter Your Name: <input type="text" id="userName" />
		<input type="button" id="okbtn" value="ok">
	</form>
	<br>
	<br>

	<strong>Ajax Response</strong>:
	<div id="ajaxGetUserServletResponse"></div>
</body>
</html>