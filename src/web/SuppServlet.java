package web;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import metier.*;
import help.*;


public class SuppServlet extends HttpServlet{
			
	
	public static void supprimer(String path,int lineNumber) throws Exception {
		/// file1
	    InputStream flux;
		try {
			flux = new FileInputStream(path+"Urls.txt");
	    InputStreamReader lecture=new InputStreamReader(flux);
	    BufferedReader buff=new BufferedReader(lecture); 
	    FileWriter fw;
		try {
			fw = new FileWriter(path+"permut.txt", false);
		BufferedWriter output = new BufferedWriter(fw);	
	    String ligne;
	    int i=0;
	    
	    while ((ligne=buff.readLine())!=null){
	    	i++;
	    	if ((i!=lineNumber) && (i!=lineNumber+1) )
	    		{	
					output.write(ligne);
					output.newLine();
					output.flush();	
	    		}	
	    }
	    output.close();
	    buff.close();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		
		new Cloner().cloner(path, "\\permut.txt", "Urls.txt");    
		
		//file2
		  InputStream flux2;
			try {
				flux2 = new FileInputStream(path+"abstract.txt");
		    InputStreamReader lecture2=new InputStreamReader(flux2);
		    BufferedReader buff2=new BufferedReader(lecture2); 
		    FileWriter fw2;
			try {
				fw2 = new FileWriter(path+"permut.txt", false);
			BufferedWriter output2 = new BufferedWriter(fw2);	
		    String ligne;
		    int i=0;
		    
		    while ((ligne=buff2.readLine())!=null){
		    	i++;
		    	if ((i!=lineNumber) && (i!=lineNumber+1) )
		    		{	
						output2.write(ligne);
						output2.newLine();
						output2.flush();	
		    		}	
		    }
		    output2.close();
		    buff2.close();
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

			
			new Cloner().cloner(path, "\\permut.txt", "abstract.txt"); 
}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String[] op2=req.getParameterValues("ch");
		String exception="nonsupp";
		
	if (op2==null)
	{
		  req.setAttribute("exception", exception);
	        req.getRequestDispatcher("AdminOpe.jsp").forward(req, resp);
	}

	       for(String s:op2 )
	    	   System.out.println(s);
		String nom = "nonsupp";

	     String  path=getServletConfig().getServletContext().getRealPath("/");
	       int i=path.indexOf(".metadata",0);
	       int j= path.indexOf("wtpwebapps",0);
	       String part1=path.substring(0,i-1);
	       String part2=path.substring(j+10,path.length());
	       String newpath=part1.concat(part2);
	       
	    
       for(String s:op2 )
        { 
    	   System.out.println(s);
    	 File repertoire= new File(newpath+"Data");
         Vector<String> liste=new Vector<String>();
		 File [] listefichiers; 
		 listefichiers=repertoire.listFiles(); 
			for(int i1=0;i1<listefichiers.length;i1++)
			{ 
			if((listefichiers[i1].getCanonicalPath()).equals(s))
		        {
			nom=(listefichiers[i1].getName()).substring(0,((listefichiers[i1].getName()).length())-4);
				break;  
			    }}
			File fichier = new File(s);
	    	fichier.delete();
	    	if(new File(s).exists()==false)
	    	 
	    	{
			    InputStream flux=new FileInputStream(newpath+"Urls.txt"); 
			    InputStreamReader lecture=new InputStreamReader(flux);
			    BufferedReader buff=new BufferedReader(lecture);
			    String ligne;
			    int i1=0;
			    while ((ligne=buff.readLine())!=null){
			    	i1++;
			    	if (ligne.equals(nom))
			    		{
			    		break;
			    		}
			    	}
			    lecture.close();
				   buff.close();
			   try {
					supprimer(newpath,i1);
					exception="supp";
					  req.setAttribute("exception", exception);
				        req.getRequestDispatcher("AdminOpe.jsp").forward(req, resp);
					 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					
				}

		     }
	    	
       }     		 		
  
}
}