import java.awt.Color;
import java.util.ArrayList;
//import java.util.List;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;


public class SeamCarver {
    
    private Picture picture;
    private int W;
    private int H;
    private int N;
    private double matrix[][]; 
    private double transMatrix[][];
    private double[] distTo;
    private ArrayList<Integer>[] pathTo;
    
    @SuppressWarnings("unchecked")
    public SeamCarver(Picture picture) {
        if (picture == null) throw new java.lang.NullPointerException();
        // create a seam carver object based on the given picture
        this.picture = picture;
        H = picture.height();
        W = picture.width();
        N = H * W;
        distTo = new double[N];
        pathTo = (ArrayList<Integer>[]) new ArrayList[N];
        //System.out.println(H);
        //System.out.println(W);
        
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

    private void findDist(int i, int j, double[][] matrix, int W, int H) {
        double startDist = distTo[createIndex(i, j, W)];
        Queue<Integer> q = new Queue<>();
        q.enqueue(createIndex(i, j, W));
        while (!q.isEmpty()) {
            if(i != H - 1) {
                if (j != W) {
                    //System.out.println(matrix[i + 1][j]);
                    //System.out.println(createIndex(i + 1, j, W));
                    if (distTo[createIndex(i + 1, j, W)] > startDist + matrix[i + 1][j]) {
                        distTo[createIndex(i + 1, j, W)] = startDist + matrix[i + 1][j];
                        pathTo[createIndex(i + 1, j, W)] = new ArrayList<Integer>(pathTo[createIndex(i, j, W)]);
                        pathTo[createIndex(i + 1, j, W)].add(j);
                    }
                    findDist(i + 1, j, matrix, W, H);
                }
                if (j != W - 1) {
                    if (distTo[createIndex(i + 1, j + 1, W)] > startDist + matrix[i + 1][j + 1]) {
                        distTo[createIndex(i + 1, j + 1, W)] = startDist + matrix[i + 1][j + 1];
                        pathTo[createIndex(i + 1, j + 1, W)] = new ArrayList<Integer>(pathTo[createIndex(i, j, W)]);
                        pathTo[createIndex(i + 1, j + 1, W)].add(j + 1);
                    }
                    findDist(i + 1, j + 1, matrix, W, H);
                }
                if (j != 0) {
                    if (distTo[createIndex(i + 1, j - 1, W)] > startDist + matrix[i + 1][j - 1]) {
                        distTo[createIndex(i + 1, j - 1, W)] = startDist + matrix[i + 1][j - 1];
                        pathTo[createIndex(i + 1, j - 1, W)] = new ArrayList<Integer>(pathTo[createIndex(i, j, W)]);
                        pathTo[createIndex(i + 1, j - 1, W)].add(j - 1);
                    }
                    findDist(i + 1, j - 1, matrix, W, H);
                }
            }    
        }
        
        /*
        //System.out.println("Dist to " + createIndex(i, j, W) + " is " + startDist);
        if(i != H - 1) {
            if (j != W) {
                //System.out.println(matrix[i + 1][j]);
                //System.out.println(createIndex(i + 1, j, W));
                if (distTo[createIndex(i + 1, j, W)] > startDist + matrix[i + 1][j]) {
                    distTo[createIndex(i + 1, j, W)] = startDist + matrix[i + 1][j];
                    pathTo[createIndex(i + 1, j, W)] = new ArrayList<Integer>(pathTo[createIndex(i, j, W)]);
                    pathTo[createIndex(i + 1, j, W)].add(j);
                }
                findDist(i + 1, j, matrix, W, H);
            }
            if (j != W - 1) {
                if (distTo[createIndex(i + 1, j + 1, W)] > startDist + matrix[i + 1][j + 1]) {
                    distTo[createIndex(i + 1, j + 1, W)] = startDist + matrix[i + 1][j + 1];
                    pathTo[createIndex(i + 1, j + 1, W)] = new ArrayList<Integer>(pathTo[createIndex(i, j, W)]);
                    pathTo[createIndex(i + 1, j + 1, W)].add(j + 1);
                }
                findDist(i + 1, j + 1, matrix, W, H);
            }
            if (j != 0) {
                if (distTo[createIndex(i + 1, j - 1, W)] > startDist + matrix[i + 1][j - 1]) {
                    distTo[createIndex(i + 1, j - 1, W)] = startDist + matrix[i + 1][j - 1];
                    pathTo[createIndex(i + 1, j - 1, W)] = new ArrayList<Integer>(pathTo[createIndex(i, j, W)]);
                    pathTo[createIndex(i + 1, j - 1, W)].add(j - 1);
                }
                findDist(i + 1, j - 1, matrix, W, H);
            }
        }
        */
    }
    
    private int[] findSeam(double[][] matrix, int W, int H) {
        
        for (int v = 0; v < N; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
            pathTo[v] = new ArrayList<>();
        }
        for (int v = 0; v < W; v++) {
            pathTo[v].add(v);
        }
        /*
        distTo[3] = 0;
        pathTo[3].add(3);
        //pathTo[3] = 3;
        findDist(0, 3, matrix, W, H);
        System.out.println(distTo[26]);
        for(Integer i : pathTo[26]) {
            System.out.println(i);
        }
        */
        
        distTo[1] = 1000;
        findDist(0, 1, matrix, W, H);
        
        double minDist = distTo[W * H - W + 1];
        int thatCell = 1;
        
        for (int i = 3; i < W; i = i + 3) {
            distTo[i] = 1000;
            findDist(0, i, matrix, W, H);
            for (int j = W * H - W + 1; j < W * H; j = j + 3) {
                if (minDist > distTo[j]) {
                    minDist = distTo[j];
                    thatCell = j;
                }
            }
            
        }
        
        //System.out.println(minDist);
        int[] path = new int[H];
        int k = 0;
        for(Integer i : pathTo[thatCell]) {
            //System.out.println(i);
            path[k] = i;
            k++;
        }
        
        return path;
    }
    
    public int[] findVerticalSeam() {
        return findSeam(matrix, W, H);
    }
    
    
    public int[] findHorizontalSeam() {
        // sequence of indices for vertical seam
        return findSeam(transMatrix, H, W);
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
        //System.out.println(W);
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
    
    private int createIndex(int i, int j, int N) {
        return i * N + j;
    }
    
    public static void main(String[] args) {
        Picture p = new Picture("12x10.png");
        SeamCarver sc = new SeamCarver(p);
        //for (int k = 0; k < 10; k++) {
            long startTime = System.currentTimeMillis();
            //System.out.println(startTime);
            int seam[] = sc.findVerticalSeam();
            long stopTime = System.currentTimeMillis();
            //System.out.println(stopTime);
            long elapsedTime = stopTime - startTime;
            System.out.println(elapsedTime);
            
           // for (int i = 0; i < seam.length; i++) {
           //     System.out.println(seam[i]);
           // }
            //sc.removeVerticalSeam(seam);
        //}
        
        //sc.picture().show();
    }
}