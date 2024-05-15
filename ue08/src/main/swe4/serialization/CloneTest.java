package swe4.serialization;

import swe4.data.Person;

import java.time.LocalDate;

public class CloneTest {

    public static void main(String[] args) {
        Person p1 = new Person("Franz", "Klammer", LocalDate.of(1950, 3, 4));
        Person p2 = p1;
        Person p3 = (Person)p1.clone();

        p1.changeLastName("Klammer-Huber");

        System.out.printf("p1 = %s%n", p1);
        System.out.printf("p2 = %s%n", p2);
        System.out.printf("p3 = %s%n", p3);
    }
}
