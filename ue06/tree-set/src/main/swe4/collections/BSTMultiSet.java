package swe4.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BSTMultiSet<T extends Comparable<T>> implements SortedMultiSet<T> {

    private static class Node<T> {
        private T value;
        private Node<T> left;
        private Node<T> right;

        public Node(T value, Node<T> left, Node<T> right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    private static class BSTMultiSetIterator<T> implements Iterator<T> {

        private Stack<Node<T>> unvisitedParents = new Stack<>();

        public BSTMultiSetIterator(Node<T> root) {
            Node<T> curr = root;
            while (curr != null) {
                unvisitedParents.push(curr);
                curr = curr.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !unvisitedParents.empty();
        }

        @Override
        public T next() {
            if (!hasNext())
                throw new NoSuchElementException(); // RuntimeException (kein throws in Signatur notwendig)

            Node<T> curr = unvisitedParents.pop();
            Node<T> next = curr.right;
            while (next != null) {
                unvisitedParents.push(next);
                next = next.left;
            }

            return curr.value;
        }
    }

    private Node<T> root = null;
    private int size = 0;

    private Node<T> addRecursive(Node<T> parent, T value) {
        Node<T> newNode = new Node<>(value, null, null);
        if (parent == null) {
            //parent = newNode; // Achtung funktioniert nicht (ändert sich nicht bei Aufrufer;
            // man kann in Java keine Referenz auf eine Referenz übergeben)
            // -> Ausweichen auf Rückgabewert (von void zu Node<T>)
            return newNode;
        } else if (value.compareTo(parent.value) < 0) { // insert left
            // value.compareTo(parent.value) < 0 => value < parent.value
            parent.left = addRecursive(parent.left, value);
        } else { // insert right
            parent.right = addRecursive(parent.right, value);

        }

        return parent;
    }

    @Override
    public void add(T item) {
        root = addRecursive(root, item);
        size++;
    }

    @Override
    public T get(T value) {
        Node<T> currentNode = root;
        while (currentNode != null) {
            int compare = value.compareTo(currentNode.value);
            if (compare < 0) { // value < currentNode.value
                currentNode = currentNode.left;
            } else if (compare > 0) { // value > currentNode.value
                currentNode = currentNode.right;
            } else { // compare == 0
                return currentNode.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T value) {
        return get(value) != null;
    }

    @Override
    public Iterator<T> iterator() {
        return new BSTMultiSetIterator<>(root);
    }
}
