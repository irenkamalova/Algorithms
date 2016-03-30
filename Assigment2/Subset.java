import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {
    public static void main(String[] args) {
        
        int k = Integer.parseInt(args[0]);
        //System.out.println(k);
        RandomizedQueue<String> rands = new RandomizedQueue<String>();
        //System.out.println(StdIn.hasNextChar());
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            //System.out.println(s);
            if (s != null)
                rands.enqueue(s);
        }
        for (int i = 0; i < k; i++) {
            //System.out.println(rands.dequeue());
            StdOut.println(rands.dequeue());
        }
    }
}
