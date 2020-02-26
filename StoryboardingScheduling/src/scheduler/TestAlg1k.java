package scheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class TestAlg1k {

	public TestAlg1k() {
		
	}
	
	public boolean testCompetitiveRatio() {
		int jobNumber = 10000;
		int n = 10000;
		double c = 4;
		for(int i = 0; i < n; i++) {
			double beta = Service.generateBeta();
			int k = Service.generateKAlg1k(beta);
			c = 1/(Math.pow(beta, k-1) * (1 - Math.pow(beta,k)));
			LinkedList<Job> totalJobs = Service.generateRandomInput(k, jobNumber);
			Alg1k alg = new Alg1k(k, beta, totalJobs);
			QuantizedChop chop = new QuantizedChop(k, beta, totalJobs);
			if(c * alg.start() < (1/Math.pow(beta, k-1)) * chop.start())
				return false;
		}
		return true;
	}
	
	public boolean testAndWriteResults2(LinkedList<Job> input, BetaGenerator bg) throws IOException {
		File f = new File("csv/ALG1k.csv");
		f.delete();
		f.createNewFile();
		FileWriter fw = new FileWriter(f);
		int n = 1000;
		double c = 4;
		double algValue = 0;
		double chopValue = 0;
		for(int i = 0; i < n; i++) {
			double beta = bg.generateBeta();
			int k = Service.generateKAlg1k(beta);
			c = 1/(Math.pow(beta, k-1) * (1 - Math.pow(beta,k)));
			Alg1k alg = new Alg1k(k, beta, Service.cloneList(input));
			QuantizedChop chop = new QuantizedChop(k, beta, Service.cloneList(input));
			algValue = alg.start();
			chopValue = chop.start();
			fw.write(c + ";" + algValue + ";" + chopValue + "\n");
			if(c * algValue < (1/Math.pow(beta, k-1)) * chopValue) {
				fw.flush();
				fw.close();
				return false;
			}
		}
		fw.flush();
		fw.close();
		return true;
	}
	
	public boolean testAndWriteResults() throws IOException {
		File f = new File("csv/ALG1k.csv");
		f.delete();
		f.createNewFile();
		FileWriter fw = new FileWriter(f);
		int jobNumber = 1000;
		int n = 1000;
		double c = 4;
		double algValue = 0;
		double chopValue = 0;
		for(int i = 0; i < n; i++) {
			double beta = Service.generateBeta();
			int k = Service.generateKAlg1k(beta);
			c = 1/(Math.pow(beta, k-1) * (1 - Math.pow(beta,k)));
			LinkedList<Job> totalJobs = Service.generateRandomInput(k, jobNumber);
			Alg1k alg = new Alg1k(k, beta, totalJobs);
			QuantizedChop chop = new QuantizedChop(k, beta, Service.cloneList(totalJobs));
			algValue = alg.start();
			chopValue = chop.start();
			fw.write(c + ";" + algValue + ";" + chopValue + "\n");
			if(c * algValue < (1/Math.pow(beta, k-1)) * chopValue) {
				fw.flush();
				fw.close();
				return false;
			}
		}
		fw.flush();
		fw.close();
		return true;
	}
}
