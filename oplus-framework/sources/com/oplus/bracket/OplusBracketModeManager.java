package com.oplus.bracket;

import android.app.OplusActivityManager;
import android.content.ComponentName;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.RemoteException;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.MotionEvent;
import android.view.View;
import com.android.internal.policy.DecorView;
import com.oplus.bracket.IOplusBracketModeChangedListener;

/* loaded from: classes.dex */
public class OplusBracketModeManager {
    public static final int CLIENT_DISABLE_TOUCH = 4;
    private static final String CLIENT_DISABLE_WINODW_STR = "CLIENT_DISABLE_TOUCH";
    public static final int CLIENT_ENABLE_SEPARATE_UI = 7;
    private static final String CLIENT_ENABLE_SEPARATE_UI_STR = "CLIENT_ENABLE_SEPARATE_UI";
    public static final int CLIENT_ENABLE_TOUCH = 3;
    private static final String CLIENT_ENABLE_TOUCH_STR = "CLIENT_ENABLE_TOUCH";
    public static final int CLIENT_ENTER_BRACKET = 1;
    private static final String CLIENT_ENTER_BRACKET_STR = "CLIENT_ENTER_BRACKET";
    public static final int CLIENT_EXIT_BRACKET = 2;
    private static final String CLIENT_EXIT_BRACKET_STR = "CLIENT_EXIT_BRACKET";
    public static final int CLIENT_INVISIBLE_TOUCH = 6;
    private static final String CLIENT_INVISIBLE_TOUCH_STR = "CLIENT_INVISIBLE_TOUCH";
    public static final int CLIENT_VISIBLE_TOUCH = 5;
    private static final String CLIENT_VISIBLE_TOUCH_STR = "CLIENT_VISIBLE_TOUCH";
    public static final int SEVER_QUEST_DCS_TOUCH = 4;
    public static final int SEVER_QUEST_TOUCH = 3;
    public static final String TAG = "OplusBracketModeManager";
    public static final int WINDOWING_MODE_BRACKET = 115;
    public static final int WINDOW_TYPE_MODE_BRACKET = 2115;
    public static final int WINDOW_TYPE_MODE_SEPARATE_GUIDE = 2116;
    private static volatile OplusBracketModeManager sInstance;
    private boolean mInBracketMode;
    private IOplusBracketWindowObserver mObserver;
    private final Object mLock = new Object();
    private Rect mBracketRect = new Rect();
    private boolean mTouchModeEnable = false;
    private boolean mHasRegister = false;
    private boolean mUpdateDCS = false;
    private InnerListener mInnerListener = new InnerListener();
    private OplusActivityManager mOAms = new OplusActivityManager();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class InnerListener extends IOplusBracketModeChangedListener.Stub {
        private InnerListener() {
        }

        public void onBracketModeChanged(int type) {
            OplusBracketLog.d(OplusBracketModeManager.TAG, "onBracketModeChanged: " + OplusBracketModeManager.modeChangeReason(type));
            switch (type) {
                case 3:
                    OplusBracketModeManager.this.mTouchModeEnable = true;
                    return;
                case 4:
                    OplusBracketModeManager.this.mTouchModeEnable = false;
                    return;
                default:
                    return;
            }
        }

        public void onBracketRegionChange(Rect rect) {
        }

        public void onBindService(IOplusBracketWindowObserver oplusBracketWindowObserver) {
            OplusBracketModeManager.this.mObserver = oplusBracketWindowObserver;
            OplusBracketModeManager.this.onSurfaceViewShow(3);
        }

        public void onUnBindService(int reason) {
            OplusBracketModeManager.this.mObserver = null;
            OplusBracketModeManager.this.mUpdateDCS = false;
        }
    }

