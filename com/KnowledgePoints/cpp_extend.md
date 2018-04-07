### C++继承  

- 父类构造函数与对象属性的赋值  

        // 继承
        // 代码的重用性
        // 人类
        class Human {
        public:
            Human(char* name, int age) {
                this->name = name;
                this->age = age;
            }
            void say() {
                cout << "说话" << endl;
            }

        protected:
            char* name;
            int age;
        };
        // 男人
        class Man : public Human {
        public:
            // 兄弟
            // 父类构造函数与对象属性赋值
            // 父类构造函数如果有多个，那么这里就要写上
            Man(char* brother, char* s_name, int s_age, char* h_name, int h_age) : Human(s_name, s_age), h(h_name, h_age){
                this->brother = brother;
            }
            void chasing() {
                cout << "说话2" << endl;
            }
            // 覆盖，并非动态
            void say() {
                cout << "男人说话" << endl;
            }
        private:
            // 兄弟
            char* brother;
            // 对象属性赋值
            Human h;
        };

        Man m1("兄弟", "jak", 18, "mak", 20);
        m1.say();
        // 调用父类
        m1.Human::say();

        // 父类引用与指针
        Human* h_p = &m1;
        h_p->say();
        Human &h1 = m1;
        h1.say();        

        // 多继承
        // 人
        class Person {

        };

        // 公民
        class Citizen {

        };

        // 学生
        class Student : public Person, public Citizen {

        };

### 继承的二义性

        //虚继承，不同路径继承来的同名成员只有一份拷贝，解决不明确的问题
        class A{
        public:
        char* name;
        };

        class A1 : virtual public A{

        };

        class A2 : virtual public A{

        };

        class B : public A1, public A2{

        };

        B b;
        b.name = "jason";
        //指定父类显示调用
        //b.A1::name = "jason";
        //b.A2::name = "jason";