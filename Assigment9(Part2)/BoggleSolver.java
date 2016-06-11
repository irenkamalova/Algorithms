import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.In;


public class BoggleSolver {
    
    private int M;
    private int N;
    private BoggleBoard board;
    private List<String> l = new ArrayList<>();
    private MyTST<Integer> tst;
    private boolean[][] marked2;
    
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

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard currentBoard) {
        
        this.board = currentBoard;
        M = board.rows();
        N = board.cols();
        //boolean[][] marked = new boolean[M][N];
        marked2 = new boolean[M][N];
        l.clear();
        for (int k = 0; k < M; k++) {
            for (int m = 0; m < N; m++) {      
                marked2[k][m] = false;
            }
        }
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {                
                StringBuilder path = new StringBuilder(board.getLetter(i, j));       
                searchingPaths(path, i, j);
            }
        }
        return l;
        
    }  

    private void searchingPaths(StringBuilder path, int i, int j) {        
        if (i >= 0 && i < M && j >= 0 && j < N  && !marked2[i][j]) {
            marked2[i][j] = true; // already was here (don't repeat)
            StringBuilder npath = new StringBuilder();
            npath.append(path);
            npath.append(board.getLetter(i, j));
            String s = npath.toString();
            if (board.getLetter(i, j) == 'Q') {
                npath.append('U');
                s = npath.toString();
            }
            
            if (tst.hasPrefix(s)) {
                if (s.length() > 2 && tst.containsThis(s) && !l.contains(s)) {
                    l.add(s);
                }
                // go in 8 cases 
                searchingPaths(npath, i - 1, j - 1); //1
                searchingPaths(npath, i - 1, j); //2
                searchingPaths(npath, i - 1, j + 1); //3
                searchingPaths(npath, i, j - 1); //4
                searchingPaths(npath, i, j + 1); //5
                searchingPaths(npath, i + 1, j - 1); //6
                searchingPaths(npath, i + 1, j); //7
                searchingPaths(npath, i + 1, j + 1); //8
            }
            marked2[i][j] = false;
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    private class MyTST<Value> {
        private Node<Value> root;   // root of TST
        private Node<Value> currentX;
        private String lastPrefix;

        private class Node<Value> {
            private char c;                        // character
            private Node<Value> left, mid, right;  // left, middle, and right subtries
            private Value val;                     // value associated with string
        }
        
        public MyTST() {
            lastPrefix = "";
            currentX = root;
        }
        
        public void put(String key, Value val) {
            if (!contains(key)) N++;
            root = put(root, key, val, 0);
        }
        
        public boolean contains(String key) {
            return get(key) != null;
        }
        
        public boolean containsThis(String key) {
            if (key == null) throw new NullPointerException();
            if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
            Node<Value> x = get(currentX, key, lastPrefix.length() - 1);
            return x.val != null;
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
             Node<Value> x;
             int a = prefix.length();
            if (prefix.length() == 1 || prefix.length() - 1 != lastPrefix.length()) {
                x = get(root, prefix, 0);
            }
            else {
                x = get(currentX, prefix, lastPrefix.length() - 1);
            }    
            if (x == null) return false;
            else {
                currentX = x;
                lastPrefix = prefix;
                return true;
            }
        }
    }
    
    
    public int scoreOf(String word) {
        int lenght = word.length();
        if (!tst.contains(word)) return 0;
        if (lenght <= 2) return 0;
        if (lenght <= 4) return 1;
        if (lenght == 5) return 2;
        if (lenght == 6) return 3;
        if (lenght == 7) return 5;
        return 11;
    }
    
    public static void main(String[] args) {
        In in = new In("dictionary-yawl.txt");
        String[] s = in.readAllStrings();
        
        BoggleBoard board = new BoggleBoard("board4x4.txt");
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
