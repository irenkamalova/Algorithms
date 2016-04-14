import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    
    private static final String Point2D = null;
    private TwoDTree<Point2D> pointSet;

    private class TwoDTree<Point2D> {

        private static final boolean RED   = true;
        private static final boolean BLACK = false;

        private Node root;     // root of the 2DT       
        
        private class Node {
            private double key;           // key
            private edu.princeton.cs.algs4.Point2D val;         // associated data
            private Node left, right;  // links to left and right subtrees
            private boolean color;     // color of parent link
            private int N;             // subtree count

            public Node(edu.princeton.cs.algs4.Point2D p, boolean color, int N) {
                if (color) this.key = p.y();
                else this.key = p.x();
                this.val = p;
                this.color = color;
                this.N = N;
                this.left = null;
                this.right = null;
            }
        }
        
        public TwoDTree() {
        }
        
        public TwoDTree(edu.princeton.cs.algs4.Point2D p) {
            root = new Node(p, false, 1);
        }
        
        private boolean isRed(Node x) {
            if (x == null) return false;
            return x.color == RED;
        }
        
        private int size(Node x) {
            if (x == null) return 0;
            return x.N;
        } 
        
        public int size() {
            return size(root);
        }
        
        public boolean isEmpty() {
            return root == null;
        }
        
        public Point2D get(double key) {
            //if (key == null) throw new NullPointerException("argument to get() is null");
            return get(root, key);
        }
        
        private Point2D get(Node n, double key) {
            while (n != null) {
                if      (key < n.key) n = n.left;
                else if (key > n.key) n = n.right;
                else return (Point2D) n.val;
            }
            return null;
        }
        
        public void put(edu.princeton.cs.algs4.Point2D p) {
            System.out.println("Here2");
            /*if (key == null) throw new NullPointerException("first argument to put() is null");
            if (val == null) {
                delete(key);
                return;
            }*/

            root = put(root, p);
            root.color = BLACK;
           
            // assert check();
        }
        
        private Node put(Node h, edu.princeton.cs.algs4.Point2D p) { 
            if (h == null) return new Node(p, RED, 1); 
            double key;
            if (h.color) key = p.y();
            else key = p.x();
            if      (key < h.key) h.left  = put(h.left,  p); 
            else if (key > h.key) h.right = put(h.right, p); 
            else h.val = p;
            // fix-up any right-leaning links
            if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
            if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
            if (isRed(h) && isRed(h.left)  &&  isRed(h.right))     flipColors(h);
            h.N = size(h.left) + size(h.right) + 1;
            return h;
        }
        
        private Node rotateRight(Node h) {
            // assert (h != null) && isRed(h.left);
            Node x = h.left;
            h.left = x.right;
            x.right = h;
            x.color = x.right.color;
            x.right.color = RED;
            x.N = h.N;
            h.N = size(h.left) + size(h.right) + 1;
            return x;
        }
        
        private Node rotateLeft(Node h) {
            // assert (h != null) && isRed(h.right);
            Node x = h.right;
            h.right = x.left;
            x.left = h;
            x.color = x.left.color;
            x.left.color = RED;
            x.N = h.N;
            h.N = size(h.left) + size(h.right) + 1;
            return x;
        }
        
        private void flipColors(Node h) {
            h.color = !h.color;
            h.left.color = !h.left.color;
            h.right.color = !h.right.color;
        }
        
        public boolean contains(edu.princeton.cs.algs4.Point2D p) {
            Node n = root;
            if (n == null) return false;
            double key;
            if (n.color) key = p.y();
            else key = p.x();
            while (n != null) {
                
                if      (key < n.key) n = n.left;
                else if (key > n.key) n = n.right;
                else return true;
            }
            return false;
        }
        
        public void draw() {
            Node n = root;
            draw(n);
        }
        
        private void draw(Node n) {
            //StdDraw.setScale(0, 10);
            
                if (n.left != null)
                    draw(n.left);
                if (n.right != null)
                    draw(n.right);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.02);
                StdDraw.point(n.val.x(), n.val.y());
                StdDraw.setPenRadius(0.002);
                if (!n.color) { // color = red, blue vertical line
                    StdDraw.setPenColor(StdDraw.RED);
                    System.out.println("Vert is " + n.val + " " + n.val.x());
                    StdDraw.line(n.val.x(), 0, n.val.x(), 10);
                }
                else { // color = black; red horiz line
                    System.out.println("Horiz is " + n.val + " " + n.val.y());
                    StdDraw.setPenColor(StdDraw.BLUE);
                    StdDraw.line(0, n.val.y(), 10, n.val.y());
                }

            
        }
    };
    
    public KdTree() {
        // construct an empty set of points 
        TwoDTree<Point2D> pointSet = new TwoDTree<>();
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
        System.out.println("Here1");
        if (pointSet == null) {
            pointSet = new TwoDTree<>(p);
        }
        else pointSet.put(p);
    }
    public boolean contains(Point2D p) {
        // does the set contain point p?
        return pointSet.contains(p);
    }
    public void draw() {
        // draw all points to standard draw
        pointSet.draw();
    }
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle
        return null;
    }
    
    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        return null;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional) 
        KdTree ps = new KdTree();
        StdDraw.square(1, 1, 1);
        ps.insert(new Point2D(0.206107, 0.095492));

        ps.insert(new Point2D(0.975528, 0.654508));

        ps.insert(new Point2D(0.024472, 0.345492));

        ps.insert(new Point2D(0.793893, 0.095492));

        ps.insert(new Point2D(0.793893, 0.904508));

        ps.insert(new Point2D(0.975528, 0.345492));

        ps.insert(new Point2D(0.206107, 0.904508));

        ps.insert(new Point2D(0.500000, 0.000000));

        ps.insert(new Point2D(0.024472, 0.654508));

        ps.insert(new Point2D(0.500000, 1.000000));
        ps.draw();
    }
 }