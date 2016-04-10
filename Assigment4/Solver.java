import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver { 
    
    private int numberOfMoves;
    private List<Board> solution;
    private boolean isSol;
    
    private class SearchNode {
        private Board board;
        private int moves;
        private SearchNode previousNode;
        
        public SearchNode(Board board, int moves, SearchNode previousNode) {
            this.board = board;
            this.moves = moves;
            this.previousNode = previousNode;
        }
        
        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public SearchNode getPreviousNode() {
            return previousNode;
        }
    }
    
    private class ComparatorBoardManhattan implements Comparator<SearchNode> {        
        public int compare(SearchNode s1, SearchNode s2) {
            int priority1 = s1.getMoves() + s1.getBoard().manhattan();
            int priority2 = s2.getMoves() + s2.getBoard().manhattan();
            if (priority1 > priority2) return 1;
            if (priority1 < priority2) return -1;
            return 0;
        }
    }
    
    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        if (initial == null) throw new java.lang.NullPointerException();
        ComparatorBoardManhattan cbm = new ComparatorBoardManhattan();
        MinPQ<SearchNode> m_pq1 = new MinPQ<>(cbm);
        MinPQ<SearchNode> m_pq2 = new MinPQ<>(cbm);
        //System.out.println(initial.toString());
        Board copy_board = initial;
        Board twin = copy_board.twin();
        
        SearchNode stepNode1 = new SearchNode(copy_board, 0, null);
        SearchNode stepNode2 = new SearchNode(twin, 0, null);
        
        m_pq1.insert(stepNode1);
        m_pq2.insert(stepNode2);
        
        while (!stepNode1.getBoard().isGoal() && !stepNode2.getBoard().isGoal()) {
            //List<Board> l1 = (List<Board>) stepNode1.getBoard().neighbors();
            Iterable<Board> iterBoard1 = stepNode1.getBoard().neighbors();
            for (Board l1 : iterBoard1) {
                if (stepNode1.getPreviousNode() == null || !l1.equals(stepNode1.getPreviousNode().getBoard())) {
                    m_pq1.insert(new SearchNode(l1, stepNode1.getMoves() + 1, stepNode1));
                }
            }
            stepNode1 = m_pq1.min();
            m_pq1.delMin();
            
            //List<Board> l2 = (List<Board>) stepNode2.getBoard().neighbors();
            Iterable<Board> iterBoard2 = stepNode2.getBoard().neighbors();
            for (Board l2 : iterBoard2) {
                if (stepNode2.getPreviousNode() == null || !l2.equals(stepNode2.getPreviousNode().getBoard())) {
                    m_pq2.insert(new SearchNode(l2, stepNode2.getMoves() + 1, stepNode2));
                }
            }
            stepNode2 = m_pq2.min();
            m_pq2.delMin();
        }
        
        if (stepNode1.getBoard().isGoal()) {
            isSol = true;
            numberOfMoves = stepNode1.getMoves();
            solution = new ArrayList<>(stepNode1.getMoves());
            
            for (int k = 0; k < numberOfMoves + 1; k++) {
                //System.out.println(stepNode1.getBoard());
                solution.add(stepNode1.getBoard());
                stepNode1 = stepNode1.getPreviousNode();
            }
        } else {
            isSol = false;
            numberOfMoves = -1;
            solution = null;
        }
        
    }
    public boolean isSolvable()  {
        // is the initial board solvable?
        return isSol;
    }
    public int moves() {
        if (!isSolvable()) return -1;
        return numberOfMoves; 
    } // min number of moves to solve initial board; -1 if unsolvable
    
    public Iterable<Board> solution()  {
        // sequence of boards in a shortest solution; null if unsolvable
        if (!isSolvable()) return null;
        List<Board> s = new ArrayList<>(solution.size());
        for (int k = solution.size() - 1; k >= 0; k--) {
            s.add(solution.get(k));
        }
        return s;
    }
    public static void main(String[] args) {
     // create initial board from file
        //In in = new In(args[0]);
        int N = 3;
        int[][] blocks = { 
                {1, 2, 3}, 
                {4, 5, 6}, 
                {8, 7, 0} 
        };
        Board initial = new Board(blocks);
        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}