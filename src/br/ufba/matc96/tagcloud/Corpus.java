package br.ufba.matc96.tagcloud;

import java.util.HashMap;
import java.util.List;

public interface Corpus
{
	public List<TagDocument> getTagDocuments();
	public HashMap<String, Tag> getTags();
}