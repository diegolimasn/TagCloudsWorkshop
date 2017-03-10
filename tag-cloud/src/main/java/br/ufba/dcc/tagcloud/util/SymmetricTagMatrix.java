package br.ufba.dcc.tagcloud.util;

public class SymmetricTagMatrix extends TagMatrix
{
	
	public SymmetricTagMatrix()
	{
		super();
	}
	
	public Float getValue(String tag1, String tag2)
	{
		Float result = null;
		if(tag1.compareToIgnoreCase(tag2) < 0)
		{
			result = matrix.get(new Pair<String, String>(tag1,tag2));
		}
		else
		{
			result = matrix.get(new Pair<String, String>(tag2,tag1));
		}
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

		if(tag1.compareToIgnoreCase(tag2) < 0)
		{
			matrix.put(new Pair<String, String>(tag1,tag2), value);
		}
		else
		{
			matrix.put(new Pair<String, String>(tag2,tag1), value);
		}
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
		if(tag1.compareToIgnoreCase(tag2) < 0)
		{
			key = new Pair<String, String>(tag1,tag2);
		}
		else
		{
			key = new Pair<String, String>(tag2,tag1);	
		}
		
		if(matrix.containsKey(key))
		{
			matrix.put(key, matrix.get(key)+1);
		}
		else
		{
			matrix.put(key, 1f);
		}
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
				else if(matrix.containsKey(new Pair<String, String>(t2,t1)))
				{
					m[i][j] = matrix.get(new Pair<String, String>(t2,t1));
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