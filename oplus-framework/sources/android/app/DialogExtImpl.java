package android.app;

import android.common.OplusFeatureCache;
import android.view.KeyEvent;
import android.view.View;
import com.oplus.darkmode.IOplusDarkModeManager;
import com.oplus.debug.InputLog;

/* loaded from: classes.dex */
public class DialogExtImpl implements IDialogExt {
    private Dialog mDialog;

    public DialogExtImpl(Object base) {
        this.mDialog = (Dialog) base;
    }

    public void changeDarkAlgorithmType(View view, int type) {
        ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).changeUsageForceDarkAlgorithmType(view, type);
    }

    public void logEvent(int level, String tag, KeyEvent event, String info) {
        if (level == 1) {
            InputLog.debugEventHandled(tag, event, info);
        } else {
            InputLog.v(tag, info + "," + event);
        }
    }
}
