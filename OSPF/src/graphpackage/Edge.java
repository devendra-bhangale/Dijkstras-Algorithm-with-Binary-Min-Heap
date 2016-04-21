package graphpackage;

public class Edge implements MapStructure {

	public int edgeStatus;
	public String edgeName;
	public double weight;

	public Edge(String edgeName, double weight) {
		edgeStatus = UP;
		this.edgeName = edgeName;
		this.weight = weight;

		// EDGE_MAP.put(edgeName, weight);
	}
}
