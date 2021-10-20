<%@page import="metier.Operation"%>
<%@page import="web.SearchBeans"%>
<%@page import="java.util.Iterator"%>
<%@page import="metier.Document"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="ConsulterCss.css" type="Text/css">
<title>Consulter collection</title>
</head>

<body>
<%
  SearchBeans sb;
if(request.getAttribute("modele")!=null)
		{
	sb= (SearchBeans)request.getAttribute("modele");
		}else{
			
			sb=new SearchBeans();
		
		}

%>


<form Action="AdminOpe.jsp">

<div class="liste"> 
<%
if(sb.getList().size()==0 && request.getAttribute("modele")!=null ){%>
<div class="msg">Aucun document existant!!!</div>
<%}else{
Iterator<Document> list=sb.getList().iterator();
int i=0;%>
<table>
<tr>
<th class="num">N°</th>
<th>Nom de document</th>
</tr>
<% while(list.hasNext())
{
	Document doc=list.next();
	i++;
	%>
	<tr>
	<td class="num"><%=i%></td>
	<td><a href="<%=doc.getUrl()%>"><%=doc.getNom()%> </a></td>
    </tr>
<%}}%>
</table>
</div>

</form>
</body>

</html>