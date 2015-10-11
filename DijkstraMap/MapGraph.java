/** A class representing a graph. Stores an array of nodes and adjacency list.
 * 
 */
import java.awt.Point;

public class MapGraph {

	private CityNode[] nodes;

	// for each vertex store a linked list of edges;
	private Edge[] adjacencyList;

	private int numNodes = 0;
	private int numEdges = 0;

	public final int EPS_DIST = 5;

	MapGraph(int numNodes) {
		nodes = new CityNode[numNodes];
		adjacencyList = new Edge[numNodes];
	}

	/**
	 * Returns a node with index i
	 */
	CityNode getNode(int i) {
		// FILL IN CODE
		return nodes[i];
	}

	/**
	 * Returns the head of the linked list of edges for a vertex with id = i
	 */
	public Edge getEdge(int i) {
		// FILL IN CODE
		return adjacencyList[i];
	}

	/**
	 * Adds a node to the graph
	 * 
	 * @param node
	 */
	public void addNode(CityNode node) {
		// FILL IN CODE
		nodes[numNodes] = node;
		numNodes++;
	}

	public int numNodes() {
		return numNodes;
	}

	/**
	 * Adds the edge to the linked list for this vertexId
	 * 
	 * @param vertexId
	 * @param edge
	 */
	public void addEdge(int nodeId, Edge edge) {
		// FILL IN CODE
		int pointBack = edge.getDestVert();
		Edge returning = new Edge(nodeId, edge.getCost());
		if (adjacencyList[nodeId] == null) {
			adjacencyList[nodeId] = edge;
		} else {
			Edge current = adjacencyList[nodeId];
			while (current.next != null) {
				current = current.next;
			}
			current.next = edge;
		}

		if (adjacencyList[pointBack] == null) {
			adjacencyList[pointBack] = returning;
		} else {
			Edge current = adjacencyList[pointBack];
			while (current.next != null) {
				current = current.next;
			}
			current.next = returning;
		}
		numEdges = numEdges + 2;
	}

	/**
	 * Given the location of the click, return the node of the graph at this
	 * location.
	 */
	public CityNode getVertex(Point loc) {
		for (CityNode v : nodes) {
			Point p = v.getLocation();
			if ((Math.abs(loc.x - p.x) < EPS_DIST)
					&& (Math.abs(loc.y - p.y) < EPS_DIST))
				return v;
		}
		return null;
	}

	/**
	 * Returns the array of all edges for drawing: each element in the array
	 * corresponds to one edge and is the array of two Point objects
	 * (corresponding to the locations of the two nodes connected by this edge).
	 */
	public Point[][] getEdges() {
		// FILL IN CODE
		Point[][] edges = new Point[numEdges][2];
		int origin = 0;
		int i = 0;
		for (Edge edge : adjacencyList) {
			while (edge != null) {
				edges[i][0] = nodes[origin].getLocation();
				edges[i][1] = nodes[edge.getDestVert()].getLocation();
				edge = edge.next;
				i++;
			}
			origin++;
		}
		return edges;
	}

	/**
	 * Returns the array of nodes as points. Used by MapGraph to draw little
	 * circles at the location of the nodes
	 */
	public Point[] getNodeLocations() {
		// FILL IN CODE
		Point[] locations = new Point[numNodes];
		int i = 0;
		for (CityNode node : nodes) {
			locations[i] = node.getLocation();
			i++;
		}
		return locations;
	}

	/**
	 * Returns the array of cities corresponding to the vertices of this graph
	 * in the array
	 * 
	 * @return
	 */
	public String[] getCities() {
		// FILL IN CODE
		String[] labels = new String[numNodes];
		int i = 0;
		for (CityNode node : nodes) {
			labels[i] = node.getCity();
			i++;
		}
		return labels;
	}

	public void printAdjacencyList() {
		int i = 0;
		for (Edge edge : adjacencyList) {
			System.out.print("vertexId-" + i + ": ");
			while (edge != null) {
				System.out.print("(" + edge.getDestVert() + ")"
						+ edge.getCost() + " ");
				edge = edge.next;
			}
			System.out.println();
			i++;
		}
	}
} // class MapGraph