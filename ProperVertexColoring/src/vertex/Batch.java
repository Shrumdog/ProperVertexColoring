package vertex;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Batch {
	private static final int NUM_TRIALS = 400;
	private static final int PRINT_FREQ = 10;

	private Batch() { }

	public static void main(String[] args) {
		int n = Main.determineVertices(args);
		double p = Main.determineProbability(args);

		int total = 0;
		long start = System.currentTimeMillis();
		for(int i = 0; i < NUM_TRIALS; i++) {
			UndirectedGraph<Vertex> g = Main.createGraph(n, p);
			GreedyColoring.color(g, 3);
			Searcher searcher = new N_Changer(g, 3);
			for(int j = 0; j < 40; j++) searcher.step();
			searcher.complete();
			for(Vertex v : g.getVertices()) {
				if(v.getColor() < 0) total++;
			}

			if(i == NUM_TRIALS - 1 || i % PRINT_FREQ == PRINT_FREQ - 1) {
				int done = i + 1;
				long elapse = System.currentTimeMillis() - start;
				System.out.printf("after%4d trials:%6.2f uncolored,%7.2f ms elapsed\n",
					done, (double) total / done, (double) elapse / done);
			}
		}
	}
}
