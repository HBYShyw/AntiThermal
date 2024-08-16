package com.android.server.broadcastradio.aidl;

import android.hardware.broadcastradio.IBroadcastRadio;
import android.hardware.radio.IAnnouncementListener;
import android.hardware.radio.ICloseHandle;
import android.hardware.radio.ITuner;
import android.hardware.radio.ITunerCallback;
import android.hardware.radio.RadioManager;
import android.os.IBinder;
import android.os.IServiceCallback;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.server.broadcastradio.RadioServiceUserController;
import com.android.server.utils.Slogf;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class BroadcastRadioServiceImpl {
    private static final String TAG = "BcRadioAidlSrv";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final Map<String, Integer> mServiceNameToModuleIdMap = new ArrayMap();

    @GuardedBy({"mLock"})
    private final SparseArray<RadioModule> mModules = new SparseArray<>();
    private final IServiceCallback.Stub mServiceListener = new IServiceCallback.Stub() { // from class: com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.1
        public void onRegistration(String str, IBinder iBinder) {
            boolean z;
            Slogf.i(BroadcastRadioServiceImpl.TAG, "onRegistration for %s", new Object[]{str});
            synchronized (BroadcastRadioServiceImpl.this.mLock) {
                Integer num = (Integer) BroadcastRadioServiceImpl.this.mServiceNameToModuleIdMap.get(str);
                if (num == null) {
                    num = Integer.valueOf(BroadcastRadioServiceImpl.this.mNextModuleId);
                    z = true;
                } else {
                    z = false;
                }
                RadioModule tryLoadingModule = RadioModule.tryLoadingModule(num.intValue(), str, iBinder);
                if (tryLoadingModule == null) {
                    Slogf.w(BroadcastRadioServiceImpl.TAG, "No module %s with id %d (HAL AIDL)", new Object[]{str, num});
                    return;
                }
                try {
                    tryLoadingModule.setInternalHalCallback();
                    if (BroadcastRadioServiceImpl.DEBUG) {
                        Slogf.d(BroadcastRadioServiceImpl.TAG, "Loaded broadcast radio module %s with id %d (HAL AIDL)", new Object[]{str, num});
                    }
                    RadioModule radioModule = (RadioModule) BroadcastRadioServiceImpl.this.mModules.get(num.intValue());
                    BroadcastRadioServiceImpl.this.mModules.put(num.intValue(), tryLoadingModule);
                    if (radioModule != null) {
                        radioModule.closeSessions(0);
                    }
                    if (z) {
                        BroadcastRadioServiceImpl.this.mServiceNameToModuleIdMap.put(str, num);
                        BroadcastRadioServiceImpl.this.mNextModuleId++;
                    }
                    try {
                        tryLoadingModule.getService().asBinder().linkToDeath(new BroadcastRadioDeathRecipient(num.intValue()), num.intValue());
                    } catch (RemoteException unused) {
                        Slogf.w(BroadcastRadioServiceImpl.TAG, "Service has already died, so remove its entry from mModules.");
                        BroadcastRadioServiceImpl.this.mModules.remove(num.intValue());
                    }
                } catch (RemoteException e) {
                    Slogf.wtf(BroadcastRadioServiceImpl.TAG, e, "Broadcast radio module %s with id %d (HAL AIDL) cannot register HAL callback", new Object[]{str, num});
                }
            }
        }
    };

    @GuardedBy({"mLock"})
    private int mNextModuleId = 0;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class BroadcastRadioDeathRecipient implements IBinder.DeathRecipient {
        private final int mModuleId;

        BroadcastRadioDeathRecipient(int i) {
            this.mModuleId = i;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Slogf.i(BroadcastRadioServiceImpl.TAG, "ServiceDied for module id %d", new Object[]{Integer.valueOf(this.mModuleId)});
            synchronized (BroadcastRadioServiceImpl.this.mLock) {
                RadioModule radioModule = (RadioModule) BroadcastRadioServiceImpl.this.mModules.removeReturnOld(this.mModuleId);
                if (radioModule != null) {
                    radioModule.closeSessions(0);
                }
                for (Map.Entry entry : BroadcastRadioServiceImpl.this.mServiceNameToModuleIdMap.entrySet()) {
                    if (((Integer) entry.getValue()).intValue() == this.mModuleId) {
                        Slogf.w(BroadcastRadioServiceImpl.TAG, "Service %s died, removed RadioModule with ID %d", new Object[]{entry.getKey(), Integer.valueOf(this.mModuleId)});
                        return;
                    }
                }
            }
        }
    }

    public BroadcastRadioServiceImpl(ArrayList<String> arrayList) {
        if (DEBUG) {
            Slogf.d(TAG, "Initializing BroadcastRadioServiceImpl %s", new Object[]{IBroadcastRadio.DESCRIPTOR});
        }
        for (int i = 0; i < arrayList.size(); i++) {
            try {
                ServiceManager.registerForNotifications(arrayList.get(i), this.mServiceListener);
            } catch (RemoteException e) {
                Slogf.e(TAG, e, "failed to register for service notifications for service %s", new Object[]{arrayList.get(i)});
            }
        }
    }

    public List<RadioManager.ModuleProperties> listModules() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mModules.size());
            for (int i = 0; i < this.mModules.size(); i++) {
                arrayList.add(this.mModules.valueAt(i).getProperties());
            }
        }
        return arrayList;
    }

    public boolean hasModule(int i) {
        boolean contains;
        synchronized (this.mLock) {
            contains = this.mModules.contains(i);
        }
        return contains;
    }

    public boolean hasAnyModules() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mModules.size() != 0;
        }
        return z;
    }

    public ITuner openSession(int i, RadioManager.BandConfig bandConfig, boolean z, ITunerCallback iTunerCallback) throws RemoteException {
        if (DEBUG) {
            Slogf.d(TAG, "Open AIDL radio session");
        }
        if (!RadioServiceUserController.isCurrentOrSystemUser()) {
            Slogf.e(TAG, "Cannot open tuner on AIDL HAL client for non-current user");
            throw new IllegalStateException("Cannot open session for non-current user");
        }
        Objects.requireNonNull(iTunerCallback);
        if (!z) {
            throw new IllegalArgumentException("Non-audio sessions not supported with AIDL HAL");
        }
        synchronized (this.mLock) {
            RadioModule radioModule = this.mModules.get(i);
            if (radioModule == null) {
                Slogf.e(TAG, "Invalid module ID %d", new Object[]{Integer.valueOf(i)});
                return null;
            }
            TunerSession openSession = radioModule.openSession(iTunerCallback);
            if (bandConfig != null) {
                openSession.setConfiguration(bandConfig);
            }
            return openSession;
        }
    }

    public ICloseHandle addAnnouncementListener(int[] iArr, IAnnouncementListener iAnnouncementListener) {
        boolean z;
        if (DEBUG) {
            Slogf.d(TAG, "Add AnnouncementListener with enable types %s", new Object[]{Arrays.toString(iArr)});
        }
        AnnouncementAggregator announcementAggregator = new AnnouncementAggregator(iAnnouncementListener, this.mLock);
        synchronized (this.mLock) {
            z = false;
            for (int i = 0; i < this.mModules.size(); i++) {
                try {
                    announcementAggregator.watchModule(this.mModules.valueAt(i), iArr);
                    z = true;
                } catch (UnsupportedOperationException e) {
                    Slogf.w(TAG, e, "Announcements not supported for this module", new Object[0]);
                }
            }
        }
        if (!z) {
            Slogf.w(TAG, "There are no HAL modules that support announcements");
        }
        return announcementAggregator;
    }

    public void dumpInfo(IndentingPrintWriter indentingPrintWriter) {
        synchronized (this.mLock) {
            indentingPrintWriter.printf("Next module id available: %d\n", new Object[]{Integer.valueOf(this.mNextModuleId)});
            indentingPrintWriter.printf("ServiceName to module id map:\n", new Object[0]);
            indentingPrintWriter.increaseIndent();
            for (Map.Entry<String, Integer> entry : this.mServiceNameToModuleIdMap.entrySet()) {
                indentingPrintWriter.printf("Service name: %s, module id: %d\n", new Object[]{entry.getKey(), entry.getValue()});
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.printf("Radio modules [%d]:\n", new Object[]{Integer.valueOf(this.mModules.size())});
            indentingPrintWriter.increaseIndent();
            for (int i = 0; i < this.mModules.size(); i++) {
                indentingPrintWriter.printf("Module id=%d:\n", new Object[]{Integer.valueOf(this.mModules.keyAt(i))});
                indentingPrintWriter.increaseIndent();
                this.mModules.valueAt(i).dumpInfo(indentingPrintWriter);
                indentingPrintWriter.decreaseIndent();
            }
            indentingPrintWriter.decreaseIndent();
        }
    }
}
