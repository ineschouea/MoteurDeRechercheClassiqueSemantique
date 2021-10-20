<%@page import="metier.Operation"%>
<%@page import="metier.Document"%>
<%@page import="metier.Request"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Vector"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="EvaluationCss.css" type="Text/css">
<script type="text/javascript">
function verif(){

	if(document.form.pertinence.value=="")
		{alert("Choisissez le niveau de pertinence de document!!!")
		return false
		}
	else
		return true;	
}
</script>
<title>Evaluation</title>
</head>
<body>
<%


Operation op= new Operation();
Operation opTout= new Operation();
if(request.getAttribute("opTout")!=null)
{
 opTout=(Operation) request.getAttribute("opTout");

session.setAttribute("opTout", opTout);
}
else
opTout=(Operation) session.getAttribute("opTout");

String requet =(String) request.getAttribute("requete");

int indice=(int)request.getAttribute("indice");
String newpath=(String)request.getAttribute("newpath");
int i=(int)request.getAttribute("start");
int j=i-1;

%>
<form name="form" method="post" action="Evaluation" onsubmit="return verif();">

<div class="doc"><%=i%>.<a href="<%=opTout.getAll().get(j).getUrl() %>"><%=opTout.getAll().get(j).getNom() %></a></div>
<%if(op.getAbstract(opTout.getAll().get(j), newpath).equals("*******************")==false) {%>
<textarea id="resum" rows="5" >
<%=op.getAbstract(opTout.getAll().get(j), newpath) %>
</textarea>
<%}


	


%>
<div class="rad">
<input type="radio" id="non-pertinent" name="pertinence" value="non-pertinent" />
<label for="non-pertinent">Non pertinent</label><br>
<input type="radio" id="peu-pertinent" name="pertinence" value="peu-pertinent"/>
<label for="peu-pertinent">Peu pertinent</label><br>
<input type="radio" id="pertinent" name="pertinence" value="pertinent"/>
<label for="pertinent" id="pertinent">Pertinent</label><br>
<input type="radio" name="pertinence" id="tres_pertinent"  value="tres_pertinent" />
<label for="tres_pertinent">Très pertinent</label><br>
</div>

<input type="hidden" value="<%=opTout.getAll().get(j).getNom()%>" name="DocNom"/>
<input type="hidden" value="<%=requet%>" name="req"/>
<input type="hidden" value="<%=newpath%>" name="newpath"/>
<input type="hidden" value="<%=i+1%>" name="start"/>
<input type="hidden" value="<%=indice%>" name="indice"/>




<%if(i<(opTout.getAll().size())){%>
<input type="submit" value="Document suivant" name="choix" class="bouton"/>

<%}else{%>

<input type="submit" value="Requête suivante" name="choix" id="suivreqdoc"/>	

	<%}%>

<div class="nbr"><%=i%>/<%=(opTout.getAll().size())%></div>
</form>
</body>
</html>