# Distributed under the OSI-approved BSD 3-Clause License.  See accompanying
# file Copyright.txt or https://cmake.org/licensing for details.

cmake_minimum_required(VERSION 3.5)

file(MAKE_DIRECTORY
  "C:/Users/tinnguyen/Documents/notes/gamedev/sketches/npaint/build/_deps/ftxui-src"
  "C:/Users/tinnguyen/Documents/notes/gamedev/sketches/npaint/build/_deps/ftxui-build"
  "C:/Users/tinnguyen/Documents/notes/gamedev/sketches/npaint/build/_deps/ftxui-subbuild/ftxui-populate-prefix"
  "C:/Users/tinnguyen/Documents/notes/gamedev/sketches/npaint/build/_deps/ftxui-subbuild/ftxui-populate-prefix/tmp"
  "C:/Users/tinnguyen/Documents/notes/gamedev/sketches/npaint/build/_deps/ftxui-subbuild/ftxui-populate-prefix/src/ftxui-populate-stamp"
  "C:/Users/tinnguyen/Documents/notes/gamedev/sketches/npaint/build/_deps/ftxui-subbuild/ftxui-populate-prefix/src"
  "C:/Users/tinnguyen/Documents/notes/gamedev/sketches/npaint/build/_deps/ftxui-subbuild/ftxui-populate-prefix/src/ftxui-populate-stamp"
)

set(configSubDirs )
foreach(subDir IN LISTS configSubDirs)
    file(MAKE_DIRECTORY "C:/Users/tinnguyen/Documents/notes/gamedev/sketches/npaint/build/_deps/ftxui-subbuild/ftxui-populate-prefix/src/ftxui-populate-stamp/${subDir}")
endforeach()
if(cfgdir)
  file(MAKE_DIRECTORY "C:/Users/tinnguyen/Documents/notes/gamedev/sketches/npaint/build/_deps/ftxui-subbuild/ftxui-populate-prefix/src/ftxui-populate-stamp${cfgdir}") # cfgdir has leading slash
endif()
