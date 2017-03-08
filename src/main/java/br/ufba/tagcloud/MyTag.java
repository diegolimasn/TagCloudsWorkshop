package br.ufba.tagcloud;

import java.util.ArrayList;
import java.util.List;

import br.ufba.tagcloud.util.SymmetricTagMatrix;

public class MyTag
{
	private String tagName;
	private Integer termFrequency;
    private List<String> docs;
	
	public MyTag()
	{
		this.tagName = "";
		this.termFrequency = 0;
        this.docs = new ArrayList<String>();
	}
    
    public MyTag(String tagName, Integer termFrequency) {
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
		return "Tag: "+this.tagName+" TF: "+this.termFrequency;
	}

    // Calculates cooccurrence of two tags 
    public static int calculateCooccurrence(MyTag i, MyTag j){
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
    
    public static SymmetricTagMatrix getCooccurrenceMatrix(List<MyTagDocument> docs)
    {
    	SymmetricTagMatrix matrix = new SymmetricTagMatrix();

    	for (MyTagDocument doc: docs)
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
