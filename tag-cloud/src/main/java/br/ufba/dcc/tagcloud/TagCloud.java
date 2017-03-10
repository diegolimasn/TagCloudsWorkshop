package br.ufba.dcc.tagcloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TagCloud
{
	private Map<String, List<MyTag>> clusters;
	
	public TagCloud()
	{
		this.clusters = new HashMap<String, List<MyTag>>();
	}

	public TagCloud(List<MyTag> tags)
	{
		this.clusters = new HashMap<String, List<MyTag>>();
		this.clusters.put("1", tags);
	}
	
	public TagCloud(Map<String, List<MyTag>> clusters)
	{
		this.clusters = clusters;
	}
	
	public Map<String, List<MyTag>> getClusters()
	{
		return this.clusters;
	}
	
	public void setClusters(Map<String, List<MyTag>> clusters)
	{
		this.clusters = clusters;
	}
	
	public List<MyTag> getAllTags()
	{
		List<MyTag> tags = new LinkedList<MyTag>();
		
		for(List<MyTag> values: clusters.values())
		{
			for(MyTag tag: values)
			{
				tags.add(tag);
			}
		}
		return tags;
	}
	
	public void addTagToCluster(String key, MyTag tag)
	{
		if(!this.clusters.containsKey(key))
			this.clusters.put(key, new ArrayList<MyTag>());
		this.clusters.get(key).add(tag);
	}
	
	public List<String> getDocs()
	{
        List<String> docstag = new ArrayList<>();
        for(MyTag tag: this.getAllTags())
        {
            for(String doc: tag.getDocs())
            {
                if(!docstag.contains(doc))
                {
                    docstag.add(doc);
                }
            }
        }
        return docstag;
    }
}
