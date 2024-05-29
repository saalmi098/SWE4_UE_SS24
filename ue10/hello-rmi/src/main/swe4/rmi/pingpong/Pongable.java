package swe4.rmi.pingpong;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public interface Pongable extends Remote {
    void pong(LocalDateTime pingTime) throws RemoteException;
}
