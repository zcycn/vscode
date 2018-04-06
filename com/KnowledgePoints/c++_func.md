### C++函数 

- C++函数可以设置默认值，如果左边参数设置了默认值，那么右边的参数必须设置默认值  
  myprint(int x, int y = 5, int z = 6)  
- 函数的重载不能有二义性  

        void myprint(int x = 10) {
            cout << x << endl;
        }  

        myprint(20);        

- 可变参数使用... 使用va_start进行读取  

        // 可变参数
        // ...类型在va_arg中定义
        void func(int i, ...) {
            // char 可变参数指针
            va_list args_p;
            // 开始读取可变参数，i是最后一个固定参数
            va_start(args_p, i);
            // 可变参数类型
            int a = va_arg(args_p, int);
            char b = va_arg(args_p, char);
            int c = va_arg(args_p, int);

            cout << a << endl;
            cout << b << endl;
            cout << c << endl;

            // 循环读取只能同类型，如果等于0也不行
            //int value = 0;
            //while (1) {
            //	value = va_arg(args_p, int);
            //	if (value <= 0) {
            //		break;
            //	}
            //	cout << value << endl;
            //}

            va_end(args_p);
        }       


        // 可变参数
        func(8, 9, 'c', 11);        


### C++中类的写法 

MyTeacher.h

        #pragma once

        class MyTeacher {
        private:
            int age;
            char* name;
        public:
            void setAge(int age); 
            int getAge();
            void setName(char* name);
            char* getName();
        };

MyTeacher.cpp 

        #include "MyTeacher.h"

        void MyTeacher::setAge(int age) {
            this->age = age;
        }
        int MyTeacher::getAge() {
            return this->age;
        }
        void MyTeacher::setName(char* name) {
            this->name = name;
        }
        char* MyTeacher::getName() {
            return this->name;
        }        

        // 类的声明与使用
        MyTeacher t1;
        t1.setName("jack");
        t1.setAge(20);
        cout << t1.getName() << endl;

### C++中构造函数  

构造函数、析构函数、拷贝函数  
使用拷贝函数时要避免动态内存分配造成释放两次的异常  

        // 构造函数
        class Teacher {
        private:
            char* name;
            int age;
        public:
            Teacher() {	
                this->name = (char*)malloc(100);
                strcpy(name, "my name");
                age = 20;
                cout << "无参构造函数" << endl;
            }
            Teacher(char* name, int age) {
                // this->name = name;
                this->name = (char*)malloc(100);
                strcpy(this->name, name);
                this->age = age;
                cout << "有参构造函数" << endl;
            }
            ~Teacher() {
                // 对象被释放时调用
                free(this->name);
                cout << "析构函数" << endl;
            }
            // 值拷贝
            // 动态内存分配时就会造成释放两次异常
            /*Teacher(const Teacher &obj) {
                this->name = obj.name;
                this->age = obj.age;
                cout << "拷贝构造函数" << endl;
            }*/
            // 深拷贝
            Teacher(const Teacher &obj) {
                int len = strlen(obj.name);
                this->name = (char*)malloc(len + 1);// 长度包括结束符
                strcpy(name, obj.name);
                this->age = obj.age;
                cout << "拷贝构造函数" << endl;
            }

            void myprint() {
                cout << name << "," << age << endl;
            }
        };

        // 有参构造函数
        // Teacher te("hello", 20);

        // 析构函数
        func();

        // 拷贝构造函数
        // 声明赋值时调用
        //Teacher t5("rose", 30);
        //Teacher t6 = t5;        


### 嵌套类的初始化  

另一个类的参数通过类的构造函数进行赋值  

        #define _CRT_SECURE_NO_WARNINGS
        #include<stdlib.h>
        #include<iostream>
        #include <stdarg.h>

        using namespace std;

        class Teacher {
        public:
            char* name;
            // static 
            static int total;
        public:
            Teacher(char* name) {
                this->name = name;
                cout << "构造函数" << endl;
            }
            ~Teacher() {
                cout << "析构函数" << endl;
            }
            void setName(char* name) {
                this->name = name;
            }
            char* getName() {
                return this->name;
            }
            static void count() {
                total++;
                cout << total++ << endl;
            }
        };

        class Student {
        private:	
            int id;
            // Teacher t1 = Teacher("name");
            Teacher t1;
            Teacher t2;
        public:
            Student(int id, char* t1_n, char* t2_n) : t1(t1_n), t2(t2_n) {// : 后面是给另外一个类的构造函数参数赋值
                this->id = id;
            }
            void myprint() {
                cout << id << "," << t1.getName() << "," << t2.getName() << endl;
            }
        };        

        // 嵌套类的初始化
        Student s1(20, "miss z", "miss h");
        s1.myprint();        

### C与C++动态内存分配  

- C的分配方式不会调用C++的构造函数和析构函数

        // C++通过new(delete)动态内存分配
        // C malloc(free)
        // Teacher对象的指针
        Teacher* t1 = new Teacher("jack");
        // 释放
        delete t1;

        // C方法不会调用构造函数和析构函数
        Teacher* t2 = (Teacher*)malloc(sizeof(Teacher));
        t2->setName("lisy");
        cout << t2->getName() << endl;
        free(t2);

### C与C++数组类型分配  

        // C与C++数组类型
        int* p = (int*)malloc(sizeof(int) * 10);
        p[0] = 9;
        free(p);

        int* p2 = new int[10];
        p2[0] = 2;
        // 释放数组[]
        delete[] p2;

### 类中的静态变量需要全局初始化后才能使用  

        // 静态变量只能在全局初始化
        int Teacher::total = 10;

        // 静态变量初始化后可以操作
        cout << Teacher::total << endl;
        Teacher::total++;
        cout << Teacher::total << endl;        

        // 类名，对象名都可以访问函数
        Teacher::count();
        Teacher t11("yue");
        t11.count();

### 类的大小  

        class A {
        public:
            int i;
            int j;
            int k;
            static int m;
        };

        class B {
        public:
            int i;
            int j;
            int k;
            // const修饰时
            // 既是指针常量，又是常量指针
            // const Teacher* const this
            void myprintf() const {
                // 常函数不能修改属性
                // this->i = 5;
                // this指针不能修改，不被const修饰也是不能修改的
                // this = (Teacher*) 0x99999;
                cout << "B" << endl;
            }
        };        


        // 类的大小
        cout << sizeof(A) << endl;
        cout << sizeof(B) << endl;
        // C/C++内存分区  栈  堆  全局（静态） 常量区（字符串） 程序代码区
        // this 当前对象的指针，函数是共享的，要有能够表示当前对象的办法

        // 常量对象只能调用常量函数，常量函数可以被非常量对象访问        