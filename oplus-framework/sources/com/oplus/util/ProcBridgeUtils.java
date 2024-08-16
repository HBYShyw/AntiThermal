package com.oplus.util;

/* loaded from: classes.dex */
public class ProcBridgeUtils {
    private static final String TAG = "ProcBridgeUtils";

    private static native String readNativeProcNode(String str);

    private static native int writeNativeProcNode(String str, String str2);

    public static void writeProcNode(String filePath, String val) {
        writeNativeProcNode(filePath, val);
    }

    public static String readProcNode(String filePath) {
        String str = readNativeProcNode(filePath);
        if (str != null) {
            String[] firstline = str.split("\n|\r");
            if (firstline.length >= 1 && firstline[0] != null) {
                return firstline[0];
            }
            return null;
        }
        return null;
    }
}
