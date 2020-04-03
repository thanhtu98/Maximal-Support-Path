package MaxSP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MSP {
	// tim kiem cac sensor gan canh delaunay
	static List<Point> setNeighbor(List<Edge> eds, Edge e) {
		List<Point> neighbor = new ArrayList<Point>();
		for (int i = 0; i < eds.size(); i++) {
			if (e.p1.equals(eds.get(i).p1)) {
				neighbor.add(eds.get(i).p1);
			}
			if (e.p1.equals(eds.get(i).p2)) {
				neighbor.add(eds.get(i).p2);
			}
			if (e.p2.equals(eds.get(i).p1)) {
				neighbor.add(eds.get(i).p1);
			}
			if (e.p2.equals(eds.get(i).p2)) {
				neighbor.add(eds.get(i).p2);
			}
		}
		return neighbor;
	}

	static boolean BFS(Point v, Point f, LinkedList<Point> adj[]) {
		boolean rs = false;
		boolean visited[] = new boolean[adj.length];

		// Create a queue for BFS
		LinkedList<Point> queue = new LinkedList<Point>();

		// Mark the current node as visited and enqueue it
		visited[v.getIdx()] = true;
		queue.add(v);

		while (queue.size() != 0) {
			// Dequeue a vertex from queue and print it
			v = queue.poll();
			// System.out.print(v + " ");

			// Get all adjacent vertices of the dequeued vertex s
			// If a adjacent has not been visited, then mark it
			// visited and enqueue it
			Iterator<Point> i = adj[v.idx].iterator();
			while (i.hasNext()) {
				Point n = i.next();
				if (!visited[n.idx]) {
					visited[n.idx] = true;
					queue.add(n);
				}
			}
		}
		if (visited[f.idx] == true) {
			rs = true;
		}
		return rs;

	}

	public static void main(String[] args) {
		List<Edge> edgeList = new ArrayList<Edge>();
		try (BufferedReader br = new BufferedReader(
				new FileReader(new File("C:\\Users\\Trant\\eclipse-workspace\\MaxSupportPath\\src\\output.txt")))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] coordinatesString = line.split(";");
				Point p1 = new Point(
						Double.parseDouble(coordinatesString[0].substring(0, coordinatesString[0].indexOf(','))),
						Double.parseDouble(coordinatesString[0].substring(coordinatesString[0].indexOf(',') + 1,
								coordinatesString[0].length() - 1)));
				Point p2 = new Point(
						Double.parseDouble(coordinatesString[1].substring(0, coordinatesString[1].indexOf(','))),
						Double.parseDouble(coordinatesString[1].substring(coordinatesString[1].indexOf(',') + 1,
								coordinatesString[1].length() - 1)));
				Edge edg = new Edge(p1, p2);
				edgeList.add(edg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// luu cac dinh cua do thi
		HashSet<Point> pointList = new HashSet<Point>();
		for (Edge edge : edgeList) {
			pointList.add(edge.p1);
			pointList.add(edge.p2);
		}
		Point[] points = new Point[pointList.size()];
		pointList.toArray(points);
		for (int i = 0; i < points.length; i++) {
			points[i].setIdx(i);
		}
		// thuat toan tim maximal suport path voi tim kiem nhi phan
		// khoi tao
		double min_weight = Double.MAX_VALUE;
		double max_weight = Double.MIN_VALUE;
		for (Edge edge : edgeList) {
			if (edge.getLength() < min_weight)
				min_weight = edge.getLength();
			if (edge.getLength() > max_weight)
				max_weight = edge.getLength();
		}
		// khoi tao diem I va F ngau nhien
		Random rd = new Random();
		Point I = points[rd.nextInt(points.length)]; // diem bat dau
		Point F = points[rd.nextInt(points.length)]; // diem ket thuc
		while (I.equals(F)) {
			F = points[rd.nextInt(points.length)];
		}

		double range = (max_weight - min_weight) / 2;
		double suport_weight = min_weight + range;
		// search ket hop duyet bfs voi binary search
		while (range > 0.0001) {
			// khoi tao do thi G'
			// them cac dinh vao danh sach ke
			LinkedList<Point> adj[] = new LinkedList[points.length];
			for (int i = 0; i < points.length; i++) {
				adj[points[i].idx] = new LinkedList<Point>();
				for (int j = 0; j < edgeList.size(); j++) {
					if (edgeList.get(j).getLength() < suport_weight) {
						if ((points[i].equals(edgeList.get(j).p1)))
							adj[points[i].idx].add(edgeList.get(j).p2);
						if ((points[i].equals(edgeList.get(j).p2)))
							adj[points[i].idx].add(edgeList.get(j).p1);
					}
				}
			}

			range /= 2;

			if (BFS(I, F, adj)) {
				suport_weight -= range;
			} else {
				suport_weight += range;
			}

		}
		System.out.println("các cạnh của tam phân Delauney");
		for (int i = 0; i < edgeList.size(); i++) {
			System.out.println(edgeList.get(i).toString());
		}
		System.out.println("Diem bat dau I=" + I.toString() + "\nDiem ket thuc F= " + F.toString());
		System.out.println("Maximal suport weight = " + suport_weight);
	}
}
