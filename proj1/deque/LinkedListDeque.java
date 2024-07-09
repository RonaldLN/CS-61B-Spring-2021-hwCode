package deque;

public class LinkedListDeque<T> {

    /** Node class */
    private class Node {
        public T item;
        public Node next;
        public Node prev;

        public Node() {
            next = null;
            prev = null;
        }

        public Node(T i, Node n, Node p) {
            item = i;
            next = n;
            prev = p;
        }
    }

    private final Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node();
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        Node newNode = new Node(item, sentinel.next, sentinel);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;
    }

    public void addLast(T item) {
        Node newNode = new Node(item, sentinel, sentinel.prev);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node n = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(n.item);
            System.out.print(" ");
        }
        System.out.println();
    }

    public T removeFirst() {
        Node node = sentinel.next;
        node.next.prev = sentinel;
        sentinel.next = node.next;
        size -= 1;
        return node.item;
    }

    public T removeLast() {
        Node node = sentinel.prev;
        node.prev.next = sentinel;
        sentinel.prev = node.prev;
        size -= 1;
        return node.item;
    }

    public T get(int index) {
        Node n = sentinel.next;
        for (int i = 0; i < index; i++) {
            n = n.next;
        }
        return n.item;
    }

    /** Returns the ith item of this IntList. */
    public T getRecursive(int index) {
        return getRecursive(index, sentinel.next);
    }

    private T getRecursive(int index, Node node) {
        if (index == 0) {
            return node.item;
        }
        return getRecursive(index - 1, node.next);
    }
}
