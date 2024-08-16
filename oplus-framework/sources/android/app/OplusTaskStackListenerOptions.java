package android.app;

/* loaded from: classes.dex */
public class OplusTaskStackListenerOptions {
    public static final int FLAG_SKLIP_CALLBACK_SNAPSHOT = 1;
    public static final String TAG = "OplusTaskStackListenerOptions";
    private int mFlags;

    private OplusTaskStackListenerOptions() {
    }

    public OplusTaskStackListenerOptions(String info) {
        if (info != null) {
            try {
                this.mFlags = Integer.parseInt(info);
            } catch (Exception e) {
            }
        }
    }

    public static OplusTaskStackListenerOptions makeBasic(int flags) {
        OplusTaskStackListenerOptions options = new OplusTaskStackListenerOptions();
        options.mFlags = flags;
        return options;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(128);
        sb.append(this.mFlags);
        return sb.toString();
    }

    public static OplusTaskStackListenerOptions fromString(String info) {
        if (info != null) {
            OplusTaskStackListenerOptions options = new OplusTaskStackListenerOptions(info);
            return options;
        }
        return null;
    }

    public static boolean shouldSkipSnapshot(String descriptor) {
        boolean skip = (getExtensionFlags(descriptor) & 1) != 0;
        return skip;
    }

    private static int getExtensionFlags(String descriptor) {
        OplusTaskStackListenerOptions options = fromString(descriptor);
        if (options == null) {
            return 0;
        }
        int flags = options.mFlags;
        return flags;
    }
}
