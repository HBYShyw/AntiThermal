package com.android.internal.policy;

import android.animation.ObjectAnimator;
import android.common.OplusFeatureCache;
import android.common.OplusFrameworkFactory;
import android.content.Context;
import android.content.pm.OplusPackageManager;
import android.content.res.Configuration;
import android.content.res.OplusBaseConfiguration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.SystemProperties;
import android.provider.oplus.Telephony;
import android.view.IOplusViewHooks;
import android.view.IViewRootImplExt;
import android.view.IWindow;
import android.view.MotionEvent;
import android.view.OplusTaskBarUtils;
import android.view.ViewRootImplExtImpl;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.view.debug.IOplusViewDebugManager;
import com.oplus.bluetooth.OplusBluetoothClass;
import com.oplus.debug.InputLog;
import com.oplus.statusbar.OplusStatusBarController;
import com.oplus.util.OplusDarkModeUtil;
import com.oplus.util.OplusTypeCastingHelper;

/* loaded from: classes.dex */
public class DecorViewExtImpl implements IDecorViewExt {
    private static final int ACTION_MODE_FADE_ALPHA_DURATION = 100;
    private static final String TAG = "DecorViewExtImpl";
    private static final String WECHAT_PACKAGE_NAME = "com.tencent.mm";
    private static boolean mDebugSystemBar = false;
    private int mDarkModeBgColor;
    private DecorView mDecorView;
    private final OplusPackageManager mOplusPm;
    private IOplusViewHooks mViewHooks;
    private PhoneWindow mWindow;
    private final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private IViewRootImplExt mViewRootImplExt = null;

    public DecorViewExtImpl(Object decorView) {
        DecorView decorView2 = (DecorView) decorView;
        this.mDecorView = decorView2;
        Resources resources = decorView2.getResources();
        this.mViewHooks = (IOplusViewHooks) OplusFrameworkFactory.getInstance().getFeature(IOplusViewHooks.DEFAULT, new Object[]{this.mDecorView, resources});
        this.mOplusPm = new OplusPackageManager(this.mDecorView.getContext());
        mDebugSystemBar = SystemProperties.getBoolean("debug.log.systembar", false);
    }

    private boolean isOplusStyle() {
        IOplusViewHooks iOplusViewHooks = this.mViewHooks;
        return iOplusViewHooks != null && iOplusViewHooks.isOplusStyle();
    }

    public boolean hookDecorView(Context context, boolean defaultForceWindowDrawsBarBackgrounds) {
        OplusPackageManager pm = new OplusPackageManager();
        if (pm.inCptWhiteList(31, context.getApplicationInfo().packageName)) {
            return false;
        }
        return defaultForceWindowDrawsBarBackgrounds;
    }

    public void hookSetHandledPrimaryActionMode(ObjectAnimator fadeAnim) {
        if (isOplusStyle() && fadeAnim != null) {
            fadeAnim.setDuration(100L);
        }
    }

    public void hookOnDestroyActionMode(ObjectAnimator fadeAnim) {
        if (isOplusStyle() && fadeAnim != null) {
            fadeAnim.setDuration(100L);
        }
    }

    public boolean isDebugSystemBar() {
        return mDebugSystemBar;
    }

