package br.ufba.tagcloud;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryparser.classic.ParseException;

import br.ufba.lucene.LuceneController;
import br.ufba.lucene.LuceneFileProvider;

public class TextFileCorpus implements Corpus, LuceneFileProvider<File>
{
	private String fileDirectory;
	private LuceneController luceneController;
	private List<MyTagDocument> tagDocs;
	private Map<String, MyTag> tags;
	
	public TextFileCorpus(String fileDirectory)
	{
		this.fileDirectory = fileDirectory;
		this.luceneController = new LuceneController();
		try
		{
			luceneController.createIndex(this);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		tagDocs = null;
		tags = null;
	}

	@Override
	public List<MyTagDocument> getTagDocuments()
	{
		if(this.tagDocs == null)
		{
			try
			{
				this.tagDocs = this.luceneController.getTagDocuments();
			}
			catch (IOException | ParseException e)
			{
				e.printStackTrace();
			}
		}
		return tagDocs;
	}

	@Override
	public Map<String, MyTag> getTags()
	{
		if(this.tags == null)
		{
			try
			{
				this.tags = this.luceneController.getTags();
			}
			catch (IOException | ParseException e)
			{
				e.printStackTrace();
			}
		}
		return this.tags;
	}

	@Override
	public File[] getFiles()
	{
		return new File(fileDirectory).listFiles();
	}
}