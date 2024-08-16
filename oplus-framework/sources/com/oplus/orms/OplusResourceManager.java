package com.oplus.orms;

import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import com.oplus.orms.info.OrmsCtrlDataParam;
import com.oplus.orms.info.OrmsNotifyParam;
import com.oplus.orms.info.OrmsSaParam;
import java.util.HashMap;

/* loaded from: classes.dex */
public class OplusResourceManager implements IOplusResourceManager {
    private static final boolean DEBUG = false;
    private static final int STACK_BEGIN = 4;
    private static final String TAG = "Orms_Manager";
    private static final int sDurationOffSet = 100;
    private static final HashMap<String, OplusResourceManager> sInstanceCache = new HashMap<>();
    private static final int sMaxVelocity = 24000;
    private static final int sMinDiffX = 150;
    private static final int sMinVelocity = 150;
    private static final int sMoveSlop = 40;
    private static final int sTimeOutZero = 0;
    private static final int sUnits = 1000;
    private String mIdentity;
    private long mRequest;
    private int mStartX;
    private int mStartY;
    private boolean mVerifyStackFailed;
    private VelocityTracker mVelocityTracker = null;
    private int mAccessPermission = -1;

    protected OplusResourceManager(Class clazz) {
        this.mIdentity = "";
        this.mVerifyStackFailed = false;
        this.mIdentity = clazz.getSimpleName();
        if (!verifyClazzStackTrace(clazz)) {
            Log.d(TAG, "Class: " + clazz.getName() + " stack trace verify failed!");
            this.mVerifyStackFailed = true;
        }
    }

    public static synchronized OplusResourceManager getInstance(Class clazz) {
        OplusResourceManager instance;
        synchronized (OplusResourceManager.class) {
            String key = clazz.getName();
            HashMap<String, OplusResourceManager> hashMap = sInstanceCache;
            instance = hashMap.get(key);
            if (instance == null) {
                instance = new OplusResourceManager(clazz);
                hashMap.put(key, instance);
            }
        }
        return instance;
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
            return false;
        }
        if (this.mAccessPermission == -1) {
            int checkAccessPermission = OplusResourceManagerInner.checkAccessPermission(this.mIdentity);
            this.mAccessPermission = checkAccessPermission;
            if (checkAccessPermission == -1) {
                Log.d(TAG, "orms service is not available now, need check again! " + this.mIdentity);
            } else if (checkAccessPermission == 0) {
                Log.d(TAG, "Package: " + this.mIdentity + " has no access permission to orms!");
            }
        }
        return this.mAccessPermission == 1;
    }

    @Override // com.oplus.orms.IOplusResourceManager
    public long ormsSetSceneAction(OrmsSaParam ormsSaParam) {
        if (!isAccessPermitted()) {
            return -1L;
        }
        if (ormsSaParam == null || ormsSaParam.scene == null || ormsSaParam.action == null) {
            Log.e(TAG, "sceneAction is illegal! ");
            return -1L;
        }
        return OplusResourceManagerInner.getInstance().setSceneAction(this.mIdentity, ormsSaParam);
    }

    @Override // com.oplus.orms.IOplusResourceManager
    public void ormsClrSceneAction(long request) {
        if (!isAccessPermitted() || request < 0) {
            return;
        }
        OplusResourceManagerInner.getInstance().clrSceneAction(this.mIdentity, request);
    }

    @Override // com.oplus.orms.IOplusResourceManager
    public void ormsSetNotification(OrmsNotifyParam ormsNotifyParam) {
        if (!isAccessPermitted()) {
            return;
        }
        if (ormsNotifyParam == null || ormsNotifyParam.param4 == null) {
            Log.e(TAG, "notification param is illegal! ");
        } else {
            OplusResourceManagerInner.getInstance().setNotification(this.mIdentity, ormsNotifyParam);
        }
    }

    @Override // com.oplus.orms.IOplusResourceManager
    public void ormsSetCtrlData(OrmsCtrlDataParam ormsCtrlDataParam) {
        if (!isAccessPermitted()) {
            return;
        }
        if (ormsCtrlDataParam == null) {
            Log.e(TAG, "ormsCtrlData param is illegal! ");
        } else {
            OplusResourceManagerInner.getInstance().setCtrlData(this.mIdentity, ormsCtrlDataParam);
        }
    }

    @Override // com.oplus.orms.IOplusResourceManager
    public void ormsClrCtrlData() {
        if (!isAccessPermitted()) {
            return;
        }
        OplusResourceManagerInner.getInstance().clrCtrlData(this.mIdentity);
    }

    @Override // com.oplus.orms.IOplusResourceManager
    public int ormsGetModeStatus(int mode) {
        if (!isAccessPermitted()) {
            return -1;
        }
        return OplusResourceManagerInner.getInstance().getModeStatus(this.mIdentity, mode);
    }

    @Override // com.oplus.orms.IOplusResourceManager
    public long[][][] ormsGetPerfLimit() {
        if (!isAccessPermitted()) {
            return null;
        }
        return OplusResourceManagerInner.getInstance().getPerfLimit(this.mIdentity);
    }

    @Override // com.oplus.orms.IOplusResourceManager
    public void ormsSendFling(MotionEvent ev, int duration) {
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
                            ormsClrSceneAction(j);
                        }
                        if (initialVelocity > 150) {
                            int duration2 = (int) (duration * ((initialVelocity * 1.0f) / 150.0f));
                            if (xVelcity > yVelcity) {
                                ormsSetSceneAction(new OrmsSaParam("", "ORMS_ACTION_SWIPE_H", duration2));
                                return;
                            } else {
                                ormsSetSceneAction(new OrmsSaParam("", "ORMS_ACTION_SWIPE_V", duration2));
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
                                    this.mRequest = ormsSetSceneAction(new OrmsSaParam("", "ORMS_ACTION_SWIPE_SLOW_H", 0));
                                    return;
                                } else {
                                    this.mRequest = ormsSetSceneAction(new OrmsSaParam("", "ORMS_ACTION_SWIPE_SLOW_V", 0));
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
                        ormsClrSceneAction(j2);
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
