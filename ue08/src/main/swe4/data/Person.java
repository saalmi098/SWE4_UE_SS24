package swe4.data;

import java.io.*;
import java.time.LocalDate;
import java.util.StringJoiner;

public class Person implements Serializable, Cloneable {

    // generiert mit CLI-Tool "serialver swe4.data.Person"
    // ... wird verwendet, um sicherzustellen, dass Serialisierung und Deserialisierung korrekt funktionieren,
    // indem sie sicherstellt, dass das Objekt, das deserialisiert wird, mit der gleichen Version der Klasse kompatibel ist,
    // mit der es serialisiert wurde. Wenn sich die Klasse ändert und die SerialVersionUID nicht aktualisiert wird,
    // kann es zu Inkompatibilitäten beim Deserialisieren kommen.
    private static final long serialVersionUID = -362194812487403915L;

    private String firstName;
    private String lastName;
    private LocalDate dob;

    // transient ... Feld wird ignoriert bei Serialisierung (da Address selbst nicht Serializable ist)
    // beim deserialisieren ist die Adresse dann null
    // Annahme: Address ist nicht unter unserer Kontrolle und kann nicht serialisiert werden
    transient Address address;

    public Person(String firstName, String lastName, LocalDate dob) {
        this(firstName, lastName, dob, null);
    }

    public Person(String firstName, String lastName, LocalDate dob, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void changeLastName(String newLastName) {
        this.lastName = newLastName;
    }

    // wird mittels Reflection gesucht und auf magische Art und Weise aufgerufen
    // Serial ... es wird geprüft, ob es was mit Serializable zu tun hat
    // Wie override, es wird geprüft, ob was dahinter steckt
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        boolean hasAddress = in.readBoolean();
        if (hasAddress) {
            this.address = new Address(in.readInt(), (String)in.readObject(), (String)in.readObject());
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        boolean hasAddress = address != null;
        out.writeBoolean(hasAddress);
        if (hasAddress) {
            out.writeInt(address.getZipCode());
            out.writeObject(address.getCity());
            out.writeObject(address.getStreet());
        }

    }

    @Override
    public Object clone() {
        Person p = null;
        try {
            p = (Person) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("date=" + dob)
                .add("address=" + address)
                .toString();
    }
}
