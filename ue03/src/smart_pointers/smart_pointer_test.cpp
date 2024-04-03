#include <iostream>
#include <fstream>
#include <string>
#include <memory>
#include <set>
#include <iterator>
#include <algorithm>

#include "person.h"
#include "student.h"

#include "scoped_ptr.h"
#include "counted_ptr.h"

using std::cout;
using std::endl;
using std::flush;

// für Sortierung von scoped_ptr
template <typename T>
static bool operator < (const scoped_ptr<T>& sp1, const scoped_ptr<T>& sp2) {
	return *sp1 < *sp2;
}

// für Sortierung von counted_ptr
template <typename T>
static bool operator < (const counted_ptr<T>& cp1, const counted_ptr<T>& cp2) {
	return *cp1 < *cp2;
}

// Var. 1) für Sortierung von std::shared_ptr
//template <typename T>
//static bool operator < (const std::shared_ptr<T>& sp1, const std::shared_ptr<T>& sp2) {
//	return *sp1 < *sp2;
//}

// Var. 2) für Sortierung von std::shared_ptr
template <typename T>
struct pointee_less {
	bool operator()(const std::shared_ptr<T>& sp1, const std::shared_ptr<T>& sp2) const {
		return *sp1 < *sp2;
	}
};

//-----------------------------------------------------------------------------
static void static_collections_test() {
	std::set<person> pers_set;

	// wenn man keine Vergleichsfunktion angibt, verwendet das Set beim Insert
	// standardmaessig den < Operator -> muss in Person überladen werden (wichtig: als const!)
	pers_set.insert(person("Karl", "Schranz"));
	pers_set.insert(person("Franz", "Klammer"));
	pers_set.insert(person("Annemarie", "Moser-Proell"));

	// Slicing!! (aus student wird person -> person-Destruktor statt student-Destruktor
	// wird aufgerufen (weil es ein statisches Objekt ist, kein Pointer) -> mat-nr bleibt hängen
	pers_set.insert(student("Susi", "Lustig", "se007"));

	// Ausgabe mittels STL-Copy (ist eine Variante, aber nicht effizienter etc.):
	// begin(...) und end(...) -> etwas allgemeiner als set.begin()
	// (funktioniert z.B. auch auf C-Arrays)
	// man kann begin statt std::begin schreiben, da pers_set in std:: ist ("Argument-dependend-lookup)
	std::copy(begin(pers_set), end(pers_set), std::ostream_iterator<person>(cout, "\n"));
}

void f(person*) {

}

//-----------------------------------------------------------------------------
static void simple_ptr_test() {
	std::set<person*> pers_ptr_set;

	// Hinweis: In C++ immer skeptisch sein, wenn man "new" schreibt (hinterfragen, ob notwendig)
	pers_ptr_set.insert(new person("Karl", "Schranz"));
	pers_ptr_set.insert(new person("Franz", "Klammer"));
	pers_ptr_set.insert(new person("Annemarie", "Moser-Proell"));
	pers_ptr_set.insert(new student("Susi", "Lustig", "se007"));
	pers_ptr_set.insert(new person("Marcel", "Hirscher"));

	// Typ von auto in diesem Fall: std::set<person*>::iterator
	for (auto it = pers_ptr_set.begin(); it != pers_ptr_set.end(); ++it) {
		// *it ... Pointer auf Person
		// **it ... Person, die hinter Pointer steckt
		cout << **it << endl;

		// Ausgabe erfolgt nicht nach Person sortiert, sondern nach Pointer-Adressen sortiert
		// (es werden Pointer verglichen)
	}

	// Kopieren
	std::set<person*> pers_ptr_set_copy{ pers_ptr_set }; // problematisch!!!
	// (shallow copy und unten wird Original disposed)
	// -> Who is responsible for deallocating the objects?
	// -> Use scoped pointers

	// Var. 1
	/*for (person* pp : pers_ptr_set)
	{
		// dadurch dass person-Destruktor virtual ist, wird auch der student-Destruktor
		// richtig aufgerufen
		delete pp;
	}*/

	// Var. 2
	std::for_each(pers_ptr_set.begin(), pers_ptr_set.end(), [](person* pp) {
		delete pp;
	});
}

//-----------------------------------------------------------------------------
static void scoped_ptr_test() {
	{
		// scoped_ptr wird automatisch disposed beim Verlassen des Scopes (Destruktor-Call)
		//scoped_ptr<person> sptr1{new person("scoped", "pointer")};
		scoped_ptr<person> sptr1 = make_scoped<person>("scoped", "pointer");
		cout << "*sptr1 = " << *sptr1 << endl;
		cout << "addr(sptr1) = " << sptr1.get() << endl;

		//scoped_ptr<person> sptr2{ sptr1 }; // nicht erlaubt
		// (2 Pointer zeigen auf selbes Objekt u. es ist nicht mehr klar, wer für das
		// freigeben zuständig ist - STL würde 2x freigeben = Problem)

		scoped_ptr<person> sptr2{ std::move(sptr1) }; // erlaubt (sptr2 erlangt Besitz)
		cout << "*sptr2 = " << *sptr2 << endl;

		cout << "addr(sptr1) = " << sptr1.get() << "; addr(sptr2) = " << sptr2.get() << endl;
	}
	cout << "------------------------------------------------" << endl;

	std::set<scoped_ptr<person>> pers_ptr_set;
	pers_ptr_set.insert(make_scoped<person>("Karl", "Schranz"));
	pers_ptr_set.insert(make_scoped<person>("Franz", "Klammer"));
	pers_ptr_set.insert(make_scoped<person>("Annemarie", "Moser-Proell"));
	pers_ptr_set.insert(make_scoped<student>("Susi", "Lustig", "se007"));
	pers_ptr_set.insert(make_scoped<person>("Marcel", "Hirscher"));

	cout << "------------------------------------------------" << endl;

	for (const auto& spp : pers_ptr_set) {
		cout << *spp << endl;
	}

	cout << "------------------------------------------------" << endl;
}

