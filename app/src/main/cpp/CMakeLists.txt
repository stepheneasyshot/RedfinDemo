cmake_minimum_required(VERSION 3.18.1)

project("terminal-channel")

add_library(terminal-channel SHARED
        common.cpp
        process.cpp
        termExec.cpp)

include_directories(include)

target_link_libraries(terminal-channel
        android
        log)