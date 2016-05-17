package br.ufba.matc96.tagcloud;

import java.util.List;

public abstract class Corpus
{
	public abstract void getCorpus(List<Tag> tags, List<TagDocument> docs);
}
