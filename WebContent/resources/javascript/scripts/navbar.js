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
	$("#hiddenMenuContainer").slideToggle( "fast" );	
});



$(document).click(function(e) {
	
		
	  if ($("#hiddenMenuContainer").is(":visible") &&
		  $(e.target).attr("id") != "hiddenMenuBtn"  &&
		  $(e.target).closest('#hiddenMenuContainer').length === 0) 
	  {
		  $( "#hiddenMenuContainer" ).slideUp( "fast" );		
	  }
});

