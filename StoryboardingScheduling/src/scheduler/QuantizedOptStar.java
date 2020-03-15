package scheduler;

import java.util.LinkedList;
import java.util.ListIterator;

public class QuantizedOptStar {
	private double beta;
	private int k;
	private int m;
	private LinkedList<Job> totalJobs;
	private LinkedList<Job> activeJobs;
	
	public QuantizedOptStar(int k, int m, double beta, LinkedList<Job> totalJobs) {
		this.k = k;
		this.m = m;
		this.beta = beta;
		this.totalJobs = totalJobs;
		this.activeJobs = new LinkedList<Job>();
	}
	
	public double start() {
		int t = 0;
		m = 2 * m;
		double expectedValue = 0;
		quantizeTotalJobs();
		while(!totalJobs.isEmpty() || !activeJobs.isEmpty()) {
			scheduleActiveJobs(t);
			sortActiveJobs();
			Job[] machines = new Job[m];		
			for(int i = 0; i < m && !activeJobs.isEmpty(); i++) 	
				machines[i] = activeJobs.poll();
			for(int i = 0; i < machines.length; i++) {
				expectedValue = expectedValue + Math.pow(beta, t) * machines[i].getValue();
				machines[i].setLength(machines[i].getLength() - 1);
				if(machines[i].getLength() != 0)
					activeJobs.add(machines[i]);
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
	
	public void sortActiveJobs() {
		activeJobs.sort(new JobComparator());
	}
	
	public void scheduleActiveJobs(int t) {
		Job j;
		ListIterator<Job> ti = totalJobs.listIterator();
		while(ti.hasNext()) {
			j = ti.next();
			if(j.getArrivalTime() <= t) {
				activeJobs.add(j);
				ti.remove();
			}
		}
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
