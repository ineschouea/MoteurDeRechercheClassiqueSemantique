
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="AdminOpeCss.css" type="Text/css">
<title>Administrateur</title>
</head>
<body>
<%
if(request.getAttribute("exception3")!=null)
{
  String e=(String)request.getAttribute("exception");
  if(e=="indexation")
  { %>
	  <script type="text/javascript">
	  alert("Erreur lors d'indexation!!");
	  </script> 
	  <%   
  }else {%>
  <script type="text/javascript">
	  alert("Operation effectuee avec succes !!!");
	  </script> 
	  <% };}%>
<%
if(request.getAttribute("exception")!=null)
{
  String e=(String)request.getAttribute("exception");
  if(e=="supp")
  { %>
	  <script type="text/javascript">
	  alert("Document(s) supprime avec succes!!");
	  </script> 
	  <%   
  }else if( e=="nonsupp"){%>
  <script type="text/javascript">
	  alert("echec!!");
	  </script> 
	  <% };}%>
<%
if(request.getAttribute("exception2")!=null)
{
  String e=(String)request.getAttribute("exception2");
  if(e=="ok")
  { %>
	  <script type="text/javascript">
	  alert("Document(s) ajouté avec succes!!");
	  </script> 
	  <%   
  }else if(e=="urlFound"){
	  %>
  <script type="text/javascript">
  alert("Le document existe deja!!!");
  </script> 
  <% 
  }else if(e=="okc"){
	  %>
  <script type="text/javascript">
  alert("Collection ajoute aveec succes!!!");
  </script>
  <%} }%>
<form name="frm2" method="post" action="Ad">
<div class="choix">
  <input type="submit" value="Consulter collection" id="cons" name="activite">
  <input type="submit" value="Ajouter document(s)" id="ajout" name="activite" >
  <input type="submit" value="Supprimer document(s)" id="supp" name="activite" >
  <input type="submit" value="Indexer Collection" id="indexer" name="activite">
</div>
</form>
</body>
</html>