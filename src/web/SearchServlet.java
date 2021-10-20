package web;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import help.LuceneConstants;
import help.Searcher;
import metier.Document;
import metier.ModifIndex;
import metier.Operation;
import metier.SortByScore;

public class SearchServlet extends HttpServlet {

	Operation  op=new Operation();

	String indexDir ="";
	String sr="";
	String newpath="";
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		
		 int indice = 0;
		 String newpath;
		 if(req.getAttribute("newpath")!=null)
			 newpath=(String) req.getAttribute("newpath") ;
		 else if(req.getParameter("newpath")!=null)
			 newpath=req.getParameter("newpath") ;
		 else
		 {
		   String  path=getServletConfig().getServletContext().getRealPath("/");
	       int i=path.indexOf(".metadata",0);
	       int j= path.indexOf("wtpwebapps",0);
	       String part1=path.substring(0,i-1);
	       String part2=path.substring(j+10,path.length());
	       newpath=part1.concat(part2);
		 }
		
	       indexDir=newpath+"\\"+"Index";
	    	//PrintWriter pw=resp.getWriter();
        
	   
		String sr=req.getParameter("recherche");
	    String choix=req.getParameter("choix");
        SearchBeans sb=new SearchBeans();
        
        
        
    	Searcher searcher = null,searcher2=null;
        TopDocs hits = null,hitsC=null,hitsS=null;
      

    	
    if(choix.equals("Documents pertinents"))
    { 
      
        indice=Integer.parseInt(req.getParameter("indice")); 		

       
    	// Vector <String>vect=new LireXml().lireXml(indice,newpath);
    	
    	 String requete=(String) req.getParameter("req");
	
		
			
    	 
		
		
    	Operation opC = new Operation(),opS = new Operation(),opC_S = new Operation(),opTout;
    	
    	//op=new Operation();
		try {
			    searcher=new Searcher(indexDir, LuceneConstants.CONTENTS);
				hits=searcher.search(requete);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

opC.setCollection(chercher(searcher, hits, "r-classique",requete));
    	 

    	//op=new Operation();
    	try {
    		searcher=new Searcher(indexDir, LuceneConstants.FILE_Semantic);
			hits=searcher.search(requete);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

opS.setCollection(chercher(searcher, hits, "r-semantique",requete));
    	 
    	//op=new Operation();
    	try {
			searcher=new Searcher(indexDir, LuceneConstants.CONTENTS);
				hitsC=searcher.search(requete);
				searcher2=new Searcher(indexDir, LuceneConstants.FILE_Semantic);
				hitsS=searcher2.search(requete);
				
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
		
		/*chercher(searcher, hitsC, choix,requete);
		chercher(searcher2, hitsS, choix,requete);*/
    	opC_S.setCollection(chercherMix( hitsC,searcher, hitsS,searcher2,requete));
      	  

  opTout=new Operation();
  op=new Operation();

opC=Operation.limiter(opC,20);
opS=Operation.limiter(opS,20);
opC_S=Operation.limiter(opC_S,20);
System.out.println(opC.getAll().size());
System.out.println(opS.getAll().size());
System.out.println(opC_S.getAll().size());


for(Document d:opC.getAll())
	 if(opTout.exist(d)==false)
  opTout.add(d);
for(Document d:opS.getAll())
	 if(opTout.exist(d)==false)
 opTout.add(d);
for(Document d:opC_S.getAll())
	 if(opTout.exist(d)==false)
 opTout.add(d);




req.setAttribute("opTout", opTout);

req.setAttribute("indice", indice);
req.setAttribute("newpath", newpath);
//req.setAttribute("requete", requete);

    	 
	

int start=1;
      	  

req.setAttribute("start", start);
      req.setAttribute("indice", indice);
      req.setAttribute("start", start);
      req.setAttribute("newpath", newpath);
      
      new EvalServlet2().doPost(req, resp);
	}
   
    else if(choix.equals(" Resultat d'évaluation")) {
    	req.setAttribute("res", " ");
    	  String request=  (String) req.getAttribute("request");

    		  Operation opC = new Operation(),opS = new Operation(),opC_S = new Operation(),opTout;
    	    	
    	    	
    			try {
    				    searcher=new Searcher(indexDir, LuceneConstants.CONTENTS);
    					hits=searcher.search(request);
    			} catch (ParseException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}

    	opC.setCollection(chercher(searcher, hits, "r-classique",request));
    	    	 

    	    	//op=new Operation();
    	    	try {
    	    		searcher=new Searcher(indexDir, LuceneConstants.FILE_Semantic);
    				hits=searcher.search(request);
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

    	opS.setCollection(chercher(searcher, hits, "r-semantique",request));
    	    	 
    	    	//op=new Operation();
    	    	try {
    				searcher=new Searcher(indexDir, LuceneConstants.CONTENTS);
    					hitsC=searcher.search(request);
    					searcher2=new Searcher(indexDir, LuceneConstants.FILE_Semantic);
    					hitsS=searcher2.search(request);
    					
    			} catch (ParseException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    		}
    			
    			
    	    	opC_S.setCollection(chercherMix( hitsC,searcher, hitsS,searcher2,request));
    	    	req.setAttribute("newpath", newpath);
    	    	req.setAttribute("opC", opC);
    	    	req.setAttribute("opS", opS);
    	    	req.setAttribute("opC_S", opC_S);
    	      	req.setAttribute("request", request); 
    	  
     
     new ResultatServlet().doPost(req, resp);
    	
    }
    else {
    	
    	if(choix.equals("r-classique")) {
    		 op=new Operation();	
 	
 			try {
 			    searcher=new Searcher(indexDir, LuceneConstants.CONTENTS);
 				hits=searcher.search(sr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 			op.setCollection(chercher(searcher, hits, choix,sr));
 			
    } else if(choix.equals("r-semantique")) {
    	 op=new Operation();	
				try {
					searcher=new Searcher(indexDir, LuceneConstants.FILE_Semantic);
	 				hits=searcher.search(sr);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    
    	op.setCollection(chercher(searcher, hits, choix,sr));
    }
	
          
else if(choix.equals("r-semantique-classique")) {
	 op=new Operation();	
					
					try {
						searcher=new Searcher(indexDir, LuceneConstants.CONTENTS);
		 				hitsC=searcher.search(sr);
		 				searcher2=new Searcher(indexDir, LuceneConstants.FILE_Semantic);
		 				hitsS=searcher2.search(sr);
		 				
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
				ArrayList<Document> list=chercherMix( hitsC,searcher, hitsS,searcher2,sr);
				if(list.size()==0)
					op=null;
				else
				op.setCollection(list);
}
 		
   
 	
 		sb.setList(op.getAll());
		  req.setAttribute("modele", sb);
	      req.getRequestDispatcher("Recherche.jsp").forward(req, resp);
	      
 
    }
    
   
}

	private ArrayList<Document> chercher(Searcher s,TopDocs t,String choix, String request) throws CorruptIndexException, IOException
	{    String field="";
    if(choix.equals("r-classique"))
	field=LuceneConstants.CONTENTS;
    else if(choix.equals("r-semantique"))
    field=LuceneConstants.FILE_Semantic;
	
		Set<String> terms = ModifIndex.buildIndexTerms(indexDir,field);
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexDir)));
		Map<String, Integer> doc_map=null;
		Map<String, Integer> req_map=null;
		RealVector doc_rv = null;
		RealVector doc_rv_TfIdf = null;
		RealVector req_rv = null;
		
		req_map=ModifIndex.getReqTermFrequencies(request);
		req_rv=ModifIndex.toRealVector(req_map, terms);
		op=new Operation();
			
		for(ScoreDoc scoreDoc:t.scoreDocs)
  		{   
			
  			org.apache.lucene.document.Document doc=s.getDocument(scoreDoc);
  			doc_map=ModifIndex.getTermFrequencies(reader, scoreDoc.doc, field);
  			doc_rv=ModifIndex.toRealVector(doc_map, terms);
  			doc_rv_TfIdf=ModifIndex.getTFIDF_Vector(indexDir, terms,field, doc_rv);
  			 double score_classic = (req_rv.dotProduct(doc_rv_TfIdf) ) / ( doc_rv_TfIdf.getNorm() * req_rv.getNorm()) ;
  			
  			String path1=doc.get(LuceneConstants.FILE_PATH);	 
  			String nom=doc.get(LuceneConstants.FILE_NAME).substring(0,(doc.get(LuceneConstants.FILE_NAME).length()-4));
  			String url=doc.get(LuceneConstants.FILE_URL);
  			float score=(float) score_classic;
  			metier.Document d=new Document(nom, path1, url, score);
			if(op.exist(d)==false)
  				op.add(d);
				 
  		}
		ArrayList<Document> list= new ArrayList<Document>();
		list=op.getAll();
		
		 Collections.sort(list, new SortByScore());
		 op.setCollection(list);
		return op.getAll();

        }
       
	private ArrayList<Document> chercherMix(TopDocs hitsC,Searcher searcherC,TopDocs hitsS, Searcher searcherS,String request) throws CorruptIndexException, IOException
	{  
		
		Operation opC = new Operation();
		Operation opnew = new Operation();
		Operation opS = new Operation();

		Operation opTout=new Operation();
		
		opC.setCollection(chercher(searcherC, hitsC, "r-classique", request));
		op=new Operation();
		opS.setCollection(chercher(searcherS, hitsS, "r-semantique", request));

		for(Document d:opC.getAll()) {
			if(opTout.exist(d)==false)
			opTout.add(d);
		}
		for(Document d:opS.getAll()) {
			if(opTout.exist(d)==false)
			opTout.add(d);
		}
		
		float scr_semantic,scr_content,max_scr_semantic,max_scr_content;
		max_scr_semantic=Operation.max(opS);
		max_scr_content=Operation.max(opC);
		for(Document d:opTout.getAll())
		{
			scr_semantic=Operation.ifExistGetScore(opS, d);
			scr_content=Operation.ifExistGetScore(opC, d);
			
			double newscore = 0.5*scr_content/max_scr_content+0.5*scr_semantic/max_scr_semantic;
			    
			   
				d.setScore((float) newscore);
			
				opnew.add(d);

		}
		
		ArrayList<Document> list= new ArrayList<Document>();
		list=opnew.getAll();
		
		 Collections.sort(list, new SortByScore());
		 opnew.setCollection(list);
		 
		 return opnew.getAll();
	}
	
	}
	

	

