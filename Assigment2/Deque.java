import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    
    private Node<Item> first;    // beginning of queue
    private Node<Item> last;     // end of queue
    private int N;               // number of elements on queue

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> oldlast;
    }

    
    public Deque() {
        first = null;
        last = null;  
        N = 0;
   }
    // construct an empty deque
   public boolean isEmpty() {
       return first == null;
   }             // is the deque empty?
   public int size() {
       return N;
   }                       // return the number of items on the deque
   public void addFirst(Item item) {
       if (item == null) throw new  NullPointerException("Element mast be not null");
       Node<Item> next = first;
       first = new Node<Item>();
       first.item = item;
       first.next = next;
       first.oldlast = null;
       if (last == null) {
           last = first;
       }
       else {
           next.oldlast = first;
       }
       N++;       
   }         // add the item to the front
   public void addLast(Item item) {
       if (item == null) throw new  NullPointerException("Element mast be not null");
       Node<Item> oldlast = last;
       last = new Node<Item>();
       last.item = item;
       last.oldlast = oldlast;
       last.next = null;
       if (first == null) {
           first = last;
       }
       else {
           oldlast.next = last;
       }
       N++;
   }         // add the item to the end
   public Item removeFirst() {
       if (isEmpty()) throw new NoSuchElementException("Queue underflow");
       Item item = first.item;
       first = first.next;
       N--;
       if (isEmpty()) {
           last = null;
       }
       else {
           first.oldlast = null;
       } // to avoid loitering
       return item;
   }            // remove and return the item from the front
   public Item removeLast() {
       if (isEmpty()) throw new NoSuchElementException("Queue underflow");
       Item item = last.item;
       last = last.oldlast;       
       N--;
       if (last == null) {
           first = null;
       }
       else {
           last.next = null;
       }
       return item;
   }              // remove and return the item from the end
   public Iterator<Item> iterator() {
       return new ListIterator<Item>(first); 
   }       // return an iterator over items in order from front to end
   
   private static class ListIterator<Item> implements Iterator<Item> {
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
       Deque<Integer> deque = new Deque<Integer>();
       deque.addLast(1);
       deque.addFirst(2);
       deque.addFirst(3);
       deque.addFirst(4);
       deque.addFirst(5);
       deque.addFirst(6);
       deque.addFirst(7);
       deque.addFirst(8);
       deque.removeLast();
       Iterator<Integer> it = deque.iterator();
       int k = 0;
       while (it.hasNext()) {
           k++;
           it.next();
       }
       System.out.println(k);
   }  // unit testing
}