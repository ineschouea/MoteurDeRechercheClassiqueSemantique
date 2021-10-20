
	
	package metier;

	import java.io.*;



import java.util.ArrayList;




	public class Operation {
		
		private ArrayList<Document> collection=new ArrayList<Document>();

		public ArrayList<Document> getCollection() {
			return this.collection;
		}

		public void setCollection(ArrayList<Document> collection) {
			this.collection = collection;
		}
		
		public void add(Document d)
		{
			this.collection.add(d);
		}
		
		public void remove(String path)
		{
			for(Document d:this.collection)
			{
				if(d.getUrl()==path)
					{this.collection.remove(d);
					break;
					}
			}
		}
		
		public ArrayList<Document> getAll() {
			return this.collection;
		}

	public boolean UrlFound(String url,String path) throws IOException,FileNotFoundException{
    boolean l=false;

    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path+"Urls.txt"))); 

    String line;    
    while ((line = reader.readLine()) != null) {  
    	if (line.equals(url))
    		{
    		l=true;
    		break;
    		
    		}
    }
    reader.close();
    return l; 			
	}
	
	public String recupUrl(String nom, String path) throws IOException,FileNotFoundException{
	    String urlex="";
	    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path+"Urls.txt"))); 

	    String line;    
	    while ((line = reader.readLine()) != null) {  
	    	if (line.equals(nom))
	    		{
	    		line=reader.readLine();
	    		urlex=line;
	    		break;	
	    		}
	    }
	    reader.close();
	    return urlex; 			
		}
	
	public boolean exist(Document doc)
	{  
	    boolean ex=false;
		ArrayList<Document> documents = this.collection;
		for(Document d:documents)
		{
			if(doc.getNom().equals(d.getNom()))
				{ex=true;
				break;
				}
		    }
		return ex;
	}
	
	public static float ifExistGetScore(Operation op,Document doc)
	{  
	    float score=0;
	    
		ArrayList<Document> documents = op.getAll();
		if (documents.size()==0)
			return 0;
		else {
		for(Document d:documents)
		{
			if(d.getNom().equals(doc.getNom()))
				{score=d.getScore();
				break;
				}
		    }
		return score;
		}
	}
	
public String getAbstract(Document doc,String path) throws IOException
{
	String abstractt="";
	   BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path+"abstract.txt"))); 

	    String line;    
	    while ((line = reader.readLine()) != null) {  
	    	if (line.equals(doc.getNom()))
    		{
    		line=reader.readLine();
    		abstractt=line;
    		break;	
    		}
    }
    reader.close();
    
	return abstractt;
}

public static Operation limiter(Operation op,int n) {
	
	Operation opex=new Operation(); 
	if(op.getAll().size()<=n)
	{
	for(Document d:op.getAll())
		opex.add(d);
	}else {
		int k=0;
		for(Document d:op.getAll())
		{  
			opex.add(d);
		   k++;
		if( k==n)
			break;
		}
		
	}
	
	
	return opex;
}

public static float max(Operation op) {
	if (op.getAll().size()==0)
		return -1;
	else {
	float maxScore=op.getAll().get(0).getScore();
	for(Document doc:op.getAll())
	{
		if(doc.getScore()>maxScore)
			maxScore=doc.getScore();
	}
	return maxScore;
}

}
public static boolean nom_doc_exist(Operation op,String nom) {
	 boolean ex=false;
		ArrayList<Document> documents = op.collection;
		for(Document d:documents)
		{
			if(d.getNom().equals(nom))
				{ex=true;
				break;
				}
		    }
		return ex;
	
}
	}	
	

