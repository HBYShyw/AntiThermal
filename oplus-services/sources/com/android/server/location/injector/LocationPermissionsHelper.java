package com.android.server.location.injector;

import android.location.util.identity.CallerIdentity;
import com.android.server.location.LocationPermissions;
import com.android.server.location.injector.AppOpsHelper;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class LocationPermissionsHelper {
    private final AppOpsHelper mAppOps;
    private final CopyOnWriteArrayList<LocationPermissionsListener> mListeners = new CopyOnWriteArrayList<>();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface LocationPermissionsListener {
        void onLocationPermissionsChanged(int i);

        void onLocationPermissionsChanged(String str);
    }

    protected abstract boolean hasPermission(String str, CallerIdentity callerIdentity);

    public LocationPermissionsHelper(AppOpsHelper appOpsHelper) {
        this.mAppOps = appOpsHelper;
        appOpsHelper.addListener(new AppOpsHelper.LocationAppOpListener() { // from class: com.android.server.location.injector.LocationPermissionsHelper$$ExternalSyntheticLambda0
            @Override // com.android.server.location.injector.AppOpsHelper.LocationAppOpListener
            public final void onAppOpsChanged(String str) {
                LocationPermissionsHelper.this.onAppOpsChanged(str);
            }
        });
    }

    protected final void notifyLocationPermissionsChanged(String str) {
        Iterator<LocationPermissionsListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onLocationPermissionsChanged(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void notifyLocationPermissionsChanged(int i) {
        Iterator<LocationPermissionsListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onLocationPermissionsChanged(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAppOpsChanged(String str) {
        notifyLocationPermissionsChanged(str);
    }

    public final void addListener(LocationPermissionsListener locationPermissionsListener) {
        this.mListeners.add(locationPermissionsListener);
    }

    public final void removeListener(LocationPermissionsListener locationPermissionsListener) {
        this.mListeners.remove(locationPermissionsListener);
    }

    public final boolean hasLocationPermissions(int i, CallerIdentity callerIdentity) {
        if (i != 0 && hasPermission(LocationPermissions.asPermission(i), callerIdentity)) {
            return this.mAppOps.checkOpNoThrow(LocationPermissions.asAppOp(i), callerIdentity);
        }
        return false;
    }
}
