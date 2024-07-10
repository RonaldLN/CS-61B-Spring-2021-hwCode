package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[100];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    /** Resizes the underlying array to the target capacity. */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int first = nextFirst + 1;
        int last = nextLast - 1;
        if (first == items.length) {
            first = 0;
        } else if (last == -1) {
            last = items.length - 1;
        }

        if (first == 0 || first == 1) {
            System.arraycopy(items, first, a, 0, size);
        } else {
            System.arraycopy(items, first, a, 0, items.length - first);
            System.arraycopy(items, 0, a, items.length - first, last + 1);
        }

        items = a;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    @Override
    public void addFirst(T item) {
        if (nextFirst == nextLast) {
            resize(items.length * 2);
        }

        items[nextFirst] = item;
//        nextFirst = nextFirst == 0 ? items.length - 1 : nextFirst - 1;
        nextFirst = indexBackward(nextFirst);
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (nextFirst == nextLast) {
            resize(items.length * 2);
        }

        items[nextLast] = item;
//        nextLast = nextLast == items.length - 1 ? 0 : nextLast + 1;
        nextLast = indexForward(nextLast);
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

    private int indexForward(int index) {
        if (index == items.length - 1) {
            return 0;
        } else {
            return index + 1;
        }
    }

    private int indexBackward(int index) {
        if (index == 0) {
            return items.length - 1;
        } else {
            return index - 1;
        }
    }

    @Override
    public void printDeque() {
        int index = indexForward(nextFirst);
        for (int i = 0; i < size; i++) {
            System.out.print(items[index]);
            System.out.print(" ");

//            index += 1;
//            if (index == items.length) {
//                index = 0;
//            }
            index = indexForward(index);
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        nextFirst = indexForward(nextFirst);
        size -= 1;
        T x = items[nextFirst];
        items[nextFirst] = null;
        return x;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        nextLast = indexBackward(nextLast);
        size -= 1;
        T x = items[nextLast];
        items[nextLast] = null;
        return x;
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        int first = indexForward(nextFirst);
        if (first + index >= items.length) {
            first -= items.length;
        }
        return items[first + index];
    }

    /** Iterator class */
    private class ArrayDequeIterator implements Iterator<T> {
        private int pos;
        private int index;

        public ArrayDequeIterator() {
            pos = 0;
            index = indexForward(nextFirst);
        }

        @Override
        public boolean hasNext() {
            return pos < size;
        }

        @Override
        public T next() {
            T returnItem = items[index];
            pos += 1;
            index = indexForward(index);
            return returnItem;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Deque)) {
            return false;
        }

        Iterator<T> iter1 = iterator();
        Iterator<T> iter2 = ((Deque<T>) o).iterator();

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
