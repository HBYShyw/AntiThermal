package com.oplus.zoomwindow;

import android.app.OplusActivityTaskManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.IRecentsAnimationController;
import android.window.WindowContainerToken;

/* loaded from: classes.dex */
public class OplusZoomTaskManagerInternal {
    private static final String TAG = "OplusZoomTaskManager";
    private static IOplusZoomTaskController sZoomTaskController;

    private OplusZoomTaskManagerInternal() {
        sZoomTaskController = getZoomTaskController();
    }

    public static OplusZoomTaskManagerInternal getInstance() {
        return LazyHolder.INSTANCE;
    }

    private IOplusZoomTaskController getZoomTaskController() {
        if (sZoomTaskController == null) {
            try {
                sZoomTaskController = OplusActivityTaskManager.getInstance().getZoomTaskController();
            } catch (RemoteException e) {
                Log.e(TAG, "getZoomTaskController error");
            }
        }
        return sZoomTaskController;
    }

    public boolean supportZoomTaskController() {
        IOplusZoomTaskController zoomTaskController = getZoomTaskController();
        return zoomTaskController != null;
    }

    public boolean registerZoomTaskListener(IOplusZoomTaskListener zoomTaskListener) {
        try {
            IOplusZoomTaskController zoomTaskController = getZoomTaskController();
            if (zoomTaskController == null) {
                return false;
            }
            boolean result = zoomTaskController.registerZoomTaskListener(zoomTaskListener);
            return result;
        } catch (Exception e) {
            Log.e(TAG, "registerZoomTaskListener error");
            return false;
        }
    }

    public boolean unregisterZoomTaskListener(IOplusZoomTaskListener zoomTaskListener) {
        try {
            IOplusZoomTaskController zoomTaskController = getZoomTaskController();
            if (zoomTaskController == null) {
                return false;
            }
            boolean result = zoomTaskController.unregisterZoomTaskListener(zoomTaskListener);
            return result;
        } catch (Exception e) {
            Log.e(TAG, "unregisterZoomTaskListener error");
            return false;
        }
    }

    public boolean recentAnimationFinished(int taskId, int type, Rect rect, int orientation, Bundle bOptions, IRecentsAnimationController recentsAnimationController, boolean moveHomeToTop, boolean sendUserLeaveHint) {
        try {
            IOplusZoomTaskController zoomTaskController = getZoomTaskController();
            if (zoomTaskController == null) {
                return false;
            }
            boolean result = zoomTaskController.recentAnimationFinished(taskId, type, rect, orientation, bOptions, recentsAnimationController, moveHomeToTop, sendUserLeaveHint);
            return result;
        } catch (Exception e) {
            Log.e(TAG, "recentAnimationFinished error");
            return false;
        }
    }

    public void onZoomStateChanged(OplusZoomTaskInfo zoomTaskInfo) {
        try {
            IOplusZoomTaskController zoomTaskController = getZoomTaskController();
            if (zoomTaskController != null) {
                zoomTaskController.onZoomStateChanged(zoomTaskInfo);
            }
        } catch (Exception e) {
            Log.e(TAG, "onZoomStateChanged error");
        }
    }

    public void onZoomRotateChanged(int fromRotation, int toRotation, OplusZoomTaskInfo zoomTaskInfo) {
        try {
            IOplusZoomTaskController zoomTaskController = getZoomTaskController();
            if (zoomTaskController != null) {
                zoomTaskController.onZoomRotateChanged(fromRotation, toRotation, zoomTaskInfo);
            }
        } catch (Exception e) {
            Log.e(TAG, "onZoomStateChanged error");
        }
    }

    public void requestChangeZoomTask(WindowContainerToken token, int flag, boolean anim) {
        try {
            IOplusZoomTaskController zoomTaskController = getZoomTaskController();
            if (zoomTaskController != null) {
                zoomTaskController.requestChangeZoomTask(token, flag, anim);
            }
        } catch (Exception e) {
            Log.e(TAG, "requestChangeZoomTask error");
        }
    }

    public void onTransitionEnd(int seq) {
        try {
            IOplusZoomTaskController zoomTaskController = getZoomTaskController();
            if (zoomTaskController != null) {
                zoomTaskController.onTransitionEnd(seq);
            }
        } catch (Exception e) {
            Log.e(TAG, "requestChangeZoomTask error");
        }
    }

    public void onTransitionStart() {
        try {
            IOplusZoomTaskController zoomTaskController = getZoomTaskController();
            if (zoomTaskController != null) {
                zoomTaskController.onTransitionStart();
            }
        } catch (Exception e) {
            Log.e(TAG, "onTransitionStart error");
        }
    }

    public void setInputToken(WindowContainerToken token, IBinder channelToken) {
        try {
            IOplusZoomTaskController zoomTaskController = getZoomTaskController();
            if (zoomTaskController != null) {
                zoomTaskController.setInputToken(token, channelToken);
            }
        } catch (Exception e) {
            Log.e(TAG, "requestChangeZoomTask error");
        }
    }

    /* loaded from: classes.dex */
    private static class LazyHolder {
        private static final OplusZoomTaskManagerInternal INSTANCE = new OplusZoomTaskManagerInternal();

        private LazyHolder() {
        }
    }
}
