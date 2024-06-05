package swe4.dal;

@SuppressWarnings("serial")
public class DataAccessException extends RuntimeException {
  public DataAccessException(String msg) {
    super(msg);
  }
}
