package help;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.Similarity.SimScorer;
import org.apache.lucene.search.similarities.Similarity.SimWeight;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import  org.apache.lucene.search.Query;
	public class Searcher {

		IndexSearcher indexSearcher;
		QueryParser queryParser;
	    Query query;
		
	public Query getQuery() {
			return query;
		}
	
	public Searcher (String indexDirectoryPath,String field ) throws IOException
	{

		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexDirectoryPath))); // filtered index
		indexSearcher= new IndexSearcher(reader);
		queryParser= new QueryParser(Version.LUCENE_47, field, new StandardAnalyzer(Version.LUCENE_47));
	}
	public TopDocs search(String searchQuery) throws IOException, ParseException
	{
	query= queryParser.parse(searchQuery);
	return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
	}
	public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException,IOException
	{
		return indexSearcher.doc(scoreDoc.doc);
	}
	/*public void close()throws IOException
	{
		indexSearcher..close();
	}*/
	
	
}