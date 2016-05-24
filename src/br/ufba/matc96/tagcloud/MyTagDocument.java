package br.ufba.matc96.tagcloud;

import java.util.ArrayList;
import java.util.List;

public class MyTagDocument
{
	private List<String> tags;
	
	public MyTagDocument()
	{
		tags = new ArrayList<String>();
	}
	
	public List<String> getTags()
	{
		return this.tags;
	}
	
	public void setTags(List<String> tags)
	{
		this.tags = tags;
	}
	
	public void addTag(String tag)
	{
		this.tags.add(tag);
	}
}
