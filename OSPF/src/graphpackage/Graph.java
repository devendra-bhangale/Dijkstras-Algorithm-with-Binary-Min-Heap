/*
 * Class:			Graph.java
 * 
 * Author:			Devendra Bhangale
 * 
 * Description:		This class implements various methods to modify the nodes and directed paths in the network graph.
 */

package graphpackage;

import java.util.HashMap;
import java.util.Map;

public class Graph implements MapStructure {
	public static Map<String, Vertex> VERTEX_MAP = new HashMap<String, Vertex>(); // data structure holding all the vertices in the graph
	public static Map<String, Edge> EDGE_MAP = new HashMap<String, Edge>(); // data structure holding all the edges in the graph

	/* If vertex does not exist, add it to VERTEX_MAP; in any case return vertex */
	public Vertex addVertex(String vertexName) {
		Vertex vertex = VERTEX_MAP.get(vertexName);

		if (vertex == null) {
			vertex = new Vertex(vertexName);
			VERTEX_MAP.put(vertexName, vertex);
		}

		return vertex;
	}

	/* reset the 'distance' and 'prev' variables of all vertices to zero and null respectively */
	public static void resetAllVertices() {
		for (Vertex vertex : VERTEX_MAP.values())
			vertex.reset();
	}

	/* If vertex does not exist, add it to VERTEX_MAP; in any case return vertex */
	public Vertex getVertex(String vertexName) {
		if (VERTEX_MAP.containsKey(vertexName)) {
			Vertex vertex = VERTEX_MAP.get(vertexName);
			return vertex;
		}

		return null;
	}

	/* if the vertex exists, mark the vertex as “down” and therefore edges to and from it will be unavailable for use */
	public void vertexDown(String vertexName) {
		Vertex vertex;
		if ((vertex = getVertex(vertexName)) != null) {
			vertex.vertexStatus = DOWN;
		}
	}

	/* if the vertex exists, mark the vertex as “up” and therefore edges to and from it will be available for use */
	public void vertexUp(String vertexName) {
		Vertex vertex;
		if ((vertex = getVertex(vertexName)) != null) {
			vertex.vertexStatus = UP;
		}
	}

	/* add new weighted edge to graph */
	public void addEdge(String fromVertex, String toVertex, double weight) {
		/* check if the tail vertex exists and add it to the map if does not */
		Vertex tail = getVertex(fromVertex);
		if (tail == null) {
			tail = addVertex(fromVertex);
		}

		/* check if the head vertex exists and add it to the map if does not */
		Vertex head = getVertex(toVertex);
		if (head == null) {
			head = addVertex(toVertex);
		}

		/* add the head vertex in the adjacency list of the tail vertex */
		if (!tail.adj.contains(head)) {
			tail.adj.add(head);
		}

		/* create a single directed edge if it does not exist with given weight and add it to the map else if it exists then update its weight */
		String edgeName = fromVertex + toVertex;
		if (!EDGE_MAP.containsKey(edgeName)) {
			Edge edge = new Edge(edgeName, weight);
			EDGE_MAP.put(edgeName, edge);
		} else {
			EDGE_MAP.get(edgeName).weight = weight;
		}
	}

	/* delete the specified directed edge from the graph if it exists */
	public void deleteEdge(String fromVertex, String toVertex) {
		Vertex tail = getVertex(fromVertex);
		Vertex head = getVertex(toVertex);

		/* remove the head vertex from the adjacency list of the tail vertex */
		while (tail.adj.contains(head)) {
			tail.adj.remove(head);
		}

		/* delete the edge from the map */
		String edgeName = fromVertex + toVertex;
		while (EDGE_MAP.containsKey(edgeName)) {
			EDGE_MAP.remove(edgeName);
		}
	}

	/* return the edge with given name if it exists */
	public Edge getEdge(String edgeName) {
		if (EDGE_MAP.containsKey(edgeName)) {
			Edge edge = EDGE_MAP.get(edgeName);
			return edge;
		}
		return null;
	}

	/* if the edge exists, mark the directed edge as “down” and therefore unavailable for use */
	public void edgeDown(String edgeName) {
		Edge edge;
		if ((edge = getEdge(edgeName)) != null) {
			edge.edgeStatus = DOWN;
		}
	}

	/* if the edge exists, mark the directed edge as “up” and therefore available for use */
	public void edgeUp(String edgeName) {
		Edge edge;
		if ((edge = getEdge(edgeName)) != null) {
			edge.edgeStatus = UP;
		}
	}

	/* return the current vertex map in the graph */
	public Map<String, Vertex> getVERTEX_MAP() {
		return VERTEX_MAP;
	}

	/* return the current edge map in the graph */
	public Map<String, Edge> getEDGE_MAP() {
		return EDGE_MAP;
	}
}
