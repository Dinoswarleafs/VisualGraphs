// models edge between two vertices
public static class Edge {
		private Vertex dest;
		private double cost;

		public Edge(Vertex d, double c) {
			dest = d;
			cost = c;
		}

		public String toString() {
			return "(" + dest.name + " " + cost + ")";
		}
	}