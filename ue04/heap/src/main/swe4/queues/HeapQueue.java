package swe4.queues;

public class HeapQueue {
    private int[] heap = null; // Array ist immer Referenzdatentyp
    private int size = 0;
    
    public HeapQueue() {
        this(10);
    }

    public HeapQueue(int maxSize) {
        heap = new int[maxSize];
    }

    private static int left(int i) {
        return 2*i + 1;
    }

    private static int right(int i) {
        return left(i) + 1;
    }

    public int size() {
        return size;
    }

    private static int parent(int i) {
        return (i-1) / 2;
    }

    public void insert(int item) {
        // best case + avg case: O(1) (avg auch O(1) weil viel mehr Elemente unten sind als oben)
        // worst case: O(log(n))

        size++;
        heap[size - 1] = item;
        upHeap(size - 1);
    }

    private void upHeap(int k) {
        if (size <= 1) return;
        
        int e = heap[k]; // backup item

        while (k > 0 && heap[parent(k)] < e) {
            heap[k] = heap[parent(k)];
            k = parent(k);
        }


        heap[k] = e;
    }

    public void removeMax() {
        heap[0] = heap[size - 1];
        size--;
        downHeap(0);
    }

    private void downHeap(int k) {
        int e = heap[k];
        
        while (k <= parent(size - 1)) {
            int j = left(k);
            if (j < size - 1 && heap[j] < heap[j + 1]) // groesseres Element beider Nachfolger ermitteln
                j++;

            if (e >= heap[j])
                break;

            heap[k] = heap[j]; // Element wandert nach oben
            k = j;
        }

        heap[k] = e;
    }

    public int max() {
        if (size < 0)
            throw new IllegalStateException("Accessed empty queue");

        return heap[0];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for(int i = 0; i < size; i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(heap[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}