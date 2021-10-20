<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="AjouterCss.css" type="Text/css">

<script type="text/javascript">
var n=1;
function desactive_bouton()
{
 
	if(n>1)
	{	document.frm3.addDoc.disabled=true;
		
	}
	n = n+1;
}
	

function validDoc()
{
		if (document.frm3.url.value == "" && document.frm3.collection.value=="0" )
		{
		alert ( "choisir une collection ou taper un URL de document!!!" );
		return false;
	}else
		return true;
}
</script>
<title>Ajouter document</title>
</head>
<body>

<form name="frm3" action="Ajs" method="post" onsubmit="return validDoc();">
 <table border="1" >
 <tr>
  <td border="0px" >
     <h3> Ajouter un document</h3>
     <input type="text" id="url" name="url" placeholder="url"/>
     <input type="submit" value="Ajouter" name="addDoc" onclick="desactive_bouton();"/>
     
  </td >
  <td border="0px">
  <h3> Ajouter une collection</h3>
  <Select id="collection" name="collection" placeholder="collection"/>
  <option name="0" value="0" selected="selected">Aucun</option>
  <option name="1" value="1">Home_computer_hardware_companies</option>
  <option name="2" value="2">Muslim-majority_countries</option>
  <option name="3" value="3">Roses</option>
  <option name="4" value="4">Programming_languages</option>
  <option name="5" value="5">Near_threatened_animals</option>
  <option name="6" value="6">Ball_games</option>  
  </Select>
  <input type="submit" value="Ajouter" name="addCol" onclick="desactive_bouton();"/>
     
  </td>
 </tr>
 </table>
 </form>
</body>
</html>