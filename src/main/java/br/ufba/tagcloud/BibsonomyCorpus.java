package br.ufba.tagcloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.Tag;

import br.ufba.tagcloud.util.BibsonomyProvider;

public class BibsonomyCorpus implements Corpus
{
	private List<MyTagDocument> tagDocs;
	private Map<String, MyTag> tags;
	private String searchTerm;

	public BibsonomyCorpus()
	{
		this.tagDocs = new ArrayList<MyTagDocument>();
		this.tags = new HashMap<String, MyTag>();
		this.searchTerm = "";
		BibsonomyProvider provider = new BibsonomyProvider();
		loadPosts(provider.getPosts(this.searchTerm));
		//printDocs();
	}
	
	public BibsonomyCorpus(String searchTerm)
	{
		this.tagDocs = new ArrayList<MyTagDocument>();
		this.tags = new HashMap<String, MyTag>();
		this.searchTerm = searchTerm.toLowerCase();
		BibsonomyProvider provider = new BibsonomyProvider();
		loadPosts(provider.getPosts(this.searchTerm));
		//printDocs();
	}

	@Override
	public List<MyTagDocument> getTagDocuments()
	{
		return this.tagDocs;
	}

	@Override
	public Map<String, MyTag> getTags()
	{
		return this.tags;
	}

	private void loadPosts(List<Post<?>> posts)
	{
		for (Post<?> post : posts)
		{
			MyTagDocument doc = new MyTagDocument();
			for(Tag tag: post.getTags())
			{
				String tagName = tag.getName().toLowerCase();
				if(!tagName.equals(this.searchTerm))
				{
					if(!this.tags.containsKey(tagName))
					{
						this.tags.put(tagName, new MyTag(tagName, 0));
					}

					MyTag t = this.tags.get(tagName);
					t.setTermFrequency(t.getTermFrequency()+1);
					t.addDoc(String.valueOf(post.hashCode()));
					doc.addTag(tagName);
				}
			}
			if(post.getTags().size() != 1)
				this.tagDocs.add(doc);
		}
	}
	
	private void printDocs()
	{
		int i = 0;
		for (MyTagDocument doc : this.tagDocs)
		{
			System.out.println("DOC "+i);
			for(String tag: doc.getTags())
			{
				//for(String d: this.tags.get(tag).getDocs())
				//	System.out.println("   DOC: " + d );
				System.out.println("   NAME: " + tag + " FREQ: " + this.tags.get(tag).getTermFrequency());
			}
			i++;
		}
	}
}
