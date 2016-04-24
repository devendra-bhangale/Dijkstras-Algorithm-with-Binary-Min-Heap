package graphpackage;

public class Edge implements MapStructure {

	public int edgeStatus; // edge status: UP / DOWN
	public String edgeName; // edge name
	public double weight; // edge weight

	/* create a new edge */
	public Edge(String edgeName, double weight) {
		edgeStatus = UP;
		this.edgeName = edgeName;
		this.weight = weight;
	}
}
