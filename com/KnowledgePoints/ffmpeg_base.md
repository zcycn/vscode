### 音视频简介 

- 播放视频的流程  

        封装格式数据（MP4、FLV）-> 解封装 -> 视频压缩数据（H.264）   ->  音视频解码 -> 视频像素数据YUV... -> 音视频同步播放       
                                           音频压缩数据（AAC、MP3）                 音频采样数据PCM...   

- FFmpeg命令行  

可以通过命名行的方式直接使用FFmpeg  
类似于DOS操作系统的命令窗口，命令行的对立面是图形界面  

- 命令行版本说明 

static：只包含3个体积很大的exe  
shared：除了3个体积较小的exe，还包含dll动态库文件  
dev：只包含了开发用的头文件(*.h)和到入库文件(*.lib)  

### 编译环境 

- 虚拟机安装  
  参考：[虚拟机安装](https://github.com/zcycn/vscode/blob/master/com/zcycn/old/Linux/虚拟机安装.md)  

- 虚拟机的两个账户  
  cnzcy 123456  
  root 123456  
  ubuntu默认是不会启用root用户的，设置方法参考下面地址：  
  https://blog.csdn.net/sunxiaoju/article/details/51993091  

- Ubuntu拒绝root用户ssh远程登录  

        #sudo vim /etc/ssh/sshd_config
        找到并用#注释掉这行：PermitRootLogin prohibit-password
        新建一行 添加：PermitRootLogin yes
        重启服务
        #sudo service ssh restart

PermitRootLogin yes  
允许root登录，设为yes  
PermitRootLogin prohibit-password   
允许root登录，但是禁止root用密码登录  

- vim与zip安装，一般默认已安装  

        sudo apt-get install vim-gtk
        sudo apt-get install zip  

### NDK安装 

1. 设置文件夹权限  

chmod 777 -R ndk //给ndk所有目录文件权限       

2. 解压ndk  

        ./android-ndk-r10e-linux-x86_64.bin      

3. 当前用户环境变量  

        // ~ 当前用户目录
        vim ~/.bashrc   

        添加：
        export NDKROOT=/usr/ndk/android-ndk-r10e
        export PATH=$NDKROOT:$PATH
        更新环境变量：
        source ~/.bashrc

4. 解压ffmpeg  

        unzip ffmpeg-2.6.9.zip  

5. 编译ffmpeg  

        1）编写shell脚本文件  
        2）给文件权限：chmod 777 android_build.sh  
        如果不是Linux文件需要通过   
        dos2unix android_build.sh   
        如果没有安装dos2unix  
        apt-get install dos2unix  
        3）执行 ./android_build.sh  

                