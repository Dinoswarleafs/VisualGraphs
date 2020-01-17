// models edge between two vertices
public class Edge {
		private Vertex dest;
		private double cost;

		public Edge(Vertex d, double c) {
			dest = d;
			cost = c;
		}
		
		public Vertex getDest() {
			return dest;
		}
		
		public double getCost() {
			return cost;
		}

		public String toString() {
			return "(" + dest.getName() + " " + cost + ")";
		}

		public void setCost(double cost) {
			this.cost = cost;
		}
	}