package swe4.rmi.util;

public class Util {

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException ex) {}
    }

    public static void wait(Object obj) {
        try {
            obj.wait();
        }
        catch (InterruptedException e) {
        }
    }
}
