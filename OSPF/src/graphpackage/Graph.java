package graphpackage;

public class Graph implements MapStructure {

	/* If vertex does not exist, add it to VERTEX_MAP; in any case return vertex */
	public Vertex getVertex(String vertexName) {
		Vertex vertex = VERTEX_MAP.get(vertexName);

		if (vertex == null) {
			vertex = new Vertex(vertexName);
			VERTEX_MAP.put(vertexName, vertex);
		}

		return vertex;
	}

	/* add new weighted edge to graph */
	public void addEdge(String fromVertex, String toVertex, double weight) {
		Vertex tail = getVertex(fromVertex);
		Vertex head = getVertex(toVertex);
		tail.adj.add(head);

		String edgeName = fromVertex + toVertex;
		Edge edge = new Edge(edgeName, weight);
		EDGE_MAP.put(edgeName, edge);
	}

	/* If edge does not exist, add it to EDGE_MAP; in any case return edge */
	public Edge getEdge(String edgeName) {
		Edge edge = EDGE_MAP.get(edgeName);
		return edge;
	}
}
