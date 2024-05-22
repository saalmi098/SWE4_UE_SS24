package swe4.util;

public class Util {

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    public static void wait(Object obj) {
        try {
            obj.wait();
        } catch (InterruptedException ignored) {
        }
    }
}
