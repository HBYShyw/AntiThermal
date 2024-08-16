package android.widget;

import android.common.OplusFeatureCache;
import android.common.OplusFrameworkFactory;
import android.content.Context;
import android.os.OplusPropertyList;
import android.os.OplusSystemProperties;
import android.os.Process;
import android.util.Log;
import android.view.IOplusViewConfigHelper;
import android.view.IViewExt;
import android.view.debug.IOplusViewDebugManager;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.uifirst.IOplusUIFirstManager;
import com.oplus.util.OplusContextUtil;

/* loaded from: classes.dex */
public class AbsListViewExtImpl implements IAbsListviewExt {
    static final int EXCEPTION_NUM = 100;
    static final int EXCEPTION_TIME_GAP = 50;
    private static final String TAG = "AbsListViewExt";
    private AbsListView mBase;
    private int mColorOverDist;
    private boolean mIsColorStyle;
    private IOplusViewConfigHelper mOplusViewConfigHelper;
    private IViewExt viewExt;
    static boolean isEnableEndFlingProtect = false;
    static long lastEndFlingTime = 0;
    static long constantEndFlingNum = 0;
    static final int[] LONG_FORMAT = {8224};
    private Context mContext = null;
    protected float mFlingFriction = 1.06f;
    protected float mRealmeFlingFriction = 0.008f;
    private boolean mTopThirdPartApp = false;

    public int getScaledOverscrollDistance(int dist) {
        if (this.mIsColorStyle) {
            Log.d("TestOverScroll", "getScaledOverscrollDistance: a mColorOverDist: " + this.mColorOverDist);
            return this.mColorOverDist;
        }
        Log.d("TestOverScroll", "getScaledOverscrollDistance: b");
        return dist;
    }

    public int getScaledOverflingDistance(int dist) {
        if (this.mIsColorStyle) {
            return this.mColorOverDist;
        }
        return dist;
    }

    public int calcRealOverScrollDist(int dist, int scrollY) {
        if (this.mIsColorStyle) {
            return (int) ((dist * (1.0f - ((Math.abs(scrollY) * 1.0f) / this.mColorOverDist))) / 3.0f);
        }
        return dist;
    }

    public int calcRealOverScrollDist(int dist, int scrollY, int range) {
        if (this.mIsColorStyle && (scrollY < 0 || scrollY > range)) {
            int overScrollY = scrollY;
            if (scrollY > range) {
                overScrollY = scrollY - range;
            }
            Log.d("TestOverScroll", "calcRealOverScrollDist: b-scrollY: " + scrollY);
            return (int) ((dist * (1.0f - ((Math.abs(overScrollY) * 1.0f) / this.mColorOverDist))) / 3.0f);
        }
        return dist;
    }

    public void initViewConfigHelper(Context context, boolean optHelperEnable, boolean forceUsingSpring) {
        IOplusViewConfigHelper iOplusViewConfigHelper = (IOplusViewConfigHelper) OplusFeatureCache.getOrCreate(IOplusViewConfigHelper.DEFAULT, new Object[]{context});
        this.mOplusViewConfigHelper = iOplusViewConfigHelper;
        iOplusViewConfigHelper.setOptEnable(optHelperEnable);
        this.mOplusViewConfigHelper.setForceUsingSpring(forceUsingSpring);
    }

    public int getOverScrollDistance(int dist) {
        return this.mOplusViewConfigHelper.getScaledOverscrollDistance(dist);
    }

    public int getOverFlingDistance(int dist) {
        return this.mOplusViewConfigHelper.getScaledOverflingDistance(dist);
    }

    public boolean getOptHelperEnable() {
        return ((IOplusScrollOptimizationHelper) OplusFrameworkFactory.getInstance().getFeature(IOplusScrollOptimizationHelper.DEFAULT, new Object[0])).enable();
    }

    public AbsListViewExtImpl(Object base) {
        this.mBase = (AbsListView) base;
    }

    public void init(Context context, AbsListView absListView) {
        this.mContext = context;
        this.mColorOverDist = context.getResources().getDisplayMetrics().heightPixels;
        this.mIsColorStyle = OplusContextUtil.isOplusOSStyle(context);
        this.viewExt = absListView.getViewWrapper().getViewExt();
        absListView.mOverscrollDistance = getScaledOverscrollDistance(absListView.mOverscrollDistance);
        absListView.mOverflingDistance = getScaledOverflingDistance(absListView.mOverflingDistance);
    }

