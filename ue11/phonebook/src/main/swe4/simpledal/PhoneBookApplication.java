package swe4.simpledal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

public class PhoneBookApplication {

    private static final String CONNECTION_STRING = "jdbc:mysql://localhost/PhoneBookDb?autoReconnect=true&useSSL=false";
    private static final String USER_NAME = "root"; // nicht fuer produktiven Einsatz geeignet
    private static final String PASSWORD = null; // moeglich, da MySQL-Server-Login in Docker ohne Passwort konfiguriert ist

    private static String promptFor(BufferedReader in, String prompt) {
        System.out.print(prompt + "> ");

        try {
            return in.readLine();
        } catch (Exception e) {
            return promptFor(in, prompt);
        }
    }

    public static void main(String args[]) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String allowedCmds = "commands: quit, list, find, insert, update, delete, meta";
        String userCmd;
        PreparedStatement findStatement = null;

        System.out.printf("Opening connection to %s ... %n", CONNECTION_STRING);
        long start = System.nanoTime();

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER_NAME, PASSWORD)) {

            long end = System.nanoTime();
            System.out.printf("Time for creating DB connection: %.2f s%n", (end - start) / 1e9);

            System.out.println();
            System.out.println(allowedCmds);
            System.out.println();

            userCmd = promptFor(in, "");

            while (!userCmd.equals("quit")) {

                switch (userCmd) {
                    case "list" -> {
                        list(connection);
                    }
                    case "find" -> {
                        if (findStatement == null) {
                            String sql = "select * from Person where last_name like ?;";
                            findStatement = connection.prepareStatement(sql);
                        }

                        String pattern = promptFor(in, "   last name");
                        find(findStatement, pattern);
                    }
                    case "insert" -> {
                        String firstName = promptFor(in, "   first name");
                        String lastName = promptFor(in, "   last name");
                        String address = promptFor(in, "   address");
                        String phoneNumber = promptFor(in, "   phone number");
                        insert(connection, firstName, lastName, address, phoneNumber);
                    }

                    default -> {
                        System.out.println("ERROR: invalid command; " + allowedCmds);
                    }
                }

                userCmd = promptFor(in, "");
            }
        } // calls connection.close() automatically (AutoCloseable)
        catch (SQLException e) {
            while (e != null) {
                System.out.println(e);
                e = e.getNextException(); // iteriert durch die SQLException-Chain
            }
        } finally {
            System.out.printf("Closing connection to %s ... %n", CONNECTION_STRING);
        }
    }

    private static void insert(Connection connection, String firstName, String lastName, String address, String phoneNumber) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "insert into Person (first_name, last_name, address, phone_number) values('%s', '%s', '%s', '%s');".formatted(firstName, lastName, address, phoneNumber);
            statement.executeUpdate(sql);
        }
    }

    private static void list(Connection connection) throws SQLException {
        String sql = "select * from Person;";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                System.out.printf("   %d, %s %s, %s, %s%n",
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone_number"));
            }
        }
    }

    private static void find(PreparedStatement findStatement, String pattern) throws SQLException {
        findStatement.setString(1, pattern);

        String sql = "select * from Person where last_name like '%s';".formatted(pattern);
        try (ResultSet resultSet = findStatement.executeQuery(sql)) {

            while (resultSet.next()) {
                System.out.printf("   %d, %s %s, %s, %s%n",
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone_number"));
            }
        }
    }
}
