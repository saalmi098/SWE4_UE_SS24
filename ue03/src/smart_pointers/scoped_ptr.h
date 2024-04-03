#pragma once

#include <concepts>

template<typename T>
class scoped_ptr final {

  template<typename U> friend class scoped_ptr;

public:
  using value_type = T;

  scoped_ptr() = default;
  explicit scoped_ptr(T *ptr) : m_ptr{ptr} {}

  // prevent copying
  scoped_ptr(const scoped_ptr &) = delete;

  template<typename U>
  scoped_ptr(const scoped_ptr<U> &) = delete;

  scoped_ptr &operator=(const scoped_ptr &) = delete;

  template<typename U>
  scoped_ptr &operator=(const scoped_ptr<U> &) = delete;

  // allow only moving
  template <std::derived_from<T> U> // variant 1
  // template <typename U> requires std::derived_from<U, T>
  scoped_ptr(scoped_ptr<U> &&other) {
    //static_assert(std::is_base_of<T, U>::value,
    //              "scoped_ptr<T>(scoped_ptr<U> &&): U must be T or a subclass of T");
    m_ptr = other.m_ptr;
    other.m_ptr = nullptr;
  }

  template <std::derived_from<T> U>
  scoped_ptr &operator=(scoped_ptr<U> &&other) {
    //static_assert(std::is_base_of<T, U>::value,
    //              "scoped_ptr<T>::operator=(scoped_ptr<U> &&): U must be T or a subclass of T");
    delete m_ptr;
    m_ptr = other.m_ptr;
    other.m_ptr = nullptr;
    return *this;
  }

  ~scoped_ptr() { delete m_ptr; }

  T *get() const { return m_ptr; }

  T &operator*()  const { return *m_ptr; } // deref
  T *operator->() const { return m_ptr;  } // method call (deref)
  operator T*()   const { return m_ptr;  } // cast to pointer

private:
  T* m_ptr{nullptr};
};

template <typename T, typename... Args>
inline scoped_ptr<T> make_scoped(Args&&... args) {
  return scoped_ptr<T>(new T(std::forward<Args>(args)...));
}
