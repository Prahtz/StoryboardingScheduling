package scheduler;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class Service {
	
	private static Random random = new Random();
	
	public static LinkedList<Job> generateRandomInput(int k, int jobNumber) {
		LinkedList<Job> list = new LinkedList<Job>();
		int lengthFactor;
		for (int i = 0; i < jobNumber; i++) {
			lengthFactor =  random.nextInt(3) + 1;
			list.add(new Job(i, random.nextInt(jobNumber) + 1, random.nextInt(jobNumber) + 1, random.nextInt(lengthFactor * k) + 1));
		}
		return list;
	}
	
	public static double generateBeta() {
		double beta;
		do
			beta = random.nextDouble();
		while (beta == 0);
		return beta;
	}
	
	public static int generateK(double beta) {
		int k;
		if (beta <= (2 / 3))
			k = 1;
		else
			k = (int) Math.floor(-Math.log(2) / Math.log(beta)) + 1;
		return k;
	}
	
	public static void printJobList(LinkedList<Job> list) {
		for(int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
		System.out.println();
	}
	
	public static LinkedList<Job> cloneList(LinkedList<Job> l) {
		LinkedList<Job> r = new LinkedList<Job>();
		ListIterator<Job> it = l.listIterator();
		while (it.hasNext()) {
			Job j = it.next();
			r.add(new Job(j));
		}
		return r;
	}
}


