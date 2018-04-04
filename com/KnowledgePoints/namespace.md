### C++ namespace  

- standard 标准命名空间  
- 命名空间类似Java中的包  
- << 运算符重载，本身没有意义  
- :: 相当于Java中 .  

        #include<stdlib.h>
        #include<iostream>
        std::cout << "this is c plus plus" << std::endl;  

简写  

        using namespace std;        
        cout << "hello world" << endl;

- 自定义命名空间  

        namespace nsp_a {
            int a = 9;
        }

        namespace nsp_b {
            int a = 12;
        }

        cout << nsp_a::a << endl;
        cout << nsp_b::a << endl;

### 类与结构体  

- C++中定义方法相同，但结构体不能继承   

        class MyCircle{
        private:
            double r;
            double s;
        public:
            void setR(double r) {
                this->r = r;
            }
            double getS() {
                return r * r * PI;
            }
        };

        struct MyTeacher {
        public:
            char name[20];
            int age;
        public:
            void say() {
                cout << this->age << endl;
            }
        };        

        // 使用类
        MyCircle c1;
        c1.setR(4);
        cout << "园的面积：" << c1.getS() << endl;

        // c++中结构体，结构体不能继承
        MyTeacher t1;
        t1.age = 10;
        t1.say();


### 引用  

- 变量名就是内存地址空间的别名，引用就是内存空间的另一个别名  
- C++中&表示引用  

        int a = 10;
        int &b = a;
        cout << b << endl;  

- 引用值交换  

        void swap(int &a, int &b) {
            int c = 0;
            c = a;
            a = b;
            b = c;
        }        

        int x = 10;
        int y = 20;
        swap(x, y);
        cout << x << "   " << y << endl;        

### 引用的作用  

- 作为函数的参数或返回值  
- 指针引用可以替代二级指针  
- 指针在函数中可能需要判NULL，而引用不用，为NULL时无法传递给函数  

        struct Teacher {
            char* name;
            int age;
        };

        // 引用不可能为null，为null无法传递
        void myprint(Teacher &t) {
            cout << t.name << "," << t.age << endl;
        }

        // 指针需要判断为null
        void myprint2(Teacher* t) {
            cout << t->name << "," << t->age << endl;
        }

        Teacher t;
        t.name = "jack";
        t.age = 20;
        myprint(t);        


替代二级指针        


        // 指针引用代替二级指针
        void getTeacher(Teacher* &p) {
            p = (Teacher*)malloc(sizeof(Teacher));
            p->age = 20;
        }

        Teacher* t2 = NULL;
        getTeacher(t2);
        cout << t2->age << endl;        


- 引用的大小 

        // 引用的大小
        struct Teacher2 {
            char name[20];
            int age;
        };        

        // 引用大小
        Teacher2 te;
        Teacher2 &te2 = te;
        Teacher2* te_p = &te;
        cout << sizeof(te2) << endl;
        cout << sizeof(te_p) << endl;        


### 指针常量与常量指针  

- 指针常量，指针是常量，那么该指针不能再次赋值  

- 常量指针，指针指向的是常量，那么该指针不能修改常量的值  

        // 指针常量  指针的常量，指针不可修改
        int a = 2, b = 3;
        int* const p1 = &a;
        // p1 = &b; 不能修改
        cout << *p1 << endl;

        // 常量指针，常量的指针，常量不能修改内容
        const int* p2 = &a;
        p2 = &b;
        // *p2 = 9; 不能修改