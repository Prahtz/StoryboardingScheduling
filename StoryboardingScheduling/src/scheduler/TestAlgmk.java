package scheduler;

import java.util.LinkedList;

public class TestAlgmk {
	public boolean testProcessPhase() {
		int jobNumber = 10;
		int n = 1;
		for(int i = 0; i < n; i++) {
			double beta = Service.generateBeta();			
			int k = Service.generateKAlgmk(beta);
			int m = Service.generateM(jobNumber);
			LinkedList<Job> totalJobs = Service.generateRandomInput(k, jobNumber);
			Service.printJobList(totalJobs);
			System.out.println(beta);
			System.out.println(k);
			Algmk a1 = new Algmk(k, m, beta, totalJobs);
			a1.start();
		}
		return true;
	}
}
