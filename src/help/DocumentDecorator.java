package help;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.displaytag.decorator.TableDecorator;

import metier.Document;
import metier.Operation;

public class DocumentDecorator extends TableDecorator {
	
	public long getId()
    {
        Document doc = (Document)getCurrentRowObject();
       
        return  getListIndex()+1;
    }

  
    public float getScore()
    {
    	Document doc = (Document)getCurrentRowObject();
        return doc.getScore();
    }

    public String getNom() throws FileNotFoundException, IOException
    {
    	Document doc = (Document)getCurrentRowObject();
    	Operation op=new Operation();
    	String newpath="C:\\Users\\toshiba\\eclipse-workspace\\MoteurClassiqueSemantique\\";
    	
    	String url = "<a href=\""+op.recupUrl(doc.getNom(), newpath)+"\">"+doc.getNom()+ "</a>";
    	
    	String desc="<div class=\"desc\">"+op.getAbstract(doc, newpath)+"</div>";
        return url+"<br><br>"+desc+"<br>";
    }
  

}
