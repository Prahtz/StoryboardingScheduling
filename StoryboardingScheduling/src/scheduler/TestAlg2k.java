package scheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

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
	
	public boolean testAndWriteResults(LinkedList<LinkedList<Job>> inputList, BetaGenerator bg, String fileName) throws IOException {
		File f = new File("csv/" + fileName);
		f.delete();
		f.createNewFile();
		FileWriter fw = new FileWriter(f);
		int n = 1000;
		for(int i = 0; i < n; i++) {
			ListIterator<LinkedList<Job>> it = inputList.listIterator();
			double beta = bg.generateBeta();
			int k = Service.generateKAlg2k(beta);
			double c = 1/(Math.pow(beta, k-1)) *  max(1/Math.pow(beta,k-1), 
					1/(1 - Math.pow(beta, 2*k)), 1 + (Math.pow(beta, 3*k) / (1 - Math.pow(beta, k))));
			fw.write(beta + ";" + k + ";" + c + "\n;");
			while(it.hasNext()) {
				LinkedList<Job> input = it.next();
				Alg2k alg = new Alg2k(k, beta, Service.cloneList(input));
		 		QuantizedChop chop = new QuantizedChop(k, beta, Service.cloneList(input));
				double algValue = alg.start();
				double chopValue = chop.start();
				fw.write(algValue + ";" + chopValue + ";");
				if(c * algValue < (1/Math.pow(beta, k-1)) * chopValue) {
					fw.flush();
					fw.close();
					return false;
				}
			}
			fw.write("\n");
		}
		fw.flush();
		fw.close();
		return true;
	}
	
	public void startTesting() throws IOException {
		int n = 1000;
		int jobNumber = 1000;
		LinkedList<LinkedList<Job>> inputList = new LinkedList<LinkedList<Job>>();
		for(int i = 0; i < n; i++)
			inputList.add(Service.generateRandomInput(jobNumber, 4));
		testAndWriteResults(inputList, getBetaGenerator(0, 0.3), "ALG2k0and0_3.csv");
		testAndWriteResults(inputList, getBetaGenerator(0.3, 0.6), "ALG2k0_3and0_6.csv");
		testAndWriteResults(inputList, getBetaGenerator(0.6, 0.8), "ALG2k0_6and0_8.csv");
		testAndWriteResults(inputList, getBetaGenerator(0.8, 1), "ALG2k0_8and1.csv");
	}
	
	private BetaGenerator getBetaGenerator(double a, double b) {
		BetaGenerator bg = () -> {
			Random random = new Random();
			double beta;
			do
				beta = random.nextDouble();
			while (beta == 0 || beta == 1 || !(beta > a && beta <= b));
			return beta;
		};
		return bg;
	}

	private double max(double a, double b, double c) {
		if(a >= b && a >= c)
			return a;
		else if(b >= a && b >= c)
			return b;
		return c;
	}

	
}
