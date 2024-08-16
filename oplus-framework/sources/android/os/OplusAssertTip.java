package android.os;

import android.graphics.Paint;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusAssertTip {
    private static final boolean DEBUG = false;
    private static final String PROP_ASSERT_TIP_RUNNING = "persist.sys.assert.state";
    private static final String TAG = "OplusAssertTip";
    private static OplusAssertTip sOplusAssertTipInstance = null;

    private native int setTipTextPaintAttr(int i, int i2);

    public native int getAssertMessageState();

    public native int hideAssertMessage();

    public native int showAssertMessage(String str);

    public native int testAddFunction(int i, int i2);

    private OplusAssertTip() {
    }

    public static OplusAssertTip getInstance() {
        if (sOplusAssertTipInstance == null) {
            sOplusAssertTipInstance = new OplusAssertTip();
        }
        return sOplusAssertTipInstance;
    }

    public void makeSureAssertTipServiceRunning() {
        boolean isAssertTipRunning = SystemProperties.getBoolean(PROP_ASSERT_TIP_RUNNING, false);
        if (!isAssertTipRunning) {
            SystemProperties.set(PROP_ASSERT_TIP_RUNNING, "true");
        }
    }

    public int requestShowAssertMessage(String message) {
        if (message == null || message.length() <= 0) {
            Log.w(TAG, "requestShowAssertMessage:message is empty!");
            return -1;
        }
        return showAssertMessage(message);
    }

    public int requestSetTipTextPaintAttr(int textSize) {
        if (textSize < 10) {
            Log.w(TAG, "size is too small! set larger than 10.");
            return 0;
        }
        char[] str = {'W'};
        Paint testPaint = new Paint();
        testPaint.setTextSize(textSize);
        float charWidth = testPaint.measureText(str, 0, 1);
        return setTipTextPaintAttr(textSize, (int) charWidth);
    }

    public boolean isAssertTipShowed() {
        int state = getAssertMessageState();
        if (state == 1) {
            return true;
        }
        return false;
    }
}
