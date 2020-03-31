package scheduler;

public class TestChop {
	public TestChop() {}
	public void testStart() {
		int n = 1000;
		int jobNumber = 1000;
		for(int i = 0 ; i < n; i++) {
			double beta = Service.generateBeta();
			int k = Service.generateKAlg1k(beta);
			Chop chop = new Chop(k, beta, Service.generateRandomInput(k, jobNumber));
			System.out.println(chop.start());
		}
	}
}
