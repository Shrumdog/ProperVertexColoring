package vertex;

import java.util.ArrayList;
import java.util.Random;

public class GreedyColoring {
	/** Executes the complete greedy algorithm on the parameter
	 *  graph. */
	public static void color(UndirectedGraph<Vertex> g, int numColors) {
		GreedyColoring alg = new GreedyColoring(g, numColors);
		while(alg.step());
	}

	/** Colors the vertex with the specified color, disqualifying that color
	 *  from all the vertex's neighbors. */
	public static void colorVertex(UndirectedGraph<Vertex> g, Vertex v,
			int color) {
		v.setColor(color);
		for(Vertex u : g.getNeighbors(v)) {
			u.setPossible(color, false);
		}
	}

	/** Removes any color associated with the vertex, and makes that color
	 *  be a possibility for any of its neighbors (except for neighbors that
	 *  still have neighbors with that color). */
	public static void uncolorVertex(UndirectedGraph<Vertex> g, Vertex v) {
		int oldColor = v.getColor();
		if(oldColor >= 0) {
			v.setColor(-1);
			for(Vertex u : g.getNeighbors(v)) {
				// See if u still has neighbor with oldColor
				boolean stillPossible = true;
				for(Vertex w : g.getNeighbors(u)) {
					if(w.getColor() == oldColor) { stillPossible = false; break; }
				}
				// Otherwise mark u as colorable with oldColor
				if(stillPossible) u.setPossible(oldColor, true);
			}
		}
	}

	private Random rand;
	private UndirectedGraph<Vertex> graph;
	private int bestPossible = Integer.MAX_VALUE;
	private ArrayList<Vertex> bestVertices = new ArrayList<Vertex>();

	/** Initializes the graph before starting the greedy algorithm. */
	public GreedyColoring(UndirectedGraph<Vertex> g, int k) {
		for(Vertex v : g.getVertices()) {
			for(int color = 0; color < k; color++) {
				v.setPossible(color, true);
				v.setColor(-1);
			}
		}

		rand = new Random();
		graph = g;
		bestPossible = k;
		bestVertices.addAll(g.getVertices());
	}

	/** Executes one step of the greedy algorithm: Selects a single vertex
	 *  and gives it a random color, returning false if no vertex was found
	 *  with a valid color. */
	public boolean step() {
		int bestP = bestPossible;
		ArrayList<Vertex> bestVs = bestVertices;

		if(bestVs.isEmpty()) return false;

		// Select a random vertex among those with fewest choices,
		// and select a random choice among its possible colors
		int uPos = rand.nextInt(bestVs.size());
		Vertex u = bestVs.remove(uPos);
		int color = u.choosePossibleColor(rand);

		// color that vertex thus, update neighbors to disqualify color,
		// and insert neigbors into bestP and bestVs
		u.setColor(color);
		for(Vertex v : graph.getNeighbors(u)) {
			if(v.getColor() >= 0) {
				v.setPossible(color, false);
			} else if(v.isPossible(color)) {
				v.setPossible(color, false);
				int k = v.getPossibleCount();
				if(k == 0) {
					bestVs.remove(v);
				} else if(k <= bestP) {
					if(k < bestP) {
						bestP = k;
						bestVs.clear();
					}
					bestVs.add(v);
				}
			}
		}

		// It may be that bestVs is now empty, in which case we need
		// to recompute it.
		if(bestVs.isEmpty()) {
			bestP = Integer.MAX_VALUE;
			for(Vertex v : graph.getVertices()) {
				if(v.getColor() < 0) {
					int k = v.getPossibleCount();
					if(k <= bestP && k > 0) {
						if(k < bestP) {
							bestP = k;
							bestVs.clear();
						}
						bestVs.add(v);
					}
				}
			}
		}

		bestPossible = bestP;
		return true;
	}
}
