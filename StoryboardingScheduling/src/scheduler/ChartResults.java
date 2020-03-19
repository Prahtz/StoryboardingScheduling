package scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class ChartResults {
	public void start() throws FileNotFoundException {
		String fileName = "ALG2k0_8and1.csv";
		Scanner scanner = new Scanner(new File("csv/" + fileName));
		scanner.useDelimiter(";|\n");
		
		LinkedList<Results> resultsList = new LinkedList<Results>();
		
		while(scanner.hasNext()) {
			Results results = new Results();
			String next = scanner.next();
			if(next.isBlank())
				break;
			
			results.setBeta(Double.parseDouble(next));
			results.setK(Integer.parseInt(scanner.next()));
			results.setC(Double.parseDouble(scanner.next()));
			scanner.next();
			
			next = scanner.next();
			while(!next.isBlank()) {
				results.addOnlineValue(Double.parseDouble(next));
				results.addOfflineValue(Double.parseDouble(scanner.next()));
				next = scanner.next();
				//System.out.println(next);
			}
			addResults(resultsList, results);
		}
		scanner.close();
		
		double[] xData = getArrayOfBetas(resultsList);
	    double[] yData = getArrayOfMinumumDifferences(resultsList);
	 
	    // Create Chart
	    XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
	 
	    // Show it
	    new SwingWrapper(chart).displayChart();
	}

	private void addResults(LinkedList<Results> resultsList, Results results) {
		ListIterator<Results> it = resultsList.listIterator();
		if(!it.hasNext())
			it.add(results);
		else {
			while(it.hasNext()) {
				Results next = it.next();
				if(results.getBeta() < next.getBeta()) {
					it.previous();
					it.add(results);
					break;
				}
				if(!it.hasNext()) 
					it.add(results);
			}
			
		}
	}
	private double[] getArrayOfBetas(LinkedList<Results> resultsList) {
		double[] betas = new double[resultsList.size()];
		Iterator<Results> it = resultsList.iterator();
		int i = 0;
		while(it.hasNext()) {
			betas[i] = it.next().getBeta();
			i++;
		}
		return betas;
	}
	
	private double[] getArrayOfMinumumDifferences(LinkedList<Results> resultsList) {
		double[] minimumDifferences = new double[resultsList.size()];
		Iterator<Results> it = resultsList.iterator();
		int i = 0;
		while(it.hasNext()) {
			Results next = it.next();
			ArrayList<Double> onlineValues = next.getOnlineValues();
			ArrayList<Double> offlineValues = next.getOfflineValues();
			double beta = next.getBeta();
			int k = next.getK();
			double c = next.getC();
			double minimumValue = Double.MAX_VALUE;
			for(int j = 0; j < onlineValues.size(); j++) {
				double difference = c * onlineValues.get(j) - ((1/Math.pow(beta, k-1)) * offlineValues.get(j));
				if(difference < minimumValue)
					minimumValue = difference;
			}
			minimumDifferences[i] = minimumValue;
			System.out.println(minimumValue);
			i++;
		}
		return minimumDifferences;
	}

}
