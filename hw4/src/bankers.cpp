//
// Created by Tyler Clark on 4/12/21.
//

#include "bankers.h"
#include <string>
#include <iostream>
#include <fstream>


using namespace std;

Bankers::Bankers(int resource_ct, int process_ct) {
    nop = process_ct;
    nor = resource_ct;
    k = 0;
    for (int i = 0; i < MAX; i++) {
        for (int j = 0; j < MAX; j++) {
            al[i][j] = 0;
            m[i][j] = 0;
            n[i][j] = 0;
        }
        avail[i] = 0;
        result[i] = 0;
        finish[i] = 0;
    }
}

void Bankers::readFile(const string &fileName) {
    ifstream inFile1(fileName, ios::in);
    if (!inFile1)
        cerr << "File open error." << endl;
    else {
        int data;
        for (int j = 0; j < nop; j++) {
            for (int i = 0; i < nor; ++i) {
                inFile1 >> data;
                m[j][i] = data;
                cout << data;
            }
            cout << "\t";
            for (int i = 0; i < nor; ++i) {
                inFile1 >> data;
                al[j][i] = data;
                n[i][j] = m[i][j] - al[i][j]; //calculate need
                cout << data;
            }

            if (j == 0) {
                cout << "\t";
                for (int i = 0; i < nor; i++) {
                    inFile1 >> data;
                    avail[i] = data;
                    work[i] = -1;
                    cout << data;
                }
            }

            cout << "\n";
        }
        for (int i = 0; i < nop; ++i) {
            finish[i] = 0;
        }
        inFile1.close();
    }
    cout << "\n";
}

void Bankers::method() {
    int i = 0, j, flag;
    while (1) {
        if (finish[i] == 0) {
            pnum = search(i);
            if (pnum != -1) {
                result[k++] = i;
                finish[i] = 1;
                for (j = 0; j < nor; j++) {
                    avail[j] = avail[j] + al[i][j];
                }
            }
        }
        i++;
        if (i == nop) {
            flag = 0;
            for (j = 0; j < nor; j++)
                if (avail[j] != work[j])

                    flag = 1;
            for (j = 0; j < nor; j++)
                work[j] = avail[j];

            if (flag == 0)
                break;
            else
                i = 0;
        }
    }
}

void Bankers::display() {
    int i, j;
    cout << endl << "OUTPUT:";
    cout << endl << "========";
    cout << endl << "PROCESS\t     ALLOTED\t     MAXIMUM\t     NEED";
    for (i = 0; i < nop; i++) {
        cout << "\nP" << i + 1 << "\t     ";
        for (j = 0; j < nor; j++) {
            cout << al[i][j] << "  ";
        }
        cout << "\t     ";
        for (j = 0; j < nor; j++) {
            cout << m[i][j] << "  ";
        }
        cout << "\t     ";
        for (j = 0; j < nor; j++) {
            cout << n[i][j] << "  ";
        }
    }
    cout << "\nThe sequence of the safe processes are: \n";
    for (i = 0; i < k; i++) {
        int temp = result[i] + 1;
        cout << "P" << temp << " ";
    }
    cout << "\nThe sequence of unsafe processes are: \n";
    int flg = 0;
    for (i = 0; i < nop; i++) {
        if (finish[i] == 0) {
            flg = 1;
        }
        cout << "P" << i << " ";
    }
    cout << endl << "RESULT:";
    cout << endl << "=======";
    if (flg == 1)
        cout << endl << "The system is not in safe state and deadlock may occur!!\n";
    else
        cout << endl << "The system is in safe state and deadlock will not occur!!\n";
}

int Bankers::search(int i) {
    int j;
    for (j = 0; j < nor; j++)
        if (n[i][j] > avail[j])
            return -1;
    return 0;
}

void Bankers::input() {

}
