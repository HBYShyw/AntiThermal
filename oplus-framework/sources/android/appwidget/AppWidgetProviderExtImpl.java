package android.appwidget;

import android.content.Context;
import android.os.PowerManager;

/* loaded from: classes.dex */
public class AppWidgetProviderExtImpl implements IAppWidgetProviderExt {
    private PowerManager mPowerManager = null;

    public AppWidgetProviderExtImpl(Object base) {
    }

    public boolean hookUpdateWidgetWhileScreenOff(Context context) {
        if (this.mPowerManager == null) {
            this.mPowerManager = (PowerManager) context.getSystemService("power");
        }
        PowerManager powerManager = this.mPowerManager;
        return powerManager == null || powerManager.isInteractive();
    }
}
