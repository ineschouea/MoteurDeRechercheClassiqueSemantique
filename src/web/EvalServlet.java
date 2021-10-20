package web;

import java.io.BufferedReader;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import help.LireXml;
import metier.Document;
import metier.Operation;
import metier.Request;

public class EvalServlet extends HttpServlet {
	int i=0;

	
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String newpath="";
		if(req.getAttribute("newpath")==null)
		{String  path=getServletConfig().getServletContext().getRealPath("/");
	       int i=path.indexOf(".metadata",0);
	       int j= path.indexOf("wtpwebapps",0);
	       String part1=path.substring(0,i-1);
	       String part2=path.substring(j+10,path.length());
	       newpath=part1.concat(part2);
	       System.out.println(newpath);}
		else 
		{
			newpath=(String) req.getAttribute("newpath");
		}
	
		
			
		int start=0;
		System.out.println(req.getParameter("indice"));
		i=Integer.parseInt(req.getParameter("indice"));
		Vector<String> vect=new Vector<String>();
		PrintWriter pw=resp.getWriter();
	
	
		 
		
		
	      
			
    //generer la requête i
	i++;
    vect=new LireXml().lireXml(i,newpath);
    Request reqt=new Request(Integer.parseInt(vect.get(0)), vect.get(1), vect.get(2));
 
	req.setAttribute("indice", i);
		req.setAttribute("reqt", reqt);
		req.setAttribute("start", start);
		req.setAttribute("newpath", newpath);
		   
		 
		req.getRequestDispatcher("Eval.jsp").forward(req, resp);
		

    }}
		

