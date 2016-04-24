package ospfpackage;

import graphpackage.Edge;
import graphpackage.Graph;
import graphpackage.Vertex;

/*	In an OSPF (Open Shortest Path First) protocol based dynamically changing network 
	Dijkstra's Algorithm to find single-source shortest paths 
	with a binary minimum heap priority queue */
public class DijkstraAlgo {
	private MinHeapQueue queue;

	/* Initiate Dijkstra's Algorithm for the corresponding source vertex */
	public DijkstraAlgo(String srcVertex) {
		Graph.resetAllVertices(); // reset parameters of all vertices: distance = INFINITY and prev = null

		Vertex source = Graph.VERTEX_MAP.get(srcVertex);
		source.distance = 0; // set source distance = 0

		queue = new MinHeapQueue(); // create a new queue for the shortest path algorithm and insert all the vertices in it
		for (Vertex vertex : Graph.VERTEX_MAP.values()) {
			// vertex.queueIndex = queue.insert(vertex);
			queue.insert(vertex);
		}
	}

	/* run the Dijkstra's Algorithm here to get the shortest path from source to destination */
	public void getShortestPath(String srcVertex, String destVertex) {
		while (queue.getQueueSize() != 0) {
			Vertex currVertex = queue.extractMin(); // extract the minimum distance vertex from the queue

			/* traverse through the adjacent vertices if the current vertex has any */
			if (currVertex.adj != null) {
				for (Vertex adjVertex : currVertex.adj) {
					if (adjVertex.vertexStatus == 0) // if the adjacent vertex is '0: DOWN'; then skip it
						continue;

					Edge currEdge = Graph.EDGE_MAP.get(currVertex.name + adjVertex.name);
					if (currEdge.edgeStatus == 0) // if the edge to adjacent vertex is '0: DOWN'; then skip it
						continue;

					/* if new shorter distance is found, then update distance and previous vertex */
					if (adjVertex.distance > (currVertex.distance + currEdge.weight)) {
						adjVertex.distance = currVertex.distance + currEdge.weight;
						adjVertex.prev = currVertex;

						/* increase priority of this vertex in the queue */
						adjVertex.queueIndex = queue.increasePriority(adjVertex.queueIndex, adjVertex.distance, adjVertex.prev);
					}

				}
			}
		}
	}
}
