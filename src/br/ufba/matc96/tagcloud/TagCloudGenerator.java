package br.ufba.matc96.tagcloud;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.util.Properties;

import edu.ucla.sspace.clustering.Assignment;
import edu.ucla.sspace.clustering.Assignments;
import edu.ucla.sspace.clustering.CKVWSpectralClustering03;
import edu.ucla.sspace.clustering.CKVWSpectralClustering06;
import edu.ucla.sspace.matrix.Matrix;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.queryparser.classic.ParseException;

import javafx.util.Pair;
import twitter4j.TwitterException;

public class TagCloudGenerator
{
	public static void main(String[] args)
	{
		try
		{
			FileUtils.cleanDirectory(new File("index/"));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		TwitterHelper luceneHelper = new TwitterHelper();
		HashMap<String, Tag> tags = null;
		SymmetricTagMatrix coMatrix = null;

		try
		{
			luceneHelper.createIndex();
			tags = luceneHelper.getTags();
			coMatrix = luceneHelper.getCooccurrenceMatrix();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		} catch (TwitterException e)
		{
			e.printStackTrace();
		}

		SymmetricTagMatrix affinityMatrix = getAffinityMatrix(coMatrix,tags);
		//TagMatrix degreeMatrix = getDegreeMatrix(affinityMatrix);
		//TagMatrix invDegreeMatrix = getInverseMatrix(degreeMatrix);
		//MyMatrix laplacianMatrix = multiplyMatrices(multiplyMatrices(invDegreeMatrix,affinityMatrix),invDegreeMatrix);
		Matrix m = affinityMatrix.toMatrix();
		CKVWSpectralClustering06 clusterer = new CKVWSpectralClustering06();
		Assignments a = clusterer.cluster(m, 10, new Properties());
		Assignment[] assignments = a.assignments();
		//List<ArrayList<Integer>> lc = new ArrayList<ArrayList<Integer>>(10);
		HashMap<String,ArrayList<Tag>> clusters = new HashMap<String,ArrayList<Tag>>();
		for (int i = 0; i < assignments.length; ++i)
        {
			if(!clusters.containsKey(Arrays.toString(assignments[i].assignments())))
			{
				clusters.put(Arrays.toString(assignments[i].assignments()), new ArrayList<Tag>());
			}
			clusters.get(Arrays.toString(assignments[i].assignments())).add(tags.get(affinityMatrix.getTags().get(i)));
            //System.out.printf("Node %d (%s) is in clusters %s%n",
            //                  i, affinityMatrix.getTags().get(i), Arrays.toString(assignments[i].assignments()));
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initUI(clusters);
            }
        });
		//TagMatrix degreeMatrix = getDegreeMatrix(affinityMatrix);
		//TagMatrix invDegreeMatrix = getInverseMatrix(degreeMatrix);
		//Matrix laplacianMatrix = multiplyMatrices(multiplyMatrices(invDegreeMatrix,affinityMatrix),invDegreeMatrix);
		//laplacianMatrix.printMatrix();
	}
	
	private static SymmetricTagMatrix getAffinityMatrix(SymmetricTagMatrix coMatrix, HashMap<String, Tag> tags)
	{
		SymmetricTagMatrix affinityMatrix = new SymmetricTagMatrix();
		for (Entry<Pair<String, String>, Float> entry : coMatrix.getMap().entrySet())
		{
			if(entry.getValue() > 2)
			{
				String tag1 = entry.getKey().getKey();
				String tag2 = entry.getKey().getValue();
				Float similarity = (float) ((2*(entry.getValue()))
						/Math.sqrt(tags.get(tag1).getTermFrequency()*tags.get(tag2).getTermFrequency()));
				//Float similarity = (float) Math.cos(entry.getValue());
				affinityMatrix.setValue(tag1, tag2, similarity);
			}
		}
		return affinityMatrix;
	}
	
	private static SymmetricTagMatrix getDegreeMatrix(SymmetricTagMatrix affinityMatrix)
	{
		SymmetricTagMatrix degreeMatrix = new SymmetricTagMatrix();
		for (Entry<Pair<String, String>, Float> entry : affinityMatrix.getMap().entrySet())
		{
				String tag1 = entry.getKey().getKey();
				String tag2 = entry.getKey().getValue();
				
				Float v1 = degreeMatrix.getValue(tag1, tag1);
				if(v1 != null)
				{
					degreeMatrix.setValue(tag1, tag1, entry.getValue() + v1);
				}
				else
				{
					degreeMatrix.setValue(tag1, tag1, entry.getValue());
				}

				Float v2 = degreeMatrix.getValue(tag2, tag2);
				if(v2 != null)
				{
					degreeMatrix.setValue(tag2, tag2, entry.getValue() + v2);
				}
				else
				{
					degreeMatrix.setValue(tag2, tag2, entry.getValue());
				}
		}
		return degreeMatrix;
	}
	
	private static SymmetricTagMatrix getInverseMatrix(SymmetricTagMatrix diagonalMatrix)
	{
		SymmetricTagMatrix inverseMatrix = new SymmetricTagMatrix();
		for (Entry<Pair<String, String>, Float> entry : diagonalMatrix.getMap().entrySet())
		{
			String tag = entry.getKey().getKey();
			inverseMatrix.setValue(tag, tag, 1/entry.getValue());
		}
		return inverseMatrix;
	}
	
	private static TagMatrix multiplyMatrices(TagMatrix A, TagMatrix B)
	{
		TagMatrix C = new TagMatrix();
		List<String> indexes = A.getTags();
		
		for (String tag1 : indexes)
		{
			for (String tag2 : indexes)
			{
				for (String tag3 : indexes)
				{
					Float value = C.getValue(tag1, tag2);
					if (value == null)
					{
						value = 0f;
					}
					Float v1 = A.getValue(tag1, tag3);
					Float v2 = B.getValue(tag3, tag2);
					if((v1 != null) && (v2 != null))
					{
						C.setValue(tag1, tag2, value + (v1*v2));
					}
				}
			}
		}
		return C;
	}
	
	protected static void initUI(HashMap<String, ArrayList<Tag>> clusters) {
        JFrame frame = new JFrame("Tag Cloud Workshop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        
        for (Entry<String, ArrayList<Tag>> c : clusters.entrySet()) {
        	Collections.sort(c.getValue(), (Tag t1, Tag t2) -> Long.compare(t1.getTermFrequency(), t2.getTermFrequency()));
            Collections.reverse(c.getValue());
        	int i = 0;
        	for(Tag tag: c.getValue())
        	{
        		if(i>10)
        			break;
	            final JLabel label = new JLabel(tag.getTagName());
	            label.setOpaque(false);
	            label.setFont(label.getFont().deriveFont((float)Math.log(tag.getTermFrequency())*5));
	            panel.add(label);
	            i++;
        	}
            final JLabel label = new JLabel(":");
            panel.add(label);
        }
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
