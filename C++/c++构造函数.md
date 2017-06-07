### c++构造函数

构造函数、析构函数、拷贝函数

    #define _CRT_SECURE_NO_WARNINGS
    #include<stdlib.h>
    #include<iostream>
    #include<stdarg.h>
    #include<string.h>
    #include "MyTeacher.h"

    using namespace std;

    class Teacher {
    private:
        char* name;
        int age;
    public:
        Teacher() {// 无参构造函数，声明就会调用
            cout << "无参构造函数" << endl;
            this->name = (char*)malloc(100);
            strcpy(this->name, "jack walson");
        }
        Teacher(char* name, int age) {
            int len = strlen(name);
            this->name = (char*)malloc(len + 1);
            strcpy(this->name, name);
            this->age = age;
            cout << "有参构造函数" << endl;
        }
        // 析构函数
        ~Teacher() {
            cout << "析构" << endl;
            // 释放内存
            free(this->name);
        }
        // 拷贝构造函数
        // 不实现默认时浅拷贝
        Teacher(const Teacher &obj) {
            // 复制name
            int len = strlen(obj.name);
            this->name = (char*)malloc(len + 1);// 结束符
            strcpy(this->name, obj.name);
            this->age = obj.age;
            cout << "拷贝构造函数" << endl;
        }

        void myprint() {
            cout << name << "," << age << endl;
        }
    };

    // 当对象被系统释放时会被调用
    // 作用时善后处理
    void func() {
        Teacher t1;
    }

    void main() {
        Teacher t1;
        Teacher t2("nihao", 20);

        // 拷贝构造函数
        Teacher t3 = t2;
        t3.myprint();

        func();

        system("pause");
    }