<%@page import="metier.Request"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="EvaluationCss.css" type="Text/css">
<title>Evaluation</title>
</head>
<body>
<% 



	
Request requet =(Request) request.getAttribute("reqt");
int indice=(int)request.getAttribute("indice");
int start=(int)request.getAttribute("start");
String newpath=(String)request.getAttribute("newpath");

%>
<form action="Rech" method="post">
<div class="num"><%=requet.getNum() %></div>
<div class="req"><%=requet.getReq() %></div>
<div class="desc"><%=requet.getDesc() %></div>
<input type="submit" value="Documents pertinents" name="choix" id="doc"/>
<input type="hidden" value="<%=requet.getReq()%>" name="req"/>
<input type="hidden" value="<%=indice %>" name="indice"/>
<input type="hidden" value="<%=start %>" name="start"/>



</form>
<form action="Ev" method="post">

<%if(indice<=19){

%>
<input type="hidden" name="indice" value="<%=indice%>"/>
<input type="submit" value="Requête suivante" name="req" class="suivante"/>
<%} %>
</form>

</body>
</html>