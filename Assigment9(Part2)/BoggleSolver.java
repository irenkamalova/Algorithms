import java.util.ArrayList;
import java.util.List;


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.TST;


public class BoggleSolver {
    
    private int M;
    private int N;
    private BoggleBoard board;
    private List<String> l = new ArrayList<>();
    private TST<Integer> tst;
    
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    //@SuppressWarnings("unchecked")
    public BoggleSolver(String[] dictionary) {
        //set = new HashSet<>(dictionary.length);
        //for (int i = 0; i < dictionary.length; i++) {
        //    set.add(dictionary[i]);
        //}
        tst = new TST<>();
        for (Integer i = 0; i < dictionary.length; i++) {
            
            tst.put(dictionary[i], i);
        }
        
    }
    /*
    private class Dictionary {
        private TST tst;
        
        public Dictionary() {
            tst = new TST<>();
            
        }
        
        public boolean checkPrefix() {
            
        }
    }
*/
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        
        this.board = board;
        M = board.rows();
        N = board.cols();
        boolean[][] marked = new boolean[M][N];
        l.clear();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {                
                StringBuilder path = new StringBuilder(board.getLetter(i, j));
                for (int k = 0; k < M; k++) {
                    for (int m = 0; m < N; m++) {      
                        marked[k][m] = false;
                    }
                }
                searchingPaths(path, i, j, marked.clone());
            }
        }
        
        //for (String s : l) {
        //    System.out.println(s);
        //}
        //System.out.println(l.size());
        //board.
        return l;
        
    }
    
    private void searchingPaths(StringBuilder path, int i, int j, boolean[][] marked) {
        boolean[][] newmarked = new boolean[M][N];
        for (int k = 0; k < M; k++) {
            for (int m = 0; m < N; m++) {      
                newmarked[k][m] = marked[k][m];
            }
        }
        if (i >= 0 && i < M && j >= 0 && j < N  && !newmarked[i][j]) {
            newmarked[i][j] = true; // already was here (don't repeat)
            StringBuilder npath = new StringBuilder();
            //System.out.println(path);
            npath.append(path);
            npath.append(board.getLetter(i, j));
            String s = new String(npath);
            if (tst.keysWithPrefix(s).iterator().hasNext()) {
                //tst.keysWithPrefix(s) != null || 
                if (s.length() > 2 && tst.contains(s) && !l.contains(s))
                    l.add(s);
                if(board.getLetter(i, j) == 'Y') {
                    //System.out.println('Y');
                }
                // go in 8 cases 
                searchingPaths(npath, i - 1, j - 1, newmarked); //1
                searchingPaths(npath, i - 1, j, newmarked); //2
                searchingPaths(npath, i - 1, j + 1, newmarked); //3
                searchingPaths(npath, i, j - 1, newmarked); //4
                searchingPaths(npath, i, j + 1, newmarked); //5
                searchingPaths(npath, i + 1, j - 1, newmarked); //6
                searchingPaths(npath, i + 1, j, newmarked); //7
                searchingPaths(npath, i + 1, j + 1, newmarked); //8
                // if this word in dict
                // return npath;
                // else
                //return null;
                //return
            }
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    
    public int scoreOf(String word) {
        return 0;
    }
    
    public static void main(String[] args) {
        In in = new In("dictionary-yawl.txt");
        String[] s = in.readAllStrings();
        
        BoggleBoard board = new BoggleBoard("board-points1.txt");
        BoggleSolver bs = new BoggleSolver(s);
        for(String result : bs.getAllValidWords(board)) {
            System.out.println(result);
        }
        
        

    }
}