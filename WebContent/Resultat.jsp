<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
function valid()
{

	if (document.frm.requests.value ==0)
	{
	alert ( "Select request number!!!!!" );
	return false;
	}
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="EvaluationCss.css" type="Text/css">
<title>Resultat</title>
</head>
<body>
<form name="frm" method="post" action="Res"  onsubmit="return valid();">
<input type="submit" value=" Resultat d'évaluation" id="resultat" name="choix" /> 
  <Select id="requests" name="requests" />
  <option name="0" value="0" selected="selected">Aucun</option>
  <option name="1" value="1">Request 1</option>
  <option name="2" value="2">Request 2</option>
  <option name="3" value="3">Request 3</option>
  <option name="4" value="4">Request 4</option>
  <option name="5" value="5">Request 5</option>
  <option name="6" value="6">Request 6</option>  
  <option name="7" value="7">Request 7</option> 
  <option name="8" value="8">Request 8</option> 
  <option name="9" value="9">Request 9</option> 
  <option name="10" value="10">Request 10</option> 
  <option name="11" value="11">Request 11</option>
  <option name="12" value="12">Request 12</option>
  <option name="13" value="13">Request 13</option>
  <option name="14" value="14">Request 14</option>
  <option name="15" value="15">Request 15</option>
  <option name="16" value="16">Request 16</option>  
  <option name="17" value="17">Request 17</option> 
  <option name="18" value="18">Request 18</option> 
  <option name="19" value="19">Request 19</option> 
  <option name="20" value="20">Request 20</option> 
  </Select>
<%if (request.getAttribute("result")=="yes"){ 
String req=(String)request.getAttribute("request");	
float p_5_C=(float)request.getAttribute("precision_5 1");
float p_5_S=(float)request.getAttribute("precision_5 2");
float p_5_C_S=(float)request.getAttribute("precision_5 3");

float p_10_C=(float)request.getAttribute("precision_10 1");
float p_10_S=(float)request.getAttribute("precision_10 2");
float p_10_C_S=(float)request.getAttribute("precision_10 3");

float NDCG_5_C,NDCG_10_C,NDCG_5_S,NDCG_10_S,NDCG_5_C_S,NDCG_10_C_S;
if(request.getAttribute("NDCG_5 1")!=null)
	NDCG_5_C=(float)request.getAttribute("NDCG_5 1");
else
	NDCG_5_C=0;
if(request.getAttribute("NDCG_5 2")!=null)
	NDCG_5_S=(float)request.getAttribute("NDCG_5 2");
else
	NDCG_5_S=0;
if(request.getAttribute("NDCG_5 3")!=null)
	NDCG_5_C_S=(float)request.getAttribute("NDCG_5 3");
else
	NDCG_5_C_S=0;
if(request.getAttribute("NDCG_10 1")!=null)
	NDCG_10_C=(float)request.getAttribute("NDCG_10 1");
else
	NDCG_10_C=0;
if(request.getAttribute("NDCG_10 2")!=null)
	NDCG_10_S=(float)request.getAttribute("NDCG_10 2");
else
	NDCG_10_S=0;
if(request.getAttribute("NDCG_10 3")!=null)
	NDCG_10_C_S=(float)request.getAttribute("NDCG_10 3");
else
	NDCG_10_C_S=0;
%>
<table>
<tr>
<th  colspan="12" id="request_title"><%=req %></th>
</tr>
<tr >
  <th colspan="4">Recherche classique</th>
  <th colspan="4">Rechercher sémantique</th>
  <th colspan="4">Rechercher classique_sémantique</th>
</tr>
<tr>
<th>Precision@5</th>
<th>Precision@10</th>
<th>NDCG@5</th>
<th>NDCG@10</th>
<th>Precision@5</th>
<th>Precision@10</th>
<th>NDCG@5</th>
<th>NDCG@10</th>
<th>Precision@5</th>
<th>Precision@10</th>
<th>NDCG@5</th>
<th>NDCG@10</th>
</tr>
<tr>
<td><%=p_5_C %></td>
<td><%=p_10_C %></td>
<td><%=NDCG_5_C %></td>
<td><%=NDCG_10_C %></td>

<td><%=p_5_S %></td>
<td><%=p_10_S %></td>
<td><%=NDCG_5_S %></td>
<td><%=NDCG_10_S %></td>

<td><%=p_5_C_S %></td>
<td><%=p_10_C_S %></td>
<td><%=NDCG_5_C_S %></td>
<td><%=NDCG_10_C_S %></td>
</tr>
</table>
<%} %>
</body>
</html>