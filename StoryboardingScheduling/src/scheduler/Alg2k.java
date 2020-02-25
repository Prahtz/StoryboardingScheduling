package scheduler;
import java.util.LinkedList;
import java.util.ListIterator;

public class Alg2k {
	private double beta;
	private int k;
	private LinkedList<Job> totalJobs;
	private LinkedList<Job> activeJobs;
	private Job preemptedJob;
	
	public Alg2k(int k, double beta, LinkedList<Job> totalJobs) {
		this.k = k;
		this.beta = beta;
		this.totalJobs = totalJobs;
		this.activeJobs = new LinkedList<Job>();
		this.preemptedJob = null;
	}
	
	public double start() {
		int phaseNumber = 0;
		double expectedValue = 0;
		while(!totalJobs.isEmpty() || !activeJobs.isEmpty()) {
			scheduleActiveJobs(phaseNumber);
			sortActiveJobs();
			expectedValue = processPhase(phaseNumber, expectedValue, createSchedule(phaseNumber));
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
	
	public LinkedList<Job> createSchedule(int phaseNumber) {
		LinkedList<Job> S = new LinkedList<Job>();
		if(!activeJobs.isEmpty()) {
			ListIterator<Job> it = activeJobs.listIterator();
			for(int i = 0; i < k;) {
				if(it.hasNext()) {
					Job j = it.next();
					it.remove();
					S.add(j);
					i = i + j.getLength();
				}
				else
					break;
			}
			if(preemptedJob != null && S.remove(preemptedJob))
				S.addFirst(preemptedJob);
		}
		activeJobs.remove(preemptedJob);
		preemptedJob = null;
		return S;
	}
	
	public double processPhase(int phaseNumber, double expectedValue, LinkedList<Job> S) {
		printJobList(S);
		if(!S.isEmpty()) {
			int actualTimeUnit = phaseNumber*k;
			ListIterator<Job> si = S.listIterator();
			int i = 0;
			while(si.hasNext()) {
				Job j = si.next();
				for(; i < k && j.getLength() != 0; i++, actualTimeUnit++) {
					expectedValue = expectedValue + Math.pow(beta, actualTimeUnit) * j.getValue();
					j.setLength(j.getLength() - 1);
				}
				if(j.getLength() != 0) {
					preemptedJob = j;
					activeJobs.add(j);
					break;
				}
			}
			/*
			Job j = si.next();
			for(int i = 0; i < k; i++, actualTimeUnit++) {
				expectedValue = expectedValue + Math.pow(beta, actualTimeUnit) * j.getValue();
				j.setLength(j.getLength() - 1);
				if(j.getLength() == 0) 
					if (si.hasNext() && i != k - 1)
						j = si.next();
					else
						break;
				else if(i == k) {
					preemptedJob = j;
					activeJobs.add(j);
				}
			}*/
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

	public Job getPreemptedJob() {
		return preemptedJob;
	}
}


