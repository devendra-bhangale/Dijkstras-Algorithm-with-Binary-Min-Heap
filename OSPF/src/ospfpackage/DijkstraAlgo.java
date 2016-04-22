package ospfpackage;

import graphpackage.Edge;
import graphpackage.Graph;
import graphpackage.Vertex;

/*	In an OSPF (Open Shortest Path First) protocol based dynamically changing network 
	Dijkstra's Algorithm to find single-source shortest paths 
	with a binary minimum heap priority queue */
public class DijkstraAlgo {
	MinHeapQueue queue;

	public DijkstraAlgo(String srcVertex) {
		Graph.resetAllVertices(); // reset parameters of all vertices: distance = INFINITY and prev = null

		Vertex source = Graph.VERTEX_MAP.get(srcVertex);
		source.distance = 0; // set source distance = 0

		queue = new MinHeapQueue(); // create a new queue for the shortest path algorithm and insert all the vertices in it
		for (Vertex vertex : Graph.VERTEX_MAP.values()){
			//vertex.queueIndex = queue.insert(vertex);
			queue.insert(vertex);
		}
	}

	/* run the Dijkstra's Algorithm here to get the shortest path from source to destination */
	public void getShortestPath(String srcVertex, String destVertex) {
		while (queue.getQueueSize() != 0) {
			Vertex currVertex = queue.extractMin();

			if(currVertex.adj != null){
				for (Vertex adjVertex : currVertex.adj) {
					Edge currEdge = Graph.EDGE_MAP.get(currVertex.name + adjVertex.name);
	
					if (adjVertex.distance > (currVertex.distance + currEdge.weight)) {
						adjVertex.distance = currVertex.distance + currEdge.weight;
						adjVertex.prev = currVertex;
						
						adjVertex.queueIndex = queue.increasePriority(adjVertex.queueIndex, adjVertex.distance, adjVertex.prev);
					}
	
				}
			}
		}
	}
}
