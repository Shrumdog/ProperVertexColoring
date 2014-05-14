package vertex;

public abstract class Searcher {
	/*reads in a graph POST GREEDY COLORING
	 *one step in SEARCHER is to apply one step of an optimization algorithm (or two or so)
	 */
	protected UndirectedGraph<Vertex> graph;
	protected int numColors;

	public Searcher(UndirectedGraph<Vertex> g, int k) {
		graph = g;
		numColors = k;
	}

	public abstract UndirectedGraph<Vertex> step();

	public void complete() {
		System.out.println("Search Complete");
	}
}
