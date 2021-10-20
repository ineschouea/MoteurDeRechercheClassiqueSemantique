<%@page import="web.SearchBeans"%>
<%@page import="java.util.Iterator"%>
<%@page import="metier.Document"%>
<%@page import="metier.Operation"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="RechercheCss.css" type="Text/css">
<script type="text/javascript">
function verif(){
	if(document.form2.recherche.value=="")
	{alert("Tapez votre recherche!!!");
	document.form2.recherche.focus();
	return false;
	}
	if(document.form2.choix.value=="")
		{alert("Choisissez le type de recherche!!!")
		return false
		}
	else
		return true;	
}
</script>
<title>Recherche</title>
</head>
<body>
<%
  SearchBeans sb;
if(request.getAttribute("modele")!=null)
		{
	
	sb= (SearchBeans)request.getAttribute("modele");
	String newpath=(String)request.getAttribute("newpath");
	
	    session.setAttribute("modele", sb);
	    session.setAttribute("newpath", newpath);
	    
	   
		}
%>
<form method="post" action="Rech" name="form2" onsubmit="return verif();">
<img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAMAAAAp4XiDAAAA81BMVEXnSD/////nRz750M774N/ra2ToUEf4ycbpU0vpWFDrZ1/4y8npW1PsbmdWVlbzpqK9urj1sq72u7j97+47OzvymJP63NoKCAjwiYOmpqb+9/f51dPn5eWyr67zoZ35+PjX1NPkr613dXTGxMVraWiztrnS2N3LzdDb3t+bnZ0xMTGopKLufXdSUlKalZS9vr/as7SdqLHt3d3nzc3gv8CiiYjUqKjHpaaUm6HCytDLtravlZWxsrawu8PFQTqdYF2he3pkMS6pcG1NGxm1TEaRgoHYQjmZRD7HSEFSODejl5aHh4i7p6bk7vLRm5vVs7WJkJTn8RnHAAACq0lEQVRIiZ2WbVeiQBTHJzAeApREzEEGNEKGLK2wjVbT3W233c00v/+n2eFBNMCA/b/gcM7M79w7d+7ce8FRKEUwJJEDB8WJkkEr0V4QfDSh88n2rfiOoG0RrSEWA4HEhhYhmlHCROyfoQWI1mDKEgAwDZMgbEmvYt/oI6B0qhAAdBQglD5IJI4GajUCAANIVREJ8FWRTLSoHH3CB4scL54QHYciP6LIc9FKnnj1lBXobrfdbtdikd9ulxbY006e/9RxlHT50tiTjCGKa+5vMXWMzQ9Qk0sz1JWyW9Yt2SKSe3uUcpVB1J0FC/UwJmawhfQd00khFNNICFnGgzGCaOxgZ48xDh/FsrH9On3zLOTCwR5zynw0Q/FsvILRYHY/6ZJYKzpyB2P50PkpUYhX5LE98ghBs01PR/AxMcPyaYSOg2U/QasW3F+/32e9oT22ipDezHZXd54n3BCkP5nCmVyIuL678iaTgLiZ0FN3gwqR5QauPAKsCeG13zZLWIiMNu7qfb1efyHE3R3aLAut6MSx8XtMkDD7/uGzbIOM/Fv4EBOm5d+ODgY5ucre6AnePwSEaVrwyd/6lb3KXcIg/3H2Ov2q646MHmcX22vJJsxeWiLXcSwZyZYzsC/O6+d29ATSafkh+eURSWKMBw4cDVuX13UYMunkTz0xNEQIDYeWqbfOWvWQqUkZhE895F4vDNX5WcxkHzIpF3ReucD1iMkpF0RiUpRqtV1VuvkbMNe/X3KIsMAxTFD6SNWTiMLiJ/55JszFr58Hi2ZOfZ0/n126+AdImOIyTs2/LyFaJP6XaRbUfPFtMd8akUq1JOpl/pK4ZZRrfNTuIJzwP+21ahMXglHBqDwqHClqaYZRtWjsUYyqY0/Qo8oMV9zecBUYElTpU4qTVCF+Uf8AvaVL+6Y2FVgAAAAASUVORK5CYII=" alt="?"/>
<input type="text" placeholder="Recherche....."  name="recherche">  
   
<div class="choix"> 
  <input type="radio" value="r-classique" name="choix">Rechercher par modèle classique<br>
  <input type="radio" value="r-semantique"  name="choix">Rechercher par modèle sémantique<br>
  <input type="radio" value="r-semantique-classique"  name="choix">Rechercher par modèle classique-sémantique
</div>
<input type="submit" value="Rechercher" > 

</form>
 <div class="resultat"> 
<%
if((session.getAttribute("modele")!=null)){
SearchBeans sb2=(SearchBeans)session.getAttribute("modele");


  if(sb2.getList().size()==0){
%>
	<div class="msg">Aucune résultat de votre recherche</div>
<%}else{
	 ArrayList<Document> List = sb2.getList();
	
	 String newpath=(String)session.getAttribute("newpath");
	
	
	 request.setAttribute("table",List);  
	%>
	
		<display:table name="table" sort="list" pagesize="5" id="table1" export="false" decorator="help.DocumentDecorator">
		    <display:column property="id" title="N°"  />
			<display:column property="nom" title="DOCUMENT" />
			<display:column property="score" title="SCORE"/>
			<display:setProperty name="paging.banner.full" value='<span class="pagelinks" align="center">[<a href="{1}">prémier</a><a href="{2}">Précédent</a>]{0}[ <a href="{3}">Suivant</a>/ <a href="{4}">Last</a>]</span>'/>

<display:setProperty name="paging.banner.no_items_found" value='<span class="pagebanner"> Aucun {0} trouvé. </span>'/>
<display:setProperty name="paging.banner.one_item_found" value='<span class="pagebanner"> Un{0} trouvé. </span>'/>
<display:setProperty name="paging.banner.all_items_found" value='<span class="pagebanner"> {0} {1} trouvés, afficher  {2}. </span>'/>
<display:setProperty name="paging.banner.some_items_found" value='<span class="pagebanner"> {0} {1} Trouvés, afficher {2} à {3}. </span>'/>
<display:setProperty name="paging.banner.first" value='<span class="pagelinks" align="center"> [Prémier/Précédent] {0} [ <a href="{3}">Suivant</a>/ <a href="{4}">dernier</a>] </span>'/>
<display:setProperty name="paging.banner.last" value='	<span class="pagelinks" align="center">[ <a href="{1}">Prémier</a>/ <a href="{2}">Précédent</a>] {0} [Suivant/dernier] </span>'/>
<display:setProperty name="paging.banner.item_name" value='élément'/>
<display:setProperty name="paging.banner.items_name" value='éléments'/>
<display:setProperty name="basic.msg.empty_list" value='Aucun élément trouvé!!'/>
<display:setProperty name="basic.msg.empty_list_row" value='	<tr class="empty"><td colspan="{0}">Aucun élément trouvé!!</td></tr>'/>
			
		</display:table>
	</body>

<%}} %>

 </div>
</body>
<html>