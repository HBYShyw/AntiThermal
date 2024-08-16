package android.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import com.oplus.touchnode.OplusTouchNodeManager;
import com.oplus.util.OplusAccidentallyTouchData;
import com.oplus.util.OplusAccidentallyTouchUtils;

/* loaded from: classes.dex */
public class OplusAccidentallyTouchHelper implements IOplusAccidentallyTouchHelper {
    private static final int DIRECT_FLAG = 5;
    private static final int ENABLE_FLAG = 7;
    static final String TAG_WM = "OplusAccidentallyManager";
    private static final int WHITE_LIST_FLAG = 6;
    private static volatile OplusAccidentallyTouchHelper sInstance = null;

    public static OplusAccidentallyTouchHelper getInstance() {
        if (sInstance == null) {
            synchronized (OplusAccidentallyTouchHelper.class) {
                if (sInstance == null) {
                    sInstance = new OplusAccidentallyTouchHelper();
                }
            }
        }
        return sInstance;
    }

    @Override // android.view.IOplusAccidentallyTouchHelper
    public MotionEvent updatePointerEvent(MotionEvent event, View mView, Configuration mLastConfiguration) {
        return OplusAccidentallyTouchUtils.getInstance().updatePointerEvent(event, mView, mLastConfiguration);
    }

    @Override // android.view.IOplusAccidentallyTouchHelper
    public void initOnAmsReady() {
        OplusAccidentallyTouchUtils.getInstance().init();
    }

    @Override // android.view.IOplusAccidentallyTouchHelper
    public void initData(Context context) {
        OplusAccidentallyTouchUtils.getInstance().initData(context);
    }

    @Override // android.view.IOplusAccidentallyTouchHelper
    public OplusAccidentallyTouchData getAccidentallyTouchData() {
        return OplusAccidentallyTouchUtils.getInstance().getTouchData();
    }

    @Override // android.view.IOplusAccidentallyTouchHelper
    public void updataeAccidentPreventionState(Context context, boolean enable, int rotation) {
        String whiteList = enable ? "1" : "0";
        switch (rotation) {
            case 0:
                updateTouchPanelInfo("1", "0", whiteList);
                return;
            case 1:
                updateTouchPanelInfo("0", "1", whiteList);
                return;
            case 2:
                updateTouchPanelInfo("1", "0", whiteList);
                return;
            case 3:
                updateTouchPanelInfo("0", "2", whiteList);
                return;
            default:
                Log.w(TAG_WM, "Unknown rotation " + rotation);
                return;
        }
    }

    private void updateTouchPanelInfo(String enable, String direct, String whiteList) {
        Log.d(TAG_WM, "accidentPrevention >>> updateTouchPanelInfo: " + whiteList + "," + direct + "," + enable);
        writeTouchPanelFile(whiteList, 6);
        writeTouchPanelFile(direct, 5);
        writeTouchPanelFile(enable, 7);
    }

    private void writeTouchPanelFile(String value, int flag) {
        boolean result;
        try {
            result = OplusTouchNodeManager.getInstance().writeNodeFile(flag, value);
        } catch (Exception e) {
            Log.e(TAG_WM, "write touch panel file excption " + e);
            result = false;
            e.printStackTrace();
        }
        Log.d(TAG_WM, "accidentPrevention >>> writeNarrowFlag: " + flag + ", " + value + ", result = " + result);
    }

    @Override // android.view.IOplusAccidentallyTouchHelper
    public int getOriEdgeT1() {
        return OplusAccidentallyTouchUtils.getInstance().getOriEdgeT1();
    }

    @Override // android.view.IOplusAccidentallyTouchHelper
    public int getEdgeT2() {
        return OplusAccidentallyTouchUtils.getInstance().getEdgeT2();
    }

    @Override // android.view.IOplusAccidentallyTouchHelper
    public boolean getEdgeEnable() {
        return OplusAccidentallyTouchUtils.getInstance().getEdgeEnable();
    }

    @Override // android.view.IOplusAccidentallyTouchHelper
    public int getEdgeT1() {
        return OplusAccidentallyTouchUtils.getInstance().getEdgeT1();
    }
}
