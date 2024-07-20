package deque;

import java.util.Iterator;

public interface Deque<T> {
    public void addFirst(T item);
    public void addLast(T item);
    public default boolean isEmpty() {
        return size() == 0;
    }
    public int size();
    public void printDeque();
    public T removeFirst();
    public T removeLast();
    public T get(int index);

    public default boolean equals(Deque<T> d) {
        if (d == this) {
            return true;
        }
        if (d == null) {
            return false;
        }
        if (d.size() != size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            T item1 = d.get(i);
            T item2 = get(i);
            if (!item1.equals(item2)) {
                return false;
            }
        }
        return true;
    }
}
