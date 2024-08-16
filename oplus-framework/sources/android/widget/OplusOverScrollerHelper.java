package android.widget;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Slog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class OplusOverScrollerHelper implements IOplusOverScrollerHelper {
    private static final String TAG = "OplusOverScrollerHelper";
    private boolean mForSpringOverScroller;
    protected OverScroller mScroller;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static boolean sOptEnable = false;

    public OplusOverScrollerHelper(OverScroller overScroller) {
        this.mScroller = overScroller;
        if ((overScroller instanceof SpringOverScroller) && !sOptEnable) {
            this.mForSpringOverScroller = true;
        }
    }

    public static void logD(String content) {
        Slog.d(TAG, content);
    }

    @Override // android.widget.IOplusOverScrollerHelper
    public void setOptEnable(boolean enable) {
        sOptEnable = enable;
    }

    @Override // android.widget.IOplusOverScrollerHelper
    public int getFinalX(int x) {
        if (this.mForSpringOverScroller) {
            int result = ((SpringOverScroller) this.mScroller).getOplusFinalX();
            return result;
        }
        return x;
    }

    @Override // android.widget.IOplusOverScrollerHelper
    public int getFinalY(int y) {
        if (this.mForSpringOverScroller) {
            int result = ((SpringOverScroller) this.mScroller).getOplusFinalY();
            return result;
        }
        return y;
    }

    @Override // android.widget.IOplusOverScrollerHelper
    public int getCurrX(int x) {
        if (this.mForSpringOverScroller) {
            int result = ((SpringOverScroller) this.mScroller).getOplusCurrX();
            return result;
        }
        return x;
    }

    @Override // android.widget.IOplusOverScrollerHelper
    public int getCurrY(int y) {
        if (this.mForSpringOverScroller) {
            int result = ((SpringOverScroller) this.mScroller).getOplusCurrY();
            return result;
        }
        return y;
    }

    @Override // android.widget.IOplusOverScrollerHelper
    public boolean setFriction(float friction) {
        if (this.mForSpringOverScroller) {
            ((SpringOverScroller) this.mScroller).setFlingFriction(friction);
            return true;
        }
        return false;
    }

    @Override // android.widget.IOplusOverScrollerHelper
    public boolean isFinished(boolean finished) {
        if (this.mForSpringOverScroller) {
            boolean result = ((SpringOverScroller) this.mScroller).isOplusFinished();
            return result;
        }
        return finished;
    }

    @Override // android.widget.IOplusOverScrollerHelper
    public boolean isForceUsingSpring(Context context, boolean optHelperEnable) {
        String packageName = context.getPackageName();
        if (packageName != null && packageName.contains("gallery3d")) {
            return false;
        }
        try {
            String packageList = Settings.System.getString(context.getContentResolver(), "spring_overscroller_package_list");
            if (packageList != null && packageList.length() > 1) {
                String subString = packageList.substring(1, packageList.length() - 1);
                List<String> list = new ArrayList<>(Arrays.asList(subString.split(", ")));
                boolean isInList = list.contains(packageName);
                if (list.contains("disable.all.spring")) {
                    return false;
                }
                if (DEBUG) {
                    logD("package: " + packageName + (isInList ? " is in packageList" : " is not in packageList"));
                }
                if (isInList) {
                    return false;
                }
                return true;
            }
        } catch (Throwable t) {
            logD(t.toString());
        }
        return true;
    }

    @Override // android.widget.IOplusOverScrollerHelper
    public boolean isForSpringOverScroller() {
        return this.mForSpringOverScroller;
    }
}
