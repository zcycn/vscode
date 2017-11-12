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

### JNI

- jni步骤    

1. 编写native方法      

    ![jni_1.png](img/jni_1.png)    

2. javah命令生成.h头文件      

    ![jni_2.png](img/jni_2.png)    

3. 复制.h头文件到cpp工程     

    ![jni_3.png](img/jni_3.png)    
    文件复制到工程下，还需要在vs工程头文件下添加现有项      
    将jni.h和jni_md.h从jdk中也拷贝到cpp工程        
    生成头文件中的#include <jni.h>需要改为#include "jni.h"，由于这里头文件不是引用的系统的       


4. 实现.h头文件中声明的函数    

        #include "com_zcycn_jni_JniTest.h"

        JNIEXPORT jstring JNICALL Java_com_zcycn_jni_JniTest_getStringFromC
        (JNIEnv *env, jclass jcls) {
            // c字符串转java字符串
            return (*env)->NewStringUTF(env, "C String");
        }

5. 生成dll文件

    ![jni_4.png](img/jni_4.png)     
    ![jni_5.png](img/jni_5.png)     
    ![jni_6.png](img/jni_6.png)             
    生成->生成解决方案      
    在工程目录下找到dll文件       

6. 配置dll文件环境变量

    新配置的环境变量，需要重启Eclipse，或拷贝到工程根目录下
    ![jni_7.png](img/jni_7.png)    

7. 调用native方法，运行

        package com.zcycn.jni;

        public class JniTest {

            public native static String getStringFromC();
            
            public static void main(String[] args) {
                System.out.println(getStringFromC());
            }
            
            // 加载动态库
            static{
                System.loadLibrary("jni_study");
            }
            
        }    

    > dll动态库，a静态库，打包的exe中是不包含dll的

- JNIEnv    

JNIEnv结构体指针，代表java运行环境，调用java中的代码       
c++中可以通过this获取当前的指针     
c中需要用到自身，那么传递结构体的指针，那么接收这个指针就必须是二级指针   
指针赋值给指针必须是二级指针      

    // 结构体指针别名
    typedef struct JNINativeInterface_ *  JNIEnv;

    struct JNINativeInterface_ {
        // 定义结构体中指针函数，模拟JNIEnv
        char* (*NewStringUTF)(JNIEnv*, char *);
    };

    char* NewStringUTF(JNIEnv* env, char * str) {
        return str;
    }

    void main() {
        // 实例化结构体
        struct JNINativeInterface_ struct_env;
        struct_env.NewStringUTF = NewStringUTF;

        // 结构体指针
        JNIEnv e = &struct_env;

        // 结构体二级指针
        JNIEnv *env = &e;

        (*env)->NewStringUTF(env, "abc");
        
        system("pause");
    }

- jclass

代表native方法所属类的class对象

- jobject

native方法是非静态方法，native方法所属的对象

	public native static String getStringFromC();
	public native String getString2FromC();

    JNIEXPORT jstring JNICALL Java_com_zcycn_jni_JniTest_getStringFromC
    (JNIEnv *env, jclass jcls) {
        // c字符串转java字符串
        return (*env)->NewStringUTF(env, "C String");
    }

    JNIEXPORT jstring JNICALL Java_com_zcycn_jni_JniTest_getString2FromC
    (JNIEnv *env, jobject jobj) {
        return (*env)->NewStringUTF(env, "C String");
    }

### jni数据类型

修改java属性值

    JNIEXPORT jstring JNICALL Java_com_zcycn_jni_JniTest_accessField
    (JNIEnv *env, jobject jobj) {
        // 获取jclass
        jclass cls = (*env)->GetObjectClass(env, jobj);
        // 获取属性ID	属性名称	属性签名	object	就是L开头加类名分号
        jfieldID fid = (*env)->GetFieldID(env, cls, "key", "Ljava/lang/String;");
        // 获取属性值
        jstring jstr = (*env)->GetObjectField(env, jobj, fid);
        // jstring转c字符串 isCopy是否复制 JNI_FALSE
        char *c_str = (*env)->GetStringUTFChars(env, jstr,NULL);
        char text[20] = "super ";
        strcat(text, c_str);
        // c字符串转jstring
        jstring new_jstr = (*env)->NewStringUTF(env, text);
        // 修改key
        (*env)->SetObjectField(env, jobj, fid, new_jstr);

        return new_jstr;
    }

    package com.zcycn.jni;

    public class JniTest {

        public native static String getStringFromC();
        
        public native String getString2FromC();
        
        public String key = "hello";
        
        /**
        * 返回修改后的属性内容
        * @return
        */
        public native String accessField();
        
        public static void main(String[] args) {
            System.out.println(getStringFromC());
            
            JniTest j = new JniTest();
            System.out.println(j.getString2FromC());
            System.out.println("key:修改前："+j.key);
            System.out.println(j.accessField());
            System.out.println("key:修改后："+j.key);
        }
        
        // 加载动态库
        static{
            System.loadLibrary("jni_study");
        }
        
    }

