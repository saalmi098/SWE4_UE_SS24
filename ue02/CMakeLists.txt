cmake_minimum_required(VERSION 3.21)
project(ue02) ### TODO: set project name, e.g. "project(ue01)"

set_property(GLOBAL PROPERTY USE_FOLDERS ON) # enable virtual folders for projects if supported by the build system/IDE

# set output directories
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/lib/static)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/lib)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/bin)
#set(CMAKE_BUILD_TYPE Debug)

# configure C17
set(CMAKE_C_STANDARD 17)          # request C17
set(CMAKE_C_STANDARD_REQUIRED ON) # enforce requested standard
set(CMAKE_C_EXTENSIONS OFF)       # disable compiler specific extensions

# configure C++23
set(CMAKE_CXX_STANDARD 23)          # request C++23
set(CMAKE_CXX_STANDARD_REQUIRED ON) # enforce requested standard
set(CMAKE_CXX_EXTENSIONS OFF)       # disable compiler specific extensions

# helper function to simplify definition of projects
function(add_target name)
   file(GLOB_RECURSE SRC "./src/${name}/*")   # recursively collect all files in sub-folder for project
   add_executable(${name} ${SRC})       # define project "name" based on files found in SRC

   #set required warning flags
   if("${CMAKE_C_COMPILER_ID}" STREQUAL "GNU"        OR "${CMAKE_CXX_COMPILER_ID}" STREQUAL "GNU"        OR
      "${CMAKE_C_COMPILER_ID}" STREQUAL "Clang"      OR "${CMAKE_CXX_COMPILER_ID}" STREQUAL "Clang"      OR
      "${CMAKE_C_COMPILER_ID}" STREQUAL "AppleClang" OR "${CMAKE_CXX_COMPILER_ID}" STREQUAL "AppleClang"   )
      target_compile_options(${name} PRIVATE
         -Wall        # enable "all" warnings
         -Wextra      # enable extra warnings
         -Wpedantic   # enable strict conformance warnings
         -Wconversion # enable warnings for dangerous implicit conversions
         -Werror=vla  # disable support for VLAs
      )
      target_link_libraries(${name} PRIVATE
         m # math library must be explicitly linked on Unix
      )
   elseif("${CMAKE_C_COMPILER_ID}" STREQUAL "MSVC" OR "${CMAKE_CXX_COMPILER_ID}" STREQUAL "MSVC")
      target_compile_options(${name} PRIVATE
         /W4          # enable almost all "necessary" warnings
         /permissive- # enforce strict standard compliance
         /JMC         # optimize debugging experience
         /MP          # enable parallel compilation
      )
   else()
      message(WARNING "unknown compiler, no warning flags set!")
   endif()
endfunction()

# Find all directories under 'src/'
file(GLOB subprojects RELATIVE ${CMAKE_SOURCE_DIR}/src ${CMAKE_SOURCE_DIR}/src/*)
foreach(subproject ${subprojects})
   if(IS_DIRECTORY ${CMAKE_SOURCE_DIR}/src/${subproject})
      add_target(${subproject})
   endif()
endforeach()
