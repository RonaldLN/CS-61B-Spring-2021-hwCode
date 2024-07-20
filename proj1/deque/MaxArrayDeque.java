package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    public T max() {
        return max(comparator);
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }

        T returnItem = get(0);

        for (T item : this) {
            if (c.compare(returnItem, item) < 0) {
                returnItem = item;
            }
        }

        return returnItem;
    }
}
