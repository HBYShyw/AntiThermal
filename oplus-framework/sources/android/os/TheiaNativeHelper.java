package android.os;

import android.util.Log;
import dalvik.annotation.optimization.FastNative;

/* loaded from: classes.dex */
public class TheiaNativeHelper {
    private static final String TAG = "Theia";
    private static final String THEIA_LIB = "theia_jni";

    @FastNative
    public static native void nativeInitSigprotector();

    static {
        try {
            System.loadLibrary(THEIA_LIB);
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Failed to LoadLibrary theia_jni");
        }
    }
}
