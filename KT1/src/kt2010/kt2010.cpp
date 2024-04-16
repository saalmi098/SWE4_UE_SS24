#include <iostream>
#include <algorithm>
#include <iterator>
#include <list>
#include <fstream>
#include <map>
#include <set>
#include <string>
#include <sstream>

using std::cout;
using std::endl;
using std::string;

typedef std::map<string, std::list<int>> StudentDataCollection;

void ReadStudentData(std::istream& is, StudentDataCollection& studData) {
	string line;
	while (is.good()) {
		std::getline(is, line);
		std::istringstream line_stream(line);

		string name;
		line_stream >> name;

		string grade_str;
		while (line_stream >> grade_str) {
			studData[name].push_back(std::stoi(grade_str));
		}
	}
}

void PrintStudentDataCollection(const StudentDataCollection& studData) {
	for (const auto& pair : studData) {
		cout << "Name: " << pair.first << ", Grades: ";
		const std::list<int>& grades = pair.second;
		for (int grade : grades) {
			cout << grade << " ";
		}
		cout << endl;
	}
}

double AverageGradeOfStudent(StudentDataCollection& studData, string matNo) {
	if (!studData.contains(matNo))
		throw std::exception();

	int sum = 0;
	std::list<int> grades = studData[matNo];

	std::for_each(grades.begin(), grades.end(), [&sum](int grade) { sum += grade; });
	return static_cast<double>(sum) / grades.size();
}

int main() {
	StudentDataCollection studData;
	std::ifstream is{ "students.txt" };
	ReadStudentData(is, studData);
	PrintStudentDataCollection(studData);

	cout << AverageGradeOfStudent(studData, "S00001") << endl;
	cout << AverageGradeOfStudent(studData, "S00002") << endl;
}