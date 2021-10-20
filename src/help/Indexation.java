package help;


import java.io.IOException;



public class Indexation {
	public String exception="";
	//String indexDir="Index";
	 // String dataDir="Data";
	  
	String indexDir="Index2";
	 String dataDir="Data2";

		public  Indexation(String path) 
		{
			Indexer indexer;
			try {
				indexer = new Indexer(indexDir,path);
				indexer.createIndex(path,dataDir, new TextFileFilter());
				indexer.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				exception="indexation";
			}
		
		}

}
