package help;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

public class Collection {
	
	
public Collection(String newpath,int indice) throws IOException,Exception {
	
	String query ="";
    switch (indice) {
    case 1: query="SELECT ?s WHERE {\r\n" + 
    		"?s <http://purl.org/dc/terms/subject><http://dbpedia.org/resource/Category:Home_computer_hardware_companies>.\r\n" + 
    		"}\r\n" + 
    		"";
             break;
    case 2: query="SELECT ?s WHERE {\r\n" + 
    		"?s <http://purl.org/dc/terms/subject><http://dbpedia.org/resource/Category:Muslim-majority_countries>.\r\n" + 
    		"}\r\n" + 
    		"";
             break;
    case 3: query="SELECT ?s WHERE {\r\n" + 
    		"?s <http://purl.org/dc/terms/subject><http://dbpedia.org/resource/Category:Roses>.\r\n" + 
    		"}\r\n" + 
    		"";
             break;
    case 4: query="SELECT ?s WHERE {\r\n" + 
    		"?s <http://purl.org/dc/terms/subject><http://dbpedia.org/resource/Category:Programming_languages>.\r\n" + 
    		"}\r\n" + 
    		"";
             break;
    case 5: query="SELECT ?s WHERE {\r\n" + 
    		"?s <http://purl.org/dc/terms/subject><http://dbpedia.org/resource/Category:Near_threatened_animals>.\r\n" + 
    		"}\r\n" + 
    		"";
             break;
    case 6: query="SELECT ?s WHERE {\r\n" + 
    		"?s <http://purl.org/dc/terms/subject><http://dbpedia.org/resource/Category:Ball_games>.\r\n" + 
    		"}\r\n" + 
    		"";
             break;
           
    }
    
        new Sparql("","file",query,newpath,false);
	FileWriter f = new FileWriter(newpath+"collection.txt", false);
	BufferedWriter bw = new BufferedWriter(f);
	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(newpath+"file.txt"))); 
 String line;    
 while ((line = reader.readLine()) != null) {   
	  line=line.substring(line.indexOf("resource")+9,line.length());
	  line="https://en.wikipedia.org/wiki/"+line;
 	    bw.write(line);
		bw.newLine();
		bw.flush();
		 
     }
   bw.close();
   reader.close();
   File file=new File(newpath+"file.txt");
   file.delete();

	}

}



