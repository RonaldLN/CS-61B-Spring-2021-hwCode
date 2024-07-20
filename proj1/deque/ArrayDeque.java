package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private static final int START_SIZE = 8;

    public ArrayDeque() {
        items = (T[]) new Object[START_SIZE];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    /** Resizes the underlying array to the target capacity. */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int first = indexForward(nextFirst);
        int last = indexBackward(nextLast);

        if (first < last) {
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
        if (items.length > START_SIZE && (float) size / items.length < 0.3) {
            resize(items.length / 2);
        }
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
        if (items.length > START_SIZE && (float) size / items.length < 0.3) {
            resize(items.length / 2);
        }
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

        private ArrayDequeIterator() {
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
        if (o.getClass() == LinkedListDeque.class || o.getClass() == ArrayDeque.class) {
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
        } else {
            Deque<T> d = (Deque<T>) o;
            int i = 0;
            while (it1.hasNext()) {
                T item1 = it1.next();
                T item2 = d.get(i);
                if (!item1.equals(item2)) {
                    return false;
                }
                i += 1;
            }
        }
        return true;
    }
}
