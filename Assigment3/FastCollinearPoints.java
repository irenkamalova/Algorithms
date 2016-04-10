import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FastCollinearPoints {
    
    private List<LineSegment> linesegments = new ArrayList<>();
    private int numberOfSegments;
    
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.NullPointerException();
        List<Point> l =  new ArrayList<Point>(Arrays.asList(points));
        
        Collections.sort(l);
        for (int i = 0; i < l.size() - 1; i++) {
           //System.out.println(l.get(i));
           if (l.get(i).compareTo(l.get(i + 1)) == 0)
               throw new java.lang.IllegalArgumentException();    
       }
        numberOfSegments = 0;
        Map<Double, List<Point>> slop_point = new HashMap<>();
        
        for (int i = 0; i < l.size(); i++) {
            Map<Double, List<Point>> slopes = new HashMap<>();
            Point p = l.get(i);    
            for (int k = i + 1; k < l.size(); k++) {
                Point c = l.get(k);
                double cur_slope = p.slopeTo(l.get(k));
                if (slopes.get(cur_slope) == null) {
                    List<Point> cur_list = new ArrayList<>();
                    cur_list.add(p);
                    cur_list.add(l.get(k));
                    slopes.put(cur_slope, cur_list);
                } else {
                    slopes.get(cur_slope).add(l.get(k));
                }
            }
            for (Entry<Double, List<Point>> m: slopes.entrySet()) {
                //System.out.println(m.size());
                if (m.getValue().size() >= 4) {
                    if (slop_point.get(m.getKey()) == null) {
                        //System.out.println("size: " + m.getValue().size());
                        //System.out.println("first: " + m.getValue().first());
                        //System.out.println("last: " + m.getValue().last());
                        //System.out.println(p);
                        linesegments.add(new LineSegment(m.getValue().get(0), m.getValue().get(m.getValue().size() - 1)));
                        List<Point> cur_points = new  ArrayList<>();
                        cur_points.add(m.getValue().get(m.getValue().size() - 1));
                        slop_point.put(m.getKey(), cur_points);
                        numberOfSegments++; 
                    }
                    else if (!slop_point.get(m.getKey()).contains(m.getValue().get(m.getValue().size() - 1))) {
                        linesegments.add(new LineSegment(m.getValue().get(0), m.getValue().get(m.getValue().size() - 1)));
                        slop_point.get(m.getKey()).add(m.getValue().get(m.getValue().size() - 1));
                        numberOfSegments++;
                    }
                }
            }
        }
    }     // finds all line segments containing 4 or more points
   
    public int numberOfSegments() {
        return numberOfSegments;
    }        // the number of line segments
   
    public LineSegment[] segments() {
        return linesegments.toArray(new LineSegment[linesegments.size()]);
    }                // the line segments
       public static void main(String[] args) {
           
           List<Point> points = new ArrayList<Point>();
           /* {{
            add(new Point(810000, 0));
            add(new Point(10000, 0));          
            add(new Point(3000, 7000));        
            add(new Point(7000, 3000));        
            add(new Point(20000, 21000));      
            add(new Point(3000, 4000));        
            add(new Point(14000, 15000));      
            add(new Point(6000, 7000));      
           }} ;
        */  
        for (int i = 0; i < 4; i++) {
            Point p = new Point(i, i);
            points.add(p);
            
            p = new Point(i + 1, i);
            points.add(p);
            
            p = new Point(i, i + 1);
            System.out.println(p);
            points.add(p);
            
        }
        points.add(new Point(4, 4));
        FastCollinearPoints b = new FastCollinearPoints(points.toArray(new Point[points.size()]));
        //for (int i = 0; i < points.size(); i++) {
        //    System.out.println(points.get(i));
        //}
        System.out.println(b.numberOfSegments());
        LineSegment[] segments = b.segments();
        for (int i = 0; i < segments.length; i++) {
            System.out.println(segments[i]);
        }
    }
}