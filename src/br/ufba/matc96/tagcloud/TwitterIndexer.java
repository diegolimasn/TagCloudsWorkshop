package br.ufba.matc96.tagcloud;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import twitter4j.TwitterException;

public class TwitterIndexer
{
	private IndexWriter writer;

	public TwitterIndexer(String indexDirectoryPath) throws IOException
	{
		Reader reader = new FileReader(new File("data/portugues-stop-words.txt"));
		//Analyzer analyzer = new StandardAnalyzer(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		Analyzer analyzer = new StandardAnalyzer(reader);
		
		Directory directory = FSDirectory.open(new File(indexDirectoryPath).toPath());
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(directory, config);
	}
	
	public void close() throws CorruptIndexException, IOException
	{
		writer.close();
	}
	
	private Document getDocument(MyTweet tweet) throws IOException
	{
		Document document = new Document();
		
		//index file contents
		FieldType fieldType = new FieldType();
		fieldType.setStored(false);
		fieldType.setStoreTermVectors(true);
		fieldType.setTokenized(true);
		fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		
		//index file contents
		Field contentField = new Field(LuceneConstants.CONTENTS, tweet.getContent(), fieldType);
		//index file name
		Field fileNameField = new StoredField(LuceneConstants.FILE_NAME, tweet.getId());
		//index file path
		//Field filePathField = new StoredField(LuceneConstants.FILE_PATH, tweet.getId());

		document.add(contentField);
		document.add(fileNameField);
		//document.add(filePathField);

		return document;
	}
	
	private void indexFile(MyTweet tweet) throws IOException
	{
		System.out.println("Indexing "+tweet.getId());
		Document document = getDocument(tweet);
		writer.addDocument(document);
	}
	
	public int createIndex(String twitterHandle) 
			throws IOException, TwitterException
	{
		Corpus corpus = new Corpus();
        ArrayList<MyTweet> tweets = corpus.getTweets(twitterHandle);

		for (MyTweet tweet: tweets) {
			indexFile(tweet);
		}

		return writer.numDocs();
	}
}