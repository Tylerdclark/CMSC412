#include <iostream>
#include <fstream>

using namespace std;
namespace fs = std::filesystem;

int menuPrompt() {
    string ans{};

    cout << "Would you like to keep going? Y/N\n";
    getline(cin >> ws, ans);
    if (ans != "y" || ans != "Y") {
        return 1;
    } else {
        return 0;
    }
}

void read_file(const string &fileName) {
    ifstream inFile1(fileName, ios::in);
    if (!inFile1)
        cerr << "File open error." << endl;
    else {
        char c;
        while ((c = inFile1.get()) != EOF)
            cout << c;
        cout << endl;
        inFile1.close();
    }
}

int main() {
    std::cout << "Current path is " << fs::current_path() << '\n'; // (1)
    while (menuPrompt()) {
        string fileName;
        cout << "What is the path to file?\n";
        cin >> fileName;
        read_file(fileName);
    }
    return 0;
}
