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
        p1 = malloc(1024*10*sizeof(int)*2);
        free(p1);

