package oplus.android;

import android.app.ApplicationPackageManager;
import android.app.IOplusAppHeapManager;
import android.app.IOplusCommonInjector;
import android.app.IOplusCompactWindowAppManager;
import android.app.IOplusEnterpriseAndOperatorFeature;
import android.app.OplusAppHeapManager;
import android.app.OplusCompactWindowAppManager;
import android.app.OplusEnterpriseAndOperatorFeature;
import android.app.UxIconPackageManagerExt;
import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.common.OplusFeatureManager;
import android.common.OplusFrameworkFactory;
import android.content.Context;
import android.content.res.IOplusThemeManager;
import android.content.res.IUxIconPackageManagerExt;
import android.content.res.OplusThemeManager;
import android.content.res.Resources;
import android.drawable.IOplusGradientHooks;
import android.drawable.OplusGradientHooksImpl;
import android.hardware.IOplusCameraStatisticsManager;
import android.hardware.IOplusCameraUtils;
import android.hardware.OplusCameraStatisticsManager;
import android.hardware.OplusCameraUtils;
import android.hardware.camera2.IOplusCamera2StatisticsManager;
import android.hardware.camera2.IOplusCameraManager;
import android.hardware.camera2.OplusCamera2StatisticsManager;
import android.hardware.camera2.OplusCameraManager;
import android.inputmethodservice.IOplusInputMethodServiceUtils;
import android.inputmethodservice.OplusInputMethodServiceUtils;
import android.nwpower.IOAppNetControlManager;
import android.nwpower.OAppNetControlManager;
import android.os.BatteryStats;
import android.os.IOplusDailyBattProtoManager;
import android.os.OplusDailyBattProtoManager;
import android.text.ITextJustificationHooks;
import android.text.TextJustificationHooksImpl;
import android.util.Log;
import android.view.IOplusAccidentallyTouchHelper;
import android.view.IOplusBurmeseZgHooks;
import android.view.IOplusDirectViewHelper;
import android.view.IOplusScrollToTopManager;
import android.view.IOplusViewHooks;
import android.view.IOplusViewRootUtil;
import android.view.OplusAccidentallyTouchHelper;
import android.view.OplusBurmeseZgFlagHooksImpl;
import android.view.OplusDirectViewHelper;
import android.view.OplusScrollToTopManager;
import android.view.OplusViewHooksImp;
import android.view.OplusViewRootUtil;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.autolayout.IOplusAutoLayoutManager;
import android.view.autolayout.OplusAutoLayoutManager;
import android.view.debug.IOplusViewDebugManager;
import android.view.debug.OplusViewDebugManager;
import android.view.rgbnormalize.IOplusRGBNormalizeManager;
import android.view.rgbnormalize.OplusRGBNormalizeManager;
import android.view.viewextract.IOplusViewExtractManager;
import android.view.viewextract.OplusViewExtractManager;
import android.widget.IOplusCursorFeedbackManager;
import android.widget.IOplusCustomizeTextViewFeature;
import android.widget.IOplusDragTextShadowHelper;
import android.widget.IOplusFloatingToolbarUtil;
import android.widget.IOplusOverScrollerHelper;
import android.widget.IOplusReorderActionMenuManager;
import android.widget.IOplusScrollOptimizationHelper;
import android.widget.OplusCursorFeedbackManager;
import android.widget.OplusCustomizeTextViewFeature;
import android.widget.OplusDragTextShadowHelper;
import android.widget.OplusFloatingToolbarUtil;
import android.widget.OplusOverScrollerHelper;
import android.widget.OplusReorderActionMenuManager;
import android.widget.OplusScrollOptimizationHelper;
import android.widget.OverScroller;
import com.android.internal.app.IOplusResolverManager;
import com.android.internal.app.IOplusResolverStyle;
import com.android.internal.app.OplusResolverManager;
import com.android.internal.app.OplusResolverStyle;
import com.oplus.app.IOplusAppDynamicFeatureManager;
import com.oplus.app.OplusAppDynamicFeatureManager;
import com.oplus.darkmode.IOplusDarkModeManager;
import com.oplus.darkmode.OplusDarkModeManager;
import com.oplus.deepthinker.IOplusDeepThinkerManager;
import com.oplus.deepthinker.OplusDeepThinkerManagerDecor;
import com.oplus.dynamicvsync.IOplusDynamicVsyncFeature;
import com.oplus.dynamicvsync.OplusDynamicVsyncFeature;
import com.oplus.favorite.IOplusFavoriteManager;
import com.oplus.favorite.OplusFavoriteManager;
import com.oplus.font.IOplusFontManager;
import com.oplus.font.OplusFontManager;
import com.oplus.hiddenapi.IOplusHiddenApiManager;
import com.oplus.hiddenapi.OplusHiddenApiManager;
import com.oplus.media.IOplusZenModeFeature;
import com.oplus.media.OplusZenModeFeature;
import com.oplus.multiapp.IOplusMultiApp;
import com.oplus.multiapp.OplusMultiAppImpl;
import com.oplus.multiuser.IOplusMultiUserStatisticsManager;
import com.oplus.multiuser.OplusMultiUserStatisticsManager;
import com.oplus.nec.IOplusNecManager;
import com.oplus.nec.OplusNecManager;
import com.oplus.orms.IOplusResourceManager;
import com.oplus.orms.OplusResourceManager;
import com.oplus.performance.IOplusPerformanceManager;
import com.oplus.performance.OplusPerformanceManager;
import com.oplus.permission.IOplusPermissionCheckInjector;
import com.oplus.permission.OplusPermissionCheckInjector;
import com.oplus.preloadsplash.IOplusPreLoadSplashManager;
import com.oplus.preloadsplash.OplusPreLoadSplashManager;
import com.oplus.screenmode.IOplusAutoResolutionFeature;
import com.oplus.screenmode.OplusAutoResolutionFeature;
import com.oplus.screenshot.IOplusScreenShotEuclidManager;
import com.oplus.screenshot.OplusScreenShotEuclidManager;
import com.oplus.theme.IOplusThemeStyle;
import com.oplus.theme.OplusThemeStyle;
import com.oplus.uifirst.IOplusUIFirstManager;
import com.oplus.uifirst.OplusUIFirstManager;
import com.oplus.view.IJankManager;
import com.oplus.view.JankManager;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class OplusFrameworkFactoryImpl extends OplusFrameworkFactory {
    private static final boolean DEBUG = false;
    private static final String TAG = "oplus.android.OplusFrameworkFactoryImpl";

    public <T extends IOplusCommonFeature> T getFeature(T t, Object... objArr) {
        verityParams(t);
        if (!OplusFeatureManager.isSupport(t)) {
            return t;
        }
        switch (AnonymousClass1.$SwitchMap$android$common$OplusFeatureList$OplusIndex[t.index().ordinal()]) {
            case 1:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusHiddenApiManager(objArr));
            case 2:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusMultiApp());
            case 3:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusThemeManager());
            case 4:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusAccidentallyTouchHelper());
            case 5:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusDirectViewHelper(objArr));
            case 6:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusViewHooks(objArr));
            case 7:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusCommonInjector());
            case 8:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusCameraUtils(objArr));
            case 9:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusCameraStatisticsManager(objArr));
            case 10:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusCamera2StatisticsManager(objArr));
            case 11:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusCameraManager(objArr));
            case 12:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusViewRootUtil(objArr));
            case 13:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusFontManager(objArr));
            case 14:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusOverScrollerHelper(objArr));
            case 15:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusScrollOptimizationHelper());
            case 16:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusFavoriteManager());
            case 17:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusScreenShotEuclidManager());
            case 18:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusDarkModeManager());
            case 19:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusInputMethodServiceUtils());
            case 20:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusResolverManager());
            case 21:
                return (T) OplusFeatureManager.getTraceMonitor(getTextJustificationHooks());
            case 22:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusGradientHooks());
            case 23:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusFloatingToolbarUtil());
            case 24:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusDeepThinkerManager(objArr));
            case 25:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusBurmeseZgFlagHooks());
            case 26:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusThemeStyle(objArr));
            case 27:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusAppDynamicFeatureManager());
            case 28:
                return (T) OplusFeatureManager.getTraceMonitor(getDragTextShadowHelper());
            case 29:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusResolverStyle());
            case 30:
                Log.i(TAG, "get feature:" + t.index().name());
                return (T) OplusFeatureManager.getTraceMonitor(getOplusAutoResolutionFeature(objArr));
            case 31:
                Log.i(TAG, "get feature:" + t.index().name());
                return (T) OplusFeatureManager.getTraceMonitor(getOplusDynamicVsyncFeature());
            case 32:
                Log.i(TAG, "get feature:" + t.index().name());
                return (T) OplusFeatureManager.getTraceMonitor(getOplusZenModeFeature(objArr));
            case 33:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusPreLoadSplashManager(objArr));
            case 34:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusUIFirstManager(objArr));
            case 35:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusResourceManager(objArr));
            case 36:
                return (T) OplusFeatureManager.getTraceMonitor(getOAppNetControlManager(objArr));
            case 37:
                return (T) OplusFeatureManager.getTraceMonitor(getJankManager(objArr));
            case 38:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusDailyBattProtoManager(objArr));
            case 39:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusPermissionCheckInjector());
            case 40:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusMultiUserStatisticsManager());
            case 41:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusPerformanceManager(objArr));
            case 42:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusEnterpriseAndOperatorFeature());
            case 43:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusCustomizeTextViewFeature(objArr));
            case 44:
                return (T) OplusFeatureManager.getTraceMonitor(getUxIconPackageManagerExt(objArr));
            case 45:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusScrollToTopManager(objArr));
            case 46:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusCursorFeedbackManager(objArr));
            case 47:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusReorderActionMenuManager(objArr));
            case 48:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusAutoLayoutManager(objArr));
            case 49:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusRGBNormalizeManager(objArr));
            case 50:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusViewDebugManager(objArr));
            case 51:
                return (T) OplusFeatureManager.getTraceMonitor(getViewExtractManager(objArr));
            case 52:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusNecManagerImpl(objArr));
            case 53:
                return (T) OplusFeatureManager.getTraceMonitor(getCompactWindowAppManager());
            case 54:
                return (T) OplusFeatureManager.getTraceMonitor(getOplusAppHeapManager(objArr));
            default:
                Log.i(TAG, "Unknow feature:" + t.index().name());
                return t;
        }
    }

    /* renamed from: oplus.android.OplusFrameworkFactoryImpl$1, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$common$OplusFeatureList$OplusIndex;

        static {
            int[] iArr = new int[OplusFeatureList.OplusIndex.values().length];
            $SwitchMap$android$common$OplusFeatureList$OplusIndex = iArr;
            try {
                iArr[OplusFeatureList.OplusIndex.IOplusHiddenApiManagerExt.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusMultiApp.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusThemeManager.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusAccidentallyTouchHelper.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusDirectViewHelper.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusViewHooks.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusCommonInjector.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusCameraUtils.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusCameraStatisticsManager.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusCamera2StatisticsManager.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusCameraManager.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusViewRootUtil.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusFontManager.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusOverScrollerHelper.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusScrollOptimizationHelper.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusFavoriteManager.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusScreenShotEuclidManager.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusDarkModeManager.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusInputMethodServiceUtils.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusResolverManager.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.ITextJustificationHooks.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusGradientHooks.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusFloatingToolbarUtil.ordinal()] = 23;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusDeepThinkerManager.ordinal()] = 24;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusBurmeseZgHooks.ordinal()] = 25;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusThemeStyle.ordinal()] = 26;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusAppDynamicFeatureManager.ordinal()] = 27;
            } catch (NoSuchFieldError e27) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusDragTextShadowHelper.ordinal()] = 28;
            } catch (NoSuchFieldError e28) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusResolverStyle.ordinal()] = 29;
            } catch (NoSuchFieldError e29) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusAutoResolutionFeature.ordinal()] = 30;
            } catch (NoSuchFieldError e30) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusDynamicVsyncFeature.ordinal()] = 31;
            } catch (NoSuchFieldError e31) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusZenModeFeature.ordinal()] = 32;
            } catch (NoSuchFieldError e32) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusPreLoadSplashManager.ordinal()] = 33;
            } catch (NoSuchFieldError e33) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusUIFirstManager.ordinal()] = 34;
            } catch (NoSuchFieldError e34) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusResourceManager.ordinal()] = 35;
            } catch (NoSuchFieldError e35) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOAppNetControlManager.ordinal()] = 36;
            } catch (NoSuchFieldError e36) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IJankManager.ordinal()] = 37;
            } catch (NoSuchFieldError e37) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusDailyBattProtoManager.ordinal()] = 38;
            } catch (NoSuchFieldError e38) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusPermissionCheckInjector.ordinal()] = 39;
            } catch (NoSuchFieldError e39) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusMultiUserStatisticsManager.ordinal()] = 40;
            } catch (NoSuchFieldError e40) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusPerformanceManager.ordinal()] = 41;
            } catch (NoSuchFieldError e41) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusEnterpriseAndOperatorFeature.ordinal()] = 42;
            } catch (NoSuchFieldError e42) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusCustomizeTextViewFeature.ordinal()] = 43;
            } catch (NoSuchFieldError e43) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IUxIconPackageManagerExt.ordinal()] = 44;
            } catch (NoSuchFieldError e44) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusScrollToTopManager.ordinal()] = 45;
            } catch (NoSuchFieldError e45) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusCursorFeedbackManager.ordinal()] = 46;
            } catch (NoSuchFieldError e46) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusReorderActionMenuManager.ordinal()] = 47;
            } catch (NoSuchFieldError e47) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusAutoLayoutManager.ordinal()] = 48;
            } catch (NoSuchFieldError e48) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusRGBNormalizeManager.ordinal()] = 49;
            } catch (NoSuchFieldError e49) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusViewDebugManager.ordinal()] = 50;
            } catch (NoSuchFieldError e50) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusViewExtractManager.ordinal()] = 51;
            } catch (NoSuchFieldError e51) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusNecManager.ordinal()] = 52;
            } catch (NoSuchFieldError e52) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusCompactWindowAppManager.ordinal()] = 53;
            } catch (NoSuchFieldError e53) {
            }
            try {
                $SwitchMap$android$common$OplusFeatureList$OplusIndex[OplusFeatureList.OplusIndex.IOplusAppHeapManager.ordinal()] = 54;
            } catch (NoSuchFieldError e54) {
            }
        }
    }

    private IOplusViewRootUtil getOplusViewRootUtil(Object... vars) {
        return OplusViewRootUtil.getInstance();
    }

    private IOplusFontManager getOplusFontManager(Object... vars) {
        return OplusFontManager.getInstance();
    }

    private IOplusDirectViewHelper getOplusDirectViewHelper(Object... vars) {
        WeakReference<ViewRootImpl> svri = (WeakReference) vars[0];
        return new OplusDirectViewHelper(svri);
    }

    private IOplusViewHooks getOplusViewHooks(Object... vars) {
        verityParamsType("getOplusViewHooks", vars, 2, new Class[]{View.class, Resources.class});
        View view = (View) vars[0];
        Resources res = (Resources) vars[1];
        return new OplusViewHooksImp(view, res);
    }

    private IOplusOverScrollerHelper getOplusOverScrollerHelper(Object... vars) {
        verityParamsType("getOplusOverScrollerHelper", vars, 1, new Class[]{OverScroller.class});
        OverScroller overScroller = (OverScroller) vars[0];
        return new OplusOverScrollerHelper(overScroller);
    }

    private IOplusScrollOptimizationHelper getOplusScrollOptimizationHelper() {
        return new OplusScrollOptimizationHelper();
    }

    private IOplusDeepThinkerManager getOplusDeepThinkerManager(Object... vars) {
        Context context = (Context) vars[0];
        return OplusDeepThinkerManagerDecor.getInstance(context);
    }

    private IOplusFavoriteManager getOplusFavoriteManager() {
        return OplusFavoriteManager.getInstance();
    }

    private IOplusDarkModeManager getOplusDarkModeManager() {
        return OplusDarkModeManager.getInstance();
    }

    private IOplusCommonInjector getOplusCommonInjector() {
        return OplusCommonInjector.getInstance();
    }

    private IOplusScreenShotEuclidManager getOplusScreenShotEuclidManager() {
        return OplusScreenShotEuclidManager.getInstance();
    }

    private IOplusInputMethodServiceUtils getOplusInputMethodServiceUtils() {
        return OplusInputMethodServiceUtils.getInstance();
    }

    private IOplusResolverManager getOplusResolverManager() {
        return new OplusResolverManager();
    }

    private IOplusResolverStyle getOplusResolverStyle() {
        return new OplusResolverStyle();
    }

    private IOplusThemeManager getOplusThemeManager() {
        return OplusThemeManager.getInstance();
    }

    private IOplusAccidentallyTouchHelper getOplusAccidentallyTouchHelper() {
        return OplusAccidentallyTouchHelper.getInstance();
    }

    private ITextJustificationHooks getTextJustificationHooks() {
        return new TextJustificationHooksImpl();
    }

    private IOplusGradientHooks getOplusGradientHooks() {
        return new OplusGradientHooksImpl();
    }

    private IOplusBurmeseZgHooks getOplusBurmeseZgFlagHooks() {
        return new OplusBurmeseZgFlagHooksImpl();
    }

    public IOplusMultiApp getOplusMultiApp() {
        return new OplusMultiAppImpl();
    }

    private IOplusThemeStyle getOplusThemeStyle(Object... vars) {
        return new OplusThemeStyle();
    }

    private IOplusAppDynamicFeatureManager getOplusAppDynamicFeatureManager() {
        return OplusAppDynamicFeatureManager.getInstance();
    }

    private IOplusFloatingToolbarUtil getOplusFloatingToolbarUtil() {
        return new OplusFloatingToolbarUtil();
    }

    private IOplusPreLoadSplashManager getOplusPreLoadSplashManager(Object... vars) {
        return OplusPreLoadSplashManager.getInstance();
    }

    private IOplusDragTextShadowHelper getDragTextShadowHelper() {
        return OplusDragTextShadowHelper.getInstance();
    }

    private IOplusCameraUtils getOplusCameraUtils(Object... vars) {
        Log.i(TAG, "getOplusCameraUtils");
        return OplusCameraUtils.getInstance();
    }

    private IOplusCameraStatisticsManager getOplusCameraStatisticsManager(Object... vars) {
        Log.i(TAG, "getOplusCameraStatisticsManager");
        return OplusCameraStatisticsManager.getInstance();
    }

    private IOplusCamera2StatisticsManager getOplusCamera2StatisticsManager(Object... vars) {
        Log.i(TAG, "getOplusCamera2StatisticsManager");
        return OplusCamera2StatisticsManager.getInstance();
    }

    private IOplusCameraManager getOplusCameraManager(Object... vars) {
        Log.i(TAG, "getOplusCameraManager");
        return OplusCameraManager.getInstance();
    }

    private IOplusAutoResolutionFeature getOplusAutoResolutionFeature(Object... vars) {
        Log.i(TAG, "getOplusAutoResolutionFeature");
        return OplusAutoResolutionFeature.getInstance();
    }

    private IOplusDynamicVsyncFeature getOplusDynamicVsyncFeature() {
        return OplusDynamicVsyncFeature.getInstance();
    }

    private IOplusZenModeFeature getOplusZenModeFeature(Object... vars) {
        return new OplusZenModeFeature();
    }

    private IOplusUIFirstManager getOplusUIFirstManager(Object... vars) {
        return OplusUIFirstManager.getInstance();
    }

    private IOplusResourceManager getOplusResourceManager(Object... vars) {
        Class clazz = (Class) vars[0];
        return OplusResourceManager.getInstance(clazz);
    }

    private IOAppNetControlManager getOAppNetControlManager(Object... vars) {
        return OAppNetControlManager.getInstance();
    }

    private IJankManager getJankManager(Object... vars) {
        return JankManager.getInstance();
    }

    private IOplusDailyBattProtoManager getOplusDailyBattProtoManager(Object... vars) {
        BatteryStats batteryStats = (BatteryStats) vars[0];
        return OplusDailyBattProtoManager.getInstance(batteryStats);
    }

    private IOplusPermissionCheckInjector getOplusPermissionCheckInjector() {
        return OplusPermissionCheckInjector.getInstance();
    }

    private IOplusPerformanceManager getOplusPerformanceManager(Object... vars) {
        return OplusPerformanceManager.getInstance();
    }

    private IOplusEnterpriseAndOperatorFeature getOplusEnterpriseAndOperatorFeature() {
        return new OplusEnterpriseAndOperatorFeature();
    }

    private IOplusMultiUserStatisticsManager getOplusMultiUserStatisticsManager() {
        return OplusMultiUserStatisticsManager.getInstance();
    }

    private IOplusCustomizeTextViewFeature getOplusCustomizeTextViewFeature(Object... vars) {
        return OplusCustomizeTextViewFeature.getInstance();
    }

    private IUxIconPackageManagerExt getUxIconPackageManagerExt(Object... vars) {
        ApplicationPackageManager packageManager = (ApplicationPackageManager) vars[0];
        Context context = (Context) vars[1];
        return new UxIconPackageManagerExt(packageManager, context);
    }

    private IOplusScrollToTopManager getOplusScrollToTopManager(Object... vars) {
        return new OplusScrollToTopManager();
    }

    private IOplusCursorFeedbackManager getOplusCursorFeedbackManager(Object... vars) {
        return new OplusCursorFeedbackManager();
    }

    private IOplusReorderActionMenuManager getOplusReorderActionMenuManager(Object... vars) {
        return new OplusReorderActionMenuManager();
    }

    private IOplusAutoLayoutManager getOplusAutoLayoutManager(Object... vars) {
        return new OplusAutoLayoutManager();
    }

    private IOplusViewExtractManager getViewExtractManager(Object... vars) {
        return new OplusViewExtractManager();
    }

    private IOplusRGBNormalizeManager getOplusRGBNormalizeManager(Object... vars) {
        return new OplusRGBNormalizeManager();
    }

    private IOplusViewDebugManager getOplusViewDebugManager(Object... vars) {
        return new OplusViewDebugManager();
    }

    private IOplusNecManager getOplusNecManagerImpl(Object... vars) {
        Context context = (Context) vars[0];
        return OplusNecManager.getInstance(context);
    }

    private IOplusHiddenApiManager getOplusHiddenApiManager(Object... vars) {
        Log.i(OplusHiddenApiManager.TAG, "get IOplusHiddenApiManagerExt feature:");
        verityParamsType("getOplusHiddenApiManager", vars, 0, new Class[0]);
        return OplusHiddenApiManager.getInstance();
    }

    public IOplusCompactWindowAppManager getCompactWindowAppManager() {
        return OplusCompactWindowAppManager.getInstance();
    }

    private IOplusAppHeapManager getOplusAppHeapManager(Object... vars) {
        return OplusAppHeapManager.getInstance();
    }
}
