package android.view;

import android.R;
import android.content.Context;
import android.content.res.CompatibilityInfo;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Slog;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class OplusViewRootUtil implements IOplusViewRootUtil {
    private static final float EVENT_270 = 1.5707964f;
    private static final float EVENT_90 = -1.5707964f;
    private static final float EVENT_ORI = 0.0f;
    private static final float EVENT_OTHER = -3.1415927f;
    private static final float GESTURE_BOTTOM_AREA_PROP = 0.05f;
    private static final float GLOABL_SCALE_COMPAT_APP = 1.333333f;
    private static final String HATEEROMORPHISM = "oppo.systemui.disable.edgepanel";
    public static final String KEY_NAVIGATIONBAR_MODE = "hide_navigationbar_enable";
    private static final boolean LOCAL_LOGV = true;
    private static final int MODE_NAVIGATIONBAR = 0;
    private static final int MODE_NAVIGATIONBAR_GESTURE = 2;
    private static final int MODE_NAVIGATIONBAR_GESTURE_SIDE = 3;
    private static final int MODE_NAVIGATIONBAR_WITH_HIDE = 1;
    private static final String TAG = "OplusViewRootUtil";
    private int mHideNavigationbarArea;
    private boolean mIgnoring;
    private boolean mIsDisplayCompatApp;
    private int mScreenHeight;
    private int mScreenWidth;
    private int mSideGestureAreaWidth;
    private int mStatusBarHeight;
    private static OplusViewRootUtil sInstance = null;
    private static boolean mHeteromorphism = false;
    private static int mHeteromorphismHeight = -1;
    private float mCompactScale = 1.0f;
    private boolean mFullScreen = true;
    private DisplayInfo mDisplayInfo = new DisplayInfo();
    private int mNavBarMode = 0;

    public static OplusViewRootUtil getInstance() {
        OplusViewRootUtil oplusViewRootUtil;
        synchronized (OplusViewRootUtil.class) {
            if (sInstance == null) {
                sInstance = new OplusViewRootUtil();
            }
            oplusViewRootUtil = sInstance;
        }
        return oplusViewRootUtil;
    }

    @Override // android.view.IOplusViewRootUtil
    public void initSwipState(Display display, Context context) {
        Slog.d("OplusViewRootUtil", "initSwipState");
        initSwipState(display, context, false);
    }

    @Override // android.view.IOplusViewRootUtil
    public void initSwipState(Display display, Context context, boolean isDisplayCompatApp) {
        Slog.d("OplusViewRootUtil", "initSwipState, isDisplayCompatApp " + isDisplayCompatApp);
        this.mIsDisplayCompatApp = isDisplayCompatApp;
        if (display != null && display.getDisplayId() != -1 && display.getDisplayId() != 0) {
            Slog.d("OplusViewRootUtil", "don't initSwipState because display " + display.getDisplayId() + " is not default display");
            return;
        }
        display.getDisplayInfo(this.mDisplayInfo);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);
        int h = displayMetrics.heightPixels;
        int w = displayMetrics.widthPixels;
        this.mScreenHeight = h > w ? h : w;
        this.mScreenWidth = h > w ? w : h;
        Slog.d("OplusViewRootUtil", "mScreenHeight " + this.mScreenHeight + ", mScreenWidth " + this.mScreenWidth);
        int height = this.mDisplayInfo.logicalHeight;
        int width = this.mDisplayInfo.logicalWidth;
        this.mHideNavigationbarArea = (context.getResources().getDimensionPixelSize(R.dimen.notification_right_icon_size) + (height > width ? height : width)) - 21;
        this.mStatusBarHeight = context.getResources().getDimensionPixelSize(201654286);
        boolean z = !context.getPackageManager().hasSystemFeature(HATEEROMORPHISM);
        mHeteromorphism = z;
        if (z) {
            mHeteromorphismHeight = context.getResources().getInteger(202178562);
        }
        DisplayMetrics appMetrics = new DisplayMetrics();
        this.mDisplayInfo.getAppMetrics(appMetrics);
        this.mCompactScale = CompatibilityInfo.computeCompatibleScaling(appMetrics, (DisplayMetrics) null);
        this.mNavBarMode = Settings.Secure.getInt(context.getContentResolver(), KEY_NAVIGATIONBAR_MODE, 0);
        this.mSideGestureAreaWidth = context.getResources().getInteger(202178573);
        if (isDisplayCompatApp) {
            Slog.d("OplusViewRootUtil", "sIsDisplayCompatApp");
            this.mScreenHeight = (int) (this.mScreenHeight * GLOABL_SCALE_COMPAT_APP);
            this.mScreenWidth = (int) (this.mScreenWidth * GLOABL_SCALE_COMPAT_APP);
        }
        this.mStatusBarHeight = (int) (this.mScreenWidth * GESTURE_BOTTOM_AREA_PROP);
    }

    @Override // android.view.IOplusViewRootUtil
    public boolean needScale(int noncompatDensity, int density, Display display) {
        CompatibilityInfo compatibilityInfo = null;
        if (display != null && display.getDisplayAdjustments() != null) {
            compatibilityInfo = display.getDisplayAdjustments().getCompatibilityInfo();
        }
        if (compatibilityInfo != null && compatibilityInfo.isScalingRequired() && !compatibilityInfo.supportsScreen() && noncompatDensity != density) {
            return true;
        }
        return false;
    }

    @Override // android.view.IOplusViewRootUtil
    public float getCompactScale() {
        return this.mCompactScale;
    }

    @Override // android.view.IOplusViewRootUtil
    public int getScreenHeight() {
        return this.mScreenHeight;
    }

    @Override // android.view.IOplusViewRootUtil
    public int getScreenWidth() {
        return this.mScreenWidth;
    }

    @Override // android.view.IOplusViewRootUtil
    public boolean swipeFromBottom(MotionEvent event, int noncompatDensity, int density, Display display) {
        Slog.d("OplusViewRootUtil", "swipeFromBottom!");
        if (display.getDisplayId() != 0) {
            Slog.e("OplusViewRootUtil", "don't intercept event because display " + display.getDisplayId() + " is not default display");
            return false;
        }
        if ((event.getSource() & 2) == 0) {
            Slog.d("OplusViewRootUtil", "don't intercept event because event source is " + event.getSource());
            return false;
        }
        switch (event.getAction()) {
            case 0:
                int downY = (int) event.getRawY();
                int downX = (int) event.getRawX();
                float f = this.mCompactScale;
                if (f != 1.0f && f != -1.0f && needScale(noncompatDensity, density, display)) {
                    float f2 = this.mCompactScale;
                    downY = (int) ((downY * f2) + 0.5f);
                    downX = (int) ((f2 * downX) + 0.5f);
                }
                this.mIgnoring = shouldIgnore(downX, downY, event, display);
                break;
            case 1:
                if (this.mIgnoring) {
                    this.mIgnoring = false;
                    return true;
                }
                break;
        }
        return this.mIgnoring;
    }

    @Override // android.view.IOplusViewRootUtil
    public void checkGestureConfig(Context context) {
        this.mNavBarMode = Settings.Secure.getInt(context.getContentResolver(), KEY_NAVIGATIONBAR_MODE, 0);
    }

    @Override // android.view.IOplusViewRootUtil
    public DisplayInfo getDisplayInfo() {
        return this.mDisplayInfo;
    }

    @Override // android.view.IOplusViewRootUtil
    public IOplusLongshotViewHelper getOplusLongshotViewHelper(WeakReference<ViewRootImpl> viewAncestor) {
        return new OplusLongshotViewHelper(viewAncestor);
    }

    private boolean shouldIgnore(int downX, int downY, MotionEvent event, Display display) {
        int downX2;
        int downY2;
        boolean ignore;
        String str;
        boolean ignore2;
        String str2;
        int i;
        int i2;
        if (event.getDownTime() != event.getEventTime()) {
            Slog.d("OplusViewRootUtil", "do not ignore inject event MotionEvent:" + event);
            return false;
        }
        int rotation = 0;
        if (display != null) {
            rotation = display.getRotation();
        }
        if (!this.mIsDisplayCompatApp) {
            downX2 = downX;
            downY2 = downY;
        } else {
            Slog.d("OplusViewRootUtil", "shouldIgnore, sIsDisplayCompatApp");
            downX2 = (int) (downX * GLOABL_SCALE_COMPAT_APP);
            downY2 = (int) (downY * GLOABL_SCALE_COMPAT_APP);
        }
        int i3 = this.mNavBarMode;
        if (i3 != 0) {
            ignore = false;
            if (i3 != 1) {
                switch (rotation) {
                    case 0:
                    case 2:
                        str2 = "OplusViewRootUtil";
                        if (downY2 < this.mScreenHeight - this.mStatusBarHeight && ((downX2 >= (i = this.mSideGestureAreaWidth) && downX2 <= this.mScreenWidth - i) || i3 != 3)) {
                            ignore2 = false;
                            break;
                        } else {
                            ignore2 = true;
                            break;
                        }
                        break;
                    case 1:
                    case 3:
                        str2 = "OplusViewRootUtil";
                        if (downY2 < this.mScreenWidth - this.mStatusBarHeight && ((downX2 >= (i2 = this.mSideGestureAreaWidth) && downX2 <= this.mScreenHeight - i2) || i3 != 3)) {
                            ignore2 = false;
                            break;
                        } else {
                            ignore2 = true;
                            break;
                        }
                        break;
                    default:
                        str2 = "OplusViewRootUtil";
                        ignore2 = false;
                        break;
                }
                Slog.d(str2, "nav gesture mode swipeFromBottom ignore " + ignore2 + " downY " + downY2 + " mScreenHeight " + this.mScreenHeight + " mScreenWidth " + this.mScreenWidth + " mStatusBarHeight " + this.mStatusBarHeight + " globalScale " + this.mCompactScale + " nav mode " + this.mNavBarMode + " event " + event + " rotation " + rotation);
                return ignore2;
            }
            str = "OplusViewRootUtil";
        } else {
            ignore = false;
            str = "OplusViewRootUtil";
        }
        switch (rotation) {
            case 0:
            case 2:
                if (downY2 < this.mScreenHeight - this.mStatusBarHeight) {
                    ignore2 = false;
                    break;
                } else {
                    ignore2 = true;
                    break;
                }
            case 1:
                if (downX2 < this.mScreenHeight - this.mStatusBarHeight) {
                    ignore2 = false;
                    break;
                } else {
                    ignore2 = true;
                    break;
                }
            case 3:
                if (downX2 > this.mStatusBarHeight) {
                    ignore2 = false;
                    break;
                } else {
                    ignore2 = true;
                    break;
                }
            default:
                ignore2 = ignore;
                break;
        }
        Slog.d(str, "nav bar mode ignore " + ignore2 + " downX " + downX2 + " downY " + downY2 + " mScreenHeight " + this.mScreenHeight + " mScreenWidth " + this.mScreenWidth + " mStatusBarHeight " + this.mStatusBarHeight + " globalScale " + this.mCompactScale + " nav mode " + this.mNavBarMode + " rotation " + rotation + " event " + event);
        return ignore2;
    }
}
