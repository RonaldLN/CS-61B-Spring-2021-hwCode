package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root;
    private int size;

    private class BSTNode {
        private K key;
        private V value;
        private BSTNode left, right;

        private BSTNode(K k, V v) {
            key = k;
            value = v;
            left = null;
            right = null;
        }
    }

    public BSTMap() {
        root = null;
        size = 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return find(key, root) != null;
    }

    @Override
    public V get(K key) {
        BSTNode node = find(key, root);
        return node == null ? null : node.value;
    }

    private BSTNode find(K key, BSTNode node) {
        if (node == null) {
            return null;
        }
        int result = key.compareTo(node.key);
        if (result == 0) {
            return node;
        } else if (result < 0) {
            return find(key, node.left);
        } else {
            return find(key, node.right);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        BSTNode newNode = new BSTNode(key, value);
        root = put(root, newNode);
        size += 1;
    }

    private BSTNode put(BSTNode node, BSTNode newNode) {
        if (node == null) {
            return newNode;
        }
        int result = newNode.key.compareTo(node.key);
        if (result == 0) {
            return newNode;
        } else if (result < 0) {
            node.left = put(node.left, newNode);
        } else {
            node.right = put(node.right, newNode);
        }
        return node;
    }

    @Override
    public Set<K> keySet() {
        TreeSet<K> ts = new TreeSet<>();
        return getKeySet(root, ts);
    }

    private Set<K> getKeySet(BSTNode node, Set<K> s) {
        if (node == null) {
            return s;
        }
        s.add(node.key);
        getKeySet(node.left, s);
        return getKeySet(node.right, s);
    }

    private class removeJudge {
        private final V targetValue;

        public removeJudge() {
            targetValue = null;
        }

        public removeJudge(V tv) {
            targetValue = tv;
        }

        public boolean judge(V value) {
            if (targetValue == null) {
                return true;
            }
            return value.equals(targetValue);
        }
    }

    private V removeReturnValue;

    @Override
    public V remove(K key) {
        removeReturnValue = null;
        root = remove(root, key, new removeJudge());
        size -= 1;
        return removeReturnValue;
    }

    @Override
    public V remove(K key, V value) {
        removeReturnValue = null;
        root = remove(root, key, new removeJudge(value));
        size -= 1;
        return removeReturnValue;
    }

    private BSTNode remove(BSTNode node, K key, removeJudge judge) {
        if (node == null) {
            return null;
        }
        int result = key.compareTo(node.key);
        if (result == 0) {
            if (!judge.judge(node.value)) {
                return node;
            }

            if (node.left == null) {
                removeReturnValue = node.value;
                return node.right;
            } else if (node.right == null) {
                removeReturnValue = node.value;
                return node.left;
            }

            BSTNode nextToRemove = maxKey(node.left);
            V returnValue = node.value;
            node.key = nextToRemove.key;
            node.value = nextToRemove.value;
            node.left = remove(node.left, nextToRemove.key, new removeJudge());
            removeReturnValue = returnValue;
        } else if (result < 0) {
            node.left = remove(node.left, key, judge);
        } else {
            node.right = remove(node.right, key, judge);
        }

        return node;
    }

    private BSTNode maxKey(BSTNode node) {
        if (node == null) {
            return null;
        } else if (node.right == null) {
            return node;
        } else {
            return maxKey(node.right);
        }
    }

    private class BSTMapIter implements Iterator<K> {
        private final K[] keyArr;
        private int pos;

        public BSTMapIter() {
            Set<K> s = keySet();
            keyArr = (K[]) s.toArray();
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            return pos < keyArr.length;
        }

        @Override
        public K next() {
            K key = keyArr[pos];
            pos += 1;
            return key;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIter();
    }

    public void printInOrder() {
        System.out.print("[");
        printInOrder(root);
        System.out.println("]");
    }

    private void printInOrder(BSTNode node) {
        if (node == null) {
            return;
        }
        printInOrder(node.left);
        System.out.print("<");
        System.out.print(node.key);
        System.out.print(", ");
        System.out.print(node.value);
        System.out.print("> ");
        printInOrder(node.right);
    }
}
