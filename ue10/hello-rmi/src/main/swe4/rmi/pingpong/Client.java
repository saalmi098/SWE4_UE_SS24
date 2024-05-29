package swe4.rmi.pingpong;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Client implements Pongable {

    public static void ping1(Pingable pingable) throws RemoteException, InterruptedException {
        for (int i = 0; i < 100; i++) {
            LocalDateTime now = LocalDateTime.now();
            pingable.ping(now);
            System.out.printf("Client: sent ping at %s%n", now);
            Thread.sleep(500);
        }
    }

    public void ping2(Pingable pingable) throws RemoteException, InterruptedException {
        for (int i = 0; i < 100; i++) {
            LocalDateTime now = LocalDateTime.now();
            pingable.ping(this, now);
            System.out.printf("Client: sent ping at %s%n", now);
            Thread.sleep(500);
        }
    }

    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException, InterruptedException {

        String hostAndPort = "localhost"; // ohne Port wird der Standardport 1099 verwendet
        if (args.length > 0) hostAndPort = args[0];

        String serviceUrl = "rmi://%s/PingService".formatted(hostAndPort);
        Pingable serviceProxy = (Pingable) Naming.lookup(serviceUrl); // proxy ... "Stellvertreter" (für das PingService)

//        ping1(serviceProxy);

        Client client = new Client();
        UnicastRemoteObject.exportObject(client, 0);
        client.ping2(serviceProxy);

        UnicastRemoteObject.unexportObject(client, true);

    }

    @Override
    public void pong(LocalDateTime pingTime) throws RemoteException {
        long nanoTime = ChronoUnit.NANOS.between(pingTime, LocalDateTime.now());
        System.out.printf("Client: time fo roundtrip = %.3f seconds%n", nanoTime / 1e9);

    }
}
