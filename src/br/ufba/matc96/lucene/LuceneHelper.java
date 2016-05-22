package br.ufba.matc96.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import br.ufba.matc96.tagcloud.SymmetricTagMatrix;
import br.ufba.matc96.tagcloud.Tag;
import twitter4j.TwitterException;

public class LuceneHelper
{
	String indexDir;
	String twitterHandle;
	LuceneIndexer indexer;
	LuceneSearcher searcher;
	
	public LuceneHelper()
	{
		this.indexDir = "index/";
		this.twitterHandle = "";
		try
		{
			//searcher = new Searcher(this.indexDir);
			indexer = new LuceneIndexer(this.indexDir);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public LuceneHelper(String indexDir, String twitterHandle)
	{
		this.indexDir = indexDir;
		this.twitterHandle = twitterHandle;
		try
		{
			//searcher = new Searcher(this.indexDir);
			indexer = new LuceneIndexer(this.indexDir);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void createIndex() throws IOException, TwitterException
	{
		int numIndexed;
		long startTime = System.currentTimeMillis();
		numIndexed = indexer.createIndex(this.indexDir);
		long endTime = System.currentTimeMillis();
		indexer.close();
		System.out.println(numIndexed+" File indexed, time taken: "
				+(endTime-startTime)+" ms");
		searcher = new LuceneSearcher(this.indexDir);
	}
	
	public ArrayList<String> search(String query) throws IOException, ParseException
	{
		searcher = new LuceneSearcher(this.indexDir);
		ArrayList<String> docs = new ArrayList<String>();
		TopDocs result = searcher.search(query);

        for (ScoreDoc sd : result.scoreDocs)
        {
            Document Doc = searcher.getDocument(sd);
            docs.add(Doc.get(LuceneConstants.FILE_NAME));
        }

		return docs;
	}
	
	public Integer searchHits(String query) throws IOException, ParseException
	{
		TopDocs result = searcher.search(query);
		return result.totalHits;
	}
	
	public void printIndex() throws IOException, ParseException
	{
		searcher.getIndex();
	}
	
	public HashMap<String, Tag> getTags() throws IOException, ParseException
	{
		return this.searcher.getTags();
	}
	
	public SymmetricTagMatrix getCooccurrenceMatrix() throws IOException, ParseException
	{
		return searcher.getCooccurrenceMatrix();
	}
}