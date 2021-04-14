//
// Created by Tyler Clark on 4/12/21.
//

#include "bankers.h"
#include <string>
#include <iostream>
#include <fstream>
#include <vector>


using namespace std;

Bankers::Bankers(int resource_ct, int process_ct) {
    nop = process_ct;
    nor = resource_ct;
    total = 0;
    for (int i = 0; i < MAX; i++) {
        for (int j = 0; j < MAX; j++) {
            al[i][j] = 0;
            m[i][j] = 0;
            n[i][j] = 0;
        }
        avail[i] = 0;
        result[i] = 0;
        finish[i] = 0;
        marked[i] = false;
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
            }
            for (int i = 0; i < nor; ++i) {
                inFile1 >> data;
                al[j][i] = data;
                 //calculate need
            }

            if (j == 0) {
                for (int i = 0; i < nor; i++) {
                    inFile1 >> data;
                    avail[i] = data;
                    work[i] = -1;
                }
            }

        }
        inFile1.close();
    }

}

bool Bankers::check_available(int process_index)
{
    bool flag = true;

    // Step 1: Check if need is <= available
    for (int i = 0; i < nor; i++) {
        if (n[process_index][i] > avail[i]) {
            flag = false;
        }
    }
    return flag;
}

void Bankers::method()
{
    for (int i = 0; i < nop; ++i) {
        if (!marked[i] && check_available(i)){
            marked[i] = true;

            for (int j = 0; j < nor; ++j) {
                avail[j] += al[i][j];
            }
            safe.push_back(i);
            method()
        }
    }
}
void Bankers::find_safe_sequence()
{

}
int Bankers::get_total(){
    return total;
}
