import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int count; // number of components
    private int[] flag; // is 0 if closed, is 1 if open
    private int pTop;
    private int pDown;
    private WeightedQuickUnionUF wQuick;
    private int N;

    public Percolation(int N) { // create N-by-N grid, with all sites blocked
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.N = N;
        count = N * N + 2;
        wQuick = new WeightedQuickUnionUF(count);
        // System.out.println("Parent 1 is "+wQuick.find(1));
        // System.out.println("Parent 2 is "+wQuick.find(2));
        flag = new int[count];
        for (int i = 1; i < count - 1; i++) {
            flag[i] = 0;
        }
        pTop = 0;
        pDown = count - 1;
        flag[pTop] = 1;
        flag[pDown] = 1;
        for (int j = 1; j <= N; j++) {
            wQuick.union(pTop, j);
            // System.out.println("Parent "+j+" is "+wQuick.find(j));
        }
        
            for (int j = pDown - N; j < pDown; j++) {
                 //System.out.println(j);
                 //System.out.println("Parent "+j+" is "+wQuick.find(j));
                
                wQuick.union(pDown, j);
              //  System.out.println("Parent "+j+" is "+wQuick.find(j));
            }
          // System.out.println("Parent "+pDown+" is "+wQuick.find(pDown));
    }

    private int convert(int i, int j) {
        return (i - 1) * N + j;
    }

    private void checkBound(int i, int j) {
        if (i <= 0 || i > N || j <= 0 || j > N) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    public void open(int i, int j) {
        // System.out.println("In open: i = "+i + " j = " + j);
        checkBound(i, j);

        if (!isOpen(i, j)) {
            int grid = convert(i, j);
            // System.out.println(grid);
            flag[grid] = 1;
            if (i != N && isOpen(i + 1, j))
                wQuick.union(grid, convert(i + 1, j));

            if (i != 1 && isOpen(i - 1, j))
                wQuick.union(grid, convert(i - 1, j));

            if (j != N && isOpen(i, j + 1))
                wQuick.union(grid, convert(i, j + 1));

            if (j != 1 && isOpen(i, j - 1))
                wQuick.union(grid, convert(i, j - 1));
        }
        // System.out.println("End open");
    } // open site (row i, column j) if it is not open already

    public boolean isOpen(int i, int j) {
        checkBound(i, j);
        int grid = convert(i, j);
        return flag[grid] == 1;
    } // is site (row i, column j) open?

    public boolean isFull(int i, int j) {
        checkBound(i, j);
        if (!isOpen(i, j))
            return false;
        int grid = convert(i, j);
        //System.out.println(grid + " " + pTop);
        if(grid >= pDown - N) {
            boolean connected = false;
            boolean changed = false;
            if (i != N) {                
                connected = isOpen(i + 1, j);
                changed = true;
            }
            if (i != 1) {
                connected |= isOpen(i - 1, j);
                changed = true;
            }
            if (j != 1) {
                connected |= isOpen(i, j - 1);
                changed = true;
            }
            if (changed && !connected) 
                return false;
        }
        return wQuick.connected(grid, pTop);
    } // is site (row i, column j) full?

    public boolean percolates() {
        if( N == 1) 
            return isOpen(1, 1);
        
        int grid = convert(N + 1, 1);
        //System.out.println(grid + " " + pTop);
//        System.out.println("grid p is" + wQuick.find(grid) + " pTop p is " + wQuick.find(pTop));
        return wQuick.connected(grid, pTop);
    } // does the system percolate?

    public static void main(String[] args) {

        Percolation p1 = new Percolation(3);
        p1.open(3, 1);
        p1.open(2, 1);
        p1.open(1, 1);
        p1.percolates();

        /* System.out.println(p1.isFull(2, 1));
        // p1.open(2, 1);
        // System.out.println(p1.percolates());
        
         * p1.open(1, 5); System.out.println(p1.percolates());
         * System.out.println(p1.isFull(1, 2)); p1.open(1, 2);
         * System.out.println(p1.percolates()); System.out.println(p1.isFull(1,
         * 2)); p1.open(5, 5); System.out.println(p1.percolates()); p1.open(4,
         * 5); System.out.println(p1.percolates()); p1.open(3, 5);
         * System.out.println(p1.percolates()); p1.open(2, 5);
         * System.out.println(p1.percolates());
         */
    } // test client (optional)

}