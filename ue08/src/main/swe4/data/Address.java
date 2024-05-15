package swe4.data;

import java.util.StringJoiner;

// We assume that Address cannot be made serializable because it is not under our control.
public class Address {
  private int    zipCode;
  private String city;
  private String street;

  public Address(int zipCode, String city, String street) {
    super();
    this.zipCode = zipCode;
    this.city = city;
    this.street = street;
  }

  public int getZipCode() {
    return zipCode;
  }

  public void setZipCode(int zipCode) {
    this.zipCode = zipCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  @Override
  public String toString() {
    StringJoiner sj = new StringJoiner(", ", "Address [", "]");
    sj.add("zipCode: " + zipCode);
    sj.add("city: " + city);
    sj.add("street: " + street);
    return sj.toString();
  }
}
