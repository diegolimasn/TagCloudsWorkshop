package br.ufba.matc96.lucene;

public interface LuceneFileProvider<F>
{
	public abstract F[] getFiles();
}
