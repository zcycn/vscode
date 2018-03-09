### 系统服务

1. 通常指手机不可缺少的服务
     
     adb shell  
     ps 查看进程  
     PPID为1即父进程为1的为核心进程，进程名加d的为守护进程  
     kill -9 pid 结束进程  

2. C进程唤醒Service

        #include <jni.h>
        #include <string>
        #include <unistd.h>
        #include "native-lib.h"

        int m_child;
        const char *userId;

        void child_do_work();

        int child_create_channel();

        void child_listen_msg();

        extern "C"
        JNIEXPORT jstring

        JNICALL
        Java_com_zcycn_ndk_MainActivity_stringFromJNI(
                JNIEnv *env,
                jobject /* this */) {
            std::string hello = "Hello from C++";
            return env->NewStringUTF(hello.c_str());
        }

        const char *PATH = "/data/data/com.zcycn.ndk/my.sock";
        void child_do_work() {

            // 开启socket 服务端
            if(child_create_channel()){
                child_listen_msg();
            }

        }

        void child_listen_msg() {
            fd_set rfds;

            struct timeval timeout = {3,0};
            while(1){
                // 清空内容
                FD_ZERO(&rfds);
                FD_SET(m_child, &rfds);
                int r = select(m_child+1, &rfds, NULL, NULL, &timeout);
                LOGE("读取消息前 %d ", r);
                if(r > 0){
                    // 缓冲区
                    char pkg[256] = {0};
                    // 保证所读到信息是指定apk
                    if(FD_ISSET(m_child, &rfds)){
                        // 阻塞式函数
                        int result = read(m_child, pkg, sizeof(pkg));
                        // 开启服务
                        execlp("am","am","startservice","--user", userId, "com.zcycn.ndk/com.zcycn.ndk.ProcessService", (char*)NULL);
                        break;
                    }
                }
            }
        }

        int child_create_channel() {
            // 协议 类型
            int listenfd = socket(AF_LOCAL, SOCK_STREAM, 0);
            // linux socket 基于文件
            struct sockaddr_un addr;
            // 清空内存
            unlink(PATH);
            memset(&addr, 0, sizeof(sockaddr_un));
            addr.sun_family = AF_LOCAL;


            int confd = 0;
            strcpy(addr.sun_path, PATH);
            if(bind(listenfd, (const sockaddr *) &addr, sizeof(sockaddr_un)) < 0){
                LOGE("绑定错误");
                return 0;
            }

            listen(listenfd, 5);// 最多监听5个客户端
            while(1){
                // 客户端的地址
                if((confd = accept(listenfd, NULL, NULL)) < 0){
                    if(errno == EINTR){// errno 内部成员变量
                        continue;
                    }else{
                        LOGE("读取错误");
                        return 0;
                    }
                }
                m_child = confd;
                break;
            }
            return 1;
        }

        extern "C"
        JNIEXPORT void JNICALL
        Java_com_zcycn_ndk_Watcher_createWatcher(JNIEnv *env, jobject instance, jstring userId_) {
            userId = env->GetStringUTFChars(userId_, 0);

            // 开双进程
            pid_t pid = fork();
            if(pid < 0){
                // 失败
            }else if(pid == 0){
                // 子进程
                child_do_work();
            }else if(pid > 0){
                // 父进程
            }

            env->ReleaseStringUTFChars(userId_, userId);
        }

        extern "C"
        JNIEXPORT void JNICALL
        Java_com_zcycn_ndk_Watcher_connectMonitor(JNIEnv *env, jobject instance) {

            int socked;
            struct sockaddr_un addr;
            while(1){
                LOGE("客户端  父进程开始连接");
                socked = socket(AF_LOCAL, SOCK_STREAM, 0);
                if(socked < 0){
                    LOGE("连接失败");
                    return;
                }

                // 清空内存
                memset(&addr, 0, sizeof(sockaddr_un));
                addr.sun_family = AF_LOCAL;
                strcpy(addr.sun_path, PATH);

                if((connect(socked, (const sockaddr *) &addr, sizeof(sockaddr_un))) < 0){
                    LOGE("连接失败");
                    close(socked);
                    sleep(1);
                    continue;
                }

                LOGE("连接成功");
                break;
            }

        }     
