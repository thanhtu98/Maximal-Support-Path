import java.io.*;
import java.util.*;
import java.util.List;

public class DelauneyTri {

    public static void main(String[] args) {

        System.out.println("Started...");
        List<Point> pointList = getParsedData();
        System.out.println(pointList.size());
        HashSet<Triangle> triangulation = getTriangulation(pointList);
        triangulation.forEach(System.out::println);
        System.out.println("Finished.");
        HashSet<Edge> ed = new HashSet<DelauneyTri.Edge>();
        for (Triangle triangle : triangulation) {
			ed.add(triangle.e1);
			ed.add(triangle.e2);
			ed.add(triangle.e3);
		}
        
        try {
			FileWriter wd = new FileWriter("C:\\Users\\Trant\\eclipse-workspace\\MaxSupportPath\\src\\output.txt");
			BufferedWriter bf= new BufferedWriter(wd);
			Edge[] edg = new Edge[ed.size()];
			ed.toArray(edg);
			for (Edge edge : edg) {
				bf.write(edge.p1.toString()+";"+edge.p2.toString());
				bf.newLine();
			}
			bf.close();
			wd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
			FileWriter wd = new FileWriter("C:\\Users\\Trant\\eclipse-workspace\\MaxSupportPath\\src\\output1.txt");
			BufferedWriter bf= new BufferedWriter(wd);
			Edge[] edg = new Edge[ed.size()];
			ed.toArray(edg);
			for (Edge edge : edg) {
				bf.write(String.valueOf(edge.p1.getX())+", ");
				bf.write(String.valueOf(edge.p2.getX())+", ");
			}
			bf.newLine();
			for (Edge edge : edg) {
				bf.write(String.valueOf(edge.p1.getY())+", ");
				bf.write(String.valueOf(edge.p2.getY())+", ");
			}
			bf.close();
			wd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    //list point from file
    private static List<Point> getParsedData() {
        List<Point> pointList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("input.txt")))) {
            String line;
            int id = 1;
            while ((line = br.readLine()) != null) {
                String[] coordinatesString = line.split(";");
                Point p = new Point(Double.parseDouble(coordinatesString[0]),
                        Double.parseDouble(coordinatesString[1]),
                        id++);
                pointList.add(p);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return pointList;

    }
    // list triangle
    private static HashSet<Triangle> getTriangulation(List<Point> pointList){
        int triangleID = 1;
        List<Triangle> triangulation = new LinkedList<>();
        double M = getMaximumAbsoluteCoordinate(pointList);
        Triangle superTriangle = new Triangle(new Point(3*M, 0, -1),//-1
                new Point(0,3*M, -2),//-2
                new Point( -3*M, -3*M, -3), -1);//-3
        triangulation.add(superTriangle);
        HashSet<Triangle> solution = new HashSet<>();
        for (Point point : pointList) {
            HashSet<Edge> edge1stAppearance = new HashSet<>(); //integer counts num of same edges
            HashSet<Edge> polygon = new HashSet<>();

            Iterator<Triangle> i = triangulation.iterator();
            while (i.hasNext()) {
                Triangle triangle = i.next();
                if (inCircle(point, triangle.p1, triangle.p2, triangle.p3)) {
                    i.remove();
                    solution.remove(triangle);
                    if (edge1stAppearance.contains(triangle.e1)) {//already appeared - will NOT be in polygon
                        //edge is not shared by any other triangles in badTriangles
                        polygon.remove(triangle.e1);
                    } else {//1st appearance
                        edge1stAppearance.add(triangle.e1);
                        polygon.add(triangle.e1);
                    }

                    if (edge1stAppearance.contains(triangle.e2)) {
                        polygon.remove(triangle.e2);
                    } else {
                        edge1stAppearance.add(triangle.e2);
                        polygon.add(triangle.e2);
                    }

                    if (edge1stAppearance.contains(triangle.e3)) {
                        polygon.remove(triangle.e3);
                    } else {
                        edge1stAppearance.add(triangle.e3);
                        polygon.add(triangle.e3);
                    }
                }
            }

            for (Edge edge : polygon) {
                Triangle newTriangle = new Triangle(point, edge.p1, edge.p2, triangleID++);
                triangulation.add(newTriangle);
                if (hasNoSuperTrianglePoint(newTriangle, superTriangle)) {
                    solution.add(newTriangle);//assume it is solution, if not remove later
                }
            }
        }
        return solution;
    }
    public static boolean hasNoSuperTrianglePoint(Triangle triangle, Triangle superTriangle){
        return  (!triangle.p1.equals(superTriangle.p1) &&
                !triangle.p1 .equals( superTriangle.p2) &&
                !triangle.p1 .equals( superTriangle.p3) &&
                !triangle.p2 .equals( superTriangle.p1) &&
                !triangle.p2 .equals( superTriangle.p2) &&
                !triangle.p2 .equals( superTriangle.p3)&&
                !triangle.p3 .equals( superTriangle.p1) &&
                !triangle.p3 .equals( superTriangle.p2) &&
                !triangle.p3 .equals( superTriangle.p3));
    }

    public static double getMaximumAbsoluteCoordinate(List<Point> pointList) {
        double M = 0.0; //absolute maximum
        for (Point point : pointList) {
            if (Math.abs(point.x) > M) M = Math.abs(point.x);
            if (Math.abs(point.y) > M) M = Math.abs(point.y);
        }
        return M;
    }

    public static boolean inCircle(Point pt, Point v1, Point v2, Point v3) {

        double ax = v1.x;
        double ay = v1.y;
        double bx = v2.x;
        double by = v2.y;
        double cx = v3.x;
        double cy = v3.y;
        double dx = pt.x;
        double dy = pt.y;

        double  ax_ = ax-dx;
        double  ay_ = ay-dy;
        double  bx_ = bx-dx;
        double  by_ = by-dy;
        double  cx_ = cx-dx;
        double  cy_ = cy-dy;
        double det=  (
                (ax_*ax_ + ay_*ay_) * (bx_*cy_-cx_*by_) -
                        (bx_*bx_ + by_*by_) * (ax_*cy_-cx_*ay_) +
                        (cx_*cx_ + cy_*cy_) * (ax_*by_-bx_*ay_)
        );

        if (ccw ( ax,  ay,  bx,  by,  cx,  cy)) {
            return (det>0);
        } else {
            return (det<0);
        }

    }

    private static boolean ccw(double ax, double ay, double bx, double by, double cx, double cy) {//counter-clockwise
        return (bx - ax)*(cy - ay)-(cx - ax)*(by - ay) > 0;
    }

    private static class Triangle{

        Point p1,p2,p3;
        Edge e1, e2, e3;
        int id;

        Triangle(Point p1, Point p2, Point p3, int id)  {
            this.id = id;
            Point[] pointsArray = new Point[]{p1,p2,p3};
            Arrays.sort(pointsArray);
            this.p1 = pointsArray[0];
            this.p2 = pointsArray[1];
            this.p3 = pointsArray[2];

            this.e1 = new Edge(p1, p2, this);
            this.e2 = new Edge(p2, p3, this);
            this.e3 = new Edge(p3, p1, this);
        }

        @Override
        public boolean equals(Object obj) {
            Triangle tr = (Triangle) obj;
            return (tr.p1.equals(this.p1) && tr.p2.equals(this.p2) && tr.p3.equals(this.p3));
        }

        @Override
        public int hashCode() {
            return id;
        }

		@Override
		public String toString() {
			return "Triangle [P"+p1.id+" " + p1 + ",P"+p2.id+" " + p2 + ",P"+p3.id+" " + p3 + "]";
		}

        
    }

    public static class Edge {

        Point p1,p2;
        Triangle t;
        
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
		Edge (Point p1, Point p2, Triangle t){
            this.t = t;
            //from lowest Point.id to highest
            if (p1.id < p2.id){
                this.p1 = p1;
                this.p2 = p2;
            } else if (p1.id > p2.id){
                this.p1 = p2;
                this.p2 = p1;
            }else {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        public double distance(){
            return Math.sqrt(Math.pow((this.p1.x-this.p2.x),2)+Math.pow((this.p1.y-this.p2.y),2));
        }
        @Override
        public boolean equals(Object obj) {

            return ((Edge) obj).p1.equals(this.p1) && ((Edge) obj).p2.equals(this.p2);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 71 * hash + this.p1.hashCode();
            hash = 71 * hash + this.p2.hashCode();
            return hash;

        }

        @Override
        public String toString() {

            return p1+";"+p2;
        }
    }

    private static class Point implements Comparable<Point>{

        double x, y;
        int id;
        
        public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}

		Point(double x, double y, int id) {
            this.x = x;
            this.y = y;
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            Point p = (Point) obj;
            return (p.x == this.x && p.y == this.y);
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public String toString() {
            return x+","+y;
            }

        @Override
        public int compareTo(Point o) {
            if (this.id > o.id ) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}


