import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Item[] items;
    private int front;
    private int back;
    private int size;

    // construct an empty deque
    public Deque() {
        items = (Item[]) new Object[2]; // Initial capacity of 2
        front = 0;
        back = 0;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Item cannot be null");
        if (size == items.length) resize(2 * items.length);
        front = (front - 1 + items.length) % items.length;
        items[front] = item;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Item cannot be null");
        if (size == items.length) resize(2 * items.length);
        items[back] = item;
        back = (back + 1) % items.length;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        Item item = items[front];
        items[front] = null; // Avoid loitering
        front = (front + 1) % items.length;
        size--;
        if (size > 0 && size == items.length / 4) resize(items.length / 2);
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        back = (back - 1 + items.length) % items.length;
        Item item = items[back];
        items[back] = null; // Avoid loitering
        size--;
        if (size > 0 && size == items.length / 4) resize(items.length / 2);
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private int current = front;
            private int count = 0;

            public boolean hasNext() {
                return count < size;
            }

            public Item next() {
                if (!hasNext()) throw new NoSuchElementException("No more items to return");
                Item item = items[current];
                current = (current + 1) % items.length;
                count++;
                return item;
            }
        };
    }

    private void resize(int capacity) {
        Item[] newItems = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newItems[i] = items[(front + i) % items.length];
        }
        items = newItems;
        front = 0;
        back = size;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        deque.addFirst("A");
        deque.addLast("B");
        System.out.println(deque.removeFirst()); // A
        System.out.println(deque.removeLast());  // B
        deque.addFirst("C");
        deque.addLast("D");
        for (String s : deque) {
            System.out.println(s); // C D
        }
    }
}
