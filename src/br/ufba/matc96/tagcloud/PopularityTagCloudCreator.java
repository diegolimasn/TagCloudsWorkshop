package br.ufba.matc96.tagcloud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PopularityTagCloudCreator implements TagCloudCreator
{
	private Corpus corpus;
	private Integer nTags;

	public PopularityTagCloudCreator(Corpus corpus, Integer nTags)
	{
		this.corpus = corpus;
		this.nTags = nTags;
	}
	
	@Override
	public TagCloud create()
	{
		List<Tag> tags = new ArrayList<Tag>(corpus.getTags().values());

        Collections.sort(tags, new Comparator<Tag>(){
			@Override
			public int compare(Tag o1, Tag o2)
			{
				return Long.compare(o1.getTermFrequency(), o2.getTermFrequency());
			}
        });
        Collections.reverse(tags);
        
        if(nTags < tags.size())
        {
        	return new TagCloud(tags.subList(0, nTags));
        }
        else
        {
        	return new TagCloud(tags);
        }
	}
}
