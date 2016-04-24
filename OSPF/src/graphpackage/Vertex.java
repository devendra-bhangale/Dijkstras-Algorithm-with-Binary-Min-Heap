package graphpackage;

import java.util.LinkedList;
import java.util.List;

public class Vertex implements MapStructure {

	public int vertexStatus; // vertex status: UP / DOWN
	public String name; // Vertex name
	public List<Vertex> adj; // list of adjacent vertices

	public double distance; // shortest path distance from the source vertex
	public Vertex prev; // previous vertex on the shortest path from the source vertex
	public int queueIndex; // Index at which the vertex is positioned in the queue for Dijkstra's Algorithm.
							// Also as status of the vertex while running BFS to find reachable vertices

	/* create a new vertex */
	public Vertex(String name) {
		vertexStatus = UP;
		this.name = name;
		adj = new LinkedList<Vertex>();
		reset();
	}

	/* reset the required parameters */
	public void reset() {
		queueIndex = 0;
		distance = (double) INFINITY;
		prev = null;
	}
}
