package com.android.server.policy;

import android.R;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SingleKeyGestureDetector {
    private static final int MSG_KEY_DELAYED_PRESS = 2;
    private static final int MSG_KEY_LONG_PRESS = 0;
    private static final int MSG_KEY_VERY_LONG_PRESS = 1;
    private static final String TAG = "SingleKeyGesture";
    static long sDefaultLongPressTimeout;
    static long sDefaultVeryLongPressTimeout;
    private int mKeyPressCounter;
    private static final boolean DEBUG = PhoneWindowManager.DEBUG_INPUT;
    static final long MULTI_PRESS_TIMEOUT = ViewConfiguration.getMultiPressTimeout();
    private boolean mBeganFromNonInteractive = false;
    private final ArrayList<SingleKeyRule> mRules = new ArrayList<>();
    private SingleKeyRule mActiveRule = null;
    private int mDownKeyCode = 0;
    private boolean mHandledByLongPress = false;
    private long mLastDownTime = 0;
    private final Handler mHandler = new KeyHandler();
    ISingleKeyGestureDetectorExt mSingleKeyGestureDetectorExt = (ISingleKeyGestureDetectorExt) ExtLoader.type(ISingleKeyGestureDetectorExt.class).create();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static abstract class SingleKeyRule {
        private final int mKeyCode;

        int getMaxMultiPressCount() {
            return 1;
        }

        void onLongPress(long j) {
        }

        void onMultiPress(long j, int i) {
        }

        abstract void onPress(long j);

        void onVeryLongPress(long j) {
        }

        boolean supportLongPress() {
            return false;
        }

        boolean supportVeryLongPress() {
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public SingleKeyRule(int i) {
            this.mKeyCode = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean shouldInterceptKey(int i) {
            return i == this.mKeyCode;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long getLongPressTimeoutMs() {
            return SingleKeyGestureDetector.sDefaultLongPressTimeout;
        }

        long getVeryLongPressTimeoutMs() {
            return SingleKeyGestureDetector.sDefaultVeryLongPressTimeout;
        }

        public String toString() {
            return "KeyCode=" + KeyEvent.keyCodeToString(this.mKeyCode) + ", LongPress=" + supportLongPress() + ", VeryLongPress=" + supportVeryLongPress() + ", MaxMultiPressCount=" + getMaxMultiPressCount();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return (obj instanceof SingleKeyRule) && this.mKeyCode == ((SingleKeyRule) obj).mKeyCode;
        }

        public int hashCode() {
            return this.mKeyCode;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SingleKeyGestureDetector get(Context context) {
        SingleKeyGestureDetector singleKeyGestureDetector = new SingleKeyGestureDetector();
        sDefaultLongPressTimeout = context.getResources().getInteger(R.integer.config_networkAvoidBadWifi);
        sDefaultVeryLongPressTimeout = context.getResources().getInteger(R.integer.preference_fragment_scrollbarStyle);
        return singleKeyGestureDetector;
    }

    private SingleKeyGestureDetector() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addRule(SingleKeyRule singleKeyRule) {
        if (this.mRules.contains(singleKeyRule)) {
            throw new IllegalArgumentException("Rule : " + singleKeyRule + " already exists.");
        }
        this.mRules.add(singleKeyRule);
    }

    void removeRule(SingleKeyRule singleKeyRule) {
        this.mRules.remove(singleKeyRule);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void interceptKey(KeyEvent keyEvent, boolean z) {
        if (keyEvent.getAction() == 0) {
            int i = this.mDownKeyCode;
            if (i == 0 || i != keyEvent.getKeyCode()) {
                this.mBeganFromNonInteractive = !z;
            }
            interceptKeyDown(keyEvent);
            return;
        }
        interceptKeyUp(keyEvent);
    }

    private void interceptKeyDown(KeyEvent keyEvent) {
        SingleKeyRule singleKeyRule;
        int keyCode = keyEvent.getKeyCode();
        int i = this.mDownKeyCode;
        if (i == keyCode) {
            if (this.mActiveRule == null || (keyEvent.getFlags() & 128) == 0 || !this.mActiveRule.supportLongPress() || this.mHandledByLongPress) {
                return;
            }
            if (DEBUG) {
                Log.i(TAG, "Long press key " + KeyEvent.keyCodeToString(keyCode));
            }
            this.mHandledByLongPress = true;
            this.mHandler.removeMessages(0);
            this.mHandler.removeMessages(1);
            Message obtainMessage = this.mHandler.obtainMessage(0, keyCode, 0, this.mActiveRule);
            obtainMessage.setAsynchronous(true);
            this.mHandler.sendMessage(obtainMessage);
            return;
        }
        if (i != 0 || ((singleKeyRule = this.mActiveRule) != null && !singleKeyRule.shouldInterceptKey(keyCode))) {
            if (DEBUG) {
                Log.i(TAG, "Press another key " + KeyEvent.keyCodeToString(keyCode));
            }
            reset();
        }
        this.mDownKeyCode = keyCode;
        if (this.mActiveRule == null) {
            int size = this.mRules.size();
            int i2 = 0;
            while (true) {
                if (i2 >= size) {
                    break;
                }
                SingleKeyRule singleKeyRule2 = this.mRules.get(i2);
                if (singleKeyRule2.shouldInterceptKey(keyCode)) {
                    if (DEBUG) {
                        Log.i(TAG, "Intercept key by rule " + singleKeyRule2);
                    }
                    this.mActiveRule = singleKeyRule2;
                } else {
                    i2++;
                }
            }
            this.mLastDownTime = 0L;
        }
        if (this.mActiveRule == null) {
            return;
        }
        long downTime = keyEvent.getDownTime() - this.mLastDownTime;
        this.mLastDownTime = keyEvent.getDownTime();
        if (downTime >= MULTI_PRESS_TIMEOUT) {
            this.mKeyPressCounter = 1;
        } else {
            this.mKeyPressCounter++;
        }
        if (this.mKeyPressCounter == 1) {
            if (this.mActiveRule.supportLongPress()) {
                Message obtainMessage2 = this.mHandler.obtainMessage(0, keyCode, 0, this.mActiveRule);
                obtainMessage2.setAsynchronous(true);
                this.mHandler.sendMessageDelayed(obtainMessage2, this.mActiveRule.getLongPressTimeoutMs());
            }
            if (this.mActiveRule.supportVeryLongPress()) {
                Message obtainMessage3 = this.mHandler.obtainMessage(1, keyCode, 0, this.mActiveRule);
                obtainMessage3.setAsynchronous(true);
                this.mHandler.sendMessageDelayed(obtainMessage3, this.mSingleKeyGestureDetectorExt.modifyPressTimeout(1, this.mActiveRule.getVeryLongPressTimeoutMs(), keyEvent));
                return;
            }
            return;
        }
        this.mHandler.removeMessages(0);
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        if (this.mActiveRule.getMaxMultiPressCount() <= 1 || this.mKeyPressCounter != this.mActiveRule.getMaxMultiPressCount()) {
            return;
        }
        if (DEBUG) {
            Log.i(TAG, "Trigger multi press " + this.mActiveRule.toString() + " for it reached the max count " + this.mKeyPressCounter);
        }
        Message obtainMessage4 = this.mHandler.obtainMessage(2, keyCode, this.mKeyPressCounter, this.mActiveRule);
        obtainMessage4.setAsynchronous(true);
        this.mHandler.sendMessage(obtainMessage4);
    }

    private boolean interceptKeyUp(KeyEvent keyEvent) {
        this.mDownKeyCode = 0;
        if (this.mActiveRule == null) {
            return false;
        }
        if (!this.mHandledByLongPress) {
            long eventTime = keyEvent.getEventTime();
            if (eventTime < this.mLastDownTime + this.mActiveRule.getLongPressTimeoutMs()) {
                this.mHandler.removeMessages(0);
            } else {
                this.mHandledByLongPress = this.mActiveRule.supportLongPress();
            }
            if (eventTime < this.mLastDownTime + this.mActiveRule.getVeryLongPressTimeoutMs()) {
                this.mHandler.removeMessages(1);
            } else {
                this.mHandledByLongPress = this.mActiveRule.supportVeryLongPress();
            }
        }
        if (this.mHandledByLongPress) {
            this.mHandledByLongPress = false;
            this.mKeyPressCounter = 0;
            this.mActiveRule = null;
            return true;
        }
        if (keyEvent.getKeyCode() == this.mActiveRule.mKeyCode) {
            if (this.mActiveRule.getMaxMultiPressCount() == 1) {
                if (DEBUG) {
                    Log.i(TAG, "press key " + KeyEvent.keyCodeToString(keyEvent.getKeyCode()));
                }
                Message obtainMessage = this.mHandler.obtainMessage(2, this.mActiveRule.mKeyCode, 1, this.mActiveRule);
                obtainMessage.setAsynchronous(true);
                this.mHandler.sendMessage(obtainMessage);
                this.mActiveRule = null;
                return true;
            }
            if (this.mKeyPressCounter < this.mActiveRule.getMaxMultiPressCount()) {
                Message obtainMessage2 = this.mHandler.obtainMessage(2, this.mActiveRule.mKeyCode, this.mKeyPressCounter, this.mActiveRule);
                obtainMessage2.setAsynchronous(true);
                this.mHandler.sendMessageDelayed(obtainMessage2, MULTI_PRESS_TIMEOUT);
            }
            this.mSingleKeyGestureDetectorExt.endHookInterceptKeyUp();
            return true;
        }
        reset();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getKeyPressCounter(int i) {
        SingleKeyRule singleKeyRule = this.mActiveRule;
        if (singleKeyRule == null || singleKeyRule.mKeyCode != i) {
            return 0;
        }
        return this.mKeyPressCounter;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset() {
        if (this.mActiveRule != null) {
            if (this.mDownKeyCode != 0) {
                this.mHandler.removeMessages(0);
                this.mHandler.removeMessages(1);
            }
            if (this.mKeyPressCounter > 0) {
                this.mHandler.removeMessages(2);
                this.mKeyPressCounter = 0;
            }
            this.mActiveRule = null;
        }
        this.mHandledByLongPress = false;
        this.mDownKeyCode = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isKeyIntercepted(int i) {
        SingleKeyRule singleKeyRule = this.mActiveRule;
        return singleKeyRule != null && singleKeyRule.shouldInterceptKey(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean beganFromNonInteractive() {
        return this.mBeganFromNonInteractive;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(String str, PrintWriter printWriter) {
        printWriter.println(str + "SingleKey rules:");
        Iterator<SingleKeyRule> it = this.mRules.iterator();
        while (it.hasNext()) {
            printWriter.println(str + "  " + it.next());
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class KeyHandler extends Handler {
        KeyHandler() {
            super(Looper.myLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SingleKeyRule singleKeyRule = (SingleKeyRule) message.obj;
            if (singleKeyRule == null) {
                Log.wtf(SingleKeyGestureDetector.TAG, "No active rule.");
                return;
            }
            int i = message.arg1;
            int i2 = message.arg2;
            int i3 = message.what;
            if (i3 == 0) {
                if (SingleKeyGestureDetector.DEBUG) {
                    Log.i(SingleKeyGestureDetector.TAG, "Detect long press " + KeyEvent.keyCodeToString(i));
                }
                singleKeyRule.onLongPress(SingleKeyGestureDetector.this.mLastDownTime);
                return;
            }
            if (i3 == 1) {
                if (SingleKeyGestureDetector.DEBUG) {
                    Log.i(SingleKeyGestureDetector.TAG, "Detect very long press " + KeyEvent.keyCodeToString(i));
                }
                singleKeyRule.onVeryLongPress(SingleKeyGestureDetector.this.mLastDownTime);
                return;
            }
            if (i3 != 2) {
                return;
            }
            if (SingleKeyGestureDetector.DEBUG) {
                Log.i(SingleKeyGestureDetector.TAG, "Detect press " + KeyEvent.keyCodeToString(i) + ", count " + i2);
            }
            if (i2 == 1) {
                singleKeyRule.onPress(SingleKeyGestureDetector.this.mLastDownTime);
            } else {
                singleKeyRule.onMultiPress(SingleKeyGestureDetector.this.mLastDownTime, i2);
            }
        }
    }
}
