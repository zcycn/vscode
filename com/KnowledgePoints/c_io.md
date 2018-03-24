### 联合体

- 成员之间共占一段内存，只存在一个成员，类似JavaObject  
- 大小是最大成员所占字节数  

        // 联合体 是不同类型的变量共同占用一段内存，只有一个成员存在
        // 可以节约内存，可以模拟各种数据类型
        // 大小是最大的成员所占字节数

        union Value {
            int x;
            int y;
            double z;
        };

        union Value d1;
        d1.x = 90;
        d1.y = 100;
        d1.z = 23.2;
        // 只有z有值
        printf("%d,%d,%lf\n", d1.x, d1.y, d1.z);

### 枚举 

        // 枚举 固定值
        // 值就是从0开始
        enum Day {
            Monday,
            Tuesday,
            Wednesday,
            Thursday,
            Friday,
            Saturday,
            Sunday
        };        

        enum Day d = Wednesday;// 只能是定义的值
        printf("%#x, %d\n", &d, d);        

### IO

- C中打开文件使用fopen，关闭使用fclose，返回的都是FILE文件指针  
- 读取字符串使用fgets，写入字符串使用fputs  
- 二进制文件读取使用fread,写入使用fwrite  
- 读取字符使用fgetc，写入字符使用fputc  


	// 读取文本文件
	// 打开
	char path[] = "E:\\friends.txt";
	FILE* fp = fopen(path, "r");
	if (fp == NULL) {
		printf("文件打开失败\n");
		return;
	} 
	// 读取
	char buff[50];
	while (fgets(buff, 50, fp)) {
		printf("%s\n", buff);
	}
	// 关闭
	fclose(fp);

    // 写入文本文件
	char * path2 = "E:\\friends_new.txt";
	FILE* fp2 = fopen(path2, "w");
	char* text = "hello world";
	fputs(text, fp2);
	fclose(fp2);

- 二进制文件读写  
  文本文件区别在于，保存时使用\r\n，使用时使用\n

	// 文本文件和二进制之分，写文本时\n转换成\r\n, 读文本时\r\n转换成\n
	char * r_path = "E:\\BaiduYunDownload\\Android_NDK\\08_08_C_05_联合体_枚举_io\\files\\liuyan.png";
	char * w_path = "E:\\BaiduYunDownload\\Android_NDK\\08_08_C_05_联合体_枚举_io\\files\\liuyan_new.png";
	FILE* rp = fopen(r_path, "rb");// 二进制文件
	FILE* wp = fopen(w_path, "wb");

	int buff2[50];
	int len = 0;
	// fread 缓冲区  缓冲区大小  缓冲区个数  读取文件
	while ((len = fread(buff2, sizeof(int), 50, rp)) != 0) {
		fwrite(buff2, sizeof(int), len, wp);
	}

	fclose(rp);
	fclose(wp);    

### 文件加解密  

- 利用异或运算，一次运算时加密，二次运算时解密  
- 异或运算同则为0，不同为1  

        // 文本文件加密
        void crpypt(char normal_path[], char crypt_path[]) {
            FILE* normal_fp = fopen(normal_path, "r");
            FILE* crypt_fp = fopen(crypt_path, "w");
            // 一次读取一个字符  用int来表示
            int ch;
            while ((ch = fgetc(normal_fp)) != EOF) {
                // 写入
                // 异或运算，同为0不同为1，1^1=0, 0^0=0, 1^0=1, 0^1=1
                // 异或一次加密，异或两次解密
                fputc(ch^9, crypt_fp);
            }
            fclose(normal_fp);
            fclose(crypt_fp);
        }

        // 文本文件解密
        void decrpypt(char crypt_path[], char decrypt_path[]) {
            FILE* crypt_fp = fopen(crypt_path, "r");
            FILE* decrypt_fp = fopen(decrypt_path, "w");
            // 一次读取一个字符  用int来表示
            int ch;
            while ((ch = fgetc(crypt_fp)) != EOF) {
                // 写入
                // 异或运算，同为0不同为1，1^1=0, 0^0=0, 1^0=1, 0^1=1
                // 异或一次加密，异或两次解密
                fputc(ch ^ 9, decrypt_fp);
            }
            fclose(crypt_fp);
            fclose(decrypt_fp);
        }   

        // 文件加密
        char * normal_path = "E:\\friends.txt";
        char * crypt_path = "E:\\friends_crypt.txt";
        char * decrypt_path = "E:\\friends_decrypt.txt";

        // crpypt(normal_path, crypt_path);
        // decrpypt(crypt_path, decrypt_path);        


        // 二进制文件加密
        void crpypt2(char normal_path[], char crypt_path[], char password[]) {
            FILE* normal_fp = fopen(normal_path, "rb");
            FILE* crypt_fp = fopen(crypt_path, "wb");
            // 一次读取一个字符  用int来表示
            int ch;
            int i = 0;
            int pwd_len = strlen(password);
            while ((ch = fgetc(normal_fp)) != EOF) {
                // 写入
                // 异或运算，同为0不同为1，1^1=0, 0^0=0, 1^0=1, 0^1=1
                // 异或一次加密，异或两次解密
                // 与密码循环加密
                fputc(ch ^ password[i % pwd_len], crypt_fp);
                i++;
            }
            fclose(normal_fp);
            fclose(crypt_fp);
        }    

        void decrpypt2(char crypt_path[], char decrypt_path[], char password[]) {
            FILE* crypt_fp = fopen(crypt_path, "rb");
            FILE* decrypt_fp = fopen(decrypt_path, "wb");
            // 一次读取一个字符  用int来表示
            int ch;
            int i = 0;
            int pwd_len = strlen(password);
            while ((ch = fgetc(crypt_fp)) != EOF) {
                // 写入
                // 异或运算，同为0不同为1，1^1=0, 0^0=0, 1^0=1, 0^1=1
                // 异或一次加密，异或两次解密
                fputc(ch ^ password[i%pwd_len], decrypt_fp);
                i++;
            }
            fclose(crypt_fp);
            fclose(decrypt_fp);
        }        

        // crpypt2(normal_path, crypt_path, "hello");
        decrpypt2(crypt_path, decrypt_path, "hello");        


### 获取文件大小  

        // 获取文件大小
        char * r_path = "E:\\BaiduYunDownload\\Android_NDK\\08_08_C_05_联合体_枚举_io\\files\\liuyan.png";
        FILE* rp = fopen(r_path, "r");
        fseek(rp, 0, SEEK_END);// 定位到文件末尾
        long fileSize = ftell(rp);//返回当前的文件指针，相对于文件开头的偏移量 单位字节
        printf("%d\n", fileSize);        