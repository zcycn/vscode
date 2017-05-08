### cmake样例
    # For more information about using CMake with Android Studio, read the
    # documentation: https://d.android.com/studio/projects/add-native-code.html

    # Sets the minimum version of CMake required to build the native library.

    cmake_minimum_required(VERSION 3.4.1)

    # Creates and names a library, sets it as either STATIC
    # or SHARED, and provides the relative paths to its source code.
    # You can define multiple libraries, and CMake builds them for you.
    # Gradle automatically packages shared libraries with your APK.

    add_library( # Sets the name of the library.
                native-lib

                # Sets the library as a shared library.
                SHARED

                # Provides a relative path to your source file(s).
                src/main/cpp/JniUtils.cpp
                src/main/cpp/web_task.cpp
                src/main/cpp/native-lib.cpp )

    # 第三方库使用到的头文件
    include_directories(
                        src/main/cpp/include/jsoncpp
                        src/main/cpp/include/curl)

    # 依赖第三方库
    add_library(curl
                # 静态 a库，SHARED so库
                STATIC
                IMPORTED)
    set_target_properties(curl
                        PROPERTIES IMPORTED_LOCATION
                        # ${CMAKE_SOURCE_DIR} 当前文件夹路径
                        # ${ANDROID_ABI}编译时会自动根据CPU架构去选择相应的库
                        ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libcurl.a)

    add_library(jsoncpp
                STATIC
                IMPORTED)
    set_target_properties(jsoncpp
                        PROPERTIES IMPORTED_LOCATION
                        ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libjsoncpp.a)



    # Searches for a specified prebuilt library and stores the path as a
    # variable. Because CMake includes system libraries in the search path by
    # default, you only need to specify the name of the public NDK library
    # you want to add. CMake verifies that the library exists before
    # completing its build.

    # 依赖ndk中的库
    find_library( # Sets the name of the path variable.
                log-lib

                # Specifies the name of the NDK library that
                # you want CMake to locate.
                log )

    # Specifies libraries CMake should link to your target library. You
    # can link multiple libraries, such as libraries you define in this
    # build script, prebuilt third-party libraries, or system libraries.


    # 添加链接库到目标库
    target_link_libraries( # Specifies the target library.
                        native-lib

                        # Links the target library to the log library
                        # included in the NDK.
                        jsoncpp
                        curl
                        ${log-lib} )

- cmake_minimum_required(VERSION 3.4.1)                         
    cmake最小使用版本     

- add_library()
    配置so库信息     
    native-lib      声明引用so库的名称      
    SHARED        动态库so库，如果静态的a库就是STATIC        
    src/main/cpp/native-lib.cpp         构建so库的源文件       

- include_directories           
        # 第三方库使用到的头文件       
        include_directories(        
                        src/main/cpp/include/jsoncpp        
                        src/main/cpp/include/curl)      

- find_library()
    与自建so库无关，是使用到的ndk的api或库，Android平台集成了很多ndk库，这里不需要指定路径，只有声明库名称                
    如上添加log相关库          
    log-lib     log库存放在log-lib中             
    log          指定使用log库               

- target_link_libraries()
    本地需要调用的ndk库或三方库，关联到本地库才能调用              
    native-lib      被关联库的名称             
    ${log-lib}      关联的库                

### gradle中引用cmake
    android {
        externalNativeBuild {
            cmake {
                path "CMakeLists.txt"
            }
        }
    }                                 

### 构建NDK源代码
实际上NDK除了有预置的库还有一个源代码（c/cpp），本地库可以关联这些代码    
        
    add_library( app-glue
                STATIC
                ${ANDROID_NDK}/sources/android/native_app_glue/android_native_app_glue.c )

    # You need to link static libraries against your shared native library.
    target_link_libraries( native-lib app-glue ${log-lib} )

- ANDROID_NDK       
    这个是Android Studio已经定义好的变量，可以直接使用它指定的是NDK源代码的根目录。        

### 使用第三方so库
通常情况都需要使用第三方so库      

    add_library( imported-lib
                SHARED
                IMPORTED )

- IMPORTED      
    表示只需要导入，不需要构建so库。       

    set_target_properties(
                        imported-lib // so库的名称
                        PROPERTIES IMPORTED_LOCATION // import so库
                        libs/libimported-lib.so // so库路径
    )    

> 当使用已经存在so库时，不应该配置target_link_libraries()方法，因为只有在build 库文件时才能进行link操作。     
