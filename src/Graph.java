/*  Student information for assignment:
 *
 *  On my honor, Skyler Vestal, this programming assignment is my own work
 *  and I have not provided this code to any other student.
 *
 *  UTEID: skv458
 *  email address: skylervestal@utexas.edu
 *  Grader name: Sam
 *  Number of slip days I am using: 0
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeSet;

/**
 * Models a directed graph. Edges can be weighted. To model an undirected graph
 * add an edge from vertex A to vertex B and another edge from vertex B to
 * vertex A. To model an unweighted graph set all edge costs to the same value,
 * typically 1 or 0.
 * 
 * @author scottm, based on the Graph class from Weiss Algorithms and Data
 *         Structures.
 */
public class Graph {

	// used to indicate a vertex has not been visited and
	// that no path exists between current start vertex.
	private static final double INFINITY = Double.MAX_VALUE;

	// The vertices in the graph. Every vertex must have a unique String label.
	private Map<String, Vertex> vertices;

	// Used to store the path between the two most distant vertices.
	// In other words the vertices with the LONGEST shortest
	// path of all the shortest paths in the graph.
	private Path longest;

	// flag to ensure all paths have been found
	// Must set to true when the allPaths method is called.
	private boolean allPathsFound;

	private String currentStartVertexName;

	// Used for djikstra's method
	private final static int VERTEX_VISITED = 1;

	/**
	 * Create an empty graph.
	 */
	public Graph() {
		vertices = new HashMap<>();
	}

	/**
	 * Add an edge to this graph. If an edge from source to dest already exists then
	 * this edge replaces the old edge. Creates vertices for source and / or dest if
	 * they are not already present. <br>
	 * pre: source != null, dest != null, cost > 0
	 * 
	 * @param source cannot be null
	 * @param dest   cannot be null
	 * @param cost   must be > 0
	 * @return return true if an edge existed from source to dest prior to this
	 *         method call, false otherwise.
	 */
	public boolean addEdge(String source, String dest, double cost) {
		if (source == null || dest == null) {
			throw new IllegalArgumentException("Violation of precondition. " + "Vertex names may not be null.");
		}
		if (cost <= 0) {
			throw new IllegalArgumentException("Violation of precondition. " + "edge costs must be > 0." + cost);
		}
		Vertex s = getVertex(source);
		Vertex d = getVertex(dest);
		return s.addEdge(d, cost);
	}

	/**
	 * Returns true if an edge (not a path, just an edge) exists from the source
	 * vertex to the destination vertex.
	 * 
	 * @param source the start vertex
	 * @param dest   the ending vertex (or destination)
	 * @return true if an edge exists from source to dest, false otherwise
	 */
	public boolean edgeExists(String source, String dest) {
		boolean result = false;
		Vertex src = vertices.get(source);
		if (src != null) {
			result = src.getEdgeWithName(dest) == null ? false : true;
		}
		return result;
	}

