// click sul pulsante di logout
// è necessario fare una post all'user validator passando come action logout
$('#logOutBtn').click(function() {
	$.ajax({
		method : "POST",
		url : '/mySelfie/userValidator',
		data : {
			action : "logout"
		},
		success : function(responseText) {
			window.location = "/mySelfie/";
		}
	});
});


$('#hiddenMenuBtn').click(function() 
{
	$( "#hiddenMenuContainer" ).slideToggle( "fast" );	
});

