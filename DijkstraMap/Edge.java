
/** Edge class represents a single node in the linked list of edges for a vertex.
 * 
 */

class Edge {
	
	private int vertexId;
	private int cost;
	Edge next;
	
	Edge(int destinationVertex, int cost) {
		this.vertexId = destinationVertex;
		this.cost = cost;
		next = null;
	}
  
	public int getDestVert() {
		return vertexId;
	}
	
	public int getCost() {
		return cost;
	}
 }