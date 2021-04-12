//
// Created by Tyler Clark on 4/10/21.
//

#ifndef HW4_BANKERSMATRIX_H
#define HW4_BANKERSMATRIX_H


#include <cassert>

class BankersMatrix {
private:
    int resource_types;
    int process_count;

public:
    BankersMatrix(int r_types, int p_count);
    void printMatrix() const;
};


#endif //HW4_BANKERSMATRIX_H
