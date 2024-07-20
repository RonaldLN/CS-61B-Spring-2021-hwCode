package deque;

public interface Deque<T> {
    void addFirst(T item);
    void addLast(T item);
    default boolean isEmpty() {
        return size() == 0;
    }
    int size();
    void printDeque();
    T removeFirst();
    T removeLast();
    T get(int index);

    default boolean equals(Deque<T> d) {
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