	/**
	 * Add a vertex with the given name to this Graph. The new vertex has no edges
	 * initially. <br>
	 * pre: name != null
	 * 
	 * @param name The name of the new vertex.
	 */
	public void addVertex(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Violation of precondition. " + "Vertex name may not be null.");
		}
		getVertex(name);
	}

	/**
	 * Find all unweighted shortest paths from the Vertex with startName to all
	 * other vertices in this Graph. <br>
	 * After this method is called, call the printPath(String) method to get the
	 * path from startName to any other vertex in this Graph.
	 *
	 * <br>
	 * pre: startName != null, containsVertex(startName) == true
	 *
	 * @param startName The starting vertex. This method will find all the
	 *                  unweighted shortest paths from the give vertex to all other
	 *                  vertices in the graph.
	 */
	public void findUnweightedShortestPath(String startName) {
		// CS314 STUDENTS - I RECOMMEND YOU LEAVE THIS METHOD AS IS.
		// DO NOT ALTER IT. Mike
		handleFUWSPPrecons(startName);

		Queue<Vertex> q = prepForFindingUnweightedShortestPaths(startName);

		while (!q.isEmpty()) {
			Vertex current = q.remove();
			for (Edge e : current.getAdjacent()) {
				Vertex neighberNode = e.getDest();
				// Is this the first time I have seen this vertex?
				if (neighberNode.weightedCostFromStartVertex == INFINITY) {
					// In an unweighted graph we treat
					// the weighted cost and number of edges from start
					// as the same.
					neighberNode.weightedCostFromStartVertex = current.weightedCostFromStartVertex + 1;
					neighberNode.numEdgesFromStartVertex = current.numEdgesFromStartVertex + 1;
					neighberNode.prev = current;
					q.add(neighberNode);
				}
			}
		}
	}

	// Set up queue and instance variables to find
	// unweighted shortest path algorithm from startName vertex
	// to every other Vertex we can reach.
	private Queue<Vertex> prepForFindingUnweightedShortestPaths(String startName) {
		currentStartVertexName = startName;
		clearAll();
		Vertex start = vertices.get(startName);
		start.weightedCostFromStartVertex = 0;
		start.numEdgesFromStartVertex = 0;
		Queue<Vertex> result = new LinkedList<>();
		result.add(start);
		return result;

	}

	// check preconditions for findUnweightedShortestPath
	private void handleFUWSPPrecons(String startName) {
		if (startName == null) {
			throw new IllegalArgumentException("Violation of precondition. " + "Vertex name may not be null.");
		}
		if (!containsVertex(startName)) {
			throw new NoSuchElementException("No Verex named " + startName + " exists in this Graph");
		}

	}

	/**
	 * Find all weighted shortest paths from the Vertex startName to all other
	 * vertices in this Graph using Dijkstra's algorithm.
	 *
	 * After this method is called, call the printPath(String) method to get the
	 * path from startNode to any other vertex in this Graph.
	 *
	 * <br>
	 * pre: startName != null, containsVertex(startName) == true
	 * 
	 * @param startName The starting vertex. This method will find all the weighted
	 *                  shortest paths from the given vertex to all other vertices
	 *                  in the graph.
	 */
	public void dijkstra(String startName) {
		if (startName == null) {
			throw new IllegalArgumentException("Violation of precondition. " + "Vertex name may not be null.");
		}
		if (!containsVertex(startName)) {
			throw new NoSuchElementException("No Vertex named " + startName + " exists in this Graph");
		}

		currentStartVertexName = startName;
		// CS314 STUDENTS - Complete this method
		// Use a PriorityQueue of Path objects (not Vertex objects)
		// It is acceptable to use the built in Java PriorityQueue class.
		// Get setup for algorithm
		PriorityQueue<Path> dQueue = getStartingQueue();
		// While there are still vertices to dequeue
		while (dQueue.size() > 0) {
			// Retrieve the vertex with the smallest weighted path
			Path temp = dQueue.remove();
			Vertex curr = temp.dest;
			// If the vertex has not been visited already
			if (curr.scratch == 0) {
				// Mark as visited
				curr.scratch = VERTEX_VISITED;
				// For every adjacent vertex
				for (Edge e : curr.getAdjacent()) {
					// If the cost from the current vertex to the adjacent vertex is less
					// than the current total cost for the vertex update the cost
					double newCost = e.getCost() + curr.weightedCostFromStartVertex;
					Vertex next = e.getDest();
					if (newCost < next.weightedCostFromStartVertex) {
						next.weightedCostFromStartVertex = newCost;
						next.prev = curr;
						next.numEdgesFromStartVertex = curr.numEdgesFromStartVertex + 1;
					}
					// Add this vertex to the queue
					dQueue.add(new Path(e.getDest(), newCost));
				}
			}
		}
	}

	// pre: startName != null
	// post: does the setup and returns the priority queue for djikstra's alg.
	private PriorityQueue<Path> getStartingQueue() {
		// Reset all the vertices
		clearAll();
		// Set the initial vertex to have a cost of 0
		Vertex start = vertices.get(currentStartVertexName);
		start.weightedCostFromStartVertex = 0;
		start.numEdgesFromStartVertex = 0;

		PriorityQueue<Path> dQueue = new PriorityQueue<>();
		// Add the initial vertex
		dQueue.add(new Path(start, 0));
		return dQueue;
	}

	/**
	 * Find all shortest paths between all pairs of vertices in this Graph
	 *
	 * <br>
	 * <br>
	 * After this method is done, each vertex shall store: <br>
	 * 1. The total number of vertices (not including itself) it is connect to. <br>
	 * 2. The sum of the number of edges in the all the shortest paths from the
	 * vertex to the vertices it connects to. (Unweighted path lengths.) <br>
	 * 3. The sum of the weighted path lengths in the all the shortest paths from
	 * the vertex to the vertices it connects to. <br>
	 * <br>
	 * Note, if the parameter weighted is false, 2 and 3 shall equal each other.
	 *
	 * <br>
	 * <br>
	 * This method also determines the shortest path with the greatest distance. The
	 * two most distant vertices in the Graph are those with the Longest shortest
	 * path between them. The longest path is based on the number of edges in the
	 * path if weighted == false and the unweighted shortest path algorithm is being
	 * used. The longest path is based on the highest cost shortest path if weighted
	 * == true and Dijkstra is used.
	 *
	 * <br>
	 * <br>
	 * if weighted == true, use dijkstra, otherwise, use unwieghted shortest path
	 *
	 * <br>
	 * <br>
	 * After this method is called the getAllPaths, getDiamter, and get longest path
	 * methods may be called.
	 *
	 * @param weighted If weighted == true use dijkstra's algorithm otherwise use
	 *                 the unweighted shortest path algorithm. (Ignore any weights
	 *                 for edges. All edge weights considered to be 1.)
	 */
	public void findAllPaths(boolean weighted) {
		allPathsFound = true;

		// CS314 Students - complete this method
		// Reset longest
		longest = new Path();
		// For every vertex
		for (String s1 : vertices.keySet()) {
			Vertex curr = vertices.get(s1);
			// Clear the current information
			curr.clearPathInfo();
			// Do the appropriate search algorithm
			if (weighted) {
				dijkstra(s1);
			} else {
				findUnweightedShortestPath(s1);
			}
			// For every other vertex
			for (String s2 : vertices.keySet()) {
				if (!s1.equals(s2)) {
					if (vertices.get(s2).weightedCostFromStartVertex != INFINITY) {
						// Increment the number connected
						curr.numVertexConnected++;
						// Add the number of edges to the unweighted
						curr.totalUnweightedPathLength += getPath(s2).getNumEdgesInPath();
						// Add the weighted cost to the total
						curr.totalWeightedPathLength += getPath(s2).weightedCost();
						// Check if the path is the longest
						// If there is a path from the current vertex to the new vertex
						Path currPath = getPath(s2);
						if (longest.weightedCostOfPath < currPath.weightedCostOfPath) {
							longest = currPath;
						}
					}
				}
			}
		}
	}

	// helper to get path from current start vertex to dest vertex
	private Path getPath(String dest) {
		Path result = new Path();
		Vertex end = vertices.get(dest);
		result.weightedCostOfPath = end.weightedCostFromStartVertex;
		if (end.weightedCostFromStartVertex != INFINITY) {
			getPath(result, end);
		}
		return result;
	}

	// helper to get path
	private void getPath(Path result, Vertex end) {
		if (end.prev != null) {
			getPath(result, end.prev);
		}
		result.add(end);
	}

	/**
	 * Get the number of edges of the shortest path from the current start vertex to
	 * the given destination vertex. If there is no path from the current start
	 * vertex to the given destination vertex, returns -1. <br>
	 * <br>
	 * pre: findUnweightedShortestPath or dijkstra called.
	 * <tt>containsVertex(dest) == true</tt>
	 * 
	 * @param dest the destination vertex.
	 * @return the number of edges from the current start vertex to the destination
	 *         vertex. returns -1 if no path exists.
	 */
	public int getNumEdgesFromStart(String dest) {
		if (currentStartVertexName == null) {
			throw new IllegalStateException("method " + "findUnweigthedShortesPath or dijkstra must be "
					+ "called before calling this method.");
		} else if (!containsVertex(dest)) {
			throw new NoSuchElementException("No Vertex named " + dest + " exists in this Graph");
		}

		int result = vertices.get(dest).numEdgesFromStartVertex;
		return (result == Integer.MAX_VALUE) ? -1 : result;
	}

	/**
	 * Get the total weighted cost of the shortest path from the current start
	 * vertex to the given destination vertex. If there is no path from the current
	 * start vertex to the given destination vertex -1 is returned. <br>
	 * <br>
	 * pre: findUnweightedShortestPath or dijkstra called.
	 * <tt>containsVertex(dest) == true</tt>
	 * 
	 * @param dest
	 * @return the total cost of the shortest path from the current start vertex to
	 *         the destination vertex. returns -1 if no path exists.
	 */
	public double getWeightedCostFromStart(String dest) {
		if (currentStartVertexName == null) {
			throw new IllegalStateException("method " + "findUnweigthedShortesPath or dijkstra must be "
					+ "called before calling this method.");
		} else if (!containsVertex(dest)) {
			throw new NoSuchElementException("No Vertex named " + dest + " exists in this Graph");
		}

		double result = vertices.get(dest).weightedCostFromStartVertex;
		if (result == INFINITY) {
			result = -1;
		}
		return result;
	}

	/**
	 * Get all path statistics for all vertices in this graph that are connected to
	 * one or more other vertices. <br>
	 * pre: findAllPaths has been called.
	 * 
	 * @return A TreeSet with AllPathsInfo for the vertices in this Graph.
	 */
	public TreeSet<AllPathsInfo> getAllPaths() {
		if (!allPathsFound) {
			throw new IllegalStateException("The method findAllPaths must be called before calling this method. ");
		}

		TreeSet<AllPathsInfo> result = new TreeSet<>();
		for (Vertex v : vertices.values()) {
			if (v.numVertexConnected > 0) {
				AllPathsInfo temp = new AllPathsInfo(v.getName(), v.numVertexConnected, v.totalWeightedPathLength);
				boolean added = result.add(temp);
				assert added : "Did not add path info for " + v + ". Why not?";
			}
		}
		return result;
	}

	/**
	 * Check if a vertex with the given name is present in this graph. <br>
	 * pre: name != null
	 * 
	 * @param name The name of the vertex to check.
	 * @return true if a vertex with name is present in this Graph, false otherwise.
	 */
	public boolean containsVertex(String name) {
		return vertices.containsKey(name);
	}

	/**
	 * Return the name of the current start vertex. <br>
	 * <br>
	 * pre: findUnweightedShortestPath or dijkstra called.
	 * 
	 * @return the label of the current starting vertex.
	 */
	public String getCurrentStartVertex() {
		if (currentStartVertexName == null) {
			throw new IllegalStateException(
					"method findUnweigthedShortesPath or dijkstra must be " + "called before calling this method.");
		}
		return this.currentStartVertexName;
	}

	/**
	 * Alternative to printPath that returns an List containing the path from the
	 * current start vertex to the vertex named destName. If no path exists an empty
	 * List is returned. <br>
	 * pre: destName != null, containsVertex(destName) == true, the startNode has
	 * been set by calling findUnweigthedShortesPath or dijkstra.
	 * 
	 * @param destName The destination vertex
	 * @return A list with the path from the current start vertex to the vertex with
	 *         destName. start vertex will be at index 0 and destName will be at
	 *         index list.size() - 1 unless there is no path from the start vertex
	 *         to destName in which case an emtpy list is returned.
	 */
	public List<String> findPath(String destName) {
		if (currentStartVertexName == null) {
			throw new IllegalStateException(
					"method findUnweigthedShortesPath or dijkstra must be " + "called before calling this method.");
		}
		if (destName == null) {
			throw new IllegalArgumentException("Violation of precondition. " + "Vertex name may not be null.");
		}
		if (!containsVertex(destName)) {
			throw new NoSuchElementException("No Vertex named " + destName + " exists in this Graph");
		}

		List<String> result = new LinkedList<>();
		Vertex end = vertices.get(destName);

		if (end.weightedCostFromStartVertex != INFINITY) {
			findPath(result, end);
		}
		return result;
	}

	// helper method to find path.
	private void findPath(List<String> result, Vertex end) {
		if (end.prev != null) {
			findPath(result, end.prev);
		}
		result.add(end.getName());
	}

	/**
	 * Print the path from the current start vertex to the vertex with name destName
	 * <br>
	 * pre: destName != null, containsVertex(destName) == true, the startNode has
	 * been set by calling findUnweigthedShortesPath or dijkstra.
	 * 
	 * @param destName The destination vertex.
	 */
	public void printPath(String destName) {
		Vertex end = vertices.get(destName);
		if (end == null) {
			throw new NoSuchElementException("No Node named " + destName + " exists in this Graph");
		} else if (end.weightedCostFromStartVertex == INFINITY) {
			System.out.println("no path to " + destName);
		} else {
			System.out.println("Cost is " + end.weightedCostFromStartVertex);
			printPath(end);
			System.out.println();
		}
	}

	// helper to print path
	private void printPath(Vertex dest) {
		if (dest.prev != null) {
			printPath(dest.prev);
		}
		System.out.println(dest.getName());
	}

	/**
	 * Return the number of edges in the longest shortest path in this Graph. <br>
	 * pre: findAllPaths has been called.
	 * 
	 * @return the diameter of this graph in terms of number of edges.
	 */
	public int getDiameter() {
		if (!allPathsFound) {
			throw new IllegalStateException("The method findAllPaths must " + "be called before calling this method. ");
		}
		return longest.getNumEdgesInPath();
	}

	/**
	 * Return the cost of the longest shortest path in this Graph. Note, the cost of
	 * the longest shortest path may differ than the number of edges in the path.
	 * (returned by getDiameter) <br>
	 * pre: findAllPaths has been called.
	 * 
	 * @return the diameter of this graph.
	 */
	public double costOfLongestShortestPath() {
		if (!allPathsFound) {
			throw new IllegalStateException("The method findAllPaths must be " + "called before calling this method. ");
		}
		return longest.weightedCostOfPath;
	}

	/**
	 * Return a path equal to the diameter of this graph. <br>
	 * pre: findAllPaths has been called.
	 * 
	 * @return the names of the vertices in a path equal to the diamter in this
	 *         graph. If there is more than one longest path one is arbitrarily
	 *         chosen.
	 */
	public String getLongestPath() {
		return longest.toString();
	}

	// helper. If name not present create new vertex.
	// return vertex with given name
	private Vertex getVertex(String name) {
		Vertex v = vertices.get(name);
		if (v == null) {
			v = new Vertex(name);
			vertices.put(name, v);
		}
		return v;
	}

	// reset all vertices to set new start vertex and find all paths
	// from new start
	private void clearAll() {
		for (Vertex v : vertices.values()) {
			v.reset();
		}
	}

	/**
	 * Prints out the name of all vertices in the Graph. Not expected to use on A12
	 */
	public void showAll() {
		for (Vertex v : vertices.values()) {
			System.out.println(v);
		}
	}

	/**
	 * Get the name of all vertices the Vertex with name is connected to. Not
	 * expected to use this on assignment 12.
	 * 
	 * @param name The name of the start Vertex
	 * @return a List with the names of the vertices connect to the Vertex name.
	 */
	public List<String> getConnections(String name) {
		List<String> result = new ArrayList<>();
		Vertex v = vertices.get(name);
		if (v != null) {
			for (Edge e : v.getAdjacent()) {
				result.add(e.getDest().getName());
			}
		}
		return result;
	}
}