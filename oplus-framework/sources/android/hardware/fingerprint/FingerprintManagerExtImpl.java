package android.hardware.fingerprint;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.util.Slog;

/* loaded from: classes.dex */
public class FingerprintManagerExtImpl implements IFingerprintManagerExt {
    public static final int FINGERPRINT_ACQUIRED_ALREADY_ENROLLED = 1002;
    public static final int FINGERPRINT_ACQUIRED_ALREADY_ENROLLED_AIDL = 102;
    public static final int FINGERPRINT_ACQUIRED_TOO_SIMILAR = 1001;
    public static final int FINGERPRINT_ACQUIRED_TOO_SIMILAR_AIDL = 101;
    public static final int FINGERPRINT_ACQUIRED_TOUCH_UP = 1301;
    private static final String TAG = "FingerprintManagerExtImpl";
    public Context mContext;
    public String mStrReturn = "already enrolled finger";

    public FingerprintManagerExtImpl(Object base) {
        Slog.d(TAG, "FingerprintManagerExtImpl is inited");
    }

    public String getAcquiredString(int acquireInfo, int vendorCode) {
        switch (acquireInfo) {
            case 101:
                this.mStrReturn = "acquared too similar";
                return "acquared too similar";
            case 102:
                this.mStrReturn = "already enrolled finger";
                return "already enrolled finger";
            case 1001:
                this.mStrReturn = "acquared too similar";
                return "acquared too similar";
            case 1002:
                this.mStrReturn = "already enrolled finger";
                return "already enrolled finger";
            case 1301:
                this.mStrReturn = "fingerprint touch up";
                return "fingerprint touch up";
            default:
                return null;
        }
    }

    public boolean isKeyguard(Context context, String clientPackage) {
        ComponentName keyguardComponent = ComponentName.unflattenFromString(context.getResources().getString(R.string.data_saver_enable_button));
        String keyguardPackage = keyguardComponent != null ? keyguardComponent.getPackageName() : "com.android.systemui";
        return keyguardPackage != null && keyguardPackage.equals(clientPackage);
    }
}
