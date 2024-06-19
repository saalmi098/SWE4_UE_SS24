package swe4.domain;

import swe4.mapper.Column;
import swe4.mapper.Table;

import java.io.Serializable;

@Table(name = "tbl_person")
public class Person implements Serializable {

  @Column(isKey = true) // wenn kein name angegeben ist, wird der Feldname verwendet (id)
  private int id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column // wenn kein name angegeben ist, wird der Feldname verwendet (address)
  private String address;

  @Column(name = "phone_number")
  private String phoneNumber;

  public Person() {
  }

  public Person(int id, String firstName, String lastName, String address,
                String phoneNumber) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.phoneNumber = phoneNumber;
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
    return String.format("(%s) %s %s; %s; %s", id, firstName, lastName, address, phoneNumber);
  }
}
