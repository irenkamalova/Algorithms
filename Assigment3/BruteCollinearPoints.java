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
       //List<Point> l =  Arrays.asList(points);
       List<Point> l =  new ArrayList<Point>(Arrays.asList(points));
       Collections.sort(l);
       for (int i = 0; i < l.size() - 1; i++) {
           //System.out.println(l.get(i));
           if (l.get(i).compareTo(l.get(i + 1)) == 0)
               throw new java.lang.IllegalArgumentException();    
       }
       numberOfSegments = 0;
       for (int i = 0; i < points.length - 3; i++) {
           for (int i1 = i + 1; i1 < points.length - 2; i1++) {
               for (int i2 = i1 + 1; i2 < points.length - 1; i2++) {
                   for (int i3 = i2 + 1; i3 < points.length; i3++) {
                        Point p = l.get(i);
                        Point q = l.get(i1);
                        Point r = l.get(i2);
                        Point s = l.get(i3); 
                        /*
                        System.out.println(p);
                        System.out.println(q);
                        System.out.println(s);
                        System.out.println(r);
                        */
                        if (p == null ||
                            q == null ||
                            s == null ||
                            r == null) throw new java.lang.NullPointerException();
                        double slope1 = p.slopeTo(q);
                        double slope2 = p.slopeTo(s);
                        double slope3 = p.slopeTo(r);
                        if (slope1 == slope2 && slope2 == slope3) {
                            numberOfSegments++;
                            /*
                            System.out.println("-------------" + numberOfSegments + "--------------");
                            System.out.println(p);
                            System.out.println(q);
                            System.out.println(s);
                            System.out.println(r);
                            */
                            linesegments.add(new LineSegment(p, s));
                            
                            
                        }
                   }
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
   
   public static void main(String[] args) {
        
    }
}