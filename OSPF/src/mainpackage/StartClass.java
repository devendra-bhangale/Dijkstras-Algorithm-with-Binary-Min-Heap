package mainpackage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

import graphpackage.Graph;

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
		while (true)
			;
	}
}
