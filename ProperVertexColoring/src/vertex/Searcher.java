package vertex;

import java.util.ArrayList;
import java.util.Random;

public class Searcher {
	private UndirectedGraph<Vertex> graph;
	private int numColors;

	public Searcher(UndirectedGraph<Vertex> g, int k) {
		graph = g;
		numColors = k;
	}

	public void step() {
		// Note: GreedyColoring provides colorVertex and uncolorVertex
		// methods, which are nice subroutines for you to use.
	}

	public void complete() {
	}
}
