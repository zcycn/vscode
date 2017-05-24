### jni传递与返回数组

主要通过GetIntArrayElements函数来转换jni数组和c数组   
数组的同步需要ReleaseIntArrayElements这个函数      

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