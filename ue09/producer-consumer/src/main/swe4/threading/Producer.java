package swe4.threading;

import swe4.util.Util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Producer extends Thread {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private String name;
    private Buffer buffer;

    public Producer(String name, Buffer b) {
        this.name = name;
        this.buffer = b;
    }

    @Override
    public void run() {
        Random random = new Random();

        for (int i = 0; i < 26; i++) {
            char newChar = (char) ('A' + i);

            System.out.printf("%-10s (%s): ++> %c%n", name, LocalDateTime.now().format(TIME_FMT), newChar);
            Util.sleep(random.nextInt(1000));
            System.out.printf("%-10s (%s): <++ %c%n", name, LocalDateTime.now().format(TIME_FMT), newChar);

            buffer.put(newChar);
        }
    }
}