    public static OplusBracketModeManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusBracketModeManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusBracketModeManager();
                }
            }
        }
        return sInstance;
    }

    public boolean addOnConfigChangedListener(IOplusBracketModeChangedListener listener) {
        OplusActivityManager oplusActivityManager;
        OplusBracketLog.i(TAG, "addOnConfigChangedListener listener = " + listener);
        synchronized (this.mLock) {
            try {
                try {
                    oplusActivityManager = this.mOAms;
                } catch (RemoteException e) {
                    OplusBracketLog.e(TAG, "addOnConfigChangedListener remoteException ");
                }
                if (oplusActivityManager != null) {
                    return oplusActivityManager.addBracketWindowConfigChangedListener(listener);
                }
                return false;
            } finally {
            }
        }
    }

    public boolean removeOnConfigChangedListener(IOplusBracketModeChangedListener listener) {
        OplusActivityManager oplusActivityManager;
        OplusBracketLog.i(TAG, "removeOnConfigChangedListener listener = " + listener);
        synchronized (this.mLock) {
            try {
                try {
                    oplusActivityManager = this.mOAms;
                } catch (RemoteException e) {
                    OplusBracketLog.e(TAG, "removeOnConfigChangedListener remoteException ");
                }
                if (oplusActivityManager != null) {
                    return oplusActivityManager.removeBracketWindowConfigChangedListener(listener);
                }
                return false;
            } finally {
            }
        }
    }

    public boolean updateInputEventInTouchPanel(View view, InputEvent event, InputEventReceiver inputEventReceiver) {
        if (view != null && this.mTouchModeEnable && this.mInBracketMode && (view instanceof DecorView) && (event instanceof MotionEvent)) {
            MotionEvent ev = (MotionEvent) event;
            if (ev.getY() > view.getHeight()) {
                ev.setLocation(ev.getX(), ev.getY() - view.getHeight());
                ev.setTainted(true);
                updateDCSTouch();
                return false;
            }
        }
        return false;
    }

    public boolean disableOnClick(View view, InputEvent event) {
        if (view == null || !this.mTouchModeEnable || !this.mInBracketMode || !(event instanceof MotionEvent)) {
            return false;
        }
        return event.isTainted();
    }

    public void onConfigChange(View view, Configuration configuration) {
        if (view == null || !(view instanceof DecorView)) {
            return;
        }
        boolean z = configuration.windowConfiguration.getWindowingMode() == 115;
        this.mInBracketMode = z;
        if (z && !this.mHasRegister) {
            this.mBracketRect.set(configuration.windowConfiguration.getAppBounds());
        }
        if (this.mInBracketMode && !this.mHasRegister) {
            addOnConfigChangedListener(this.mInnerListener);
            this.mHasRegister = true;
        }
        if (this.mHasRegister && !this.mInBracketMode) {
            removeOnConfigChangedListener(this.mInnerListener);
            this.mHasRegister = false;
        }
    }

    public void onSurfaceViewShow(int msg) {
        IOplusBracketWindowObserver iOplusBracketWindowObserver = this.mObserver;
        if (iOplusBracketWindowObserver == null) {
            return;
        }
        try {
            iOplusBracketWindowObserver.onSurfaceViewShow(this.mInnerListener, msg);
        } catch (Exception e) {
            OplusBracketLog.d(TAG, "setSurfaceViewFlag: ");
        }
    }

    public void updateDCSTouch() {
        if (!this.mUpdateDCS) {
            onSurfaceViewShow(4);
            this.mUpdateDCS = true;
        }
    }

    public static String modeChangeReason(int reason) {
        switch (reason) {
            case 1:
                return CLIENT_ENTER_BRACKET_STR;
            case 2:
                return CLIENT_EXIT_BRACKET_STR;
            case 3:
                return CLIENT_ENABLE_TOUCH_STR;
            case 4:
                return CLIENT_DISABLE_WINODW_STR;
            case 5:
                return CLIENT_VISIBLE_TOUCH_STR;
            case 6:
                return CLIENT_INVISIBLE_TOUCH_STR;
            case 7:
                return CLIENT_ENABLE_SEPARATE_UI_STR;
            default:
                return String.valueOf(reason);
        }
    }

    public boolean isNeedDisableWindowLayoutInfo(ComponentName componentName) {
        OplusActivityManager oplusActivityManager;
        synchronized (this.mLock) {
            try {
                try {
                    oplusActivityManager = this.mOAms;
                } catch (RemoteException e) {
                    OplusBracketLog.e(TAG, "isNeedDisableWindowLayoutInfo remoteException ");
                }
                if (oplusActivityManager != null) {
                    return oplusActivityManager.isNeedDisableWindowLayoutInfo(componentName);
                }
                return false;
            } finally {
            }
        }
    }
}
