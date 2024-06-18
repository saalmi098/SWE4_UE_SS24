package swe4.dal;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class PersonDaoJdbc implements PersonDao {
  
  private DataSource ds;

  public PersonDaoJdbc(DataSource ds) {
    this.ds = ds;
  }

  /**
   * @return number of records in table Person
   */
  public int getCount() throws DataAccessException {
    int count = 0;
    try (Connection conn = ds.getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet =
            statement.executeQuery("select count(id) as count from Person")) {
      if (resultSet.next()) count = resultSet.getInt(1);
    }
    catch (SQLException ex) {
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
    Logger.getAnonymousLogger().info("Query: select * from Person " + query);
    
    try (Connection conn = ds.getConnection();
        PreparedStatement statement =
            conn.prepareStatement("select * from Person " + query)) {
      // set value for place holders
      for (int i = 0; i < args.length; i++) {
        statement.setObject(i + 1, args[i]);
        Logger.getAnonymousLogger().info("   argument: " + args[i]);
      }
      
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          c.add(new Person(resultSet.getInt("id"), resultSet.getString("first_name"),
              resultSet.getString("last_name"), resultSet.getString("address"),
              resultSet.getString("phone_number")));
        }
      } // includes finally resultSet.close();
    }
    catch (SQLException ex) {
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
    return getPersonWhere("WHERE last_name LIKE ?", lastName);
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
    try (Connection conn = ds.getConnection();
        PreparedStatement statement =
            conn.prepareStatement("DELETE FROM Person WHERE id = ?")) {
      statement.setInt(1, id);
      statement.executeUpdate();
    }
    catch (SQLException ex) {
      throw new DataAccessException("SQLException: " + ex.getMessage());
    } // includes finally statement.close();
  }

  public void store(Person p) throws DataAccessException {
    if (p.getId() != -1)
      throw new DataAccessException("Object can't be inserted twice.");
    try (Connection conn = ds.getConnection();
        PreparedStatement statement = conn.prepareStatement(
            "insert into Person " +
                "(first_name, last_name, address, phone_number) values (?, ?, ?, ?)",
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
    }
    catch (SQLException ex) {
      throw new DataAccessException("SQLException: " + ex.getMessage());
    } // includes finally statement.close();
  }

  /**
   * update object's data in data base
   */
  public void update(Person p) throws DataAccessException {
    if (p.getId() == -1)
      throw new DataAccessException("Can't update nonexisting object.");
    try (Connection conn = ds.getConnection();
        PreparedStatement statement = conn.prepareStatement(
            "update Person SET first_name=?, last_name=?, address=?, phone_number=?" +
                "where id = ?")) {
      statement.setString(1, p.getFirstName());
      statement.setString(2, p.getLastName());
      statement.setString(3, p.getAddress());
      statement.setString(4, p.getPhoneNumber());
      statement.setInt(5, p.getId());
      statement.executeUpdate();
    }
    catch (SQLException ex) {
      throw new DataAccessException("SQLException: " + ex.getMessage());
    } // includes finally statement.close();
  }

  @Override
  public void close() {
  }

} // PersonDaoJdbc
