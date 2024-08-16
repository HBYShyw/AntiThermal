package com.oplus.screenshot;

import android.common.OplusFrameworkFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.oplus.content.OplusContext;
import com.oplus.dynamicvsync.IOplusDynamicVsyncFeature;
import com.oplus.screenshot.IOplusScreenshotManager;
import com.oplus.util.OplusLog;

/* loaded from: classes.dex */
public final class OplusScreenshotManager {
    private static final boolean DBG = true;
    public static final String GLOBAL_ACTION_VISIBLE = "global_action_visible";
    public static final String NAVIGATIONBAR_VISIBLE = "navigationbar_visible";
    public static final String SCREENSHOT_DIRECTION = "screenshot_direction";
    public static final String SCREENSHOT_ORIENTATION = "screenshot_orientation";
    public static final String SCREENSHOT_SOURCE = "screenshot_source";
    private static final int SECONDS_30 = 30000;
    public static final String STATUSBAR_VISIBLE = "statusbar_visible";
    private static final String TAG = "LongshotDump/OplusScreenshotManager";
    private int mRotation;
    private volatile IOplusScreenshotManager mService;
    private static volatile OplusScreenshotManager sInstance = null;
    private static IOplusDynamicVsyncFeature sIOplusDynamicVsyncFeature = null;
    private final IBinder.DeathRecipient mListenerDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.screenshot.OplusScreenshotManager.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (OplusScreenshotManager.class) {
                if (OplusScreenshotManager.this.mService != null) {
                    OplusScreenshotManager.this.mService.asBinder().unlinkToDeath(this, 0);
                    OplusScreenshotManager.this.mService = null;
                }
            }
        }
    };
    private boolean mIsLaunching = false;

    private OplusScreenshotManager() {
    }

    public static OplusScreenshotManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusScreenshotManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusScreenshotManager();
                }
            }
        }
        return sInstance;
    }

    public static OplusScreenshotManager peekInstance() {
        return sInstance;
    }

    public void takeScreenshot(Bundle extras) {
        IOplusScreenshotManager service = getService();
        if (service != null) {
            try {
                service.takeScreenshot(extras);
            } catch (RemoteException e) {
                OplusLog.e(true, TAG, "takeScreenshot : " + e.toString());
            } catch (Exception e2) {
                OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            }
        }
    }

    public boolean isScreenshotMode() {
        IOplusScreenshotManager service = getService();
        if (service == null) {
            return false;
        }
        try {
            boolean result = service.isScreenshotMode();
            return result;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "isScreenshotMode : " + e.toString());
            return false;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return false;
        }
    }

    public boolean isScreenshotEdit() {
        IOplusScreenshotManager service = getService();
        if (service == null) {
            return false;
        }
        try {
            boolean result = service.isScreenshotEdit();
            return result;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "isScreenshotEdit : " + e.toString());
            return false;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return false;
        }
    }

    public void takeLongshot(boolean statusBarVisible, boolean navBarVisible) {
        IOplusScreenshotManager service = getService();
        if (service != null) {
            try {
                if (sIOplusDynamicVsyncFeature == null) {
                    sIOplusDynamicVsyncFeature = (IOplusDynamicVsyncFeature) OplusFrameworkFactory.getInstance().getFeature(IOplusDynamicVsyncFeature.DEFAULT, new Object[0]);
                }
                sIOplusDynamicVsyncFeature.doAnimation(30000, "Longshot");
                service.takeLongshot(statusBarVisible, navBarVisible);
            } catch (RemoteException e) {
                OplusLog.e(true, TAG, "takeLongshot : " + e.toString());
            } catch (Exception e2) {
                OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            }
        }
    }

    public void stopLongshot() {
        IOplusScreenshotManager service = getService();
        if (service != null) {
            try {
                service.stopLongshot();
            } catch (RemoteException e) {
                OplusLog.e(true, TAG, "stopLongshot : " + e.toString());
            } catch (Exception e2) {
                OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            }
        }
    }

    public boolean isLongshotMode() {
        IOplusScreenshotManager service = getService();
        if (service == null) {
            return false;
        }
        try {
            boolean result = service.isLongshotMode();
            return result;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "isLongshotMode : " + e.toString());
            return false;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return false;
        }
    }

    public boolean isLongshotDisabled() {
        IOplusScreenshotManager service = getService();
        if (service == null) {
            return false;
        }
        try {
            boolean result = service.isLongshotDisabled();
            return result;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "isLongshotDisabled : " + e.toString());
            return false;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return false;
        }
    }

    public void reportLongshotDumpResult(OplusLongshotDump result) {
        IOplusScreenshotManager service = getService();
        if (service != null) {
            try {
                service.reportLongshotDumpResult(result);
            } catch (RemoteException e) {
                OplusLog.e(true, TAG, "reportLongshotDumpResult : " + e.toString());
            } catch (Exception e2) {
                OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            }
        }
    }

    public boolean isScreenshotSupported() {
        IOplusScreenshotManager service = getService();
        if (service == null) {
            return true;
        }
        try {
            boolean result = service.isScreenshotSupported();
            return result;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "isScreenshotSupported : " + e.toString());
            return true;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return true;
        }
    }

    public void setScreenshotEnabled(boolean enabled) {
        IOplusScreenshotManager service = getService();
        if (service != null) {
            try {
                service.setScreenshotEnabled(enabled);
            } catch (RemoteException e) {
                OplusLog.e(true, TAG, "setScreenshotEnabled : " + e.toString());
            } catch (Exception e2) {
                OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            }
        }
    }

    public boolean isScreenshotEnabled() {
        IOplusScreenshotManager service = getService();
        if (service == null) {
            return true;
        }
        try {
            boolean result = service.isScreenshotEnabled();
            return result;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "isScreenshotEnabled : " + e.toString());
            return true;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return true;
        }
    }

    public void setLongshotEnabled(boolean enabled) {
        IOplusScreenshotManager service = getService();
        if (service != null) {
            try {
                service.setLongshotEnabled(enabled);
            } catch (RemoteException e) {
                OplusLog.e(true, TAG, "setLongshotEnabled : " + e.toString());
            } catch (Exception e2) {
                OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            }
        }
    }

    public boolean isLongshotEnabled() {
        IOplusScreenshotManager service = getService();
        if (service == null) {
            return true;
        }
        try {
            boolean result = service.isLongshotEnabled();
            return result;
        } catch (RemoteException e) {
            OplusLog.e(true, TAG, "isLongshotEnabled : " + e.toString());
            return true;
        } catch (Exception e2) {
            OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            return true;
        }
    }

    @Deprecated
    public void notifyOverScroll(OplusLongshotEvent event) {
        IOplusScreenshotManager service = getService();
        if (service != null) {
            try {
                service.notifyOverScroll(event);
            } catch (RemoteException e) {
                OplusLog.e(true, TAG, "notifyOverScroll : " + e.toString());
            } catch (Exception e2) {
                OplusLog.e(true, TAG, Log.getStackTraceString(e2));
            }
        }
    }

    public void setFixedRotationLaunchingAppUnchecked(boolean isLaunching, int rotation) {
        this.mIsLaunching = isLaunching;
        this.mRotation = rotation;
    }

    public boolean isLaunching() {
        return this.mIsLaunching;
    }

    public int getRatation() {
        return this.mRotation;
    }

    private IOplusScreenshotManager getService() {
        IOplusScreenshotManager service;
        synchronized (OplusScreenshotManager.class) {
            if (this.mService == null && (service = IOplusScreenshotManager.Stub.asInterface(ServiceManager.getService(OplusContext.SCREENSHOT_SERVICE))) != null) {
                try {
                    service.asBinder().linkToDeath(this.mListenerDeathRecipient, 0);
                    this.mService = service;
                } catch (RemoteException e) {
                    OplusLog.e(TAG, "color_screenshot service is not exists.");
                }
            }
        }
        return this.mService;
    }
}
