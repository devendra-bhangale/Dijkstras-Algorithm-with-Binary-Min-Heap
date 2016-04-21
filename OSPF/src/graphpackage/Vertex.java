package graphpackage;

import java.util.LinkedList;
import java.util.List;

public class Vertex implements MapStructure {

	public int vertexStatus;
	public String name; // Vertex name
	public List<Vertex> adj; // Adjacent vertices
	
	public int distance;
	public Vertex prev;

	public Vertex(String name) {
		vertexStatus = UP;
		this.name = name;
		adj = new LinkedList<Vertex>();
	}
	
	public void reset(){
		distance = 0;
		prev = null;
	}
}
