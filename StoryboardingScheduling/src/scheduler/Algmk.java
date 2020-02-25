package scheduler;
import java.util.LinkedList;
import java.util.ListIterator;

public class Algmk {
	private double beta;
	private int m;
	private int k;
	private LinkedList<Job> totalJobs;
	private LinkedList<Job> activeJobs;
	
	public Algmk(int k, int m, double beta, LinkedList<Job> totalJobs) {
		this.k = k;
		this.m = m;
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

	public void scheduleActiveJobs(int phaseNumber) {
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
	
	public void sortActiveJobs() {
		activeJobs.sort(new JobComparator());
	}
	
	public double processPhase(int phaseNumber, double expectedValue) {
		int actualTimeUnit = phaseNumber*k;	
		Job[] machines = new Job[m];
		for(int i = 0; i < m && !activeJobs.isEmpty(); i++)
			machines[i] = activeJobs.poll();
		for(int i = 0; i < k; i++, actualTimeUnit++) {
			printJobArray(machines);
			for(int j = 0; j < machines.length; j++) {
				if(machines[j] != null) {
					expectedValue = expectedValue + Math.pow(beta, actualTimeUnit) * machines[j].getValue();
					machines[j].setLength(machines[j].getLength() - 1);
					if(machines[j].getLength() == 0)
						if(i < k - 1 && !activeJobs.isEmpty())
							machines[j] = activeJobs.poll();
						else
							machines[j] = null;
				}
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
	
	public void printJobArray(Job[] a) {
		for(int i = 0; i < a.length; i++) {
			if(a[i] != null)
				System.out.println(a[i].toString());
			else
				System.out.println("null");
		}
		System.out.println();
	}

}