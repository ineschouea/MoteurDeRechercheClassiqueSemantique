package metier;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import help.LuceneConstants;


public class ModifIndex {
	

	
	//termes de collection
	public static Set<String> buildIndexTerms(String index_path, String field) throws IOException {
		Set<String> terms = new HashSet<String>();
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index_path)));
		Terms all_terms = SlowCompositeReaderWrapper.wrap(reader).terms(field);
		TermsEnum termsEnum = all_terms.iterator(null);
		BytesRef text;
		String term =null;
		while((text = termsEnum.next()) != null) {
			term = text.utf8ToString().toLowerCase();
			terms.add(term);
		}
		return terms;
	}
	
	//créer un vecteur à partir de la collection
	
	public static RealVector build_reference(String index_path, Set<String> terms, String field, double vref_norm /* somme de fréquence de tous les termes dans la collection*/) throws IOException {
		RealVector vref = new ArrayRealVector(terms.size());
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index_path)));
		vref = new ArrayRealVector(terms.size());
		int i=0;
		double vref_entry=0;
		for(String term : terms){
			System.out.println(reader.totalTermFreq(new Term(field, (String)term)));
			vref_entry=1.0*reader.totalTermFreq(new Term(field, (String)term))/vref_norm;
			vref.setEntry(i++, vref_entry);
		}
		reader.close();
		return vref;
	}
	
	public static HashMap<String, Integer> getTermFrequencies(IndexReader reader, int docId, String field) throws IOException {
		Terms vector = reader.getTermVector(docId, field);
		if(vector==null) return null;
		TermsEnum termsEnum = null;
		termsEnum = vector.iterator(termsEnum);
		HashMap<String, Integer> frequencies = new HashMap<String,Integer>();
		BytesRef text = null;
		while ((text = termsEnum.next()) != null) {
			String term = text.utf8ToString();
			term=term.toLowerCase();
			int freq = (int) termsEnum.totalTermFreq();
			frequencies.put(term, freq);
		}
		return frequencies;
	}
	
	public static Map<String, Integer> getReqTermFrequencies(String request) throws IOException  {
		IndexReader reader = DirectoryReader.open(indexInRAM(request));
		Map<String, Integer> f1 = getTermFrequencies(reader, 0, "content");
		return f1;
	}


/***
 * Index a text in RAM, an return the directory to use it in search 
 * @param texte
 * @return
 * @throws IOException
 */
public static Directory indexInRAM(String texte) throws IOException{
    Directory directory = new RAMDirectory();
    //Collection<String> stop_words_collection = Arrays.asList(StopWords.stop_words);
	//CharArraySet stop_words = new CharArraySet(Version.LUCENE_47, stop_words_collection, true);
	//Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47,stop_words);
    IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, new StandardAnalyzer(Version.LUCENE_47));
    IndexWriter writer = new IndexWriter(directory, iwc);
    org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
    Field field = new Field("content", texte, Lucene_Field_Type_Stored.TYPE_STORED);
    doc.add(field);
    writer.addDocument(doc);;
    writer.close();
    return directory;
}




public static RealVector toRealVector(Map<String, Integer> map, Set<String> terms) throws IOException {
		if(map ==null) return null;
		RealVector vector = new ArrayRealVector(terms.size());
		int i = 0;
		for (String term : terms) {
			int value = map.containsKey(term) ? map.get(term) : 0;
			vector.setEntry(i, value);
			i++;
		}
		return vector;
	}

public static RealVector idftoRealVector( Set<String> terms, TopDocs t,IndexReader reader) throws IOException {
	
	RealVector vector = new ArrayRealVector(terms.size());
	   for(ScoreDoc scoreDoc:t.scoreDocs) {
		   Map<String, Integer> doc_map =getTermFrequencies(reader, scoreDoc.doc, LuceneConstants.CONTENTS);
		   RealVector doc_rv=ModifIndex.toRealVector(doc_map, terms);
		   for(int j=0;j<doc_rv.getDimension();j++)
		   {
			   if(doc_rv.getEntry(j)!=0)
				   doc_rv.setEntry(j, 1);
		   }
		  vector.add(doc_rv);//df de terms
		
	}
	for(int i=0;i<vector.getDimension();i++)
	{
		vector.setEntry(i,Math.log((terms.size())/vector.getEntry(i))); //  idf de terms : idf(t) =  log( N / df)
	}
	
	return vector;
}

public static RealVector getTFIDF_Vector(String index_path, Set<String> terms, String field, RealVector vd_freq) throws IOException {

	if(vd_freq==null) return null;

	

	double entry = 0;

	double df =0;

	double idf =0;

	double tf = 0;

	double N=0;

	

	IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index_path)));

	for (int i = 0; i<terms.size(); i++) {

		entry=vd_freq.getEntry(i);

		if(entry==0) continue;

		df = reader.docFreq(new Term(field, (String)terms.toArray()[i]));

		N = reader.numDocs();

		idf = Standard_Weighting.getIDF(df, N, "s");

		tf = Standard_Weighting.getTF(entry, "l");

		vd_freq.setEntry(i, tf*idf);

	}

	return vd_freq;

}




}
