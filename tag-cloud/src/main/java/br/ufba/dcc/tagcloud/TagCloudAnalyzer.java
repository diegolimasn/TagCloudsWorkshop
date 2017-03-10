package br.ufba.dcc.tagcloud;

import java.util.ArrayList;
import java.util.List;

public class TagCloudAnalyzer
{
	private TagCloud tagCloud;
	private Corpus corpus;

	public TagCloudAnalyzer(TagCloud tagCloud, Corpus corpus)
	{
		this.tagCloud = tagCloud;
		this.corpus = corpus;
	}
    
    private static int intersecSetString(List<String> setstr1, List<String> setstr2){
        int intersec = 0;
        for(String str1: setstr1)
        {
            for(String str2: setstr2)
            {
                if(str2.equals(str1))
                	intersec++;
            }
        }
        return intersec;
    }
    
    public double calcCov()
    {
    	List<String> docs = this.tagCloud.getDocs();
        double cov = (double) docs.size() / corpus.getTagDocuments().size();
        return cov;
    }
    
    public double calcOverlap()
    {
    	List<MyTag> tags = this.tagCloud.getAllTags();
        double overlap = 0.0;
        for(MyTag tag1: tags)
        {
            for(MyTag tag2: tags)
            {
                if(tag1 != tag2)
                {
                    int intersec = 0;
                    List<String> docs1 = tag1.getDocs();
                    List<String> docs2 = tag2.getDocs();
                    intersec = intersecSetString(docs1,docs2);
                    if(docs1.size() <= docs2.size())
                    {
                    	overlap += ((double) intersec / (double)docs1.size());
                    }
                    else
                    {
                    	overlap += ((double) intersec / (double)docs2.size());
                    }
                }
            }
        }
        List<String> tc = this.tagCloud.getDocs();
        overlap = overlap / (double) tc.size();
        return overlap;
    }
    
    public double calcRelevance()
    {
    	List<MyTag> tagcloud = this.tagCloud.getAllTags();
    	List<MyTag> tags = new ArrayList<MyTag>(this.corpus.getTags().values());
    	
        double relevance = 0.0;
        for(MyTag tag1: tagcloud)
        {
            for(MyTag tag2: tags)
            {
                //if(!tag1.equals(tag2)){
                    List<String> docs1 = tag1.getDocs();
                    List<String> docs2 = tag2.getDocs();
                    int union = intersecSetString(docs1, docs2);
                    relevance += ((double) union / (double) docs1.size());
                //}
            }
        }
        relevance = relevance / (double) this.corpus.getTagDocuments().size();
        return relevance;
    }
}