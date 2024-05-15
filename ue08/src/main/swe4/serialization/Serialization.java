package swe4.serialization;

import swe4.data.Address;
import swe4.data.Person;

import java.io.*;
import java.time.LocalDate;

public class Serialization {
    public static void main(String[] args) {
        if (args.length != 1) {
            showUsageMessageAndExit();
        }

        try {
            switch (args[0]) {
                case "-s" -> {
                    try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream("person.ser"))) {
                        Address address = new Address(1010, "Wien", "Hauptstrasse");
                        Person person = new Person("Franz", "Klammer", LocalDate.of(1950, 3, 4), address);
                        out.writeObject(person);
                        System.out.printf("%s serialized%n", person);
                        out.flush();
                    }
                }
                case "-d" -> {
                    try (ObjectInput in = new ObjectInputStream(new FileInputStream("person.ser"))) {
                        Person person = (Person)in.readObject();
                        System.out.printf("%s deserialized%n", person);
                    }
                }
                default -> {
                    showUsageMessageAndExit();
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void showUsageMessageAndExit() {
        System.out.printf("usage: java %s (-s|-d)", Serialization.class.getName());
        System.exit(-1);
    }
}
