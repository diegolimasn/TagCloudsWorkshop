package br.ufba.dcc.lucene;

public interface LuceneFileProvider<F>
{
	public abstract F[] getFiles();
}
