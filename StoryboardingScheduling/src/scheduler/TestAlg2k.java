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
		int max = 10000000;
		double beta = 0.86;
		int k = Service.generateKAlg2k(beta);
		Alg2k alg;
		QuantizedChop chop;
		double a,qc;
		double c = 1/(Math.pow(beta, k-1)) *  max(1/Math.pow(beta,k-1), 
				1/(1 - Math.pow(beta, 2*k)), 1 + (Math.pow(beta, 3*k) / (1 - Math.pow(beta, k))));
		
		//if (k==1) return;
		LinkedList<Job> totalJobs = new LinkedList<Job>();
		/*
		int n = 100;
		for(int i = 0, j = 0; j < n; i+=2*k,j++) {
			totalJobs.add(new Job(i, i, 0, k));
			totalJobs.add(new Job(i+1, i, 1, k - 1));
		}
		for(int i = k, j = 0; j < n; i+=2*k, j++) {
			totalJobs.add(new Job(i, i, max, 1));
		}

		
		Alg2k alg = new Alg2k(k, beta, Service.cloneList(totalJobs));
		QuantizedChop chop = new QuantizedChop(k, beta, totalJobs);
		double a = alg.start();
		double qc = chop.start();

		System.out.println(k + " " + a + " " + qc);
		System.out.println(c * a >= 1/(Math.pow(beta, k-1)) * qc);
		System.out.println(c * a + " " + 1/(Math.pow(beta, k-1)) * qc);*/
		
		totalJobs = new LinkedList<Job>();
		int maxL = max;
		
		totalJobs.add(new Job(0, 0, max - 1, maxL));
		totalJobs.add(new Job(1, k, max, 1));
		
		alg = new Alg2k(k, beta, Service.cloneList(totalJobs));
		chop = new QuantizedChop(k, beta, totalJobs);
		
		a = alg.start();
		qc = chop.start();
		//qc = chop.start();
		
		System.out.println(k + " " + a + " " + qc);
		System.out.println(c * a >= 1/(Math.pow(beta, k-1)) * qc);
		System.out.println(c * a + " " + 1/(Math.pow(beta, k-1)) * qc);
		System.out.println((max-1) * (1 - Math.pow(beta, k) + Math.pow(beta, k + 1)) / (1-beta)  + Math.pow(beta, k) * max);
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
			fw.write(beta + ";" + k + ";" + c + ";\n");
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
		int jobNumber = 100;
		int lengthFactor = 4;
		LinkedList<LinkedList<Job>> inputList = new LinkedList<LinkedList<Job>>();
		for(int i = 0; i < n; i++)
			inputList.add(Service.generateRandomInput(lengthFactor, jobNumber));
		if(!testAndWriteResults(inputList, getBetaGenerator(0, 0.3), "ALG2k0and0_3.csv"))
			System.out.println(false);
		if(!testAndWriteResults(inputList, getBetaGenerator(0.3, 0.6), "ALG2k0_3and0_6.csv"))
			System.out.println(false);
		if(!testAndWriteResults(inputList, getBetaGenerator(0.6, 0.8), "ALG2k0_6and0_8.csv"))
			System.out.println(false);
		if(!testAndWriteResults(inputList, getBetaGenerator(0.8, 1), "ALG2k0_8and1.csv"))
			System.out.println(false);
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
