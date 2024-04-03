#pragma once

#include <iostream>
#include <string>

class person {
  friend std::ostream &operator<<(std::ostream &os, const person &p);

public:

  person() {
    std::cout << "person()" << std::endl;
  }

  person(std::string fn, std::string ln) :
      m_firstname{std::move(fn)},
      m_lastname{std::move(ln)} {
    
    std::cout << "person(" << m_firstname << "," << m_lastname << ")" << std::endl;
  } // person

  person(const person &p) : 
      m_firstname{p.m_firstname}, 
      m_lastname{p.m_lastname} {
    
    std::cout << "person(" << m_firstname << "," << m_lastname
              << ") - copy constructor" << std::endl;
  } // person

  // move constructor
  person(person &&p) noexcept :
      m_firstname{std::move(p.m_firstname)},
      m_lastname{std::move(p.m_lastname)} {
    
    std::cout << "person(" << m_firstname << "," << m_lastname
              << ") - move constructor" << std::endl;
  } // person

  virtual ~person() {
    std::cout << "~person(" << m_firstname << "," << m_lastname << ")" << std::endl;
  }

  bool operator < (const person& other) const {
      return this->m_lastname < other.m_lastname ||
          this->m_lastname == other.m_lastname && this->m_firstname < other.m_firstname;
  }

protected:
  virtual void print(std::ostream &os) const {
    os << m_firstname << " " << m_lastname << std::flush;
  }

  std::string m_firstname;
  std::string m_lastname;
}; // person

inline std::ostream &operator<<(std::ostream &os, const person &p) {
  p.print(os);
  return os;
} // operator<<

