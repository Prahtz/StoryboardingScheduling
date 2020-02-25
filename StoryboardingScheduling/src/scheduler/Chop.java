package scheduler;
import java.util.LinkedList;
import java.util.ListIterator;

public class Chop {
	private double beta;
	private LinkedList<Job> totalJobs;
	
	public Chop(double beta, LinkedList<Job> totalJobs) {
		this.beta = beta;
		this.totalJobs = totalJobs;
	}
	
	public double start() {
		double expectedValue = 0;
		int t = 0;
		sortTotalJobs();
		
		while(!totalJobs.isEmpty()) {
			ListIterator<Job> it = totalJobs.listIterator();
			while(it.hasNext()) {
				Job j = it.next();
				if(j.getArrivalTime() <= t) {
					it.remove();
					expectedValue = expectedValue + Math.pow(beta, t) * j.getValue();
					j.setLength(j.getLength() - 1);
					if(j.getLength() != 0) 
						it.add(j);
					break;
				}
			}
			t++;
		}
		return expectedValue;
	}
	
		
	public void sortTotalJobs() {
		totalJobs.sort(new JobComparator());
	}
	
	public void printJobList(LinkedList<Job> list) {
		for(int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
		System.out.println();
	}
}
