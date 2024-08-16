package com.android.server.display;

import android.os.Trace;
import android.util.Slog;
import android.view.Display;
import android.view.DisplayAddress;
import com.android.internal.annotations.GuardedBy;
import com.android.server.display.DisplayAdapter;
import com.android.server.display.DisplayManagerService;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DisplayDeviceRepository implements DisplayAdapter.Listener {
    private static final Boolean DEBUG = Boolean.FALSE;
    public static final int DISPLAY_DEVICE_EVENT_ADDED = 1;
    public static final int DISPLAY_DEVICE_EVENT_REMOVED = 3;
    private static final String TAG = "DisplayDeviceRepository";
    private final PersistentDataStore mPersistentDataStore;
    private final DisplayManagerService.SyncRoot mSyncRoot;

    @GuardedBy({"mSyncRoot"})
    private final List<DisplayDevice> mDisplayDevices = new ArrayList();

    @GuardedBy({"mSyncRoot"})
    private final List<Listener> mListeners = new ArrayList();
    private IDisplayDeviceRepositoryExt mDisplayDeviceRepositoryExtImpl = (IDisplayDeviceRepositoryExt) ExtLoader.type(IDisplayDeviceRepositoryExt.class).base(this).create();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Listener {
        void onDisplayDeviceChangedLocked(DisplayDevice displayDevice, int i);

        void onDisplayDeviceEventLocked(DisplayDevice displayDevice, int i);

        void onTraversalRequested();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayDeviceRepository(DisplayManagerService.SyncRoot syncRoot, PersistentDataStore persistentDataStore) {
        this.mSyncRoot = syncRoot;
        this.mPersistentDataStore = persistentDataStore;
    }

    public void addListener(Listener listener) {
        this.mListeners.add(listener);
    }

    @Override // com.android.server.display.DisplayAdapter.Listener
    public void onDisplayDeviceEvent(DisplayDevice displayDevice, int i) {
        String str;
        Boolean bool = DEBUG;
        if (bool.booleanValue()) {
            str = "DisplayDeviceRepository#onDisplayDeviceEvent (event=" + i + ")";
            Trace.beginAsyncSection(str, 0);
        } else {
            str = null;
        }
        if (i == 1) {
            handleDisplayDeviceAdded(displayDevice);
        } else if (i == 2) {
            handleDisplayDeviceChanged(displayDevice);
        } else if (i == 3) {
            handleDisplayDeviceRemoved(displayDevice);
        }
        if (bool.booleanValue()) {
            Trace.endAsyncSection(str, 0);
        }
    }

    @Override // com.android.server.display.DisplayAdapter.Listener
    public void onDisplayDeviceEvent(DisplayDevice displayDevice, int i, long j) {
        this.mDisplayDeviceRepositoryExtImpl.onDisplayDeviceEvent(displayDevice, i, j);
    }

    @Override // com.android.server.display.DisplayAdapter.Listener
    public void onTraversalRequested() {
        int size = this.mListeners.size();
        for (int i = 0; i < size; i++) {
            this.mListeners.get(i).onTraversalRequested();
        }
    }

    public boolean containsLocked(DisplayDevice displayDevice) {
        return this.mDisplayDevices.contains(displayDevice);
    }

    public int sizeLocked() {
        return this.mDisplayDevices.size();
    }

    public void forEachLocked(Consumer<DisplayDevice> consumer) {
        int size = this.mDisplayDevices.size();
        for (int i = 0; i < size; i++) {
            consumer.accept(this.mDisplayDevices.get(i));
        }
    }

    public DisplayDevice getByAddressLocked(DisplayAddress displayAddress) {
        for (int size = this.mDisplayDevices.size() - 1; size >= 0; size--) {
            DisplayDevice displayDevice = this.mDisplayDevices.get(size);
            if (displayAddress.equals(displayDevice.getDisplayDeviceInfoLocked().address)) {
                return displayDevice;
            }
        }
        return null;
    }

    public DisplayDevice getByUniqueIdLocked(String str) {
        for (int size = this.mDisplayDevices.size() - 1; size >= 0; size--) {
            DisplayDevice displayDevice = this.mDisplayDevices.get(size);
            if (displayDevice.getUniqueId().equals(str)) {
                return displayDevice;
            }
        }
        return null;
    }

    private void handleDisplayDeviceAdded(DisplayDevice displayDevice) {
        synchronized (this.mSyncRoot) {
            DisplayDeviceInfo displayDeviceInfoLocked = displayDevice.getDisplayDeviceInfoLocked();
            if (this.mDisplayDevices.contains(displayDevice)) {
                Slog.w(TAG, "Attempted to add already added display device: " + displayDeviceInfoLocked);
                return;
            }
            Slog.i(TAG, "Display device added: " + displayDeviceInfoLocked);
            if (displayDeviceInfoLocked != null) {
                this.mDisplayDeviceRepositoryExtImpl.handleDisplayDeviceAdded(displayDeviceInfoLocked.ownerPackageName, displayDeviceInfoLocked.ownerUid);
            }
            if (displayDeviceInfoLocked == null || displayDeviceInfoLocked.ownerPackageName == null || !this.mDisplayDeviceRepositoryExtImpl.interceptDisplayDeviceAdded(this.mDisplayDevices, displayDeviceInfoLocked)) {
                displayDevice.mDebugLastLoggedDeviceInfo = displayDeviceInfoLocked;
                this.mDisplayDeviceRepositoryExtImpl.handleDisplayDeviceAdded(displayDevice);
                this.mDisplayDevices.add(displayDevice);
                sendEventLocked(displayDevice, 1);
            }
        }
    }

    private void handleDisplayDeviceChanged(DisplayDevice displayDevice) {
        synchronized (this.mSyncRoot) {
            DisplayDeviceInfo displayDeviceInfoLocked = displayDevice.getDisplayDeviceInfoLocked();
            if (!this.mDisplayDevices.contains(displayDevice)) {
                Slog.w(TAG, "Attempted to change non-existent display device: " + displayDeviceInfoLocked);
                return;
            }
            Boolean bool = DEBUG;
            if (bool.booleanValue()) {
                Trace.traceBegin(131072L, "handleDisplayDeviceChanged");
            }
            int diff = displayDevice.mDebugLastLoggedDeviceInfo.diff(displayDeviceInfoLocked);
            if (diff == 1) {
                Slog.i(TAG, "Display device changed state: \"" + displayDeviceInfoLocked.name + "\", " + Display.stateToString(displayDeviceInfoLocked.state));
            } else if (diff != 8) {
                Slog.i(TAG, "Display device changed: " + displayDeviceInfoLocked);
            }
            if ((diff & 4) != 0) {
                try {
                    this.mPersistentDataStore.setColorMode(displayDevice, displayDeviceInfoLocked.colorMode);
                    this.mPersistentDataStore.saveIfNeeded();
                } catch (Throwable th) {
                    this.mPersistentDataStore.saveIfNeeded();
                    throw th;
                }
            }
            displayDevice.mDebugLastLoggedDeviceInfo = displayDeviceInfoLocked;
            displayDevice.applyPendingDisplayDeviceInfoChangesLocked();
            sendChangedEventLocked(displayDevice, diff);
            if (displayDeviceInfoLocked != null && displayDeviceInfoLocked.state == 1) {
                this.mDisplayDeviceRepositoryExtImpl.handleDisplayDeviceRemoved(displayDeviceInfoLocked.ownerPackageName, displayDeviceInfoLocked.ownerUid);
            }
            this.mDisplayDeviceRepositoryExtImpl.handleDisplayDeviceChanged(displayDevice);
            if (bool.booleanValue()) {
                Trace.traceEnd(131072L);
            }
        }
    }

    private void handleDisplayDeviceRemoved(DisplayDevice displayDevice) {
        synchronized (this.mSyncRoot) {
            DisplayDeviceInfo displayDeviceInfoLocked = displayDevice.getDisplayDeviceInfoLocked();
            if (!this.mDisplayDevices.remove(displayDevice)) {
                Slog.w(TAG, "Attempted to remove non-existent display device: " + displayDeviceInfoLocked);
                return;
            }
            Slog.i(TAG, "Display device removed: " + displayDeviceInfoLocked);
            this.mDisplayDeviceRepositoryExtImpl.onDisplayRemoved(displayDevice);
            if (displayDeviceInfoLocked != null) {
                this.mDisplayDeviceRepositoryExtImpl.handleDisplayDeviceRemoved(displayDeviceInfoLocked.ownerPackageName, displayDeviceInfoLocked.ownerUid);
            }
            this.mDisplayDeviceRepositoryExtImpl.handleDisplayDeviceRemoved(displayDevice);
            displayDevice.mDebugLastLoggedDeviceInfo = displayDeviceInfoLocked;
            sendEventLocked(displayDevice, 3);
        }
    }

    private void sendEventLocked(DisplayDevice displayDevice, int i) {
        int size = this.mListeners.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.mListeners.get(i2).onDisplayDeviceEventLocked(displayDevice, i);
        }
    }

    @GuardedBy({"mSyncRoot"})
    private void sendChangedEventLocked(DisplayDevice displayDevice, int i) {
        int size = this.mListeners.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.mListeners.get(i2).onDisplayDeviceChangedLocked(displayDevice, i);
        }
    }
}
