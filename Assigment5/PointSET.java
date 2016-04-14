import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;

public class PointSET {
    
    private Set<Point2D> pointSet;
   
    public PointSET() {
        // construct an empty set of points
        pointSet = new TreeSet<>();
    }
    
    public boolean isEmpty() {
        // is the set empty?
        return pointSet.isEmpty();
    }
    
    public int size() {
        // number of points in the set
        return pointSet.size();
    }
    
    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new java.lang.NullPointerException();
        if (!this.contains(p)) {
            pointSet.add(p);
        }
    }
    
    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) throw new java.lang.NullPointerException();
        return pointSet.contains(p);
    }
    
    public void draw() {
        // draw all points to standard draw
        for (Point2D p : pointSet) {
            StdDraw.point(p.x(), p.y());
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle 
        if (rect == null) throw new java.lang.NullPointerException();
        List<Point2D> l = new ArrayList<>();
        for (Point2D p : pointSet) {
            if (rect.contains(p)) {
                l.add(p);
            }
        }
        return l;
    }
    
    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null) throw new java.lang.NullPointerException();
        if (this.isEmpty()) return null;
        Point2D nearestPoint = pointSet.iterator().next();
        double minDist = nearestPoint.distanceSquaredTo(p);
        for (Point2D tp : pointSet) {
            if (tp.distanceSquaredTo(p) < minDist) {
                minDist = tp.distanceSquaredTo(p);
                nearestPoint = tp;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional) 
        PointSET ps = new PointSET();
        ps.insert(new Point2D(1,1));
        ps.insert(new Point2D(2,2));
        ps.insert(new Point2D(2,3));
        System.out.println(ps.nearest(new Point2D(3,3)));
    }
}