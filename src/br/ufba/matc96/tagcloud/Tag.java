package br.ufba.matc96.tagcloud;

import java.util.ArrayList;
import java.util.List;

public class Tag
{
	private String tagName;
	private Integer termFrequency;
	private Integer documentFrequency;
    private List<String> docs;
	
	public Tag()
	{
		this.tagName = "";
		this.termFrequency = 0;
		this.documentFrequency = 0;
	}
    
    public Tag(String tagName, Integer termFrequency) {
        this.tagName = tagName;
        this.termFrequency = termFrequency;
        this.documentFrequency = 0;
        this.docs = new ArrayList<String>();
    }
	
	public Tag(String tagName, Integer termFrequency, Integer documentFrequency)
	{
		this.tagName = tagName;
		this.termFrequency = termFrequency;
		this.documentFrequency = documentFrequency;
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
		return documentFrequency;
	}
	
	public void setDocumentFrequency(Integer documentFrequency)
	{
		this.documentFrequency = documentFrequency;
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
		return "Tag: "+this.tagName+" TF: "+this.termFrequency+" DF: "+this.documentFrequency;
	}
}
