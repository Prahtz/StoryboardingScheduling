package scheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class TestAlgmk {
	
	public void test() {
		double beta = 0.3;
		int max = 100;
		int maxLength = 10000;
		int m = 10;
		int k = Service.generateKAlg1k(beta);
		double c = (1/(Math.pow(beta, k-1)) * (1 + 1/(1 - Math.pow(beta,k))));
		LinkedList<Job> input = new LinkedList<Job>();
		for(int j = 0; j < 2*m; j++) 
			input.add(new Job(j, 0, max, maxLength));
		Algmk alg = new Algmk(k, m, beta, Service.cloneList(input));
		OptStar op = new OptStar(k, m, beta, input);
		
		double algValue = alg.start();
		double opValue = op.start();
		double mathAlg = max * m + beta * max * m;
		double mathOp = max * 2 * m + max * 2 * m *(beta/(1-beta));
		
		System.out.println(beta);
		System.out.println(algValue + " " + mathAlg);
		System.out.println(opValue + " " + mathOp);
		System.out.println(1/(Math.pow(beta, k-1)) * opValue / algValue);
	}
	
	public boolean testAndWriteWorstCaseResults(BetaGenerator bg, String fileName) throws IOException {
		File f = new File("csv/" + fileName);
		f.delete();
		f.createNewFile();
		FileWriter fw = new FileWriter(f);
		int max = 100;
		int maxLength = 10000;
		int n = 1000;
		int m = 10;
		for(int i = 0; i < n; i++) {
			double beta = bg.generateBeta();
			int k = Service.generateKAlgmk(beta);
			double c = (1/(Math.pow(beta, k-1)) * (1 + 1/(1 - Math.pow(beta,k))));
			fw.write(beta + ";" + k + ";" + c + ";\n");
			LinkedList<Job> input = new LinkedList<Job>();
			for(int j = 0; j < 2*m; j++) 
				input.add(new Job(j, 0, max, maxLength));
			Algmk alg = new Algmk(k, m, beta, Service.cloneList(input));
			OptStar op = new OptStar(k, m, beta, input);
			
			double algValue = alg.start();
			double opValue = op.start();
			fw.write(algValue + ";" + opValue + ";");
		
			if(c*algValue  < (1/Math.pow(beta, k-1)) * opValue) {
				System.out.println(c * algValue + " " + (1/Math.pow(beta, k-1)) * opValue);
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
		int m = 10;
		for(int i = 0; i < n; i++) {
			ListIterator<LinkedList<Job>> it = inputList.listIterator();
			double beta = bg.generateBeta();
			int k = Service.generateKAlgmk(beta);
			double c = (1/(Math.pow(beta, k-1)) * (1 + 1/(1 - Math.pow(beta,k))));
			fw.write(beta + ";" + k + ";" + c + ";\n");
			while(it.hasNext()) {
				LinkedList<Job> input = it.next();
				Algmk alg = new Algmk(k, m, beta, Service.cloneList(input));
		 		OptStar op = new OptStar(k, m, beta, Service.cloneList(input));
				double algValue = alg.start();
				double opValue = op.start();
				fw.write(algValue + ";" + opValue + ";");
				if(c * algValue < (1/Math.pow(beta, k-1)) * opValue) {
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
		if(!testAndWriteWorstCaseResults(getBetaGenerator(0,0.3), "ALGmk0and0_3m10Worst.csv")) 
			System.out.println(false);
		if(!testAndWriteWorstCaseResults(getBetaGenerator(0.3,0.6), "ALGmk0_3and0_6m10Worst.csv")) 
			System.out.println(false);
		if(!testAndWriteWorstCaseResults(getBetaGenerator(0.6,0.8), "ALGmk0_6and0_8m10Worst.csv")) 
			System.out.println(false);
		if(!testAndWriteWorstCaseResults(getBetaGenerator(0.8, 1), "ALGmk0_8and1m10Worst.csv")) 
			System.out.println(false);
	}
	
	public void startMaximumTesting() throws IOException {
		int n = 1000;
		int jobNumber = 100;
		int lengthFactor = 4;
		LinkedList<LinkedList<Job>> inputList = new LinkedList<LinkedList<Job>>();
		for(int i = 0; i < n; i++)
			inputList.add(Service.generateRandomInput(lengthFactor, jobNumber));
		if(!testAndWriteMaximumResults(inputList, getBetaGenerator(0, 0.3), "ALGmk0and0_3.csv"))
			System.out.println(false);
		if(!testAndWriteMaximumResults(inputList, getBetaGenerator(0.3, 0.6), "ALGmk0_3and0_6.csv"))
			System.out.println(false);
		if(!testAndWriteMaximumResults(inputList, getBetaGenerator(0.6, 0.8), "ALGmk0_6and0_8.csv"))
			System.out.println(false);
		if(!testAndWriteMaximumResults(inputList, getBetaGenerator(0.8, 1), "ALGmk0_8and1.csv"))
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
