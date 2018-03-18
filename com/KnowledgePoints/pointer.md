### 标准输入输出

1. 基础数据类型  
   int short long float double char  

2. 头文件  
   只是函数声明，没有实现，类似Java接口     

        // 宏定义 取消安全检查
        #define _CRT_SECURE_NO_WARNINGS
        // 引入头文件 标准输入输出
        // 头文件只有函数声明，没有实现
        // 编译时会找到函数的实现
        #include<stdio.h>
        // scanf
        #include<stdlib.h>   

3. 常用输出占位符  
   %#x 十六进制     %o八进制      %s字符串      double lf%      short %d   

        printf("Hello World!\n");

        int i = 0;
        printf("%d\n", i);

        float f = 23.3;
        printf("%f\n", f);

        // int类型所占字节
        printf("int占%d字节\n", sizeof(int));

        // for循环
        int i2 = 0;
        for (; i2 < 10; i2++) {
            printf("%d\n", i2);
        }

        // 输入输出
        int i3;
        printf("请输入一个整数：");
        scanf("%d", &i3);
        printf("i的值为：%d\n", i3);        

### 指针 

- 指针保存的是地址  
- 指针本身有地址，变量有地址，它们又都保存着值  
- 指针P赋值给一级指针，这个一级指针不能操作指针P，一级指针的改变不能改变指针P的指向  

        // 指针 保存的是地址
        // 指针需要理解 指针的值，指针本身的地址，变量的值，变量的地址
        // 指针赋给一级指针，只是将指针中存储的值赋值过去，一级指针的改变是不能改变原指针的指向
        // 二级指针可以改变原指针的指向
        int i4 = 0;
        int* p = &i4;
        printf("%#x\n", p);

        int i5 = 90;
        int* p2 = &i5;
        *p2 = 200;
        printf("%d\n", i5);

        // 等待输入
        system("pause");       

- 指针的类型，地址只能表示开始位置，那么类型就是表示结束位置  
- 指针指向为NULL的地址，是不能操作的  

        // 指针类型，地址只是开始的位置，类型读取到什么位置结束
        int i = 98;
        int* p = &i;
        printf("%#x\n", p);

        // NULL空指针
        int* p2 = NULL;
        printf("%#x\n", p2);
        // printf("%d\n", *p2); 空指针地址指向的值是不能访问的

        // 二级指针
        int a = 50;
        int* p3 = &a;
        int** p4 = &p3;
        printf("%d\n", **p4);
        **p4 = 60;
        printf("%d\n", a);

### 指针运算  

        // 数组在内存上是连续存储的
        int ids[] = {78, 95, 23};
        printf("%#x\n", ids);
        printf("%#x\n", &ids[0]);
        int *p5 = ids;
        printf("%d\n", *p5);
        p5++;// 移动sizeof(数据类型)个字节
        printf("%d\n", *p5);

        // 指针给数组赋值
        int uids[5];
        int* p6 = uids;
        int i2 = 0;
        for (; p6 < uids + 5; p6++) {
            *p6 = i2;
            i2++;
        }

### 函数指针

- C中可以将一个函数传递给另一个函数来回调，在Java中只能通过实现类中方法进行回调  
- 指针函数的格式：返回值 函数指针名 参数         

        // 宏定义 取消安全检查
        #define _CRT_SECURE_NO_WARNINGS
        // 引入头文件 标准输入输出
        // 头文件只有函数声明，没有实现
        // 编译时会找到函数的实现
        #include<stdio.h>
        // scanf
        #include<stdlib.h>
        #include<Windows.h>
        #include<math.h>
        #include<time.h>

        int add(int a, int b) {
            return a + b;
        }

        int minus(int a, int b) {
            return a - b;
        }        

        void msg(char* msg, char* title) {
            MessageBox(0, msg, title, 0);
        }

        // 传递一个函数指针作为参数
        int msg2(int(*fun_1)(int a, int b), int m, int n) {
            int r = fun_1(m, n);
            printf("%d\n", r);
            return 0;
        }

        // 函数指针  可以将一个函数传递给另一个函数来调用，在java中只能通过类中方法回调
        // msg();
        // 返回值 函数指针名称 参数
        void(*fun)(char* msg, char* title) = msg;
        fun("content", "title");
        printf("%#x\n", msg);// 函数的地址

        msg2(add, 10, 20);
        msg2(minus, 50, 10);

