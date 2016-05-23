package br.ufba.matc96.tagcloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;

import br.ufba.matc96.lucene.LuceneConstants;
import br.ufba.matc96.tagcloud.util.SymmetricTagMatrix;

public class Tag
{
	private String tagName;
	private Integer termFrequency;
    private List<String> docs;
	
	public Tag()
	{
		this.tagName = "";
		this.termFrequency = 0;
	}
    
    public Tag(String tagName, Integer termFrequency) {
        this.tagName = tagName;
        this.termFrequency = termFrequency;
        this.docs = new ArrayList<String>();
    }
	
	public String getTagName()
	{
		return tagName;
	}
	
	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}
	
	public Integer getTermFrequency()
	{
		return termFrequency;
	}
	
	public void setTermFrequency(Integer termFrequency)
	{
		this.termFrequency = termFrequency;
	}
	
	public Integer getDocumentFrequency()
	{
		return this.docs.size();
	}
    
    public List<String> getDocs() {
        return docs;
    }

    public void setDocs(List<String> docs) {
        this.docs = docs;
    }
    
    public void addDoc(String doc){
        this.getDocs().add(doc);
    }
	
	@Override
	public String toString()
	{
		return "Tag: "+this.tagName+" TF: "+this.termFrequency+" DF: "+this.docs.size();
	}

    // Calculates cooccurrence of two tags 
    public static int calculateCooccurrence(Tag i, Tag j){
        int total = 0;
        List<String> docsI, docsJ;
        docsI = i.getDocs();
        docsJ = j.getDocs();

        for(String strI: docsI)
        {
            for(String strJ: docsJ)
            {
                if(strI.equals(strJ))
                	total++;
            }
        }
        return total;
    }
    
    /*public static SymmetricTagMatrix getCooccurrenceMatrix(HashMap<String,Tag> tags)
    {
    	tags.values()

    	return matrix;
    }*/
    
    public static SymmetricTagMatrix getCooccurrenceMatrix(List<TagDocument> docs)
    {
    	SymmetricTagMatrix matrix = new SymmetricTagMatrix();

    	for (TagDocument doc: docs)
		{		    
			for(int j = 0; j < doc.getTags().size(); j++)
			{
				String t1 = doc.getTags().get(j);
				for(int k = j+1; k < doc.getTags().size(); k++)
				{
					String t2 = doc.getTags().get(k);
					matrix.increaseValue(t1, t2);
				}
			}
		}

    	return matrix;
    }
}
