import java.util.LinkedList;
import java.util.List;


// Models a path between vertices.
	// Best not to try and store all paths for any but small graphs.
	// Used in the Dijkstra method and to track the longest shortest path
	// in a graph when get all paths is called.
	// We store Path objects in the PriorityQueue instead of
	// Vertex objects because a given Vertex may have several paths
	// to it and altering the Vertex may disrupt the PriorityQueue that
	// stores them.

public class Path implements Comparable<Path> {

		public List<Vertex> verticesInPath; // not necessary for temporary paths in Dijkstra's
		public double weightedCostOfPath;
		public Vertex dest;

		public Path() {
			verticesInPath = new LinkedList<>();
		}

		public Path(Vertex v, double c) {
			dest = v;
			weightedCostOfPath = c;
			verticesInPath = new LinkedList<Vertex>();
		}

		public void add(Vertex v) {
			verticesInPath.add(v);
		}

		// return number of vertices in this Path
		public int getNumVerticesInPath() {
			return verticesInPath.size();
		}

		// return the number of edges in this Path
		public int getNumEdgesInPath() {
			return verticesInPath.size() - 1;
		}

		// return weighted cost of path
		public double weightedCost() {
			return weightedCostOfPath;
		}

		public String toString() {
			StringBuilder result = new StringBuilder();
			result.append("[");
			for (Vertex v : verticesInPath) {
				result.append(v.getName());
				result.append(", ");
			}
			if (verticesInPath.size() > 0) {
				result.delete(result.length() - 2, result.length());
			}
			result.append("]");
			if (verticesInPath.size() > 0) {
				result.append(" cost: ");
				result.append(weightedCostOfPath);
			}
			return result.toString();
		}

		public int compareTo(Path other) {
			return (weightedCostOfPath < other.weightedCostOfPath) ? -1
					: (weightedCostOfPath > other.weightedCostOfPath) ? 1 : 0;
		}
	}