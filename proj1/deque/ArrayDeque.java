package deque;

public class ArrayDeque<T> {
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

    public void addFirst(T item) {
        if (nextFirst == nextLast) {
            resize(items.length * 2);
        }

        items[nextFirst] = item;
//        nextFirst = nextFirst == 0 ? items.length - 1 : nextFirst - 1;
        nextFirst = indexBackward(nextFirst);
        size += 1;
    }

    public void addLast(T item) {
        if (nextFirst == nextLast) {
            resize(items.length * 2);
        }

        items[nextLast] = item;
//        nextLast = nextLast == items.length - 1 ? 0 : nextLast + 1;
        nextLast = indexForward(nextLast);
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

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
}
