package scheduler;

import java.util.LinkedList;

public class TestAlg2k {
	public TestAlg2k() {}
	
	public boolean testCompetitiveRatio() {
		int jobNumber = 1000;
		int n = 10000;
		double c = 4;
		for(int i = 0; i < n; i++) {
			double beta = Service.generateBeta();
			int k = Service.generateKAlg2k(beta);
			c = 1 + ((1 + Math.sqrt(5)) / 2);
			//c = 1/(Math.pow(beta, k-1) * (1 - Math.pow(beta,k)));
			LinkedList<Job> totalJobs = Service.generateRandomInput(k, jobNumber);
			Alg2k alg = new Alg2k(k, beta, Service.cloneList(totalJobs));
			QuantizedChop chop = new QuantizedChop(k, beta, totalJobs);
			double a = alg.start();
			double qc = chop.start();
			System.out.println(a + " " + qc);
			if(c * a < (1/Math.pow(beta, k-1)) * qc)
				return false;
		}
		return true;
	}
	
	public void testWorstCase() {
		//int max = Integer.MAX_VALUE;
		int max = 9999999;
		double beta = 0.7;
		int k = Service.generateKAlg2k(beta);
		LinkedList<Job> totalJobs = new LinkedList<Job>();
		int n = 10000;
		for(int i = 0, j = 0; j < n; i+=4,j++) {
			totalJobs.add(new Job(i,i,0,k));
			totalJobs.add(new Job(i+1,i,1,1));
		}
		for(int i = 2, j = 0; j < n; i+=4, j++) {
			totalJobs.add(new Job(i,i,max,1));
		}

		
		Alg2k alg = new Alg2k(k, beta, Service.cloneList(totalJobs));
		QuantizedChop chop = new QuantizedChop(k, beta, totalJobs);
		double a = alg.start();
		double qc = chop.start();
		System.out.println(k + " " + a + " " + qc);
	}

	
}
