<%@page import="help.Cloner"%>
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
<%   String  path=getServletConfig().getServletContext().getRealPath("/");
int i1=path.indexOf(".metadata",0);
int j= path.indexOf("wtpwebapps",0);
String part1=path.substring(0,i1-1);
String part2=path.substring(j+10,path.length());
String newpath=part1.concat(part2);


int indice=0;

 %>
<div class="title">Démarrer votre évaluation</div>
<div class="nbreq">(20 requêtes)</div>
<form name="frm" method="post" action="Ev">
<input type="hidden" name="indice" value="<%=indice%>"/>
<input type="submit" value="Démarrer" id="demarrer" name="demarrer" /> 

</form>
</body>
</html>