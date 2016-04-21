package graphpackage;

import java.util.HashMap;
import java.util.Map;

public interface MapStructure {

	public Map<String, Vertex> VERTEX_MAP = new HashMap<String, Vertex>(); // data structure holding all the vertices in the graph
	public Map<String, Edge> EDGE_MAP = new HashMap<String, Edge>(); // data structure holding all the edges in the graph

	public static final int INFINITY = Integer.MAX_VALUE; // infinite distance value
	public static final int UP = 1; // vertex or edge is UP and working
	public static final int DOWN = 0; // vertex or edge is DOWN and not working
}
