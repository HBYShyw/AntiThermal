package com.android.server.broadcastradio.hal2;

import android.hardware.radio.Announcement;
import android.hardware.radio.IAnnouncementListener;
import android.hardware.radio.ICloseHandle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AnnouncementAggregator extends ICloseHandle.Stub {
    private static final String TAG = "BcRadio2Srv.AnnAggr";
    private final IBinder.DeathRecipient mDeathRecipient;

    @GuardedBy({"mLock"})
    private boolean mIsClosed;
    private final IAnnouncementListener mListener;
    private final Object mLock;

    @GuardedBy({"mLock"})
    private final Collection<ModuleWatcher> mModuleWatchers;

    public AnnouncementAggregator(IAnnouncementListener iAnnouncementListener, Object obj) {
        DeathRecipient deathRecipient = new DeathRecipient();
        this.mDeathRecipient = deathRecipient;
        this.mModuleWatchers = new ArrayList();
        this.mIsClosed = false;
        Objects.requireNonNull(iAnnouncementListener);
        this.mListener = iAnnouncementListener;
        Objects.requireNonNull(obj);
        this.mLock = obj;
        try {
            iAnnouncementListener.asBinder().linkToDeath(deathRecipient, 0);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class ModuleWatcher extends IAnnouncementListener.Stub {
        public List<Announcement> currentList;
        private ICloseHandle mCloseHandle;

        private ModuleWatcher() {
            this.currentList = new ArrayList();
        }

        public void onListUpdated(List<Announcement> list) {
            Objects.requireNonNull(list);
            this.currentList = list;
            AnnouncementAggregator.this.onListUpdated();
        }

        public void setCloseHandle(ICloseHandle iCloseHandle) {
            Objects.requireNonNull(iCloseHandle);
            this.mCloseHandle = iCloseHandle;
        }

        public void close() throws RemoteException {
            ICloseHandle iCloseHandle = this.mCloseHandle;
            if (iCloseHandle != null) {
                iCloseHandle.close();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class DeathRecipient implements IBinder.DeathRecipient {
        private DeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            try {
                AnnouncementAggregator.this.close();
            } catch (RemoteException unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onListUpdated() {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                Slog.e(TAG, "Announcement aggregator is closed, it shouldn't receive callbacks");
                return;
            }
            ArrayList arrayList = new ArrayList();
            Iterator<ModuleWatcher> it = this.mModuleWatchers.iterator();
            while (it.hasNext()) {
                arrayList.addAll(it.next().currentList);
            }
            try {
                this.mListener.onListUpdated(arrayList);
            } catch (RemoteException e) {
                Slog.e(TAG, "mListener.onListUpdated() failed: ", e);
            }
        }
    }

    public void watchModule(RadioModule radioModule, int[] iArr) {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                throw new IllegalStateException("Failed to watch modulesince announcement aggregator has already been closed");
            }
            ModuleWatcher moduleWatcher = new ModuleWatcher();
            try {
                moduleWatcher.setCloseHandle(radioModule.addAnnouncementListener(iArr, moduleWatcher));
                this.mModuleWatchers.add(moduleWatcher);
            } catch (RemoteException e) {
                Slog.e(TAG, "Failed to add announcement listener", e);
            }
        }
    }

    public void close() throws RemoteException {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                return;
            }
            this.mIsClosed = true;
            this.mListener.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
            Iterator<ModuleWatcher> it = this.mModuleWatchers.iterator();
            while (it.hasNext()) {
                it.next().close();
            }
            this.mModuleWatchers.clear();
        }
    }
}
