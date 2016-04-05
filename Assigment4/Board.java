import edu.princeton.cs.algs4.MinPQ;

public class Board {
    private int N;
    private int[][] board;
    private int[] right_i;
    private int[] right_j;
    
    private int convert(int i, int j) {
        return i * N + j + 1;
    }
    
    public int getBlock(int i, int j) {
        return board[i][j];
    }
    
    public Board(int[][] blocks) {
        N = blocks.length;
        board = blocks.clone();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        right_i = new int[N * N];
        right_j = new int[N * N];
        int i = 0, j = 0;
        for (int k = 0; k < N * N; k++) {
            right_i[k] = i;
            right_j[k] = j;
            j++;
            if (j == N) {
                j = 0;
                i++;
            }                
        }
    }    
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        return N * N;
    }
    // board dimension N
    public int hamming() {
        // number of blocks out of place
        int sum = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] != convert(i, j) && board[i][j] != 0) {
                    sum++;
                }
            }
        }
        return sum;
    }
    
    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        int sum = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                //System.out.println("num is "+board[i][j]);
                if (board[i][j] != convert(i, j) && board[i][j] != 0) {
                    sum+= Math.abs(right_j[board[i][j] - 1] - j) + Math.abs(right_i[board[i][j] - 1] - i);
                }
            }
        }
        return sum;
    }
    
    public boolean isGoal() {
        // is this board the goal board?
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                //System.out.println("num is "+board[i][j]);
                if (board[i][j] != convert(i, j) && board[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }
    /*
    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
    }
    */
    public boolean equals(Object y) {
        // does this board equal y?
        if (this == y) return true;
        if (y instanceof Board) {
            if (this.dimension() == ((Board) y).dimension()) {
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        if (board[i][j] != ((Board) y).getBlock(i,j)) {
                            return false;
                        }
                    }
                }
            return true;    
            }
            else return false;
        } else return false;
    }
    
    public Iterable<Board> neighbors() {
        // all neighboring boards
        
    }
    public String toString() {
        // string representation of this board (in the output format specified below)
    }
    
    public static void main(String[] args) {
        // unit tests (not graded)
        //N = 3;
        int[][] sample = { 
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };
        Board b = new Board(sample);
        //System.out.println(b.dimension());
        System.out.println(b.hamming());
        System.out.println(b.manhattan());
        System.out.println(b.isGoal());
    }
}