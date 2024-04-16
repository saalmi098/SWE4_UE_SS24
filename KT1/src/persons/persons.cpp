#include <iostream>
#include <string>
#include <vector>
#include <fstream>
#include <iterator>
#include <algorithm>

using std::cout;
using std::endl;

class person {
public:
    int get_birthday() {
        return birthday;
    }

    friend std::istream& operator>>(std::istream& is, person& p) {
        is >> p.name;
        is >> p.birthday;
        return is;
    }

    friend std::ostream& operator<<(std::ostream& os, const person& p) {
        return os << "Name: " << p.name << ", Birthday: " << p.birthday;
    }

private:
    std::string name;
    int birthday;
};

bool compare(person& p) {
    return p.get_birthday() > 19700101;
}

int main(int argc, char* argv)
{
    /*std::vector<person> persons;
    std::ifstream ifs{ "persons.txt" };

    std::transform(
        std::istream_iterator<std::string>(f_stream),
        std::istream_iterator<std::string>(),
        std::back_inserter(persons),
        get_person
    );*/

    std::vector<person> persons;

    std::ifstream ifs{ "persons.txt" };

    if (!ifs.good()) {
        std::cerr << "file not found!" << endl;
        return 1;
    }

    /*std::transform(
        std::istream_iterator<std::string>{ifs},
        std::istream_iterator<std::string>{},
        std::back_inserter(persons),
        get_person
    );*/

    while (ifs.good()) {
        person p;
        ifs >> p;
        persons.push_back(p);
    }

    std::copy(persons.begin(), persons.end(), std::ostream_iterator<person>(cout, "\n"));

    cout << "------------" << endl;

    std::copy_if(persons.begin(), persons.end(), std::ostream_iterator<person>(cout, "\n"), [](person& p) {
        return p.get_birthday() >= 19950410;
    });

    cout << "------------" << endl;

    std::vector<person>::iterator new_end = std::remove_if(persons.begin(), persons.end(), [](person& p) {
        return p.get_birthday() >= 19950410;
    });
    persons.erase(new_end, persons.end());
    std::copy(persons.begin(), persons.end(), std::ostream_iterator<person>(cout, "\n"));
}