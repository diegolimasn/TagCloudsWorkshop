package br.ufba.matc96.tagcloud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TagCloudMain
{
	public static void main(String[] args)
	{

		Corpus corpus = new TwitterCorpus("bbcbrasil");
		//Corpus corpus = new TextFileCorpus("test_data");
		//TagCloudCreator creator = new SCTagCloudCreator(corpus, 5, 20);
		TagCloudCreator creator = new PopularityTagCloudCreator(corpus, 100);
		TagCloud tagCloud = creator.create();
		displayTagCloud(tagCloud);

		//if(args[0].equals("0")){
		/*boolean sclusteringOn = true;
		if(sclusteringOn)
		{
			final List<Tag> onlyTags = new ArrayList<>();
            for(String s : tags.keySet())
            	onlyTags.add(tags.get(s));

	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	            	List<Tag> myTagCloud = initUI(clusters2);
                    double cov = calcCov(myTagCloud, onlyTags);
                    System.out.println("Coverage = " + cov);
                    double over = calcOverlap(myTagCloud, onlyTags);
                    System.out.println("Overlap = " + over);
                    double rel = calcRelevance(myTagCloud, onlyTags);
                    System.out.println("Relevance = " + rel);
	            }
	        });
		}
		else
		{
            
            final List<Tag> onlyTags2 = onlyTags;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    List<Tag> myTagCloud = initUI(onlyTags2);
                    double cov = calcCov(myTagCloud, onlyTags2);
                    System.out.println("Coverage = " + cov);
                    double over = calcOverlap(myTagCloud, onlyTags2);
                    System.out.println("Overlap = " + over);
                    double rel = calcRelevance(myTagCloud, onlyTags2);
                    System.out.println("Relevance = " + rel);
                }
            });
		}*/
	}
	
	protected static void displayTagCloud(final TagCloud tagCloud)
	{
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	initUI(tagCloud.getClusters());
            }
        });
	}
	
	protected static List<Tag> initUI(Map<String, List<Tag>> clusters) {
        JFrame frame = new JFrame("Tag Cloud Workshop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        List<Tag> myTagCloud = new ArrayList<>();
        
        for (Entry<String, List<Tag>> c : clusters.entrySet()) {
        	Collections.sort(c.getValue(), new Comparator<Tag>(){
				@Override
				public int compare(Tag o1, Tag o2)
				{
					return Long.compare(o1.getTermFrequency(), o2.getTermFrequency());
				}
            });
            Collections.reverse(c.getValue());
            
        	for(Tag tag: c.getValue())
        	{
	            final JLabel label = new JLabel(tag.getTagName());
	            label.setOpaque(false);
	            label.setFont(label.getFont().deriveFont(
	            		(float) (Math.log(tag.getTermFrequency())*5)));
	            panel.add(label);
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
    
    public static double calcOverlap(List<Tag> tagcloud, List<Tag> tags){
        double overlap = 0.0;
        //ArrayList<String> docs = getDocsFromTagCloud(tags);
        for(Tag tag1: tagcloud){
            for(Tag tag2: tagcloud){
                if(tag1 != tag2){
                    int intersec = 0;
                    List<String> docs1 = tag1.getDocs();
                    List<String> docs2 = tag2.getDocs();
                    intersec = intersecSetString(docs1,docs2);
                    if(docs1.size() <= docs2.size()) overlap += ((double) intersec / (double)docs1.size());
                    else overlap += ((double) intersec / (double)docs2.size());
                }
            }
        }
        List<String> tc = getDocsFromTagCloud(tagcloud);
        overlap = overlap / (double) tc.size();
        return overlap;
    }
    
    public static double calcRelevance(List<Tag> tagcloud, List<Tag> tags){
        double relevance = 0.0;
        for(Tag tag1: tagcloud){
            for(Tag tag2: tags){
                //if(!tag1.equals(tag2)){
                    List<String> docs1 = tag1.getDocs();
                    List<String> docs2 = tag2.getDocs();
                    int union = intersecSetString(docs1, docs2);
                    relevance += ((double) union / (double) docs1.size());
                //}
            }
        }
        List<String> tc = getDocsFromTagCloud(tags);
        relevance = relevance / (double) tc.size();
        return relevance;
    }
}
