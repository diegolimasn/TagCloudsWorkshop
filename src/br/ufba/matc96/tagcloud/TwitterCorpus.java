/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.matc96.tagcloud;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.queryparser.classic.ParseException;

import br.ufba.matc96.lucene.LuceneController;
import br.ufba.matc96.lucene.LuceneFileProvider;
import br.ufba.matc96.lucene.MyFile;
import br.ufba.matc96.tagcloud.util.TwitterProvider;
import twitter4j.TwitterException;

/**
 *
 * @author felipe
 */

public class TwitterCorpus implements Corpus, LuceneFileProvider<MyFile>
{
	private String twitterHandle;
	private LuceneController luceneController;
	private List<MyTagDocument> tagDocs;
	private Map<String, MyTag> tags;
	
	public TwitterCorpus(String twitterHandle)
	{
		this.twitterHandle = twitterHandle;
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
	public MyFile[] getFiles()
	{
        List<MyFile> tweets = new ArrayList<MyFile>();

        try
		{
        	TwitterProvider f = new TwitterProvider();
			f.start(twitterHandle, 200);

	        String regex = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	        Pattern pattern = Pattern.compile(regex);
	        String url = null;
	        int count = 0;
	        for (String text: f.getTexts())
	        {
	            Matcher matcher = pattern.matcher(text);
	            if(matcher.find())
	                url = matcher.group(0);
	            text = text.replaceAll(regex, "");
	            MyFile tweet = new MyFile(Integer.toString(count), text);
	            tweet.setUrl(url);
	            tweets.add(tweet);
	            count++;
	        }
		}
        catch (TwitterException e)
		{
			e.printStackTrace();
		}
        
        MyFile[] files = new MyFile[tweets.size()];
        int i = 0;
        for(MyFile file: tweets)
        {
        	files[i] = file;
        	i++;
        }
        return files;
	}
}