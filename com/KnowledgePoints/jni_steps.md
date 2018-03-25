### 预处理

jni中c与c++采用了不同的结构体指针，这里就用到了预处理  

- 预处理也叫预编译，为编译工作做准备，完成代码文本的替换  

- c程序从编译到执行，有以下三步  
   编译：形成目标代码.obj  
   连接：将目标代码与c函数库合并形成可执行文件  
   执行  

- define可以定义标示，常数，宏函数  

        // 定义标示可见jni.h中c++的判断  
        #define MAX 100;// 常数

- 宏函数  

        void com_jni_read() {
            printf("read\n");
        }

        void com_jni_write() {
            printf("write\n");
        }

        // NAME表示参数，随意定义
        #define jni(NAME) com_jni_##NAME();// 宏函数
        jni(read);

        // __VA_ARGS__ 可变参数
        // FORMAT 不作要求
        // 加##表示参数，也可不加
        // #define LOG(FORMAT, ...) printf(FORMAT, __VA_ARGS__);
        #define LOG(FORMAT, ...) printf(##FORMAT, __VA_ARGS__);

        #define LOG_I(FORMAT, ...) printf("INFO:"); printf(##FORMAT, __VA_ARGS__);

        // #define LOGI(FORMAT,...) __android_log_print(ANDROID_LOG_INFO,"jason",FORMAT,##__VA_ARGS__);

        LOG("%s\n", "hello");
        LOG_I("%s\n", "hello");

- define防止文件重复引入  

  A.h  

        #ifndef AH // 如果没有定义标示
        #define AH // 定义标示
        #include "B.h"

        void printfA();

        #endif

        // 新版写法
        // #pragma once
        // #include "B.h"

        // void printfA();        

  B.h        

        #ifndef BH
        #define BH
        #include "A.h"

        void printfB();

        #endif  

  man中调用  

        void printfA() {
            printf("printfA\n");
        }

        printfA();

### jni步骤  

1. Java中定义好native方法等  

        public native static String getStringFromC();

        public static void main(String[] args) {
            System.out.println(getStringFromC());
        }
        
        static {
            System.loadLibrary("c");
        }        

2. 通过javah生成对应头文件  

        格式为javah + 全类名
        D:\EclipseWork\jni_study\src> javah com.zcycn.jni.JniTest  

3. 拷贝生成的头文件以及JDK中include目录下jni.h和jni_md.h到VS工程目录中  

        /* DO NOT EDIT THIS FILE - it is machine generated */
        #include "jni.h"
        /* Header for class com_zcycn_jni_JniTest */

        #ifndef _Included_com_zcycn_jni_JniTest
        #define _Included_com_zcycn_jni_JniTest
        #ifdef __cplusplus
        extern "C" {
        #endif
        /*
        * Class:     com_zcycn_jni_JniTest
        * Method:    getStringFromC
        * Signature: ()V
        */
        JNIEXPORT jstring JNICALL Java_com_zcycn_jni_JniTest_getStringFromC
        (JNIEnv* , jclass);

        #ifdef __cplusplus
        }
        #endif
        #endif

4. VS中添加现有项，加入头文件  

5. 实现头文件中的函数  

        // Java Native Interface
        #define _CRT_SECURE_NO_WARNINGS
        #include<stdio.h>
        #include<stdlib.h>
        #include<string.h>
        #include "com_zcycn_jni_JniTest.h"

        // JNIEnv
        // 代表Java运行环境，调用Java中的代码
        // 在c中env是一个二级指针

        JNIEXPORT jstring JNICALL Java_com_zcycn_jni_JniTest_getStringFromC
        (JNIEnv* env , jclass jcls) {
            // c字符串转java字符串
            return (*env)->NewStringUTF(env, "hello world from c");
        }

6. 生成dll动态库  

        生产-配置管理器-x64 
        项目属性-配置类型-dll  

7. 生产解决方案，生成dll动态库，配置到环境变量Path中或拷贝到Java工程目录下，与src评级  

8. 运行Java代码，实现c调用                