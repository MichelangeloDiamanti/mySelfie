

//all' avvio, riempie tutti i campi con le informazioni già fornite dall 'utente
$( document ).ready(function() 
{
		
	//chiamata post con ajax per rempire i campi
	$.ajax(
	{
		method: "POST",
		url : '/mySelfie/protected/modifyProfile',
		data : 
		{ 
			reqType: "getInfo"
		},
		success : function(responseText) 
		{
			//divido tutti i campi e li scorro uno ad uno
			var fields = responseText.split('%');
			//riempie le textbox con i valori precedenti
			var i=0;
			for(i=0; i<7; i++)
			{
				var field = fields[i].split(':');
				if(field[1]!="null" && field[0]!="birthdate")
					$('[name='+field[0]+']').val(field[1]);
					
				if(field[0]=="birthdate"){
					$("#my-datepicker").datepicker("update", field[1]);

				}
					
			}
			//seleziona il corretto radiobutton
			var field = fields[7].split(':');
			if(field[1]=="M" || field[1]=="m")
				jQuery("#radioBtnM").attr('checked', 'checked');
			if(field[1]=="F" || field[1]=="f")
				jQuery("#radioBtnF").attr('checked', 'checked');
		}
	});
});
	

//non permette input diversi da numeri per il campo phone
$(document).ready(function() {
    $("#phone").keydown(function (e) {
        // Allow: backspace, delete, tab, escape, enter and .
        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110]) !== -1 ||
             // Allow: Ctrl+A
            (e.keyCode == 65 && e.ctrlKey === true) ||
             // Allow: Ctrl+C
            (e.keyCode == 67 && e.ctrlKey === true) ||
             // Allow: Ctrl+X
            (e.keyCode == 88 && e.ctrlKey === true) ||
             // Allow: home, end, left, right
            (e.keyCode >= 35 && e.keyCode <= 39)) {
                 // let it happen, don't do anything
                 return;
        }
        // Ensure that it is a number and stop the keypress
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
        }
    });
});



/* Controllo username già in uso */		
$("#username").on({
	'change':function () { 
		var my_txt = $(this).val();
		var len = my_txt.length;
		if(len > 0) {
			$.ajax({
				url : '/mySelfie/homepage/checkUsername',
				data : {
					username : $('#username').val(),
					reqType : "checkUsr"
				},
				success : function(responseText) {
					$('#username').css({ borderBottomLeftRadius: 0, borderBottomRightRadius: 0});
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



//controllo password prima del submit
function checkPwd()
{
	var check = false;
	var np = $('#mnpassword').val();
	var cp = $('#mcpassword').val();
	if(np != cp)
		toastr.error("Passwords don't match", "Update failed");
	else
		check = true;
	return check;
}
	