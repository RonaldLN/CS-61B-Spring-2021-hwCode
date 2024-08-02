package bstmap;

import java.util.Iterator;
import java.util.Set;

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
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {
        System.out.print("[");
        printInOrder(root);
        System.out.println("]");
    }

    public void printInOrder(BSTNode node) {
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
