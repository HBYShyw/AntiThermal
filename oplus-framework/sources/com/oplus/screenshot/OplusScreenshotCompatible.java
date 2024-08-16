package com.oplus.screenshot;

import android.R;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.OplusActivityManager;
import android.app.OplusStatusBarManager;
import android.app.OplusWhiteListManager;
import android.app.StatusBarManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Rect;
import android.os.RemoteException;
import android.provider.oplus.Telephony;
import android.util.Log;
import android.view.IWindowManager;
import android.view.InputEvent;
import android.view.OplusWindowManager;
import android.view.WindowManagerGlobal;
import com.android.internal.notification.SystemNotificationChannels;
import com.oplus.util.OplusLog;
import java.lang.reflect.Method;
import java.util.List;

/* loaded from: classes.dex */
public final class OplusScreenshotCompatible {
    private static final String CHANNEL_ID = SystemNotificationChannels.FOREGROUND_SERVICE;
    private static final boolean DBG = true;
    private static final String TAG = "LongshotDump/OplusScreenshotCompatible";
    private final ActivityManager mActivityManager;
    private final Context mContext;
    private final NotificationManager mNotificationManager;
    private final StatusBarManager mStatusBarManager;
    private final OplusWhiteListManager mWhiteListManager;
    private final OplusStatusBarManager mOplusStatusBarManager = new OplusStatusBarManager();
    private final OplusWindowManager mOplusWindowManager = new OplusWindowManager();
    private final OplusActivityManager mOplusAms = new OplusActivityManager();

    public OplusScreenshotCompatible(Context context) {
        this.mContext = context;
        this.mActivityManager = (ActivityManager) context.getSystemService("activity");
        this.mNotificationManager = (NotificationManager) context.getSystemService(Telephony.ThreadsColumns.NOTIFICATION);
        this.mStatusBarManager = (StatusBarManager) context.getSystemService("statusbar");
        this.mWhiteListManager = new OplusWhiteListManager(context);
    }

    public boolean isInMultiWindowMode() {
        int dockSide = -1;
        try {
            IWindowManager wm = WindowManagerGlobal.getWindowManagerService();
            dockSide = wm.getDockedStackSide();
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "isInMultiWindowMode : " + e.toString());
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
        }
        return -1 != dockSide;
    }

    public void longshotNotifyConnected(boolean isConnected) {
        try {
            this.mOplusWindowManager.longshotNotifyConnected(isConnected);
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "longshotNotifyConnected : " + e.toString());
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
        }
    }

    public int getLongshotSurfaceLayer() {
        try {
            int layer = this.mOplusWindowManager.getLongshotSurfaceLayer();
            return layer;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "getLongshotSurfaceLayer : " + e.toString());
            return 0;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return 0;
        }
    }

    public int getLongshotSurfaceLayerByType(int type) {
        try {
            int layer = this.mOplusWindowManager.getLongshotSurfaceLayerByType(type);
            return layer;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "getLongshotSurfaceLayerByType : " + e.toString());
            return 0;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return 0;
        }
    }

    public boolean isInputShow() {
        try {
            boolean result = this.mOplusWindowManager.isInputShow();
            return result;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "isInputShow : " + e.toString());
            return false;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return false;
        }
    }

    public boolean isVolumeShow() {
        try {
            boolean result = this.mOplusWindowManager.isVolumeShow();
            return result;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "isVolumeShow : " + e.toString());
            return false;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return false;
        }
    }

    public boolean isShortcutsPanelShow() {
        try {
            boolean result = this.mOplusWindowManager.isShortcutsPanelShow();
            return result;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "isShortcutsPanelShow : " + e.toString());
            return false;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return false;
        }
    }

    public boolean isStatusBarVisible() {
        try {
            boolean result = this.mOplusWindowManager.isStatusBarVisible();
            return result;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "isStatusBarVisible : " + e.toString());
            return false;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return false;
        }
    }

    public boolean isNavigationBarVisible() {
        try {
            boolean result = this.mOplusWindowManager.isNavigationBarVisible();
            return result;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "isNavigationBarVisible : " + e.toString());
            return false;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return false;
        }
    }

    public boolean isKeyguardShowingAndNotOccluded() {
        try {
            boolean result = this.mOplusWindowManager.isKeyguardShowingAndNotOccluded();
            return result;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "isKeyguardShowingAndNotOccluded : " + e.toString());
            return false;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return false;
        }
    }

    public void getFocusedWindowFrame(Rect frame) {
        try {
            this.mOplusWindowManager.getFocusedWindowFrame(frame);
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "getFocusedWindowFrame : " + e.toString());
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
        }
    }

    public boolean injectInputEvent(InputEvent event, int mode) {
        try {
            this.mOplusWindowManager.longshotInjectInput(event, mode);
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "injectInputEvent : " + e.toString());
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
        }
        return true;
    }

    public void injectInputBegin() {
        try {
            this.mOplusWindowManager.longshotInjectInputBegin();
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "injectInputBegin : " + e.toString());
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
        }
    }

    public void injectInputEnd() {
        try {
            this.mOplusWindowManager.longshotInjectInputEnd();
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "injectInputEnd : " + e.toString());
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
        }
    }

    @Deprecated
    public Notification.Builder createNotificationBuilder() {
        String str = CHANNEL_ID;
        NotificationChannel foregroundChannel = new NotificationChannel(str, this.mContext.getString(R.string.permlab_invokeCarrierSetup), 0);
        this.mNotificationManager.createNotificationChannel(foregroundChannel);
        return new Notification.Builder(this.mContext, str);
    }

    @Deprecated
    public void cancelNotification(int notificatinID) {
        this.mNotificationManager.deleteNotificationChannel(CHANNEL_ID);
        this.mNotificationManager.cancel(notificatinID);
    }

    public void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        this.mNotificationManager.createNotificationChannel(channel);
    }

    public void createNotificationChannel(String channelId, String channelName) {
        createNotificationChannel(channelId, channelName, 0);
    }

    public void deleteNotificationChannel(String channelId) {
        this.mNotificationManager.deleteNotificationChannel(channelId);
    }

    public Notification.Builder createNotificationBuilder(String channelId) {
        return new Notification.Builder(this.mContext, channelId);
    }

    public void showNavigationBar(boolean show) {
    }

    public void setShortcutsPanelState(boolean enable) {
    }

    public void addStageProtectInfo(String pkg, long timeout) {
        this.mWhiteListManager.addStageProtectInfo(pkg, timeout);
    }

    public void removeStageProtectInfo(String pkg) {
        this.mWhiteListManager.removeStageProtectInfo(pkg);
    }

    @Deprecated
    public ComponentName getTopActivity() {
        return null;
    }

    public String getTopPackage() {
        List<String> list = null;
        try {
            Method getAllTopPkgName = this.mActivityManager.getClass().getMethod("getAllTopPkgName", new Class[0]);
            getAllTopPkgName.setAccessible(true);
            list = (List) getAllTopPkgName.invoke(this.mActivityManager, new Object[0]);
        } catch (Exception e) {
        }
        if (list != null) {
            int size = list.size();
            if (size > 0) {
                return list.get(0);
            }
            return null;
        }
        return null;
    }
}
