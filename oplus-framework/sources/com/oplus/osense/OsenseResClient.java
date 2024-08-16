package com.oplus.osense;

import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import com.oplus.osense.info.OsenseCtrlDataRequest;
import com.oplus.osense.info.OsenseNotifyRequest;
import com.oplus.osense.info.OsenseSaRequest;
import java.util.HashMap;

/* loaded from: classes.dex */
public class OsenseResClient {
    private static final int POINTER_ID_MAX = 31;
    private static final int POINTER_ID_MIN = 0;
    private static final int STACK_BEGIN = 4;
    private static final int sDurationOffSet = 100;
    private static final int sMaxVelocity = 24000;
    private static final int sMinDiffX = 150;
    private static final int sMinVelocity = 150;
    private static final int sMoveSlop = 40;
    private static OsenseResClient sSwResManager = null;
    private static final int sTimeOutZero = 0;
    private static final int sUnits = 1000;
    private String mIdentity;
    private long mRequest;
    private int mStartX;
    private int mStartY;
    private VelocityTracker mVelocityTracker = null;
    private boolean mVerifyStackFailed;
    private static final String TAG = OsenseResClient.class.getSimpleName();
    private static boolean DEBUG_OSENSE = false;
    private static HashMap<String, OsenseResClient> sOsenseResClientMap = new HashMap<>();

    protected OsenseResClient(Class cls) {
        this.mIdentity = "";
        this.mVerifyStackFailed = false;
        this.mIdentity = cls.getSimpleName();
        if (!verifyClazzStackTrace(cls)) {
            Log.d(TAG, "Class: " + cls.getName() + " stack trace verify failed!");
            this.mVerifyStackFailed = true;
        }
    }

    public static OsenseResClient get(Class cls) {
        OsenseResClient resClient;
        if (cls == null) {
            Log.e(TAG, "the parameter class is null!!!");
            return null;
        }
        String className = cls.getSimpleName();
        synchronized (sOsenseResClientMap) {
            resClient = sOsenseResClientMap.get(className);
            if (resClient == null) {
                resClient = new OsenseResClient(cls);
                sOsenseResClientMap.put(className, resClient);
            }
        }
        return resClient;
    }

