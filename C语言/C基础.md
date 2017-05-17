***
### 头文件     
- 引入的头文件，只是函数的声明，编译时会找到函数的实现    

### 等待输入函数    
- system("pause")     
- getchar()

### 输入输出函数
- printf("");
- scanf("%d", &i);      
    > scanf运行时提示不安全时  
    > 在最上面加上 #define _CRT_SECURE_NO_WARNINGS

### 指针类型
- 如果指针类型错误，那么指针*取值时会出错。地址读取起点，类型读取数量        
- NULL表示空指针，指针通常先赋值，int* p = NULL;空指针的默认地址是0，所以访问*p时操作系统不允许同样会出错     

### 数组的地址
1. int ids[] = {1, 2, 3};      
那么ids就是数组首地址，或者&ids[0]、&ids
2. int *p = ids;       
那么*p就是首个元素的值，p++就是后一个元素的值，移动的时sizeof(数据类型)个字节

### 函数指针
    void msg(char* msg, char* title){
        MessageBox(0,msg,title,0);
    }

    // 函数返回类型，函数指针名，函数参数
    void(*fun_p)(char* msg, char* title) = msg;
    fun_p("消息内容","消息标题");

> msg和&msg打印的都是函数的地址    
> 函数指针可以直接在函数的参数中定义，实现函数中传递函数

    #include <math.h>
    #include <time.h>
    int* getMinPointer(int ids[], int len){
        int i = 0; 	
        int* p = &ids[0];
        for (; i < len; i++){
            if (ids[i] < *p){			
                p = &ids[i];
            }
        }
        return p;
    }

    void main(){
        int ids[10];
        int i = 0;
        //初始化随机数发生器，设置种子，种子不一样，随机数才不一样
        //当前时间作为种子 有符号 int -xx - > +xx
        srand((unsigned)time(NULL));
        for (; i < 10; i++){
            //100范围内
            ids[i] = rand() % 100;
            printf("%d\n", ids[i]);
        }

        int* p = getMinPointer(ids, sizeof(ids) / sizeof(int));
        printf("%#x,%d\n",p,*p);
        getchar();
    }

### 动态内存分配

1. 如果直接定义一个int a[1024 * 1024 * 10];那么就会报stack overflow，由于占用了40M内存    
    这个就时静态内存分配    

2. c语言内存分配
    栈区stack，windows下栈内存分配2M，上门就是超出了限制，具有自动分配、释放的特点  
    堆区heap，手动分配释放，堆区可以分配操作系统80%的内存  
    全局或静态区  
    字符常量区   
    程序代码区   

3. 分配内存     
    // 返回void* 任意类型指针
    int* p = malloc(1024 * 1024 * 10 * sizeof(int));   
    // 释放 
    free(p);

4. 堆内存存数据       

        void main(){
            // 静态内存分配数组，数组大小必须固定
            // int a[10];

            int len;
            printf("输入数组的长度:");
            scanf("%d", &len);
            // 开辟内存
            // int* p = malloc(len * sizeof(int));

            // 效果相同
            int* p = calloc(len, sizeof(int));

            // 赋值
            int i = 0;
            for(;i<len;i++){
                p[i] = rand()%100;
                printf("%d,%#x\n", p[i], &p[i]);
            }

            int addLen;
            printf("第二次输入增加的长度：");
            scanf("%d", &addLen);
            // 扩大内存，内存不够用
            // 原内存指针，内存扩大后总大小
            int* p2 = realloc(p, sizeof(int)*(len+addLen)); 

            if(p2 == NULL){
                printf("重新分配失败\n");
            }

            // 扩充后重新赋值
            i = 0;
            for(;i<len+addLen;i++){
                p2[i] = rand()%200;
                printf("%d,%#x\n", p2[i], &p2[i]);
            }

            // 释放
            if(p != NULL){
                free(p);
                p = NULL;
            }
            if(p2 != NULL){}
                free(p2);
                p2 = NULL;
            }
        }

    > 重新分配内存，缩小，缩小的那部分会消失       
    > 扩大，如果当前内存段后面有足够的内存空间，直接扩展这段内存空间，realloc返回原指针      
    > 扩大，如果当前内存段不够，那么使用堆中第一个能满足要求的内存块，将目前数据复制到新的内存位置，并将原来的数据块释放掉        
    > 扩大，如果申请失败，返回NULL，原指针仍然有效      
    > 释放时不能多次释放，释放完给指针置空，来标志释放完成        
    > 重新分配内存后再释放p指针会造成内存泄漏，所以在重新赋值前要先释放掉        

        int* p1 = malloc(1024*10*sizeof(int));
        // 如果不释放就重新分配就会内存泄漏 free(p1);
        // 这里如果调用了free已经释放了，但p1仍然有值，并不为NULL
        // p1 = NULL;
        p1 = malloc(1024*10*sizeof(int)*2);
        free(p1);
        p1 = NULL;

### 字符串
1. 字符数组存储字符串        

        // 几种写法，0为结束标记
        char str[] = {'c','h','i','\0'};
        char str[4] = {'c','h','i'};
        char str[10] = "china";
        // 可修改
        str[0] = 'a';
        printf("%s\n",str);// 字符串
        printf("%#x\n",str);// 首地址

2. 通过字符指针

        char* str = "how are you";
        // 此时str不能修改
        // str[0] = 'w'; 错误
        printf("%s\n", str);
        printf("%#x\n",str);

        str+=3;
        while(*str){
            printf("%c", *str);
            str++;
        }

