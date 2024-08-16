package android.view;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.content.res.Configuration;
import com.oplus.util.OplusAccidentallyTouchData;

/* loaded from: classes.dex */
public interface IOplusAccidentallyTouchHelper extends IOplusCommonFeature {
    public static final IOplusAccidentallyTouchHelper DEFAULT = new IOplusAccidentallyTouchHelper() { // from class: android.view.IOplusAccidentallyTouchHelper.1
    };

    default IOplusAccidentallyTouchHelper getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusAccidentallyTouchHelper;
    }

    default void initOnAmsReady() {
    }

    default OplusAccidentallyTouchData getAccidentallyTouchData() {
        return new OplusAccidentallyTouchData();
    }

    default MotionEvent updatePointerEvent(MotionEvent event, View mView, Configuration mLastConfiguration) {
        return event;
    }

    default void updataeAccidentPreventionState(Context context, boolean enable, int rotation) {
    }

    default void initData(Context context) {
    }

    default boolean getEdgeEnable() {
        return false;
    }

    default int getEdgeT1() {
        return 10;
    }

    default int getEdgeT2() {
        return 30;
    }

    default int getOriEdgeT1() {
        return 10;
    }
}
