
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private TwoDTree<edu.princeton.cs.algs4.Point2D> pointSet;

    //@SuppressWarnings("hiding")
    private class TwoDTree<Point2D> {

        private static final boolean RED = true;
        private static final boolean BLACK = false;

        private Node root;     // root of the 2DT       

        private class Node {

            private double key;           // key
            private edu.princeton.cs.algs4.Point2D val;         // associated data
            private Node left, right;  // links to left and right subtrees
            private boolean color;     // color of parent link
            private int N;             // subtree count
            private RectHV rect;

            public Node(edu.princeton.cs.algs4.Point2D p, boolean color, int N,
                    double x1, double y1, double x2, double y2) {
                if (color) {
                    this.key = p.y();
                } else {
                    this.key = p.x();
                }
                this.val = p;
                this.color = color;
                this.N = N;
                this.left = null;
                this.right = null;
            }
        }

        public TwoDTree() {
            root = null;
        }

        public TwoDTree(edu.princeton.cs.algs4.Point2D p) {
            root = new Node(p, false, 1, 0, 0, 1, 1);
        }

        private int size(Node x) {
            if (x == null) {
                return 0;
            }
            return x.N;
        }

        public int size() {
            return size(root);
        }

        public boolean isEmpty() {
            return root == null;
        }

        public void put(edu.princeton.cs.algs4.Point2D p) {
            //System.out.println("Here2");
            /*if (key == null) throw new NullPointerException("first argument to put() is null");
            if (val == null) {
                delete(key);
                return;
            }*/
            if (root == null) {
                root = new Node(p, BLACK, 1);
            } else {
                root = put(root, p, !root.color);
            }
            //root.color = BLACK;
            // assert check();
        }

        private Node put(Node h, edu.princeton.cs.algs4.Point2D p, boolean color) {
            if (h == null) {
                return new Node(p, color, 1);
            }
            if (h.val == p) {
                return h;
            }
            double key;
            if (h.color) {
                key = p.y();
            } else {
                key = p.x();
            }
            if (key < h.key) {
                h.left = put(h.left, p, !h.color);
            } else {
                h.right = put(h.right, p, !h.color);
            }
            // fix-up any right-leaning links
            //if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
            //if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
            //if (isRed(h) && isRed(h.left)  &&  isRed(h.right))     flipColors(h);
            h.N = size(h.left) + size(h.right) + 1;
            return h;
        }

        public boolean contains(edu.princeton.cs.algs4.Point2D p) {
            Node n = root;
            if (n == null) {
                return false;
            }
            double key;
            while (n != null) {
                if (n.val.equals(p)) {
                    return true;
                }
                if (n.color) {
                    key = p.y();
                } else {
                    key = p.x();
                }
                if (key < n.key) {
                    n = n.left;
                } else {
                    n = n.right;
                }
            }
            return false;
        }

        public void draw() {
            Node n = root;
            StdDraw.setPenColor(StdDraw.GREEN);
            StdDraw.setPenRadius(0.02);
            StdDraw.point(n.val.x(), n.val.y());
            draw(n, 0, 1, 0, 1);
        }

        private void draw(Node n, double xBorderDown, double xBorderUp, double yBorderDown, double yBorderUp) {
            //StdDraw.setScale(0, 10);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(n.val.x(), n.val.y());
            StdDraw.setPenRadius(0.002);
            if (!n.color) { // color = red, blue vertical line
                StdDraw.setPenColor(StdDraw.RED);
                //System.out.println("Vert is " + n.val + " " + n.val.x());
                StdDraw.line(n.val.x(), yBorderDown, n.val.x(), yBorderUp);
            } else { // color = black; red horiz line
                //System.out.println("Horiz is " + n.val + " " + n.val.y());
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(xBorderDown, n.val.y(), xBorderUp, n.val.y());
            }
            if (n.left != null) {
                if (!n.color) //color line is red
                {
                    draw(n.left, xBorderDown, n.val.x(), yBorderDown, yBorderUp);
                } else {
                    draw(n.left, xBorderDown, xBorderUp, yBorderDown, n.val.y());
                }
            }
            if (n.right != null) {
                if (!n.color) //color line is red
                {
                    draw(n.right, n.val.x(), xBorderUp, yBorderDown, yBorderUp);
                } else {
                    draw(n.right, xBorderDown, xBorderUp, n.val.y(), yBorderUp);
                }
            }
        }

        public Iterable<edu.princeton.cs.algs4.Point2D> range(RectHV rect) {
            createRectangles(root, 0, 0, 1, 1);
            List<edu.princeton.cs.algs4.Point2D> l = new ArrayList<>();
            //RectHV that = new RectHV()
            //rect.intersects(rect)
            Node h = root;
            range(h, rect, l);
            return l;
        }

        private void range(Node h, RectHV rect, List<edu.princeton.cs.algs4.Point2D> l) {
            if (rect.contains(h.val)) {
                l.add(h.val);
            }
            if (h.left != null && h.left.rect.intersects(rect)) {
                range(h.left, rect, l);
            }
            if (h.right != null && h.right.rect.intersects(rect)) {
                range(h.right, rect, l);
            }
        }

        private void createRectangles(Node h, double x1, double y1,
                double x2, double y2) {
            h.rect = new RectHV(x1, y1, x2, y2);
            //StdDraw.rectangle(x1, y1, x2, y2);
            if (h.left != null) {
                
                if (!h.color) // colore is red
                {
                    createRectangles(h.left, x1, y1, h.val.x(), y2);
                } else {
                    createRectangles(h.left, x1, y1, x2, h.val.y());
                }
            }

            if (h.right != null) {
                if (!h.color) // colore is red
                {
                    createRectangles(h.right, h.val.x(), y1, x2, y2);
                } else {
                    createRectangles(h.right, x1, h.val.y(), x2, y2);
                }
            }
        }

        private class NearestPoint {
            private double minDist;
            private edu.princeton.cs.algs4.Point2D nearestPoint;
            
            NearestPoint(edu.princeton.cs.algs4.Point2D p) {
                minDist = root.val.distanceSquaredTo(p);
                nearestPoint = root.val;
                nearest(root, p);
            }
            
            private void nearest(Node h, edu.princeton.cs.algs4.Point2D p) {
                //StdDraw.setPenColor(Color.GREEN);
                //StdDraw.point(nearestPoint.x(), nearestPoint.y());
                
                if (h.left != null && h.left.rect.distanceSquaredTo(p) < minDist) {
                    double left = h.left.rect.distanceSquaredTo(p);
                    double leftval = h.left.val.distanceSquaredTo(p);
                    
                    if (h.left.val.distanceSquaredTo(p) < minDist) {
                        minDist = h.left.val.distanceSquaredTo(p);
                        nearestPoint = h.left.val;
                    }
                    nearest(h.left, p);
                }
                if (h.right != null && h.right.rect.distanceSquaredTo(p) < minDist) {
                    double right = h.right.rect.distanceSquaredTo(p);
                    double rightval = h.right.val.distanceSquaredTo(p);
                    if (h.right.val.distanceSquaredTo(p) < minDist) {
                        minDist = h.right.val.distanceSquaredTo(p);
                        nearestPoint = h.right.val;
                    }
                    nearest(h.right, p);
                }
                //StdDraw.point(nearestPoint.x(), nearestPoint.y());
    
            }
            
            public edu.princeton.cs.algs4.Point2D getNearestPoint() {
                return nearestPoint;
            }
        };
        
        public edu.princeton.cs.algs4.Point2D nearest(edu.princeton.cs.algs4.Point2D p) {
            createRectangles(root, 0, 0, 1, 1);
            NearestPoint nearestPoint = new NearestPoint(p);
            return nearestPoint.getNearestPoint();
        }
        /*
        private edu.princeton.cs.algs4.Point2D nearest(Node h,
                edu.princeton.cs.algs4.Point2D p, double min, edu.princeton.cs.algs4.Point2D nearestPoint) {
            StdDraw.setPenColor(Color.GREEN);
            StdDraw.point(nearestPoint.x(), nearestPoint.y());
            if (h.left != null && h.left.rect.distanceSquaredTo(p) < min) {
                if (h.left.val.distanceSquaredTo(p) < min) {
                    nearestPoint = h.left.val;
                }
                nearestPoint = nearest(h.left, p, min, nearestPoint);
            }
            if (h.right != null && h.right.rect.distanceSquaredTo(p) < min) {
                if (h.right.val.distanceSquaredTo(p) < min) {
                    nearestPoint = h.right.val;
                }
                nearestPoint = nearest(h.right, p, min, nearestPoint);
            }
            StdDraw.point(nearestPoint.x(), nearestPoint.y());
            return nearestPoint;
        }
        */
    };

    public KdTree() {
        // construct an empty set of points 
        pointSet = new TwoDTree<>();
    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    public int size() {
        return pointSet.size();
    }

    public void insert(Point2D p) {
        if (!pointSet.contains(p))
            pointSet.put(p);
    }

    public boolean contains(Point2D p) {
        return pointSet.contains(p);
    }

    public void draw() {
        // draw all points to standard draw
        pointSet.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle
        if (rect == null) {
            throw new java.lang.NullPointerException();
        }
        return pointSet.range(rect);
    }

    public edu.princeton.cs.algs4.Point2D nearest(edu.princeton.cs.algs4.Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        return pointSet.nearest(p);
    }

    public static void main(String[] args) {

        // unit testing of the methods (optional) 
        //StdDraw.setScale(0, 2);
        KdTree ps = new KdTree();
        Point2D p;
        System.out.println(ps.size());
        ps.insert(new Point2D(0.7, 0.2));
        ps.insert(new Point2D(0.7, 0.2));
        ps.insert(new Point2D(0.5, 0.4));
        ps.insert(new Point2D(0.2, 0.3));
        ps.insert(new Point2D(0.4, 0.7)); //1
        ps.insert(new Point2D(0.9, 0.6));
        ps.insert(new Point2D(0.9, 0.6));
        ps.insert(new Point2D(0.1, 0.5)); //2
        ps.insert(new Point2D(0.15, 0.55));
        ps.insert(new Point2D(0.2, 0.6));
        ps.insert(new Point2D(0.3, 0.7));
        ps.insert(new Point2D(0.4, 0.8));
        ps.insert(new Point2D(0.5, 0.9)); //7
        ps.insert(new Point2D(0.45, 0.8));
        ps.insert(new Point2D(0.35, 0.75));
        ps.insert(new Point2D(0.55, 0.65)); //10
        ps.draw();
        PointSET brute = new PointSET();
        brute.insert(new Point2D(0.7, 0.2));
        brute.insert(new Point2D(0.7, 0.2));
        brute.insert(new Point2D(0.5, 0.4));
        brute.insert(new Point2D(0.2, 0.3));
        brute.insert(new Point2D(0.4, 0.7)); //1
        brute.insert(new Point2D(0.9, 0.6));
        brute.insert(new Point2D(0.9, 0.6));
        brute.insert(new Point2D(0.1, 0.5)); //2
        brute.insert(new Point2D(0.15, 0.55));
        brute.insert(new Point2D(0.2, 0.6));
        brute.insert(new Point2D(0.3, 0.7));
        brute.insert(new Point2D(0.4, 0.8));
        brute.insert(new Point2D(0.5, 0.9)); //7
        brute.insert(new Point2D(0.45, 0.8));
        brute.insert(new Point2D(0.35, 0.75));
        brute.insert(new Point2D(0.55, 0.65)); //10
        
        int i = 0;
        while (i < 100) {
            
            edu.princeton.cs.algs4.Point2D pop = 
                    new edu.princeton.cs.algs4.Point2D(StdRandom.uniform(.0, 1.0), StdRandom.uniform(.0, 1.0));
            //System.out.println(pp);
            //System.out.println(StdRandom.getSeed());
            //StdDraw.setPenRadius(0.01);
            //StdDraw.point(pp.x(), pp.y());
            if (!brute.nearest(pop).equals(ps.nearest(pop))) {
                System.out.println(pop);
            }
            i++;
        }
        
        
        ps.draw();
        /*
        List<edu.princeton.cs.algs4.Point2D> l = new ArrayList<>();
        l = (List) ps.range(new RectHV(0.1, 0.5, 0.6, 0.9));
        int k = 1;
        for (Point2D i : l) {
            System.out.println(k + " " + i);
            k++;
        }
        //System.out.println(ps.isEmpty());
        
        edu.princeton.cs.algs4.Point2D pp = new edu.princeton.cs.algs4.Point2D(0.36, 0.75);
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.point(pp.x(), pp.y());
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.point(ps.nearest(pp).x(), ps.nearest(pp).y());
        
        //edu.princeton.cs.algs4.Point2D Nearest = 
        System.out.println(ps.nearest(pp));
        //
         * 
         */

    }
}