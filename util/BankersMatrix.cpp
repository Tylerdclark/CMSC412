//
// Created by Tyler Clark on 4/10/21.
//

#include <iostream>
#include <random>
#include "BankersMatrix.h"

BankersMatrix::BankersMatrix(int r_types, int p_count)
{
    assert(r_types > 0 || p_count > 0);
    resource_types = r_types;
    process_count = p_count;
}

void BankersMatrix::printMatrix() const {

    std::random_device dev;
    std::mt19937 rng(dev());
    std::uniform_int_distribution<std::mt19937::result_type> dist6(0,6);
    int saved_matrix[this->process_count][this->resource_types];

    for (int i = 0; i < this->process_count; ++i) {
        for (int j = 0; j < this->resource_types; ++j) {
            int num = dist6(rng);
            saved_matrix[i][j] = num;
            std::cout << num << " ";
        }
        std::cout << "\n";
    }
}
int main(int argv, char**)
{
    int r_types{0};
    int p_ct{0};

    std::cout << "How many resource types?\n";
    std::cin >> r_types;
    std::cout << "How many processes?\n";
    std::cin >> p_ct;
    std::cout << "\n";

    BankersMatrix matrix = BankersMatrix(r_types, p_ct);
    matrix.printMatrix();
    return 0;
}
