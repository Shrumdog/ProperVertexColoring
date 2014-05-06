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
	private int edgeCount;

	public UndirectedGraph() {
		vertices = new ArrayList<V>();
		verticesView = Collections.unmodifiableList(vertices);
		neighbors = new HashMap<V,LinkedHashSet<V>>();
		neighborsView = new HashMap<V,Set<V>>();
	}

	public int getVertexCount() {
		return vertices.size();
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
}