    Context getContext() {
        return this.mContext;
    }

    public boolean enableEndFlingProtectIfNeeded() {
        String packageName = getContext().getPackageName();
        if (packageName.equals("com.tencent.mm") || packageName.equals("gavin.example.abslistviewtest")) {
            isEnableEndFlingProtect = true;
            return true;
        }
        return false;
    }

    public void execEndFlingProtectIfNeeded() {
        if (isEnableEndFlingProtect) {
            long curTime = System.currentTimeMillis();
            if (curTime - lastEndFlingTime < 50) {
                long j = constantEndFlingNum + 1;
                constantEndFlingNum = j;
                if (j >= 100) {
                    long[] oom_adj = new long[1];
                    int pid = Process.myPid();
                    Process.readProcFile("/proc/" + pid + "/oom_adj", LONG_FORMAT, null, oom_adj, null);
                    if (oom_adj[0] > 1) {
                        Log.d(TAG, "pid=" + pid + " killed");
                        Process.sendSignal(pid, 9);
                    } else {
                        Log.d(TAG, "waiting pid=" + pid + " to be background");
                    }
                }
            } else {
                constantEndFlingNum = 0L;
            }
            lastEndFlingTime = curTime;
        }
    }

    public OverScroller getOverScroller(Context mContext) {
        IViewExt iViewExt = this.viewExt;
        if (iViewExt != null && iViewExt.isOplusOSStyle()) {
            SpringOverScroller s = new SpringOverScroller(mContext);
            s.setFlingFriction(this.mFlingFriction);
            if (OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_LIST_OPTIMIZE) && OplusSystemProperties.getBoolean(OplusPropertyList.PROPETY_LIST_OPTIMIZE_ENABLE, false)) {
                s.setFlingFriction(this.mRealmeFlingFriction);
                return s;
            }
            return s;
        }
        OverScroller overScroller = new OverScroller(mContext);
        return overScroller;
    }

    public void setFlingFriction(float f) {
        this.mFlingFriction = f;
    }

    public void setFlingMode(int mode) {
        switch (mode) {
            case 0:
                setFlingFriction(0.76f);
                return;
            case 1:
                return;
            default:
                throw new IllegalArgumentException("wrong fling argument");
        }
    }

    public int getTouchMode() {
        return this.mBase.mTouchMode;
    }

    public void setTouchMode(int mode) {
        this.mBase.mTouchMode = mode;
    }

    public void startSpringback() {
        if (this.mBase.getWrapper().getFlingRunnable() == null) {
            this.mBase.getWrapper().setFlingRunnable();
        }
        this.mBase.getWrapper().startSpringback();
    }

    public boolean isSystemStyle() {
        if (this.mTopThirdPartApp) {
            return false;
        }
        return this.viewExt.isOplusOSStyle();
    }

    public boolean isSupportedStyle() {
        if (this.mTopThirdPartApp) {
            return false;
        }
        return this.viewExt.isOplusStyle();
    }

    public FastScroller getFastScroller(AbsListView absListView, int style) {
        IViewExt viewExtForFastScroller = absListView.getViewWrapper().getViewExt();
        if (viewExtForFastScroller.isOplusOSStyle()) {
            return new OplusFastScroller(absListView, style);
        }
        return new FastScroller(absListView, style);
    }

    public void obtainViewHook() {
        ((IOplusUIFirstManager) OplusFeatureCache.getOrCreate(IOplusUIFirstManager.DEFAULT, new Object[0])).ofbBoostHint(Process.myPid(), 0, 0, 0, 203, 0, 0L, 0L, 0L);
    }

    public void markBeforeScroll(int y, int lastY, int touchMode, int rawDeltaY) {
        getViewDebugManager().markBeforeScroll(y, lastY, touchMode, rawDeltaY);
    }

    public void markAfterScroll() {
        getViewDebugManager().markAfterScroll();
    }

    public IOplusViewDebugManager getViewDebugManager() {
        return (IOplusViewDebugManager) OplusFeatureCache.getOrCreate(IOplusViewDebugManager.mDefault, new Object[0]);
    }
}
