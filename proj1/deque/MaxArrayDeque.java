package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> implements Deque<T> {
    private ArrayDeque<T> arrayDeque;
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        arrayDeque = new ArrayDeque<T>();
        comparator = c;
    }

    public T max() {
        return max(comparator);
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }

        Iterator<T> iter = arrayDeque.iterator();
        T returnItem = iter.next();

        while (iter.hasNext()) {
            T item = iter.next();
            if (c.compare(returnItem, item) < 0) {
                returnItem = item;
            }
        }

        return returnItem;
    }

    @Override
    public void addFirst(T item) {
        arrayDeque.addFirst(item);
    }

    @Override
    public void addLast(T item) {
        arrayDeque.addLast(item);
    }

    @Override
    public boolean isEmpty() {
        return arrayDeque.isEmpty();
    }

    @Override
    public int size() {
        return arrayDeque.size();
    }

    @Override
    public void printDeque() {
        arrayDeque.printDeque();
    }

    @Override
    public T removeFirst() {
        return arrayDeque.removeFirst();
    }

    @Override
    public T removeLast() {
        return arrayDeque.removeLast();
    }

    @Override
    public T get(int index) {
        return arrayDeque.get(index);
    }

    @Override
    public Iterator<T> iterator() {
        return arrayDeque.iterator();
    }

    @Override
    public boolean equals(Object o) {
        return arrayDeque.equals(o);
    }
}
