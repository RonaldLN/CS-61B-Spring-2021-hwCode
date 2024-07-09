package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T> {

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

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

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

        public LinkedlistDequeIterator() {
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
        if (!(o instanceof Deque)) {
            return false;
        }

        Iterator<T> iter1 = iterator();
        Iterator<T> iter2 = o.iterator();

        while (iter1.hasNext() && iter2.hasNext()) {
            if (iter1.next() != iter2.next()) {
                return false;
            }
        }

        if (iter1.hasNext() || iter2.hasNext()) {
            return false;
        }

        return true;
    }
}
