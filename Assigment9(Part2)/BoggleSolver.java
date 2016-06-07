import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.In;


public class BoggleSolver {
    
    private int M;
    private int N;
    private BoggleBoard board;
    private List<String> l = new ArrayList<>();
    private MyTST<Integer> tst;
    
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    //@SuppressWarnings("unchecked")
    public BoggleSolver(String[] dictionary) {
        //set = new HashSet<>(dictionary.length);
        //for (int i = 0; i < dictionary.length; i++) {
        //    set.add(dictionary[i]);
        //}
        tst = new MyTST<>();
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
        for (int k = 0; k < M; k++) {
            for (int m = 0; m < N; m++) {      
                marked[k][m] = false;
            }
        }
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {                
                StringBuilder path = new StringBuilder(board.getLetter(i, j));
                
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
            String s = npath.toString();
            if (board.getLetter(i, j) == 'Q') {
                //if (s.length() > 2 && tst.contains(s) && !l.contains(s)) {
                //    l.add(s);
                //}
                npath.append('U');
                s = npath.toString();
            }
            
            if (tst.hasPrefix(s)) {
                //tst.keysWithPrefix(s) != null || 
                if (s.length() > 2 && tst.contains(s) && !l.contains(s)) {
                    l.add(s);
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
    private class MyTST<Value> {
        private Node<Value> root;   // root of TST

        private class Node<Value> {
            private char c;                        // character
            private Node<Value> left, mid, right;  // left, middle, and right subtries
            private Value val;                     // value associated with string
        }
        
        public MyTST() {
        }
        
        public void put(String key, Value val) {
            if (!contains(key)) N++;
            root = put(root, key, val, 0);
        }
        
        public boolean contains(String key) {
            return get(key) != null;
        }
        
        public Value get(String key) {
            if (key == null) throw new NullPointerException();
            if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
            Node<Value> x = get(root, key, 0);
            if (x == null) return null;
            return x.val;
        }
        
        private Node<Value> put(Node<Value> x, String key, Value val, int d) {
            char c = key.charAt(d);
            if (x == null) {
                x = new Node<Value>();
                x.c = c;
            }
            if      (c < x.c)               x.left  = put(x.left,  key, val, d);
            else if (c > x.c)               x.right = put(x.right, key, val, d);
            else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
            else                            x.val   = val;
            return x;
        }
        
        // return subtrie corresponding to given key
        private Node<Value> get(Node<Value> x, String key, int d) {
            if (key == null) throw new NullPointerException();
            if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
            if (x == null) return null;
            char c = key.charAt(d);
            if      (c < x.c)              return get(x.left,  key, d);
            else if (c > x.c)              return get(x.right, key, d);
            else if (d < key.length() - 1) return get(x.mid,   key, d+1);
            else                           return x;
        }
        
        public boolean hasPrefix(String prefix) {
            //Queue<String> queue = new Queue<String>();
            Node<Value> x = get(root, prefix, 0);
            if (x == null) return false;
            else return true;
        }
    }
    
    
    public int scoreOf(String word) {
        return 0;
    }
    
    public static void main(String[] args) {
        In in = new In("dictionary-yawl.txt");
        String[] s = in.readAllStrings();
        
        BoggleBoard board = new BoggleBoard("board-quinquevalencies.txt");
        BoggleSolver bs = new BoggleSolver(s);
        long startTime = System.currentTimeMillis();
        bs.getAllValidWords(board);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time is " + elapsedTime);
        for(String result : bs.getAllValidWords(board)) {
            System.out.println(result);
        }
        
        

    }
}