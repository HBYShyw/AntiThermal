package android.view.accessibility;

/* loaded from: classes.dex */
public class AccessibilityManagerExtImpl implements IAccessibilityManagerExt {
    private static final int COLOR_STATE_DIRECT_ENABLED = 128;
    private AccessibilityManager mAccessibilityManager;
    private boolean mIsDirectEnabled = false;
    private final Object mLock = new Object();

    public AccessibilityManagerExtImpl(Object object) {
        if (object != null && (object instanceof AccessibilityManager)) {
            this.mAccessibilityManager = (AccessibilityManager) object;
        }
    }

    public void setDirectEnabledState(int stateFlags) {
        synchronized (this.mLock) {
            this.mIsDirectEnabled = (stateFlags & 128) != 0;
        }
    }

    public boolean getDirectEnabledState() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsDirectEnabled;
        }
        return z;
    }

    public boolean directEnable(boolean managerEnable) {
        return managerEnable && getDirectEnabledState();
    }
}
