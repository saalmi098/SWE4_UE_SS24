#include <iostream>
#include <fstream>
#include <set>
#include <string>
#include <algorithm>
#include <iterator>
#include <cctype> // fuer ispunct, isdigit, ...

using std::cout;
using std::cerr;
using std::endl;

// punct ... punctuation character (Sonderzeichen)
// inline ... effizienter
inline bool is_punct(char ch) { return std::ispunct(ch); }
inline bool is_digit(char ch) {	return std::isdigit(ch); }
inline char to_lower(char ch) { return static_cast<char>(std::tolower(ch)); }

template <typename it_t>
void print(it_t first, it_t last, std::ostream& os = cout) {
	while (first != last) {
		os << *first << endl;
		++first;
	}
}

std::string normalize(std::string word) {
	// remove_if löscht nichts, sondern kopiert nur auf ein neues Element
	// und gibt das neue One-Past-End Element zurück.
	// remove_if löscht nicht, weil es nicht weiss, ob dieses Element
	// löschen überhaupt unterstüzt.
	auto new_end = std::remove_if(word.begin(), word.end(), is_punct);
	new_end = std::remove_if(word.begin(), new_end, is_digit);
	word.erase(new_end, word.end());

	// Var. 1
	// std::transform(word.begin(), word.end(), word.begin(), to_lower);
	
	// Var. 2
	std::transform(word.begin(), word.end(), word.begin(), [](char ch) {
		return static_cast<char>(std::tolower(ch));
	});


	return word;
}

int main(int argc, char* argv[]) {
	if (argc != 2) {
		cerr << "usage: " << argv[0] << " <filename>" << endl;
		return 1;
	}

	std::ifstream fin(argv[1]);
	if (!fin) { // !fin == fin.fail()
		cout << "file '" << argv[1] << "' not found!" << endl;
	}

	std::set<std::string> words;

	std::transform(
		std::istream_iterator<std::string>{fin},
		std::istream_iterator<std::string>{},
		std::inserter(words, words.begin()), // verwendet insert() von set
		normalize
	);

	// sort, unique (u. damit erase) nicht mehr notwendig (weil set)

	print(words.begin(), words.end());
}