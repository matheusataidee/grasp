package problems.qbfpt.solver.multithread;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import problems.qbfpt.solver.GRASP_QBFPT;
import solutions.Solution;

public class GRASP_QBFPT_multithreaded  extends GRASP_QBFPT implements Runnable{
		public GRASP_QBFPT_multithreaded(Double alpha, Integer iterations, String filename, boolean firstImproving,
			boolean pop, int biasType, double biasExponent) throws IOException {
		super(alpha, iterations, filename, firstImproving, pop, biasType, biasExponent);
		this.done = false;
	}
	public Solution<Integer> finalSolution;	
	public long totalTime;
	public Boolean done;
	public void run() {
		try {
			long startTime = System.currentTimeMillis();
			this.finalSolution = this.solve();
			this.totalTime = System.currentTimeMillis() - startTime;
			this.done = true;
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
		int maxIter = 50000;
		for(int i = 4;i<5;i++) {
			System.out.println("Instancia: "+instances[i]);
			
			GRASP_QBFPT_multithreaded graspPadrao = new GRASP_QBFPT_multithreaded(0.05, maxIter, instances[i], true, false, 0, 0);
			GRASP_QBFPT_multithreaded graspDiffAlpha = new GRASP_QBFPT_multithreaded(0.15, maxIter, instances[i], true, false, 0, 0);
			GRASP_QBFPT_multithreaded graspBestImprov = new GRASP_QBFPT_multithreaded(0.05, maxIter, instances[i], false, false, 0, 0);
			GRASP_QBFPT_multithreaded graspPop = new GRASP_QBFPT_multithreaded(0.05, maxIter, instances[i], true, true, 0, 0);
			GRASP_QBFPT_multithreaded graspExpBias = new GRASP_QBFPT_multithreaded(0.05, maxIter, instances[i], true, false, 1, 0);
			GRASP_QBFPT_multithreaded graspPolyBias = new GRASP_QBFPT_multithreaded(0.05, maxIter, instances[i], true, false, 0, -0.5);
			
			Thread graspPadraoThread = new Thread(graspPadrao);
			Thread graspDiffAlphaThread = new Thread(graspDiffAlpha);
			Thread graspBestImprovThread = new Thread(graspBestImprov);
			Thread graspPopThread = new Thread(graspPop);
			Thread graspExpBiasThread = new Thread(graspExpBias);
			Thread graspPolyBiasThread = new Thread(graspPolyBias);
			
			graspPadraoThread.start();
			graspDiffAlphaThread.start();
			graspBestImprovThread.start();
			graspPopThread.start();
			graspExpBiasThread.start();
			graspPolyBiasThread.start();
			
			try {
				TimeUnit.MILLISECONDS.sleep(maxIter);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
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