修改java静态属性

    JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_accessStaticField
    (JNIEnv *env, jobject jobj) {
        jclass cls = (*env)->GetObjectClass(env, jobj);
        jfieldID fid = (*env)->GetStaticFieldID(env, cls, "count", "I");
        jint count = (*env)->GetStaticIntField(env, cls, fid);
        count++;
        // 修改
        (*env)->SetStaticIntField(env, cls, fid, count);
    }

    package com.zcycn.jni;

    public class JniTest {

        public static int count = 9;
        
        public native void accessStaticField();
        
        public static void main(String[] args) {
            System.out.println("count修改前："+count);
            j.accessStaticField();
            System.out.println("count修改后："+count);
        }
        
        // 加载动态库
        static{
            System.loadLibrary("jni_study");
        }
        
    }    

调用java方法

    JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_accessMethod
    (JNIEnv *env, jobject jobj) {
        jclass cls = (*env)->GetObjectClass(env, jobj);
        //在java项目的bin目录下运行javap -s -p 完整类名		就可显示全部方法签名
        jmethodID mid = (*env)->GetMethodID(env, cls, "getRandomInt", "(I)I");
        // 调用	后面为可变参数，由方法的参数个数决定
        jint random = (*env)->CallIntMethod(env, jobj, mid, 200);
        printf("random num:%ld", random);
    }    

调用java静态方法

    JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_accessStaticMethod
    (JNIEnv *env, jobject jobj) {
        jclass cls = (*env)->GetObjectClass(env, jobj);
        jmethodID mid = (*env)->GetStaticMethodID(env, cls, "getUUID", "()Ljava/lang/String;");
        // 调用
        jstring uuid = (*env)->CallStaticObjectMethod(env, cls, mid);

        // 生成随机文件名称  JNI_FALSE c与java执行的同一字符串
        char *uuid_str = (*env)->GetStringUTFChars(env, uuid, JNI_FALSE);
        char filename[100];
        sprintf(filename, "D://%s.txt", uuid_str);
        FILE *fp = fopen(filename, "w");
        fputs("i love json", fp);
        fclose(fp);
    }    

共用的java类

    package com.zcycn.jni;

    import java.util.Random;
    import java.util.UUID;

    public class JniTest {

        public native static String getStringFromC();
        
        public native String getString2FromC();
        
        public String key = "hello";
        
        /**
        * 返回修改后的属性内容
        * @return
        */
        public native String accessField();
        
        public static int count = 9;
        
        public native void accessStaticField();
        
        public native void accessMethod();
        
        public native void accessStaticMethod();
        
        public static void main(String[] args) {
            System.out.println(getStringFromC());
            
            JniTest j = new JniTest();
            System.out.println(j.getString2FromC());
            System.out.println("key:修改前："+j.key);
            System.out.println(j.accessField());
            System.out.println("key:修改后："+j.key);
            
            System.out.println("count修改前："+count);
            j.accessStaticField();
            System.out.println("count修改后："+count);
            
            j.accessMethod();
            
            j.accessStaticMethod();
        }
        
        /**
        * 获取一个设置最大值的随机数
        * @param max
        * @return
        */
        public int getRandomInt(int max){
            System.out.println("getRandomInt");
            return new Random().nextInt(max);
        }
        
        /**
        * 获取UUID
        * @return
        */
        public static String getUUID(){
            return UUID.randomUUID().toString();
        }
        
        
        // 加载动态库
        static{
            System.loadLibrary("jni_study");
        }
        
    }    

调用构造方法

    // 使用java.util.Date产生时间戳
    JNIEXPORT jobject JNICALL Java_com_zcycn_jni_JniTest_accessConstructor
    (JNIEnv *env, jobject jobj) {
        jclass cls = (*env)->FindClass(env, "java/util/Date");
        // <init> 构造函数
        jmethodID constructor_mid = (*env)->GetMethodID(env, cls, "<init>", "()V");
        // 实例化对象
        jobject date_obj = (*env)->NewObject(env, cls, constructor_mid);
        // 调用getTime
        jmethodID mid = (*env)->GetMethodID(env, cls, "getTime", "()J");
        jlong time = (*env)->CallLongMethod(env, date_obj, mid);
        printf("time:%lld\n", time);
        return date_obj;
    }

调用父类的方法

    // 调用父类的方法
    JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_accessNonvirtualMethod
    (JNIEnv *env, jobject jobj) {
        // 获取man属性
        jclass cls = (*env)->GetObjectClass(env, jobj);
        jfieldID fid = (*env)->GetFieldID(env, cls, "human", "Lcom/zcycn/jni/Human;");
        // 获取属性
        jobject human_obj = (*env)->GetObjectField(env, jobj, fid);
        // 执行方法  父类的名称
        jclass human_cls = (*env)->FindClass(env, "com/zcycn/jni/Human");
        jmethodID mid = (*env)->GetMethodID(env, human_cls, "sayHi", "()V");
        // 调用的子类方法
        (*env)->CallObjectMethod(env, human_obj, mid);
        // 调用到父类的方法
        (*env)->CallNonvirtualObjectMethod(env, human_obj, human_cls, mid);
    }    

