package com.zcycn.fmod;

/**
 * <p>Class: com.zcycn.fmod.EffectUtils</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/4/9/8:50
 */
public class EffectUtils {

    //音效的类型
    public static final int MODE_NORMAL = 0;
    public static final int MODE_LUOLI = 1;
    public static final int MODE_DASHU = 2;
    public static final int MODE_JINGSONG = 3;
    public static final int MODE_GAOGUAI = 4;
    public static final int MODE_KONGLING = 5;

    /**
     * 音效处理
     * @param path String
     * @param type int
     */
    public static native void fix(String path, int type);

    static{
        // Try logging libraries...
        try {
            System.loadLibrary("fmodL");
        } catch (UnsatisfiedLinkError e) {
        }
        // Try release libraries...
        try {
            System.loadLibrary("fmod");
        } catch (UnsatisfiedLinkError e) {
        }

        // System.loadLibrary("stlport_shared");
        System.loadLibrary("effect_fix");
    }

}
