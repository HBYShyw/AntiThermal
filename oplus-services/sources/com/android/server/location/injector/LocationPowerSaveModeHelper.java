package com.android.server.location.injector;

import android.os.PowerManager;
import android.util.Log;
import com.android.server.location.LocationManagerService;
import com.android.server.location.eventlog.LocationEventLog;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class LocationPowerSaveModeHelper {
    private final CopyOnWriteArrayList<LocationPowerSaveModeChangedListener> mListeners = new CopyOnWriteArrayList<>();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface LocationPowerSaveModeChangedListener {
        void onLocationPowerSaveModeChanged(int i);
    }

    public abstract int getLocationPowerSaveMode();

    public final void addListener(LocationPowerSaveModeChangedListener locationPowerSaveModeChangedListener) {
        this.mListeners.add(locationPowerSaveModeChangedListener);
    }

    public final void removeListener(LocationPowerSaveModeChangedListener locationPowerSaveModeChangedListener) {
        this.mListeners.remove(locationPowerSaveModeChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void notifyLocationPowerSaveModeChanged(int i) {
        Log.d(LocationManagerService.TAG, "location power save mode is now " + PowerManager.locationPowerSaveModeToString(i));
        LocationEventLog.EVENT_LOG.logLocationPowerSaveMode(i);
        Iterator<LocationPowerSaveModeChangedListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onLocationPowerSaveModeChanged(i);
        }
    }
}
