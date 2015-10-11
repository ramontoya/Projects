import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Dijkstra {

	private int sourceVertex; // source vertex
	private DikjstraTableEntry[] dTable = DikjstraTable(); // cost table
	private MapGraph graph;
	private HashTable table;

	Dijkstra(String filename) throws IOException {
		loadGraph(filename);
	}

	public MapGraph getGraph() {
		return graph;
	}

	public static class DikjstraTableEntry {
		double distance;
		int path;

		DikjstraTableEntry(double distance, int path) {
			this.distance = distance;
			this.path = path;
		}
	}

	public DikjstraTableEntry[] DikjstraTable() {
		DikjstraTableEntry[] dTable = new DikjstraTableEntry[20];
		for (int i = 0; i < dTable.length; i++) {
			dTable[i] = new DikjstraTableEntry(Double.POSITIVE_INFINITY, -1);
		}
		return dTable;
	}

	public void printDikjstraTable(DikjstraTableEntry[] table) {
		int i = 0;
		for (DikjstraTableEntry d : table) {
			System.out.println();
			System.out.print("vertex-" + i + "   " + d.distance + "    "
					+ d.path);
			i++;
		}
		System.out.println();
	}

	/**
	 * Compute all the shortest paths from the source vertex to all the other
	 * vertices in the graph; This function is called from GUIApp, when the user
	 * clicks on the city.
	 */
	public void computePaths(CityNode vSource) {
		PriorityQueue queue = new PriorityQueue(graph.numNodes()); 
		sourceVertex = table.get(vSource.getCity()); 
		dTable = DikjstraTable(); // reset cost table
		dTable[sourceVertex].distance = 0;
		dTable[sourceVertex].path = -1;
		for (int i = 0; i < dTable.length; i++) {
			if (i == sourceVertex) {
				queue.insert(i, 0);
			} else {
				queue.insert(i, Double.POSITIVE_INFINITY);
			}
		}

		while (!queue.empty()) {
			int min = queue.removemin().elem;
			double currentCost = dTable[min].distance;
			Edge edge = graph.getEdge(min);
			while (edge != null) {
				if (currentCost + edge.getCost() < dTable[edge.getDestVert()].distance) {
					dTable[edge.getDestVert()].distance = currentCost
							+ edge.getCost();
					dTable[edge.getDestVert()].path = min;
					queue.reduceKey(edge.getDestVert(), dTable[edge.getDestVert()].distance);
				}
				edge = edge.next;
			}
		}
		printDikjstraTable(dTable);
	}

	/**
	 * Returns the shortest path between the source vertex and this vertex.
	 * Returns the array of node id-s on the path
	 */
	public ArrayList<Integer> shortestPath(CityNode vTarget) {
		ArrayList<Integer> path = new ArrayList<>();
		int destination = table.get(vTarget.getCity());
		int fullPath = dTable[table.get(vTarget.getCity())].path;
		
		while(fullPath != sourceVertex) {
			path.add(0, fullPath);
			fullPath = dTable[fullPath].path;
		}
		path.add(0, fullPath);
		path.add(destination);
		
		System.out.print("SHORTEST PATH: ");
		for(Integer city : path) {
			System.out.print(city + " ");
		}
		return path;
	}

	/**
	 * Loads graph info from the text file into MapGraph graph
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public void loadGraph(String filename) throws IOException {
		table = new HashTable();
		boolean nodes = true;
		boolean arcs = false;
		try (BufferedReader br = Files.newBufferedReader(Paths.get(filename),
				Charset.forName("UTF-8"))) {
			String line = br.readLine();
			int numNodes = Integer.parseInt(br.readLine());
			graph = new MapGraph((numNodes));
			int id = 0;
			while ((line = br.readLine()) != null) {
				if (line.equals("ARCS")) {
					nodes = false;
					arcs = true;
				} else if (nodes) {
					String[] elements = line.split(" ");
					table.add(elements[0], id);
					graph.addNode(new CityNode(elements[0], Double
							.parseDouble(elements[1]), Double
							.parseDouble(elements[2])));
					id++;
				} else if (arcs) {
					String[] elements = line.split(" ");
					graph.addEdge(
							table.get(elements[0]),
							new Edge(table.get(elements[1]), Integer
									.parseInt(elements[2])));
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		
		 // Create an instance of the Dijkstra class
		 // The parameter is the name of the file
		 Dijkstra dijkstra = new Dijkstra(args[0]);
		
		 // create the GUI window with the panel
		 GUIApp app = new GUIApp(dijkstra);
	}
}
