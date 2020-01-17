import java.util.LinkedList;
import java.util.List;


// model a vertex. uses list of edges from this vertex to others
public class Vertex {

	private String name;
	private List<Edge> adjacent;
	private final double INFINITY = Double.MAX_VALUE;

	// Number of other vertices this vertex is connected to.
	// This variable should be updated in the findAllPaths method.
	public int numVertexConnected;

	// Sum of all unweighted shortest paths from this vertex to other vertices.
	// This variable should be updated in the findAllPaths method.
	public double totalUnweightedPathLength;

	// Sum of all weighted shortest paths from this vertex to other vertices.
	// This variable should be updated in the findAllPaths method.
	public double totalWeightedPathLength;

	// for graph algorithms
	public double weightedCostFromStartVertex;
	public int numEdgesFromStartVertex;
	public Vertex prev;
	public int scratch;

	public Vertex(String n) {
		name = n;
		adjacent = new LinkedList<>();
		reset();
	}

	// called when looking for new shortest path info
	public void reset() {
		weightedCostFromStartVertex = INFINITY;
		numEdgesFromStartVertex = Integer.MAX_VALUE;
		prev = null;
		scratch = 0;
	}

	// zero out the sum of paths
	public void clearPathInfo() {
		numVertexConnected = 0;
		totalUnweightedPathLength = 0;
		totalWeightedPathLength = 0;
	}

	public String toString() {
		return "{" + name + ", " + ", connected to: " + numVertexConnected + "adjacent: " + adjacent + "}";
	}

	// Add an edge from this vertex to dest.
	// If an edge already exists replace the previous
	// cost with the new cost.
	// Return true if an edge existed from this
	// vertex to dest prior to the method call.
	public boolean addEdge(Vertex dest, double cost) {
		Edge e = getEdgeWithName(dest.name);
		if (e == null) {
			adjacent.add(new Edge(dest, cost));
		} else {
			e.setCost(cost);
		}
		return e != null;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Edge> getAdjacent() {
		return adjacent;
	}
	
	public boolean equals(Object other) {
		boolean result = other instanceof Vertex;
		if (result) {
			result = name.equals(((Vertex) other).name);
		}
		return result;
	}

	public int hashCode() {
		return name.hashCode();
	}

	// Gets the edge from this Vertex to
	// dest if one exists. If one does not
	// exist return null.
	public Edge getEdgeWithName(String dest) {
		for (Edge e : adjacent) {
			if (e.getDest().name.equals(dest)) {
				return e;
			}
		}
		return null;
	}
} // end of the Vertex class
