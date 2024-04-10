package swe4.client;

import swe4.queues.HeapQueue;

// Kompilieren + Ausfuehren:
// ant compile | java --class-path bin/classes swe4.client.HeapQueueClient

public class HeapQueueClient {
    public static void main(String[] args) {
        System.out.println("=== Test of HeapQueueClient ===");

        HeapQueue queue = new HeapQueue(15);
        queue.insert(19);
        queue.insert(36);
        queue.insert(2);
        queue.insert(50);
        queue.insert(100);
        queue.insert(3);
        queue.insert(25);
        queue.insert(17);
        queue.insert(1);
        queue.insert(7);
        
        System.out.println("queue = " + queue.toString());
        
        while (queue.size() > 0) {
            System.out.println("max = " + queue.max());
            System.out.println("removing max...");
            queue.removeMax();
        }

        System.out.println("===============================");
    }
}