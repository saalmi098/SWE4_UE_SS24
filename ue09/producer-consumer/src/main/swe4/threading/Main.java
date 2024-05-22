package swe4.threading;

public class Main {
    public static void main(String[] args) {
        Buffer buffer = new Buffer("Buffer", 4);
        Producer producer1 = new Producer("Producer 1", buffer);
        Consumer consumer1 = new Consumer("Consumer 1", buffer);
        Consumer consumer2 = new Consumer("Consumer 2", buffer);

        producer1.start();
        consumer1.start();
        consumer2.start();
    }
}
