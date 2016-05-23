package br.ufba.matc96.lucene;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

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
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import br.ufba.matc96.tagcloud.Corpus;
import br.ufba.matc96.tagcloud.MyFile;

public class LuceneIndexer
{
	private IndexWriter writer;

	public LuceneIndexer(String indexDirectoryPath) throws IOException
	{
		//Creates reader for stop words file
		Reader stopWordsReader = new FileReader(new File(LuceneConstants.STOP_WORDS_FILE));
		Analyzer analyzer = new StandardAnalyzer(stopWordsReader);

		//Opens directory to store index
		Directory directory = FSDirectory.open(new File(indexDirectoryPath).toPath());
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		// Clears index folder before indexing
		config.setOpenMode(OpenMode.CREATE);
		writer = new IndexWriter(directory, config);
	}
	
	public void close() throws CorruptIndexException, IOException
	{
		writer.close();
	}
	
	public <F> int createIndex(Corpus<F> corpus) throws IOException
	{
        F[] files = corpus.getFiles();

        if(files instanceof MyFile[])
        {
        	return createIndex((MyFile[]) files);
        }
        else if(files instanceof File[])
        {
        	return createIndex((File[]) files);
        }

		return writer.numDocs();
	}
	
	private int createIndex(MyFile[] files) throws IOException
	{
		for (MyFile file: files)
		{
			indexFile(file);
		}
		return writer.numDocs();
	}
	
	private void indexFile(MyFile file) throws IOException
	{
		System.out.println("Indexing " + file.getName());
		Document document = getDocument(file);
		writer.addDocument(document);
	}
	
	private Document getDocument(MyFile file) throws IOException
	{
		Document document = new Document();

		//index file name
		Field fileNameField = new StoredField(LuceneConstants.FILE_NAME, file.getName());
		//index file path
		Field filePathField = new StoredField(LuceneConstants.FILE_PATH, file.getUrl());
		//index file contents
		FieldType fieldType = new FieldType();
		fieldType.setStored(false);
		fieldType.setStoreTermVectors(true);
		fieldType.setTokenized(true);
		fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		Field contentField = new Field(LuceneConstants.CONTENTS, file.getContent(), fieldType);

		document.add(fileNameField);
		document.add(filePathField);
		document.add(contentField);

		return document;
	}

	private int createIndex(File[] files) throws IOException
	{
		FileFilter filter = new LuceneFileFilter();
		
		for (File file : files)
		{
			if(!file.isDirectory()
					&& !file.isHidden()
					&& file.exists()
					&& file.canRead()
					&& filter.accept(file))
			{
				indexFile(file);
			}
			else if(file.isDirectory())
			{
				createIndex(file.listFiles());
			}
		}
		return writer.numDocs();
	}
	
	private void indexFile(File file) throws IOException
	{
		System.out.println("Indexing "+file.getCanonicalPath());
		Document document = getDocument(file);
		writer.addDocument(document);
	}

	private Document getDocument(File file) throws IOException
	{
		Document document = new Document();
		
		//index file name
		Field fileNameField = new StoredField(LuceneConstants.FILE_NAME, file.getName());
		//index file path
		Field filePathField = new StoredField(LuceneConstants.FILE_PATH, file.getCanonicalPath());
		//index file contents
		FieldType fieldType = new FieldType();
		fieldType.setStored(false);
		fieldType.setStoreTermVectors(true);
		fieldType.setTokenized(true);
		fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		Field contentField = new Field(LuceneConstants.CONTENTS, new FileReader(file), fieldType);

		document.add(fileNameField);
		document.add(filePathField);
		document.add(contentField);

		return document;
	}
}