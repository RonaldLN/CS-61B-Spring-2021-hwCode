package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    /** Node class */
    private class Node {
        private T item;
        private Node next;
        private Node prev;

        private Node() {
            next = null;
            prev = null;
        }

        private Node(T i, Node n, Node p) {
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

    @Override
    public void addFirst(T item) {
        Node newNode = new Node(item, sentinel.next, sentinel);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        Node newNode = new Node(item, sentinel, sentinel.prev);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

//    @Override
//    public boolean isEmpty() {
//        return size == 0;
//    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node n = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(n.item);
            System.out.print(" ");
            n = n.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node node = sentinel.next;
        node.next.prev = sentinel;
        sentinel.next = node.next;
        size -= 1;
        return node.item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        Node node = sentinel.prev;
        node.prev.next = sentinel;
        sentinel.prev = node.prev;
        size -= 1;
        return node.item;
    }

    @Override
    public T get(int index) {
        Node n = sentinel.next;
        for (int i = 0; i < index; i++) {
            n = n.next;
        }
        return n.item;
    }

    public T getRecursive(int index) {
        return getRecursive(index, sentinel.next);
    }

    /** Helper method for getRecursive */
    private T getRecursive(int index, Node node) {
        if (node == null) {
            return null;
        } else if (index == 0) {
            return node.item;
        }
        return getRecursive(index - 1, node.next);
    }

    /** Iterator class */
    private class LinkedlistDequeIterator implements Iterator<T> {
        private int pos;
        private Node node;

        private LinkedlistDequeIterator() {
            pos = 0;
            node = sentinel;
        }

        @Override
        public boolean hasNext() {
            return pos < size;
        }

        @Override
        public T next() {
            node = node.next;
            pos += 1;
            return node.item;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedlistDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        if (((Deque<T>) o).size() != size) {
            return false;
        }
        Iterator<T> it1 = iterator();
        Iterator<T> it2;
        if (o.getClass() == LinkedListDeque.class) {
            it2 = ((LinkedListDeque<T>) o).iterator();
        } else {
            it2 = ((ArrayDeque<T>) o).iterator();
        }
        while (it1.hasNext()) {
            T item1 = it1.next();
            T item2 = it2.next();
            if (!item1.equals(item2)) {
                return false;
            }
        }
        return true;
    }
}
