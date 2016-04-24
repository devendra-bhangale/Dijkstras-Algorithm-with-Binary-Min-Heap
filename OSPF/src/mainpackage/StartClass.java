package mainpackage;

import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

import graphpackage.Edge;
import graphpackage.Graph;
import graphpackage.Vertex;
import ospfpackage.BFS;
import ospfpackage.DijkstraAlgo;

public class StartClass {
	private static DecimalFormat df = new DecimalFormat("#0.00");
	private static Graph graph;

	public static void main(String[] args) {
		FileReader fin = null;
		Scanner graphFile = null;

		try {
			graph = new Graph();
			fin = new FileReader(args[0]);
			graphFile = new Scanner(fin);

			// Read the edges, weights and insert
			String line;
			while (graphFile.hasNextLine()) {
				line = graphFile.nextLine();
				StringTokenizer st = new StringTokenizer(line);

				try {
					if (st.countTokens() != 3) {
						System.err.println("Skipping ill-formatted line: " + line);
						continue;
					}

					String source = st.nextToken();
					String dest = st.nextToken();
					double weight = Double.parseDouble(st.nextToken());

					/* if src and dest are same vertex then generate error and skip to next line */
					if (source.equals(dest)) {
						System.err.println("Skipping line: " + line + " Source and Destination are same");
						graph.addVertex(source); // nevertheless add the vertex if it does not exist
						continue;
					}

					/* if edge weight is either zero or negative then generate error and skip to next line */
					if (weight <= 0.0) {
						System.err.println("Skipping line: " + line + " Edge weight is either zero or negative");
						graph.addVertex(source); // nevertheless add the vertex if it does not exist
						graph.addVertex(dest); // nevertheless add the vertex if it does not exist
						continue;
					}

					/* add two directed edges, one in each direction with same weight */
					graph.addEdge(source, dest, weight);
					graph.addEdge(dest, source, weight);

				} catch (NumberFormatException e) {
					System.err.println("Skipping ill-formatted line: " + line);
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		} finally { 
			try {
				/* close resources */
				graphFile.close();
				fin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Scanner in = new Scanner(System.in);
		StringTokenizer command;
		String line, query;
		boolean success;

		/* service users queries */
		while (true) {
			try {
				/* get query from user as input */
				System.out.print("\nEnter query: ");
				line = in.nextLine();
				command = new StringTokenizer(line);

				/* if the query is empty then generate error and skip to next query */
				if (command.countTokens() == 0) {
					System.err.println("Error: Query is empty");
					continue;
				}

				/* extract the main query */
				query = command.nextToken();
				success = false; // reset success; used later to check the success or failure of the query

				/* exit if it is a quit query */
				if ((command.countTokens() == 0) && query.equals("quit"))
					break;

				/* service the query */
				switch (query) {

				case "addedge":
					success = addedge(command);
					break;
				case "deleteedge":
					success = deleteedge(command);
					break;
				case "edgedown":
					success = edgeStatus(command, query);
					break;
				case "edgeup":
					success = edgeStatus(command, query);
					break;
				case "vertexdown":
					success = vertexStatus(command, query);
					break;
				case "vertexup":
					success = vertexStatus(command, query);
					break;
				case "path":
					success = path(command);
					break;
				case "print":
					success = print(command);
					break;
				case "reachable":
					success = reachable(command);
					break;
				}

				/* if the query was not successfully handled, print error */
				if (!success)
					System.err.println("Error: Invalid query format");

				Thread.sleep(100); // sleep for 100 milliseconds to avoid clash between the system input and output

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		in.close(); // close resources
	}

	/* Query 'addedge tailvertex headvertex transmit_time': add this directed edge to graph if it does not exist; else update its weight */
	private static boolean addedge(StringTokenizer command) {
		if (command.countTokens() != 3) {
			System.err.println("Error: Either source or destination or weight or all not provided");
			return false;
		}

		try {
			/* extract the source, destination and weight from query */
			String srcVertex = command.nextToken(); // next query token is taken as tail vertex
			String destVertex = command.nextToken(); // next query token is taken as head vertex
			double weight = Double.parseDouble(command.nextToken()); // next query token is taken as weight of this edge

			/* if edge weight is either zero or negative then generate error and skip to next query */
			if (weight <= 0.0) {
				System.err.println("Error: Edge weight is either zero or negative");
				return false;
			}

			/* add this edge to graph if it does not exist; else update its weight */
			graph.addEdge(srcVertex, destVertex, weight);

		} catch (Exception e) {
			System.err.println("Error: Invalid weight input");
		}
		return true;
	}

	/* Query 'deleteedge tailvertex headvertex': delete the specified directed edge from the graph */
	private static boolean deleteedge(StringTokenizer command) {
		if (command.countTokens() != 2) {
			System.err.println("Error: Either source or destination or both not provided");
			return false;
		}

		/* extract the source, destination and weight from query */
		String srcVertex = command.nextToken(); // next query token is taken as tail vertex
		String destVertex = command.nextToken(); // next query token is taken as head vertex

		/* check for possible vertex errors */
		if (!errorCheck2(srcVertex, destVertex))
			return false;

		/* delete the specified directed edge from the graph */
		graph.deleteEdge(srcVertex, destVertex);

		return true;
	}

	/* Query 'edgedown/edgeup tailvertex headvertex': mark the directed edge as “down” or "up" and therefore unavailable or available for use respectively */
	private static boolean edgeStatus(StringTokenizer command, String query) {
		if (command.countTokens() != 2) {
			System.err.println("Error: Either source or destination or both not provided");
			return false;
		}

		/* extract the source, destination and weight from query */
		String srcVertex = command.nextToken(); // next query token is taken as tail vertex
		String destVertex = command.nextToken(); // next query token is taken as head vertex

		/* check for possible vertex errors */
		if (!errorCheck2(srcVertex, destVertex))
			return false;

		String edgeName = srcVertex + destVertex;
		if (query.equals("edgedown")) {
			/* mark the directed edge as “down” and therefore unavailable for use */
			graph.edgeDown(edgeName);
		} else if (query.equals("edgeup")) {
			/* mark the directed edge as “up” and therefore available for use */
			graph.edgeUp(edgeName);
		}

		return true;
	}

	/* Query 'vertexdown/vertexup vertex': mark the vertex as “down” or "up" so that none of its edges can be used */
	private static boolean vertexStatus(StringTokenizer command, String query) {
		if (command.countTokens() != 1) {
			System.err.println("Error: Vertex not provided");
			return false;
		}

		/* extract the vertex from query */
		String vertex = command.nextToken(); // next query token is taken as the vertex

		/* check for possible vertex errors */
		if (!errorCheck1(vertex))
			return false;

		if (query.equals("vertexdown")) {
			/* mark the vertex as “down” and therefore edges to and from it will be unavailable for use */
			graph.vertexDown(vertex);
		} else if (query.equals("vertexup")) {
			/* mark the vertex as “up” and therefore edges to and from it will be available for use */
			graph.vertexUp(vertex);
		}

		return true;
	}

	/* Query 'path from_vertex to_vertex': run Dijkstra's Algorithm to get the shortest path from source to destination */
	private static boolean path(StringTokenizer command) {
		if (command.countTokens() != 2) {
			System.err.println("Error: Either source or destination or both not provided");
			return false;
		}

		/* extract the source and destination from query */
		String srcVertex = command.nextToken(); // next query token is taken as tail vertex
		String destVertex = command.nextToken(); // next query token is taken as head vertex

		/* check for possible vertex errors */
		if (!errorCheck2(srcVertex, destVertex))
			return false;

		/* run Dijkstra's Algorithm to get the shortest path from source to destination */
		DijkstraAlgo dijkstraAlgo = new DijkstraAlgo(srcVertex);
		dijkstraAlgo.getShortestPath(srcVertex, destVertex);

		/* print the shortest path */
		String shortestPath = destVertex + "\t" + df.format(graph.getVertex(destVertex).distance);
		if (graph.getVertex(destVertex).prev != null) {
			String prevVertex = graph.getVertex(destVertex).prev.name;
			while (!prevVertex.equals(srcVertex)) {
				shortestPath = prevVertex + " -> " + shortestPath;
				prevVertex = graph.getVertex(prevVertex).prev.name;
			}
			shortestPath = srcVertex + " -> " + shortestPath;
			System.out.println(shortestPath);
		} else
			System.out.println("No path was found from " + srcVertex + " to " + destVertex);

		return true;
	}

	/* Query 'print': print the contents of the graph; all in alphabetical order */
	private static boolean print(StringTokenizer command) {
		if (command.countTokens() != 0) {
			return false;
		}

		/* convert the vertices map into an alphabetically ordered tree map */
		Map<String, Vertex> orderedVertices = new TreeMap<String, Vertex>(graph.getVERTEX_MAP());
		Map<String, Vertex> orderedAdjVertices;

		for (Vertex vertex : orderedVertices.values()) {
			/* If a vertex status is '0: down', then append 'DOWN' to its name and print */
			if (vertex.vertexStatus == 1)
				System.out.println(vertex.name);
			else if (vertex.vertexStatus == 0)
				System.out.println(vertex.name + " DOWN");

			/* convert the adjacent vertices list into an alphabetically ordered tree map */
			orderedAdjVertices = new TreeMap<String, Vertex>();
			for (Vertex adjVertex : vertex.adj) {
				orderedAdjVertices.put(adjVertex.name, adjVertex);
			}

			/* If an edge status is '0: down', then append 'DOWN' to its name and print */
			for (Vertex adjVertex : orderedAdjVertices.values()) {
				Edge edge = graph.getEdge(vertex.name + adjVertex.name);
				if (edge.edgeStatus == 1)
					System.out.println("\t" + adjVertex.name + " " + edge.weight);
				else if (edge.edgeStatus == 0)
					System.out.println("\t" + adjVertex.name + " " + edge.weight + " DOWN");
			}
		}

		return true;
	}

	/* Query 'reachable': print for each “up” vertex, all vertices that are reachable from it; all in alphabetical order */
	private static boolean reachable(StringTokenizer command) {
		if (command.countTokens() != 0) {
			return false;
		}

		/* convert the vertices map into an alphabetically ordered tree map */
		Map<String, Vertex> orderedVertices = new TreeMap<String, Vertex>(graph.getVERTEX_MAP());
		Map<String, Vertex> reachableVertices = new TreeMap<String, Vertex>(graph.getVERTEX_MAP());

		String printReachable = "";
		for (Vertex vertex : orderedVertices.values()) {
			if (vertex.vertexStatus == 0) // if the vertex is '0: DOWN'; then skip it
				continue;

			printReachable = printReachable + vertex.name + "\n";

			/* run the BFS for 'UP' vertices to find their reachable vertices */
			BFS bfs = new BFS(vertex.name);
			bfs.reachable(vertex.name, graph.getVERTEX_MAP().size());

			for (Vertex nextVertex : reachableVertices.values()) {
				/* if the vertex is at distance INFINITY then it's NOT reachable; then skip it */
				if (nextVertex.distance == Graph.INFINITY || nextVertex.name.equals(vertex.name))
					continue;

				printReachable = printReachable + "\t" + nextVertex.name + "\n";
			}
		}
		System.out.println(printReachable);

		return true;
	}

	/* check for all possible errors with respect to source and destination vertices and print if any error is found */
	private static boolean errorCheck2(String srcVertex, String destVertex) {
		/* if src and dest are same vertex then generate error and skip to next query */
		if (srcVertex.equals(destVertex)) {
			System.err.println("Error: Source and Destination are same");
			return false;
		}

		/* if src vertex is not found in the graph, then generate error and skip to next query */
		if (graph.getVertex(srcVertex) == null) {
			System.err.println("Error: Source not found in Graph");
			return false;
		}

		/* if dest vertex is not found in the graph, then generate error and skip to next query */
		if (graph.getVertex(destVertex) == null) {
			System.err.println("Error: Destination not found in Graph");
			return false;
		}

		return true;
	}

	/* check for all possible errors with respect to a vertex and print if any error is found */
	private static boolean errorCheck1(String vertex) {
		/* if the vertex is not found in the graph, then generate error and skip to next query */
		if (graph.getVertex(vertex) == null) {
			System.err.println("Error: Destination not found in Graph");
			return false;
		}

		return true;
	}
}
