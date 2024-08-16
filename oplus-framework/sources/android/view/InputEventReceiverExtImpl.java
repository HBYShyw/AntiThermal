package android.view;

import android.common.OplusFeatureCache;
import android.view.debug.IOplusViewDebugManager;

/* loaded from: classes.dex */
public class InputEventReceiverExtImpl implements IInputEventReceiverExt {
    public void markOnInputEvent(InputEvent event) {
        getViewDebugManager().markOnInputEvent(event);
    }

    public IOplusViewDebugManager getViewDebugManager() {
        return (IOplusViewDebugManager) OplusFeatureCache.getOrCreate(IOplusViewDebugManager.mDefault, new Object[0]);
    }
}
