#include <algorithm>
#include <chrono>
#include <iostream>
#include <random>
#include <algorithm>
#include <vector>
#include <set>

#include "random_number_generator.h"

using std::cerr;
using std::cout;
using std::endl;

using watch = std::chrono::high_resolution_clock;

constexpr int line_length = 75;
constexpr int max_elements = 15;

static void print_header(std::string text) {
	if (text.length() + 2 > line_length) {
		cout << text << endl;
		return;
	}

	text = " " + text + " ";
	std::string line(line_length, '-');
	int l = (int) line.length() - (int) text.length();
	std::copy(text.begin(), text.end(), line.begin() + l / 2);
	cout << line << endl;
}

static double time_diff(std::chrono::time_point<watch> from,
												std::chrono::time_point<watch> to) {
	return std::chrono::duration<double>(to - from).count(); // seconds
}

static void test_random_number_generators() {
	std::random_device random;
	std::default_random_engine engine1;
	std::default_random_engine engine2{ random() }; // Startwert (seed) wird durch random() gesetzt
	std::uniform_int_distribution<int> distrib{ 0, 10 }; // Gleichverteilung

	for (int i = 0; i < 10; i++) {
		//cout << random() << " ";

		// reproduzierbar (bei jedem Neustart kommt immer das selbe raus)
		//cout << engine1() << " ";

		// nicht mehr reproduzierbar wegen Random-Seed
		//cout << engine2() << " ";

		// mit Engine wird nächste Zufallszahl ermittelt und in diese Verteilung gepresst
		cout << distrib(engine2) << " ";
	}
	cout << endl;

	random_number_generator<int> rand{ 0, 10 };
	for (int i = 0; i < 10; i++) {
		cout << rand() << " ";
	}
	cout << endl;
}

template <typename It>
static void print(It from, It to) {
	for (It it = from; it != to; ++it) {
		cout << *it << " ";
	}
	cout << endl;
}

static void vector_demo() {
	random_number_generator<int> rand{ 0, max_elements };
	std::vector<int> v;

	for (int i = 0; i < max_elements; i++) {
		v.push_back(rand()); // O(1) amortisiert (grundsätzlich konstant, aber falls
		// Vektor vergrössert werden muss linear, weil Vektor kopiert werden muss)
	}

	print(v.begin(), v.end());

	cout << "contained: ";
	for (int i = 0; i < max_elements; i++) {
		if (std::find(v.begin(), v.end(), i) != v.end()) {
			// wenn find(...) != v.end() -> wir haben das Element gefunden (ansonsten nicht)
			cout << i << " ";
		}
	}
	cout << endl;
}

static void set_demo() {
	// Set beinhaltet nur eindeutige Elemente (distinct)
	// ist sortiert

	random_number_generator<int> rand{ 0, max_elements };
	std::set<int> v;

	for (int i = 0; i < max_elements; i++) {
		v.insert(rand()); // O(log n)
	}

	print(v.begin(), v.end());

	cout << "contained: ";
	for (int i = 0; i < max_elements; i++) {
		//if (std::find(v.begin(), v.end(), i) != v.end()) {
		// std::find möglich, aber sehr schlecht (Baum linear zu durchsuchen) -> v.find verwenden
		// (mögliche Klausurfrage)

		if (v.find(i) != v.end()) { // O(log n)
			cout << i << " ";
		}
	}
	cout << endl;
}

int main() {
	print_header("Random number generators");
	test_random_number_generators();

	print_header("vector demo");
	vector_demo();

	print_header("set demo");
	set_demo();
}

