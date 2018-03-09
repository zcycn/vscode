package com.zcycn.ndk;

/**
 * <p>Class: com.zcycn.ndk.Watcher</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/3/8/20:48
 */

public class Watcher {

    static {
        System.loadLibrary("native-lib");
    }

    public native void createWatcher(String userId);
    public native void connectMonitor();

}
