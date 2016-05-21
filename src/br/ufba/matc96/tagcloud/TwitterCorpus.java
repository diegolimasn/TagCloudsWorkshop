/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.matc96.tagcloud;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.ufba.matc96.lucene.LuceneFileFilter;
import twitter4j.TwitterException;

/**
 *
 * @author felipe
 */

public class TwitterCorpus {
    private final File[] documents;
    
    public TwitterCorpus(){
        File directory = new File("corpus");
        this.documents = directory.listFiles(new LuceneFileFilter());
    }

    public File[] getDocuments() {
        return documents;
    }
    
    // It also gets the url of each tweet
    public List<MyDocument> getTweets(String twitterHandle) throws TwitterException{
        List<MyDocument> tweets = new ArrayList<>();
        FindTweets f = new FindTweets();
        f.start("bbcbrasil",200);
        int count = 0;
        String regex = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        String url = null;
        Pattern pattern = Pattern.compile(regex);
        for (String text : f.getTexts()) {
            Matcher matcher = pattern.matcher(text);
            if(matcher.find())
                url = matcher.group(0);
            text = text.replaceAll(regex, "");
            MyDocument tweet = new MyDocument(Integer.toString(count),text);
            tweet.setUrl(url);
            tweets.add(tweet);
            count++;
        }
        return tweets;
    }
    
   
}
