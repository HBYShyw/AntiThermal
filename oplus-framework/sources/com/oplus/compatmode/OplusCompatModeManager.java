package com.oplus.compatmode;

import android.app.OplusActivityManager;
import android.os.RemoteException;
import android.util.Log;
import com.oplus.compactwindow.IOplusCompactWindowObserver;
import com.oplus.compactwindow.OplusCompactWindowInfo;

/* loaded from: classes.dex */
public class OplusCompatModeManager {
    private static final String TAG = "OplusCompatModeManager";
    private static IOplusCompatModeSession sCompatModeSession;
    private static volatile OplusCompatModeManager sInstance;
    private final OplusActivityManager mAms = new OplusActivityManager();

    private OplusCompatModeManager() {
    }

    public static OplusCompatModeManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusCompatModeManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusCompatModeManager();
                }
            }
        }
        return sInstance;
    }

    public int moveCompatModeWindowToLeft(int taskId) {
        try {
            IOplusCompatModeSession session = getCompatModeSession();
            if (session != null) {
                return session.moveCompatModeWindowToLeft(taskId);
            }
            return -1;
        } catch (Exception e) {
            Log.e(TAG, "moveCompatModeWindowToLeft error");
            return -1;
        }
    }

    public int moveCompatModeWindowToRight(int taskId) {
        try {
            IOplusCompatModeSession session = getCompatModeSession();
            if (session != null) {
                return session.moveCompatModeWindowToRight(taskId);
            }
            return -1;
        } catch (Exception e) {
            Log.e(TAG, "moveCompatModeWindowToRight error");
            return -1;
        }
    }

    public OplusCompactWindowInfo getCompactWindowInfo(int taskId) {
        try {
            IOplusCompatModeSession session = getCompatModeSession();
            if (session != null) {
                return session.getCompactWindowInfo(taskId);
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "getCompactWindowInfo error");
            return null;
        }
    }

    public boolean registerCompactWindowObserver(IOplusCompactWindowObserver observer) {
        try {
            IOplusCompatModeSession session = getCompatModeSession();
            if (session != null) {
                return session.registerCompactWindowObserver(observer);
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "getCompactWindowInfo error");
            return false;
        }
    }

    public boolean unregisterCompactWindowObserver(IOplusCompactWindowObserver observer) {
        try {
            IOplusCompatModeSession session = getCompatModeSession();
            if (session != null) {
                return session.unregisterCompactWindowObserver(observer);
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "getCompactWindowInfo error");
            return false;
        }
    }

    public void onRecentAnimationStart() {
        try {
            IOplusCompatModeSession session = getCompatModeSession();
            if (session != null) {
                session.onRecentAnimationStart();
            }
        } catch (Exception e) {
            Log.e(TAG, "onRecentAnimationStart error", e);
        }
    }

    public void onRecentAnimationEnd() {
        try {
            IOplusCompatModeSession session = getCompatModeSession();
            if (session != null) {
                session.onRecentAnimationEnd();
            }
        } catch (Exception e) {
            Log.e(TAG, "onRecentAnimationEnd error", e);
        }
    }

    public void compatUIInit() {
        try {
            IOplusCompatModeSession session = getCompatModeSession();
            if (session != null) {
                session.compatUIInit();
            }
        } catch (Exception e) {
            Log.e(TAG, "compatUIInit error", e);
        }
    }

    public void restartProcessAsUser(String pkg, int userId) {
        try {
            IOplusCompatModeSession session = getCompatModeSession();
            if (session != null) {
                session.forceStopPackageAsUser(pkg, userId);
            }
        } catch (Exception e) {
            Log.e(TAG, "forceStopPackageAsUser error", e);
        }
    }

    private IOplusCompatModeSession getCompatModeSession() {
        IOplusCompatModeSession iOplusCompatModeSession;
        synchronized (OplusCompatModeManager.class) {
            if (sCompatModeSession == null) {
                try {
                    sCompatModeSession = this.mAms.getCompatModeSession();
                } catch (RemoteException e) {
                    Log.e(TAG, "getCompatModeSession error");
                }
            }
            iOplusCompatModeSession = sCompatModeSession;
        }
        return iOplusCompatModeSession;
    }
}
