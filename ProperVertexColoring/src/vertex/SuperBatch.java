package vertex;

public class SuperBatch {
	private static final int NUM_TRIALS = 100;
	private static final int STEPS_PER_TRIAL = 40;
	private static final int PRINT_FREQ = 25;
	private static final int MAX_NUMCHANGE = 4;

	private SuperBatch() { }

	public static void main(String[] args) {
		int n = 20;

		for(int j = 1; j < MAX_NUMCHANGE; j++){
			for(double eP = 0.5; eP < 1; eP+=.1){
				eP = ((double)(Math.round(eP * 10)) / 10);
				int sig = 0; int before = 0; int total = 0; long start = System.currentTimeMillis();
				System.out.println("eP: " + eP + " numChange: " + j);
				for(int y = 0; y < NUM_TRIALS; y++){
					UndirectedGraph<Vertex> g = Main.createGraph(n, eP);
					GreedyColoring.color(g, 3);

					if(Main.getUncoloredCount(g) > 0){
						sig++; before += Main.getUncoloredCount(g);
						Searcher searcher = new N_Changer(g, 3, j);
						for(int x = 0; x < STEPS_PER_TRIAL; x++){searcher.step();}
						//							searcher.complete();

						for(Vertex v : g.getVertices()) {
							if(v.getColor() < 0) total++;
						}
					}

					if(y == NUM_TRIALS - 1 /*|| y % PRINT_FREQ == PRINT_FREQ - 1*/) {
						int done = y + 1;
						long elapse = System.currentTimeMillis() - start;
						System.out.printf("after%4d trials:%6.2f uncolored,%7.2f ms elapsed\n",
								done, (double) total / sig, (double) elapse / sig);
						System.out.println("      sig: " + sig + " before: " + before + " after: " + total + " Percent gain: " + (1.0-(double)total/before));
					}
				}
			}
		}
	}
}
