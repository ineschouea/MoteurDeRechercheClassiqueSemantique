package web;

import java.io.IOException;



import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import metier.Document;
import metier.Operation;

import metier.Request;

public class EvalServlet2 extends HttpServlet {
	Vector<Request> reqs;
	int indice=0;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     
		   
	 int start=(int) req.getAttribute("start") ;  
	 
	 Operation opTout=(Operation) req.getAttribute("opTout");
	 Operation opC=(Operation) req.getAttribute("opC");
	 Operation opS=(Operation) req.getAttribute("opS");
	 Operation opC_S=(Operation) req.getAttribute("opC_S");
	 

     String requete =(String) req.getAttribute("requete");
     String newpath =(String) req.getAttribute("newpath");
 
	
		  
         if(req.getAttribute("indice")!=null)
        	 indice=(int)req.getAttribute("indice");
   
      
    
    
       req.setAttribute("opTout", opTout);
       req.setAttribute("opC", opC);
       req.setAttribute("opS", opS);
       req.setAttribute("opC_S", opC_S);
       
       req.setAttribute("requete", requete);
       req.setAttribute("indice", indice);
       req.setAttribute("newpath", newpath);
       req.setAttribute("start", start);
       
     
	    req.getRequestDispatcher("Eval2.jsp").forward(req, resp); 	
	}

}

