package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

import help.LireXml;
import metier.Document;
import metier.Operation;
import metier.Request;




public class ResultatServlet extends HttpServlet {


	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String newpath;
		if(req.getAttribute("newpath")!=null)
			newpath=(String) req.getAttribute("newpath");
		else {
			
		String  path=getServletConfig().getServletContext().getRealPath("/");
	       int i=path.indexOf(".metadata",0);
	       int j= path.indexOf("wtpwebapps",0);
	       String part1=path.substring(0,i-1);
	       String part2=path.substring(j+10,path.length());
	        newpath=part1.concat(part2);	
		}
	
  String request;       
    PrintWriter pw=resp.getWriter();
	  int index=Integer.parseInt(req.getParameter("requests"));

    if(req.getAttribute("res")==null) {   

    	  Vector<String> vect=new LireXml().lireXml(index,newpath);
    	  Request reqt=new Request(Integer.parseInt(vect.get(0)), vect.get(1), vect.get(2));
    	  request=reqt.getReq();
    
    req.setAttribute("request", request);
    req.setAttribute("newpath", newpath);
    new SearchServlet().doPost(req, resp);	
	}
    else {
    	
    	   Vector <String> doc_pert = new Vector<String>();
    	   Vector  docs_C = new Vector();
    	   Vector  ord_docs_C = new Vector();
    	   Vector  docs_S = new Vector();
    	   Vector  ord_docs_S= new Vector();
    	   Vector  docs_C_S = new Vector();
    	   Vector  ord_docs_C_S = new Vector();
    	   Vector  DG ;
    	   Vector  DCG;
    	   Vector  iDG;
    	   Vector  iDCG;
    	   Vector  NDCG;
 		  Operation opC = new Operation(),opS = new Operation(),opC_S = new Operation(),op=new Operation(),opPert=new Operation();
    	   opC=(Operation) req.getAttribute("opC");
			System.out.println("sizeOpC"+ opC.getAll().size());
	    	opS=(Operation) req.getAttribute("opS");
			System.out.println("sizeOpS"+ opS.getAll().size());
	    	opC_S=(Operation) req.getAttribute("opC_S");
			System.out.println("sizeOpC_S"+ opC_S.getAll().size());
           String req_title=(String) req.getAttribute("request");

    	try {
			//Class Driver
			Class.forName("com.mysql.jdbc.Driver");
			//connector
			Connection cn=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/evaluation","root","");
			//objet commande
	         //liste de documents pertinents
			String  sqlReq="SELECT * \r\n" + 
					"FROM docsevaluation \r\n" + 
					"WHERE query_id =?\r\n" + 
					"AND (\r\n" + 
					"relevance =?\r\n" + 
					"OR relevance =?\r\n" + 
					")";
			
			PreparedStatement pr=(PreparedStatement) cn.prepareStatement(sqlReq);
			pr.setInt(1, index);
			pr.setInt(2, 2);
			pr.setInt(3, 3);
		    ResultSet rs=(ResultSet) pr.executeQuery();
		    while(rs.next()) {
		    doc_pert.add(rs.getString("doc_title"));
		    }
		    //liste des relevances triée
			String  sqlReq2="SELECT *\r\n" + 
					"FROM `docsevaluation`\r\n" + 
					"WHERE `query_id` =?\r\n" + 
					"ORDER BY `relevance` DESC\r\n";  
			PreparedStatement pr2=(PreparedStatement) cn.prepareStatement(sqlReq2);
			pr2.setInt(1, index);
		    ResultSet rs2=(ResultSet) pr2.executeQuery();
		    while(rs2.next()) {
		    	String nom_doc=rs2.getString("doc_title");
		    	if(Operation.nom_doc_exist(opC, nom_doc))
		    	  ord_docs_C.add(rs2.getFloat("relevance"));
		    	if(Operation.nom_doc_exist(opS, nom_doc))
			    	  ord_docs_S.add(rs2.getFloat("relevance"));
		    	if(Operation.nom_doc_exist(opC_S, nom_doc))
			    	  ord_docs_C_S.add(rs2.getFloat("relevance"));
		    }
		    //liste des relevances
		 			String  sqlReq3="SELECT *\r\n" + 
		 					"FROM `docsevaluation`\r\n" + 
		 					"WHERE `query_id` =?\r\n" ; 
		 					  
		 			PreparedStatement pr3=(PreparedStatement) cn.prepareStatement(sqlReq3);
		 			pr3.setInt(1, index);
		 		    ResultSet rs3=(ResultSet) pr3.executeQuery();
		 		    while(rs3.next()) {
		 		    	String nom_doc=rs3.getString("doc_title");
				    	if(Operation.nom_doc_exist(opC, nom_doc))
				    	  docs_C.add(rs3.getFloat("relevance"));
				    	if(Operation.nom_doc_exist(opS, nom_doc))
					      docs_S.add(rs3.getFloat("relevance"));
				    	if(Operation.nom_doc_exist(opC_S, nom_doc))
					      docs_C_S.add(rs3.getFloat("relevance"));
		 		  
		 		    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  for(String s:doc_pert) {
			  Document d=new Document(s);
			  opPert.add(d);    	
		    }
		  
    			
                System.out.println("sizeOpPert"+opPert.getAll().size());
    	    	
    	     float precision_5,precision_10,NDCG_5,NDCG_10,docs_pert_5,docs_pert_10;
    	    
//recherche classique
    	     Operation []methodes= {opC,opS,opC_S};
    	     Vector[]docs= {docs_C,docs_S,docs_C_S};
    	     Vector[]ord_docs= {ord_docs_C,ord_docs_S,ord_docs_C_S};
   for(int k=0;k<3;k++) {
    	     System.out.println("///////////////////////////////////  methode "+(k+1)+" //////////////////////////////////");
    	     precision_5=0;precision_10=0;NDCG_5=0;NDCG_10=0;docs_pert_5=0;docs_pert_10=0;
    	    DG = new Vector();
      	    DCG = new Vector();
      	    iDG = new Vector();
      	    iDCG = new Vector();
      	    NDCG = new Vector();
    	     
    	 if(methodes[k].getAll().size()!=0) {
    		//precision@5
    		 op=Operation.limiter(methodes[k], 5);
             for(Document doc:op.getAll()) {
            	 if(opPert.exist(doc))
            		 docs_pert_5++;
             }
             precision_5=docs_pert_5/5;
           //precision@10
    		 op=Operation.limiter(methodes[k], 10);
             for(Document doc:op.getAll()) {
            	 if(opPert.exist(doc))
            		 docs_pert_10++;
             }
             precision_10=docs_pert_10/10;
        	  
    	 
    	 System.out.println("docs_pert_5 "+docs_pert_5);
         System.out.println("docs_pert_10 "+docs_pert_10);
 		 System.out.println(precision_5+","+precision_10);
 	
 		 
 		 System.out.println("*****docs*****");
 		 for(int i=0;i<docs[k].size();i++) {
 			 System.out.println(docs[k].get(i));
 		 }
 		

 		//DG
 		
 		 DG.add(docs[k].get(0));
 		 for(int i=1;i<docs[k].size();i++) {
 			float log_rank=(float) (Math.log(i+1)/Math.log(2));
 			DG.add((float)docs[k].get(i)/log_rank);
 			
 		 }
 		System.out.println("******DG********* ");
		for(int i=0;i<DG.size();i++) {
			System.out.println(" "+DG.get(i));
		}
 		
 		 //DCG
 		
 		for(int i=0;i<DG.size();i++) {
 			float sum=0;
 			for(int j=0;j<=i;j++) {
 				sum+=(float)DG.get(j);	
 			}
 			DCG.add(sum);
 			
 		 }

		System.out.println("********DCG********* ");
		for(int i=0;i<DCG.size();i++) {
			System.out.println(" "+DCG.get(i));
		}
		
		 System.out.println("*****ord_docs*****");
 		 for(int i=0;i<ord_docs[k].size();i++) {
 			 System.out.println(ord_docs[k].get(i));
 		 } 
 		 
		//iDG
 		
		 iDG.add(ord_docs[k].get(0));
		 for(int i=1;i<ord_docs[k].size();i++) {
			float log_rank=(float) (Math.log(i+1)/Math.log(2));
			iDG.add((float)ord_docs[k].get(i)/log_rank);
			
		 }
		System.out.println("********iDG****** ");
		for(int i=0;i<iDG.size();i++) {
			System.out.println(" "+iDG.get(i));
		}
		
		 //iDCG
		
		for(int i=0;i<iDG.size();i++) {
			float sum=0;
			for(int j=0;j<=i;j++) {
				sum+=(float)iDG.get(j);	
			}
			iDCG.add(sum);
			
		 }

		System.out.println("***********iDCG********* ");
		for(int i=0;i<iDCG.size();i++) {
			System.out.println(" "+iDCG.get(i));
		}
	//NDCG
		
		for(int i=0; i<DCG.size();i++) {
			if((float)iDCG.get(i)==0)
				NDCG.add(Float.parseFloat("0"))	;
			else
			NDCG.add((float)DCG.get(i)/(float)iDCG.get(i));	
		}
		System.out.println("*******NDCG********** ");
		for(int i=0;i<NDCG.size();i++) {
			System.out.println(" "+NDCG.get(i));
		}
    	 }
    	 //precision@5
    	 req.setAttribute("precision_5 "+(k+1), precision_5);
    	 //precision@10
 		 req.setAttribute("precision_10 "+(k+1), precision_10);
 		 float ndcg_5,ndcg_10;
 		 //NDCG_5
 		 if(NDCG.size()==0)
 			ndcg_5=0;
 		 else if(NDCG.size()>=5)
 			ndcg_5=(float) NDCG.get(4);
 		 else
 			ndcg_5=(float) NDCG.get(NDCG.size()-1);
 		 req.setAttribute("NDCG_5 "+(k+1), ndcg_5);
 		 //NDCG_10
 		 if(NDCG.size()==0)
 			ndcg_10=0;
 		 else if(NDCG.size()>=10)
 			ndcg_10=(float) NDCG.get(9);
 		 else
 			ndcg_10=(float) NDCG.get(NDCG.size()-1);
 		 req.setAttribute("NDCG_10 "+(k+1), ndcg_10); 		
 		 
		
   }
    req.setAttribute("request", req_title);
    req.setAttribute("result", "yes"); 
   req.getRequestDispatcher("Resultat.jsp").forward(req, resp);


    }    
    	
    }

}
