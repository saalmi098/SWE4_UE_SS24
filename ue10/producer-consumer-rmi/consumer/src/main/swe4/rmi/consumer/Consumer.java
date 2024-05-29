package swe4.rmi.consumer;

import swe4.rmi.buffer.Buffer;
import swe4.rmi.util.Util;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Consumer {

  private static final DateTimeFormatter TIME_FMT = 
      DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
  private Buffer buffer;

  public Consumer(String hostAndPort) throws MalformedURLException, NotBoundException, RemoteException {
    String bufferUrl = "rmi://%s/BufferService".formatted(hostAndPort);
    System.out.printf("Connecting to buffer at %s%n", bufferUrl);
    buffer = (Buffer) Naming.lookup(bufferUrl);
  }

  public void consume() throws RemoteException { // former run method
    Random random = new Random();

    for (int i = 0; i < 26; i++) {
      char ch = buffer.get();

      System.out.printf("%-10s (%s): --> %c%n", "Consumer", LocalDateTime.now().format(TIME_FMT), ch);
      Util.sleep(random.nextInt(1000));
      System.out.printf("%-10s (%s): <-- %c%n", "Consumer", LocalDateTime.now().format(TIME_FMT), ch);

    }
  }

  public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {

    String hostAndPort = "localhost";
    if (args.length > 0) hostAndPort = args[0];

    Consumer consumer = new Consumer(hostAndPort);
    consumer.consume();
  }
}
