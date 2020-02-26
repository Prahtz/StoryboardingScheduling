package scheduler;

import java.util.LinkedList;
import java.util.ListIterator;

public class TestAlg2k {
	public TestAlg2k() {}
	
	public boolean testCompetitiveRatio() {
		int jobNumber = 1000;
		int n = 1000;
		double c = 1 + (1 + Math.sqrt(5))/2;
		for(int i = 0; i < n; i++) {
			double beta = Service.generateBeta();
			int k = Service.generateKAlg1k(beta);
			LinkedList<Job> totalJobs = Service.generateRandomInput(k, jobNumber);
			Alg2k alg = new Alg2k(k, beta, totalJobs);
			Chop chop = new Chop(beta, totalJobs);
			if(c * alg.start() < chop.start())
				return false;
		}
		return true;
	}
	
	public boolean testCreateScheduleAndProcessPhase() {
		int jobNumber = 1000;
		int n = 1000;
		for(int i = 0; i < n; i++) {
			double expectedValue1 = 0;
			double expectedValue2 = 0;
			double beta = Service.generateBeta();
			int k = Service.generateKAlg2k(beta);
			LinkedList<Job> totalJobs = Service.generateRandomInput(k, jobNumber);
			Alg2k a2 = new Alg2k(k, beta, totalJobs);
			Job preemptedJob = null;
			for(int phaseNumber = 0; !a2.getTotalJobs().isEmpty() || !a2.getActiveJobs().isEmpty() ; phaseNumber++) {
				a2.scheduleActiveJobs(phaseNumber);
				a2.sortActiveJobs();
				LinkedList<Job> activeJobs = Service.cloneList(a2.getActiveJobs());
				if(!activeJobs.isEmpty()) {
					LinkedList<Job> S = new LinkedList<Job>();
					ListIterator<Job> it = activeJobs.listIterator();
					int d = 0;
					while(it.hasNext() && d < k) {
						Job j = new Job(it.next());					
						S.add(j);
						d = d + j.getLength();	
					}
					if(preemptedJob != null && S.remove(preemptedJob))
						S.addFirst(preemptedJob);
					preemptedJob = null;
					
					d = 0;
					it = S.listIterator();
					Service.printJobList(S);
					while(it.hasNext()) {
						Job j = new Job(it.next());
						while(j.getLength() != 0 && d < k) {
							expectedValue1 = expectedValue1 +  Math.pow(beta, phaseNumber*k + d) * j.getValue();
							j.setLength(j.getLength() - 1);
							d++;
						}
						if(j.getLength() != 0) {
							preemptedJob = new Job(j);
							activeJobs.add(new Job(j));
							break;
						}
					}	
				}
				if(!activeJobs.isEmpty())
					expectedValue2 = a2.processPhase(phaseNumber, expectedValue2, a2.createSchedule(phaseNumber));
				System.out.println(expectedValue1 + " ------------ " + expectedValue2);
				String s = "Test: ";
				String t = "null";
				String a = "null";
				if(preemptedJob == null)
					s = s + "null" + " -------- Alg: ";
				else {
					t = preemptedJob.toString();
					s = s + preemptedJob.toString() + " -------- ";
				}
				if(a2.getPreemptedJob() == null)
					s = s + "null";
				else {
					s = s + a2.getPreemptedJob().toString();
					a = a2.getPreemptedJob().toString();
				}
				System.out.println(s);
				if(expectedValue1 != expectedValue2 || !t.equals(a))
					return false;
				
			}
		}
		return true;
	}
	
}
