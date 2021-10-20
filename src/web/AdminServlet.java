package web;

import help.Indexation;

import help.Indexer;
import help.LuceneConstants;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import metier.Document;
import metier.Operation;



public class AdminServlet extends HttpServlet {

	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	
	
		String[] activite=req.getParameterValues("activite");

		String username=req.getParameter("username");
		String password=req.getParameter("password");
		String exception=null;
		Operation ope=new Operation();
		SearchBeans sb=new SearchBeans();
		   String  path=getServletConfig().getServletContext().getRealPath("/");
	       int i=path.indexOf(".metadata",0);
	       int j= path.indexOf("wtpwebapps",0);
	       String part1=path.substring(0,i-1);
	       String part2=path.substring(j+10,path.length());
	       String newpath=part1.concat(part2);
		
		
		
	//login verification
	if(username!=null && password!=null)
		if(username.equals("admin")&& password.equals("password"))
			req.getRequestDispatcher("AdminOpe.jsp").forward(req, resp);
		
		
		if(activite[0].equals("Indexer Collection"))
	      {
		 Indexation in=new Indexation(newpath);
		 exception=in.exception;
		 req.setAttribute("username", username);
		 req.setAttribute("password", password);
		 req.setAttribute("exception3", exception);
		 req.getRequestDispatcher("AdminOpe.jsp").forward(req, resp);
		}
		
		
		else if(activite[0].equals("Consulter collection"))
		  { 
              
               File repertoire= new File(newpath+"Data");
                Vector<String> liste=new Vector<String>();
				File [] listefichiers; 
                String nom,path1,url;
                Document d=new Document();
				listefichiers=repertoire.listFiles(); 
				for(int i1=0;i1<listefichiers.length;i1++)
				{
			     nom=listefichiers[i1].getName().substring(0, listefichiers[i1].getName().length()-4);
			     path1=listefichiers[i1].getAbsolutePath();
			     url=ope.recupUrl(nom, newpath);
			     d=new Document(nom,path1,url);
			    	ope.add(d);
			     }
		sb.setList(ope.getAll()); 
		req.setAttribute("modele", sb);
	    req.getRequestDispatcher("Consulter.jsp").forward(req, resp);
						 	
		  }
	else if(activite[0].equals("Ajouter document(s)"))

	{  
		req.getRequestDispatcher("Ajouter.jsp").forward(req, resp);
	    
		
		     
	}	

		else if(activite[0].equals("Supprimer document(s)"))
		{  
		
		  File repertoire= new File(newpath+"Data");
          Vector<String> liste=new Vector<String>();
			File [] listefichiers; 
          String nom,path1;
          Document d=new Document();
			listefichiers=repertoire.listFiles(); 
		     
			for(int i1=0;i1<listefichiers.length;i1++)
			{
		     nom=listefichiers[i1].getName().substring(0, listefichiers[i1].getName().length()-4);
		     path1=listefichiers[i1].getAbsolutePath();
			
			
		     d=new Document(nom,path1);
			ope.add(d);	
			}	

	sb.setList(ope.getAll());
	req.setAttribute("modele", sb);
	req.getRequestDispatcher("Supprimer.jsp").forward(req, resp);

	}	
		
	}}

