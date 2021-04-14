//
// Created by Tyler Clark on 4/12/21.
//

#ifndef HW4_BANKERS_H
#define HW4_BANKERS_H

#include <string>
#include <vector>

#define MAX 10

class Bankers {
private:
    int al[MAX][MAX]{},m[MAX][MAX]{},n[MAX][MAX]{},avail[MAX]{};
    int nop,nor,total,result[MAX]{},pnum{},work[MAX]{},finish[MAX]{};
    bool marked[MAX];
    std::vector<int> safe;

public:
    Bankers(int res_ct, int prc_ct);
    void readFile(const std::string &fileName);
    void method();
    bool check_available(int process_index);

    int get_total();

    void find_safe_sequence();
};

#endif //HW4_BANKERS_H
