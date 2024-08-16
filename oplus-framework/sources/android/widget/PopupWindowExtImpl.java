package android.widget;

import android.common.OplusFeatureCache;
import android.view.View;
import com.oplus.darkmode.IOplusDarkModeManager;

/* loaded from: classes.dex */
public class PopupWindowExtImpl implements IPopupWindowExt {
    public PopupWindowExtImpl(Object base) {
    }

    public void changeUsageForceDarkAlgorithmType(View view, int type) {
        ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).changeUsageForceDarkAlgorithmType(view, type);
    }
}
