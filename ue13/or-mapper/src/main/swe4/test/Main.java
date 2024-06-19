package swe4.test;

import swe4.domain.Person;
import swe4.mapper.PersistenceManager;
import swe4.mapper.QueryBuilder;
import swe4.mapper.SimpleQueryBuilder;

import java.sql.SQLException;

public class Main {

    private static final String CONNECTION_STRING = "jdbc:mysql://localhost/PhoneBookDb?autoReconnect=true&useSSL=false";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = null;

    public static void main(String[] args) throws SQLException {
//        Person p = new Person();
//        SimpleQueryBuilder builder = new SimpleQueryBuilder(p.getClass());

        System.out.println("===== SimpleQueryBuilder =====");
        SimpleQueryBuilder simpleQueryBuilder = new SimpleQueryBuilder(Person.class);
        System.out.printf("tableName(Person) = %s%n", simpleQueryBuilder.getTableName());
        System.out.printf("insertQuery(Person) = '%s'%n", simpleQueryBuilder.buildInsertQuery());
        System.out.printf("selectByIdQuery(Person) = '%s'%n", simpleQueryBuilder.buildSelectByIdQuery());
        System.out.println();

        System.out.println("===== QueryBuilder =====");
        QueryBuilder queryBuilder = new QueryBuilder(Person.class);
        System.out.printf("tableName(Person) = %s%n", queryBuilder.getTableName());
        System.out.printf("insertQuery(Person) = '%s'%n", queryBuilder.buildInsertQuery());
        System.out.printf("selectByIdQuery(Person) = '%s'%n", queryBuilder.buildSelectByIdQuery());

        System.out.println("===== PersistenceManager =====");
        PersistenceManager pm = new PersistenceManager(CONNECTION_STRING, USER_NAME, PASSWORD);
        Person p1 = pm.get(Person.class, 1);
        System.out.printf("person with id 1: %s%n", p1);
    }
}
