#include <iostream>
#include <algorithm>
#include <iterator>
#include <list>
#include <fstream>
#include <map>
#include <set>

using std::cout;
using std::endl;
using std::string;

struct EntryT {
	string name;
	string date;
};
typedef std::list<EntryT> EntriesT;
typedef std::map<std::string, std::set<std::string>> TimesT;

std::istream& operator >> (std::istream& is, EntryT& e) {
	is >> e.name;
	is >> e.date;
	return is;
}

void Print(EntriesT& const entries) {
	for (EntryT e : entries) {
		cout << "Name: " << e.name << ", Date: " << e.date << endl;
	}
}

void Print(TimesT& const timesT) {
	for (const std::pair<const std::string, std::set<std::string>>& pair : timesT) {
		cout << "Name: " << pair.first << ", Dates: " << endl;
		for (string const& time : pair.second) {
			cout << "  " << time << endl;
		}
	}
}

EntriesT ReadEntries(std::istream& is) {
	EntriesT list;
	while (is.good()) {
		EntryT e;
		is >> e;
		list.push_back(e);
	}
	return list;
}

TimesT ConvertEntries(const EntriesT& entries) {
	TimesT timesT;
	/*for (EntryT e : entries) {
		timesT[e.name].insert(e.date);
	}*/
	std::for_each(entries.begin(), entries.end(), [&timesT](const EntryT& e) {
		timesT[e.name].insert(e.date);
	});
	return timesT;
}

int main() {
	std::ifstream is{ "entries.txt" };
	EntriesT entries = ReadEntries(is);
	Print(entries);

	cout << "------------------" << endl;

	TimesT times = ConvertEntries(entries);
	Print(times);
}