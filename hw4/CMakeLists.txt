cmake_minimum_required(VERSION 3.17)
project(hw4)
set(CMAKE_CXX_STANDARD 20)
set(OUTPUT_DIR "bin")

file(COPY ${CMAKE_CURRENT_SOURCE_DIR}/data.txt
        DESTINATION ${CMAKE_CURRENT_BINARY_DIR})

file(GLOB SRC
        "src/*.h"
        "src/*.hpp"
        )

add_executable(exec ${SRC} src/main.cpp)