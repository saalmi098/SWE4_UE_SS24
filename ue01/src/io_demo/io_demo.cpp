#include <iostream>
#include <cmath>
#include <iomanip>
#include <format>
#include <fstream>
#include <string>

//using namespace std; // Bad practice (führt zu Namenskonflikte
// absolut verboten vor allem in Header files, man zwingt andere damit)

using std::cout;
using std::endl;

static void formatted_output() {
	auto old_prec = cout.precision(2); // Ausgabe doubles auf 2 signifikante (default = 6 signifikante Stellen)
	// stellt aber precision für gesamte Laufzeit um -> old_precision merken und später wieder zurückstellen
	// ansonsten würde unten pi als "3.1" ausgegeben werden -> ungünstig

	// cout.width(2); // diese Manipulatoren werden intern in Flags gespeichert 
	// cout.setf() // Bitmuster für Flags setzen

	for (int x = 0; x <= 100; x += 20)
	{
		// std::setw(3) ... IO-Manipulator -> nächstes Element, das ausgegeben wird, hat eine Breite von 3
		// std::fixed ... fixe Anzahl an Nachkommastellen (std::scientific ... wissenschaftliche Notation)
		cout << std::setw(3) << x << " " << std::setw(10) << std::fixed << sqrt(x) << endl;
	}

	cout.precision(old_prec);
	
	//cout << "-----------" << std::endl;
	cout << std::format("{0:-<14}", "") << std::endl; // gleiche Ausgabe wie darüber -> gibt 14 Bindestriche aus
	// (Compiler erfordert Angabe ob links- (<) oder rechtsbündig (>)

	// std::format (ab C++ 20)
	// Vorteil zu printf: Compiler kann viel zur Übersetzungszeit prüfen

	for (int x = 0; x <= 100; x += 20)
	{
		// Breitenangabe nach Doppelpunkt (Parameter 0 soll 3 Breit sein)
		// > ... rechtsbündige Ausgabe ("_" direkt davor = Fill Character)
		cout << std::format("{0:_>3} {1:>10.2f}\n", x, sqrt(x));
	}
}

static void file_io() {
	// test.txt wird erstellt in: out/build/x64-Debug/bin
	std::ofstream out("test.txt"); // RAII; out wird implizit geöffnet
	out << "Hinz " << 50 << endl;
	out << "Hunz " << 50 << endl;
	out.close(); // implizites flush

	std::ifstream in("test.txt");
	if (!in) { // gleich wie if (in.fail)
		// Bits bei ifstream:
		// bad_bit, fail_bit, eof_bit
		// fail() prüft die ersten beiden
		// good() prüft alle 3 -> !fail() != good() (wegen Prüfung eof_bit)
		std::cerr << "File 'test.txt' does not exist" << endl;
	}

	std::string name;
	int age;

	// 1) Einlesen in Variablen
	in >> name >> age;
	while (in.good()) {
		cout << "name: " << name << "; age: " << age << endl;
		in >> name >> age;
	}

	in.clear(); // löscht alle Bits
	in.seekg(0, std::ios_base::beg); // beg, end, cur

	// 2) Gesamte Zeile lesen
	std::string line;
	int line_num = 1;
	std::getline(in, line);
	while (in.good()) {
		cout << line_num << ": " << line << endl;

		line_num++;
		std::getline(in, line);
	}

	// in.close() wird implizit durch Zerstörung des Objekts aufgerufen (RAII)
}

int main() {
	// cout vs printf aus C:
	// cout: typsicher; printf: kann formatiert ausgeben (z.B. Tabellen)
	// Unterschied std::endl zu \n: endl flushed (wichtig falls Programm abstürzt)
	cout << "------ formatted_output ------" << endl;
	formatted_output();

	cout << "pi=" << 3.1415927 << endl;

	cout << "------ file_io ------" << endl;
	file_io();
}