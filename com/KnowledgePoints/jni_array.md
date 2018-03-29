### 数组操作  

- 数据类型的转换  
  jintArray -> jint指针 -> c int数组  
  获取jni数组  GetXXXArrayElements  
  同步jni数组  ReleseXXXArrayElements  


        // 传入数组
        public native void giveArray(int[] arr);

		int[] array = {40, 12, 50, 86, 121};
		test.giveArray(array);
		for(int i:array) {
			System.out.println("--->" + i);
		}  

        int compare(int const* a, int const* b) {
            return (*a) - (*b);
        }

        // 数组操作
        JNIEXPORT void JNICALL Java_com_zcycn_jni_JniTest_giveArray
        (JNIEnv* env, jobject jobj, jintArray arr) {

            // jintArray -> jint指针 ->c int数组
            jint *elems = (*env)->GetIntArrayElements(env, arr, JNI_FALSE);
            // 数组长度
            int len = (*env)->GetArrayLength(env, arr);
            // 排序
            qsort(elems, len, sizeof(jint), compare);
            // 同步
            // JNI_OK  Java数组更新，释放C数组
            // JNI_ABORT  Java数组不更新，释放C数组
            // JNI_COMMIT  Java数组更新，不释放C数组，函数执行完还是会释放的
            (*env)->ReleaseIntArrayElements(env, arr, elems, JNI_OK);
        }        


### 返回一个固定长度的数组  

        // 返回数组
        JNIEXPORT jintArray JNICALL Java_com_zcycn_jni_JniTest_getArray
        (JNIEnv* env, jobject jobj, jint len) {

            // 创建指定大小数组
            jintArray jnit_arr = (*env)->NewIntArray(env, len);
            // 获取数组指针
            jint* elems = (*env)->GetIntArrayElements(env, jnit_arr, JNI_FALSE);
            int i = 0;
            for (; i < len; i++) {
                elems[i] = i;
            }
            // 两个不同数组，需要同步
            (*env)->ReleaseIntArrayElements(env, jnit_arr, elems, JNI_OK);
            return jnit_arr;

        }        

        // 获取一个固定长度的数组
        public native int[] getArray(int len);

        int[] array2 = test.getArray(10);
		for(int i:array2) {
			System.out.println("--->" + i);
		}