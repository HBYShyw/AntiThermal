package android.hardware.display;

import android.view.DisplayInfo;
import com.oplus.dynamicframerate.DynamicFrameRateController;

/* loaded from: classes.dex */
public class DisplayManagerGlobalExtImpl implements IDisplayManagerGlobalExt {
    public DisplayManagerGlobalExtImpl(Object base) {
    }

    public void onHandleDisplayEvent(int displayId, int event, DisplayInfo info) {
        if (DynamicFrameRateController.hasInstance()) {
            DynamicFrameRateController.getInstance().onHandleDisplayEvent(displayId, event, info);
        }
    }
}
