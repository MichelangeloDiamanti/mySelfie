$(document).ready(function(){
    
    $(document).mousemove(function(e)
    {
	
    	//ricava altezza e larghezza della pagina
		var Pheight = Math.max( document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight );
		var Pwidth = Math.max( document.body.scrollWidth, document.body.offsetWidth, document.documentElement.clientWidth, document.documentElement.scrollWidth, document.documentElement.offsetWidth );    	
	
		//in base alla posizione del mouse, vanno spostate le immagini
		
    	/* YOU */
    	if(e.pageY <= Pheight/2)
    	{
    		var Ytos = 6*e.pageY/Pheight;
			$('#you').css("top",  315-6+Ytos);
			$('#flash').css("top",  275-6+Ytos);	   	
    	}  		
    	if(e.pageY > Pheight/2)
    	{
    		var Ytos = 6*(e.pageY-Pheight)/Pheight;
    		$('#you').css("top",  315+Ytos);
    		$('#flash').css("top",  275+Ytos);
    	}
		if(e.pageX <= Pwidth/2) 
		{
			var Ylos = 7*e.pageX/Pwidth;
			$('#you').css("left",  950-6+Ylos);	
			$('#flash').css("left",  950-6+Ylos);	   	
		} 		
    	if(e.pageX > Pwidth/2)
    	{
			var Ylos = 6*(e.pageX-Pwidth)/Pwidth;
			$('#you').css("left",  950+Ylos);	
			$('#flash').css("left",  950-6+Ylos);	   	
		} 		


		/* GROUP */
    	if(e.pageY <= Pheight/2)
    	{
    		var Gtos = 4*e.pageY/Pheight;
			$('#group').css("top",  315+4-Gtos);	   	
    	}  		
    	if(e.pageY > Pheight/2)
    	{
    		var Gtos = 4*(e.pageY-Pheight)/Pheight;
    		$('#group').css("top",  315-Gtos);
    	}
		if(e.pageX <= Pwidth/2) 
		{
			var Glos = 4*e.pageX/Pwidth;
			$('#group').css("left",  880+4-Glos);	   	
		} 		
    	if(e.pageX > Pwidth/2)
    	{
			var Glos = 4*(e.pageX-Pwidth)/Pwidth;
			$('#group').css("left",  880-Glos);	   	
		} 

		/* FLOOR */
    	if(e.pageY <= Pheight/2)
    	{
    		var Ftos = 2*e.pageY/Pheight;
			$('#floor').css("top",  415+2-Ftos);	   	
    	}  		
    	if(e.pageY > Pheight/2)
    	{
    		var Ftos = 2*(e.pageY-Pheight)/Pheight;
    		$('#floor').css("top",  415-Ftos);
    	}
		if(e.pageX <= Pwidth/2) 
		{
			var Flos = 2*e.pageX/Pwidth;
			$('#floor').css("left",  750+2-Flos);	   	
		} 		
    	if(e.pageX > Pwidth/2)
    	{
			var Flos = 2*(e.pageX-Pwidth)/Pwidth;
			$('#floor').css("left",  750-Flos);	   	
		} 			
    });


	$("#you").click(function()
	{
		showFlash();
	});
	$("#group").click(function()
	{
		showFlash();
	});

});


function showFlash()
{
	$("#flash").fadeIn(150, function(){	$("#flash").fadeOut(350); });  
}