#include <iostream>
#include "bankers.h"

using namespace std;

void menuPrompt(bool &quit) {
    string ans{};

    cout << "Would you like to read in a file? Y/N\n";
    getline(cin >> ws, ans);
    if (ans != "y" || ans != "Y") {
        quit = false;
    } else {
        quit = true;
    }
}

int main() {
    int prcCt{0};
    int resCt{0};
    string fileName;
    bool quit = false;

    while (!quit) {
        cout << "How many Processes?\n";
        cin >> prcCt;
        cout << "How many resources?\n";
        cin >> resCt;
        Bankers b{resCt, prcCt};
        cout << "What is the path to file?\n";
        cin >> fileName;
        b.readFile(fileName);
        b.method();
        b.display();
        menuPrompt(quit);
    }
    return 0;
}
