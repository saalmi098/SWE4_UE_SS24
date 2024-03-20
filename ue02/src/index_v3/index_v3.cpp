#include <iostream>
#include <fstream>
#include <sstream>
#include <set>
#include <map>
#include <string>
#include <algorithm>
#include <iterator>
#include <cctype> // fuer ispunct, isdigit, ...
#include <iomanip>

using word_map = std::map<std::string, std::set<int>>;

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

class word_adder {
public:
	word_adder(word_map& words, int line_num) : m_words{ words }, m_line_num{ line_num } {
	}

	void operator()(std::string word) {
		word = normalize(word);
		if (!word.empty()) {
			m_words[word].insert(m_line_num);
			// falls m_words[word] (noch) nicht existiert, wird Default-Konstruktor aufgerufen
			// und damit ein leeres Set erstellt; in dieses wird m_line_num eingefuegt
		}
	}
private:
	word_map& m_words;
	int m_line_num;
};

int main(int argc, char* argv[]) {
	if (argc != 2) {
		cerr << "usage: " << argv[0] << " <filename>" << endl;
		return 1;
	}

	std::ifstream fin(argv[1]);
	if (!fin) { // !fin == fin.fail()
		cout << "file '" << argv[1] << "' not found!" << endl;
	}

	word_map words;

	int line_num = 0;
	std::string line;
	while (fin.good()) { // solange File-Ende nicht erreicht
		line_num++;
		std::getline(fin, line);
		std::istringstream line_in(line);

		word_adder adder{ words, line_num };
		std::for_each(
			std::istream_iterator<std::string>{line_in},
			std::istream_iterator<std::string> {},
			adder);
	}

	for (auto& [word, line_nums] : words) {
		cout << std::setw(15) << word << ": ";
		for (int lnum : line_nums)
			cout << lnum << " ";

		cout << endl;
	}

	cout << line_num << ": " << line << endl;
}