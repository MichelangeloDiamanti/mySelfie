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


