cmake_minimum_required(VERSION 3.17)
project(hw4)
set(CMAKE_CXX_STANDARD 20)

file(COPY ${CMAKE_CURRENT_SOURCE_DIR}/data.txt data2.txt
        DESTINATION ${CMAKE_CURRENT_BINARY_DIR})

add_executable(${PROJECT_NAME}
        src/main.cpp
        src/bankers.cpp
        src/bankers.h)