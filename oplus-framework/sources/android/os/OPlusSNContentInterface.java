package android.os;

import android.util.Log;

/* loaded from: classes.dex */
public class OPlusSNContentInterface {
    public static final int CMD_COPY = 3;
    public static final int CMD_COPY_CANCEL = 6;
    public static final int CMD_GENERIC_FAIL = 7;
    public static final int CMD_GENERIC_RESERVED = 8;
    public static final int CMD_MEDIA_RECEIVED = 5;
    public static final int CMD_MOUNT = 1;
    public static final int CMD_MW_FAIL = 9;
    public static final int CMD_REFRESH = 4;
    public static final int CMD_UNMOUNT = 2;
    public static final String KEY_COMMAND = "key_command";
    public static final String KEY_COPIED_DATA_SIZE = "key_copied_data_size";
    public static final String KEY_MEDIA_PATH = "key_media_path";
    public static final String KEY_STATUS = "key_status";
    public static final String KEY_TOTAL_SIZE = "key_total_size";
    public static final int RESULT_CODE = 1;
    private static final String TAG = OPlusSNContentInterface.class.getSimpleName();
    private static ResultReceiver mResultReceiver;

    public static native boolean nativeInitMW(Object obj);

    public native int nativeDeInitMW();

    public native int nativeSendUpdateCmd(String str, String str2, int i, int i2);

    public native void nativeSetExternalSDPath(String str);

    public native void nativeSetWhiteListedUID(int i);

    static {
        System.loadLibrary("snmcjni");
    }

    public static void addResultReceiver(ResultReceiver resultReceiver) {
        mResultReceiver = resultReceiver;
    }

    public static void removeResultReceiver() {
        mResultReceiver = null;
    }

    public static int PostServiceCBFromNative(Object o1, int command, int status, Object o2, int copiedDatasize, int totalsize, String mediaPath) {
        String str = TAG;
        Log.d(str, "Received callback from native layer");
        Log.d(str, "PostServiceCBFromNative Command: " + command + " Status: " + status + "Copied Data size: " + copiedDatasize + "Total size: " + totalsize + "MediaPath: " + mediaPath);
        if (mResultReceiver != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_COMMAND, command);
            bundle.putInt(KEY_STATUS, status);
            bundle.putInt(KEY_TOTAL_SIZE, totalsize);
            bundle.putInt(KEY_COPIED_DATA_SIZE, copiedDatasize);
            bundle.putString(KEY_MEDIA_PATH, mediaPath);
            Log.d(str, "Send result to app layer ");
            mResultReceiver.send(1, bundle);
        } else {
            Log.d(str, "Callback not register");
        }
        return 1;
    }
}
