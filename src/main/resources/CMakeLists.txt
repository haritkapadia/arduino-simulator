cmake_minimum_required(VERSION 3.10)
project(codelib)

set(CODEFILE "" CACHE STRING "User-created code file")

include_directories("${CMAKE_CURRENT_SOURCE_DIR}/resources" "$ENV{JAVA_HOME}/include" "$ENV{JAVA_HOME}/include/win32")

add_library(codelib SHARED
  "source/arduino.c"
  "source/${CODEFILE}"
  )
set_property(TARGET codelib PROPERTY C_STANDARD 11)
