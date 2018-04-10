# CMakeLists.txt 文件的配置是支持继承的，在子配置文件中只是写了不同的特殊配置项的配置

add_library( effect_fix

             SHARED

             effect_fix.cpp )

target_link_libraries( # Specifies the target library.
                       # 被关联的本地库
                       effect_fix

                       # 关联的库
                       fmod
                       fmodL
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )