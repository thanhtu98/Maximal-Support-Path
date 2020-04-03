package MaxSP;


public class Edge {
	Point p1;
	Point p2;
	double length; 

	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public double getLength() {
		return length;
	}

	public Edge(Point p1, Point p2) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.length =Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
	}

	@Override
	public String toString() {
		return "Edge [p1=" + p1 + ", p2=" + p2 +"length = "+length+ "]";
	}
}
