/** @author: Ivana Akrum (s2861348)
*	@organisation: University of Groningen
*	@description: Clustering algorithm using KMeans method. Code does not run on its own,
	but gives a clear indication of how one would design such a KMeans algorithm.
**/
import java.util.*;

public class KMeans extends ClusteringAlgorithm
{
	// Number of clusters
	private int k;

	// Dimensionality of the vectors
	private int dim;
	
	// Threshold above which the corresponding html is prefetched
	private double prefetchThreshold;
	
	// Array of k clusters, class cluster is used for easy bookkeeping
	private Cluster[] clusters;
	
	// This class represents the clusters, it contains the prototype (the mean of all it's members)
	// and memberlists with the ID's (which are Integer objects) of the datapoints that are member of that cluster.
	// You also want to remember the previous members so you can check if the clusters are stable.
	static class Cluster
	{
		float[] prototype;

		Set<Integer> currentMembers;
		Set<Integer> previousMembers;
		  
		public Cluster()
		{
			currentMembers = new HashSet<Integer>();
			previousMembers = new HashSet<Integer>();
		}
	}
	// These vectors contains the feature vectors you need; the feature vectors are float arrays.
	// Remember that you have to cast them first, since vectors return objects.
	private Vector<float[]> trainData;
	private Vector<float[]> testData;

	// Results of test()
	private double hitrate;
	private double accuracy;
	
	public KMeans(int k, Vector<float[]> trainData, Vector<float[]> testData, int dim)
	{
		this.k = k;
		this.trainData = trainData;
		this.testData = testData; 
		this.dim = dim;
		prefetchThreshold = 0.5;
		
		// Here k new cluster are initialized
		clusters = new Cluster[k];
		for (int ic = 0; ic < k; ic++)
			clusters[ic] = new Cluster();
	}
  
	public boolean train()
	{
		/// start with random partition
		randomPartition();
		/// generate new partitions until stable
		while(!isStable()) {
			recalcPrototypes();
			newPartition();
		}
		return true;
	}

  /// Make a random partition
  private void randomPartition() {
		Random random = new Random();
		for (int i = 0; i < trainData.size(); i++) {
			clusters[random.nextInt(k)].currentMembers.add(new Integer(i));
		}
  }
  
	/// Checks if clustermembership is stable 
	private boolean isStable()
	{
		for(Cluster c : clusters) {
			/// stable if there is no change in membership 
			if(!c.currentMembers.equals(c.previousMembers)) {
				return false;
			}
		}
		return true;
	}
	
	/// Recalculate cluster centers through sum(members)/members.size
	private void recalcPrototypes() {
		for(Cluster c: clusters) {
			c.prototype = new float[dim];
			Iterator iter = c.currentMembers.iterator();
			
			while(iter.hasNext()) {
				int member = ((Integer)iter.next()).intValue();
				float[] data = (float[])trainData.get(member);
				for(int i = 0; i < dim; i++) {
					c.prototype[i] += data[i];
				}
			}
			
			for(int i = 0; i < dim; i++) {
				c.prototype[i] /= c.currentMembers.size();
			}
		}
	}
  
  /// Generate new partition by reassigning each datapoint to its closest cluster center
  private void newPartition() {
		clearClusterMembers();
		for (int i = 0; i < trainData.size(); i++) {
			int closest = findClosest((float[])trainData.get(i));
			clusters[closest].currentMembers.add(new Integer(i));
		}
  }
  
  /// Move currentMembers to previousMembers and clear currentMembers
  private void clearClusterMembers() {
		for(Cluster c : clusters) {
			c.previousMembers.clear();
			Iterator iter = c.currentMembers.iterator();
			while(iter.hasNext()) {
				c.previousMembers.add((Integer)iter.next());
			}
			c.currentMembers.clear();
		}
  }
  
	/// Find closest cluster center to datapoint using euclidean distance
	private int findClosest(float[] data) {
		int closest = -1;
		double min = 10000;
		for(int ic = 0; ic < k; ic++) {
			double distance = 0;
			for(int i = 0; i < dim; i++) {
				distance += Math.pow(data[i] - clusters[ic].prototype[i], 2.0);
			}
			if(distance < min || ic == 0) {
				min = distance;
				closest = ic;
			} 
		}
		/// Return index of closest cluster member
		return closest;
	}
  
	public boolean test()
	{
		double hits = 0;
		double prefetched = 0;
		double requests = 0;
		/// Iterate over all clusters
		for(Cluster c : clusters) {
			Iterator iter = c.currentMembers.iterator();
			while(iter.hasNext()) {
				/// Get client information 
				int clientID = ((Integer)iter.next()).intValue();
				float[] clientData = (float[])testData.get(clientID);
				/// For full dimension, compute parameters
				for(int i = 0; i < dim; i++) {
					if(c.prototype[i] > prefetchThreshold) {
						prefetched++;
						if(Math.round(clientData[i]) == 1) {
							hits++;
						}
					}
					if(Math.round(clientData[i]) == 1) {
						requests++;
					}
				}
			}
		}
		
		/// Extra output so that it is clear how hitrate and accuracy were computed
		System.out.println("hits: " + hits);
		System.out.println("requests: " + requests);
		System.out.println("prefetched: " + prefetched);
		
		/// Determine hitrate and accuracy
		hitrate = (hits / requests);
		accuracy = (hits / prefetched);
		return true;
	}


	// The following members are called by RunClustering, in order to present information to the user
	public void showTest()
	{
		System.out.println("Prefetch threshold=" + this.prefetchThreshold);
		System.out.println("Hitrate: " + this.hitrate);
		System.out.println("Accuracy: " + this.accuracy);
		System.out.println("Hitrate+Accuracy=" + (this.hitrate + this.accuracy));
	}
	
	public void showMembers()
	{
		for (int i = 0; i < k; i++)
			System.out.println("\nMembers cluster["+i+"] :" + clusters[i].currentMembers);
	}
	
	public void showPrototypes()
	{
		for (int ic = 0; ic < k; ic++) {
			System.out.print("\nPrototype cluster["+ic+"] :");
			
			for (int ip = 0; ip < dim; ip++)
				System.out.print(clusters[ic].prototype[ip] + " ");
			
			System.out.println();
		 }
	}

	// With this function you can set the prefetch threshold.
	public void setPrefetchThreshold(double prefetchThreshold)
	{
		this.prefetchThreshold = prefetchThreshold;
	}
}
