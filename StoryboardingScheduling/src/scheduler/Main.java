package scheduler;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class Main {

	public static void main(String[] args) throws IOException {
		// testAlg1kScheduling();
		TestAlg1k t1 = new TestAlg1k();
		TestChop tc = new TestChop();
		TestAlg2k t2 = new TestAlg2k();
		TestAlgmk tm = new TestAlgmk();
		ChartResults cr = new ChartResults();
		//System.out.println(t1.testSorting());
		//System.out.println(t1.testScheduleActiveJob());
		//System.out.println(t1.testProcessPhase());
		//System.out.println(tm.testProcessPhase());
		//System.out.println(t2.testCompetitiveRatio());
		//tc.testStart();
		//System.out.println(t1.testAndWriteResults());
		//t1.startTesting();
		//t2.testWorstCase();
		//t2.startTesting();
		cr.start();
		/*
	    double[] xData = new double[] { 0.0, 1.0, 2.0 };
	    double[] yData = new double[] { 2.0, 1.0, 0.0 };
	 
	    // Create Chart
	    XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
	 
	    // Show it
	    new SwingWrapper(chart).displayChart();*/
		
	}
}
