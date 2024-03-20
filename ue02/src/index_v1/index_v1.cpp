#include <iostream>
#include <fstream>
#include <vector>
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

	std::vector<std::string> words;

	// Var. 1)
	/*std::copy(std::istream_iterator<std::string>{fin},
		std::istream_iterator<std::string>{},
		// words.begin()); [1]
		std::back_inserter(words));

	// [1] ... liefert Absturz, da Vector.size = 0 ist und somit copy beim Dereferenzierne
	// des Iterators (*it = ... siehe CPPReference std::copy) abstürzt
	// daher back_inserter verwenden, dieser macht aus *it = value --> words.push_back(value)
	// back_inserter ... muss ein Behaelter sein, der push_back() unterstützt

	// Sonderzeichen und Zahlen loeschen, in Kleinschreibung konvertieren
	std::transform(words.begin(), words.end(), words.begin(), normalize);*/

	// Var. 2) Optimierte/effiziertere Variante
	std::transform(
		std::istream_iterator<std::string>{fin},
		std::istream_iterator<std::string>{},
		std::back_inserter(words),
		normalize
	);

	// Sortieren
	std::sort(words.begin(), words.end());

	// unique() setzt voraus, dass Elemente sortiert sind
	// unique verhält sich wie remove_if (löscht nicht, sondern kopiert nach vorne
	// daher müssen wir alles ab new_end löschen)
	auto new_end = std::unique(words.begin(), words.end());
	words.erase(new_end, words.end());

	print(words.begin(), words.end());
	// oder: (macht genau das selbe; ist für Datenstrukturen, die kein begin/end bieten)
	// print(std::begin(words), std::begin(words));
}