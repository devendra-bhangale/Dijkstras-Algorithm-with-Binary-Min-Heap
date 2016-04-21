package graphpackage;

import java.util.LinkedList;
import java.util.List;

public class Vertex implements MapStructure {

	public int vertexStatus;
	public String name; // Vertex name
	public List<Vertex> adj; // Adjacent vertices

	public int queueIndex;
	public double distance;
	public Vertex prev;

	public Vertex(String name) {
		vertexStatus = UP;
		this.name = name;
		adj = new LinkedList<Vertex>();
		reset();
	}

	public void reset() {
		queueIndex = 0;
		distance = (double) INFINITY;
		prev = null;
	}
}
