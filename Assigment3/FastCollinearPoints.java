import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastCollinearPoints {
    
    private List<LineSegment> linesegments = new ArrayList<>();
    private int numberOfSegments;
    
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.NullPointerException();
        List<Point> l = Arrays.asList(points);
        
        Collections.sort(l);
        for (int i = 0; i < l.size() - 1; i++) {
            if (l.get(i) == l.get(i + 1))
                throw new java.lang.IllegalArgumentException();    
        }
        numberOfSegments = 0;
        for (int i = 0; i < l.size(); i++) {
            Point p = l.get(i);
            Map<Double, List<Point>> slopes = new HashMap<>(); 
            for (int k = i + 1; k < l.size(); k++) {
                double cur_slope = p.slopeTo(l.get(k));
                if (slopes.get(cur_slope) == null) {
                    List<Point> cur_list = new ArrayList<>();
                    cur_list.add(l.get(k));
                    slopes.put(cur_slope, cur_list);
                } else {
                    slopes.get(cur_slope).add(l.get(k));
                }
            }
            
            for (List<Point> m: slopes.values()) {
                //System.out.println(m.size());
                if (m.size() >= 4) {
                    //System.out.println(m.get(m.size() - 1));
                    //System.out.println(p);
                    linesegments.add(new LineSegment(p, m.get(m.size() - 1)));
                    numberOfSegments++;                    
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
    
}
