import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BruteCollinearPoints {
    
   private List<LineSegment> linesegments = new ArrayList<>();
   private int numberOfSegments;
   
   public BruteCollinearPoints(Point[] points) {
       // finds all line segments containing 4 points
       if (points == null) throw new java.lang.NullPointerException();
       List<Point> l = Arrays.asList(points);
       Collections.sort(l);
       for (int i = 0; i < l.size() - 1; i++) {
           if (l.get(i) == l.get(i + 1))
               throw new java.lang.IllegalArgumentException();    
       }
       numberOfSegments = 0;
       for (int i = 0; i < points.length; i++) {
           for (int k = i; k < points.length - 3; k++) {
               Point p = l.get(k);
               Point q = l.get(k + 1);
               Point s = l.get(k + 2);
               Point r = l.get(k + 3);
               if( p == null ||
                       q == null ||
                       s == null ||
                       r == null) throw new java.lang.NullPointerException();
               double slope1 = p.slopeTo(q);
               double slope2 = p.slopeTo(s);
               double slope3 = p.slopeTo(r);
               if (slope1 == slope2 && slope2 == slope3) {
                   linesegments.add(new LineSegment(p, s));
                   numberOfSegments++;
               }
           }
       }
   }
   
   public int numberOfSegments() {
       return numberOfSegments;
   } // the number of line segments
   
   public LineSegment[] segments() {
       return linesegments.toArray(new LineSegment[linesegments.size()]);
   } // the line segments
   
}