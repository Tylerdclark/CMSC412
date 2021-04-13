//
// Created by Tyler Clark on 4/12/21.
//

#ifndef HW4_BANKERS_H
#define HW4_BANKERS_H

#include <string>

#define MAX 20

class Bankers {
private:
    int al[MAX][MAX]{},m[MAX][MAX]{},n[MAX][MAX]{},avail[MAX]{};
    int nop,nor,k,result[MAX]{},pnum{},work[MAX]{},finish[MAX]{};

public:
    Bankers(int res_ct, int prc_ct);
    void readFile(const std::string &fileName);
    void method();
    int search(int);
    void input();
    void display();
};

#endif //HW4_BANKERS_H
