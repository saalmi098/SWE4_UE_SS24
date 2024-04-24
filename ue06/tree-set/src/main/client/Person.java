package client;

import java.util.Objects;

public class Person implements Comparable<Person> {
  private final String firstName;
  private final String lastName;
  private final int    age;

  public Person(String firstName, String lastName, int age) {
    this.firstName = firstName;
    this.lastName  = lastName;
    this.age       = age;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getAge() {
    return age;
  }

  @Override
  public String toString() {
    return String.format("%s %s (%d)", firstName, lastName, age);
  }

  // equals, hashCode, and compareTo should be implemented
  // in a robust and consistent manner.
  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof Person otherPerson)) return false;
    return compareTo(otherPerson) == 0;
  }

  @Override
  public int compareTo(Person other) {
    if (lastName != null) {
      if (other.lastName == null) return 1;
      int cmp = lastName.compareTo(other.lastName);
      if (cmp != 0) return cmp;
    }
    if (firstName != null) {
      if (other.firstName == null) return 1;
      return firstName.compareTo(other.firstName);
    }
    return 0;
  }
}
