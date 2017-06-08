### 构造函数属性初始化

    // 构造函数属性初始化列表
    class Teacher {
    private:
        char* name;
    public:
        Teacher(char* name) {
            this->name = name;
            cout << "有参构造函数" << endl;
        }
        ~Teacher() {
            cout << "析构函数" << endl;
        }
        char* getName() {
            return this->name;
        }
    };

    class Student {
    private:
        int id;
        // 这里必须要初始化
        // Teacher t = Teacher("miss");
        Teacher t1;
        Teacher t2;
    public:
        Student(int id, char* t1_n, char* t2_n) : t1(t1_n), t2(t2_n){// 给Teacher赋值  给对象属性赋值
            this->id = id;
            cout << "Student有参构造函数" << endl;
        }
        void myprint() {
            cout << id << "," << t1.getName() << endl;
        }

        ~Student() {
            cout << "Student析构函数" << endl;
        }
    };


    void func() {
        Student s1(10, "miss teacher", "miss niu");
        Student s2(20, "miss zhang", "miss hh");
        s1.myprint();
        s2.myprint();
    }

    void main() {
        func();

        system("pause");
    }

### new对象指针的使用

    class Teacher {
    private:
        char* name;
    public:
        Teacher(char* name) {
            this->name = name;
            cout << "Teacher构造函数" << endl;		
        }
        ~Teacher() {
            cout << "Teacher析构函数" << endl;
        }
        char* getName() {
            return this->name;
        }
        void setName(char* name) {
            this->name = name;
        }
    };


    void func() {
        // C++方式
        // new的是指针，这个是堆内存分配的，所以要释放
        Teacher* t1 = new Teacher("jack");
        cout << t1->getName() << endl;
        // 释放 不调用就不调用析构函数
        delete t1;

        // C写法  不会调用构造函数和析构函数，都是在堆内存开辟空间
        Teacher * t2 = (Teacher*)malloc(sizeof(Teacher));
        t2->setName("hello");
        free(t2);
    }


    void main() {
        func();

        // 数组
        int* p = (int*)malloc(sizeof(int) * 10);
        p[0] = 9;
        free(p);
        // C++
        int *p2 = new int[10];
        p2[0] = 2;
        delete[] p2;// 释放数组要括号

        system("pause");
    }   

### static静态的使用

    // static 静态
    class Teacher {
    public:
        char* name;
        static int total;
    public:
        Teacher(char* name) {
            this->name = name;
            cout << "Teacher构造函数" << endl;
        }
        ~Teacher() {
            cout << "Teacher析构函数" << endl;
        }
        char* getName() {
            return this->name;
        }
        void setName(char* name) {
            this->name = name;
        }
        // 静态函数
        static void count() {
            total++;
            cout << total << endl;
        }
    };


    // 静态属性初始化赋值
    int Teacher::total = 9;

    void main() {
        Teacher::total = 10;
        cout << Teacher::total << endl;
        // 函数被调用时前面时类名，类名前面是命名空间
        Teacher::count();
        // 通过对象也可以调用
        Teacher t1("yuehan");
        t1.count();

        system("pause");
    }    

### 类大小

    // 类大小
    class A {
    public:
        int i;
        int j;
        int k;
    };

    class B {
    public:
        int i;
        int j;
        int k;
        void myprintf() {
            cout << "打印" << endl;
        }
    };

    void main() {
        // 大小是相等的
        cout << sizeof(A) << endl;
        cout << sizeof(B) << endl;

        // 内存分区：栈/堆/全局静态/常量/代码区

        system("pause");
    }    

### this指针

    // this 当前对象的指针
    // 函数是共享的，必须要有能够标识当前对象是谁的办法

    class Teacher {
    public:
        char* name;
        static int total;
    public:
        Teacher(char* name) {
            this->name = name;
            cout << "Teacher构造函数" << endl;
        }
        ~Teacher() {
            cout << "Teacher析构函数" << endl;
        }
        // 常函数 修饰的是this 既不能改变指针的值，也不能改变指针的内容
        void myprint() const {
            printf("%#x\n", this);
            // 不能改变
            // this->name = "yuehan";
            cout << this->name << endl;
        }
    };