- 查找最小值的示例  

        int* getMinPointer(int ids[], int len) {
            int i = 0;
            int* p = ids;
            for (; i < len; i++) {
                if (ids[i] < *p) {
                    p = &ids[i];
                }
            }
            return p;
        }        

        // 查找最小值，返回最小值的地址
        int ids2[10];
        int i3 = 0;
        // 初始化随机数发生器，只有种子不一样，随机数才不一样
        // 无符号参数
        srand((unsigned)time(NULL));
        for (; i3 < 10; i3++) {
            ids2[i3] = rand() % 100;// 随机数 100以内
            printf("%d\n", ids2[i3]);
        }
        int* p8 = getMinPointer(ids2, sizeof(ids2) / sizeof(int));
        printf("%d,%#x\n", *p8, p8);

### 动态内存分配 

1. 静态内存分配  

        // 静态内存分配
        // 40M 栈溢出
        // int a[1024 * 1024 * 10];      

2. C中内存分配  

        // C内存分配
        // 栈区stack：通常2M，自动分配、释放
        // 堆区heap：通常系统的80%内存，手动分配、释放
        // 全局区或静态区
        // 字符常量区
        // 程序代码区

3. 动态内存分配  
   malloc分配  
   calloc分配，传递两个参数    
   free释放  

        // 堆内存
        // 40M
        // 返回void* 任意类型的指针
        int* p = malloc(1024 * 1024 * 10 * sizeof(int));
        // int* p = calloc(len, sizeof(int));
        // 释放
        free(p);

4. 动态内存再次分配  
   缩小：丢失缩小的部分  
   扩大：当前内存段有空间，则继续扩大，返回原指针  
         当前空间不足，则分配新的内存空间，copy原指针的内容，再释放原空间  
         申请失败，返回NULL，原指针仍然有效  
   分配的空间不能多次释放，所以释放完要置空  

        // 动态指定数组大小
        int len;
        printf("输入数组的长度：");
        scanf("%d", &len);
        int* p2 = malloc(len * sizeof(int));
        int i = 0;
        for (; i < len; i++) {
            p2[i] = rand() % 100;
            printf("%d, %#x\n", p2[i], &p2[i]);
        }

        int addLen;
        printf("增加数组的长度：");
        scanf("%d", &addLen);
        // 再分配内存 原指针 增加之后的总大小
        int* p3 = realloc(p2, sizeof(int) * (len + addLen));

        // 重新分配内存
        // 缩小：缩小的那部分数据丢失
        // 扩大： 如果当前内存段有空间可以扩大，则返回原指针
        //            空间不够，则重新分配，把原指针内存copy过来，再释放原空间
        //            申请失败，返回NULL，原指针仍有效

        if (p2 != NULL) {
            free(p2);
            p2 = NULL;// 由于不能多次释放，所以释放完置NULL
        } 
        if(p3 != NULL){
            free(p3);
            p3 = NULL;
        }

5. 多次分配空间，给同一个指针，那么指针原先指向的空间不会自动释放  

        // 多次开辟内存，并没有释放，重新赋值需要先释放
        //int* p = malloc(1024 * 1024 * 10 * sizeof(int));
        //free(p);// 需要先释放再赋值
        //p = NULL;
        //p = malloc(1024 * 1024 * 20 * sizeof(int));
        //free(p);
        //p = NULL;        

### 字符串  

- 字符串可以用字符数组或字符指针来表示  
- 它们区别在于，字符指针指向的字符串不能修改，字符数组可以修改，由于字符串存储在字符常量区  
- 字符数组需要结束标记，或者通过大小来约定 

        // 字符数组存储字符串 \0表示结束
        // char str[] = {'c', 'h', 'i', 'n', 'a', '\0'};
        // char str[6] = { 'c', 'h', 'i', 'n', 'a' };
        char str[6] = "china";
        printf("%s\n", str);

        // 字符指针
        // 字符不能修改字符，字符数组可以修改
        // 字符串存储在字符常量区
        char* s = "hello";
        printf("%s\n", s);      

- 字符串复制与拼接，由于字符串不能修改，只能在字符数组中进行  

        // 需要#include<string.h>
        // 字符串copy与cat
        char dest[50];
        char* a = "china";
        char* b = " is powerful!";
        strcpy(dest, a);
        strcat(dest, b);
        printf("%s\n", dest);        