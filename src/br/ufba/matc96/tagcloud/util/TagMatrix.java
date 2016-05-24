package br.ufba.matc96.tagcloud.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import edu.ucla.sspace.matrix.ArrayMatrix;
import edu.ucla.sspace.matrix.Matrix;

public class TagMatrix
{
	protected HashMap<Pair<String, String>, Float> matrix;
	protected List<String> tags;
	
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
	
	public void printMatrix()
	{
		for (Entry<Pair<String, String>, Float> entry : this.matrix.entrySet())
		{
				System.out.println(entry.getKey() + "/" + entry.getValue());
		}
	}

	/*public void printMatrixToFile()
	{
		
		PrintWriter writer;
		try
		{
			writer = new PrintWriter("teste/teste.arff", "UTF-8");
			writer.println("@relation cooccurrence\n");
			writer.print("@attribute tag1 {");
			writer.print(String.join(",", tags));
			writer.println("}");
			writer.print("@attribute tag2 {");
			writer.print(String.join(",", tags));
			writer.println("}");
			writer.println("@attribute cooc real\n");
			writer.println("@data");
			for (Entry<Pair<String, String>, Float> entry : this.matrix.entrySet())
			{
				writer.println(entry.getKey().getKey() + "," + entry.getKey().getValue() + "," + entry.getValue());
			}
			writer.close();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}