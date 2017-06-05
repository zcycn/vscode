### c++命名空间

    #include<stdlib.h>
    #include<iostream>

    using namespace std;

    // 自定义命名空间
    namespace NS_A {
        int a = 9;

        struct Teacher {
            char name[20];
            int age;
        };
    }

    namespace NS_B {
        int a = 12;

        // 命名空间嵌套
        namespace NS_C {
            int c = 11;
        }
    }

    void main11() {
        
        // std 标准命名空间  运算符重载
        std::cout << "this is c plus plus" << std::endl;

        // 如果使用了命名空间

        cout << "this is c plus plus" << endl;

        // 使用命名空间
        // ::访问修饰符
        cout << NS_A::a << endl;
        cout << NS_B::a << endl;
        cout << NS_B::NS_C::c << endl;

        // 使用命名空间中结构体
        struct NS_A::Teacher t;
        t.age = 10;

        using namespace NS_A;
        Teacher teacher;
        teacher.age = 10;

        system("pause");
    }

    #define PI 3.14
    class MyCircle {
    private:
        double r;
        double s;
    public:
        void setR(double r) {
            this->r = r;
        }
        double getS() {
            return PI * r * r;
        }
    };

    void main22() {

        MyCircle c1;
        c1.setR(4);
        cout << c1.getS() << endl;

        system("pause");
    }

    // 结构体不能继承，类可以new

    struct MyTeacher {
    public:
        char name[20];
        int age;
    public:
        void say() {
            cout << this->age << endl;
        }
    };

    void main33() {
        MyTeacher t1;
        t1.age = 11;
        t1.say();
        system("pause");
    }

    void main44() {
        bool isSingle = true;
        cout << isSingle << endl;

        int a = 10, b = 20;
        int c = (a > b) ? a : b;
        system("pause");
    }

    // 引用
    void main() {
        int a = 10;
        int &b = a;// 别名
        cout << b << endl;
        system("pause");
    }    