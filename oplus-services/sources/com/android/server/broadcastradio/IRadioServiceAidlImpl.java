package com.android.server.broadcastradio;

import android.hardware.broadcastradio.IBroadcastRadio;
import android.hardware.radio.IAnnouncementListener;
import android.hardware.radio.ICloseHandle;
import android.hardware.radio.IRadioService;
import android.hardware.radio.ITuner;
import android.hardware.radio.ITunerCallback;
import android.hardware.radio.RadioManager;
import android.os.Binder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.IndentingPrintWriter;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl;
import com.android.server.utils.Slogf;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class IRadioServiceAidlImpl extends IRadioService.Stub {
    private static final List<String> SERVICE_NAMES;
    private static final String TAG = "BcRadioSrvAidl";
    private final BroadcastRadioServiceImpl mHalAidl;
    private final BroadcastRadioService mService;

    static {
        StringBuilder sb = new StringBuilder();
        String str = IBroadcastRadio.DESCRIPTOR;
        sb.append(str);
        sb.append("/amfm");
        SERVICE_NAMES = Arrays.asList(sb.toString(), str + "/dab");
    }

    public static ArrayList<String> getServicesNames() {
        ArrayList<String> arrayList = new ArrayList<>();
        int i = 0;
        while (true) {
            List<String> list = SERVICE_NAMES;
            if (i >= list.size()) {
                return arrayList;
            }
            if (ServiceManager.waitForDeclaredService(list.get(i)) != null) {
                arrayList.add(list.get(i));
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IRadioServiceAidlImpl(BroadcastRadioService broadcastRadioService, ArrayList<String> arrayList) {
        this(broadcastRadioService, new BroadcastRadioServiceImpl(arrayList));
        Slogf.i(TAG, "Initialize BroadcastRadioServiceAidl(%s)", new Object[]{broadcastRadioService});
    }

    @VisibleForTesting
    IRadioServiceAidlImpl(BroadcastRadioService broadcastRadioService, BroadcastRadioServiceImpl broadcastRadioServiceImpl) {
        Objects.requireNonNull(broadcastRadioService, "Broadcast radio service cannot be null");
        this.mService = broadcastRadioService;
        Objects.requireNonNull(broadcastRadioServiceImpl, "Broadcast radio service implementation for AIDL HAL cannot be null");
        this.mHalAidl = broadcastRadioServiceImpl;
    }

    public List<RadioManager.ModuleProperties> listModules() {
        this.mService.enforcePolicyAccess();
        return this.mHalAidl.listModules();
    }

    public ITuner openTuner(int i, RadioManager.BandConfig bandConfig, boolean z, ITunerCallback iTunerCallback) throws RemoteException {
        if (isDebugEnabled()) {
            Slogf.d(TAG, "Opening module %d", new Object[]{Integer.valueOf(i)});
        }
        this.mService.enforcePolicyAccess();
        if (iTunerCallback == null) {
            throw new IllegalArgumentException("Callback must not be null");
        }
        return this.mHalAidl.openSession(i, bandConfig, z, iTunerCallback);
    }

    public ICloseHandle addAnnouncementListener(int[] iArr, IAnnouncementListener iAnnouncementListener) {
        if (isDebugEnabled()) {
            Slogf.d(TAG, "Adding announcement listener for %s", new Object[]{Arrays.toString(iArr)});
        }
        Objects.requireNonNull(iArr, "Enabled announcement types cannot be null");
        Objects.requireNonNull(iAnnouncementListener, "Announcement listener cannot be null");
        this.mService.enforcePolicyAccess();
        return this.mHalAidl.addAnnouncementListener(iArr, iAnnouncementListener);
    }

    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (this.mService.getContext().checkCallingOrSelfPermission("android.permission.DUMP") != 0) {
            printWriter.println("Permission Denial: can't dump AIDL BroadcastRadioService from from pid=" + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid() + " without permission android.permission.DUMP");
            return;
        }
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.printf("BroadcastRadioService\n", new Object[0]);
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.printf("AIDL HAL:\n", new Object[0]);
        indentingPrintWriter.increaseIndent();
        this.mHalAidl.dumpInfo(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
    }

    private static boolean isDebugEnabled() {
        return Log.isLoggable(TAG, 3);
    }
}
