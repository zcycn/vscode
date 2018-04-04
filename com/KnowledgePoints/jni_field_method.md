### JNI数据类型 

1. JNI基本数据类型  
   Java类型->JNI类型->C类型  

2. 引用类型  
   String jstring  
   Object jobject     
   byte[] jbyteArray  
   Object[](String[]) jobjectArray  

3. JNIEXPORT和JNICALL都是JNI的关键字，表示此函数是要被JNI调用的   

### C访问Java属性  

1. 访问流程  
   获取jclass            GetObjectClass    
   获取jfieldID          GetFieldID           GetStaticFieldID  
   获取属性值             GetXXXField          GetStaticXXXField  
   设置属性               SetXXXField          SetStaticXXXField  

2. 属性签名  
   普通数据类型有简写，除了boolean为Z，其余为首字母大写  
   Object  L+完整类名+;  (完整类名需要/间隔)  
   Object数组  [Ljava/lang/Object;   
   int数组     [I;  

Java  

        public String key = "json";
        public native String accessField();     
        ...
        JniTest test = new JniTest();
        System.out.println(test.accessField());

C

        JNIEXPORT jstring JNICALL Java_com_zcycn_jni_JniTest_accessField
        (JNIEnv* env, jobject jobj) {
            // 获取jclass
            jclass cls = (*env)->GetObjectClass(env, jobj);
            // 获取jfieldID
            // 属性名称、属性签名
            jfieldID fid = (*env)->GetFieldID(env, cls, "key", "Ljava/lang/String;");
            // 获取对象属性值
            jstring jstr = (*env)->GetObjectField(env, jobj, fid);
            // jstring转c字符串，isCopy  jboolean指针
            // jboolean isCopy = NULL;
            // const char* normal_path = (*env)->GetStringUTFChars(env, path_jstr, &isCopy);
            // 内部复制了icCopy为JNI_TRUE，没有复制为JNI_FALSE
            // if(isCopy){
            //     
            // }
            char* c_str = (*env)->GetStringUTFChars(env, jstr, NULL);
            // 修改字符串
            char text[20] = "super ";
            strcat(text, c_str);// 拼接字符串
            // 转jstring类型
            jstring new_jstr = (*env)->NewStringUTF(env, text);
            (*env)->SetObjectField(env, jobj, fid, new_jstr);

            return new_jstr;
        }        


静态属性 

        public static int count = 9;
        public native int accessStaticField();     
        System.out.println(test.accessStaticField());

C  

        JNIEXPORT jint JNICALL Java_com_zcycn_jni_JniTest_accessStaticField
        (JNIEnv* env, jobject jobj) {
            // 获取jclass
            jclass cls = (*env)->GetObjectClass(env, jobj);
            // 获取fieldID
            jfieldID fid = (*env)->GetStaticFieldID(env, cls, "count", "I");
            // 获取属性值
            jint count = (*env)->GetStaticIntField(env, cls, fid);
            count++;
            // 设置值
            (*env)->SetStaticIntField(env, cls, fid, count);

            return count;
        }           

### C访问Java方法  

1. 获取方法签名  
   bin目录即class文件，javap -s -p 完整类名  
   javap -s -p com.zcycn.jni.JniTest  

2. 流程  
   获取jclass       GetObjectClass  
   获取jmethodID    GetMethodID           GetStaticMethodID   
   调用方法         CallXXXMethod          CallStaticXXXMethod           

3. 访问方法  

        // 产生最大范围的随机值
        public int getRandomInt(int max) {
            return new Random().nextInt(max);	
        }   
        public native int accessMethod();
        test.accessMethod()

        // 访问java方法
        JNIEXPORT jint JNICALL Java_com_zcycn_jni_JniTest_accessMethod
        (JNIEnv* env, jobject jobj) {
            // 获取jclass
            jclass jcls = (*env)->GetObjectClass(env, jobj);
            // 获取methodID
            // 获取方法前面  进入bin目录，javap -s -p com.zcycn.jni.JniTest
            jmethodID mid = (*env)->GetMethodID(env, jcls, "getRandomInt", "(I)I");
            // 调用方法
            jint random = (*env)->CallIntMethod(env, jobj, mid, 200);
            printf("%ld\n", random);

            return random;
        }        

4. 访问静态方法  

        public static String getUUID() {
            return UUID.randomUUID().toString();
        }        

        public native void accessStaticMethod();
        test.accessStaticMethod();

        JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_accessStaticMethod
        (JNIEnv* env, jobject jobj) {
            jclass jcls = (*env)->GetObjectClass(env, jobj);
            jmethodID mid = (*env)->GetStaticMethodID(env, jcls, "getUUID", "()Ljava/lang/String;");
            jstring uuid = (*env)->CallStaticObjectMethod(env, jcls, mid);
            char* uuid_str = (*env)->GetStringUTFChars(env, uuid, JNI_FALSE);
            // 拼接
            char filename[100];
            sprintf(filename, "D://%s.txt", uuid_str);
            FILE* fp = fopen(filename, "w");
            fputs("hello jni", fp);
            fclose(fp);
        }        

5. 字符串的拼接可以使用strcat或printf        