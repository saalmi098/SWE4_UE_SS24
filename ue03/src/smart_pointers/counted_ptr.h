// Counted Pointers: pointers that can share objects
//   through reference counting.

#pragma once

#include <cstddef>
#include <concepts>

template <typename T> 
class counted_ptr final {
public:
  using value_type =  T;

  explicit counted_ptr(T *p = nullptr) : m_ref_cnt{nullptr} {
    if (p) m_ref_cnt = new counter_t(p);
  }

  ~counted_ptr() {
    release();
  }

  counted_ptr(const counted_ptr &r) {
    acquire(r.m_ref_cnt);
  }

  template <std::derived_from<T> U>
  counted_ptr(const counted_ptr<U> &ru) {
    // static_assert(std::is_base_of<T, U>::value, 
    //               "counted_ptr<T>(const counted_ptr<U> &): U must be T or a subclass of T");
    const counted_ptr<T> &r = reinterpret_cast<const counted_ptr<T> &>(ru);
    acquire(r.m_ref_cnt);
  }

  counted_ptr &operator=(const counted_ptr &r) {
    if (this != &r) {
      release();
      acquire(r.m_ref_cnt);
    }
    return *this;
  }

  template <std::derived_from<T> U>
  counted_ptr &operator=(const counted_ptr<U> &ru) {
    //static_assert(std::is_base_of<T, U>::value,
    //              "counted_ptr<T>::operator=(const counted_ptr<U> &): U must be T or a subclass of T");

    if (this != &ru) {
      release();
      acquire(ru.m_ref_cnt);
    }
    return *this;
  }


        T &operator*()       { return *m_ref_cnt->ptr; }
  const T &operator*() const { return *m_ref_cnt->ptr; }
        T *operator->()      { return  m_ref_cnt->ptr; }

private:

  struct counter_t {
    T           *ptr;
    std::size_t  count;
    counter_t(T *p = nullptr, std::size_t c = 1) : ptr{p}, count{c} {}
  };

  counter_t *m_ref_cnt;

  void acquire(counter_t *c) {
    m_ref_cnt = c;
    if (c) ++c->count;
  }

  void release() {
    // decrement the count, delete if it is 0
    if (m_ref_cnt) {
      if (--m_ref_cnt->count == 0) {
        delete m_ref_cnt->ptr;
        delete m_ref_cnt;
        m_ref_cnt = nullptr;
      }
    }
  }
};

template <typename T, typename... Args>
counted_ptr<T> make_counted(Args&&... args) {
  return counted_ptr<T>(new T(std::forward<Args>(args)...));
}
