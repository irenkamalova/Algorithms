import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private Node<Item> first;    // beginning of queue
    private Node<Item> last;     // end of queue
    private int N;               // number of elements on queue

    // helper linked list class
    private static class Node<Item>  {
        private Item item;
        private Node<Item> next;
    }
    
    public RandomizedQueue() {
        first = null;
        last = null;
        N = 0;
    }                // construct an empty randomized queue
    public boolean isEmpty() {
        return N == 0;
    }                 // is the queue empty?
    public int size() {
        return N;
    }                       // return the number of items on the queue
    public void enqueue(Item item) {
        if (item == null) throw new  NullPointerException("Element mast be not null");
        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else 
            oldlast.next = last;
        N++;
    }          // add the item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Node<Item> item = first;
        Node<Item> prev_item = null;
        for (int i = 0; i < StdRandom.uniform(N); i++) {
            prev_item = item;
            item = item.next;
        }
        if (prev_item != null) {
            if (item.next != null) {
                prev_item.next = item.next;
            }
            else {
                last = prev_item;
            }
        }
        else {
            first = item.next;
        }
        N--;
        if (isEmpty()) last = null;
        return item.item;    
    }                   // remove and return a random item
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Node<Item> item = first;
        for (int i = 0; i < StdRandom.uniform(N); i++) {
            item = item.next;
        }
        return item.item;
    }                     // return (but do not remove) a random item
    public Iterator<Item> iterator()  {
        return new ListIterator<Item>(first);  
    }     // return an independent iterator over items in random order
    
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
    
    public static void main(String[] args) {
        RandomizedQueue<Integer> r1 = new RandomizedQueue<Integer>();
        RandomizedQueue<Integer> r2 = new RandomizedQueue<Integer>();
        for (int i = 0; i < 10; i++) {
            r1.enqueue(i);
            r2.enqueue(i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.print(r1.sample());
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.println(r1.dequeue() + " " + r2.dequeue());
            //System.out.println("Size is " + r1.size());
        }
        for (int i = 0; i < 10; i++) {
            r1.enqueue(i);
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.print(r1.dequeue());
            //System.out.println("Size is " + r1.size());
        }
    } // unit testing
}
