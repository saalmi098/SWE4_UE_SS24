package swe4.client;

import swe4.collections.EmptyListException;
import swe4.collections.SLList;

public class SLListClient {

    // ant clean compile
    // ant run
    // ant run-unit-tests

    public static void main(String[] args) {
        System.out.println("===== START ===== ");

        try {
            SLList<String> cities = new SLList<>();
            //cities.append("Linz");
            //cities.append("Hagenberg");
            //cities.append("Salzburg");
            //System.out.printf("cities.first() = %s%n", cities.first()); // printf!!


            cities.first();
        } catch (EmptyListException e) {
            System.out.println("===== CATCH ===== ");
            System.out.println("EmptyListException: " + e.getMessage());
        }
        finally {
            System.out.println("===== FINALLY ===== ");
        }

        System.out.println("===== END ===== ");
    }
}
