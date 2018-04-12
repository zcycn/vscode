### 异常抛出与捕获  

- 异常抛出捕获  

        // c++异常
        try {
            int age = 300;
            if (age > 200) { 
                throw "异常";
            }
        }
        catch (int a) {
            cout << "int异常" << endl;
        }
        catch (char* b) {
            cout << b << endl;
        }
        catch (...) {
            cout << "未知异常" << endl;
        }

### 函数异常  

- 函数中抛出，函数中捕获继续抛出  

        void mydiv(int a, int b) {
            if (b == 0) {
                throw "除数为0";
            }
        }

        // 继续抛异常
        void func2() {
            try {
                mydiv(8, 0);
            }
            catch (char* a) {
                throw a;
            }
        }

        // 函数异常
        try {
            func2();
        }
        catch (char* a) {
            cout << a << endl;
        }

### 异常中抛出对象  

        // 抛出对象
        class MyException {
        public:
            MyException() {

            }
        };    

        // 抛出对象
        try {
            mydiv2(0, 0);
        }
        catch (MyException& e) {
            cout << "MyException" << endl;
        }
        //catch (MyException e2) {// 会产生对象的副本
        //	cout << "MyException2" << endl;
        //}
        catch (...) {
            cout << "未知异常" << endl;
        }        

### 标准异常  

- 可以自定义，类似于Java内置异常  

        class Err {
        public:
            class MyException2 {
            public:
                MyException2() {

                }
            };
        };

        // 标准异常  类似Java内置异常 
        void mydiv4(int a, int b) {
            if (b > 10) {
                throw out_of_range("超出范围");
            }
            else if (b == 5) {
                throw invalid_argument("参数不合法");
            }
            else if (b == NULL) {
                throw NullPointerException("NULL");
            }
            else if (b > 20) {
                throw Err::MyException2();// :: 可能是外部类，可能是命名空间，可能是函数实现
            }
        }

        // 标准异常
        try {
            // mydiv4(0, 11);
            mydiv4(0, NULL);
        }
        catch(out_of_range e1){
            cout << e1.what() << endl;
        }
        catch (NullPointerException& e2) {
            cout << e2.what() << endl;
        }

### c++类型转换  

- 原始转换类型可读性不高，有潜在风险  
  static_cast  普遍情况  
  const_cast  去常量转换  
  dynamic_cast  父类转子类类型  
  reinterpret_cast  函数指针转型  

- 自动类型转换  

        // 自动类型转换
        int i = 0;
        double d = i;

- static_cast  

        double e = 9.5;
        int j = 8;
        j = static_cast<int>(e);
        cout << j << endl;        

        void* func() {
            int i = 0;
            return &i;
        }

        // int* p = (int*)func();
        // int* p = static_cast<int*>(func());  

- const_cast

        void func2(const char c[]) {
            char* c1 = const_cast<char*>(c);
            c1[0] = 'x';
            cout << c << endl;
        }

        char c[] = "hello";
        func2(c);

- dynamic_cast  

        class Person {
        public:
            virtual void print() {
                cout << "人" << endl;
            }
        };

        class Man : public Person{
        public:
            void print() {// 没有头文件时，多态可以不需要virtual关键字
                cout << "男人" << endl;
            }

            void chasing() {
                cout << "玩" << endl;
            }
        };

        class Woman : public Person{
        public:
            void print() {
                cout << "女人" << endl;
            }

            void baby() {
                cout << "baby" << endl;
            }
        };

        void func3(Person* obj) {
            // obj->print();

            // 调用子类特有函数需要转为实际类型
            // 转型失败返回NULL
            Man* m = dynamic_cast<Man*>(obj);
            // 类型不一致就会出错
            // m->print();
            if (m != NULL) {
                m->chasing();
            }
        }        


        Woman w1;
        Person* p1 = &w1;
        func3(p1);        

- reinterpret_cast  

        void func11() {

        }

        char* func12() {
            return "abc";
        }

        typedef void(*f_p)();        

        // reinterpret_cast
        // 函数指针数组
        f_p f_array[6];
        f_array[0] = func11;
        f_array[1] = (f_p)func12;
        // 不具备移植性
        f_array[2] = reinterpret_cast<f_p>(func12);        