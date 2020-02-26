package scheduler;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {
		// testAlg1kScheduling();
		TestAlg1k t1 = new TestAlg1k();
		TestAlg2k t2 = new TestAlg2k();
		TestAlgmk tm = new TestAlgmk();
		//System.out.println(t1.testSorting());
		//System.out.println(t1.testScheduleActiveJob());
		//System.out.println(t1.testProcessPhase());
		//System.out.println(tm.testProcessPhase());
		System.out.println(t2.testCompetitiveRatio());
		//System.out.println(t2.testCompetitiveRatio());
		
	}

	private static void start() {
		Random r = new Random();
		double beta;
		do
			beta = r.nextDouble();
		while (beta == 0);
		int k;
		beta = 0.8;
		if (beta <= (2 / 3))
			k = 1;
		else
			k = (int) Math.floor(-Math.log(2) / Math.log(beta)) + 1;

		LinkedList<Job> totalJobs = generateInput(k);
		Alg1k a1 = new Alg1k(k, beta, cloneList(totalJobs));
		Alg2k a2 = new Alg2k(k, beta, cloneList(totalJobs));
		Chop c = new Chop(beta, cloneList(totalJobs));

		System.out.println("ALG1k:");
		double ealg1 = a1.start();
		System.out.println("Val atteso: " + ealg1);

		System.out.println("ALG2k:");
		double ealg2 = a2.start();
		System.out.println("Val atteso: " + ealg2);

		System.out.println("Chop:");
		double eopt = c.start();
		System.out.println("Val atteso: " + eopt);
		double cRatio = 1 / ((Math.pow(beta, k - 1)) * (1 - Math.pow(beta, k)));

		System.out.println("Beta = " + beta);
		System.out.println("K = " + k);
		System.out.println("C-ratio = " + cRatio);
		System.out.println("c * ALG1k: " + cRatio * ealg1 + "	OPT: " + eopt);

	}


	
	
	private static LinkedList<Job> generateInput(int k) {
		LinkedList<Job> totalJobs = new LinkedList<Job>();
		Random random = new Random();
		int n = 10;
		int x = k * n;
		int y = n;
		int z = k + 3;
		for (int i = 0; i < n; i++) {
			int a = random.nextInt(x) + 1;
			int v = random.nextInt(y) + 1;
			int l = random.nextInt(z) + 1;
			totalJobs.add(new Job(i, a, v, l));
		}
		return totalJobs;
	}
	private static LinkedList<Job> cloneList(LinkedList<Job> l) {
		LinkedList<Job> r = new LinkedList<Job>();
		ListIterator<Job> it = l.listIterator();
		while (it.hasNext()) {
			Job j = it.next();
			r.add(new Job(j));
		}
		return r;
	}

}
