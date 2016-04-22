package mainpackage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

import graphpackage.Graph;
import ospfpackage.DijkstraAlgo;

public class StartClass {

	public static void main(String[] args) {
		Graph graph = new Graph();

		try {
			FileReader fin = new FileReader(args[0]);
			Scanner graphFile = new Scanner(fin);

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
					graph.addEdge(source, dest, weight);
				} catch (NumberFormatException e) {
					System.err.println("Here Skipping ill-formatted line: " + line);
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		Scanner in = new Scanner(System.in);
		while (true) {
			try{
				System.out.print("Enter query: ");
				String line = in.nextLine();
				StringTokenizer command = new StringTokenizer(line);
				
				String query = command.nextToken();
				String srcVertex = command.nextToken();
				String destVertex = command.nextToken();

				DijkstraAlgo dijkstraAlgo = new DijkstraAlgo(srcVertex);
				dijkstraAlgo.getShortestPath(srcVertex, destVertex);
				
				String path = destVertex;
				String prevVertex = (Graph.VERTEX_MAP.get(destVertex).prev.name != null) ? Graph.VERTEX_MAP.get(destVertex).prev.name : "";
				while(prevVertex != "" && !prevVertex.equals(srcVertex)){
					path = prevVertex + "->" + path;
					prevVertex = Graph.VERTEX_MAP.get(prevVertex).prev.name;
				}
				path = srcVertex + " -> " + path + "\n";
				System.out.print("Shortest Path: " + path);
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
