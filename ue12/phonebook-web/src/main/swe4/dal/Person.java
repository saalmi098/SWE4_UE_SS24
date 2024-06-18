package swe4.dal;

import java.io.Serializable;

public class Person implements Serializable {

  private static final long serialVersionUID = -7465306074426456777L;

  private int    id;         // -1: object not stored yet
  private String firstName;
  private String lastName;
  private String address;
  private String phoneNumber;

  public Person(int id, String firstName, String lastName, String address,
      String phoneNumber) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.phoneNumber = phoneNumber;
  }

  public Person(String fn, String ln, String a, String pn) {
    this(-1, fn, ln, a, pn);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String toString() {
    return String.format("(%s) %s %s; %s; %s", id, firstName, lastName, address,
        phoneNumber);
  } // toString
}
