### 友元函数  

- 友元函数可以访问私有属性  
- 友元类可以访问类的任何成员  

        #define _CRT_SECURE_NO_WARNINGS
        #include<stdlib.h>
        #include<iostream>

        using namespace std;

        // 友元函数
        class A {
            // 友元类
            friend class B;
        private:
            int i;
        public:
            A(int i) {
                this->i = i;
            }
            void myprint() {
                cout << i << endl;
            }
            // 友元函数
            friend void modify_i(A* p, int a);
        };

        // 友元函数实现
        // 友元函数中可以访问私有属性
        void modify_i(A* p, int a) {
            p->i = a;
        }

        class B {
        public:
            // 友元类可以访问A的任何成员  
            void accessAny() {
                a.i = 10;
            }
        private:
            A a;
        };

        // 友元函数
        A* a = new A(10);
        a->myprint();
        modify_i(a, 5);
        a->myprint();        

### 运算符重载  

        // 运算符重载 
        class Point {
            friend Point operator-(Point &p1, Point &p2);
        public:
            int x;
            int y;
        public:
            Point(int x = 0, int y = 0) {
                this->x = x;
                this->y = y;
            }
            void print() {
                cout << x << "," << y << endl;
            }
            // 重载+
            // 成员的形式
            Point operator+(Point &p2) {
                Point tmp(this->x + p2.x, this->y + p2.y);
                return tmp;
            }
        };

        // 重载+
        //Point operator+(Point &p1, Point &p2) {
        //	Point tmp(p1.x + p2.x, p1.y + p2.y);
        //	return tmp;
        //}

        Point operator-(Point &p1, Point &p2) {
            Point tmp(p1.x - p2.x, p1.y - p2.y);
            return tmp;
        }


        // 运算符重载
        Point p1(10, 20);
        Point p2(20, 10);
        // 本质是函数调用
        Point p3 = p1 + p2;
        p3.print();