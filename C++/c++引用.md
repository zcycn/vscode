### c++引用

    struct Teacher {
        char* name;
        int age;
    };

    // 指针与别名
    void getTeacher(Teacher **p) {
        Teacher *tmp = (Teacher*)malloc(sizeof(Teacher));
        *p = tmp;
        (*p)->age = 20;
    }

    void getTeacher(Teacher *&p) {
        p = (Teacher*)malloc(sizeof(Teacher));
        p->age = 30;
    }

    // 指针是保存变量的地址，引用是保存变量的别名
    void main66() {
        Teacher *t = NULL;
        getTeacher(t);
        getTeacher(&t);// 指针的引用
        system("pause");
    }



    void main77() {
        // 指针常量 能改变内容 不能改变地址
        int a = 2, b = 3;
        int *const p1 = &a;
        // 可以改变指向内容
        *p1 = 4;

        // 常量指针  能改变地址 不能改变内容
        const int *p2 = &a;
        p2 = &b;
    }


    // 函数 前面给了默认值，后面也要给默认值
    void myprint(int x = 10, int y = 9) {
        cout << x << endl;
    }

    // 可变参数
    // #include<stdarg.h>
    void func(int i, ...) {
        // 可变参数指针
        va_list args_p;
        // 开始可变参数，i是最后一个固定参数
        va_start(args_p, i);

        int a = va_arg(args_p, int);// 指针， 类型
        int b = va_arg(args_p, char);// 具体有几个无法知道

        // 结束
        va_end(args_p);
    }


    void main() {
        func(1, 10, 2, 30);

    }