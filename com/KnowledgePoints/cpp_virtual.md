### 虚函数 

- 多态 
  动态多态 继承，重写  
  静态多态 重载  

- C++中父类调用子类函数需要使用virtual修饰  

Plane.h  

        #pragma once
        // 普通飞机
        class Plane {
        public:
            virtual void fly();
            virtual void land();
        };

Plane.cpp

        #include "Plane.h"

        #include<iostream>

        using namespace std;

        void Plane::fly() {
            cout << "起飞" << endl;
        }

        void Plane::land() {
            cout << "着落" << endl;
        }        

Jet.h  

        #pragma once

        #include "Plane.h"
        // 直升飞机
        class Jet :public Plane {
            virtual void fly();
            virtual void land();
        };        

Jet.cpp

        #include "Jet.h"

        #include<iostream>

        using namespace std;

        void Jet::fly() {
            cout << "直升飞机起飞" << endl;
        }

        void Jet::land() {
            cout << "直升飞机着落" << endl;
        }        

Copter.h

        #pragma once

        #include "Plane.h"
        // 喷气飞机
        class Copter : public Plane {
            virtual void fly();
            virtual void land();
        };        

Copter.cpp  

        #include "Copter.h"

        #include<iostream>

        using namespace std;

        void Copter::fly() {
            cout << "喷气飞机起飞" << endl;
        }

        void Copter::land() {
            cout << "喷气飞机着落" << endl;
        }        

main

        #include "Plane.h"
        #include "Jet.h"
        #include "Copter.h"
        void bizPlay(Plane& p){
            p.fly();
            p.land();
        }        

        Plane p;
        bizPlay(p);

        Jet p2;
        bizPlay(p2);

        Copter p3;
        bizPlay(p3);

        Circle c(10);
        c.sayArea();     

模板函数类似Java泛型  

        //模板函数（泛型）
        /*
        void myswap(int& a,int& b){
        int tmp = 0;
        tmp = a;
        a = b;
        b = tmp;
        }

        void myswap(char& a, char& b){
        char tmp = 0;
        tmp = a;
        a = b;
        b = tmp;
        }
        */

        //发现：这两个函数业务逻辑一样，数据类型不一样
        template <typename T> //<typename T, typename Z> 可以定义多个
        void myswap(T& a, T& b) {
            T tmp = 0;
            tmp = a;
            a = b;
            b = tmp;
        }         

        //根据实际类型，自动推导
        int a = 10, b = 20;
        myswap<int>(a, b);
        cout << a << "," << b << endl;

        char x = 'v', y = 'w';
        myswap(x, y);
        cout << x << "," << y << endl;  