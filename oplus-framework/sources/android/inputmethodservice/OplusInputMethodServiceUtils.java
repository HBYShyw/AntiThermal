package android.inputmethodservice;

import android.content.Context;

/* loaded from: classes.dex */
public class OplusInputMethodServiceUtils implements IOplusInputMethodServiceUtils {
    public static final int MODE_NAVIGATIONBAR_GESTURE = 2;
    public static final int MODE_NAVIGATIONBAR_GESTURE_SIDE = 3;
    public static final int OPLUS_NAVIGATION_BAR_COLOR = -1;
    public static final int RETRY_COUNT_MAX = 3;
    private static final String TAG = "InputMethodServiceUtils";
    private static volatile OplusInputMethodServiceUtils sInstance = null;
    private InputMethodService mInputMethodService;

    private OplusInputMethodServiceUtils() {
    }

    public static OplusInputMethodServiceUtils getInstance() {
        if (sInstance == null) {
            synchronized (OplusInputMethodServiceUtils.class) {
                if (sInstance == null) {
                    sInstance = new OplusInputMethodServiceUtils();
                }
            }
        }
        return sInstance;
    }

    @Override // android.inputmethodservice.IOplusInputMethodServiceUtils
    public void init(Context context) {
        this.mInputMethodService = (InputMethodService) context;
    }
}
