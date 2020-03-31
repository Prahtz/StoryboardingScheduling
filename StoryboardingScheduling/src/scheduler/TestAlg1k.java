package scheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class TestAlg1k {
	
	public TestAlg1k() {}
	
	public boolean testAndWriteWorstCaseResults(BetaGenerator bg, String fileName) throws IOException {
		File f = new File("csv/" + fileName);
		f.delete();
		f.createNewFile();
		FileWriter fw = new FileWriter(f);
		int max = 100000;
		int maxLength = 1000000;
		int n = 1000;
		for(int i = 0; i < n; i++) {
			double beta = bg.generateBeta();
			int k = Service.generateKAlg1k(beta);
			double c = Math.floor(1/(Math.pow(beta, k-1) * (1 - Math.pow(beta,k))) * 1000000 + 1)/1000000;
			fw.write(beta + ";" + k + ";" + c + ";\n");
			LinkedList<Job> input = new LinkedList<Job>();
			input.add(new Job(0, 0, max, maxLength));
			Alg1k alg = new Alg1k(k, beta, Service.cloneList(input));
			QuantizedChop qc = new QuantizedChop(k, beta, input);
			
			double algValue = alg.start();
			double chopValue = qc.start();
			fw.write(algValue + ";" + chopValue + ";");
		
			if(c*algValue  < (1/Math.pow(beta, k-1)) * chopValue) {
				System.out.println(c * algValue + " " + (1/Math.pow(beta, k-1)) * chopValue);
				fw.flush();
				fw.close();
				return false;
			}
			fw.write("\n");
		}
		fw.flush();
		fw.close();
		return true;
	}
	
	public boolean testAndWriteMaximumResults(LinkedList<LinkedList<Job>> inputList, BetaGenerator bg, String fileName) throws IOException {
		File f = new File("csv/" + fileName);
		f.delete();
		f.createNewFile();
		FileWriter fw = new FileWriter(f);
		int n = 1000;
		for(int i = 0; i < n; i++) {
			ListIterator<LinkedList<Job>> it = inputList.listIterator();
			double beta = bg.generateBeta();
			int k = Service.generateKAlg1k(beta);
			double c = 1/(Math.pow(beta, k-1) * (1 - Math.pow(beta,k)));
			fw.write(beta + ";" + k + ";" + c + ";\n");
			while(it.hasNext()) {
				LinkedList<Job> input = it.next();
				Alg1k alg = new Alg1k(k, beta, Service.cloneList(input));
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
	
	public void startWorstCaseTesting() throws IOException {
		if(!testAndWriteWorstCaseResults(getBetaGenerator(0,0.3), "ALG1k0and0_3Worst.csv")) 
			System.out.println(false);
		if(!testAndWriteWorstCaseResults(getBetaGenerator(0.3,0.6), "ALG1k0_3and0_6Worst.csv")) 
			System.out.println(false);
		if(!testAndWriteWorstCaseResults(getBetaGenerator(0.6,0.8), "ALG1k0_6and0_8Worst.csv")) 
			System.out.println(false);
		if(!testAndWriteWorstCaseResults(getBetaGenerator(0.8, 1), "ALG1k0_8and1Worst.csv")) 
			System.out.println(false);
	}
	
	public void startTesting() throws IOException {
		int n = 1000;
		int jobNumber = 100;
		int lengthFactor = 4;
		LinkedList<LinkedList<Job>> inputList = new LinkedList<LinkedList<Job>>();
		for(int i = 0; i < n; i++)
			inputList.add(Service.generateRandomInput(lengthFactor, jobNumber));
		
		if(!testAndWriteMaximumResults(inputList, getBetaGenerator(0, (double)2/3), "ALG1kSmallBetas.csv"))
			System.out.println(false);
		if(!testAndWriteMaximumResults(inputList, getBetaGenerator((double)2/3, 1), "ALG1kBigBetas.csv"))
			System.out.println(false);
		if(!testAndWriteMaximumResults(inputList, getBetaGenerator(0, 0.3), "ALG1k0and0_3.csv"))
			System.out.println(false);
		if(!testAndWriteMaximumResults(inputList, getBetaGenerator(0.3, 0.6), "ALG1k0_3and0_6.csv"))
			System.out.println(false);
		if(!testAndWriteMaximumResults(inputList, getBetaGenerator(0.6, 0.8), "ALG1k0_6and0_8.csv"))
			System.out.println(false);
		if(!testAndWriteMaximumResults(inputList, getBetaGenerator(0.8, 1), "ALG1k0_8and1.csv"))
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
	

}
