package swe4.mapper;

public class DataAccessException extends RuntimeException {
  public DataAccessException(String message) {
    super(message);
  }
}
