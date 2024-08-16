package com.oplus.dft;

/* loaded from: classes.dex */
public class Postman {
    public static native int nativedftPrintlnNative(DftHeapConfig dftHeapConfig, int i, String str, String str2);

    public static native boolean nativefreeDftHeap(DftHeapConfig dftHeapConfig);

    public static native boolean nativeinitDftHeap(DftHeapConfig dftHeapConfig);

    static {
        System.loadLibrary("postman_jni");
    }

    public static boolean initDftHeap(DftHeapConfig heapConfig) {
        return nativeinitDftHeap(heapConfig);
    }

    public static boolean freeDftHeap(DftHeapConfig heapConfig) {
        return nativefreeDftHeap(heapConfig);
    }

    public static int dftPrintlnNative(DftHeapConfig heapConfig, int prio, String tag, String msg) {
        return nativedftPrintlnNative(heapConfig, prio, tag, msg);
    }
}
