package android.inputmethodservice;

import android.content.Context;
import android.text.TextUtils;

/* loaded from: classes.dex */
public class OplusInputMethodCompatUtils {
    private static final int FULL_NAV_HEIGHT = 44;
    private static final int HALF_NAV_HEIGHT = 16;
    private static final String INPUTMETHOD_PACKAGE_SECURITY = "com.oplus.securitykeyboard";
    private static final int MAX_FLOATING_HEIGHT = 100;
    private static final int MIN_FLOATING_HEIGHT = 16;
    private static final float PIXEL_OFFSET = 0.5f;
    private static final String STR_ANDROID_INPUTMETHOD_PACKAGE = "com.android.";
    private static final int VALUE_DEFAULT_GESTURE_BOTTOM = 22;
    private static final int VALUE_DEFAULT_GESTURE_SIDE = 24;
    private static final int VALUE_DEFAULT_GESTURE_SIDE_MAX = 40;
    private Context mContext;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OplusInputMethodCompatUtils(Context context) {
        this.mContext = context;
    }

    public boolean supportFloating() {
        String packageName = this.mContext.getPackageName();
        if (!TextUtils.isEmpty(packageName)) {
            if (INPUTMETHOD_PACKAGE_SECURITY.equals(packageName) || packageName.startsWith(STR_ANDROID_INPUTMETHOD_PACKAGE)) {
                return false;
            }
            return true;
        }
        return true;
    }

    public boolean isAndroidPackage() {
        String packageName = this.mContext.getPackageName();
        return !TextUtils.isEmpty(packageName) && packageName.startsWith(STR_ANDROID_INPUTMETHOD_PACKAGE);
    }

    public int getMinFloatingHeight() {
        return (int) (this.mContext.getResources().getDisplayMetrics().density * 16.0f);
    }

    public int getMaxFloatingHeight() {
        return (int) (this.mContext.getResources().getDisplayMetrics().density * 100.0f);
    }

    public int getHalfNavHeight() {
        return (int) (this.mContext.getResources().getDisplayMetrics().density * 16.0f);
    }

    public int getFullNavHeight() {
        return (int) (this.mContext.getResources().getDisplayMetrics().density * 44.0f);
    }

    public int getBottomGestureHeight() {
        return dpToPx(22);
    }

    public int getMaxSideGestureWidth() {
        return dpToPx(40);
    }

    public int getSideGestureWidth() {
        return dpToPx(24);
    }

    public int dpToPx(int dp) {
        return (int) ((this.mContext.getResources().getDisplayMetrics().density * dp) + 0.5f);
    }
}
