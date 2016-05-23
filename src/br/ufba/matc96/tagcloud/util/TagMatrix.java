package br.ufba.matc96.tagcloud.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import edu.ucla.sspace.matrix.ArrayMatrix;
import edu.ucla.sspace.matrix.Matrix;

public class TagMatrix
{
	private HashMap<Pair<String, String>, Float> matrix;
	private List<String> tags;
	
	public TagMatrix()
	{
		matrix = new HashMap<Pair<String, String>, Float>();
		tags = new ArrayList<String>();
	}
	
	public HashMap<Pair<String, String>, Float> getMap()
	{
		return this.matrix;
	}
	
	public Float getValue(String tag1, String tag2)
	{
		Float result = null;
		result = matrix.get(new Pair<String, String>(tag1,tag2));
		return result;
	}

	public void setValue(String tag1, String tag2, Float value)
	{
		if(!tags.contains(tag1))
		{
			tags.add(tag1);
		}
		if(!tags.contains(tag2))
		{
			tags.add(tag2);
		}
		matrix.put(new Pair<String, String>(tag1,tag2), value);
	}

	public void increaseValue(String tag1, String tag2)
	{
		if(!tags.contains(tag1))
		{
			tags.add(tag1);
		}
		if(!tags.contains(tag2))
		{
			tags.add(tag2);
		}

		Pair<String, String> key;
		key = new Pair<String, String>(tag1,tag2);
		if(matrix.containsKey(key))
		{
			matrix.put(key, matrix.get(key)+1);
		}
		else
		{
			matrix.put(key, 1f);
		}
	}
	
	public List<String> getTags()
	{
		return tags;
	}
	
	public void printMatrix()
	{
		for (Entry<Pair<String, String>, Float> entry : this.matrix.entrySet())
		{
				System.out.println(entry.getKey() + "/" + entry.getValue());
		}
	}
	
	public Matrix toMatrix()
	{
		Matrix m = new ArrayMatrix(this.to2DArray());
		return m;
	}
	
	public double[][] to2DArray()
	{
		double[][] m = new double[tags.size()][tags.size()];

		for(int i = 0; i<tags.size();i++)
		{
			for(int j = 0; j<tags.size(); j++)
			{
				String t1 = tags.get(i);
				String t2 = tags.get(j);
				if(matrix.containsKey(new Pair<String, String>(t1,t2)))
				{
					m[i][j] = matrix.get(new Pair<String, String>(t1,t2));
				}
				else
				{
					m[i][j] = 0f;
				}
			}
		}
		
		return m;
	}
}