package com.oplus.acmutils;

/* loaded from: classes.dex */
public class OplusAcmUtils {
    private static final String TAG = "OplusAcmUtils";

    private static native int nativeAddDirName(String str, long j);

    private static native int nativeAddNomediaDirName(String str);

    private static native int nativeAddPkgName(String str, long j);

    private static native int nativeCloseDev();

    private static native int nativeDelDirName(String str);

    private static native int nativeDelNomediaDirName(String str);

    private static native int nativeDelPkgName(String str);

    private static native int nativeGetAcmOpstat();

    private static native long nativeGetDirFlag(String str);

    private static native long nativeGetPkgFlag(String str);

    private static native int nativeOpenDev();

    private static native int nativeSearchNomediaDir(String str);

    private static native int nativeSetAcmOpstat(int i);

    private OplusAcmUtils() {
    }

    public static int sOplusAcmOpenDev() {
        int ret = nativeOpenDev();
        return ret;
    }

    public static int sOplusAcmCloseDev() {
        int ret = nativeCloseDev();
        return ret;
    }

    public static int sOplusAcmSetAcmOpstat(int flag) {
        int ret = nativeSetAcmOpstat(flag);
        return ret;
    }

    public static int sOplusAcmGetAcmOpstat() {
        int ret = nativeGetAcmOpstat();
        return ret;
    }

    public static int sOplusAcmAddPkgName(String pkgname, long flag) {
        int ret = nativeAddPkgName(pkgname, flag);
        return ret;
    }

    public static int sOplusAcmDelPkgName(String pkgname) {
        int ret = nativeDelPkgName(pkgname);
        return ret;
    }

    public static long sOplusAcmGetPkgFlag(String pkgname) {
        long ret = nativeGetPkgFlag(pkgname);
        return ret;
    }

    public static int sOplusAcmAddDirName(String dirname, long flag) {
        int ret = nativeAddDirName(dirname, flag);
        return ret;
    }

    public static int sOplusAcmDelDirName(String dirname) {
        int ret = nativeDelDirName(dirname);
        return ret;
    }

    public static long sOplusAcmGetDirFlag(String dirname) {
        long ret = nativeGetDirFlag(dirname);
        return ret;
    }

    public static int sOplusAcmAddNomediaDirName(String dirname) {
        int ret = nativeAddNomediaDirName(dirname);
        return ret;
    }

    public static int sOplusAcmDelNomediaDirName(String dirname) {
        int ret = nativeDelNomediaDirName(dirname);
        return ret;
    }

    public static int sOplusAcmSearchNomediaDir(String dirname) {
        int ret = nativeSearchNomediaDir(dirname);
        return ret;
    }
}
