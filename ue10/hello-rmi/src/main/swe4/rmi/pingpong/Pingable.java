package swe4.rmi.pingpong;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

// Interface muss public sein und Remote implementieren, damit es von RMI verwendet werden kann
public interface Pingable extends Remote {

    // jede Methode, die ueber RMI verwendet werden soll, muss RemoteException werfen
    // und alle Parameter und Rueckgabewerte muessen serialisierbar sein (da sie über Prozessgrenzen hinweg übertragen werden)
    void ping(LocalDateTime pingTime) throws RemoteException;

    void ping(Pongable client, LocalDateTime pingTime) throws RemoteException;
}
