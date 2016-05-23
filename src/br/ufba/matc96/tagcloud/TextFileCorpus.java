package br.ufba.matc96.tagcloud;

import java.io.File;

public class TextFileCorpus implements Corpus<File>
{
	private String fileDirectory;
	
	public TextFileCorpus(String fileDirectory)
	{
		this.fileDirectory = fileDirectory;
	}

	@Override
	public File[] getFiles()
	{
		return new File(fileDirectory).listFiles();
	}
}