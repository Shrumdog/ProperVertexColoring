package vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;

public class UndirectedGraph<V> {
	private ArrayList<V> vertices;
	private List<V> verticesView;
	private HashMap<V,LinkedHashSet<V>> neighbors;
	private HashMap<V,Set<V>> neighborsView;
	private int edgeCount, numRows, numCols;

	public UndirectedGraph(int rows, int cols) {
		this.numRows = rows; this.numCols = cols;
		vertices = new ArrayList<V>();
		verticesView = Collections.unmodifiableList(vertices);
		neighbors = new HashMap<V,LinkedHashSet<V>>();
		neighborsView = new HashMap<V,Set<V>>();
	}
	
	public UndirectedGraph(UndirectedGraph<V> g){
		this.numRows = g.numRows; this.numCols = g.numCols;
		this.vertices = new ArrayList<V>(g.vertices);
		this.verticesView = Collections.unmodifiableList(this.vertices);
		this.neighbors = new HashMap<V, LinkedHashSet<V>>(g.neighbors);
		this.neighborsView = new HashMap<V, Set<V>>(g.neighborsView);
	}
	
	public UndirectedGraph<V> clone(){
		UndirectedGraph<V> g = new UndirectedGraph<V>(this);
		return g;
	}
	
//	private void cloneHashMaps(UndirectedGraph<V> other){
//		Set<V> keys = this.neighbors.keySet();
//		for(V v : keys){
//			Object[] elements = new Object[neighbors.get(v).size()];
//			elements = neighbors.get(v).toArray(elements);
//			for(Object obj : elements){
//				
//			}
//		}
//		
//	}
		
	public int numRows(){
		return numRows;
	}
	
	public int numCols(){
		return numCols;
	}

	public int getVertexCount() {
		return vertices.size();
	}
	
	public V getVertex(int row, int col){
		int index = ((row * numCols) + col);
		if (index < vertices.size()){
			return vertices.get(index);
		} return vertices.get(vertices.size() - 1);
		
	}

	public V getVertex(int index) {
		return vertices.get(index);
	}

	public List<V> getVertices() {
		return verticesView;
	}

	public void addVertex(V vid) {
		vertices.add(vid);
		LinkedHashSet<V> vneighb = new LinkedHashSet<V>();
		neighbors.put(vid, vneighb);
		neighborsView.put(vid, Collections.unmodifiableSet(vneighb));
	}

	public void removeVertex(V vid) {
		if(vertices.remove(vid)) {
			LinkedHashSet<V> vneighb = neighbors.remove(vid);
			for(V uid : vneighb) {
				LinkedHashSet<V> uneighb = neighbors.get(uid);
				uneighb.remove(vid);
				--edgeCount;
			}
			neighborsView.remove(vid);
		}
	}

	public int getEdgeCount() {
		return edgeCount;
	}

	public int getNeighborCount(V vid) {
		LinkedHashSet<V> vneighb = neighbors.get(vid);
		return vneighb == null ? 0 : vneighb.size();
	}

	public Set<V> getNeighbors(V vid) {
		return neighborsView.get(vid);
	}

	public boolean isConnected(V uid, V vid) {
		LinkedHashSet<V> uneighb = neighbors.get(uid);
		return uneighb != null && uneighb.contains(vid);
	}

	public void addEdge(V uid, V vid) {
		LinkedHashSet<V> uneighb = neighbors.get(uid);
		LinkedHashSet<V> vneighb = neighbors.get(vid);
		if(uneighb != null && vneighb != null
				&& uneighb.add(vid) && vneighb.add(uid)) {
			++edgeCount;
		}
	}

	public void removeEdge(V uid, V vid) {
		LinkedHashSet<V> uneighb = neighbors.get(uid);
		LinkedHashSet<V> vneighb = neighbors.get(vid);
		if(uneighb != null && vneighb != null
				&& uneighb.remove(vid) && vneighb.remove(uid)) {
			--edgeCount;
		}
	}
	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof UndirectedGraph<?>)){return false;}
		else {return this.equals((UndirectedGraph<V>) other);}
	}
	
	private boolean equals(UndirectedGraph<V> g){
		if(this.vertices.equals(g.vertices) && 
				this.neighbors.equals(g.neighbors) &&
				this.neighborsView.equals(g.neighborsView)){
			return true;
		} return false;
	}
}
