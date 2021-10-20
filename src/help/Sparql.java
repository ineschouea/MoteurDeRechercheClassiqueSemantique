package help;

import java.io.BufferedWriter;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

public class Sparql {
	
public Sparql(String fname,String file,String query,String newpath, boolean nonEcraser) throws IOException {
	
	try{
        FileWriter fostream = new FileWriter(newpath+file+".txt", nonEcraser);
        BufferedWriter out = new BufferedWriter(fostream);
        String service ="http://dbpedia.org/sparql";
        
        QueryExecution qexecctest = QueryExecutionFactory.sparqlService(service, query);

        try {
        ResultSet responseetest = qexecctest.execSelect();

          if(fname!="")
     		{
            out.write(fname);
     		out.newLine();
     		out.flush(); 
     		}
          String triple;
          if( responseetest.hasNext()==false)
          {out.write("*******************");
          out.newLine();}
         while( responseetest.hasNext()){
            QuerySolution solnntest = responseetest.nextSolution();
            RDFNode body = solnntest.get("?s");
            triple= body.toString();
            if((fname!="") && (triple.length()>400))
            {
            	String newTriple = "";
            	int index;
			    newTriple = triple.substring(0,400);
                index=newTriple.lastIndexOf(" ");
            	out.write(triple.substring(0, index).replace("@en", "")+"....");
            	out.newLine();	
			
          
            }else {
            	out.write(triple.replace("@en", ""));
            	out.newLine();
            }
            }
       
         
        } finally {
        qexecctest.close();
        out.close();}

        }catch (Exception e){
          System.err.println("Error: " + e.getMessage());
		}


}
}