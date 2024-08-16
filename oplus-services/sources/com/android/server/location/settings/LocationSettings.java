package com.android.server.location.settings;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.RemoteException;
import android.util.IndentingPrintWriter;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.FgThread;
import com.android.server.location.settings.LocationSettings;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LocationSettings {
    private static final String LOCATION_DIRNAME = "location";
    private static final String LOCATION_SETTINGS_FILENAME = "settings";
    final Context mContext;

    @GuardedBy({"mUserSettings"})
    private final SparseArray<LocationUserSettingsStore> mUserSettings = new SparseArray<>(1);
    private final CopyOnWriteArrayList<LocationUserSettingsListener> mUserSettingsListeners = new CopyOnWriteArrayList<>();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface LocationUserSettingsListener {
        void onLocationUserSettingsChanged(int i, LocationUserSettings locationUserSettings, LocationUserSettings locationUserSettings2);
    }

    public LocationSettings(Context context) {
        this.mContext = context;
    }

    public final void registerLocationUserSettingsListener(LocationUserSettingsListener locationUserSettingsListener) {
        this.mUserSettingsListeners.add(locationUserSettingsListener);
    }

    public final void unregisterLocationUserSettingsListener(LocationUserSettingsListener locationUserSettingsListener) {
        this.mUserSettingsListeners.remove(locationUserSettingsListener);
    }

    protected File getUserSettingsDir(int i) {
        return Environment.getDataSystemDeDirectory(i);
    }

    protected LocationUserSettingsStore createUserSettingsStore(int i, File file) {
        return new LocationUserSettingsStore(i, file);
    }

    private LocationUserSettingsStore getUserSettingsStore(int i) {
        LocationUserSettingsStore locationUserSettingsStore;
        synchronized (this.mUserSettings) {
            locationUserSettingsStore = this.mUserSettings.get(i);
            if (locationUserSettingsStore == null) {
                locationUserSettingsStore = createUserSettingsStore(i, new File(new File(getUserSettingsDir(i), LOCATION_DIRNAME), LOCATION_SETTINGS_FILENAME));
                this.mUserSettings.put(i, locationUserSettingsStore);
            }
        }
        return locationUserSettingsStore;
    }

    public final LocationUserSettings getUserSettings(int i) {
        return getUserSettingsStore(i).get();
    }

    public final void updateUserSettings(int i, Function<LocationUserSettings, LocationUserSettings> function) {
        getUserSettingsStore(i).update(function);
    }

    public final void dump(FileDescriptor fileDescriptor, IndentingPrintWriter indentingPrintWriter, String[] strArr) {
        try {
            int[] runningUserIds = ActivityManager.getService().getRunningUserIds();
            if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
                indentingPrintWriter.print("ADAS Location Setting: ");
                indentingPrintWriter.increaseIndent();
                if (runningUserIds.length > 1) {
                    indentingPrintWriter.println();
                    for (int i : runningUserIds) {
                        indentingPrintWriter.print("[u");
                        indentingPrintWriter.print(i);
                        indentingPrintWriter.print("] ");
                        indentingPrintWriter.println(getUserSettings(i).isAdasGnssLocationEnabled());
                    }
                } else {
                    indentingPrintWriter.println(getUserSettings(runningUserIds[0]).isAdasGnssLocationEnabled());
                }
                indentingPrintWriter.decreaseIndent();
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @VisibleForTesting
    final void flushFiles() throws InterruptedException {
        synchronized (this.mUserSettings) {
            int size = this.mUserSettings.size();
            for (int i = 0; i < size; i++) {
                this.mUserSettings.valueAt(i).flushFile();
            }
        }
    }

    @VisibleForTesting
    final void deleteFiles() throws InterruptedException {
        synchronized (this.mUserSettings) {
            int size = this.mUserSettings.size();
            for (int i = 0; i < size; i++) {
                this.mUserSettings.valueAt(i).deleteFile();
            }
        }
    }

    protected final void fireListeners(int i, LocationUserSettings locationUserSettings, LocationUserSettings locationUserSettings2) {
        Iterator<LocationUserSettingsListener> it = this.mUserSettingsListeners.iterator();
        while (it.hasNext()) {
            it.next().onLocationUserSettingsChanged(i, locationUserSettings, locationUserSettings2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class LocationUserSettingsStore extends SettingsStore<LocationUserSettings> {
        protected final int mUserId;

        LocationUserSettingsStore(int i, File file) {
            super(file);
            this.mUserId = i;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.android.server.location.settings.SettingsStore
        public LocationUserSettings read(int i, DataInput dataInput) throws IOException {
            return filterSettings(LocationUserSettings.read(LocationSettings.this.mContext.getResources(), i, dataInput));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.settings.SettingsStore
        public void write(DataOutput dataOutput, LocationUserSettings locationUserSettings) throws IOException {
            locationUserSettings.write(dataOutput);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ LocationUserSettings lambda$update$0(Function function, LocationUserSettings locationUserSettings) {
            return filterSettings((LocationUserSettings) function.apply(locationUserSettings));
        }

        @Override // com.android.server.location.settings.SettingsStore
        public void update(final Function<LocationUserSettings, LocationUserSettings> function) {
            super.update(new Function() { // from class: com.android.server.location.settings.LocationSettings$LocationUserSettingsStore$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    LocationUserSettings lambda$update$0;
                    lambda$update$0 = LocationSettings.LocationUserSettingsStore.this.lambda$update$0(function, (LocationUserSettings) obj);
                    return lambda$update$0;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChange$1(LocationUserSettings locationUserSettings, LocationUserSettings locationUserSettings2) {
            LocationSettings.this.fireListeners(this.mUserId, locationUserSettings, locationUserSettings2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.settings.SettingsStore
        public void onChange(final LocationUserSettings locationUserSettings, final LocationUserSettings locationUserSettings2) {
            FgThread.getExecutor().execute(new Runnable() { // from class: com.android.server.location.settings.LocationSettings$LocationUserSettingsStore$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LocationSettings.LocationUserSettingsStore.this.lambda$onChange$1(locationUserSettings, locationUserSettings2);
                }
            });
        }

        private LocationUserSettings filterSettings(LocationUserSettings locationUserSettings) {
            return (!locationUserSettings.isAdasGnssLocationEnabled() || LocationSettings.this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) ? locationUserSettings : locationUserSettings.withAdasGnssLocationEnabled(false);
        }
    }
}
