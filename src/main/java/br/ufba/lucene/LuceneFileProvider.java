package br.ufba.lucene;

public interface LuceneFileProvider<F>
{
	public abstract F[] getFiles();
}
