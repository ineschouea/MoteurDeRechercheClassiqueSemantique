
package web;

import java.io.*;



import java.net.URL;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.SysexMessage;

import org.wikipedia.Wiki;

import help.Collection;
import help.Sparql;
import metier.Document;
import metier.Operation;


public class AjoutServlet  extends HttpServlet {
	
	Operation ope=new Operation();

    

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
        String addDoc=req.getParameter("addDoc")	;	 
        String addCol=req.getParameter("addCol")	;	 

		System.out.println("addDoc=="+addDoc);
		System.out.println("addCol=="+addCol);

		String exception="";
		
	     String  path=getServletConfig().getServletContext().getRealPath("/");
	       int i=path.indexOf(".metadata",0);
	       int j= path.indexOf("wtpwebapps",0);
	       String part1=path.substring(0,i-1);
	       String part2=path.substring(j+10,path.length());
	       String newpath=part1.concat(part2);
	       Wiki enWiki = Wiki.newSession("en.wikipedia.org"); 
	       enWiki.setMaxLag(-1);
	      
	        
			if(addCol==null)
{
				String url=req.getParameter("url");
		if(ope.UrlFound(url,newpath)==true)
		
			{exception="urlFound";
	        req.setAttribute("exception2", exception);
	        req.getRequestDispatcher("AdminOpe.jsp").forward(req, resp);
			}
		else{
			
			ajouterDoc(url, newpath,enWiki);
			exception="ok";
			req.setAttribute("exception2", exception);
			req.getRequestDispatcher("AdminOpe.jsp").forward(req, resp);
			}
}
			else	if(addDoc==null)
			{
				String col=req.getParameter("collection");
				System.out.println(col);
				
                 
				 try {
					new Collection(newpath,Integer.parseInt(col));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(newpath+"\\collection.txt"))); 
				    String line;    
				    while ((line = reader.readLine()) != null) {  
				       if(ope.UrlFound(line,newpath)==false)
				    	ajouterDoc(line, newpath, enWiki);
				    		}
				    
				    reader.close();
				    exception="okc";
			     req.setAttribute("exception2", exception);
				 req.getRequestDispatcher("AdminOpe.jsp").forward(req, resp);
			}
	}
	public void ajouterDoc(String url, String newpath, Wiki enWiki) throws IOException, FileNotFoundException{
		String fname=url.substring(url.indexOf("/wiki/",0)+6, url.length());
		System.out.println(fname);
	if(fname.indexOf('/')!=-1)
	fname=fname.replace('/', '-');
	if(fname.indexOf('?')!=-1)
	fname=fname.replace('?', '-');
	
	
	     //ajouter le fichier
				
	            FileWriter fw = new FileWriter(newpath+fname+".txt", true);
				BufferedWriter output = new BufferedWriter(fw);
			    String msg= enWiki.getPageText(fname);
			    
				try {
				
					output.write(msg);
					output.flush();
					output.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				FileWriter f = new FileWriter(newpath+"Data\\"+fname+".txt", true);
				BufferedWriter bw = new BufferedWriter(f);
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(newpath+fname+".txt"))); 
			 String line;    
			 while ((line = reader.readLine()) != null) {   
				 if(line.indexOf("[[Category:")==-1)
			 	    bw.write(line);
					bw.newLine();
					bw.flush();
					 
			     }
			   bw.close();
			   reader.close();
			   File file=new File(newpath+fname+".txt");
			   file.delete();	 
				 
	            //ajouter url et nom
				FileWriter f1 = new FileWriter(newpath+"\\Urls.txt", true);
				BufferedWriter bw1 = new BufferedWriter(f1);
				bw1.write(fname);
				bw1.newLine();
				bw1.write(url);
				bw1.newLine();
				bw1.flush();
				bw1.close();
				System.out.println(fname);
				//ajouter abstract
				   String query ="select ?s  where { \r\n" + 
				   		"<http://dbpedia.org/resource/"+fname+"> <http://dbpedia.org/ontology/abstract> ?s.\r\n" + 
				   		"filter(langMatches(lang(?s),\"en\")) \r\n" + 
				   		"}";
				
				 new Sparql(fname,"abstract",query,newpath,true);	
		        //ajouter Doc
				Document d = new Document(fname, newpath+"Data\\"+fname+".txt", url);
				ope.add(d);
		
	}
}


