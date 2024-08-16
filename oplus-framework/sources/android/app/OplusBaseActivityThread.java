package android.app;

import android.app.ActivityThread;
import android.appwidget.AppWidgetManager;
import android.appwidget.OplusBaseAppWidgetManager;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.Choreographer;
import android.view.OplusBaseWindowManagerGlobal;
import android.view.View;
import android.view.WindowManagerGlobal;
import com.oplus.util.OplusResolverIntentUtil;
import com.oplus.util.OplusTypeCastingHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public abstract class OplusBaseActivityThread extends ClientTransactionHandler {
    private static final long MAX_INTERVAL = 120000;
    private static final String TAG = "OplusBaseActivityThread";
    private static boolean sDebugSpecial = false;
    private static boolean sIsInitedSkipDoframeEnable = false;
    private static boolean sIsSkipDoframeEnable = false;
    private static boolean sLastRet = true;
    private static boolean sIsShouldDoCheck = true;
    private static final long MIN_INTERVAL = 10000;
    private static long sCurInterval = MIN_INTERVAL;
    private static long sLastCheckTime = -1;
    private static String sSpecialPkg = "def";
    public static boolean sStateChanged = false;
    public static boolean sIsWhitelisted = false;
    public static int sUidCurProcState = 2;
    public static Object sLock = new Object();
    public Choreographer sC = null;
    public Context mContext = null;
    AppWidgetManager mAppWidgetManager = null;
    OplusBaseAppWidgetManager mOplusBaseAppWidgetManager = null;
    OplusBaseWindowManagerGlobal mOplusBaseWindowManagerGlobal = null;
    INotificationManager mNotiService = null;
    final ArrayMap<IBinder, ActivityThread.ActivityClientRecord> mActivities = new ArrayMap<>();
    private Choreographer.FrameCallback framecallback = new Choreographer.FrameCallback() { // from class: android.app.OplusBaseActivityThread.1
        @Override // android.view.Choreographer.FrameCallback
        public void doFrame(long frameTimeNanos) {
            Slog.d(OplusBaseActivityThread.TAG, "pid:" + Process.myPid() + " tid:" + Process.myTid() + " doframe Callback");
        }
    };

    public abstract Application getApplication();

    public abstract String getProcessName();

    public void dispatchWhiteList(boolean isWhiteListApp) {
        sIsWhitelisted = isWhiteListApp;
    }

    public void updateUidCurProcState(int state) {
        boolean change = false;
        synchronized (sLock) {
            int i = sUidCurProcState;
            if ((i <= 6 && state > 6) || (i > 6 && state <= 6)) {
                change = true;
                sStateChanged = true;
            }
            if (i != state) {
                sUidCurProcState = state;
            }
        }
        if (change && state > 6) {
            shouldDoCheck();
        }
        if (change && state <= 6 && this.sC != null) {
            Slog.d(TAG, "post a callback for sC:" + this.sC + " by pid:" + Process.myPid() + " tid:" + Process.myTid());
            this.sC.postFrameCallback(this.framecallback);
        }
    }

    public void shouldDoCheck() {
        synchronized (sLock) {
            sIsShouldDoCheck = true;
        }
    }

    private boolean getAppVisibility() {
        boolean isvisibility = false;
        for (int i = 0; i < this.mActivities.size(); i++) {
            if ((this.mActivities.valueAt(i) == null || this.mActivities.valueAt(i).window == null || !this.mActivities.valueAt(i).window.isFloating()) && this.mActivities.valueAt(i) != null && this.mActivities.valueAt(i).activity != null && this.mActivities.valueAt(i).activity.mStopped) {
                isvisibility = false;
            } else {
                return true;
            }
        }
        return isvisibility;
    }

    private boolean hasActiveNotifications() {
        Context context;
        if (this.mNotiService == null) {
            this.mNotiService = NotificationManager.getService();
        }
        INotificationManager iNotificationManager = this.mNotiService;
        if (iNotificationManager == null || (context = this.mContext) == null) {
            return true;
        }
        try {
            ParceledListSlice<StatusBarNotification> parceledList = iNotificationManager.getAppActiveNotifications(context.getPackageName(), this.mContext.getUserId());
            if (parceledList != null) {
                if (parceledList.getList().size() != 0) {
                    return true;
                }
            }
            return false;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private AppWidgetManager getAppWidgetManager() {
        if (this.mAppWidgetManager == null) {
            this.mAppWidgetManager = (AppWidgetManager) this.mContext.getSystemService(AppWidgetManager.class);
        }
        return this.mAppWidgetManager;
    }

    private OplusBaseAppWidgetManager typeCastingAppWidgetManager() {
        if (this.mContext != null) {
            getAppWidgetManager();
            AppWidgetManager appWidgetManager = this.mAppWidgetManager;
            if (appWidgetManager != null) {
                OplusBaseAppWidgetManager oplusBaseAppWidgetManager = this.mOplusBaseAppWidgetManager;
                if (oplusBaseAppWidgetManager == null) {
                    return (OplusBaseAppWidgetManager) OplusTypeCastingHelper.typeCasting(OplusBaseAppWidgetManager.class, appWidgetManager);
                }
                return oplusBaseAppWidgetManager;
            }
            return null;
        }
        return null;
    }

    private boolean hasBoundWidget() {
        OplusBaseAppWidgetManager typeCastingAppWidgetManager = typeCastingAppWidgetManager();
        this.mOplusBaseAppWidgetManager = typeCastingAppWidgetManager;
        if (typeCastingAppWidgetManager != null) {
            List<String> widgetPkgList = typeCastingAppWidgetManager.getAppWidgetPackageList();
            if (widgetPkgList == null || !widgetPkgList.contains(this.mContext.getPackageName())) {
                return false;
            }
            return true;
        }
        return true;
    }

    private boolean hasRemoteView() {
        return hasBoundWidget() || hasActiveNotifications();
    }

    private void getOplusBaseWindowManagerGlobal() {
        if (this.mOplusBaseWindowManagerGlobal == null) {
            this.mOplusBaseWindowManagerGlobal = (OplusBaseWindowManagerGlobal) OplusTypeCastingHelper.typeCasting(OplusBaseWindowManagerGlobal.class, WindowManagerGlobal.getInstance());
        }
    }

    private int getWindowVisibility() {
        getOplusBaseWindowManagerGlobal();
        OplusBaseWindowManagerGlobal oplusBaseWindowManagerGlobal = this.mOplusBaseWindowManagerGlobal;
        if (oplusBaseWindowManagerGlobal == null) {
            return 0;
        }
        int visibility = 4;
        ArrayList<View> views = oplusBaseWindowManagerGlobal.getViews();
        if (views == null) {
            return 4;
        }
        int count = views.size();
        for (int i = 0; i < count; i++) {
            int tempVisibility = views.get(i).getWindowVisibility();
            if (tempVisibility == 0) {
                return tempVisibility;
            }
            visibility = tempVisibility;
        }
        return visibility;
    }

    public boolean isSkipDoFrameInBG() {
        if (!sIsInitedSkipDoframeEnable) {
            String value = SystemProperties.get("persist.sys.opsd.enable", "0");
            if ("1".equals(value)) {
                sIsSkipDoframeEnable = true;
            }
            sIsInitedSkipDoframeEnable = true;
            String special = SystemProperties.get("persist.sys.skipBGDoFrameSpecialPkg", "def");
            if (special != null && !"def".equals(special)) {
                sDebugSpecial = true;
                sSpecialPkg = special;
            }
        }
        return sIsSkipDoframeEnable;
    }

    public int isCare() {
        if (getApplication() == null || getApplication().getApplicationInfo() == null) {
            return 1;
        }
        int ret = (Process.myUid() < 10000 || getApplication().getApplicationInfo().processName.contains("systemui") || getApplication().getApplicationInfo().processName.contains(OplusResolverIntentUtil.DEFAULT_APP_LAUNCHER) || getApplication().getApplicationInfo().processName.contains(OplusResolverIntentUtil.DEFAULT_APP_CAMERA) || sIsWhitelisted) ? 1 : 2;
        return ret;
    }

    public boolean shouldDoFrameInBG() {
        boolean ret = sLastRet;
        boolean doCheck = false;
        synchronized (sLock) {
            if (sIsShouldDoCheck) {
                doCheck = true;
                sIsShouldDoCheck = false;
            }
        }
        if (sLastRet) {
            if (doCheck) {
                boolean ret2 = toCheck();
                return ret2;
            }
            return ret;
        }
        boolean ret3 = toCheck();
        return ret3;
    }

    protected void reportBindApplicationFinished(Application app) {
        try {
            OplusActivityManager.getInstance().reportBindApplicationFinished(app.getPackageName(), app.getUserId(), Process.myPid());
        } catch (Exception ex) {
            Slog.e(TAG, "Exception = " + ex);
        }
    }

    private boolean getProcState() {
        boolean z;
        synchronized (sLock) {
            z = sUidCurProcState <= 6;
        }
        return z;
    }

    private boolean toCheck() {
        boolean z = true;
        sLastRet = getProcState() || getAppVisibility() || getWindowVisibility() == 0 || hasRemoteView();
        if (sDebugSpecial && getProcessName().equals(sSpecialPkg)) {
            ArrayList<View> views = new ArrayList<>();
            boolean isHoldWin = true;
            OplusBaseWindowManagerGlobal oplusBaseWindowManagerGlobal = this.mOplusBaseWindowManagerGlobal;
            if (oplusBaseWindowManagerGlobal != null && (views = oplusBaseWindowManagerGlobal.getViews()) == null) {
                isHoldWin = false;
            }
            synchronized (sLock) {
                StringBuilder append = new StringBuilder().append("shouldDoFrameInBG pkg:").append(sSpecialPkg).append(" pid:").append(Process.myPid()).append(" tid:").append(Process.myTid()).append(" isDoFrameInBG:").append(sLastRet).append(" procState:").append(sUidCurProcState).append(" getAppVisibility:").append(getAppVisibility()).append(" getWindowVisibility:");
                if (getWindowVisibility() != 0) {
                    z = false;
                }
                Slog.d(TAG, append.append(z).append(" hasRemoteView:").append(hasRemoteView()).append(" mActivities.size:").append(this.mActivities.size()).append(" views.size:").append(isHoldWin ? views.size() : 0).toString());
            }
        }
        return sLastRet;
    }
}
