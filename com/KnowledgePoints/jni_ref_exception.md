### JNI引用变量

1. 局部引用  

- 通过DeleteLocalRef释放  

        // 局部引用
        public native void localRef();

        // 局部引用通过DeleteLocalRef释放
        JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_localRef
        (JNIEnv* env, jobject jobj) {

            int i = 0;
            for (; i < 5; i++) {
                jclass cls = (*env)->FindClass(env, "java/util/Date");
                jmethodID constructor_mid = (*env)->GetMethodID(env, cls, "<init>", "()V");
                jobject obj = (*env)->NewObject(env, cls, constructor_mid);
                // 执行逻辑

                // 回收
                (*env)->DeleteLocalRef(env, obj);
            }

        }

2. 全局引用 

        public native void createGlobalRef();
        
        public native String getGlobalRef();
        
        public native void deleteGlobalRef();     

        test.createGlobalRef();
		System.out.println(test.getGlobalRef());
		test.deleteGlobalRef();   


        // 全局引用

        jstring global_str;

        JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_createGlobalRef
        (JNIEnv* env, jobject jobj) {

            jstring obj = (*env)->NewStringUTF(env, "jni developement is powerful");
            global_str = (*env)->NewGlobalRef(env, obj);

        }

        JNIEXPORT jstring JNICALL Java_com_zcycn_jni_JniTest_getGlobalRef
        (JNIEnv* env, jobject jobj) {

            return global_str;

        }

        JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_deleteGlobalRef
        (JNIEnv* env, jobject jobj) {

            (*env)->DeleteGlobalRef(env, global_str);

        }        

3. 弱引用，内存不足时释放

        // NewWeekGlobalRef
        // DeleteWeekGlobalRef        

### 异常处理  

- JNI异常，Throwable捕获异常，不捕获那么停止运行  
- JNI中进行异常检测，通过ExceptionClear方法，可以让Java继续运行  
- JNI中异常，如果Java中需要捕获，就要通过ThrowNew函数创建异常  

	    public native void exception();        
        try {
			test.exception();
		} catch (Exception e) {
			e.printStackTrace();
		}  

        // 异常
        JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_exception
        (JNIEnv* env, jobject jobj) {

            jclass cls = (*env)->GetObjectClass(env, jobj);
            // 访问没有的属性
            jfieldID fid = (*env)->GetFieldID(env, cls, "key2", "Ljava/lang/String;");
            // 检测异常
            jthrowable exception = (*env)->ExceptionOccurred(env);
            if (exception != NULL) {
                // 让Java代码可以继续运行
                // 清空异常
                (*env)->ExceptionClear(env);

                // 补救措施
                fid = (*env)->GetFieldID(env, cls, "key", "Ljava/lang/String;");
            }

            jstring jstr = (*env)->GetObjectField(env, jobj, fid);
            char* str = (*env)->GetStringUTFChars(env, jstr, JNI_FALSE);

            // 对比字符串是否相等
            if (strcmp(str, "super json1") != 0) {
                // 不相等，抛出异常
                jclass newExcCls = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
                (*env)->ThrowNew(env, newExcCls, "key's value is invalid!");
            }

        }