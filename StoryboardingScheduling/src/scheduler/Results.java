package scheduler;

import java.util.ArrayList;

public class Results {
	private double beta;
	private int k;
	private double c;
	private ArrayList<Double> onlineValues;
	private ArrayList<Double> offlineValues;
	
	public Results(double beta, int k, double c) {
		this.beta = beta;
		this.k = k;
		this.c = c;
		this.onlineValues = new ArrayList<Double>();
		this.offlineValues = new ArrayList<Double>();
	}
	
	public Results() {
		this.beta = 0;
		this.k = 0;
		this.c = 0;
		this.onlineValues = new ArrayList<Double>();
		this.offlineValues = new ArrayList<Double>();
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}
	
	public void addOnlineValue(Double value) {
		onlineValues.add(value);
	}
	
	public void addOfflineValue(Double value) {
		offlineValues.add(value);
	}
	
	public ArrayList<Double> getOnlineValues() {
		return onlineValues;
	}

	public ArrayList<Double> getOfflineValues() {
		return offlineValues;
	}
	
	@Override
	public String toString() {
		String r = beta + " " + k + " " + c + "\n\n";
		for(int i = 0; i < onlineValues.size(); i++) {
			r = r + onlineValues.get(i) + " " + offlineValues.get(i) + "\n"; 
		}
		return r;
	}
}
