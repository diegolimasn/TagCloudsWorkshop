package br.ufba.matc96.tagcloud;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

import br.ufba.matc96.lucene.LuceneController;
import br.ufba.matc96.tagcloud.util.Pair;
import br.ufba.matc96.tagcloud.util.SymmetricTagMatrix;
import twitter4j.TwitterException;

public class TagCloudGenerator
{
	public static void main(String[] args)
	{
		/*try
		{
			FileUtils.cleanDirectory(new File("index/"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}*/
		LuceneController luceneController = new LuceneController();
		HashMap<String, Tag> tags = null;
		List<TagDocument> docs = null;
		SymmetricTagMatrix coMatrix = null;

		try
		{
			Corpus<?> corpus = new TweetCorpus("bbcbrasil");
			//Corpus<?> corpus = new TextFileCorpus("test_data");
			luceneController.createIndex(corpus);
			tags = luceneController.getTags();
			docs = luceneController.getTagDocuments();
			//coMatrix = luceneController.getCooccurrenceMatrix();
			coMatrix = Tag.getCooccurrenceMatrix(docs);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		catch (TwitterException e)
		{
			e.printStackTrace();
		}

		//if(args[0].equals("0")){
		boolean sclusteringOn = true;
		if(sclusteringOn){
			SymmetricTagMatrix affinityMatrix = getAffinityMatrix(coMatrix,tags);
			Matrix m = affinityMatrix.toMatrix();

			CKVWSpectralClustering06 clusterer = new CKVWSpectralClustering06();
			Assignments a = clusterer.cluster(m, 10, new Properties());
			Assignment[] assignments = a.assignments();
			HashMap<String,ArrayList<Tag>> clusters = new HashMap<String,ArrayList<Tag>>();
			for (int i = 0; i < assignments.length; ++i)
	        {
				if(!clusters.containsKey(Arrays.toString(assignments[i].assignments())))
				{
					clusters.put(Arrays.toString(assignments[i].assignments()), new ArrayList<Tag>());
				}
				clusters.get(Arrays.toString(assignments[i].assignments())).add(tags.get(affinityMatrix.getTags().get(i)));
	        }

			final HashMap<String,ArrayList<Tag>> clusters2 = clusters;

			final ArrayList<Tag> onlyTags = new ArrayList<>();
            for(String s : tags.keySet())
            	onlyTags.add(tags.get(s));

	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	            	List<Tag> myTagCloud = initUI(clusters2);
                    double cov = calcCov(myTagCloud, onlyTags);
                    System.out.println("Coverage = " + cov);
                    double over = calcOverlap(myTagCloud, onlyTags.size());
                    System.out.println("Overlap = " + over);
                    double rel = calcRelevance(myTagCloud, onlyTags);
                    System.out.println("Relevance = " + rel);
	            }
	        });
		}
		else
		{
            List<Tag> onlyTags = new ArrayList<>();
            for(String s : tags.keySet()) onlyTags.add(tags.get(s));
            
            Collections.sort(onlyTags, new Comparator<Tag>(){
				@Override
				public int compare(Tag o1, Tag o2)
				{
					return Long.compare(o1.getTermFrequency(), o2.getTermFrequency());
				}
            });
            Collections.reverse(onlyTags);
            
            final List<Tag> onlyTags2 = onlyTags;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    List<Tag> myTagCloud = initUI(onlyTags2);
                    double cov = calcCov(myTagCloud, onlyTags2);
                    System.out.println("Coverage = " + cov);
                    double over = calcOverlap(myTagCloud, onlyTags2.size());
                    System.out.println("Overlap = " + over);
                    double rel = calcRelevance(myTagCloud, onlyTags2);
                    System.out.println("Relevance = " + rel);
                }
            });
		}
	}
	
	private static SymmetricTagMatrix getAffinityMatrix(SymmetricTagMatrix coMatrix, HashMap<String, Tag> tags)
	{
		SymmetricTagMatrix affinityMatrix = new SymmetricTagMatrix();
		for (Entry<Pair<String, String>, Float> entry : coMatrix.getMap().entrySet())
		{
			if(entry.getValue() > 2)
			{
				String tag1 = entry.getKey().getFirst();
				String tag2 = entry.getKey().getSecond();
				Float similarity = (float) ((2*(entry.getValue()))
						/Math.sqrt(tags.get(tag1).getTermFrequency()*tags.get(tag2).getTermFrequency()));
				affinityMatrix.setValue(tag1, tag2, similarity);
			}
		}
		return affinityMatrix;
	}
	
	protected static List<Tag> initUI(HashMap<String, ArrayList<Tag>> clusters) {
        JFrame frame = new JFrame("Tag Cloud Workshop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        List<Tag> myTagCloud = new ArrayList<>();
        
        for (Entry<String, ArrayList<Tag>> c : clusters.entrySet()) {
        	Collections.sort(c.getValue(), new Comparator<Tag>(){
				@Override
				public int compare(Tag o1, Tag o2)
				{
					return Long.compare(o1.getTermFrequency(), o2.getTermFrequency());
				}
            });
            Collections.reverse(c.getValue());
            
        	int i = 0;
        	for(Tag tag: c.getValue())
        	{
        		if(i>10)
        			break;
	            final JLabel label = new JLabel(tag.getTagName());
	            label.setOpaque(false);
	            label.setFont(label.getFont().deriveFont(
	            		(float)
	            		(Math.log(tag.getTermFrequency())
	            		*5
	            		)));
	            panel.add(label);
	            i++;
                myTagCloud.add(tag);
        	}
            final JLabel label = new JLabel(".");
            panel.add(label);
        }
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setVisible(true);
        return myTagCloud;
    }
    
	protected static List<Tag> initUI(List<Tag> tags){
	    JFrame frame = new JFrame("Tag Cloud Workshop");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JPanel panel = new JPanel();
        List<Tag> myTagCloud = new ArrayList<>();
	    
	    int i = 0;
	    for(Tag tag: tags){
	        if(i < 100)
	        {
	            final JLabel label = new JLabel(tag.getTagName());
		        label.setOpaque(false);
		        label.setFont(label.getFont().deriveFont((float)Math.log(tag.getTermFrequency())*5));
		        panel.add(label);
		        i++;
	            myTagCloud.add(tag);
	        }
	        else
	        	break;
	    }
	    frame.add(panel);
	    frame.setSize(800, 600);
	    frame.setVisible(true);
        return myTagCloud;
	}

	private static List<String> getDocsFromTagCloud(List<Tag> tagcloud){
        List<String> docstag = new ArrayList<>();
        for(Tag tag: tagcloud){
            for(String doc: tag.getDocs()){
                if(!docstag.contains(doc)){
                    docstag.add(doc);
                }
            }
        }
        return docstag;
    }
    
    private static int intersecSetString(List<String> setstr1, List<String> setstr2){
        int intersec = 0;
        for(String str1: setstr1){
            for(String str2: setstr2){
                if(str2.equals(str1)) intersec++;
            }
        }
        return intersec;
    }
    
    public static double calcCov(List<Tag> tagcloud, List<Tag> tags){
        List<String> docstag = getDocsFromTagCloud(tagcloud);
        List<String> docs = getDocsFromTagCloud(tags);
        double cov = (double) docstag.size() / docs.size();
        return cov;
    }
    
    public static double calcOverlap(List<Tag> tagcloud, int numTweets){
        double overlap = 0.0;
        
        for(Tag tag1: tagcloud){
            for(Tag tag2: tagcloud){
                if(tag1 != tag2){
                    int intersec = 0;
                    List<String> docs1 = tag1.getDocs();
                    List<String> docs2 = tag2.getDocs();
                    intersec = intersecSetString(docs1,docs2);
                    if(docs1.size() <= docs2.size()) overlap += (double) intersec / (double)docs1.size();
                    else overlap += (double) intersec / (double)docs2.size();
                }
            }
        }
        List<String> tc = getDocsFromTagCloud(tagcloud);
        overlap = overlap / (double) tc.size();
        return overlap;
    }
    
    public static double calcRelevance(List<Tag> tagcloud, List<Tag> tags){
        double relevance = 0.0;
        //ArrayList<String> docs = getDocsFromTagCloud(tags);
        for(Tag tag1: tagcloud){
            for(Tag tag2: tags){
                List<String> docs1 = tag1.getDocs();
                List<String> docs2 = tag2.getDocs();
                int intersec = intersecSetString(docs1,docs2);
                relevance += (double) intersec / (double)docs1.size();
            }
        }
        List<String> tc = getDocsFromTagCloud(tagcloud);
        relevance = relevance / (double)tc.size();
        return relevance;
    }
}
