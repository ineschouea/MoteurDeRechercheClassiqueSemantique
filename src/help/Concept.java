package help;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

public class Concept {
	
	public String concept(String filename, int ordreConcept, String newpath) throws IOException {
		    String concept="";
	        String query ="";
		 switch(ordreConcept)
		 {
		 case 0:query="SELECT   distinct ?s WHERE {\r\n" + 
			 		"<http://dbpedia.org/resource/"+filename+"><http://purl.org/dc/terms/subject> ?c1.\r\n"+
			 		"?c1 <http://www.w3.org/2000/01/rdf-schema#label> ?s.}\r\n" + 
			 		"";
			 break;
		 case 1:query="SELECT   distinct ?s WHERE {\r\n" + 
		 		"<http://dbpedia.org/resource/"+filename+"><http://purl.org/dc/terms/subject> ?c1.\r\n" + 
		 		"?c1 <http://www.w3.org/2004/02/skos/core#broader> ?c2.\r\n" + 
		 		"?c2 <http://www.w3.org/2000/01/rdf-schema#label> ?s.}\r\n" + 
		 		"";
			 break;
		 case 2:query="SELECT   distinct ?s WHERE {\r\n" + 
		 		"<http://dbpedia.org/resource/"+filename+"><http://purl.org/dc/terms/subject> ?c1.\r\n" + 
		 		"?c1 <http://www.w3.org/2004/02/skos/core#broader> ?c2.\r\n" + 
		 		"?c2 <http://www.w3.org/2004/02/skos/core#broader> ?c3.\r\n" + 
		 		"?c3 <http://www.w3.org/2000/01/rdf-schema#label> ?s.\r\n" + 
		 		"}\r\n" + 
		 		"";
			 break;
		 }
		
		  new Sparql("","concept",query,newpath,false);	
		  
		  BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(newpath+"concept.txt"))); 
		  String line;    
		  while ((line = reader.readLine()) != null) {   
		      line=line.replaceAll("@en"," ");
			  concept+=" "+line;
		 		 
		      }
		   
		    reader.close();
	return concept;	  
}
}