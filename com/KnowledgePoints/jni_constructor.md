### C调用Java构造方法  

- 与普通方法属性一样需要先获取jmethodID，区别在于，方法名称为"<init>"  

Java  

        // 访问构造方法
        public native Date accessConstructor();
        test.accessConstructor();

C

        // 访问构造方法
        // C:\Users\zhuch>javap -s -p java.util.Date
        // <init> 构造方法
        JNIEXPORT jobject JNICALL Java_com_zcycn_jni_JniTest_accessConstructor
        (JNIEnv* env, jobject jobj) {
            jclass jcls = (*env)->FindClass(env, "java/util/Date");
            jmethodID constructor_mid = (*env)->GetMethodID(env, jcls, "<init>", "()V");
            jobject date_obj = (*env)->NewObject(env, jcls, constructor_mid);
            // 调用方法
            jmethodID mid = (*env)->GetMethodID(env, jcls, "getTime", "()J");
            jlong time = (*env)->CallLongMethod(env, date_obj, mid);

            printf("%lld\n", time);
            printf("%d, %d,%d\n", sizeof(int), sizeof(long), sizeof(long long));
            return date_obj;
        }

### C调用子类父类方法  

- 使用CallNonvirtualObjectMethod函数调用父类方法  
- 获取jclass时使用FindClass，避免多层嵌套

        public class Human {

            public void sayHi() {
                System.out.println("human say hi");
            }
            
        }

        public class Man extends Human{

            @Override
            public void sayHi() {
                System.out.println("man say hi");
            }
            
        }

        // 访问父类方法
        public Human man = new Man();
        public native void accessNonvirtualMethod();
        test.accessNonvirtualMethod();

        JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_accessNonvirtualMethod
        (JNIEnv* env, jobject jobj) {
            jclass jcls = (*env)->GetObjectClass(env, jobj);
            jfieldID fid = (*env)->GetFieldID(env, jcls, "man", "Lcom/zcycn/jni/Human;");
            // 获取属性
            jobject human_obj = (*env)->GetObjectField(env, jobj, fid);
            // 执行方法
            // 这里FindClass的原因，子类可能嵌套很深
            jclass human_jcls = (*env)->FindClass(env, "com/zcycn/jni/Human");
            jmethodID mid = (*env)->GetMethodID(env, human_jcls, "sayHi", "()V");
            
            // 调用子类的方法
            (*env)->CallObjectMethod(env, human_obj, mid);
            // 调用父类的方法
            (*env)->CallNonvirtualObjectMethod(env, human_obj, human_jcls, mid);
        }


### Java获取C字符串乱码问题  

- 由于C中采用UTF-16编码，因此采用调用Java的String方法进行编码  

        // 中文编码
        public native String chinaChars(String in);      

        System.out.println(test.chinaChars("你好 中国"));  


        JNIEXPORT jstring JNICALL Java_com_zcycn_jni_JniTest_chinaChars
        (JNIEnv* env, jobject jobj, jstring in) {

            char* c_str = (*env)->GetStringUTFChars(env, in, JNI_FALSE);
            printf("%s\n", c_str);

            char* str = "字符串";
            jstring jstr = (*env)->NewStringUTF(env, str);
            printf("len=%d\n", strlen(str));

            // 通过java执行new String
            jclass str_cls = (*env)->FindClass(env, "java/lang/String");
            jmethodID constructor_mid = (*env)->GetMethodID(env, str_cls, "<init>", "([BLjava/lang/String;)V");
            
            // jbyte -> char
            jbyteArray bytes = (*env)->NewByteArray(env, strlen(str));
            (*env)->SetByteArrayRegion(env, bytes, 0, strlen(str), str);

            // 字符编码
            jstring charsetName = (*env)->NewStringUTF(env, "GB2312");
            return (*env)->NewObject(env, str_cls, constructor_mid, bytes, charsetName);
        }        