/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.matc96.tagcloud;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.TwitterException;

/**
 *
 * @author felipe
 */

public class TweetCorpus implements Corpus<MyFile>
{
	private String twitterHandle;
	
	public TweetCorpus(String twitterHandle)
	{
		this.twitterHandle = twitterHandle;
	}
	
	@Override
	public MyFile[] getFiles()
	{
        List<MyFile> tweets = new ArrayList<MyFile>();

        try
		{
        	TweetFinder f = new TweetFinder();
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