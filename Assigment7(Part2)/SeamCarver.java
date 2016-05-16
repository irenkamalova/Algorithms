import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.Picture;


public class SeamCarver {
    
    private Picture picture;
    private int W;
    private int H;
    private double matrix[][]; 
    private double transMatrix[][];
    
    public SeamCarver(Picture picture) {
        if (picture == null) throw new java.lang.NullPointerException();
        // create a seam carver object based on the given picture
        this.picture = picture;
        H = picture.height();
        W = picture.width();
        
        matrix = new double[H][W];
        transMatrix = new double[W][H];
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                double e = energy(j, i);
                matrix[i][j] = e;
                transMatrix[j][i] = e;
                //System.out.print((int)matrix[i][j] + " ");
            }
            //System.out.println();
        }
    }
   
    public Picture picture() {
        // current picture
        return picture;
    }
    
    public int width() {
        // width of current picture
        return W;
    }
    
    public int height() {
        // height of current picture
        return H;
    }
    
    public double energy(int x, int y) {
        if (x < 0 || x > W - 1 || y < 0 || y > H - 1)
            throw new java.lang.IndexOutOfBoundsException();
        // energy of pixel at column x and row y
        if (x == 0 || y == 0 || x == W - 1 || y == H - 1) {
            return 1000;
        }
        Color leftPoint = picture.get(x - 1, y);
        Color rigthPoint = picture.get(x + 1, y);
        
        int rX = rigthPoint.getRed() - leftPoint.getRed();
        int gX = rigthPoint.getGreen() - leftPoint.getGreen();
        int bX = rigthPoint.getBlue() - leftPoint.getBlue();
        
        double deltaX = rX * rX + gX * gX + bX * bX;
        
        Color upPoint = picture.get(x, y + 1);
        Color downPoint = picture.get(x, y - 1);
        
        int rY = upPoint.getRed() - downPoint.getRed();
        int gY = upPoint.getGreen() - downPoint.getGreen();
        int bY = upPoint.getBlue() - downPoint.getBlue();
        
        double deltaY = rY * rY + gY * gY + bY * bY;
        
        return Math.sqrt(deltaX + deltaY);
    }
    
    public int[] findVerticalSeam() {
        // sequence of indices for horizontal seam
        //create 2D matrix
        
        MatrixSP msp = new MatrixSP(matrix, W, H, 0);
        double minDist = msp.distTo(W * H - W + 1);
        int path[] = msp.pathTo(W * H - W + 1);
        
        for (int i = 3; i < W; i = i + 3) {
            msp = new MatrixSP(matrix, W, H, i);
            for (int j = W * H - W + 1; j < W * H; j = j + 3) {
                if (minDist > msp.distTo(j)) {
                    minDist = msp.distTo(j);
                    path = msp.pathTo(j);
                }
            }
            
        }
        //System.out.println(minDist);
        //DirectedEdge de = new DirectedEdge(v, w, weight)
        return path;
    }
    
    public   int[] findHorizontalSeam() {
        // sequence of indices for vertical seam
        
        MatrixSP msp = new MatrixSP(transMatrix, H, W, 0);
        double minDist = msp.distTo(H * W - H + 1);
        int path[] = msp.pathTo(H * W - H + 1);
        
        for (int i = 3; i < H; i = i + 3) {
            msp = new MatrixSP(transMatrix, H, W, i);
            for (int j = H * W - H + 1; j < H * W; j = j + 3) {
                if (minDist > msp.distTo(j)) {
                    minDist = msp.distTo(j);
                    path = msp.pathTo(j);
                }
            }
            
        }
        //System.out.println(minDist);
        //DirectedEdge de = new DirectedEdge(v, w, weight)
        return path;
    }
    
    public void removeHorizontalSeam(int[] seam) {
        // remove horizontal seam from current picture
        if (picture == null) throw new java.lang.NullPointerException();
        if (H <= 1 || seam == null || seam.length != W) throw new java.lang.IllegalArgumentException();
        Picture newPict = new Picture(W, H - 1);
        
        for (int col = 0; col < W; col++) {
            int newrow = 0;
            for (int row = 0; row < H; row++) {
                //System.out.print( "(" + col + "," + row + ") ");
                if (row != seam[col]) {
                    Color c = picture.get(col, row);
                    newPict.set(col, newrow, c);
                    newrow++;
                }
                else {
                    //System.out.print("Row is " + row);
                }
            }
            //System.out.println();
        }
        picture = newPict;

    }
    
    public    void removeVerticalSeam(int[] seam) {
        // remove vertical seam from current picture
        if (picture == null) throw new java.lang.NullPointerException();
        if (W <= 1 || seam == null || seam.length != H) throw new java.lang.IllegalArgumentException();
        //W = W - 1;
        //System.out.println("W is " + W);
        //System.out.println("H is " + H);
        Picture newPict = new Picture(W - 1, H);
        
        for (int col = 0; col < H; col++) {
            int newrow = 0;
            for (int row = 0; row < W; row++) {
                //System.out.print( "(" + col + "," + row + ") ");
                if (row != seam[col]) {
                    Color c = picture.get(row, col);
                    newPict.set(newrow, col, c);
                    newrow++;
                }
                else {
                    //System.out.print("Row is " + row);
                }
            }
            //System.out.println();
        }
        picture = newPict;
        
    }

    private class MatrixSP {
        
        private double[] distTo;         // distTo[v] = distance  of shortest s->v path
        private DirectedEdge[] edgeTo;   // edgeTo[v] = last edge on shortest s->v path
        private int s;
        private int height;
        private int weight;
        public MatrixSP(double[][] matrix, int W, int H, int s) {
            this.s = s;
            height = H;
            weight = W;
            List<DirectedEdge> edges = new ArrayList<>();
            int N = W * H;
            for (int i = 0; i < H - 1; i++) {
                for (int j = 0; j < W - 1; j++) {
                    int x = createIndex(i, j, W);
                    edges.add(new DirectedEdge(x, createIndex(i + 1, j, W), matrix[i + 1][j]));
                    //System.out.println("From " + x + " to " + createIndex(i + 1, j, W) + " weight " + matrix[i + 1][j]);
                    if (j != 0) {
                        edges.add(new DirectedEdge(x, createIndex(i + 1, j - 1, W), matrix[i + 1][j - 1]));
                        //System.out.println("From " + x + " to " + createIndex(i + 1, j - 1, W) + " weight " + matrix[i + 1][j - 1]);
                    }
                    if (j != W - 1) {
                        edges.add(new DirectedEdge(x, createIndex(i + 1, j + 1, W), matrix[i + 1][j + 1]));
                    //System.out.println("From " + x + " to " + createIndex(i + 1, j + 1, W) + " weight " + matrix[i + 1][j + 1]);
                    }
                }
            }
            edges.toString();
            
            distTo = new double[N];
            edgeTo = new DirectedEdge[N];
            for (int v = 0; v < N; v++)
                distTo[v] = Double.POSITIVE_INFINITY;
            distTo[s] = 0.0;

            // visit vertices in toplogical order
            for (DirectedEdge e : edges) {
                    relax(e);
            }
            //*/
        }
        
        private int createIndex(int i, int j, int N) {
            return i * N + j;
        }

        // relax edge e
        private void relax(DirectedEdge e) {
            int v = e.from(), w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
            }       
        }

        public double distTo(int v) {
            return distTo[v] + 1000;
        }

        public boolean hasPathTo(int v) {
            return distTo[v] < Double.POSITIVE_INFINITY;
        }

        public int[] pathTo(int v) {
            if (!hasPathTo(v)) return null;
            int path[] = new int[height];
            int i = height - 1;
            //System.out.println("i is " + i + " W is " + W);
            for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
                path[i] = e.to() - i * weight;
                i--;
            }
            path[0] = s - i * weight;
            return path;
        }

    }
    
    public static void main(String[] args) {
        Picture p = new Picture("6x5.png");
        SeamCarver sc = new SeamCarver(p);
        //for (int k = 0; k < 10; k++) {
            int seam[] = sc.findVerticalSeam();
            sc.removeVerticalSeam(seam);
        //}
        sc.picture().show();
    }
}