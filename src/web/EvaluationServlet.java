package web;

import java.io.IOException;

import java.sql.DriverManager;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;

import metier.Operation;


public class EvaluationServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String newpath=req.getParameter("newpath");
		String docNom=req.getParameter("DocNom");
		 String requete=req.getParameter("req");
		int indice=Integer.parseInt(req.getParameter("indice"));
		int start=Integer.parseInt(req.getParameter("start"));

		try {
			//Class Driver
			Class.forName("com.mysql.jdbc.Driver");
			//connector
			Connection cn=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/evaluation","root","");
			//objet commande
					
			int value = 0;
			String rad[]=req.getParameterValues("pertinence");
			
			 if(rad[0].equals("peu-pertinent"))
			       value=1;
			
			  else if(rad[0].equals("pertinent"))
				  value=2;
		
			  else if(rad[0].equals("tres_pertinent"))
				  value=3;
			  
			String  sqlReq="INSERT INTO docsevaluation VALUES(?,?,?)";
			PreparedStatement pr=(PreparedStatement) cn.prepareStatement(sqlReq);
			pr.setInt(1, indice);
			pr.setString(2, docNom);
			pr.setInt(3, value);
			
			
	
			//execution
			pr.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			 
		
		req.setAttribute("newpath", newpath);
		req.setAttribute("suivant", "oui");
		req.setAttribute("req", requete);
		;
		String choix=req.getParameter("choix");
		req.setAttribute("indice", indice);
		if(choix.equals("Requête suivante"))
			{
			new EvalServlet().doPost(req, resp);
			}
		else if(choix.equals("Bilan"))
			new ResultatServlet().doPost(req, resp);
		else {
		
			req.setAttribute("start", start);
			new EvalServlet2().doPost(req, resp);
		}
	}

}
