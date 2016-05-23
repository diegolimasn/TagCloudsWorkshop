package br.ufba.matc96.lucene;

import java.io.File;
import java.io.FileFilter;

public class LuceneFileFilter implements FileFilter
{
	@Override
	public boolean accept(File pathname)
	{
		return pathname.getName().toLowerCase().endsWith(LuceneConstants.FILE_EXTENSION);
	}
}
