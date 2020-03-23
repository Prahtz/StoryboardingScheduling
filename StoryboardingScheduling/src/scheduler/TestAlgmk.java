package scheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class TestAlgmk {
	public boolean testCompetitiveRatio() {
		int jobNumber = 10000;
		int n = 10000;
		int m = 2;
		double c = 4;
		for(int i = 0; i < n; i++) {
			double beta = Service.generateBeta();
			int k = Service.generateKAlg1k(beta);
			c = 1/(Math.pow(beta, k-1) * (1 + 1/(1 - 1/Math.pow(beta,k))));
			LinkedList<Job> totalJobs = Service.generateRandomInput(k, jobNumber);
			Algmk alg = new Algmk(k, m, beta, Service.cloneList(totalJobs));
			OptStar opt = new OptStar(k, m, beta, totalJobs);
			if(c * alg.start() < /*(1/Math.pow(beta, k-1)) **/opt.start())
				return false;
		}
		return true;
	}
	
	public boolean testWorstCase() {	
		double beta = 0.3;
		int k = Service.generateKAlgmk(beta);
		int m = 10000;
		double c = (1/(Math.pow(beta, k-1)) * (1 + 1/(1 - Math.pow(beta,k))));
		int max = 1;
		int maxL = 10000;
		LinkedList<Job> input = new LinkedList<Job>();
		for(int i = 0; i < 2*m; i++) 
			input.add(new Job(i,0,max,maxL));
		Algmk alg = new Algmk(k, m, beta, Service.cloneList(input));
		OptStar opt = new OptStar(k, m, beta, input);
		
		double a = alg.start();
		double o = opt.start();
		
		System.out.println(k + " " + a + " " + o +" " + c);
		System.out.println(c * a >= 1/(Math.pow(beta, k-1)) * o);
		System.out.println(c * a + " " + 1/(Math.pow(beta, k-1)) * o);
		return c * a >= 1/(Math.pow(beta, k-1)) * o;
	}
	
	public boolean testAndWriteResults(LinkedList<LinkedList<Job>> inputList, BetaGenerator bg, String fileName) throws IOException {
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
	
	public void startTesting() throws IOException {
		int n = 1000;
		int jobNumber = 100;
		int lengthFactor = 4;
		LinkedList<LinkedList<Job>> inputList = new LinkedList<LinkedList<Job>>();
		for(int i = 0; i < n; i++)
			inputList.add(Service.generateRandomInput(lengthFactor, jobNumber));
		if(!testAndWriteResults(inputList, getBetaGenerator(0, 0.3), "ALGmk0and0_3.csv"))
			System.out.println(false);
		if(!testAndWriteResults(inputList, getBetaGenerator(0.3, 0.6), "ALGmk0_3and0_6.csv"))
			System.out.println(false);
		if(!testAndWriteResults(inputList, getBetaGenerator(0.6, 0.8), "ALGmk0_6and0_8.csv"))
			System.out.println(false);
		if(!testAndWriteResults(inputList, getBetaGenerator(0.8, 1), "ALGmk0_8and1.csv"))
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
