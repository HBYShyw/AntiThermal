package android.view.performance;

import android.app.OplusActivityManager;
import android.content.Context;
import android.content.pm.OplusPackageManager;
import android.graphics.Paint;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.BoostFramework;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.oplus.zoomwindow.OplusZoomWindowManager;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class OplusViewPerfInjector implements IOplusViewPerfInjector, IOplusAdjustlayerType {
    private static final String BOOST_VIEW_BILIBILI = "com.bilibili.lib.homepage.widget.TabHost";
    private static final String BOOST_VIEW_CLASS = "ConversationOverscrollListView";
    private static final String BOOST_VIEW_IQIYI = "org.qiyi.basecore.widget";
    private static final String BOOST_VIEW_REDPACKET = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
    private static final String BOOST_VIEW_WEIXIN_TAB = "com.tencent.mm.ui.LauncherUIBottomTabView";
    private static final String CLASS_NAME_SPECIAL_VIEW_01 = "com.tencent.mm.ui.base.MultiTouchImageView";
    private static final String MTK_PERF_CLASS = "com.mediatek.powerhalmgr.PowerHalMgrImpl";
    private static final float SCALE_CURRENT_DURATION = 0.8f;
    private static final int WEIXIN_TAB_BOOST_DURATION = 30;
    private static Method sPerfLockAcquire;
    private static Object sPerfObject;
    private Context mContext;
    private Paint mLayerPaint;
    private BoostFramework mPerf;
    private OplusPackageManager mPm;
    private final View mView;
    private static final int[] MTK_PERF_LIST = {21037056, 1, OplusZoomWindowManager.FLAG_TOUCH_OUTSIDE_CONTROL_VIEW, 3000000, 4194560, 3000000, 4194816, 3000000};
    private static final int[] QCOM_PERF_LIST = {1086324736, 1, 1086849024, Process.myPid(), 1082130432, 4095, 1082130688, 4095, 1082130944, 4095};
    private static final boolean WEIXIN_TAB_BOOST_ENABLED = SystemProperties.getBoolean("ro.oplus.boost_weixin_tab", false);
    private static boolean sTriedLoadMtkPerfClass = false;
    private static int mBoostTime = SystemProperties.getInt("persist.sys.weixin.launchuiboost", 300);
    private static String TAG = OplusViewPerfInjector.class.getSimpleName();
    private static boolean DEBUG = false;
    private static boolean CHANGE_LAYERTYPE_ENABLE = true;
    private boolean mIgnoreOnDescendantInvalidated = false;
    private boolean mIgnoreOnDescendantInvalidatedInViewGroup = false;
    private boolean mShouldBoostAnimation = false;
    private boolean mIsFinishBoost = false;
    private boolean mShouldCheckTouchBoost = false;
    private int mPendingLayerType = 0;
    private int mLayerType = 0;
    int mForceHWSize = SystemProperties.getInt("debug.imageview.forcehw.size", 16777216);

    public OplusViewPerfInjector(View view) {
        this.mView = view;
        this.mLayerPaint = view.getViewWrapper().getLayerPaint();
        this.mContext = view.getContext();
        this.mPm = OplusPackageManager.getOplusPackageManager(view.getContext());
    }

    @Override // android.view.performance.IOplusViewPerfInjector
    public IOplusAdjustlayerType getOplusAdjustlayerTypeInstance() {
        if (CHANGE_LAYERTYPE_ENABLE) {
            return this;
        }
        return IOplusAdjustlayerType.DEFAULT;
    }

    @Override // android.view.performance.IOplusViewPerfInjector
    public void initView() {
        View view;
        Context context = this.mContext;
        if (context != null && context.getPackageName() != null && this.mContext.getPackageName().equals("com.tencent.mm")) {
            this.mShouldCheckTouchBoost = true;
        }
        View view2 = this.mView;
        if (view2 != null) {
            String viewClassName = view2.getClass().getName();
            if (CLASS_NAME_SPECIAL_VIEW_01.equals(viewClassName)) {
                this.mIgnoreOnDescendantInvalidated = true;
            }
        }
        if (WEIXIN_TAB_BOOST_ENABLED && (view = this.mView) != null && view.getContext() != null && this.mView.getContext().getPackageName().equals("com.tencent.mm") && !sTriedLoadMtkPerfClass && (sPerfObject == null || sPerfLockAcquire == null)) {
            try {
                sTriedLoadMtkPerfClass = true;
                Class clazz = Class.forName(MTK_PERF_CLASS, false, null);
                sPerfObject = clazz.getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
                sPerfLockAcquire = clazz.getDeclaredMethod("perfLockAcquire", Integer.TYPE, Integer.TYPE, int[].class);
            } catch (Exception e) {
                Log.e(TAG, "Load MTK perf Class Exception:" + e);
            }
        }
        boolean z = DEBUG;
        if (z && this.mView != null && this.mContext != null) {
            if (z) {
                Log.d(TAG, "PackageName " + this.mContext.getPackageName() + " view " + this.mView.getClass().getName());
            }
            if (this.mView.getParent() == null || !DEBUG) {
                return;
            }
            Log.d(TAG, "mView.getParent() " + this.mView.getParent().getClass().getSimpleName());
        }
    }

    @Override // android.view.performance.IOplusAdjustlayerType
    public void adjustImageViewLayerType(int width, int height) {
        if ((this.mView instanceof ImageView) && width > 0 && height > 0) {
            adjustLayerTypeInternal(width, height);
        }
    }

    @Override // android.view.performance.IOplusAdjustlayerType
    public boolean checkMutiTouchView() {
        if (getClass().getSimpleName().equals("MultiTouchImageView")) {
            return true;
        }
        return false;
    }

    private void adjustLayerTypeInternal(int width, int height) {
        int layerType = this.mView.getLayerType();
        this.mLayerType = layerType;
        if (layerType == 1 || this.mPendingLayerType == 2) {
            if (width * height <= this.mForceHWSize) {
                this.mView.setLayerType(2, this.mLayerPaint);
                if (DEBUG) {
                    Log.d(TAG, "adjustLayerTypeInternal  to LAYER_TYPE_HARDWARE " + this.mView.getLayerType());
                    return;
                }
                return;
            }
            this.mPendingLayerType = 0;
            return;
        }
        if (layerType == 2 && width * height > this.mForceHWSize) {
            this.mView.setLayerType(1, this.mLayerPaint);
            if (DEBUG) {
                Log.d(TAG, "adjustLayerTypeInternal  to LAYER_TYPE_SOFTWARE " + this.mView.getLayerType());
            }
            this.mPendingLayerType = 0;
            this.mShouldBoostAnimation = true;
        }
    }

    @Override // android.view.performance.IOplusViewPerfInjector
    public void checkBoostAnimation(Animation animation) {
        if (this.mShouldBoostAnimation) {
            if (DEBUG) {
                Log.d(TAG, "Boost  webchat animation");
            }
            long duration = animation.getDuration();
            int timeout = duration > 1000 ? 1000 : (int) duration;
            try {
                OplusActivityManager.getInstance().setSceneActionTransit("", "OSENSE_ACTION_ANIMATION", timeout);
                return;
            } catch (RemoteException e) {
                Log.e(TAG, "setSceneActionTransit failed because remoteException");
                return;
            }
        }
        if (this.mView.getViewRootImpl() != null && this.mView.getViewRootImpl().getWrapper().getExtImpl().isFragmentAnimUI()) {
            if (DEBUG) {
                Log.d(TAG, "Boost FragmentUI animation");
            }
            try {
                OplusActivityManager.getInstance().setSceneActionTransit("", "OSENSE_ACTION_ANIMATION", mBoostTime);
            } catch (RemoteException e2) {
                Log.e(TAG, "setSceneActionTransit failed because remoteException");
            }
            if (!animation.mSpeeduped) {
                animation.scaleCurrentDuration(SCALE_CURRENT_DURATION);
                animation.mSpeeduped = true;
            }
        }
    }

    @Override // android.view.performance.IOplusViewPerfInjector
    public void checkBoostBuildDrawingCache() {
        if ("SnsCommentShowAbLayout".equals(this.mView.getClass().getSimpleName())) {
            if (DEBUG) {
                Log.d(TAG, "Boost  SnsCommentShowAbLayout");
            }
            try {
                OplusActivityManager.getInstance().setSceneActionTransit("", "OSENSE_ACTION_ANIMATION", 100);
                return;
            } catch (RemoteException e) {
                Log.e(TAG, "setSceneActionTransit failed because remoteException");
                return;
            }
        }
        if ("MultiTouchImageView".equals(getClass().getSimpleName())) {
            if (DEBUG) {
                Log.d(TAG, "Boost  MultiTouchImageView");
            }
            try {
                OplusActivityManager.getInstance().setSceneActionTransit("", "OSENSE_ACTION_ANIMATION", 30);
            } catch (RemoteException e2) {
                Log.e(TAG, "setSceneActionTransit failed because remoteException");
            }
        }
    }

    @Override // android.view.performance.IOplusViewPerfInjector
    public void checkBoostTouchEvent(int action) {
        if (this.mShouldCheckTouchBoost && action == 0 && this.mView.getParent() != null && this.mView.getParent().getClass().toString().contains(BOOST_VIEW_CLASS)) {
            if (DEBUG) {
                Log.d(TAG, "Boost  press enter webchat conversation.");
            }
            try {
                OplusActivityManager.getInstance().setSceneActionTransit("", "OSENSE_ACTION_TOUCH_BOOST", 500);
            } catch (RemoteException e) {
                Log.e(TAG, "setSceneActionTransit failed because remoteException");
            }
        }
    }

    @Override // android.view.performance.IOplusViewPerfInjector
    public void checkBoostOnPerformClick(View.OnClickListener onClickListener) {
        String liStr;
        String packageName = null;
        View view = this.mView;
        if (view != null && view.getContext() != null) {
            packageName = this.mView.getContext().getPackageName();
        }
        if (packageName != null && !packageName.isEmpty()) {
            if ((packageName.startsWith("com.qiyi.video") || packageName.startsWith("tv.danmaku.bili") || packageName.startsWith("com.tencent.mm")) && onClickListener != null && (liStr = onClickListener.toString()) != null && !liStr.isEmpty()) {
                if (liStr.startsWith(BOOST_VIEW_IQIYI) || liStr.startsWith(BOOST_VIEW_BILIBILI)) {
                    if (DEBUG) {
                        Log.i(TAG, "Boost view " + liStr);
                    }
                    try {
                        OplusActivityManager.getInstance().setSceneActionTransit("", "OSENSE_ACTION_ACTIVITY_START", mBoostTime);
                        return;
                    } catch (RemoteException e) {
                        Log.e(TAG, "setSceneActionTransit failed because remoteException");
                        return;
                    }
                }
                if (liStr.startsWith(BOOST_VIEW_REDPACKET)) {
                    if (DEBUG) {
                        Log.i(TAG, "Boost view " + liStr);
                    }
                    try {
                        OplusActivityManager.getInstance().setSceneActionTransit("", "OSENSE_ACTION_LUCKY_MONEY", 1000);
                        return;
                    } catch (RemoteException e2) {
                        Log.e(TAG, "setSceneActionTransit failed because remoteException");
                        return;
                    }
                }
                if (WEIXIN_TAB_BOOST_ENABLED && liStr.startsWith(BOOST_VIEW_WEIXIN_TAB)) {
                    boostWeiXinTab();
                }
            }
        }
    }

    private void boostWeiXinTab() {
        Method method = sPerfLockAcquire;
        if (method != null) {
            try {
                method.invoke(sPerfObject, 0, 30, MTK_PERF_LIST);
                return;
            } catch (Exception e) {
                Log.e(TAG, "perfLockAcquire error:" + e.toString());
                return;
            }
        }
        if (this.mPerf == null) {
            this.mPerf = new BoostFramework(this.mView.getContext());
        }
        this.mPerf.perfLockAcquire(30, QCOM_PERF_LIST);
    }

    @Override // android.view.performance.IOplusViewPerfInjector
    public void checkNeedBoostedPropertyAnimator(ViewPropertyAnimator animator) {
        if (this.mShouldBoostAnimation) {
            long duration = animator.getDuration();
            int timeout = duration > 1000 ? 1000 : (int) duration;
            if (DEBUG) {
                Log.i(TAG, "Boost animator " + animator);
            }
            try {
                OplusActivityManager.getInstance().setSceneActionTransit("", "OSENSE_ACTION_ANIMATION", timeout);
            } catch (RemoteException e) {
                Log.e(TAG, "setSceneActionTransit failed because remoteException");
            }
        }
    }

    @Override // android.view.performance.IOplusViewPerfInjector
    public void ignoreSpecailViewDescendantInvalidated(ViewParent p) {
        if (this.mIgnoreOnDescendantInvalidated && (p instanceof ViewGroup)) {
            ViewGroup parentViewGroup = (ViewGroup) p;
            if (parentViewGroup != null) {
                this.mIgnoreOnDescendantInvalidatedInViewGroup = true;
            }
        }
    }

    @Override // android.view.performance.IOplusViewPerfInjector
    public boolean isIgnoreSpecailViewDescendantInvalidated() {
        return this.mIgnoreOnDescendantInvalidatedInViewGroup;
    }
}
