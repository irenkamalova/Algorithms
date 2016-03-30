import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int T;
    private double[] x;

    public PercolationStats(int N, int T) {
        if (T <= 0 || N <= 0)
            throw new java.lang.IllegalArgumentException();
        this.T = T;
        x = new double[T];
        for (int k = 0; k < T; k++) {
            Percolation perc = new Percolation(N);
            int counter = 0;
            while (!perc.percolates() && counter < N * N) {
                int i = StdRandom.uniform(N);
                int j = StdRandom.uniform(N);
                if (!perc.isOpen(i + 1, j + 1)) {
                    counter++;
                    perc.open(i + 1, j + 1);
                }

            }
            x[k] = ((double) counter) / (N * N);
        }
    }

    // perform T independent experiments on an N-by-N grid

    public double mean() {
        return StdStats.mean(x);
    }

    // sample mean of percolation threshold

    public double stddev() {
        return StdStats.stddev(x);
    }

    // sample standard deviation of percolation threshold

    public double confidenceLo() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    // low endpoint of 95% confidence interval

    public double confidenceHi() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(N, T);
        System.out.println("mean = " + ps.mean());
        System.out.println("stddev = " + ps.stddev());
        double cLo = ps.confidenceLo();
        double cHi = ps.confidenceHi();
        System.out.println(cLo + " " + cHi);
    }
    // test client (described below)
}