    private static boolean verifyClazzStackTrace(Class clazz) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (int index = 4; index < stack.length; index++) {
            if (stack[index].getClassName().contains(clazz.getPackage().getName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isAccessPermitted() {
        if (this.mVerifyStackFailed) {
            Log.e(TAG, "Package: " + this.mIdentity + " has no access permission!");
            return false;
        }
        return true;
    }

    public long osenseSetSceneAction(OsenseSaRequest request) {
        if (!isAccessPermitted()) {
            return -1L;
        }
        if (request == null || request.getScene() == null || request.getAction() == null) {
            Log.e(TAG, "osenseSetSceneAction...request or parameters is null!");
            return -1L;
        }
        if (DEBUG_OSENSE) {
            Log.i(TAG, "osenseSetSceneAction: " + this.mIdentity + "|" + request.toString());
        }
        return OsenseResManager.getInstance().setSceneAction(this.mIdentity, request);
    }

    public void osenseClrSceneAction(long handle) {
        if (isAccessPermitted()) {
            if (handle < 0) {
                Log.e(TAG, "osenseClrSceneAction: handle is illegal");
                return;
            }
            if (DEBUG_OSENSE) {
                Log.i(TAG, "osenseClrSceneAction: handle = " + handle);
            }
            OsenseResManager.getInstance().clrSceneAction(this.mIdentity, handle);
        }
    }

    public void osenseSetNotification(OsenseNotifyRequest request) {
        if (!isAccessPermitted()) {
            return;
        }
        if (request == null || request.getParam4() == null) {
            Log.e(TAG, "osenseSetNotification: request is illegal!");
            return;
        }
        if (DEBUG_OSENSE) {
            Log.i(TAG, "osenseSetNotification: " + request.toString());
        }
        OsenseResManager.getInstance().setNotification(this.mIdentity, request);
    }

    public void osenseSetCtrlData(OsenseCtrlDataRequest request) {
        if (!isAccessPermitted()) {
            return;
        }
        if (request == null) {
            Log.e(TAG, "osenseSetCtrlData: request is illegal!");
            return;
        }
        if (DEBUG_OSENSE) {
            Log.i(TAG, "osenseSetCtrlData by " + this.mIdentity);
        }
        OsenseResManager.getInstance().setCtrlData(this.mIdentity, request);
    }

    public void osenseClrCtrlData() {
        if (!isAccessPermitted()) {
            return;
        }
        if (DEBUG_OSENSE) {
            Log.i(TAG, "osenseClrCtrlData by " + this.mIdentity);
        }
        OsenseResManager.getInstance().clrCtrlData(this.mIdentity);
    }

    public int osenseGetModeStatus(int mode) {
        if (!isAccessPermitted()) {
            return -1;
        }
        if (DEBUG_OSENSE) {
            Log.i(TAG, "osenseGetModeStatus: identity =" + this.mIdentity + " mode =" + mode);
        }
        return OsenseResManager.getInstance().getModeStatus(this.mIdentity, mode);
    }

    public long[][][] osenseGetPerfLimit() {
        if (!isAccessPermitted()) {
            return null;
        }
        if (DEBUG_OSENSE) {
            Log.i(TAG, "osenseGetPerfLimit: identity =" + this.mIdentity);
        }
        return OsenseResManager.getInstance().getPerfLimit(this.mIdentity);
    }

    public void osenseSendFling(MotionEvent ev, int duration) {
        try {
            int actionMasked = ev.getActionMasked();
            int pointerIndex = ev.getActionIndex();
            int pointerId = ev.getPointerId(pointerIndex);
            if (pointerId < 0 || pointerId > 31) {
                Log.w(TAG, "invalid motionEvent.pointerId: " + pointerId + ", return");
                return;
            }
            switch (actionMasked) {
                case 0:
                    this.mStartX = (int) ev.getX(pointerIndex);
                    this.mStartY = (int) ev.getY(pointerIndex);
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    if (velocityTracker == null) {
                        this.mVelocityTracker = VelocityTracker.obtain();
                    } else {
                        velocityTracker.clear();
                    }
                    VelocityTracker velocityTracker2 = this.mVelocityTracker;
                    if (velocityTracker2 != null) {
                        velocityTracker2.addMovement(ev);
                        this.mRequest = -1L;
                        return;
                    }
                    return;
                case 1:
                    VelocityTracker velocityTracker3 = this.mVelocityTracker;
                    if (velocityTracker3 != null) {
                        velocityTracker3.addMovement(ev);
                        this.mVelocityTracker.computeCurrentVelocity(1000, 24000.0f);
                        float xVelcity = Math.abs(this.mVelocityTracker.getXVelocity(pointerId));
                        float yVelcity = Math.abs(this.mVelocityTracker.getYVelocity(pointerId));
                        int initialVelocity = (int) Math.max(xVelcity, yVelcity);
                        long j = this.mRequest;
                        if (j != -1) {
                            osenseClrSceneAction(j);
                        }
                        if (initialVelocity > 150) {
                            int duration2 = (int) (duration * ((initialVelocity * 1.0f) / 150.0f));
                            if (xVelcity > yVelcity) {
                                osenseSetSceneAction(new OsenseSaRequest("", "OSENSE_ACTION_SWIPE_H", duration2));
                                return;
                            } else {
                                osenseSetSceneAction(new OsenseSaRequest("", "OSENSE_ACTION_SWIPE_V", duration2));
                                return;
                            }
                        }
                        return;
                    }
                    return;
                case 2:
                    VelocityTracker velocityTracker4 = this.mVelocityTracker;
                    if (velocityTracker4 != null) {
                        velocityTracker4.addMovement(ev);
                        if (this.mRequest == -1) {
                            int diffX = Math.abs(((int) ev.getX(pointerIndex)) - this.mStartX);
                            int diffY = Math.abs(((int) ev.getY(pointerIndex)) - this.mStartY);
                            if (Math.max(diffX, diffY) > 40) {
                                if (diffX > diffY) {
                                    this.mRequest = osenseSetSceneAction(new OsenseSaRequest("", "OSENSE_ACTION_SWIPE_SLOW_H", 0));
                                    return;
                                } else {
                                    this.mRequest = osenseSetSceneAction(new OsenseSaRequest("", "OSENSE_ACTION_SWIPE_SLOW_V", 0));
                                    return;
                                }
                            }
                            return;
                        }
                        return;
                    }
                    return;
                case 3:
                    long j2 = this.mRequest;
                    if (j2 != -1) {
                        osenseClrSceneAction(j2);
                        return;
                    }
                    return;
                default:
                    return;
            }
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "java.lang.IllegalArgumentException");
        }
    }
}
