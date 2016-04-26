package br.ufba.matc96.tagcloud;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.MultiPostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class TwitterSearcher
{
	IndexReader reader;
	IndexSearcher indexSearcher;
	QueryParser queryParser;
	Query query;

	public TwitterSearcher(String indexDirectoryPath) throws IOException
	{
		Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath).toPath());
		reader = DirectoryReader.open(indexDirectory);
		indexSearcher = new IndexSearcher(reader);
		Reader fReader = new FileReader(new File("data/portugues-stop-words.txt"));
		queryParser = new QueryParser(LuceneConstants.CONTENTS, new StandardAnalyzer(fReader));
	}

	public TopDocs search(String searchQuery) 
			throws IOException, ParseException
	{
		query = queryParser.parse(searchQuery);
		return indexSearcher.search(query, LuceneConstants.MAX_SEARCH, Sort.RELEVANCE, true, false);
	}
	
	public HashMap<String, Tag> getTags() throws IOException, ParseException
	{
		HashMap<String, Tag> tags = new HashMap<String, Tag>();
		Terms terms = MultiFields.getTerms(reader, LuceneConstants.CONTENTS);
		TermsEnum termEnum = terms.iterator();
		
		BytesRef bytesRef;
		while((bytesRef = termEnum.next()) != null)
		{
			String tagName = termEnum.term().utf8ToString();
			Tag tag = new Tag(tagName, (int)(termEnum.totalTermFreq()));
			tags.put(tagName,tag);
			ArrayList<String> docs = new ArrayList<>();
			Query q = queryParser.parse(tagName);
	        TopDocs result = indexSearcher.search(q, 100);
	        
	        for (ScoreDoc sd : result.scoreDocs){
	            Document Doc = indexSearcher.doc(sd.doc);
	            docs.add(Doc.get("title"));
	        }
	        tag.setDocs(docs);
		}
		return tags;
	}
	
	public SymmetricTagMatrix getCooccurrenceMatrix() throws IOException, ParseException
	{
		SymmetricTagMatrix matrix = new SymmetricTagMatrix();
		for (int i=0; i<reader.maxDoc(); i++)
		{
			Document doc = reader.document(i);
		    if (doc == null)
		        continue;
		    
		    Terms terms = reader.getTermVector(i, LuceneConstants.CONTENTS);
		    TermsEnum termEnum = terms.iterator();
		    List<String> tags = new LinkedList<String>();

			BytesRef bytesRef;
			while((bytesRef = termEnum.next()) != null)
			{
				tags.add(termEnum.term().utf8ToString());
			}
		    
			for(int j = 0; j < tags.size(); j++)
			{
				String t1 = tags.get(j);
				for(int k = j+1; k < tags.size(); k++)
				{
					String t2 = tags.get(k);
					matrix.increaseValue(t1, t2);
				}
			}
		}

		return matrix;
	}

	public List<Tag> getIndex() throws IOException
	{
		List<Tag> tags = new ArrayList<Tag>();
		Terms terms = MultiFields.getTerms(reader, LuceneConstants.CONTENTS);
		TermsEnum termEnum = terms.iterator();
		
		BytesRef bytesRef;
		while((bytesRef = termEnum.next()) != null)
		{
			tags.add(new Tag(termEnum.term().utf8ToString(),
					(int)(termEnum.totalTermFreq()))
					);
		}
		for(Tag t: tags)
		{
			System.out.println(t.toString());
		}
		return tags;
	}

	public Document getDocument(ScoreDoc scoreDoc) 
			throws CorruptIndexException, IOException
	{
		return indexSearcher.doc(scoreDoc.doc);	
	}
}
