import java.util.ArrayList;
import java.util.List;


import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private int N;
    private int[][] board;
    
    public Board(int[][] blocks) {
        N = blocks.length;
        board = new int[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(blocks[i], 0, board[i], 0, N);
        }

    } 

    private int convert(int i, int j) {
        return i * N + j + 1;
    }
    
    //@Override
    //private Object clone() {
    //    Board boardClone = new Board(board);
    //    return boardClone;
    //}
    
    private int getBlock(int i, int j) {
        return board[i][j];
    }
    
    private void setBlock(int i, int j, int value) {
        board[i][j] = value;
    }   
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        return N;
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
        int[] rightI;
        int[] rightJ;       
        rightI = new int[N * N];
        rightJ = new int[N * N];
        int i = 0, j = 0;
        for (int k = 0; k < N * N; k++) {
            rightI[k] = i;
            rightJ[k] = j;
            j++;
            if (j == N) {
                j = 0;
                i++;
            }                
        }
        int sum = 0;
        for (i = 0; i < N; i++) {
            for (j = 0; j < N; j++) {
                //System.out.println("num is "+board[i][j]);
                if (board[i][j] != convert(i, j) && board[i][j] != 0) {
                    sum += Math.abs(rightJ[board[i][j] - 1] - j) + Math.abs(rightI[board[i][j] - 1] - i);
                }
            }
        }
        return sum;
    }
    
    public boolean isGoal() {
        // is this board the goal board?
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] != convert(i, j) && board[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        int i_1 = StdRandom.uniform(0, N);
        int j_1 = StdRandom.uniform(0, N);
        while (board[i_1][j_1] == 0) {
            i_1 = StdRandom.uniform(0, N);
            j_1 = StdRandom.uniform(0, N);
        }
        //System.out.println(i_1 + " " + j_1);
        int i_2 = StdRandom.uniform(0, N);
        int j_2 = StdRandom.uniform(0, N);
        while (board[i_2][j_2] == 0 || board[i_2][j_2] == board[i_1][j_1]) {
            i_2 = StdRandom.uniform(0, N);
            j_2 = StdRandom.uniform(0, N);
        }
        //System.out.println(i_2 + " " + j_2);
        
        Board new_board = new Board(board);
        new_board.setBlock(i_1, j_1, board[i_2][j_2]);
        new_board.setBlock(i_2, j_2, board[i_1][j_1]);
        
        return new_board;
    }
    
    public boolean equals(Object y) {
        // does this board equal y?
        if (this == y) return true;
        if (y == null) return false;
        if (getClass() == y.getClass()) {
            if (this.dimension() == ((Board) y).dimension()) {
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        if (board[i][j] != ((Board) y).getBlock(i, j)) {
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
        List<Board> arr_list = new ArrayList<>();
        int i = 0, j = 0;
        while ((board[i][j] != 0) && (j < N)) {
            i++;
            if (i == N) {
                i = 0;
                j++;
            }
        }
        //System.out.println(i + " " + j);
        if (i != N - 1) {
            Board new_board = new Board(board);
           // System.out.println("1 " + new_board.getBlock(i, j));
            //System.out.println(board[i][j]);
            //System.out.println();
            new_board.setBlock(i, j, board[i + 1][j]);
            //System.out.println(board[i][j]);
           // System.out.println("1 " + new_board.getBlock(i, j));
            //System.out.println("2 " + new_board.getBlock(i + 1, j));
            new_board.setBlock(i + 1, j, board[i][j]);
            //System.out.println("2 " + new_board.getBlock(i + 1, j));
            arr_list.add(new_board);
            //System.out.println(arr_list.get(0));
        }
        if (i != 0) {
            Board new_board = new Board(board);
            new_board.setBlock(i, j, board[i - 1][j]);
            new_board.setBlock(i - 1, j, board[i][j]);
            arr_list.add(new_board);
        }
        if (j != N - 1) {
            Board new_board = new Board(board);
            new_board.setBlock(i, j, board[i][j + 1]);
            new_board.setBlock(i, j + 1, board[i][j]);
            arr_list.add(new_board);
        }
        if (j != 0) {
            Board new_board = new Board(board);
            new_board.setBlock(i, j, board[i][j - 1]);
            new_board.setBlock(i, j - 1, board[i][j]);
            arr_list.add(new_board);
        }
        return arr_list;        
    }
    public String toString() {
        // string representation of this board (in the output format specified below)
        StringBuilder strb = new StringBuilder(4 * N * N);
        strb.append(N).append("\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                strb.append(" ").append(board[i][j]);
            }
            strb.append("\n");
        }
        return strb.toString();
    }
    
    public static void main(String[] args) {
        // unit tests (not graded)
        //N = 3;
        int[][] sample = { 
                {1, 2, 3},
                {4, 0, 6},
                {5, 8, 7}
        };
        Board b = new Board(sample);
        System.out.println(b.toString());
        //List<Board> l = new ArrayList<>();
        Board b2 = b.twin();
        System.out.println(b2.toString());
        
        //System.out.println(b.dimension());
        //System.out.println(b.hamming());
        //System.out.println(b.manhattan());
        //System.out.println(b.isGoal());
    }
}