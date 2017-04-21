## 1、文件IO

文本文件

    #define  _CRT_SECURE_NO_WARNINGS
    #include<stdio.h>
    #include<stdlib.h>

    void main(){
        char* path = "D:\\test.text";
        char* path_new = "D:\\test_new.text";

        FILE* f_read = fopen(path, "r");
        FILE* f_write = fopen(path_new, "w");

        if(f_read == NULL || f_write == NULL){
            printf("文件打开失败");
            return;
        }

        char buff[50];
        while(fgets(buff, 50, f_read)){
            fputs(buff, f_write);
        }

        fclose(f_read);
        fclose(f_write);

        system("pause");
    }

> _CRT_SECURE_NO_WARNINGS 编译去警告

二进制文件

> 写文本，'\n'会转换成'\r\n'    
> 读文本，'\r\n'会转换成'\n'    
> 读写二进制文件不会转换

    #define _CRT_SECURE_NO_WARNINGS
    #include<stdio.h>
    #include<stdlib.h>

    void main(){
        //文件路径
        char* path = "D:\\test.png";
        char* path_new = "D:\\test_new.png";

        //打开文件，返回文件的指针，b代表是二进制文件
        FILE* f_read = fopen(path, "rb");
        FILE* f_write = fopen(path_new, "wb");

        //文件是否成功打开
        if (f_read == NULL || f_write == NULL){
            printf("文件打开失败");
            return;
        }

        int buff[1024];
        int len = 0;
        while((len = fread(buff, sizeof(int), 1024, f_read))){
            fwrite(buff, sizeof(int), len, f_write);
        }

        fclose(f_read);
        fclose(f_write);
        
        system("pause");
    }

获取文件大小

    #define _CRT_SECURE_NO_WARNINGS
    #include<stdio.h>
    #include<stdlib.h>

    void main(){

        //文件路径
        char* path = "D:\\test.png";

        //打开文件，返回文件的指针，b代表是二进制文件
        FILE* f_read = fopen(path, "rb");

        //文件是否成功打开
        if (f_read == NULL){
            printf("文件打开失败");
            return;
        }

        //类似于多线程下载的概念，首先将文件长度按N段分，然后将每段文件读取并写入到相应的临时文件
        fseek(f_read, 0L, SEEK_END);//seek到文件的结尾，0L代表向前偏移几个字节
        long len = ftell(f_read);//返回当前的文件指针相对于文件开头的位移量
        printf("%ld", len);

        //关闭文件
        fclose(f_read);

        system("pause");
    }    

异或运算文件加密

    通过异或加密，同则为0，不同为1

    void crpypt(char normal_path[], char crypt_path[], char password){
        //打开文件
        FILE *normal_fp = fopen(normal_path, "rb");
        FILE *crypt_fp = fopen(crypt_path, "wb");
        //一次读取一个字符
        int ch;
        int i = 0; //循环使用密码中的字母进行异或运算
        int pwd_len = strlen(password); //密码的长度
        while ((ch = fgetc(normal_fp)) != EOF){ //End of File
            //写入（异或运算）
            fputc(ch ^ password[i % pwd_len], crypt_fp);
            i++;
        }
        //关闭
        fclose(crypt_fp);
        fclose(normal_fp);
    }

> 解密是一个逆过程，再调用一遍即可

## 2、预编译

include
创建text.txt文件：

    printf("我被包含进来了");

在主函数里面使用：
    #include<stdio.h>
    #include<stdlib.h>

    void main(){
        #include "text.txt"
        system("pause");
    }    

宏定义，定义标识、常数、宏函数

定义标识
> 根据标识判断支持语法、平台等

    #ifdef __cplusplus

    #endif

    #ifdef ANDROID

    #endif

常量

    #define MAX 100

    void main(){

        int i = 100;
        if (i == MAX){
            printf("哈哈");
        }

        system("pause");
    }    

宏函数示例

    // 普通函数
    void _jni_define_func_read() {
        printf("read\n");
    }

    void _jni_define_func_write() {
        printf("write\n");
    }

    // 定义宏函数
    #define jni(NAME) _jni_define_func_##NAME() ;

    void main() {

        // 宏函数
        //jni(read); 可以简化函数名称
        jni(write) ;

        system("pause");
    }

宏函数的核心就是替换 ， 只要记住这一点就够了 。下面我们就来做另一个实例 ， 打印类似android Log日志的形式的函数。

    // 模拟Android日志输出 , 核心就是替换
    #define LOG(LEVE,FORMAT,...) printf(##LEVE); printf(##FORMAT,__VA_ARGS__) ;
    #define LOGI(FORMAT,...) LOG("INFO:",##FORMAT,__VA_ARGS__) ;
    #define LOGE(FORMAT,...) LOG("ERROR:",##FORMAT,__VA_ARGS__) ;
    #define LOGW(FORMAT,...) LOG("WARN:",##FORMAT,__VA_ARGS__) ;

    void main() {

        LOGI("%s", "自定义日志。。。。\n");
        LOGE("%s", "我是错误日志...\n");

        system("pause");
    }    

> 上述代码中LEVE,FORMAT都是自定义的 ， 可以是任意名称 ， 只有后面替换的名称一致即可  
> LOG(LEVE,FORMAT,...)中的...表示可变参数 ， 替换则是使用__VA_ARGS__这种固定写法     
> 宏函数的核心就是——替换

