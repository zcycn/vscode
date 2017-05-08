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

