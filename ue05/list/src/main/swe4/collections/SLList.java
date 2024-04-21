package swe4.collections;

public class SLList<T> {

    // ohne private wäre package-visible (wollen wir nicht)
    private Node<T> first;
    private Node<T> last;
    int size;

    // 4 Arten von inneren Klassen in Java:
    // innere Klassen ohne Static
    // static nested Classes
    // lokale Klassen innerhalb Methoden (sehr selten)
    // Anonyme Klassen

    private static class Node<T> {
        private Node<T> next;
        private T value;

        public Node(T value){
            // Default-Parameter über eigenen Konstruktor
            this(value, null);
        }

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    public void prepend(T item) {
        first = new Node<T>(item, first);
        if (last == null) {
            // Sonderfall für leere Liste
            last = first;
        }
        size++;
    }

    public void append(T item) {
        Node<T> newNode = new Node<T>(item);
        if (first == null)
            first = last = newNode;
        else
            last = last.next = newNode; // last.next = newNode; last = last.next;

        size++;
    }

    public T first() throws EmptyListException {
        if (isEmpty())
            throw new EmptyListException("first() invoked for empty list");

        return first.value;
    }

    public T last() throws EmptyListException {
        if (isEmpty())
            throw new EmptyListException("last() invoked for empty list");

        return last.value;
    }

    public boolean isEmpty() {
        return first == null && last == null;
    }

    public int size() {
        return size;
    }
}