### 结构体
1. 结构体初始化

        struct Man{
            char* name;
            int age;
        };

        void main(){
            // struct Man m1 = {"jack", 20};
            struct Man m1;
            m1.age = 23;
            m1.name = "rose"
        }       

2. 结构体的几种写法         

        struct Man{
            char name[20];
            int age;
        }m1, m2 = {"jack", 20};// 结构体变量名

        // main中使用
        strcpy(m1.name, "jack");// 这里不是指针所以用字符串方法赋值        

    匿名结构体

        // 控制结构体个数
        struct{
            char name[20];
            int age;
        }m1, m2;// 不取变量没法用，变量可以多个，相当于单例     

    结构体嵌套

        struct Teacher{
            char name[20];
        };

        struct Student{
            char name[20];
            int age;
            struct Teacher t;
        };

        // 或者这样定义也可以
        struct Student{
            int age;
            struct Teacher{
                char name[20];
            }t;
        };

        // main中使用
        struct Student s1 = {"jack", 20, {"jason"}};

        struct Student s1;
        s1.age = 10;
        strcpy(s1.t.name, "jason");

3. 结构体指针    

        struct Man{
            char name[20];
            int age;
        };

        struct Man m1 = {"ack", 20};
        struct Man* p = &m1;

        // p->name p->age (*p).name 的简写

4. 结构体数组

        struct Man mans[] = {{"ack", 20}, {"ack", 20}};  
        // 遍历数组
        struct Man* p = mans;
        for(;p<mans+(sizeof(mans)/sizeof(struct Man)));p++){
            printf("%s,%d\n", p->name, p->age);
        }      

5. 结构体大小

        最宽基本类型的整数倍，就是会字节对齐，方便读取效率
        struct Man{
            int age;// 4字节
            double weight;// 8字节
        };        
        结构体大小为16字节而不是12字节

### 结构体动态内存分配

    struct Man{
        char* name;
        int age;
    };
    struct Man* man_p = (struct Man*)malloc(sizeof(struct Man)*10);   
    struct Man* p = man_p;
    p->name = "jack";
    p->age=10;
    p++;
    p->name = "jack";
    p->age=10;        

    struct Man* loop_p = man_p;
    for(; loop_p<man_p+2;loop_p++){
        printf("%s,%d\n", loop_p->name, loop_p->age);
    }

    free(man_p);
    man_p = NULL;

### typedef别名
    代表不同情况显示不同名称，书写简洁           
    typedef int Age;// int 类型别名     
    typedef int* Ap;// 指针类型别名   

1. 结构体别名    

        typedef struct Man* JM;// 结构体指针别名   

        // 结构体前加typedef表示别名，这是一种简写
        typedef struct Woman{
            char name[20];
            int age;
        }W , *WP;// 这两个都是别名

        W w1 = {"rose", 20};
        WP wp1 = &w1;
        printf("%s,%d", wp1->name, wp1->age);

        // 结构体函数指针成员
        typedef struct Girl{
            char name[20];
            int age;
            void(*sayHi)(char*);
        }Girl;// 定义的结构体别名与结构体名称相同可以省写struct

        typedef Girl* GirlP;//通过别名再定义结构体指针别名

        void sayHi(char* text){
            MessageBox(0,"hello","title", 0);
        }

        void main(){
            struct Girl gl;
            gl.name = "lucy";// 数组不能直接赋值，这里省写
            g1.sayHi = sayHi;
            GirlP gp1 = &g1;// 结构体指针的作用是可以在方法间传递
        }

        // 字符数组只能在声明时这样赋值，否则用strcpy函数赋值
        // 相比字符指针而言的好处就是可以修改
        char a[10] = "happy";
        strcpy(a, "baby");
        // 字符指针不能修改，但可以多次赋值
        char *b = "Friend";
        b = "Family";
        
        结构体如果定义了多个变量，那么变量间数据是独立的，不能相互取值

### 联合体        

不同类型的变量共同占用一段内存     
大小就是最大的成员所占的字节数     

    union Data{
        int x;
        int y;
        double z;
    };

    union Data d1;
    d1.x = 90;
    d1.y = 100;
    d1.z = 23.8;

### 枚举

    enum Day{
        Monday,
        Tuesday,
        Wednesday
    };

    相当于

    enum Day{
        Monday = 0,
        Tuesday = 1,
        Wednesday = 2
    };

    enum Day d = Monday;

### IO

    // 读
    char path[] = "E:\\text.txt";
    FILE *fp = fopen(path, "r");    
    if(fp == NULL){
        printf("文件打开失败");
        return;
    }
    char buff[50];
    while(fgets(buff, 50, fp)){
        printf("%s", buff);
    }
    fclose(fp);

    // 写
    char *text = "hello";
    fputs(text, fp);
    fclose(fp);

二进制文件

> 读写文本文件和二进制文件的区别是回车换行符
> 写文本时，\n会转成\r\n
> 读文本时，\r\n会转换成\n

    // b表示操作二进制文件
    FILE *read = fopen(r_path, "rb");
    FILE *write = fopen(w_path, "wb");
    int buff[50];
    int len = 0;
    while((len = fread(buff, sizeof(int)), 50, read) != 0){
        fwrite(buff, sizeof(int), len, write);
    }
    fclose(read);
    fclose(write);