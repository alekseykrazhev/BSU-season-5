cmake_minimum_required(VERSION 3.23)
project(lab2 C)

set(CMAKE_C_STANDARD 17)

add_executable(lab2 main.c)
target_link_libraries(lab2 m)
