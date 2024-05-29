package swe4.rmi.pingpong;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

public class PingService implements Pingable {

    public static void main(String[] args) throws RemoteException, MalformedURLException {

        int registryPort = Registry.REGISTRY_PORT;
        String serverHostName = "localhost";
        if (args.length > 0) {
            String[] hostAndPort = args[0].split(":");
            if (hostAndPort.length > 0) serverHostName = hostAndPort[0];
            if (hostAndPort.length > 1) registryPort = Integer.parseInt(hostAndPort[1]);
        }

        System.setProperty("java.rmi.server.hostname", serverHostName);

        String internalUrl = "rmi://localhost:%d/PingService".formatted(registryPort);
        String externalUrl = "rmi://%s:%d/PingService".formatted(serverHostName, registryPort);

        Pingable pingService = new PingService();
        Remote pingServiceStub = UnicastRemoteObject.exportObject(pingService, registryPort);

        LocateRegistry.createRegistry(registryPort);
        Naming.rebind(internalUrl, pingServiceStub);

        System.out.printf("PingService available at %s", externalUrl);
    }

    @Override
    public void ping(LocalDateTime pingTime) throws RemoteException {
        System.out.printf("Service: pingTime = %s%n", pingTime);
    }

    @Override
    public void ping(Pongable client, LocalDateTime pingTime) throws RemoteException {
        System.out.printf("Service: pingTime = %s%n", pingTime);
        client.pong(pingTime);
    }
}

