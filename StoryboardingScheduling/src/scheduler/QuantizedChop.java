package scheduler;

import java.util.LinkedList;
import java.util.ListIterator;

public class QuantizedChop {
	private int k;
	private double beta;
	private LinkedList<Job> totalJobs;
	
	public QuantizedChop(int k, double beta, LinkedList<Job> totalJobs) {
		this.k = k;
		this.beta = beta;
		this.totalJobs = totalJobs;
	}
	
	public double start() {
		double expectedValue = 0;
		int t = 0;
		quantizeTotalJobs();
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
	
		
	private void quantizeTotalJobs() {
		ListIterator<Job> it = totalJobs.listIterator();
		while(it.hasNext()) {
			Job j = it.next();
			j.setArrivalTime((int) (k * Math.ceil((double)j.getArrivalTime()/k)));
		}
		
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
