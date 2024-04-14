#include <iostream>
#include <vector>
#include <algorithm>
#include <iterator>
#include <cstdlib>
#include <ctime>
#include <set>

using std::cout;
using std::endl;

int random_number() {
    return rand() % 1000 + 1;
}

template <typename T>
void print(T& t) {
    std::copy(t.begin(), t.end(), std::ostream_iterator<int>(cout, " "));
    cout << endl;
}

void task_a(std::vector<int>& v) {
    cout << endl << "---------- Task a ----------" << endl;

    // Seed random gen
    srand(static_cast<unsigned int>(time(nullptr)));

    std::generate_n(std::back_inserter(v), 1000, random_number);
    cout << "Vector size after insertion: " << v.size() << endl;
    print(v);
}

void task_b(std::vector<int>& v) {
    cout << endl << "---------- Task b ----------" << endl;

    std::sort(v.begin(), v.end());

    // remove duplicates
    auto last = std::unique(v.begin(), v.end());

    // erase all items after last duplicate
    v.erase(last, v.end());

    cout << "Vector without duplicates (size=" << v.size() << "):" << endl;
    print(v);
}

bool is_even(int num) { return num % 2 == 0; }

void task_c(std::vector<int>& v) {
    cout << endl << "---------- Task c ----------" << endl;

    // remove even numbers
    auto last = std::remove_if(v.begin(), v.end(), is_even);

    // erase deleted numbers
    v.erase(last, v.end());

    cout << "Vector without even numbers (size=" << v.size() << "):" << endl;
    print(v);
}

void task_d() {
    cout << endl << "---------- Task d ----------" << endl;

    std::set<int> s;
    for (int i = 0; i < 1000; i++)
        s.insert(random_number());

    cout << "Set size after insertion: " << s.size() << endl;
    print(s);
}

int main() {
    std::vector<int> v;

    task_a(v);
    task_b(v);
    task_c(v);
    task_d();

    // task e:
    // vector.push_back() = O(1)
    // set.insert() = O(log n)

    return 0;
}