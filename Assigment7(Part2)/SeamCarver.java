import java.awt.Color;
import java.util.ArrayList;
//import java.util.List;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;


public class SeamCarver {
    
    private Picture picture;
    private int W;
    private int H;
    
    
    public SeamCarver(Picture picture) {
        if (picture == null) throw new java.lang.NullPointerException();
        // create a seam carver object based on the given picture
        this.picture = new Picture(picture);
        //picture.set(0, 0, Color.black);
        //this.picture.set(0, 1, Color.black);
        H = picture.height();
        W = picture.width(); 
        //System.out.println(H);
        //System.out.println(W);
        

    }
   
    public Picture picture() {
        // current picture
        Picture p = new Picture(picture);
        return p;
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
    
    private class Pixel {
    	private final int x;
    	private final int y;
    	//private double weight;
    	private boolean checked;
    	
    	public Pixel(int x, int y) {
    		checked = false;
    		this.x = x;
    		this.y = y;
    		//this.weight = w;
    	}
    	
    	public boolean checked() {
    		return checked;
    	}
    	
    	public void check() {
    		checked = true;
    	}
    	
    	public void uncheck() {
    		checked = false;
    	}
    	
    	public int getX() {
    		return x;
    	}
    	
    	public int getY() {
    		return y;
    	}
    }
    
    private int[] findSeam(double[][] matrix, int W, int H) {
    	double[] distTo = new double[H * W];
    	int[] pathTo = new int[H * W];
    	ArrayList<Pixel> pixels = new ArrayList<>();
        for (int v = 0; v < H * W; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
            pathTo[v] = 0;
        }
        for (int v = 0; v < W; v++) {
        	distTo[v] = 1000;
            //pathTo[v].add(v);
        }
        pixels.clear();
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
            	pixels.add(new Pixel(i, j));
                //System.out.print((int)matrix[i][j] + " ");
            }
            //System.out.println();
        }
        
        
        Queue<Pixel> q = new Queue<>();
        int i = 0;
        int j = 1;
        for (; j < W; j = j + 3) {
        	//System.out.println(j + " " + i);
        	q.enqueue(pixels.get(createIndex(i, j, W)));
        }
        
        while (!q.isEmpty()) {
        	Pixel currPixel = q.dequeue();
        	currPixel.uncheck();
        	i = currPixel.getX();
        	j = currPixel.getY();
                int currPosition = createIndex(i, j, W);
        	double startDist = distTo[currPosition];
        	//System.out.println(matrix[i][j] + " stDist is " + startDist);
            if (i != H - 1) {
            	if (j != 0) {
            		//System.out.print(matrix[i + 1][j - 1] + " -- ");
                    int index = createIndex(i + 1, j - 1, W);
                    if (distTo[index] > startDist + matrix[i + 1][j - 1]) {
                        distTo[index] = startDist + matrix[i + 1][j - 1];
                        pathTo[index] = currPosition;
                        if (!pixels.get(index).checked()) {
                        	q.enqueue(pixels.get(createIndex(i + 1, j - 1, W)));
                        	pixels.get(index).check();
                        }
                    }
                }
                if (j != W) {
                	//System.out.print(matrix[i + 1][j] + " -- ");
                    //System.out.println(createIndex(i + 1, j, W));
                    int index = createIndex(i + 1, j, W);
                    if (distTo[index] > startDist + matrix[i + 1][j]) {
                        distTo[index] = startDist + matrix[i + 1][j];
                        pathTo[index] = currPosition;
                        if (!pixels.get(index).checked()) {
                        	q.enqueue(pixels.get(index));
                        	pixels.get(index).check();
                        }
                    }
                }
                if (j != W - 1) {
                	//System.out.print(matrix[i + 1][j + 1]);
                    int index = createIndex(i + 1, j + 1, W);
                    if (distTo[index] > startDist + matrix[i + 1][j + 1]) {
                        distTo[index] = startDist + matrix[i + 1][j + 1];
                        pathTo[index] = currPosition;
                        if (!pixels.get(index).checked()) {
                        	q.enqueue(pixels.get(index));
                        }
                    }
                }
                //System.out.println();
            }    
        }
        
