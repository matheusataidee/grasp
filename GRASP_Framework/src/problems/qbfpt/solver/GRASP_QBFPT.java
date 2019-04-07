package problems.qbfpt.solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import metaheuristics.grasp.AbstractGRASP;
import problems.qbfpt.QBFPT_Inverse;
import solutions.Solution;



/**
 * Metaheuristic GRASP (Greedy Randomized Adaptive Search Procedure) for
 * obtaining an optimal solution to a QBF (Quadractive Binary Function --
 * {@link #QuadracticBinaryFunction}). Since by default this GRASP considers
 * minimization problems, an inverse QBF function is adopted.
 * 
 * @author ccavellucci, fusberti
 */
public class GRASP_QBFPT extends AbstractGRASP<Integer> {

	boolean firstImproving;
	
	/**
	 * Constructor for the GRASP_QBF class. An inverse QBF objective function is
	 * passed as argument for the superclass constructor.
	 * 
	 * @param alpha
	 *            The GRASP greediness-randomness parameter (within the range
	 *            [0,1])
	 * @param iterations
	 *            The number of iterations which the GRASP will be executed.
	 * @param filename
	 *            Name of the file for which the objective function parameters
	 *            should be read.
	 * @throws IOException
	 *             necessary for I/O operations.
	 */
	public GRASP_QBFPT(Double alpha, Integer iterations, String filename, boolean firstImproving) throws IOException {
		super(new QBFPT_Inverse(filename), alpha, iterations);
		this.firstImproving = firstImproving;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see grasp.abstracts.AbstractGRASP#makeCL()
	 */
	@Override
	public ArrayList<Integer> makeCL() {

		ArrayList<Integer> _CL = new ArrayList<Integer>();
		for (int i = 0; i < ObjFunction.getDomainSize(); i++) {
			Integer cand = new Integer(i);
			_CL.add(cand);
		}

		return _CL;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see grasp.abstracts.AbstractGRASP#makeRCL()
	 */
	@Override
	public ArrayList<Integer> makeRCL() {

		ArrayList<Integer> _RCL = new ArrayList<Integer>();

		return _RCL;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see grasp.abstracts.AbstractGRASP#updateCL()
	 */
	@Override
	public void updateCL() {

		// do nothing since all elements off the solution are viable candidates.
		this.CL = this.ObjFunction.GetCL(this.incumbentSol);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This createEmptySol instantiates an empty solution and it attributes a
	 * zero cost, since it is known that a QBF solution with all variables set
	 * to zero has also zero cost.
	 */
	@Override
	public Solution<Integer> createEmptySol() {
		Solution<Integer> sol = new Solution<Integer>();
		sol.cost = 0.0;
		return sol;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * The local search operator developed for the QBF objective function is
	 * composed by the neighborhood moves Insertion, Removal and 2-Exchange.
	 */
	@Override
	public Solution<Integer> localSearch() {

		Double minDeltaCost;
		Integer bestCandIn = null, bestCandOut = null;

		do {
			minDeltaCost = Double.POSITIVE_INFINITY;
			updateCL();
			
			ArrayList<Integer> typeOrder = new ArrayList<Integer>();
			typeOrder.add(0); typeOrder.add(1); typeOrder.add(2);
			
			Collections.shuffle(typeOrder);
			Collections.shuffle(CL);
			Collections.shuffle(incumbentSol);
			
			for (int type : typeOrder)
			{
				if (type == 0)
				{
					// Evaluate insertions
					for (Integer candIn : CL) {
						double deltaCost = ObjFunction.evaluateInsertionCost(candIn, incumbentSol);
						if (deltaCost < minDeltaCost) {
							minDeltaCost = deltaCost;
							bestCandIn = candIn;
							bestCandOut = null;
						}
					}
				}
				else if (type == 1)
				{
					// Evaluate removals
					for (Integer candOut : incumbentSol) {
						double deltaCost = ObjFunction.evaluateRemovalCost(candOut, incumbentSol);
						if (deltaCost < minDeltaCost) {
							minDeltaCost = deltaCost;
							bestCandIn = null;
							bestCandOut = candOut;
						}
					}					
				}
				else
				{
					// Evaluate exchanges
					for (Integer candIn : CL) {
						for (Integer candOut : incumbentSol) {
							double deltaCost = ObjFunction.evaluateExchangeCost(candIn, candOut, incumbentSol);
							if (deltaCost < minDeltaCost) {
								minDeltaCost = deltaCost;
								bestCandIn = candIn;
								bestCandOut = candOut;
							}
						}
					}	
				}
				
				// If in first improving mode and found an option reducing cost, stop searching.
				if (this.firstImproving && minDeltaCost < -Double.MIN_VALUE)
				{
					break;
				}
			}
			
			// Implement the best move, if it reduces the solution cost.
			if (minDeltaCost < -Double.MIN_VALUE) {
				if (bestCandOut != null) {
					incumbentSol.remove(bestCandOut);
					CL.add(bestCandOut);
				}
				if (bestCandIn != null) {
					incumbentSol.add(bestCandIn);
					CL.remove(bestCandIn);
				}
				ObjFunction.evaluate(incumbentSol);
			}
		} while (minDeltaCost < -Double.MIN_VALUE);

		return null;
	}

	/**
	 * A main method used for testing the GRASP metaheuristic.
	 * 
	 */
	public static void main(String[] args) throws IOException {

		long startTime = System.currentTimeMillis();
		GRASP_QBFPT grasp = new GRASP_QBFPT(0.05, 1000, "instances/qbf020", true);
		Solution<Integer> bestSol = grasp.solve();
		System.out.println("maxVal = " + bestSol);
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time = "+(double)totalTime/(double)1000+" seg");

	}
}


