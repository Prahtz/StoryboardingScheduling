package scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.Rectangle;

public class ChartResults {
	public void start() throws IOException {
		Stream<Path> walk = Files.walk(Paths.get("csv"));
		List<String> files = walk.filter(Files::isRegularFile)
				.map(x ->(x.toString().substring(4))).collect(Collectors.toList());
		walk.close();
		
		Iterator<String> it = files.iterator();
		while(it.hasNext()) {
			String fileName = it.next();
			LinkedList<Results> resultsList = getResultsList(fileName);
			double[] xData = getArrayOfBetas(resultsList);
		    fileName = fileName.substring(0, fileName.length() - 4);
		    
		    XYChart chart = new XYChartBuilder().width(800).height(600).title(fileName).xAxisTitle("Betas").yAxisTitle("Ratios").build();
		    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
		    chart.getStyler().setMarkerSize(0);
		    chart.addSeries("Maximum Ratios", xData, getArrayOfMaximumRatios(resultsList));
		    chart.addSeries("Competitive Ratios", xData, getArrayOfCompetitiveRatios(resultsList));
		    BitmapEncoder.saveBitmap(chart, "png/max/" + fileName, BitmapFormat.PNG);
		    
		    chart = new XYChartBuilder().width(800).height(600).title(fileName).xAxisTitle("Betas").yAxisTitle("Ratios").build();
		    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
		    chart.getStyler().setMarkerSize(0);
		    chart.addSeries("Average Ratios", xData, getArrayOfAverageRatios(resultsList));
		    BitmapEncoder.saveBitmap(chart, "png/avg/" + fileName, BitmapFormat.PNG);
		}
	}
	
	private LinkedList<Results> getResultsList(String fileName) throws FileNotFoundException {
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
			}
			addResults(resultsList, results);
		}
		scanner.close();
		return resultsList;
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
	
	private double[] getArrayOfMinumumRatios(LinkedList<Results> resultsList) {
		double[] minimumRatios = new double[resultsList.size()];
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
				double ratio = c * onlineValues.get(j) / ((1/Math.pow(beta, k-1)) * offlineValues.get(j));
				if(ratio < minimumValue)
					minimumValue = ratio;
			}
			minimumRatios[i] = minimumValue;
			i++;
		}
		return minimumRatios;
	}
	
	private double[] getArrayOfMaximumRatios(LinkedList<Results> resultsList) {
		double[] maximumRatios = new double[resultsList.size()];
		Iterator<Results> it = resultsList.iterator();
		int i = 0;
		while(it.hasNext()) {
			Results next = it.next();
			ArrayList<Double> onlineValues = next.getOnlineValues();
			ArrayList<Double> offlineValues = next.getOfflineValues();
			double beta = next.getBeta();
			int k = next.getK();
			double c = next.getC();
			double maximumValue = 0;
			for(int j = 0; j < onlineValues.size(); j++) {
				double ratio = ((1/Math.pow(beta, k-1)) * offlineValues.get(j)) / onlineValues.get(j);
				if(ratio > maximumValue)
					maximumValue = ratio;
			}
			maximumRatios[i] = maximumValue;
			i++;
		}
		return maximumRatios;
	}
	
	private double[] getArrayOfAverageRatios(LinkedList<Results> resultsList) {
		double[] averageRatios = new double[resultsList.size()];
		Iterator<Results> it = resultsList.iterator();
		int i = 0;
		while(it.hasNext()) {
			Results next = it.next();
			ArrayList<Double> onlineValues = next.getOnlineValues();
			ArrayList<Double> offlineValues = next.getOfflineValues();
			double beta = next.getBeta();
			int k = next.getK();
			double c = next.getC();
			double averageValue = 0;
			for(int j = 0; j < onlineValues.size(); j++) 
				//averageValue = averageValue + c * onlineValues.get(j) / ((1/Math.pow(beta, k-1)) * offlineValues.get(j));
				averageValue = averageValue + (((1/Math.pow(beta, k-1)) * offlineValues.get(j)) / onlineValues.get(j));
			averageRatios[i] = averageValue / onlineValues.size();
			i++;
		}
		return averageRatios;
	}
	
	private double[] getArrayOfCompetitiveRatios(LinkedList<Results> resultsList) {
		double[] cRatios = new double[resultsList.size()];
		Iterator<Results> it = resultsList.iterator();
		int i = 0;
		while(it.hasNext()) {
			cRatios[i] = it.next().getC();
			i++;
		}
		return cRatios;
	}
	
}
