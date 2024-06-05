package swe4.dal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;

public class PhoneBookBL {
    // private static final String CONNECTION_STRING =
    // "jdbc:derby://localhost/PhoneBookDb";
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost/PhoneBookDb?autoReconnect=true&useSSL=false";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = null;

    public static String promptFor(BufferedReader in, String p) {
        System.out.print(p + "> ");
        try {
            return in.readLine();
        } catch (Exception e) {
            return promptFor(in, p);
        } // try/catch
    } // prompt

    public static void main(String[] args) {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        String userCmd;

        try (PersonDao personDao = new PersonDaoJdbc(CONNECTION_STRING, USER_NAME, PASSWORD)) {

            System.out.println();
            System.out.println("currently " + personDao.getCount() + " entries in phone book");

            System.out.println();
            System.out.println("valid commands: quit, list, find, insert, update and delete");
            System.out.println();

            userCmd = promptFor(in, "");

            while (!userCmd.equals("quit")) {

                int id;
                String lastName;
                Person person;

                switch (userCmd) {

                    case "list" -> {
                        for (Person p : personDao.getAll()) {
                            System.out.println(p);
                        } // while
                    }

                    case "find" -> {
                        lastName = promptFor(in, "  last name ");
                        Collection<Person> persons = personDao.get(lastName);
                        for (Person p : persons)
                            System.out.println(p);
                        if (persons.isEmpty())
                            System.out.println("  no entries with last name " + lastName + " found");
                    }

                    case "insert" -> {
                        person =
                                new Person(promptFor(in, "  first name   "), promptFor(in, "  last name    "),
                                        promptFor(in, "  address      "), promptFor(in, "  phone number "));
                        personDao.store(person);
                        System.out.printf("inserted new person <%s>%n", person);
                    }

                    case "update" -> {
                        id = Integer.parseInt(promptFor(in, "  id "));
                        person = personDao.get(id);
                        if (person != null) {
                            System.out.println("  " + person);
                            person.setAddress(promptFor(in, "  new address "));
                            personDao.update(person);
                        } else
                            System.out.println("  no entry with id " + id);
                    }

                    case "delete" -> {
                        id = Integer.parseInt(promptFor(in, "  id "));
                        personDao.delete(id);
                    }

                    default -> {
                        System.out.println("ERROR: invalid command");
                    }
                } // switch

                userCmd = promptFor(in, "");

            } // while
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

    } // main

} // PhoneBookApplicationDAL

