package scheduler;
import java.util.LinkedList;
import java.util.ListIterator;

public class Alg1k {
	private double beta;
	private int k;
	private LinkedList<Job> totalJobs;
	private LinkedList<Job> activeJobs;
	
	public Alg1k(int k, double beta, LinkedList<Job> totalJobs) {
		this.k = k;
		this.beta = beta;
		this.totalJobs = totalJobs;
		this.activeJobs = new LinkedList<Job>();
	}
	
	public double start() {
		int phaseNumber = 0;
		double expectedValue = 0;
		while(!totalJobs.isEmpty() || !activeJobs.isEmpty()) {
			scheduleActiveJobs(phaseNumber);
			sortActiveJobs();
			expectedValue = processPhase(phaseNumber, expectedValue);
			phaseNumber++;
		}
		return expectedValue;
	}
	
	private void scheduleActiveJobs(int phaseNumber) {
		int actualTimeUnit = phaseNumber*k;
		Job j;
		ListIterator<Job> ti = totalJobs.listIterator();
		while(ti.hasNext()) {
			j = ti.next();
			if(j.getArrivalTime() <= actualTimeUnit) {
				activeJobs.add(j);
				ti.remove();
			}
		}
	}
	
	private void sortActiveJobs() {
		activeJobs.sort(new JobComparator());
	}
	
	private double processPhase(int phaseNumber, double expectedValue) {
		if(!activeJobs.isEmpty()) {
			int actualTimeUnit = phaseNumber*k;
			ListIterator<Job> ai = activeJobs.listIterator();
			Job j = ai.next();
			ai.remove();
			for(int i = 0; i < k; i++, actualTimeUnit++) {
				expectedValue = expectedValue + Math.pow(beta, actualTimeUnit) * j.getValue();
				j.setLength(j.getLength() - 1); 
				if(j.getLength() == 0) 
					if (ai.hasNext() && i != k - 1) {
						j = ai.next();
						ai.remove();
					}
					else
						break;
			}
		}
		return expectedValue;
	}
	
	
	public LinkedList<Job> getTotalJobs() {
		return totalJobs;
	}

	public void setTotalJobs(LinkedList<Job> totalJobs) {
		this.totalJobs = totalJobs;
	}

	public LinkedList<Job> getActiveJobs() {
		return activeJobs;
	}

	public void setActiveJobs(LinkedList<Job> activeJobs) {
		this.activeJobs = activeJobs;
	}
	
	public void printJobList(LinkedList<Job> list) {
		for(int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
		System.out.println();
	}
}


