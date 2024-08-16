package com.android.server.broadcastradio.aidl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.broadcastradio.AmFmRegionConfig;
import android.hardware.broadcastradio.Announcement;
import android.hardware.broadcastradio.DabTableEntry;
import android.hardware.broadcastradio.IAnnouncementListener;
import android.hardware.broadcastradio.IBroadcastRadio;
import android.hardware.broadcastradio.ITunerCallback;
import android.hardware.broadcastradio.ProgramInfo;
import android.hardware.broadcastradio.ProgramListChunk;
import android.hardware.broadcastradio.ProgramSelector;
import android.hardware.broadcastradio.VendorKeyValue;
import android.hardware.radio.IAnnouncementListener;
import android.hardware.radio.ICloseHandle;
import android.hardware.radio.ProgramList;
import android.hardware.radio.RadioManager;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.broadcastradio.RadioServiceUserController;
import com.android.server.broadcastradio.aidl.RadioModule;
import com.android.server.utils.Slogf;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class RadioModule {
    private static final int RADIO_EVENT_LOGGER_QUEUE_SIZE = 25;
    private static final String TAG = "BcRadioAidlSrv.module";

    @GuardedBy({"mLock"})
    private Boolean mAntennaConnected;

    @GuardedBy({"mLock"})
    private RadioManager.ProgramInfo mCurrentProgramInfo;
    private final Handler mHandler;
    private final RadioLogger mLogger;
    private final RadioManager.ModuleProperties mProperties;
    private final IBroadcastRadio mService;

    @GuardedBy({"mLock"})
    private ProgramList.Filter mUnionOfAidlProgramFilters;
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final ProgramInfoCache mProgramInfoCache = new ProgramInfoCache(null);

    @GuardedBy({"mLock"})
    private final ArraySet<TunerSession> mAidlTunerSessions = new ArraySet<>();
    private final ITunerCallback mHalTunerCallback = new AnonymousClass1();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface AidlCallbackRunnable {
        void run(android.hardware.radio.ITunerCallback iTunerCallback, int i) throws RemoteException;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.broadcastradio.aidl.RadioModule$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass1 extends ITunerCallback.Stub {
        @Override // android.hardware.broadcastradio.ITunerCallback
        public String getInterfaceHash() {
            return "3c864ddf392d28cfbf95849bedf0b753b81cc013";
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public int getInterfaceVersion() {
            return 1;
        }

        AnonymousClass1() {
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onTuneFailed(final int i, final ProgramSelector programSelector) {
            RadioModule.this.fireLater(new Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    RadioModule.AnonymousClass1.this.lambda$onTuneFailed$1(programSelector, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTuneFailed$1(ProgramSelector programSelector, int i) {
            final android.hardware.radio.ProgramSelector programSelectorFromHalProgramSelector = ConversionUtils.programSelectorFromHalProgramSelector(programSelector);
            final int halResultToTunerResult = ConversionUtils.halResultToTunerResult(i);
            synchronized (RadioModule.this.mLock) {
                RadioModule.this.fanoutAidlCallbackLocked(new AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda5
                    @Override // com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable
                    public final void run(android.hardware.radio.ITunerCallback iTunerCallback, int i2) {
                        RadioModule.AnonymousClass1.lambda$onTuneFailed$0(programSelectorFromHalProgramSelector, halResultToTunerResult, iTunerCallback, i2);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onTuneFailed$0(android.hardware.radio.ProgramSelector programSelector, int i, android.hardware.radio.ITunerCallback iTunerCallback, int i2) throws RemoteException {
            if (programSelector != null && !ConversionUtils.programSelectorMeetsSdkVersionRequirement(programSelector, i2)) {
                Slogf.e(RadioModule.TAG, "onTuneFailed: cannot send program selector requiring higher target SDK version");
            } else {
                iTunerCallback.onTuneFailed(i, programSelector);
            }
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onCurrentProgramInfoChanged(final ProgramInfo programInfo) {
            RadioModule.this.fireLater(new Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    RadioModule.AnonymousClass1.this.lambda$onCurrentProgramInfoChanged$3(programInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCurrentProgramInfoChanged$3(ProgramInfo programInfo) {
            final RadioManager.ProgramInfo programInfoFromHalProgramInfo = ConversionUtils.programInfoFromHalProgramInfo(programInfo);
            Objects.requireNonNull(programInfoFromHalProgramInfo, "Program info from AIDL HAL is invalid");
            synchronized (RadioModule.this.mLock) {
                RadioModule.this.mCurrentProgramInfo = programInfoFromHalProgramInfo;
                RadioModule.this.fanoutAidlCallbackLocked(new AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda1
                    @Override // com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable
                    public final void run(android.hardware.radio.ITunerCallback iTunerCallback, int i) {
                        RadioModule.AnonymousClass1.lambda$onCurrentProgramInfoChanged$2(programInfoFromHalProgramInfo, iTunerCallback, i);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onCurrentProgramInfoChanged$2(RadioManager.ProgramInfo programInfo, android.hardware.radio.ITunerCallback iTunerCallback, int i) throws RemoteException {
            if (!ConversionUtils.programInfoMeetsSdkVersionRequirement(programInfo, i)) {
                Slogf.e(RadioModule.TAG, "onCurrentProgramInfoChanged: cannot send program info requiring higher target SDK version");
            } else {
                iTunerCallback.onCurrentProgramInfoChanged(programInfo);
            }
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onProgramListUpdated(final ProgramListChunk programListChunk) {
            RadioModule.this.fireLater(new Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    RadioModule.AnonymousClass1.this.lambda$onProgramListUpdated$4(programListChunk);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onProgramListUpdated$4(ProgramListChunk programListChunk) {
            synchronized (RadioModule.this.mLock) {
                ProgramList.Chunk chunkFromHalProgramListChunk = ConversionUtils.chunkFromHalProgramListChunk(programListChunk);
                RadioModule.this.mProgramInfoCache.filterAndApplyChunk(chunkFromHalProgramListChunk);
                for (int i = 0; i < RadioModule.this.mAidlTunerSessions.size(); i++) {
                    ((TunerSession) RadioModule.this.mAidlTunerSessions.valueAt(i)).onMergedProgramListUpdateFromHal(chunkFromHalProgramListChunk);
                }
            }
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onAntennaStateChange(final boolean z) {
            RadioModule.this.fireLater(new Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    RadioModule.AnonymousClass1.this.lambda$onAntennaStateChange$6(z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAntennaStateChange$6(final boolean z) {
            synchronized (RadioModule.this.mLock) {
                RadioModule.this.mAntennaConnected = Boolean.valueOf(z);
                RadioModule.this.fanoutAidlCallbackLocked(new AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda7
                    @Override // com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable
                    public final void run(android.hardware.radio.ITunerCallback iTunerCallback, int i) {
                        iTunerCallback.onAntennaState(z);
                    }
                });
            }
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onConfigFlagUpdated(final int i, final boolean z) {
            RadioModule.this.fireLater(new Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    RadioModule.AnonymousClass1.this.lambda$onConfigFlagUpdated$8(i, z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigFlagUpdated$8(final int i, final boolean z) {
            synchronized (RadioModule.this.mLock) {
                RadioModule.this.fanoutAidlCallbackLocked(new AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda3
                    @Override // com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable
                    public final void run(android.hardware.radio.ITunerCallback iTunerCallback, int i2) {
                        iTunerCallback.onConfigFlagUpdated(i, z);
                    }
                });
            }
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onParametersUpdated(final VendorKeyValue[] vendorKeyValueArr) {
            RadioModule.this.fireLater(new Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    RadioModule.AnonymousClass1.this.lambda$onParametersUpdated$10(vendorKeyValueArr);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onParametersUpdated$10(VendorKeyValue[] vendorKeyValueArr) {
            synchronized (RadioModule.this.mLock) {
                final Map<String, String> vendorInfoFromHalVendorKeyValues = ConversionUtils.vendorInfoFromHalVendorKeyValues(vendorKeyValueArr);
                RadioModule.this.fanoutAidlCallbackLocked(new AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda2
                    @Override // com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable
                    public final void run(android.hardware.radio.ITunerCallback iTunerCallback, int i) {
                        iTunerCallback.onParametersUpdated(vendorInfoFromHalVendorKeyValues);
                    }
                });
            }
        }
    }

    @VisibleForTesting
    RadioModule(IBroadcastRadio iBroadcastRadio, RadioManager.ModuleProperties moduleProperties) {
        Objects.requireNonNull(moduleProperties, "properties cannot be null");
        this.mProperties = moduleProperties;
        Objects.requireNonNull(iBroadcastRadio, "service cannot be null");
        this.mService = iBroadcastRadio;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mLogger = new RadioLogger(TAG, 25);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RadioModule tryLoadingModule(int i, String str, IBinder iBinder) {
        AmFmRegionConfig amFmRegionConfig;
        DabTableEntry[] dabTableEntryArr;
        try {
            Slogf.i(TAG, "Try loading module for module id = %d, module name = %s", new Object[]{Integer.valueOf(i), str});
            IBroadcastRadio asInterface = IBroadcastRadio.Stub.asInterface(iBinder);
            if (asInterface == null) {
                Slogf.w(TAG, "Module %s is null", new Object[]{str});
                return null;
            }
            try {
                amFmRegionConfig = asInterface.getAmFmRegionConfig(false);
            } catch (RuntimeException unused) {
                Slogf.i(TAG, "Module %s does not has AMFM config", new Object[]{str});
                amFmRegionConfig = null;
            }
            try {
                dabTableEntryArr = asInterface.getDabRegionConfig();
            } catch (RuntimeException unused2) {
                Slogf.i(TAG, "Module %s does not has DAB config", new Object[]{str});
                dabTableEntryArr = null;
            }
            return new RadioModule(asInterface, ConversionUtils.propertiesFromHalProperties(i, str, asInterface.getProperties(), amFmRegionConfig, dabTableEntryArr));
        } catch (RemoteException e) {
            Slogf.e(TAG, e, "Failed to load module %s", new Object[]{str});
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBroadcastRadio getService() {
        return this.mService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RadioManager.ModuleProperties getProperties() {
        return this.mProperties;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInternalHalCallback() throws RemoteException {
        this.mService.setTunerCallback(this.mHalTunerCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TunerSession openSession(android.hardware.radio.ITunerCallback iTunerCallback) throws RemoteException {
        TunerSession tunerSession;
        Boolean bool;
        RadioManager.ProgramInfo programInfo;
        this.mLogger.logRadioEvent("Open TunerSession", new Object[0]);
        synchronized (this.mLock) {
            tunerSession = new TunerSession(this, this.mService, iTunerCallback);
            this.mAidlTunerSessions.add(tunerSession);
            bool = this.mAntennaConnected;
            programInfo = this.mCurrentProgramInfo;
        }
        if (bool != null) {
            iTunerCallback.onAntennaState(bool.booleanValue());
        }
        if (programInfo != null) {
            iTunerCallback.onCurrentProgramInfoChanged(programInfo);
        }
        return tunerSession;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeSessions(int i) {
        int size;
        TunerSession[] tunerSessionArr;
        this.mLogger.logRadioEvent("Close TunerSessions %d", Integer.valueOf(i));
        synchronized (this.mLock) {
            size = this.mAidlTunerSessions.size();
            tunerSessionArr = new TunerSession[size];
            this.mAidlTunerSessions.toArray(tunerSessionArr);
            this.mAidlTunerSessions.clear();
        }
        for (int i2 = 0; i2 < size; i2++) {
            TunerSession tunerSession = tunerSessionArr[i2];
            try {
                tunerSession.close(Integer.valueOf(i));
            } catch (Exception e) {
                Slogf.e(TAG, "Failed to close TunerSession %s: %s", new Object[]{tunerSession, e});
            }
        }
    }

    @GuardedBy({"mLock"})
    private ProgramList.Filter buildUnionOfTunerSessionFiltersLocked() {
        ArraySet arraySet = null;
        ArraySet arraySet2 = null;
        boolean z = true;
        boolean z2 = false;
        for (int i = 0; i < this.mAidlTunerSessions.size(); i++) {
            ProgramList.Filter programListFilter = this.mAidlTunerSessions.valueAt(i).getProgramListFilter();
            if (programListFilter != null) {
                if (arraySet == null) {
                    arraySet = new ArraySet(programListFilter.getIdentifierTypes());
                    arraySet2 = new ArraySet(programListFilter.getIdentifiers());
                    z2 = programListFilter.areCategoriesIncluded();
                    z = programListFilter.areModificationsExcluded();
                } else {
                    if (!arraySet.isEmpty()) {
                        if (programListFilter.getIdentifierTypes().isEmpty()) {
                            arraySet.clear();
                        } else {
                            arraySet.addAll(programListFilter.getIdentifierTypes());
                        }
                    }
                    if (!arraySet2.isEmpty()) {
                        if (programListFilter.getIdentifiers().isEmpty()) {
                            arraySet2.clear();
                        } else {
                            arraySet2.addAll(programListFilter.getIdentifiers());
                        }
                    }
                    z2 |= programListFilter.areCategoriesIncluded();
                    z &= programListFilter.areModificationsExcluded();
                }
            }
        }
        if (arraySet == null) {
            return null;
        }
        return new ProgramList.Filter(arraySet, arraySet2, z2, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTunerSessionProgramListFilterChanged(TunerSession tunerSession) {
        synchronized (this.mLock) {
            onTunerSessionProgramListFilterChangedLocked(tunerSession);
        }
    }

    @GuardedBy({"mLock"})
    private void onTunerSessionProgramListFilterChangedLocked(TunerSession tunerSession) {
        ProgramList.Filter buildUnionOfTunerSessionFiltersLocked = buildUnionOfTunerSessionFiltersLocked();
        if (buildUnionOfTunerSessionFiltersLocked == null) {
            if (this.mUnionOfAidlProgramFilters == null) {
                return;
            }
            this.mUnionOfAidlProgramFilters = null;
            try {
                this.mService.stopProgramListUpdates();
                return;
            } catch (RemoteException e) {
                Slogf.e(TAG, e, "mHalTunerSession.stopProgramListUpdates() failed", new Object[0]);
                return;
            }
        }
        synchronized (this.mLock) {
            if (buildUnionOfTunerSessionFiltersLocked.equals(this.mUnionOfAidlProgramFilters)) {
                if (tunerSession != null) {
                    tunerSession.updateProgramInfoFromHalCache(this.mProgramInfoCache);
                }
                return;
            }
            this.mUnionOfAidlProgramFilters = buildUnionOfTunerSessionFiltersLocked;
            try {
                this.mService.startProgramListUpdates(ConversionUtils.filterToHalProgramFilter(buildUnionOfTunerSessionFiltersLocked));
            } catch (RemoteException e2) {
                Slogf.e(TAG, e2, "mHalTunerSession.startProgramListUpdates() failed", new Object[0]);
            } catch (RuntimeException e3) {
                throw ConversionUtils.throwOnError(e3, "Start Program ListUpdates");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTunerSessionClosed(TunerSession tunerSession) {
        synchronized (this.mLock) {
            onTunerSessionsClosedLocked(tunerSession);
        }
    }

    @GuardedBy({"mLock"})
    private void onTunerSessionsClosedLocked(TunerSession... tunerSessionArr) {
        for (TunerSession tunerSession : tunerSessionArr) {
            this.mAidlTunerSessions.remove(tunerSession);
        }
        onTunerSessionProgramListFilterChanged(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireLater(final Runnable runnable) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                runnable.run();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void fanoutAidlCallback(final AidlCallbackRunnable aidlCallbackRunnable) {
        fireLater(new Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                RadioModule.this.lambda$fanoutAidlCallback$1(aidlCallbackRunnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fanoutAidlCallback$1(AidlCallbackRunnable aidlCallbackRunnable) {
        synchronized (this.mLock) {
            fanoutAidlCallbackLocked(aidlCallbackRunnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void fanoutAidlCallbackLocked(AidlCallbackRunnable aidlCallbackRunnable) {
        int currentUser = RadioServiceUserController.getCurrentUser();
        ArrayList arrayList = null;
        for (int i = 0; i < this.mAidlTunerSessions.size(); i++) {
            if (this.mAidlTunerSessions.valueAt(i).mUserId == currentUser || this.mAidlTunerSessions.valueAt(i).mUserId == 0) {
                try {
                    aidlCallbackRunnable.run(this.mAidlTunerSessions.valueAt(i).mCallback, this.mAidlTunerSessions.valueAt(i).getUid());
                } catch (DeadObjectException unused) {
                    Slogf.e(TAG, "Removing dead TunerSession");
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(this.mAidlTunerSessions.valueAt(i));
                } catch (RemoteException e) {
                    Slogf.e(TAG, e, "Failed to invoke ITunerCallback", new Object[0]);
                }
            }
        }
        if (arrayList != null) {
            onTunerSessionsClosedLocked((TunerSession[]) arrayList.toArray(new TunerSession[arrayList.size()]));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ICloseHandle addAnnouncementListener(final IAnnouncementListener iAnnouncementListener, int[] iArr) throws RemoteException {
        this.mLogger.logRadioEvent("Add AnnouncementListener", new Object[0]);
        int length = iArr.length;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr[i] = (byte) iArr[i];
        }
        final android.hardware.broadcastradio.ICloseHandle[] iCloseHandleArr = {null};
        try {
            iCloseHandleArr[0] = this.mService.registerAnnouncementListener(new IAnnouncementListener.Stub() { // from class: com.android.server.broadcastradio.aidl.RadioModule.2
                @Override // android.hardware.broadcastradio.IAnnouncementListener
                public String getInterfaceHash() {
                    return "3c864ddf392d28cfbf95849bedf0b753b81cc013";
                }

                @Override // android.hardware.broadcastradio.IAnnouncementListener
                public int getInterfaceVersion() {
                    return 1;
                }

                @Override // android.hardware.broadcastradio.IAnnouncementListener
                public void onListUpdated(Announcement[] announcementArr) throws RemoteException {
                    ArrayList arrayList = new ArrayList(announcementArr.length);
                    for (Announcement announcement : announcementArr) {
                        arrayList.add(ConversionUtils.announcementFromHalAnnouncement(announcement));
                    }
                    iAnnouncementListener.onListUpdated(arrayList);
                }
            }, bArr);
            return new ICloseHandle.Stub() { // from class: com.android.server.broadcastradio.aidl.RadioModule.3
                public void close() {
                    try {
                        iCloseHandleArr[0].close();
                    } catch (RemoteException e) {
                        Slogf.e(RadioModule.TAG, e, "Failed closing announcement listener", new Object[0]);
                    }
                    iCloseHandleArr[0] = null;
                }
            };
        } catch (RuntimeException e) {
            throw ConversionUtils.throwOnError(e, "AnnouncementListener");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bitmap getImage(int i) {
        this.mLogger.logRadioEvent("Get image for id = %d", Integer.valueOf(i));
        if (i == 0) {
            throw new IllegalArgumentException("Image ID is missing");
        }
        try {
            byte[] image = this.mService.getImage(i);
            if (image == null || image.length == 0) {
                return null;
            }
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpInfo(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.printf("RadioModule\n", new Object[0]);
        indentingPrintWriter.increaseIndent();
        synchronized (this.mLock) {
            indentingPrintWriter.printf("BroadcastRadioServiceImpl: %s\n", new Object[]{this.mService});
            indentingPrintWriter.printf("Properties: %s\n", new Object[]{this.mProperties});
            indentingPrintWriter.printf("Antenna state: ", new Object[0]);
            Boolean bool = this.mAntennaConnected;
            if (bool == null) {
                indentingPrintWriter.printf("undetermined\n", new Object[0]);
            } else {
                Object[] objArr = new Object[1];
                objArr[0] = bool.booleanValue() ? "connected" : "not connected";
                indentingPrintWriter.printf("%s\n", objArr);
            }
            indentingPrintWriter.printf("current ProgramInfo: %s\n", new Object[]{this.mCurrentProgramInfo});
            indentingPrintWriter.printf("ProgramInfoCache: %s\n", new Object[]{this.mProgramInfoCache});
            indentingPrintWriter.printf("Union of AIDL ProgramFilters: %s\n", new Object[]{this.mUnionOfAidlProgramFilters});
            indentingPrintWriter.printf("AIDL TunerSessions [%d]:\n", new Object[]{Integer.valueOf(this.mAidlTunerSessions.size())});
            indentingPrintWriter.increaseIndent();
            for (int i = 0; i < this.mAidlTunerSessions.size(); i++) {
                this.mAidlTunerSessions.valueAt(i).dumpInfo(indentingPrintWriter);
            }
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.printf("Radio module events:\n", new Object[0]);
        indentingPrintWriter.increaseIndent();
        this.mLogger.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
    }
}
