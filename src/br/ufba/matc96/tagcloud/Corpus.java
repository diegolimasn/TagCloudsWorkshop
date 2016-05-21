package br.ufba.matc96.tagcloud;

import java.util.List;

public abstract class Corpus
{
	public abstract List<Tag> getTags();
	public abstract List<TagDocument> getTagDocuments();
}