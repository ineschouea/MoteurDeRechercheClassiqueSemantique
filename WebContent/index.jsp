
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="AdminCss.css" type="Text/css">
<script>

function validLogin() // java script function is created
{
if(document.frm1.username.value == "" && document.frm1.password.value == "")
{
alert ( "S'il vous plait tapez vos donnees" ); //java script message will be display
document.frm1.username.focus();
return false;
}
if (document.frm1.password.value == "")
{
alert ( "S'il vous plaît tapez votre password" );
document.frm1.username.focus();
return false;
}
if (document.frm1.password.value == "")
{
alert ( "S'il vous plaît tapez votre username" );
document.frm1.username.focus();
return false;
}
if(document.frm1.username.value == "admin" && document.frm1.password.value == "password")
{
alert ( "Acces autorise" ); //java script message will be display
document.frm1.username.focus();
return true;
}
if(document.frm1.username.value != "admin" || document.frm1.password.value != "password")
{
alert ( "Acces non autorise" ); //java script message will be display
document.frm1.username.focus();
return false;
}
}


function validUrl()
{

	if (document.frm1.url.value == "")
	{
	alert ( "Url obligatoire" );
	return false;
	}
}





function showPass() {
	  var x = document.getElementById("password");
	  if (x.type =="password") {
	    x.type = "text";
	  } else {
	    x.type = "password";
	  }
}
</script>

<title>Admin Login </title>
</head>
<body>


<form name="frm1" method="post" action="Ad" onsubmit="return validLogin();">
   <div class="login">
       <label><h1>Administrateur authentification</h1></label>
       <input type="text" placeholder="Username" id="username" name="username" >  
       <input type="password" placeholder="password" id="password" name="password"> 
 
         <div class="affiche">  
       <input type="checkbox" onclick="showPass()">Afficher le mot de passe
         </div>
      <input type="submit" value="Log in" id="login" >
         </div>
        <div class="shadow"></div>
   </div>


</form>

</body>
</html>