package problems.qbfpt.solver.multithread;

import java.io.IOException;

import problems.qbfpt.solver.GRASP_QBFPT;
import solutions.Solution;

public class GRASP_QBFPT_multithreaded  extends GRASP_QBFPT implements Runnable{
		public GRASP_QBFPT_multithreaded(Double alpha, Integer iterations, String filename, boolean firstImproving,
			boolean pop, int biasType, double biasExponent) throws IOException {
		super(alpha, iterations, filename, firstImproving, pop, biasType, biasExponent);
	}
	public Solution<Integer> finalSolution;	
	public long totalTime;
	public void run() {
		try {
			long startTime = System.currentTimeMillis();
			this.finalSolution = this.solve();
			this.totalTime = System.currentTimeMillis() - startTime;

		}catch(Exception e){
			System.out.println("Espero nunca ver isso.");
		}
	}
	public void displaySolution() {
		System.out.println("maxVal = " + bestSol);
		System.out.println("Time = "+(double)totalTime/(double)1000+" seg");
	}
	public static void main(String args[]) throws IOException {
		String instances[] = {"instances/qbf020","instances/qbf040","instances/qbf060","instances/qbf080","instances/qbf100","instances/qbf200","instances/qbf400"};
		GRASP_QBFPT_multithreaded.verbose = false;
		for(int i = 0;i<3;i++) {
			System.out.println("Instancia: "+instances[i]);
			
			GRASP_QBFPT_multithreaded graspPadrao = new GRASP_QBFPT_multithreaded(0.05, 100, instances[i], true, false, 0, 0);
			GRASP_QBFPT_multithreaded graspDiffAlpha = new GRASP_QBFPT_multithreaded(0.15, 100, instances[i], true, false, 0, 0);
			GRASP_QBFPT_multithreaded graspBestImprov = new GRASP_QBFPT_multithreaded(0.05, 100, instances[i], false, false, 0, 0);
			GRASP_QBFPT_multithreaded graspPop = new GRASP_QBFPT_multithreaded(0.05, 100, instances[i], true, true, 0, 0);
			GRASP_QBFPT_multithreaded graspExpBias = new GRASP_QBFPT_multithreaded(0.05, 100, instances[i], true, false, 1, 0);
			GRASP_QBFPT_multithreaded graspPolyBias = new GRASP_QBFPT_multithreaded(0.05, 100, instances[i], true, false, 0, -1);
			
			graspPadrao.run();
			graspDiffAlpha.run();
			graspBestImprov.run();
			graspPop.run();
			graspExpBias.run();
			graspPolyBias.run();
			
			System.out.println("Grasp padrao: ");
			graspPadrao.displaySolution();
			System.out.println("");
			System.out.println("Grasp alpha mudado: ");
			graspDiffAlpha.displaySolution();
			System.out.println("");
			System.out.println("Grasp best improving: ");
			graspBestImprov.displaySolution();
			System.out.println("");
			System.out.println("Grasp pop: ");
			graspPop.displaySolution();
			System.out.println("");
			System.out.println("Grasp exp bias: ");
			graspExpBias.displaySolution();
			System.out.println("");
			System.out.println("Grasp poly bias: ");
			graspPolyBias.displaySolution();
			System.out.println("");
			System.out.println("");
		}
	}
}
