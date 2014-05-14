package vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class N_Changer extends Searcher{
	/*this solution is designed to take in a graph g as input
	 *the returned graph should be the local optimal solution that is found by n_change
	 *in order to reach a solution, a set of vertices that can be recolored is constructed
	 *a set of vertices with cardinality equal to n are changed to another color that is available to them
	 *when all possibilities are exhausted, the optimal local solution is returned
	 */
	Random rand;
	int numChange = -1; long start;
	ArrayList<HashSet<Vertex>> disallowed;
	ArrayList<UndirectedGraph<Vertex>> graphs;
	ArrayList<Vertex> currentChanges;
	boolean outputDepth;

	public N_Changer(UndirectedGraph<Vertex> g, int k) {
		super(g, k);

		outputDepth = false;
		numChange = takeAndHandleInput();
	}

	public N_Changer(UndirectedGraph<Vertex> g, int k, int j) {
		super(g, k);
		rand = new Random();

		numChange = j;
	}

	private int takeAndHandleInput(){
		int ret = -1;
		Scanner reader = new Scanner(System.in);
		while(ret == -1){
			System.out.print("Please enter the desired (positive) number of changes per step: ");
			int n = reader.nextInt();
			System.out.println();
			if(n <= 0){
				System.out.print("That number is not allowed");
			} else {
				ret = n;
			}
		}
		return ret;
	}

	private void initHistoricalControl(){
		initDisallowed();
		initGraphHistory();
		initCurrentChanges();
	}

	private void initDisallowed(){
		if(numChange > 1){
			disallowed = new ArrayList<HashSet<Vertex>>();
			HashSet<Vertex> clean = new HashSet<Vertex>();
			clean.add(new Vertex(graph.numRows(), graph.numCols()));
			for(int x = 0; x < numChange; x++){
				HashSet<Vertex> clone = new HashSet<Vertex>(clean);
				disallowed.add(clone);
				disallowed.get(x);
			}
		} else {

		}
	}

	private void initGraphHistory(){
		this.graphs = new ArrayList<UndirectedGraph<Vertex>>();
		for(int x = 0; x < numChange; x++){
			UndirectedGraph<Vertex> g = new UndirectedGraph<Vertex>(this.graph);
			graphs.add(x, g);
		}
	}

	private void initCurrentChanges(){
		this.currentChanges = new ArrayList<Vertex>();
		for(int x = 0; x < numChange; x++){
			this.currentChanges.add(null);
		}
	}

	private Vertex findVertex(UndirectedGraph<Vertex> g, HashSet<Vertex> dis){
		Vertex pos = null;
		for(int x = g.numRows()-1; x > -1; x--){
			for(int y = g.numCols()-1; y > -1; y--){
				pos = g.getVertex(x, y);
				if(pos.getPossibleCount() > 0 && !dis.contains(pos)){
					return pos;
				}					
			}
		} return null;
	}

	private int pushChange(UndirectedGraph<Vertex> g, Vertex v){
		if(v == null || v.getPossibleCount() == 0) return -1;

		int oldColor = v.getColor();
		int newColor = v.choosePossibleColor(rand);

		GreedyColoring.uncolorVertex(g, v);
		GreedyColoring.colorVertex(g, v, newColor);

		return oldColor;
	}

	private UndirectedGraph<Vertex> multiStep(){
		initHistoricalControl();
		UndirectedGraph<Vertex> forReturn = graph.clone();
		UndirectedGraph<Vertex> forModification = null;
		rand = new Random();

		int x = 0, curColor;
		while(x < numChange){
			Vertex toChange;
			HashSet<Vertex> dis = new HashSet<Vertex>(disallowed.get(x));
			dis.addAll(currentChanges);
			if(forModification == null){forModification = graphs.get(x).clone();}
			toChange = findVertex(forModification, dis);

			if(toChange != null){
				disallowed.get(x).add(toChange);
				currentChanges.set(x, toChange);

				curColor = pushChange(forModification, toChange);

				if(x == numChange - 1){
					if(Main.getUncoloredCount(forModification) < Main.getUncoloredCount(forReturn)){
						forReturn = forModification;
					}
				} else {
					graphs.set(x, forModification);
					x++;
				}
			} else {
				if (x == 0) break;
				else{
					forModification = null;
					curColor = -1;
					currentChanges.set(x, null);
					disallowed.set(x, new HashSet<Vertex>(disallowed.get(x - 1)));
					x--;
				}
			}

		}
		return forReturn;
	}

	private UndirectedGraph<Vertex> singleStep(){
		UndirectedGraph<Vertex> check = new UndirectedGraph<Vertex>(graph);
		HashSet<Vertex> dis = new HashSet<Vertex>();
		Vertex toChange = findVertex(graph, dis);

		while(toChange != null){
			UndirectedGraph<Vertex> d = new UndirectedGraph<Vertex>(check);
			pushChange(d, toChange);
			outputDepth = false;
			if(Main.getUncoloredCount(d) < Main.getUncoloredCount(check)){
				System.out.println("Changed graph for return");
				check = d;
			}
			dis.add(toChange);
			toChange = findVertex(graph, dis);
		}

		return check;
	}

	public UndirectedGraph<Vertex> step() {
		UndirectedGraph<Vertex> dur;
		if(numChange == 1){
			dur = singleStep();
			return dur;
		} else {
			dur = multiStep();
			return dur;
		}

	}

}


