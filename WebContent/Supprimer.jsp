<%@page import="web.SearchBeans"%>
<%@page import="java.util.Iterator"%>
<%@page import="metier.Document"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="SupprimerCss.css" type="Text/css">
<script language="JavaScript">
function cocher(source) {
  checkboxes = document.getElementsByName("ch");
  
  for(var i in checkboxes)
	  checkboxes[i].checked =true;
}
function decocher(source) {
	  checkboxes = document.getElementsByName("ch");
	  for(var i in checkboxes)
	    checkboxes[i].checked =false; 
	}
</script>
<title>Supprimer document</title>
</head>
<body>
<%

  SearchBeans sb;
if(request.getAttribute("modele")!=null)
		{
	
	sb= (SearchBeans)request.getAttribute("modele");
	   if(request.getAttribute("exception")=="ok")
	   {%>
	   <script >
	   alert("Document supprime avec succes !!!");
	   
	   </script>
		   
	   <% }
		}else{
			sb=new SearchBeans();
			
		}
%>

<form Action="Ss" method="post">
<div class="liste"> 
<%
if((request.getAttribute("modele")!=null)&& (sb.getList().size()==0))
	out.println("Aucune document existant ");
else{	%>
<table>
<% 
Iterator<Document> list=sb.getList().iterator();
while(list.hasNext())
{
	Document doc=list.next();
%>
<tr>
<td><input type="checkbox" id="bt" name="ch" value="<%=doc.getPath()%>"/></td>
<td><label for=<%=doc.getPath()  %>><%=doc.getNom()%></label></td>
</tr>
<%}}%>
</table>
 </div>
 <input type="submit" value="Supprimer" name="operation" id="supp"/>
 <input type="button" value="Tous cocher"  onClick="cocher(this)"/>
 <input type="button" value="Tous décocher" onclick="decocher(this)"/>
 </form>
</body>
</html>