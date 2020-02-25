package scheduler;

import java.util.LinkedList;
import java.util.ListIterator;

public class TestAlg1k {

	public TestAlg1k() {
		
	}
	
	public boolean testScheduleActiveJob() {
		int jobNumber = 1000;
		int n = 1000;
		for(int i = 0; i < n; i++) {
			double beta = Service.generateBeta();
			int k = Service.generateK(beta);
			LinkedList<Job> totalJobs = Service.generateRandomInput(k, jobNumber);
			Alg1k a1 = new Alg1k(k, beta, totalJobs);
			for(int j = 0; j <= jobNumber; j++) {
				a1.scheduleActiveJobs(j);
				LinkedList<Job> activeJobs = a1.getActiveJobs();
				ListIterator<Job> it = activeJobs.listIterator();
				while(it.hasNext()) {
					Job j1 = it.next();
					if(j1.getArrivalTime() > j*k) 
						return false;
				}
			}		
		}
		return true;
	}
	
	public boolean testSorting() {
		int jobNumber = 1000;
		int n = 1000;
		for(int i = 0; i < n; i++) {
			double beta = Service.generateBeta();
			int k = Service.generateK(beta);
			LinkedList<Job> totalJobs = Service.generateRandomInput(k, jobNumber);
			Alg1k a1 = new Alg1k(k, beta, totalJobs);
			a1.scheduleActiveJobs(jobNumber);
			a1.sortActiveJobs();
			LinkedList<Job> activeJobs = a1.getActiveJobs();
			ListIterator<Job> it1 = activeJobs.listIterator(0);
			ListIterator<Job> it2 = activeJobs.listIterator(1);
			while(it2.hasNext()) {
				Job j1 = it1.next();
				Job j2 = it2.next();
				if(j1.getValue() < j2.getValue() || (j1.getValue() == j2.getValue() && j1.getArrivalTime() > j2.getArrivalTime())) 
					return false;
			}
		}
		return true;
	}
	
	public boolean testProcessPhase() {
		int jobNumber = 1000;
		int n = 10000;
		for(int i = 0; i < n; i++) {
			double expectedValue1 = 0;
			double expectedValue2 = 0;
			double beta = Service.generateBeta();			
			int k = Service.generateK(beta);
			LinkedList<Job> totalJobs = Service.generateRandomInput(k, jobNumber);
			Alg1k a1 = new Alg1k(k, beta, totalJobs);
			for(int phaseNumber = 0; !a1.getTotalJobs().isEmpty() || !a1.getActiveJobs().isEmpty(); phaseNumber++) {
				a1.scheduleActiveJobs(phaseNumber);
				a1.sortActiveJobs();
				LinkedList<Job> activeJobs = a1.getActiveJobs();
				if(!activeJobs.isEmpty()) {
					ListIterator<Job> it = activeJobs.listIterator();
					int d = 0;
					while(it.hasNext()) {
						Job j = new Job(it.next());
						while(j.getLength() != 0 && d < k) {
							expectedValue1 = expectedValue1 +  Math.pow(beta, phaseNumber*k + d) * j.getValue();
							j.setLength(j.getLength() - 1);
							d++;
						}
					}
				}
				expectedValue2 = a1.processPhase(phaseNumber, expectedValue2);
				System.out.println(expectedValue1 + " ------------ " + expectedValue2);
				if(expectedValue1 != expectedValue2)
					return false;
			}
		}
		return true;
	}
}