        /*
        distTo[3] = 1000;
        findDist(0, 3, matrix, W, H);
        System.out.println(distTo[26]);
        for(Integer i : pathTo[26]) {
            System.out.println(i);
        }
        System.out.println();
        */
        ///*
        if (W == 1) {
                int[] path = new int[H];
                for (int k = 0; k < H; k++) {
                    path[k] = 0;
                }
                return path;
        }
        else {
            int thatCell;
            double minDist = distTo[W * H - W + 1];
            thatCell = W * H - W + 1;

            for (j = W * H - W + 1; j < W * H; j = j + 3) {
                    //System.out.println("j is " + j);
                if (minDist > distTo[j]) {
                    minDist = distTo[j];
                    thatCell = j;
                }
            }

            //System.out.println(minDist);
            //System.out.println("thatCell is " + thatCell);
            int[] path = new int[H];
            //int k = 0;
            for (int k = H - 1; k >= 0; k--) {
                //System.out.println(i);
                path[k] = thatCell - W * k;
                thatCell = pathTo[thatCell];
                //k++;
            }

            return path;
        }
        //*/
        //return null;
    }
    
    public int[] findVerticalSeam() {
        double[][] matrix = new double[H][W];
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                double e = energy(j, i);
                matrix[i][j] = e;
                
                //System.out.print((int)matrix[i][j] + " ");
            }
            //System.out.println();
        }
        return findSeam(matrix, W, H);
    }
    
    
    public int[] findHorizontalSeam() {
        // sequence of indices for vertical seam
        double[][] transMatrix = new double[W][H];
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                double e = energy(j, i);
                transMatrix[j][i] = e;
                
                //System.out.print((int)matrix[i][j] + " ");
            }
            //System.out.println();
        }
        return findSeam(transMatrix, H, W);
    }
    
    private void removeSeam(int[] seam, int W, int H, boolean isVertical) {
        if (picture == null || seam == null) throw new java.lang.NullPointerException();
        if (W <= 1 || seam.length != H || !seamIsCorrect(seam, W)) throw new java.lang.IllegalArgumentException();
        Picture newPict;
        if (isVertical) {
            newPict = new Picture(W - 1, H);
        } else {
            newPict = new Picture(H, W - 1);
        }
        for (int col = 0; col < H; col++) {
            int newrow = 0;
            for (int row = 0; row < W; row++) {
                if (row != seam[col]) {
                    if (isVertical) {
                        Color c = picture.get(row, col);
                        newPict.set(newrow, col, c);
                    }
                    else {
                        Color c = picture.get(col, row);
                        newPict.set(col, newrow, c);						
                    }
                    newrow++;
                }
            }
        }
        picture = newPict;
        if (isVertical) {
            this.W = this.W - 1;
        } else {
            this.H = this.H - 1;
        }
    }
    
    private boolean seamIsCorrect(int[] seam, int bound) {
    	if (seam[0] < 0) return false; 
    	for (int i = 1; i < seam.length; i++) {
    		if (seam[i] < 0) return false;
    		if (seam[i] > bound - 1) return false;
    		if (Math.abs((seam[i - 1] - seam[i])) > 1) return false;
    	}
    	return true;
    }
    
    public void removeVerticalSeam(int[] seam) {
        removeSeam(seam, W, H, true);
    }
    
    public void removeHorizontalSeam(int[] seam) {
        removeSeam(seam, H, W, false);
    }
    
    private int createIndex(int i, int j, int N) {
        return i * N + j;
    }
    
    public static void main(String[] args) {
        Picture p = new Picture("chameleon.png");
        SeamCarver sc = new SeamCarver(p);
        //for (int k = 0; k < 10; k++) {
            long startTime = System.currentTimeMillis();
            //System.out.println(startTime);
            for (int i = 0; i < 100; i++) {
                int[] seam = sc.findVerticalSeam();
                sc.removeVerticalSeam(seam);
            }
            long stopTime = System.currentTimeMillis();
            //System.out.println(stopTime);
            long elapsedTime = stopTime - startTime;
            System.out.println("Time is " + elapsedTime);
            
            //for (int i = 0; i < seam.length; i++) {
            //    System.out.println(seam[i]);
            //}
            //sc.removeVerticalSeam(seam);
            //seam = sc.findVerticalSeam();
            //for (int i = 0; i < seam.length; i++) {
            //    System.out.println(seam[i]);
            //}
            //sc.removeVerticalSeam(seam);
        //}
        
        sc.picture().show();
    }
}