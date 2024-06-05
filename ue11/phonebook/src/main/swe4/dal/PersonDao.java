package swe4.dal;
import java.util.Collection;

// DAO interface for accessing Person table
public interface PersonDao extends AutoCloseable {
  int getCount()                          throws DataAccessException;
  Person get(int id)                      throws DataAccessException;
  Collection<Person> get(String lastName) throws DataAccessException;
  Collection<Person> getAll()             throws DataAccessException;
  void delete(int id)                     throws DataAccessException;
  void store(Person p)                    throws DataAccessException;
  void update(Person p)                   throws DataAccessException;
}