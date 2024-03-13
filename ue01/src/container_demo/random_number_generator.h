#pragma once

#include <random>

template<typename T>
class random_number_generator {
public:
	random_number_generator(T from, T to, bool random_seed = true)
		: m_engine{ random_seed ? std::random_device{}() : 0 },
		m_distrib{ from, to } {
	}

	T operator()() {
		return m_distrib(m_engine);
	}

private:
	std::default_random_engine m_engine;
	std::uniform_int_distribution<T> m_distrib;
};