    public void updatePhoneWindow(PhoneWindow window) {
        this.mWindow = window;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void inputLog(String level, String tag, String msg, MotionEvent event) {
        char c;
        switch (level.hashCode()) {
            case 100:
                if (level.equals("d")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 118:
                if (level.equals(Telephony.BaseMmsColumns.MMS_VERSION)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 3149:
                if (level.equals("d1")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 3707:
                if (level.equals("v1")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                if (InputLog.isLevelDebug()) {
                    InputLog.d(tag, msg);
                    return;
                }
                return;
            case 1:
                InputLog.v(tag, msg);
                return;
            case 2:
                if (event != null && InputLog.isLevelDebug() && InputLog.isVerboseAction(event.getAction())) {
                    InputLog.d(tag, msg);
                    return;
                }
                return;
            case 3:
                if (InputLog.isLevelVerbose()) {
                    InputLog.v(tag, msg);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public int getLegacyNavBarBackgroundColor() {
        if (OplusDarkModeUtil.isNightMode(this.mDecorView.getContext()) || this.mOplusPm.isClosedSuperFirewall()) {
            return OplusBluetoothClass.Device.UNKNOWN;
        }
        return -1;
    }

    public void draw(Canvas canvas, int width, int height) {
    }

    public void setWindow(IWindow window) {
    }

    public void onConfigurationChanged(Configuration newConfig, Context context) {
        if (this.mWindow.getWrapper().getExtImpl().isUseDefaultNavigationBarColor()) {
            this.mWindow.mNavigationBarColor = OplusStatusBarController.getDefaultNavigationBarColor(this.mDecorView.getContext());
        }
        this.mDecorView.updateColorViews((WindowInsets) null, false);
    }

    ViewRootImplExtImpl getViewrootExtImpl() {
        IViewRootImplExt iViewRootImplExt = this.mViewRootImplExt;
        if (iViewRootImplExt != null) {
            return (ViewRootImplExtImpl) OplusTypeCastingHelper.typeCasting(ViewRootImplExtImpl.class, iViewRootImplExt);
        }
        DecorView decorView = this.mDecorView;
        if (decorView != null && decorView.getViewRootImpl() != null) {
            this.mViewRootImplExt = this.mDecorView.getViewRootImpl().getWrapper().getExtImpl();
        }
        return (ViewRootImplExtImpl) OplusTypeCastingHelper.typeCasting(ViewRootImplExtImpl.class, this.mViewRootImplExt);
    }

    public void initDarkModeBackgroundColor() {
        this.mDarkModeBgColor = OplusStatusBarController.getDarkModeBackgroundColor(this.mDecorView.getContext());
    }

    public void requestLayoutForDarkModeBackgroundView() {
        int newDarkModeBgColor = OplusStatusBarController.getDarkModeBackgroundColor(this.mDecorView.getContext());
        if (this.mDarkModeBgColor != newDarkModeBgColor) {
            if (this.mDecorView.getNavigationBarBackgroundView() != null) {
                this.mDecorView.getNavigationBarBackgroundView().requestLayout();
            }
            this.mDarkModeBgColor = newDarkModeBgColor;
        }
    }

    public void requestUpdateColorViewsForPocketStudio(Context context, DecorView decor, Configuration newConfig) {
        WindowMetrics windowMetrics;
        OplusBaseConfiguration baseConfig = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, newConfig);
        if (baseConfig == null) {
            return;
        }
        int newScenario = baseConfig.mOplusExtraConfiguration.getScenario();
        String packageName = context.getPackageName();
        if (packageName != null && packageName.equals(WECHAT_PACKAGE_NAME) && (windowMetrics = ((WindowManager) context.getSystemService(WindowManager.class)).getCurrentWindowMetrics()) != null && newScenario == 2) {
            decor.updateColorViews(windowMetrics.getWindowInsets(), true);
        }
    }

    public boolean isClosedSuperFirewall() {
        OplusPackageManager oplusPackageManager = this.mOplusPm;
        return oplusPackageManager != null && oplusPackageManager.isClosedSuperFirewall();
    }

    public void markOnDecorMeasure(DecorView decorView, int widthMeasuredSpec, int heightMeasuredSpec) {
        getViewDebugManager().markOnDecorMeasure(decorView, widthMeasuredSpec, heightMeasuredSpec);
    }

    public void markOnDecorLayout(DecorView decorView, int left, int top, int right, int bottom) {
        getViewDebugManager().markOnDecorLayout(decorView, left, top, right, bottom);
    }

    public void markOnDecorSetFrame(DecorView decorView, int l, int t, int r, int b) {
        getViewDebugManager().markOnDecorSetFrame(decorView, l, t, r, b);
    }

    public IOplusViewDebugManager getViewDebugManager() {
        return (IOplusViewDebugManager) OplusFeatureCache.getOrCreate(IOplusViewDebugManager.mDefault, new Object[0]);
    }

    public int forceMarginByTaskBar(WindowInsets insets, DecorView decorView, int lastBottom) {
        return OplusTaskBarUtils.getInstance().forceMarginByTaskBar(insets, decorView, lastBottom);
    }
}
