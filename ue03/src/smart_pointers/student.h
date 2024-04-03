#pragma once

#include <iostream>
#include <string>

#include "student.h"

class student : public person {

public:
  student(std::string fn, std::string ln, std::string mn) :
      person  {std::move(fn), std::move(ln)},
      m_mat_nr{std::move(mn)} {

    std::cout << "student(" << m_firstname << "," << m_lastname
              << "," << m_mat_nr << ")" << std::endl;
  }

  student(const student &s) : 
      person{s}, 
      m_mat_nr{s.m_mat_nr} {
    
    std::cout << "student(" << m_firstname << "," << m_lastname << ","
              << m_mat_nr << ") - copy constructor" << std::endl;
  }

  // move constructor
  student(student &&s) noexcept :
      person{std::move(s)},
      m_mat_nr{std::move(m_mat_nr)} {

    std::cout << "student(" << m_firstname << "," << m_lastname << ","
              << m_mat_nr << ") - move constructor" << std::endl;
  }

  virtual ~student() {
    std::cout << "~student(" << m_firstname << "," << m_lastname
              << "," << m_mat_nr << ")" << std::endl;
  }

protected:
  virtual void print(std::ostream &os) const {
    os << m_firstname << " " << m_lastname << " (" << m_mat_nr << ")" << std::flush;
  }

  std::string m_mat_nr;
};
