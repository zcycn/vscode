### 结构体初始化方式

- 可以定义匿名结构体  
- 结构体可以在定义是直接定义多个变量，以及初始化  

        struct Man {
            char* name;
            char  name2[20];
            int age;
        };

        struct Man2 {
            char name[20];
            int age;
        } man2, man22 = {"jack", 20};// 结构体变量，直接使用，可以定义多个，还可以赋值

        struct {
            char name[20];
            int age;
        } man3;// 匿名结构体，作用是控制结构体变量个数

- 初始化可以声明时直接赋值，或之后赋值  

        // 方式一
        struct Man m1 = { "jack", "hello", 21};

        // 方式二
        struct Man m2;
        m2.age = 23;
        m2.name = "rouse";
        strcpy(m2.name2, "hello");// 字符数组的赋值方式

        // 匿名结构体
        strcpy(man2.name, "jack");
        man2.age = 10;        

- 结构体嵌套  

        // 结构体嵌套
        struct Teacher {
            char name[20];
        };

        struct Student {
            char name[20];
            struct Teacher t;
        };

        struct Student2 {
            char name[20];
            struct Teacher2 {
                char name[20];
            } t;
        };

        // 结构体嵌套
        struct Student s1 = { "hello", {"teacher"} };

        struct Student s2;
        strcpy(s2.t.name, "json");

### 结构体指针  

- 遍历结构体时可以通过结构体数组/结构体大小得到数量

        struct Man {
            char name[20];
            int age;
        };

        struct Man m1 = { "jack", 20 };
        struct Man *p = &m1;
        printf("%s, %d\n", (*p).name, (*p).age);

        struct Man mans[] = { {"hello", 20}, {"world", 30} };
        // 遍历数组
        // (sizeof(mans)/sizeof(struct Man) 结构体数组的长度，就是2
        struct Man*p2 = mans;
        for (; p2 < mans + sizeof(mans)/sizeof(struct Man); p2++) {
            printf("%s, %d\n", p2->name, p2->age);
        }            

- 结构体是字节对齐的，结构体大小要能被最大的数据类型整除，作用是提升读取效率  

        struct Man2 {
            int age;
            double weight;
        };        

        // 结构体大小
        // 4 + 8  16 
        // 字节对齐，结构体大小要能被最大的数据类型整除
        // 提升读取效率
        struct Man2 man2 = {20, 89.8};
        printf("%d,%#x\n", sizeof(man2), &man2);


### 结构体动态内存分配

- 返回值是void*,这里可以做强转处理  

        struct Man {
            char* name;
            int age;
        };

        struct Man* man_p = (struct Man*)malloc(sizeof(struct Man) * 10);
        struct Man* p = man_p;
        p->name = "jack";
        p->age = 20;
        p++;
        p->name = "rose";
        p->age = 20;

        struct Man* loop_p = man_p;
        for (; loop_p < man_p + 2; loop_p++) {
            printf("%s,%d\n", loop_p->name, loop_p->age);
        }

        free(man_p);       

### 结构体别名  

        // 结构体别名
        typedef struct Man JavaMan;
        typedef struct Man* JM;
        typedef struct Woman {
            char name[20];
            int age;
        } Woman, *Wp;// 加typedef表示别名

- 结构体中函数指针  

        // 结构体函数指针
        typedef struct Girl {
            char* name;
            int age;
            void(*sayHi)(char*);
        } Girl;

        typedef Girl* Girl_p;

        void sayHi(char* test){
            printf("%s\n", test);
        }

        // 只有传递指针才能修改
        void reName(Girl_p gp) {
            gp->name = "lily";
        }

- 它们的使用  

        Woman w1 = {"rouse", 12};
        Wp wp1 = &w1;
        printf("%s, %d\n", wp1->name, wp1->age);

        Girl g1;
        g1.name = "lucy";
        g1.age = 18;
        g1.sayHi = sayHi;

        g1.sayHi("hao");

        Girl_p gp = &g1;
        printf("%#x\n", gp);
        reName(gp);