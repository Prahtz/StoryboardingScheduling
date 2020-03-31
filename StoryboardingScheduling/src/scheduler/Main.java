package scheduler;
import java.io.IOException;

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
		//t1.startWorstCaseTesting();
		//t2.startWorstCaseTesting();
		//cr.start();
		//System.out.println(tm.testCompetitiveRatio());
		//System.out.println(tm.testWorstCase());
		//tm.startTesting();
		//t2.startWorstCaseTesting();
		//tm.startWorstCaseTesting();
		cr.start();
	}
}
