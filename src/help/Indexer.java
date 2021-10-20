package help;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.wikipedia.*;

import metier.Lucene_Field_Type_Stored;
import metier.Operation;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class Indexer {
	

	private IndexWriter writer=null;
	Operation ope=new Operation();
	BufferedWriter bw;
	FileWriter f ;
	public Indexer(String indexDirectoryPath,String path) throws IOException
	{
		// f = new FileWriter(path+"document.txt", false);
		f = new FileWriter(path+"document2.txt", false);
	 bw = new BufferedWriter(f);
		//Ce document va contenir l'index	
	Directory indexDirectory=FSDirectory.open(new File(path+indexDirectoryPath));
//System.out.println("indexDirectoryPath=="+path+indexDirectoryPath);
	    //créer l'index
	IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47,	new StandardAnalyzer(Version.LUCENE_47));
	//IndexWriter indexwriter;
	writer = new IndexWriter(indexDirectory, config);
		
	}
	
	

	public void close() throws CorruptIndexException, IOException{
			
		writer.close();
		bw.close();
	
		}
	
	private Document getDocument(File file, String path) throws IOException
	{
		
	
		Document document=new Document();
		
		String body =""; 
		
	    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file))); 

	    String line;    
	    while ((line = reader.readLine()) != null) {  
	    	
	    		body+=" "+line;
	    			
	    		}
	    
	    reader.close();
		
		//le contenu du fichier d'index
		Field contentField=new Field(LuceneConstants.CONTENTS, body , Lucene_Field_Type_Stored.TYPE_STORED);
		
		//nom de fichier d'index
		Field filenameField=new TextField(LuceneConstants.FILE_NAME, 
				                    file.getName(), 
				                    Field.Store.YES);
		    bw.write("File txt name=="+file.getName());
			bw.newLine();
			bw.flush();
System.out.println( "File txt name=="+file.getName());

		//le chemin de fichier d'index
		Field filePathField=new TextField(LuceneConstants.FILE_PATH, 
				        file.getCanonicalPath(), Field.Store.YES);
System.out.println(file.getCanonicalPath());

		//l'url de fichier
		String fname=file.getName().substring(0,file.getName().length()-4);
System.out.println("file name=="+fname);
		Field fileUrlField= new TextField(LuceneConstants.FILE_URL, 
				ope.recupUrl(fname,path), Field.Store.YES);	
System.out.println("url=="+ope.recupUrl(fname,path));
	
//Concepts
   String concept0=new Concept().concept(fname, 0, path);
	System.out.println("concept0="+concept0);
 String concept1=new Concept().concept(fname, 1, path);
	System.out.println("concept1="+concept1);
 String concept2=new Concept().concept(fname, 2, path);
	System.out.println("concept2="+concept2);
	
String concept=concept0+" \n"+concept1+" \n"+concept2;
System.out.println("concept"+concept);
 //catégories
String categories="";
Wiki enWiki = Wiki.newSession("en.wikipedia.org");
enWiki.setMaxLag(-1);

for (String cat : enWiki.getCategories(fname))
	{
	cat=cat.replace('@', ' ');
	cat=cat.replaceAll("Category:", " ");
	categories+=" "+cat;
	
	}
System.out.println("categories="+categories);
      Field filesemantic= new Field(LuceneConstants.FILE_Semantic,concept+" \n"+categories,Lucene_Field_Type_Stored.TYPE_STORED);
      bw.write("Semantic="+concept+" \n"+categories);
      bw.newLine();
      bw.newLine();
      bw.newLine();
      bw.flush();
//System.out.println("CATHEGORIE="+categories);

		document.add(contentField);
		document.add(filenameField);
		document.add(filePathField);
		document.add(fileUrlField);
		document.add(filesemantic);
	
		return document;
	
}
	private void indexFile(File file, String path) throws IOException
	{
		//System.out.println("Indexing"+file.getCanonicalPath());
		Document document=getDocument(file,path);
		writer.addDocument(document);
	}
	
	public void createIndex (String path,String dataDirPath, TextFileFilter textFileFilter) throws IOException
	{
		//avoir tous les fichier dans le dossier data
		File[] files=new File(path+dataDirPath).listFiles();
		for(File file:files)
		{
			if(!(file.isDirectory()&& !file.exists()&& file.canRead()&& textFileFilter.accept(file)))
					{
				    indexFile(file,path);
					}	
		}
			
		
	}
	
}
