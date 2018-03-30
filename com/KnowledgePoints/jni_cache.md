### 缓存策略

- 局部静态变量  
  声明周期与程序同步，作用域只在当前函数  

        // cache
        for(int i = 0;i<10; i++) {
            test.cached();
        }

        // 局部静态变量缓存
        // 只获取一次fid
        JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_cached
        (JNIEnv* env, jobject jobj) {

            jclass cls = (*env)->GetObjectClass(env, jobj);
            // 局部静态变量，声明周期保存到程序结束，作用域只在当前函数
            static jfieldID key_id = NULL;
            if (key_id == NULL) {
                key_id = (*env)->GetFieldID(env, cls, "key", "Ljava/lang/String;");
                printf("load--------->\n");// 只会打印一次
            }

        }


- 全局变量缓存  
  library加载完成后调用  

        // 全局变量缓存
        // 初始化全局变量
        jfieldID key_fid;
        jmethodID random_mid;
        JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_initIds
        (JNIEnv* env, jclass cls) {
            key_fid = (*env)->GetFieldID(env, cls, "key", "Ljava/lang/String;");
            random_mid = (*env)->GetMethodID(env, cls, "getRandomInt", "(I)I");
        }        

        static {
            System.loadLibrary("c");
            // 加载完动态库执行
            initIds();
        }