package vertex;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
	private Main() { }

	private static class Listener extends WindowAdapter
			implements MouseListener {
		private GraphCanvas canvas;
		private Searcher searcher;

		Listener(GraphCanvas c, Searcher s) { canvas = c; searcher = s; }

		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) { }
		public void mouseClicked(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) { }
		public void mousePressed(MouseEvent e) {
			int count = 1;
			if(e.getButton() > 1) count = 5;
			for(int i = 0; i < count; i++) searcher.step();
			printUncoloredCount(canvas.getGraph());
			canvas.repaint();
		}

		public void windowClosing(WindowEvent e) {
			searcher.complete();
			printUncoloredCount(canvas.getGraph());
		}
	}

	public static void main(String[] args) {
		int n = determineVertices(args);
		double p = determineProbability(args);

		UndirectedGraph<Vertex> g = createGraph(n, p);
		GreedyColoring.color(g, 3);
		printUncoloredCount(g);
		Searcher searcher = new Searcher(g, 3);

		GraphCanvas canvas = GraphCanvas.showWindow(g);
		Listener l = new Listener(canvas, searcher);
		canvas.addMouseListener(l);
		canvas.getWindow().addWindowListener(l);
	}

	public static int determineVertices(String[] args) {
		if(args.length == 0) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				while(true) {
					System.out.print("Number of vertices? ");
					String line = in.readLine();
					if(line == null) System.exit(0);
					try {
						int n = Integer.parseInt(line);
						if(n < 0) {
							System.out.println("Number must be positive");
						} else if(n > 400) {
							System.out.println("Number cannot exceed 400");
						} else {
							return n;
						}
					} catch(NumberFormatException e) {
						System.out.println("Must enter a positive integer");
					}
				}
			} catch(IOException e) {
				System.err.println("Fatal error initializing terminal");
			}
		} else if(args.length == 1 || args.length == 2) {
			try {
				int n = Integer.parseInt(args[0]);
				if(n < 0) {
					System.err.println("Argument must be positive");
				} else if(n > 400) {
					System.err.println("Argument cannot exceed 400");
				} else {
					return n;
				}
			} catch(NumberFormatException e) {
				System.err.println("Argument must be an integer");
			}
		} else {
			System.err.println("usage: java Main [NUM_VERTICES] [EDGE_PROBABILITY]");
		}
		System.exit(0);
		return 0;
	}

	public static double determineProbability(String[] args) {
		if(args.length == 0) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				while(true) {
					System.out.print("Edge probability? ");
					String line = in.readLine();
					if(line == null) System.exit(0);
					try {
						double p = Double.parseDouble(line);
						if(p < 0 || p > 1) {
							System.out.println("Must be between 0 and 1");
						} else {
							return p;
						}
					} catch(NumberFormatException e) {
						System.out.println("Must enter a number between 0 and 1");
					}
				}
			} catch(IOException e) {
				System.err.println("Fatal error initializing terminal");
			}
		} else if(args.length == 1) {
			return 0.75;
		} else if(args.length == 2) {
			try {
				double p = Double.parseDouble(args[1]);
				if(p >= 0 && p <= 1) return p;
			} catch(NumberFormatException e) { }
			System.err.println("Edge probability must be between 0 and 1");
		}
		System.exit(0);
		return 0;
	}


	public static UndirectedGraph<Vertex> createGraph(int n, double p) {
		int rows = (int) Math.sqrt(n);
		int cols = (int) Math.ceil((double) n / rows);
		UndirectedGraph<Vertex> g = new UndirectedGraph<Vertex>();
		Random rands = new Random();
		for(int i = 0; i < n; i++) {
			g.addVertex(new Vertex(i / cols, i % cols));
		}
		int[] offs = { 1, cols - 1, cols, cols + 1 };
		for(int u = 0; u < n; u++) {
			for(int j = 0; j < offs.length; j++) {
				int v = u + offs[j];
				if(v >= 0 && v < n && rands.nextDouble() < p) {
					Vertex uu = g.getVertex(u);
					Vertex vv = g.getVertex(v);
					if(Math.abs(uu.getColumn() - vv.getColumn()) <= 1) {
						g.addEdge(uu, vv);
					}
				}
			}
		}
		return g;
	}

	public static void printUncoloredCount(UndirectedGraph<Vertex> g) {
		int count = 0;
		for(Vertex v : g.getVertices()) {
			if(v.getColor() < 0) count++;
		}
		System.out.println("uncolored vertices: " + count);
	}
}
