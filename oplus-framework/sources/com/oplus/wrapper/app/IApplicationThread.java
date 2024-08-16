package com.oplus.wrapper.app;

import android.app.ContentProviderHolder;
import android.app.IApplicationThread;
import android.app.IInstrumentationWatcher;
import android.app.IUiAutomationConnection;
import android.app.ProfilerInfo;
import android.app.ReceiverInfo;
import android.app.servertransaction.ClientTransaction;
import android.content.AutofillOptions;
import android.content.ComponentName;
import android.content.ContentCaptureOptions;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ParceledListSlice;
import android.content.pm.ProviderInfo;
import android.content.pm.ProviderInfoList;
import android.content.pm.ServiceInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.SharedMemory;
import android.view.autofill.AutofillId;
import android.view.translation.TranslationSpec;
import android.view.translation.UiTranslationSpec;
import com.android.internal.app.IVoiceInteractor;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public interface IApplicationThread {

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IApplicationThread {
        private final android.app.IApplicationThread mTarget = new IApplicationThread.Stub() { // from class: com.oplus.wrapper.app.IApplicationThread.Stub.1
            public void scheduleReceiver(Intent intent, ActivityInfo activityInfo, CompatibilityInfo compatibilityInfo, int i, String s, Bundle bundle, boolean b, boolean b1, int i1, int i2, int i3, String s1) throws RemoteException {
            }

            public void scheduleReceiverList(List<ReceiverInfo> list) throws RemoteException {
            }

            public void scheduleCreateService(IBinder iBinder, ServiceInfo serviceInfo, CompatibilityInfo compatibilityInfo, int i) throws RemoteException {
            }

            public void scheduleStopService(IBinder iBinder) throws RemoteException {
            }

            public void bindApplication(String s, ApplicationInfo applicationInfo, String s1, String s2, ProviderInfoList providerInfoList, ComponentName componentName, ProfilerInfo profilerInfo, Bundle bundle, IInstrumentationWatcher iInstrumentationWatcher, IUiAutomationConnection iUiAutomationConnection, int i, boolean b, boolean b1, boolean b2, boolean b3, Configuration configuration, CompatibilityInfo compatibilityInfo, Map map, Bundle bundle1, String s3, AutofillOptions autofillOptions, ContentCaptureOptions contentCaptureOptions, long[] longs, SharedMemory sharedMemory, long l, long l1) throws RemoteException {
            }

            public void runIsolatedEntryPoint(String s, String[] strings) throws RemoteException {
            }

            public void scheduleExit() throws RemoteException {
            }

            public void scheduleServiceArgs(IBinder iBinder, ParceledListSlice parceledListSlice) throws RemoteException {
            }

            public void updateTimeZone() throws RemoteException {
            }

            public void processInBackground() throws RemoteException {
            }

            public void scheduleBindService(IBinder iBinder, Intent intent, boolean b, int i, long l) throws RemoteException {
            }

            public void scheduleUnbindService(IBinder iBinder, Intent intent) throws RemoteException {
            }

            public void dumpService(ParcelFileDescriptor parcelFileDescriptor, IBinder iBinder, String[] strings) throws RemoteException {
            }

            public void scheduleRegisteredReceiver(IIntentReceiver iIntentReceiver, Intent intent, int i, String s, Bundle bundle, boolean b, boolean b1, boolean b2, int i1, int i2, int i3, String s1) throws RemoteException {
            }

            public void scheduleLowMemory() throws RemoteException {
            }

            public void profilerControl(boolean b, ProfilerInfo profilerInfo, int i) throws RemoteException {
            }

            public void setSchedulingGroup(int i) throws RemoteException {
            }

            public void scheduleCreateBackupAgent(ApplicationInfo applicationInfo, int i, int i1, int i2) throws RemoteException {
            }

            public void scheduleDestroyBackupAgent(ApplicationInfo applicationInfo, int i) throws RemoteException {
            }

            public void scheduleOnNewActivityOptions(IBinder iBinder, Bundle bundle) throws RemoteException {
            }

            public void scheduleSuicide() throws RemoteException {
            }

            public void dispatchPackageBroadcast(int i, String[] strings) throws RemoteException {
            }

            public void scheduleCrash(String s, int i, Bundle bundle) throws RemoteException {
            }

            public void dumpHeap(boolean b, boolean b1, boolean b2, String s, ParcelFileDescriptor parcelFileDescriptor, RemoteCallback remoteCallback) throws RemoteException {
            }

            public void dumpActivity(ParcelFileDescriptor parcelFileDescriptor, IBinder iBinder, String s, String[] strings) throws RemoteException {
            }

            public void dumpResources(ParcelFileDescriptor parcelFileDescriptor, RemoteCallback remoteCallback) throws RemoteException {
            }

            public void clearDnsCache() throws RemoteException {
            }

            public void updateHttpProxy() throws RemoteException {
            }

            public void setCoreSettings(Bundle bundle) throws RemoteException {
            }

            public void updatePackageCompatibilityInfo(String s, CompatibilityInfo compatibilityInfo) throws RemoteException {
            }

            public void scheduleTrimMemory(int i) throws RemoteException {
            }

            public void dumpMemInfo(ParcelFileDescriptor parcelFileDescriptor, Debug.MemoryInfo memoryInfo, boolean b, boolean b1, boolean b2, boolean b3, boolean b4, String[] strings) throws RemoteException {
            }

            public void dumpMemInfoProto(ParcelFileDescriptor parcelFileDescriptor, Debug.MemoryInfo memoryInfo, boolean b, boolean b1, boolean b2, boolean b3, String[] strings) throws RemoteException {
            }

            public void dumpGfxInfo(ParcelFileDescriptor parcelFileDescriptor, String[] strings) throws RemoteException {
            }

            public void dumpCacheInfo(ParcelFileDescriptor parcelFileDescriptor, String[] strings) throws RemoteException {
            }

            public void dumpProvider(ParcelFileDescriptor parcelFileDescriptor, IBinder iBinder, String[] strings) throws RemoteException {
            }

            public void dumpDbInfo(ParcelFileDescriptor parcelFileDescriptor, String[] strings) throws RemoteException {
            }

            public void unstableProviderDied(IBinder iBinder) throws RemoteException {
            }

            public void requestAssistContextExtras(IBinder iBinder, IBinder iBinder1, int i, int i1, int i2) throws RemoteException {
            }

            public void scheduleTranslucentConversionComplete(IBinder iBinder, boolean b) throws RemoteException {
            }

            public void setProcessState(int i) throws RemoteException {
            }

            public void scheduleInstallProvider(ProviderInfo providerInfo) throws RemoteException {
            }

            public void updateTimePrefs(int i) throws RemoteException {
            }

            public void scheduleEnterAnimationComplete(IBinder iBinder) throws RemoteException {
            }

            public void notifyCleartextNetwork(byte[] bytes) throws RemoteException {
            }

            public void startBinderTracking() throws RemoteException {
            }

            public void stopBinderTrackingAndDump(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException {
            }

            public void scheduleLocalVoiceInteractionStarted(IBinder iBinder, IVoiceInteractor iVoiceInteractor) throws RemoteException {
            }

            public void handleTrustStorageUpdate() throws RemoteException {
            }

            public void attachAgent(String s) throws RemoteException {
            }

            public void attachStartupAgents(String s) throws RemoteException {
            }

            public void scheduleApplicationInfoChanged(ApplicationInfo applicationInfo) throws RemoteException {
            }

            public void setNetworkBlockSeq(long l) throws RemoteException {
            }

            public void scheduleTransaction(ClientTransaction clientTransaction) throws RemoteException {
            }

            public void requestDirectActions(IBinder iBinder, IVoiceInteractor iVoiceInteractor, RemoteCallback remoteCallback, RemoteCallback remoteCallback1) throws RemoteException {
            }

            public void performDirectAction(IBinder iBinder, String s, Bundle bundle, RemoteCallback remoteCallback, RemoteCallback remoteCallback1) throws RemoteException {
            }

            public void notifyContentProviderPublishStatus(ContentProviderHolder contentProviderHolder, String s, int i, boolean b) throws RemoteException {
            }

            public void instrumentWithoutRestart(ComponentName componentName, Bundle bundle, IInstrumentationWatcher iInstrumentationWatcher, IUiAutomationConnection iUiAutomationConnection, ApplicationInfo applicationInfo) throws RemoteException {
            }

            public void updateUiTranslationState(IBinder iBinder, int i, TranslationSpec translationSpec, TranslationSpec translationSpec1, List<AutofillId> list, UiTranslationSpec uiTranslationSpec) throws RemoteException {
            }

            public void scheduleTimeoutService(IBinder iBinder, int i) throws RemoteException {
            }

            public void schedulePing(RemoteCallback remoteCallback) throws RemoteException {
            }

            public void dumpMainLooperTrackedMsg() throws RemoteException {
            }

            public void oplusScheduleReceiver(Intent intent, ActivityInfo info, CompatibilityInfo compatInfo, int resultCode, String data, Bundle extras, boolean sync, int sendingUser, int processState, int hasCode) throws RemoteException {
            }

            public void enableActivityThreadLog(boolean isEnable) throws RemoteException {
            }

            public void dumpMessage(boolean all) throws RemoteException {
            }

            public void setDynamicalLogEnable(boolean on) throws RemoteException {
            }

            public void setDynamicalLogConfig(List<String> configs) throws RemoteException {
            }

            public void getBroadcastState(int flag) throws RemoteException {
            }

            public void enableProcessMainThreadLooperLog() throws RemoteException {
            }
        };

        public static IApplicationThread asInterface(IBinder obj) {
            return new Proxy(IApplicationThread.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IApplicationThread {
            private final android.app.IApplicationThread mTarget;

            Proxy(android.app.IApplicationThread target) {
                this.mTarget = target;
            }
        }
    }
}
