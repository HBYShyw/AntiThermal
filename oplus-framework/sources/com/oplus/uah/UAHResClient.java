package com.oplus.uah;

import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import com.oplus.uah.info.UAHEventRequest;
import com.oplus.uah.info.UAHResRequest;
import com.oplus.uah.info.UAHRuleCtrlRequest;
import java.util.HashMap;

/* loaded from: classes.dex */
public class UAHResClient {
    private static final int MAX_VELOCITY = 24000;
    private static final int MIN_VELOCITY = 150;
    private static final int MOVE_SLOP = 40;
    private static final int STACK_BEGIN = 4;
    private static final int TIMEOUT_ZERO = 0;
    private static final int UNITS = 1000;
    private String mIdentity;
    private int mRequest;
    private int mStartX;
    private int mStartY;
    private VelocityTracker mVelocityTracker = null;
    private boolean mVerifyStackFailed;
    private static final String TAG = UAHResClient.class.getSimpleName();
    private static boolean DEBUG_UAH = false;
    private static HashMap<String, UAHResClient> sUahResClientMap = new HashMap<>();

    private UAHResClient(Class cls) {
        this.mIdentity = "";
        this.mVerifyStackFailed = false;
        this.mIdentity = cls.getSimpleName();
        if (!verifyClazzStackTrace(cls)) {
            Log.d(TAG, "Class: " + cls.getName() + " stack trace verify failed!");
            this.mVerifyStackFailed = true;
        }
    }

    public static UAHResClient get(Class cls) {
        UAHResClient resClient;
        if (cls == null) {
            Log.e(TAG, "the parameter class is null");
            return null;
        }
        String fullClassName = cls.getName();
        synchronized (sUahResClientMap) {
            resClient = sUahResClientMap.get(fullClassName);
            if (resClient == null) {
                resClient = new UAHResClient(cls);
                sUahResClientMap.put(fullClassName, resClient);
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

    public int acquireEvent(UAHEventRequest mUahEventRequest) {
        if (!isAccessPermitted()) {
            Log.e(TAG, "AccessPermitted check failed return -1!");
            return -1;
        }
        return UAHPerfManager.getInstance().uahEventAcquire(this.mIdentity, mUahEventRequest);
    }

    public int acquireResource(UAHResRequest mUAHResRequest) {
        if (!isAccessPermitted()) {
            Log.e(TAG, "AccessPermitted check failed return -1!");
            return -1;
        }
        return UAHPerfManager.getInstance().uahResAcquire(this.mIdentity, mUAHResRequest);
    }

    public void release(int handle) {
        if (!isAccessPermitted()) {
            Log.e(TAG, "AccessPermitted check failed return!");
        } else {
            UAHPerfManager.getInstance().uahRelease(handle);
        }
    }

    public void ctrlRule(UAHRuleCtrlRequest mUahCtrlRequest) {
        if (!isAccessPermitted()) {
            Log.e(TAG, "AccessPermitted check failed return!");
        } else {
            UAHPerfManager.getInstance().uahRuleCtrl(this.mIdentity, mUahCtrlRequest);
        }
    }

    public int getModeStatus(int mode) {
        if (!isAccessPermitted()) {
            Log.e(TAG, "AccessPermitted check failed return!");
            return -1;
        }
        if (DEBUG_UAH) {
            Log.i(TAG, "UahGetModeStatus: identity =" + this.mIdentity + " mode =" + mode);
        }
        return UAHPerfManager.getInstance().getModeStatus(this.mIdentity, mode);
    }

    public String getResState(int opCode) {
        if (!isAccessPermitted()) {
            Log.e(TAG, "AccessPermitted check failed return null!");
            return null;
        }
        if (DEBUG_UAH) {
            Log.i(TAG, "osenseGetModeStatus: uahReadFile opCode =" + opCode);
        }
        return UAHPerfManager.getInstance().uahReadFile(this.mIdentity, opCode);
    }

    public long[][][] getHistory() {
        if (!isAccessPermitted()) {
            Log.e(TAG, "AccessPermitted check failed return null!");
            return null;
        }
        if (DEBUG_UAH) {
            Log.i(TAG, "uahGetHistory: identity =" + this.mIdentity);
        }
        return UAHPerfManager.getInstance().getGetHistory();
    }

    public void sendFling(MotionEvent ev, int duration) {
        Log.w(TAG, "UAH uahSendFling duration = " + duration);
        try {
            int actionMasked = ev.getActionMasked();
            int pointerIndex = ev.getActionIndex();
            int pointerId = ev.getPointerId(pointerIndex);
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
                        this.mRequest = -1;
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
                        int i = this.mRequest;
                        if (i != -1) {
                            release(i);
                        }
                        if (initialVelocity > 150) {
                            int tDuration = (int) (duration * ((initialVelocity * 1.0f) / 150.0f));
                            if (xVelcity > yVelcity) {
                                acquireEvent(new UAHEventRequest(69, "", tDuration, null));
                                return;
                            } else {
                                acquireEvent(new UAHEventRequest(68, "", tDuration, null));
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
                                    this.mRequest = acquireEvent(new UAHEventRequest(69, "", 0, null));
                                    return;
                                } else {
                                    this.mRequest = acquireEvent(new UAHEventRequest(68, "", 0, null));
                                    return;
                                }
                            }
                            return;
                        }
                        return;
                    }
                    return;
                case 3:
                    int i2 = this.mRequest;
                    if (i2 != -1) {
                        release(i2);
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
