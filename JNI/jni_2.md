### jni传递与返回数组

主要通过GetIntArrayElements函数来转换jni数组和c数组   
数组的同步需要ReleaseIntArrayElements这个函数    

    int compare(int *a, int *b) {
        return (*a) - (*b);
    }

    JNIEXPORT jintArray JNICALL Java_com_zcycn_jni_JniTest_giveArray
    (JNIEnv *env, jobject jobj, jintArray arr, jint length) {
        // jintArray -> jint指针 -> c int数组
        jint *elems = (*env)->GetIntArrayElements(env, arr, NULL);
        int len = (*env)->GetArrayLength(env, arr);
        // 排序
        qsort(elems, len, sizeof(jint), compare);
        // 同步c 与 java
        // mode 0 java数组更新，并释放c数组
        // JNI_ABORT java数组不更新，但释放c数组
        // JNI_COMMIT java数组更新，不释放c数组（函数执行完释放）
        (*env)->ReleaseIntArrayElements(env, arr, elems, 0);

        // 创建指定大小数组
        jintArray jint_arr = (*env)->NewIntArray(env, length);
        jint *es = (*env)->GetIntArrayElements(env, jint_arr, NULL);
        int i = 0;
        for (; i < length; i++) {
            es[i] = i;
        }
        // 同步  同步的原因是jint_arr 与 es是两个不同的数组
        (*env)->ReleaseIntArrayElements(env, jint_arr, es, 0);
        return jint_arr;
    }    


    public native int[] giveArray(int[] array, int len);


    int[] array = {5,10,6,9,8,20};
    int[] array2 = j.giveArray(array, 10);
    for(int i : array){
        System.out.println(i);
    }
    
    for(int i : array2){
        System.out.println(i+"===");
    }


### 局部引用

使用过程中释放，以节省内存

    // 局部引用
    // 访问一个较大的java对象，使用完还要进行其他操作
    // 创建太多局部引用，占用太多资源，与后面操作无关联性

    JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_localRef
    (JNIEnv *env, jobject jobj) {
        int i = 0;
        for (; i < 5; i++) {
            jclass cls = (*env)->FindClass(env, "java/util/Date");
            jmethodID constructor_mid = (*env)->GetMethodID(env, cls, "<init>", "()V");
            jobject obj = (*env)->NewObject(env, cls, constructor_mid);
            // 进行操作
            // 用完释放
            (*env)->DeleteLocalRef(env, obj);
            // 进行操作
        }
    }    


### 全局引用

JNI提供了全局引用的创建和释放函数

    // 全局引用
    // 共享可以跨线程
    jstring global_str;

    // 创建
    JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_createGlobalRef
    (JNIEnv *env, jobject jobj) {

        jstring obj = (*env)->NewStringUTF(env, "jni development is powerful!");
        // 创建全局引用
        global_str = (*env)->NewGlobalRef(env, obj);

    }

    // 获得
    JNIEXPORT jstring JNICALL Java_com_zcycn_jni_JniTest_getGlobalRef
    (JNIEnv *env, jobject jobj) {

        return global_str;

    }

    // 释放
    JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_deleteGlobalRef
    (JNIEnv *env, jobject jobj) {

        (*env)->DeleteGlobalRef(env, global_str);

    }


    // 创建
	public native void createGlobalRef();
	
	// 获得
	public native String getGlobalRef();
	
	// 释放
	public native void deleteGlobalRef();

    j.createGlobalRef();
    System.out.println(j.getGlobalRef());
    j.deleteGlobalRef();
    System.out.println(j.getGlobalRef());

    // 释放后如果java继续调用就会空指针异常

> 弱全局引用         
> 在内存不足时，释放引用对象     
> 创建 NewWeakGloblRef 销毁 DeleteGlobalWeakRef     

### 异常处理

保证java能继续执行，JNI发生异常后，java之后的代码就会停止运行
需要自己判断异常有没有发生       
需要自己处理才可以抛异常给java   
JNI自己抛出的异常无法被java层捕获        

    // 异常
	public native void exception();

    try {
        j.exception();
    } catch (Exception e) {
        e.printStackTrace();
    }

    // 异常处理
    // 保证java能继续执行
    // 可以抛异常给java
    // JNI自己抛出的异常无法被java层捕获
    JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_exception
    (JNIEnv *env, jobject jobj) {

        jclass cls = (*env)->GetObjectClass(env, jobj);
        jfieldID fid = (*env)->GetFieldID(env, cls, "key2", "Ljava/lang/String;");
        // 检测异常
        jthrowable exception = (*env)->ExceptionOccurred(env);
        if (exception != NULL) {
            // 让java能继续执行，这里做清空异常处理
            (*env)->ExceptionClear(env);

            // 补救
            fid = (*env)->GetFieldID(env, cls, "key", "Ljava/lang/String;");
        }

        // 获得属性
        jstring jstr = (*env)->GetObjectField(env, jobj, fid);
        char *str = (*env)->GetStringUTFChars(env, jstr, NULL);

        // 对比字符串  不相等
        if (_stricmp(str, "super json1") != 0) {
            // 给java层处理
            jclass newExcCls = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
            (*env)->ThrowNew(env, newExcCls, "keys is invalid");
        }
    }

### 缓存策略

如果jni中一个函数经常调用，那么一些变量就可以写出局部静态变量，多次使用，但这个局部变量在函数执行完变量虽然销毁，但值还在内存中

    // 缓存策略
    JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_cached
    (JNIEnv *env, jobject jobj) {
        jclass cls = (*env)->GetObjectClass(env, jobj);
        
        // 只获取一次
        // 局部静态变量，作用域销毁，但值还在内存中
        static jfieldID key_id = NULL;
        if (key_id == NULL) {
            key_id = (*env)->GetFieldID(env, cls, "key", "Ljava/lang/String;");
            printf("------GetFieldID-----\n");
        }
    }

### jni加载时初始化

    // 加载动态库
	static{
		System.loadLibrary("jni_study");
		initIds();
	}

    public static native void initIds();

    那么c中可以如下写法

    // 全局初始化，动态库加载完立即缓存
    jfieldID key_fid;
    jmethodID random_mid;
    JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_initIds
    (JNIEnv *env, jclass jcls) {
        key_fid = (*env)->GetFieldID(env, jcls, "key", "Ljava/lang/String;");
        random_mid = (*env)->GetMethodID(env, jcls, "genRandomInt", "(I)I");
    }