//-----------------------------------------------------------------------------
static void unique_ptr_test() {
	{
		//auto uptr1 = std::unique_ptr<person>{ new person("unique", "pointer") };
		auto uptr1 = std::make_unique<person>("unique", "pointer");
		cout << "*uptr1 = " << *uptr1 << endl;
		cout << "addr(uptr1) = " << uptr1.get() << endl;

		std::unique_ptr<person> uptr2{ std::move(uptr1) };
		cout << "*uptr2 = " << *uptr2 << endl;

		cout << "addr(uptr1) = " << uptr1.get() << "; addr(uptr2) = " << uptr2.get() << endl;

	}
	cout << "------------------------------------------------" << endl;

	std::set<std::unique_ptr<person>> pers_ptr_set;
	pers_ptr_set.insert(std::make_unique<person>("Karl", "Schranz"));
	pers_ptr_set.insert(std::make_unique<person>("Franz", "Klammer"));
	pers_ptr_set.insert(std::make_unique<person>("Annemarie", "Moser-Proell"));
	pers_ptr_set.insert(std::make_unique<student>("Susi", "Lustig", "se007"));
	pers_ptr_set.insert(std::make_unique<person>("Marcel", "Hirscher"));

	cout << "------------------------------------------------" << endl;

	for (const auto& upp : pers_ptr_set)
		cout << *upp << endl;

	cout << "------------------------------------------------" << endl;

	std::set<std::unique_ptr<person>> pers_ptr_set_copy{ std::move(pers_ptr_set) };
	cout << "pers_ptr_set.size() = " << pers_ptr_set.size() << endl;
	cout << "pers_ptr_set_copy.size() = " << pers_ptr_set_copy.size() << endl;
}

//-----------------------------------------------------------------------------
static void counted_ptr_test() {
	{
		counted_ptr<person> cp1 = make_counted<person>("counted", "pointer");
		cout << "*cp1 = " << *cp1 << endl;

		counted_ptr<person> cp2{ cp1 };
		cout << "*cp2 = " << *cp2 << endl;

	}
	cout << "------------------------------------------------" << endl;
	
	std::set<counted_ptr<person>> pers_ptr_set;
	pers_ptr_set.insert(make_counted<person>("Karl", "Schranz"));
	pers_ptr_set.insert(make_counted<person>("Franz", "Klammer"));
	pers_ptr_set.insert(make_counted<person>("Annemarie", "Moser-Proell"));
	pers_ptr_set.insert(make_counted<student>("Susi", "Lustig", "se007"));
	pers_ptr_set.insert(make_counted<person>("Marcel", "Hirscher"));
	
	cout << "------------------------------------------------" << endl;

	for (const auto& cpp : pers_ptr_set)
		cout << *cpp << endl;

	cout << "------------------------------------------------" << endl;

	std::set<counted_ptr<person>> pers_ptr_set_copy{ pers_ptr_set };
	cout << "pers_ptr_set.size() = " << pers_ptr_set.size() << endl;
	cout << "pers_ptr_set_copy.size() = " << pers_ptr_set_copy.size() << endl;
}

//-----------------------------------------------------------------------------
static void shared_ptr_test() {

	// Var. 1
	//std::set<std::shared_ptr<person>> pers_ptr_set;

	// Var. 2
	std::set<std::shared_ptr<person>, pointee_less<person>> pers_ptr_set;

	pers_ptr_set.insert(std::make_shared<person>("Karl", "Schranz"));
	pers_ptr_set.insert(std::make_shared<person>("Franz", "Klammer"));
	pers_ptr_set.insert(std::make_shared<person>("Annemarie", "Moser-Proell"));
	pers_ptr_set.insert(std::make_shared<student>("Susi", "Lustig", "se007"));
	pers_ptr_set.insert(std::make_shared<person>("Marcel", "Hirscher"));

	cout << "------------------------------------------------" << endl;

	for (const auto& spp : pers_ptr_set)
		cout << *spp << endl;

	cout << "------------------------------------------------" << endl;

	std::set<std::shared_ptr<person>, pointee_less<person>> pers_ptr_set_copy{ pers_ptr_set };
	cout << "pers_ptr_set.size() = " << pers_ptr_set.size() << endl;
	cout << "pers_ptr_set_copy.size() = " << pers_ptr_set_copy.size() << endl;
}

//-----------------------------------------------------------------------------
int main() {
	cout << "======= Collections with static objects ========" << endl;
	static_collections_test();
	cout << "================================================\n" << endl;

	cout << "======= Collections with simple pointers =======" << endl;
	simple_ptr_test();
	cout << "================================================\n" << endl;

	cout << "======= Collections with scoped pointers =======" << endl;
	scoped_ptr_test();
	cout << "================================================\n" << endl;

	cout << "======= Collections with unique pointers =======" << endl;
	unique_ptr_test();
	cout << "================================================\n" << endl;

	cout << "======= Collections with counted pointers ======" << endl;
	counted_ptr_test();
	cout << "================================================\n" << endl;

	cout << "======= Collections with shared pointers =======" << endl;
	shared_ptr_test();
	cout << "================================================\n" << endl;

	return 0;
}