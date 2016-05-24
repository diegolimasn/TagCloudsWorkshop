package br.ufba.matc96.tagcloud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import br.ufba.matc96.tagcloud.util.Pair;
import br.ufba.matc96.tagcloud.util.SymmetricTagMatrix;
import edu.ucla.sspace.clustering.Assignment;
import edu.ucla.sspace.clustering.Assignments;
import edu.ucla.sspace.clustering.CKVWSpectralClustering06;
import edu.ucla.sspace.matrix.Matrix;

public class SCTagCloudCreator implements TagCloudCreator
{
	Corpus corpus;
	Integer nClusters;
	Integer nTagsCluster;

	public SCTagCloudCreator(Corpus corpus, Integer nClusters, Integer nTagsCluster)
	{
		this.corpus = corpus;
		this.nClusters = nClusters;
		this.nTagsCluster = nTagsCluster;
	}
	
	@Override
	public TagCloud create()
	{
		Map<String, MyTag> tags = corpus.getTags();
		List<MyTagDocument> docs = corpus.getTagDocuments();

		SymmetricTagMatrix coMatrix = MyTag.getCooccurrenceMatrix(docs);
		SymmetricTagMatrix affinityMatrix = getAffinityMatrix(coMatrix,tags);
		Matrix m = affinityMatrix.toMatrix();

		CKVWSpectralClustering06 clusterer = new CKVWSpectralClustering06();
		Assignments a = clusterer.cluster(m, this.nClusters, new Properties());
		Assignment[] assignments = a.assignments();
		Map<String, List<MyTag>> clusters = new HashMap<String, List<MyTag>>();
		for (int i = 0; i < assignments.length; ++i)
        {
			if(!clusters.containsKey(Arrays.toString(assignments[i].assignments())))
			{
				clusters.put(Arrays.toString(assignments[i].assignments()), new ArrayList<MyTag>());
			}
			clusters.get(Arrays.toString(assignments[i].assignments())).add(tags.get(affinityMatrix.getTags().get(i)));
        }
		TagCloud tagCloud = new TagCloud();
		
		for (Entry<String, List<MyTag>> c : clusters.entrySet()) {
        	Collections.sort(c.getValue(), new Comparator<MyTag>(){
				@Override
				public int compare(MyTag o1, MyTag o2)
				{
					return Long.compare(o1.getTermFrequency(), o2.getTermFrequency());
				}
            });
            Collections.reverse(c.getValue());
            
        	int i = 0;
        	for(MyTag tag: c.getValue())
        	{
        		if(i>this.nTagsCluster)
        			break;
        		tagCloud.addTagToCluster(c.getKey(), tag);
        		i++;
        	}
		}
		
		return tagCloud;
	}

	private static SymmetricTagMatrix getAffinityMatrix(SymmetricTagMatrix coMatrix, Map<String, MyTag> tags)
	{
		SymmetricTagMatrix affinityMatrix = new SymmetricTagMatrix();
		for (Entry<Pair<String, String>, Float> entry : coMatrix.getMap().entrySet())
		{
			if(entry.getValue() > 3)
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
}
