import java.util.Random;

public class Printer extends Thread {
    private char ch;
    private int rows;
    private int cols;

    private static Object syncObject = new Object();

    public Printer(char ch, int rows, int cols) {
        this.ch = ch;
        this.rows = rows;
        this.cols = cols;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    public void print1() {
        Random r = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(ch);
                sleep(r.nextInt(10));
            }
            System.out.println();
        }
    }

    public void print2() {
        // Synchronisationsvariable kann beliebiger Referenzdatentyp sein

        Random r = new Random();

        for (int i = 0; i < rows; i++) {
            // Dadurch dass syncObject statisch ist, darf immer nur 1 Thread in den Block
            // (könnte auch eine Datenkomponente sein, auf die synchronisiert wird - dann durch diese synchronisiert)
            synchronized (syncObject) {
                for (int j = 0; j < cols; j++) {
                    System.out.print(ch);
                    sleep(r.nextInt(10));
                }
                System.out.println();
            }
            sleep(r.nextInt(10));
        }
    }

    @Override
    public void run() {
        // Methoden die hier aufgerufen werden, müssen thread-safe sein
        // system.out.println ist thread-safe

//        print1();
        print2();
    }

    public static void main(String[] args) throws InterruptedException {
        Printer p1 = new Printer('x', 10, 60);
        Printer p2 = new Printer('o', 10, 60);

        // Programm wird sofort beendet, weil beim Verlassen des main-Threads alle Daemon-Threads beendet werden
        // (es gibt Daemon-Threads und "normale" (?) Threads)
        p1.setDaemon(true);
        p2.setDaemon(true);

//        p1.print1();
//        p2.print1();

        // start ruft run auf; start blockiert nicht (startet nur den Thread)
        // run läuft in eigenen Thread, main-Thread laeuft weiter
        // run-Thread läuft so lange, so lange die run-Methode was zu tun hat
        // Programm hat 3 Threads: main, p1, p2
        p1.start();
        p2.start();

        // warten bis p1 und p2 fertig sind
        System.out.println("====== before join ======");
        p1.join();
        p2.join();

        System.out.println("====== end of main ======");

    }
}