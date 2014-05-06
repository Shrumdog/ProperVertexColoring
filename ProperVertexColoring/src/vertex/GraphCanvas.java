package vertex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class GraphCanvas extends JComponent {
	public static GraphCanvas showWindow(UndirectedGraph<Vertex> g) {
		JFrame frame = new JFrame();
		GraphCanvas ret = new GraphCanvas(frame, g);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(ret);
		frame.pack();
		frame.setVisible(true);
		return ret;
	}

	private static final int VERTEX_RADIUS = 10;
	private static final int VERTEX_DIST = 40;
	private static final int BORDER = 20;

	private JFrame frame;
	private UndirectedGraph<Vertex> graph;

	public GraphCanvas(JFrame f, UndirectedGraph<Vertex> g) {
		frame = f;
		graph = g;
		int n = g.getVertexCount();
		int rows = (int) Math.sqrt(n);
		int cols = (int) Math.ceil((double) n / rows);
		setPreferredSize(new Dimension((cols - 1) * VERTEX_DIST + 2 * BORDER,
			(rows - 1) * VERTEX_DIST + 2 * BORDER));
	}

	public JFrame getWindow() {
		return frame;
	}

	public UndirectedGraph<Vertex> getGraph() {
		return graph;
	}

	public void setGraph(UndirectedGraph<Vertex> value) {
		graph = value;
		repaint();
	}

	public void paintComponent(Graphics g) {
		int n = graph.getVertexCount();
		for(Vertex u : graph.getVertices()) {
			int ux = BORDER + u.getColumn() * VERTEX_DIST;
			int uy = BORDER + u.getRow() * VERTEX_DIST;
			for(Vertex v : graph.getNeighbors(u)) {
				int vx = BORDER + v.getColumn() * VERTEX_DIST;
				int vy = BORDER + v.getRow() * VERTEX_DIST;
				g.drawLine(ux, uy, vx, vy);
			}
		}
		for(Vertex u : graph.getVertices()) {
			int ux = BORDER + u.getColumn() * VERTEX_DIST - VERTEX_RADIUS;
			int uy = BORDER + u.getRow() * VERTEX_DIST - VERTEX_RADIUS;
			switch(u.getColor()) {
			case 0:  g.setColor(Color.BLUE); break;
			case 1:  g.setColor(Color.RED); break;
			case 2:  g.setColor(Color.GREEN); break;
			default: g.setColor(Color.WHITE);
			}
			g.fillOval(ux, uy, 2 * VERTEX_RADIUS, 2 * VERTEX_RADIUS);
			g.setColor(Color.BLACK);
			g.drawOval(ux, uy, 2 * VERTEX_RADIUS, 2 * VERTEX_RADIUS);
		}
	}
}
