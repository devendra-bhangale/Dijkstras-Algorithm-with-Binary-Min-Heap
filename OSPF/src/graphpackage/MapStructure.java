package graphpackage;

import java.util.HashMap;
import java.util.Map;

public interface MapStructure {

	public Map<String, Vertex> VERTEX_MAP = new HashMap<String, Vertex>();
	public Map<String, Edge> EDGE_MAP = new HashMap<String, Edge>();

	public static final int INFINITY = Integer.MAX_VALUE;
	public static final int UP = 1;
	public static final int DOWN = 0;
}
