$(document).ready(function()
{
	$('.tab').click(function()
	{
		$("#leftMenu > li").removeClass("seltab");
		$(this).addClass("seltab");
	});
});



//binda il caricamento delle immagini home o explore al click del menu
$(document).ready(function()
{
	//click sul menu sinistro (home)
	$('#tabH').click(function()
	{
		window.location = "/mySelfie/";
	});
	
	
	//click sul menu sinistro (expolre)
	$('#tabE').click(function()
	{
		window.location = "/mySelfie/?queryType=explore";
	});
	
	
});

