### 预编译指令

> C执行流程，编译obj目标代码，连接，将目标代码与C函数库连接合并，形成可执行文件
> 预编译，为编译做准备，完成代码文本替换

比如#include引入头文件

1. define   

定义标示       
        
    #ifdef __cplusplus 标识支持c++语法    
        
    防止文件重复引入   
    #ifndef AH // 如果没有定义AH
    #define AH // 那么定义AH
    ...
    #endif 

    A.h文件
    #ifndef AH
    #define AH
    #include "B.h"

    void printfA();

    #endif

    B.h文件
    #ifndef BH
    #define BH
    #include "A.h"

    void printfB();

    #endif

    // 使用时就不会重复引用
    #include "A.h"

    void printfA(){

    }

    // A.h头文件新的定义方法
    // 新版本的宏定义，让编译器自动处理循环包含问题
    #pragma once
    #include "B.h"
    void printfA();

定义常数    

    #define MAX 100
    if(i < MAX){

    }

定义宏函数

    void dn_com_jni_read(){
        
    }

    void dn_com_jni_write(){
        
    }

    #define jni(NAME) dn_com_jni_##NAME();

    // 调用
    jni(read);

Android中日志输出

    // __VA_ARGS__ 可变参数
    #define LOG(FORMAT,...) printf(FORMAT,__VA_ARGS__);
    // 使用
    LOG("%s%d","大小：",89);

    // 日志加级别
    #define LOG_I(FORMAT,...) printf("INFO:"); printf(FORMAT,__VA_ARGS__);
    LOG_I("%s%d","大小：",89);// 输出会多个info：

    // 升级版
    #define LOG(LEVEL,FORMAT,...) printf(LEVEL); printf(FORMAT,__VA_ARGS__);
    #define LOG_I(FORMAT,...) LOG("INFO",FORMAT,__VA_ARGS__);
    #define LOG_E(FORMAT,...) LOG("ERROR",FORMAT,__VA_ARGS__);

    > 这边##可加也可不加，都表示替换，后面是任意名字

