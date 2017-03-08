package br.ufba.tagcloud.util;

import java.util.ArrayList;
import java.util.List;

import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.rest.client.queries.get.GetPostsQuery;
import org.bibsonomy.rest.exceptions.ErrorPerformingRequestException;

public class BibsonomyProvider
{
	private final static String USERNAME = "egosantana";
	private final static String API_KEY = "d68788f924188a1df87267ab7e235d8f";
	private List<Post<?>> posts;
	
	public BibsonomyProvider()
	{
		this.posts = new ArrayList<Post<?>>();
	}
	
	public List<Post<?>> getPosts(String searchTerm)
	{
		Bibsonomy bib = new Bibsonomy(USERNAME, API_KEY);
		bib.setApiURL("http://www.bibsonomy.org/api");
		List<String> searchSet = new ArrayList<String>();
		searchSet.add(searchTerm);

		int initial_index = 0;
		int offset = 500;
		int max = offset;
		
		for(int i=0; i<10; i++, max+=offset, initial_index+=offset)
		{
			// instantiate query object
			GetPostsQuery gpq = new GetPostsQuery(initial_index, max);
			gpq.setTags(searchSet);
			//gpq.setResourceType(BibTex.class);
			gpq.setResourceType(Bookmark.class);
			try
			{
				// perform query
				bib.executeQuery(gpq);
	
				// on success, read results
				if (gpq.getHttpStatusCode() == 200)
				{
					this.posts.addAll(gpq.getResult());
				}
				else
				{
					System.out.println("Something went wrong. HTTP status code: "+gpq.getHttpStatusCode());
				}
			} catch (ErrorPerformingRequestException e) {
				 // happens on network failure for example
				 
			}
		}
		
		return this.posts;
	}
}
