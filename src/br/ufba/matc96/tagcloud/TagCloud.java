package br.ufba.matc96.tagcloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TagCloud
{
	private Map<String, List<Tag>> clusters;
	
	public TagCloud()
	{
		this.clusters = new HashMap<String, List<Tag>>();
	}

	public TagCloud(List<Tag> tags)
	{
		this.clusters = new HashMap<String, List<Tag>>();
		this.clusters.put("1", tags);
	}
	
	public TagCloud(Map<String, List<Tag>> clusters)
	{
		this.clusters = clusters;
	}
	
	public Map<String, List<Tag>> getClusters()
	{
		return this.clusters;
	}
	
	public void setClusters(Map<String, List<Tag>> clusters)
	{
		this.clusters = clusters;
	}
	
	public List<Tag> getAllTags()
	{
		List<Tag> tags = new LinkedList<Tag>();
		
		for(List<Tag> values: clusters.values())
		{
			for(Tag tag: values)
			{
				tags.add(tag);
			}
		}
		return tags;
	}
	
	public void addTagToCluster(String key, Tag tag)
	{
		if(!this.clusters.containsKey(key))
			this.clusters.put(key, new ArrayList<Tag>());
		this.clusters.get(key).add(tag);
	}
}
