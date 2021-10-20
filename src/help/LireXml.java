package help;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class LireXml {
	 static org.jdom.Document document;
	   static Element racine;

	   public Vector<String> lireXml(int indice, String newpath)
	   {
		   Vector<String> req=new Vector<String>();
	      //On cr�e une instance de SAXBuilder
	      SAXBuilder sxb = new SAXBuilder();
	      try
	      {
	         //On cr�e un nouveau document JDOM avec en argument le fichier XML
	         //Le parsing est termin� ;)
	         document = sxb.build(new File(newpath+"Requests.xml"));
	      }
	      catch(Exception e){}

	      //On initialise un nouvel �l�ment racine avec l'�l�ment racine du document.
	      racine = document.getRootElement();

	      //Recup�rer les �l�ments
	    //On cr�e une List contenant tous les noeuds "etudiant" de l'Element racine
	      List listRequests = racine.getChildren("request");

	      //On cr�e un Iterator sur notre liste
	      Iterator i = listRequests.iterator();
	      int j=0;
	      while(i.hasNext())
	      {
	       
	         { Element courant = (Element)i.next();
	           j++;
			if(j==indice)
			{
	        req.add(courant.getChild("numero").getText());
	        req.add(courant.getChild("mots_cles").getText());
	        req.add(courant.getChild("description").getText());
			}

	   }
	    
	   }
		return req;

}}
