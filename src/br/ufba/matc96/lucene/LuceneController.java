package br.ufba.matc96.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import br.ufba.matc96.tagcloud.Tag;
import br.ufba.matc96.tagcloud.TagDocument;

public class LuceneController
{
	String indexDir;
	LuceneIndexer indexer;
	LuceneSearcher searcher;
	
	public LuceneController()
	{
		this.indexDir = "index/";
		try
		{
			indexer = new LuceneIndexer(this.indexDir);
			searcher = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public LuceneController(String indexDir)
	{
		this.indexDir = indexDir;
		try
		{
			indexer = new LuceneIndexer(this.indexDir);
			searcher = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void createIndex(LuceneFileProvider<?> fileProvider) throws IOException
	{
		int numIndexed;
		long startTime = System.currentTimeMillis();
		numIndexed = indexer.createIndex(fileProvider);
		long endTime = System.currentTimeMillis();
		indexer.close();
		//Sets up searcher
		searcher = new LuceneSearcher(this.indexDir);

		System.out.println(numIndexed+" file(s) indexed, time taken: "
				+(endTime-startTime)+" ms");
	}
	
	public List<String> search(String query) throws IOException, ParseException
	{
		if(searcher == null)
			searcher = new LuceneSearcher(this.indexDir);

		List<String> docs = new ArrayList<String>();
		TopDocs result = searcher.search(query);

        for (ScoreDoc sd : result.scoreDocs)
        {
            Document Doc = searcher.getDocument(sd);
            docs.add(Doc.get(LuceneConstants.FILE_NAME));
        }

		return docs;
	}
	
	public void printIndex() throws IOException, ParseException
	{
		searcher.getIndex();
	}
	
	public HashMap<String, Tag> getTags() throws IOException, ParseException
	{
		return this.searcher.getTags();
	}
	
	public List<TagDocument> getTagDocuments() throws IOException, ParseException
	{
		return this.searcher.getTagDocuments();
	}
}