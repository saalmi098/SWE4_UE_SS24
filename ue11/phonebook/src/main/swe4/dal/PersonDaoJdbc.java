package swe4.dal;

import java.sql.*;
import java.util.*;

public class PersonDaoJdbc implements PersonDao {

    private Connection connection;
    private String connectionString;
    private String userName;
    private String password;

    public PersonDaoJdbc(String connectionString) {
        this(connectionString, null, null);
    }

    public PersonDaoJdbc(String connectionString, String userName, String password) {
        this.connectionString = connectionString;
        this.userName = userName;
        this.password = password;
    }

    public Connection getConnection() throws DataAccessException {
        try {
            if (connection == null)
                connection = DriverManager.getConnection(connectionString, userName, password);
            return connection;
        } catch (SQLException ex) {
            throw new DataAccessException("Can't establish connection to database. SQLException: "
                    + ex.getMessage());
        }// try/catch
    } // getConnection

    /**
     * @return number of records in table Person
     */
    public int getCount() throws DataAccessException {
        int count = 0;
        try (Statement statement = getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery("select count(id) as count from Person")) {
            if (resultSet.next()) count = resultSet.getInt(1);
        } catch (SQLException ex) {
            throw new DataAccessException("SQLException: " + ex.getMessage());
        } // includes finally resultSet.close(); statement.close();
        return count;
    }

    /**
     * @return collection containing all Persons matching whereClause
     */
    private Collection<Person> getPersonWhere(String query, Object... args)
            throws DataAccessException {
        Collection<Person> c = new ArrayList<Person>();
        try (PreparedStatement statement = getConnection().prepareStatement("select * from Person " + query)) {
            for (int i = 0; i < args.length; i++)
                statement.setObject(i + 1, args[i]);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    c.add(new Person(resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("address"),
                            resultSet.getString("phone_number")));
                }
            } // includes finally resultSet.close();
        } catch (SQLException ex) {
            throw new DataAccessException("SQLException: " + ex.getMessage());
        } // includes finally statement.close()
        return c;
    }

    /**
     * @return Person matching id
     */
    public Person get(int id) throws DataAccessException {
        Collection<Person> c = getPersonWhere("where id = ?", id);
        Iterator<Person> it = c.iterator();
        return it.hasNext() ? it.next() : null;
    }

    /**
     * @return collection containing all Persons with lastName
     */
    public Collection<Person> get(String lastName) {
        return getPersonWhere("where last_name like ?", lastName);
    }

    /**
     * @return collection containing all Persons
     */
    public Collection<Person> getAll() {
        return getPersonWhere("");
    }

    /**
     * deletes Person with id
     */
    public void delete(int id) throws DataAccessException {
        try (PreparedStatement statement =
                     getConnection().prepareStatement("delete FROM Person where id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("SQLException: " + ex.getMessage());
        } // includes finally statement.close();
    }

    public void store(Person p) throws DataAccessException {
        if (p.getId() != -1) throw new DataAccessException("Object can't be inserted twice.");
        try (PreparedStatement statement =
                     getConnection().prepareStatement("insert into Person "
                                     + "(first_name, last_name, address, phone_number) values (?, ?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, p.getFirstName());
            statement.setString(2, p.getLastName());
            statement.setString(3, p.getAddress());
            statement.setString(4, p.getPhoneNumber());
            // 1. insert the new entry
            statement.executeUpdate();
            // 2. now get the auto generated id for the new entry
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet != null && resultSet.next())
                    p.setId(resultSet.getInt(1));
                else
                    throw new DataAccessException("Auto generated keys not supported.");
            } // includes finally resultSet.close();
        } catch (SQLException ex) {
            throw new DataAccessException("SQLException: " + ex.getMessage());
        } // includes finally statement.close();
    }

    /**
     * update object's data in data base
     */
    public void update(Person p) throws DataAccessException {
        if (p.getId() == -1) throw new DataAccessException("Can't update nonexisting object.");
        try (PreparedStatement statement =
                     getConnection().prepareStatement("update Person SET first_name=?, last_name=?, address=?, phone_number=?"
                             + "where id = ?")) {
            statement.setString(1, p.getFirstName());
            statement.setString(2, p.getLastName());
            statement.setString(3, p.getAddress());
            statement.setString(4, p.getPhoneNumber());
            statement.setInt(5, p.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("SQLException: " + ex.getMessage());
        } // includes finally statement.close();
    }

    @Override
    public void close() throws DataAccessException {
        try {
            if (connection != null) connection.close();
            connection = null;
        } catch (SQLException ex) {
            throw new DataAccessException("Problems closing database connection: SQLException: " + ex.getMessage());
        } // catch
    }

} // PersonDaoJdbc