package swe4.threading;

import swe4.util.Util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringJoiner;

public class Buffer {

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private String name;
    private int capacity;
    private Queue<Character> queue;

    public Buffer(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    private synchronized void print(char ch, String dir) {
        print(ch, dir, "");
    }

    private synchronized void print(char ch, String dir, String postfix) {
        StringJoiner sj = new StringJoiner(
                ", ",
                "%-10s (%s): %c %s ["
                        .formatted(name, LocalTime.now().format(TIME_FMT), ch, dir),
                "]: %s".formatted(postfix));
        queue.forEach(c -> sj.add(String.valueOf(c)));
        System.out.println(sj);
    }

    // alle Methoden, die auf gemeinsame Datenkomponenten (queue) zugreifen, sind synchronized
    // = kuerzere Schreibweise zu synchronized(this) { ... } innerhalb der Methode
    public synchronized boolean empty() {
        return queue.isEmpty();
    }

    public synchronized boolean full() {
        return queue.size() >= capacity;
    }

    public synchronized void put(char ch) {
        while (full()) {
            // wartet bis ich notifiziert werde, dass sich was geändert hat
            // und notifiziert werde ich sobald jemand was aus der Queue rausnimmt (notifyAll)
            print(ch, "==>", ": rejected because of full queue");
            Util.wait(this);
        }


        print(ch, "==>");
        queue.add(ch);
        this.notifyAll();
    }

    public synchronized char get() {
        while (empty()) {
            // wartet bis ich notifiziert werde, dass sich was geändert hat
            // und notifiziert werde ich sobald jemand was in die Queue reinlegt (notifyAll)
            print('_', "<==", ": rejected because of empty queue");
            Util.wait(this);
        }

        char ch = queue.remove();
        print(ch, "<==");

        // benachrichtige alle wartenden Threads, dass sich der Zustand geändert hat
        // ggf. kann dann ein wartender Thread in put weitermachen
        this.notifyAll();

        return ch;
    }
}
