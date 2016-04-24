/*
 * Class:			BFS.java
 * 
 * Author:			Devendra Bhangale
 * 
 * Description:		This class implements BFS (Breadth First Search) Algorithm to find all the reachable vertices in a dynamically changing network.
 */

package ospfpackage;

import java.util.LinkedList;
import java.util.List;

import graphpackage.Edge;
import graphpackage.Graph;
import graphpackage.Vertex;

public class BFS {
	private final int WHITE = 0; // undiscovered vertex
	private final int GRAY = 1; // discovered vertex
	private final int BLACK = -1; // finished vertex

	private List<Vertex> queue = null; // queue of vertices

	/* Initiate BFS Algorithm for the corresponding source vertex */
	public BFS(String source) {
		Graph.resetAllVertices();

		Vertex srcVertex = Graph.VERTEX_MAP.get(source);
		srcVertex.distance = 0; // set source vertex distance = 0
		srcVertex.queueIndex = GRAY; // set source vertex distance = 0

		queue = new LinkedList<Vertex>();
		queue.add(srcVertex);
	}

	/* run BFS to find all reachable vertices for specified source vertex */
	public void reachable(String source, int size) {
		for (int i = 0;; i++) { // loop through all the queued vertices
			Vertex vertex;

			try {
				vertex = deQueue(i); // dequeue next vertex from the queue; throws exception when all vertices are dequeued, i.e. it's empty
			} catch (Exception e) {
				break; // break the infinite loop when queue is empty
			}

			/* traverse through the adjacent vertices if the current vertex has any */
			if (vertex.adj != null) {
				for (Vertex adjVertex : vertex.adj) {
					if (adjVertex.vertexStatus == 0) // if the adjacent vertex is '0: DOWN'; then skip it
						continue;

					Edge currEdge = Graph.EDGE_MAP.get(vertex.name + adjVertex.name);
					if (currEdge.edgeStatus == 0) // if the edge to adjacent vertex is '0: DOWN'; then skip it
						continue;

					/* if the adjacent vertex is not discovered previously, update its parameters and enqueue it */
					if (adjVertex.queueIndex == WHITE) {
						adjVertex.queueIndex = GRAY;
						adjVertex.distance = vertex.distance + 1;
						adjVertex.prev = vertex;

						enQueue(adjVertex);
					}
				}
			}

			vertex.queueIndex = BLACK; // finished vertex
		}
	}

	/* dequeue the vertex at given index */
	private Vertex deQueue(int index) {
		Vertex vertex = queue.get(index);

		return vertex;
	}

	/* enqueue the given vertex at the end of the list */
	private void enQueue(Vertex vertex) {
		queue.add(vertex);
	}
}
