package swe4.rmi.buffer;

import swe4.rmi.util.Util;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.StringJoiner;

public class BufferService implements Buffer {

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private String name;
    private int capacity;
    private Queue<Character> queue;

    public BufferService(int capacity) {
        this.capacity = capacity;
        queue = new java.util.LinkedList<>();
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

    public boolean empty() {
        return queue.isEmpty();
    }

    public boolean full() {
        return queue.size() == capacity;
    }

    public synchronized void put(char ch) {

        while (full()) {
            print(ch, "==>", "rejected because of full queue");
            Util.wait(this);
        }

        print(ch, "==>");
        queue.add(ch);
    }

    public synchronized char get() {

        while (queue.isEmpty()) {
            print('\0', "<==", "rejected because of empty queue");
            Util.wait(this);
        }

        Character ch = queue.remove();
        print(ch, "<==");
        return ch;
    }


    public static void main(String[] args) throws RemoteException, MalformedURLException {
        int registryPort = Registry.REGISTRY_PORT;
        String serverHostName = "localhost";
        if (args.length > 0) {
            String[] hostAndPort = args[0].split(":");
            if (hostAndPort.length > 0) serverHostName = hostAndPort[0];
            if (hostAndPort.length > 1) registryPort = Integer.parseInt(hostAndPort[1]);
        }

        System.setProperty("java.rmi.server.hostname", serverHostName);

        String internalUrl = "rmi://localhost:%d/BufferService".formatted(registryPort);
        String externalUrl = "rmi://%s:%d/BufferService".formatted(serverHostName, registryPort);

        Buffer bufferService = new BufferService(5);
        Remote bufferServiceStub = UnicastRemoteObject.exportObject(bufferService, registryPort);

        LocateRegistry.createRegistry(registryPort);
        Naming.rebind(internalUrl, bufferServiceStub);

        System.out.printf("BufferService available at %s", externalUrl);
    }


}