中文乱码问题

    // 传入中文
    JNIEXPORT jstring JNICALL Java_com_zcycn_jni_JniTest_chineseChars
    (JNIEnv *env, jobject jobj, jstring in) {
        char *c_str = (*env)->GetStringUTFChars(env, in, JNI_FALSE);
        printf("%s\n", c_str);// 这里会由乱码

        char *cstr = "你好啊";
        //jstring jstr = (*env)->NewStringUTF(env, cstr);

        // 避免乱码
        jclass str_cls = (*env)->FindClass(env, "java/lang/String");
        jmethodID constructor_mid = (*env)->GetMethodID(env, str_cls, "<init>", "([BLjava/lang/String;)V");
        
        // jbyte -> char  复制数据到bytes jbyteArray -> char[]
        jbyteArray bytes = (*env)->NewByteArray(env, strlen(cstr));
        (*env)->SetByteArrayRegion(env, bytes, 0, strlen(cstr), cstr);

        // 字符编码
        jstring charsetName = (*env)->NewStringUTF(env, "GB2312");

        jstring js = (*env)->NewObject(env, str_cls, constructor_mid, bytes, charsetName);

        return js;
    }

    public class JniTest {

        public native static String getStringFromC();
        
        public native String getString2FromC();
        
        public String key = "hello";
        
        /**
        * 返回修改后的属性内容
        * @return
        */
        public native String accessField();
        
        public static int count = 9;
        
        public native void accessStaticField();
        
        public native void accessMethod();
        
        public native void accessStaticMethod();
        
        public native Date accessConstructor();
        
        public Human human = new Man();
        
        public native void accessNonvirtualMethod();
        
        public native String chineseChars(String in);
        
        public static void main(String[] args) {
            System.out.println(getStringFromC());
            
            JniTest j = new JniTest();
            System.out.println(j.getString2FromC());
            System.out.println("key:修改前："+j.key);
            System.out.println(j.accessField());
            System.out.println("key:修改后："+j.key);
            
            System.out.println("count修改前："+count);
            j.accessStaticField();
            System.out.println("count修改后："+count);
            
            j.accessMethod();
            
            j.accessStaticMethod();
            
            j.accessConstructor();
            
            j.accessNonvirtualMethod();
            
            // 这里无法调用父类的方法，在jni中可以
            j.human.sayHi();
            
            System.out.println(j.chineseChars("你好，中国"));
        }
        
        /**
        * 获取一个设置最大值的随机数
        * @param max
        * @return
        */
        public int getRandomInt(int max){
            System.out.println("getRandomInt");
            return new Random().nextInt(max);
        }
        
        /**
        * 获取UUID
        * @return
        */
        public static String getUUID(){
            return UUID.randomUUID().toString();
        }
        
        
        // 加载动态库
        static{
            System.loadLibrary("jni_study");
        }
        
    }    

    // 参考http://ironurbane.iteye.com/blog/425513
    // 以上乱码可以下方法，当然是window平台
    char* jstringToWindows(JNIEnv *env, jstring jstr)
    {
        int length = (*env)->GetStringLength(env, jstr);
        const jchar* jcstr = (*env)->GetStringChars(env, jstr, 0);
        char* rtn = (char*)malloc(length * 2 + 1);
        int size = 0;
        size = WideCharToMultiByte(CP_ACP, 0, (LPCWSTR)jcstr, length, rtn, (length * 2 + 1), NULL, NULL);
        if (size <= 0)
            return NULL;
        (*env)->ReleaseStringChars(env, jstr, jcstr);
        rtn[size] = 0;
        return rtn;
    }

    // 传入中文
    JNIEXPORT jstring JNICALL Java_com_zcycn_jni_JniTest_chineseChars
    (JNIEnv *env, jobject jobj, jstring in) {
        char * recvtest = jstringToWindows(env, in);
        // char *c_str = (*env)->GetStringUTFChars(env, in, JNI_FALSE);
        printf("%s\n", recvtest);// 这里会由乱码

        char *cstr = "你好啊";
        //jstring jstr = (*env)->NewStringUTF(env, cstr);

        // 避免乱码
        jclass str_cls = (*env)->FindClass(env, "java/lang/String");
        jmethodID constructor_mid = (*env)->GetMethodID(env, str_cls, "<init>", "([BLjava/lang/String;)V");
        
        // jbyte -> char  复制数据到bytes jbyteArray -> char[]
        jbyteArray bytes = (*env)->NewByteArray(env, strlen(cstr));
        (*env)->SetByteArrayRegion(env, bytes, 0, strlen(cstr), cstr);

        // 字符编码
        jstring charsetName = (*env)->NewStringUTF(env, "GB2312");

        jstring js = (*env)->NewObject(env, str_cls, constructor_mid, bytes, charsetName);

        return js;
    }