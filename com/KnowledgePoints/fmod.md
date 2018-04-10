### FMOD的使用  

- cmake编译本地多cpp文件  
- cmake导入第三方so文件  
- 本地多个so文件生成  

        # For more information about using CMake with Android Studio, read the
        # documentation: https://d.android.com/studio/projects/add-native-code.html

        # Sets the minimum version of CMake required to build the native library.

        cmake_minimum_required(VERSION 3.4.1)

        # C 的编译选项是 CMAKE_C_FLAGS
        # 指定编译参数，可选
        # SET(CMAKE_CXX_FLAGS "-Wno-error=format-security -Wno-error=pointer-sign")

        #设置生成的so动态库最后输出的路径
        # set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})

        # 设置头文件搜索路径（和此txt同个路径的头文件无需设置），可选
        # INCLUDE_DIRECTORIES(${CMAKE_CURRENT_SOURCE_DIR}/common)

        # 指定用到的系统库或者NDK库或者第三方库的搜索路径，可选。
        # LINK_DIRECTORIES(/usr/local/lib)

        # Creates and names a library, sets it as either STATIC
        # or SHARED, and provides the relative paths to its source code.
        # You can define multiple libraries, and CMake builds them for you.
        # Gradle automatically packages shared libraries with your APK.

        # 配置工程路径
        # 这里没有使用
        # set(path_project D:/AndroidStudio/Sound)

        # 自己生产的so库
        add_library( # Sets the name of the library.
                    native-lib

                    # Sets the library as a shared library.
                    SHARED

                    # Provides a relative path to your source file(s).
                    # 本地库编译的cpp
                    # 如果不写，也可以在编译的cpp里 #include "xxx.cpp"
                    src/main/cpp/common.cpp
                    src/main/cpp/common_platform.cpp
                    src/main/cpp/play_sound.cpp )

        # 第三方的库
        # STATIC a库（静态）， SHARED so库
        # IMPORTED 表示导入的库，不需要构建
        # PROPERTIES IMPORTED_LOCATION 导入本地库
        # ${CMAKE_SOURCE_DIR} 当前文件夹路径
        # ${ANDROID_ABI}编译时会自动根据CPU架构去选择相应的库
        add_library( fmod
                    SHARED
                    IMPORTED )
        set_target_properties( fmod
                            PROPERTIES IMPORTED_LOCATION
                            ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libfmod.so)

        add_library( fmodL
                    SHARED
                    IMPORTED )
        set_target_properties( fmodL
                            PROPERTIES IMPORTED_LOCATION
                            ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libfmodL.so)

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

        # 预构建库与本地库相关联，本地库需要使用到的第三方库或NDK提供的库，关联到本地库
        target_link_libraries( # Specifies the target library.
                            # 被关联的本地库
                            native-lib

                            # 关联的库
                            fmod
                            fmodL
                            # Links the target library to the log library
                            # included in the NDK.
                            ${log-lib} )

        # 编译多个so
        ADD_SUBDIRECTORY(src/main/cpp/effect)

        # add_library( effect-lib
        #              SHARED
        #              src/main/cpp/effect-lib.cpp )
        #
        # target_link_libraries( # Specifies the target library.
        #                        # 被关联的本地库
        #                        effect-lib
        #                        # 关联的库
        #                        fmod
        #                        fmodL
        #                        # Links the target library to the log library
        #                        # included in the NDK.
        #                        ${log-lib} )  


> 具体参考fmod
