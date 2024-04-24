package client;

import swe4.collections.BSTMultiSet;
import swe4.collections.SortedMultiSet;

import java.util.Iterator;

public class MultiSetClient {
    public static void main(String[] args) {
        SortedMultiSet<Person> personSet = new BSTMultiSet<>();

        System.out.println("==== add ====");
        personSet.add(new Person("Franz", "Mayer", 30));
        personSet.add(new Person("Susi", "Kunz", 35));
        personSet.add(new Person("Tamara", "Hinz", 21));

        System.out.println("==== iterator ====");
        Iterator<Person> persIt = personSet.iterator();
        while (persIt.hasNext()) {
            Person p = persIt.next();
            System.out.println(p);
        }

        System.out.println("==== range-based for loop ====");
        for (Person p : personSet) {
            System.out.println(p);
        }

        System.out.println("==== persons sorted by age ====");
        SortedMultiSet<Person> personSortedByAgeDesc = new BSTMultiSet<>((Person p1, Person p2) -> p2.getAge() - p1.getAge());
        personSortedByAgeDesc.add(new Person("Franz", "Mayer", 30));
        personSortedByAgeDesc.add(new Person("Susi", "Kunz", 35));
        personSortedByAgeDesc.add(new Person("Tamara", "Hinz", 21));
        for (Person p : personSortedByAgeDesc) {
            System.out.println(p);
        }
    }
}
