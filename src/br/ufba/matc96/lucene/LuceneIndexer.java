package br.ufba.matc96.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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

import br.ufba.matc96.tagcloud.TwitterCorpus;
import br.ufba.matc96.tagcloud.MyDocument;
import twitter4j.TwitterException;

public class LuceneIndexer
{
	private IndexWriter writer;

	public LuceneIndexer(String indexDirectoryPath) throws IOException
	{
		Reader reader = new FileReader(new File(LuceneConstants.STOP_WORDS_FILE));
		Analyzer analyzer = new StandardAnalyzer(reader);
		
		Directory directory = FSDirectory.open(new File(indexDirectoryPath).toPath());
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(directory, config);
	}
	
	public void close() throws CorruptIndexException, IOException
	{
		writer.close();
	}
	
	private Document getDocument(MyDocument doc) throws IOException
	{
		Document document = new Document();
		
		//index file contents
		FieldType fieldType = new FieldType();
		fieldType.setStored(false);
		fieldType.setStoreTermVectors(true);
		fieldType.setTokenized(true);
		fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);

		//index file name
		Field fileNameField = new StoredField(LuceneConstants.FILE_NAME, doc.getName());
		//index file path
		Field filePathField = new StoredField(LuceneConstants.FILE_PATH, doc.getUrl());
		//index file contents
		Field contentField = new Field(LuceneConstants.CONTENTS, doc.getContent(), fieldType);

		document.add(contentField);
		document.add(fileNameField);
		document.add(filePathField);

		return document;
	}
	
	private void indexFile(MyDocument doc) throws IOException
	{
		System.out.println("Indexing " + doc.getName());
		Document document = getDocument(doc);
		writer.addDocument(document);
	}
	
	public int createIndex(String twitterHandle) 
			throws IOException, TwitterException
	{
		TwitterCorpus corpus = new TwitterCorpus();
        List<MyDocument> tweets = corpus.getTweets(twitterHandle);

		for (MyDocument tweet: tweets) {
			indexFile(tweet);
		}

		return writer.numDocs();
	}
	
	public int createIndex(List<MyDocument> docs) 
			throws IOException, TwitterException
	{
		for (MyDocument doc: docs) {
			indexFile(doc);
		}

		return writer.numDocs();
	}
}