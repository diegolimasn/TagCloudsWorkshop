/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.matc96.tagcloud;
import java.util.ArrayList;
import twitter4j.*;
import java.util.List;
import twitter4j.auth.AccessToken;
/**
 *
 * @author felipe
 */
public class FindTweets {
    private ArrayList<String> texts;
    private final static String CONSUMER_KEY = "uNsb6FQluNPkKSj6ENJS2q7XS";
    private final static String CONSUMER_KEY_SECRET = "ZKf2sPcUsD7TAI8tz69iKgVGGP5vsWYtR1wi63zvstaIfYjjps";
    private final static String ACCESS_TOKEN = "15971498-SKuEW1ITeYVtRP6kBU6i4ZPiiBbBCYOmy4s6UUYox";
    private final static String ACCESS_TOKEN_SECRET = "6ImUSjY8MofvzmiKPCQ5i3D9IlSBVs8EZx9J8Di7GWq7z";
    
    public FindTweets(){
        texts = new ArrayList<>();
    }
    
    public void start(String user, int max) throws TwitterException{
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
        AccessToken oathAccessToken = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
        twitter.setOAuthAccessToken(oathAccessToken);
        this.getTweets(twitter,user,max);

    }
    
    public void getTweets(Twitter twitter, String user, int max) throws TwitterException{
        List<Status> tweets = new ArrayList<Status>();
        for(int i=1; i<20;i++)
        {
        	tweets.addAll(twitter.getUserTimeline(user, new Paging(i,max)));
        }
        for(Status tweet: tweets){
            this.texts.add(tweet.getText());
        }
    }

    public ArrayList<String> getTexts() {
        return texts;
    }
    
}
