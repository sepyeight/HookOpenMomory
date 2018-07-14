# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

#设置头文件搜索路径（和此txt同个路径的头文件无需设置），可选
INCLUDE_DIRECTORIES(src/main/cpp/
                    src/main/cpp/include/) #这里就是你要编译的so需要的c语言文件头地址

add_library( # Sets the name of the library.
            hook #产生so后的名字


             # Sets the library as a shared library.
             SHARED # so文件类型

             # Provides a relative path to your source file(s).
             src/main/cpp/inlineHook.c  #把所有需要编译的c,c++文件都写上来。
             src/main/cpp/relocate.c
             src/main/cpp/dump.cpp
             src/main/cpp/dlopen.c)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                        hook

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )