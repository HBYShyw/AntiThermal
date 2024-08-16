package android.view;

import android.app.OplusActivityThreadExtImpl;
import android.app.WindowConfiguration;
import android.graphics.Rect;
import android.os.Process;
import android.os.RemoteException;
import android.os.Trace;
import android.util.Log;
import android.util.SparseArray;
import android.view.IInsetsControllerExt;
import android.view.InsetsController;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.view.inputmethod.InputMethodManager;
import com.oplus.android.internal.util.OplusFrameworkStatsLog;
import com.oplus.content.OplusFeatureConfigManager;

/* loaded from: classes.dex */
public class InsetsControllerExtImpl implements IInsetsControllerExt {
    private static final String FEATURE_FOLD = "oplus.hardware.type.fold";
    private static final String FEATURE_FOLD_REMAP_DISPLAY_DISABLED = "oplus.software.fold_remap_display_disabled";
    private static final int ID_EXTRA_NAVIGATION_BAR = InsetsSource.createId((Object) null, 1, WindowInsets.Type.navigationBars());
    private static final int STATUS_BAR_HIDE = 0;
    private static final int STATUS_BAR_VISIBLE = 1;

    /* loaded from: classes.dex */
    public static class StaticExtImpl implements IInsetsControllerExt.IStaticExt {
        private static final int ANIMATION_DURATION_UNSYNC_IME_MS = 200;
        private static final int ANIMATION_DURATION_UNSYNC_IME_MS_OPLUS = 350;
        private static final long BASE_IME_INTERVAL = 13000000;
        private static final int BASE_MULTIPLE = 3;
        private static final int BASE_MULTIPLE_UNIT = 1000000;
        private static final int IME_THRESHOLD_LOCAL = 10;
        private static final Interpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR_OPLUS = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.0f);
        private static final int PLUS_MULTIPLE = 4;
        private static final int TWO_MULTIPLE = 2;
        private static OplusWindowManager sOplusWindowManager;
        private long mImeAnimEndTimeStamp;
        private int mImeAnimJankCount;
        private int mImeAnimJankCountLocal;
        private long mImeAnimStartTimeStamp;
        private boolean mImeAnimStarted;
        private long mImeJankInterval;
        private long mInterval;
        private long mLastImeAnimTimeStamp;

        private StaticExtImpl() {
            this.mImeAnimStarted = false;
            this.mImeAnimJankCount = 0;
            this.mImeAnimJankCountLocal = 0;
            this.mLastImeAnimTimeStamp = 0L;
        }

        public static StaticExtImpl getInstance(Object obj) {
            return LazyHolder.INSTANCE;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class LazyHolder {
            private static final StaticExtImpl INSTANCE = new StaticExtImpl();

            private LazyHolder() {
            }
        }

        public Interpolator replaceIMEInterpolator(Interpolator ip) {
            return ip;
        }

        public long replaceIMEDurationMs(boolean show, int time) {
            return show ? 350L : 200L;
        }

        public boolean setInsetAnimationTid(int type) {
            if (sOplusWindowManager == null) {
                sOplusWindowManager = new OplusWindowManager();
            }
            try {
                boolean enable = (WindowInsets.Type.ime() & type) != 0;
                sOplusWindowManager.setInsetAnimationTid(Process.myPid(), Process.myTid(), enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return true;
        }

        public void imeAnimatorStart(int requestedTypes, int types, long duration) {
            if (requestedTypes == types && requestedTypes == 8) {
                this.mImeAnimStarted = true;
                this.mImeAnimJankCount = 0;
                long nanoTime = System.nanoTime();
                this.mLastImeAnimTimeStamp = nanoTime;
                this.mImeAnimStartTimeStamp = nanoTime;
                long interval = Choreographer.getInstance().getFrameIntervalNanos();
                long interval2 = interval > 0 ? interval : 13000000L;
                this.mInterval = interval2;
                this.mImeAnimJankCountLocal = 0;
                long j = (interval2 >= BASE_IME_INTERVAL ? 3L : 4L) * interval2;
                this.mImeJankInterval = j;
                long j2 = this.mLastImeAnimTimeStamp;
                this.mImeAnimStartTimeStamp = j2 + j;
                this.mImeAnimEndTimeStamp = (j2 + (1000000 * duration)) - j;
            }
        }

        public void imeAnimatorUpdate(int requestedTypes, int types) {
            if (this.mImeAnimStarted && requestedTypes == types && requestedTypes == 8 && this.mLastImeAnimTimeStamp != 0) {
                long currentTime = System.nanoTime();
                long imeSkipTime = currentTime - this.mLastImeAnimTimeStamp;
                this.mLastImeAnimTimeStamp = currentTime;
                if (currentTime < this.mImeAnimStartTimeStamp || currentTime >= this.mImeAnimEndTimeStamp) {
                    return;
                }
                if (imeSkipTime > this.mInterval * 2) {
                    this.mImeAnimJankCountLocal++;
                }
                if (imeSkipTime > this.mImeJankInterval) {
                    this.mImeAnimJankCount++;
                    Trace.traceCounter(8L, "ime_anim_jank", 1);
                }
            }
        }

        public long imeAnimatorFinished(int requestedTypes, long preJankCount) {
            if (requestedTypes != 8) {
                return preJankCount;
            }
            this.mImeAnimStarted = false;
            this.mLastImeAnimTimeStamp = 0L;
            long preCount = preJankCount + 1;
            InputMethodManager imm = InputMethodManager.getInstanceForDisplay(0);
            String curId = imm.getWrapper().getCurId();
            if (this.mImeAnimJankCountLocal > 10 || this.mImeAnimJankCount > 0) {
                if (curId == null) {
                    Log.i("QualityReport", "pkgName:null,ime_anim_jank,flag:1");
                }
                Log.p("Quality", "local:ime_anim_jank,pkgName:" + curId + ",flag:2,jankCount:" + this.mImeAnimJankCount + ",jankCountLocal:" + this.mImeAnimJankCountLocal + ",preCount:" + preCount);
                this.mImeAnimJankCountLocal = 0;
            }
            if (this.mImeAnimJankCount > 0) {
                Log.p("Quality", "ime_anim_jank:" + this.mImeAnimJankCount + "," + this.mImeJankInterval + "," + preCount);
                OplusFrameworkStatsLog.write(100025, System.currentTimeMillis(), "ime_anim_jank", OplusActivityThreadExtImpl.getPid(), Process.myTid(), this.mImeAnimJankCount, this.mImeJankInterval, preCount);
                this.mImeAnimJankCount = 0;
                return 0L;
            }
            return preCount;
        }

        public void imeAnimatorCancelled(int requestedTypes) {
            if (requestedTypes == 8) {
                this.mImeAnimStarted = false;
                this.mLastImeAnimTimeStamp = 0L;
            }
        }
    }

    public InsetsControllerExtImpl(Object base) {
    }

    public boolean isIgnoreTaskBarAnim(int types, SparseArray<InsetsSourceConsumer> sourceConsumers) {
        if (OplusFeatureConfigManager.getInstance().hasFeature("oplus.hardware.type.fold") && !OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.fold_remap_display_disabled") && (WindowInsets.Type.navigationBars() & types) != 0) {
            InsetsSourceConsumer consumer = sourceConsumers.get(ID_EXTRA_NAVIGATION_BAR);
            InsetsSourceControl taskBarControl = consumer != null ? consumer.getControl() : null;
            if (taskBarControl != null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isIgnoreStatusBarAnim(boolean shown, int lastMRequestedVisibleTypes, int curMRequestedVisibleTypes, InsetsController.Host host) {
        boolean boundsFlag;
        boolean resizeVisibleFlag;
        boolean windowFlag;
        if (host == null || host.getRootViewContext() == null) {
            return false;
        }
        WindowConfiguration windowConfiguration = host.getRootViewContext().getResources().getConfiguration().windowConfiguration;
        int windowingMode = windowConfiguration.getWindowingMode();
        Rect appBounds = windowConfiguration.getAppBounds();
        Rect bounds = windowConfiguration.getBounds();
        if (appBounds == null || !appBounds.equals(bounds)) {
            boundsFlag = false;
        } else {
            boundsFlag = true;
        }
        if (!shown || (WindowInsets.Type.statusBars() & lastMRequestedVisibleTypes) != 0 || (WindowInsets.Type.statusBars() & curMRequestedVisibleTypes) != 1) {
            resizeVisibleFlag = false;
        } else {
            resizeVisibleFlag = true;
        }
        if (windowingMode != 100) {
            windowFlag = false;
        } else {
            windowFlag = true;
        }
        if (!boundsFlag || !resizeVisibleFlag || !windowFlag) {
            return false;
        }
        return true;
    }
}
