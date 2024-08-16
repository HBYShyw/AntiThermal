package com.android.server.hdmi;

import android.hardware.hdmi.HdmiPortInfo;
import android.hardware.tv.cec.V1_0.HotplugEvent;
import android.hardware.tv.cec.V1_0.IHdmiCec;
import android.hardware.tv.cec.V1_0.IHdmiCecCallback;
import android.hardware.tv.cec.V1_1.IHdmiCecCallback;
import android.hardware.tv.hdmi.cec.CecMessage;
import android.hardware.tv.hdmi.cec.IHdmiCec;
import android.hardware.tv.hdmi.cec.IHdmiCecCallback;
import android.hardware.tv.hdmi.connection.IHdmiConnection;
import android.hardware.tv.hdmi.connection.IHdmiConnectionCallback;
import android.icu.util.IllformedLocaleException;
import android.icu.util.ULocale;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.IHwBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.hdmi.Constants;
import com.android.server.hdmi.HdmiAnnotations;
import com.android.server.hdmi.HdmiCecController;
import com.android.server.hdmi.HdmiControlService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Predicate;
import libcore.util.EmptyArray;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class HdmiCecController {
    private static final int ACTION_ON_RECEIVE_MSG = 2;
    private static final int CEC_DISABLED_DROP_MSG = 4;
    private static final int CEC_DISABLED_IGNORE = 1;
    private static final int CEC_DISABLED_LOG_WARNING = 2;
    private static final byte[] EMPTY_BODY = EmptyArray.BYTE;
    protected static final int HDMI_CEC_HAL_DEATH_COOKIE = 353;
    private static final int INITIAL_HDMI_MESSAGE_HISTORY_SIZE = 250;
    private static final int INVALID_PHYSICAL_ADDRESS = 65535;
    private static final int MAX_DEDICATED_ADDRESS = 11;
    private static final int NUM_LOGICAL_ADDRESS = 16;
    private static final String TAG = "HdmiCecController";
    private Handler mControlHandler;
    private final HdmiCecAtomWriter mHdmiCecAtomWriter;
    private Handler mIoHandler;
    private final NativeWrapper mNativeWrapperImpl;
    private final HdmiControlService mService;
    private final Predicate<Integer> mRemoteDeviceAddressPredicate = new Predicate<Integer>() { // from class: com.android.server.hdmi.HdmiCecController.1
        @Override // java.util.function.Predicate
        public boolean test(Integer num) {
            return !HdmiCecController.this.mService.getHdmiCecNetwork().isAllocatedLocalDeviceAddress(num.intValue());
        }
    };
    private final Predicate<Integer> mSystemAudioAddressPredicate = new Predicate<Integer>() { // from class: com.android.server.hdmi.HdmiCecController.2
        @Override // java.util.function.Predicate
        public boolean test(Integer num) {
            return HdmiUtils.isEligibleAddressForDevice(5, num.intValue());
        }
    };
    private ArrayBlockingQueue<Dumpable> mMessageHistory = new ArrayBlockingQueue<>(INITIAL_HDMI_MESSAGE_HISTORY_SIZE);
    private final Object mMessageHistoryLock = new Object();
    private long mLogicalAddressAllocationDelay = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface AllocateAddressCallback {
        void onAllocated(int i, int i2);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static abstract class Dumpable {
        protected final long mTime = System.currentTimeMillis();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract void dump(IndentingPrintWriter indentingPrintWriter, SimpleDateFormat simpleDateFormat);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface NativeWrapper {
        void enableCec(boolean z);

        void enableSystemCecControl(boolean z);

        void enableWakeupByOtp(boolean z);

        int nativeAddLogicalAddress(int i);

        void nativeClearLogicalAddress();

        void nativeEnableAudioReturnChannel(int i, boolean z);

        int nativeGetHpdSignalType(int i);

        int nativeGetPhysicalAddress();

        HdmiPortInfo[] nativeGetPortInfos();

        int nativeGetVendorId();

        int nativeGetVersion();

        String nativeInit();

        boolean nativeIsConnected(int i);

        int nativeSendCecCommand(int i, int i2, byte[] bArr);

        void nativeSetHpdSignalType(int i, int i2);

        void nativeSetLanguage(String str);

        void setCallback(HdmiCecCallback hdmiCecCallback);
    }

    private HdmiCecController(HdmiControlService hdmiControlService, NativeWrapper nativeWrapper, HdmiCecAtomWriter hdmiCecAtomWriter) {
        this.mService = hdmiControlService;
        this.mNativeWrapperImpl = nativeWrapper;
        this.mHdmiCecAtomWriter = hdmiCecAtomWriter;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecController create(HdmiControlService hdmiControlService, HdmiCecAtomWriter hdmiCecAtomWriter) {
        byte b = 0;
        byte b2 = 0;
        HdmiCecController createWithNativeWrapper = createWithNativeWrapper(hdmiControlService, new NativeWrapperImplAidl(), hdmiCecAtomWriter);
        if (createWithNativeWrapper != null) {
            return createWithNativeWrapper;
        }
        HdmiLogger.warning("Unable to use CEC and HDMI Connection AIDL HALs", new Object[0]);
        HdmiCecController createWithNativeWrapper2 = createWithNativeWrapper(hdmiControlService, new NativeWrapperImpl11(), hdmiCecAtomWriter);
        if (createWithNativeWrapper2 != null) {
            return createWithNativeWrapper2;
        }
        HdmiLogger.warning("Unable to use cec@1.1", new Object[0]);
        return createWithNativeWrapper(hdmiControlService, new NativeWrapperImpl(), hdmiCecAtomWriter);
    }

    static HdmiCecController createWithNativeWrapper(HdmiControlService hdmiControlService, NativeWrapper nativeWrapper, HdmiCecAtomWriter hdmiCecAtomWriter) {
        HdmiCecController hdmiCecController = new HdmiCecController(hdmiControlService, nativeWrapper, hdmiCecAtomWriter);
        if (nativeWrapper.nativeInit() == null) {
            HdmiLogger.warning("Couldn't get tv.cec service.", new Object[0]);
            return null;
        }
        hdmiCecController.init(nativeWrapper);
        return hdmiCecController;
    }

    private void init(NativeWrapper nativeWrapper) {
        this.mIoHandler = new Handler(this.mService.getIoLooper());
        this.mControlHandler = new Handler(this.mService.getServiceLooper());
        nativeWrapper.setCallback(new HdmiCecCallback());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void allocateLogicalAddress(final int i, final int i2, final AllocateAddressCallback allocateAddressCallback) {
        assertRunOnServiceThread();
        this.mIoHandler.postDelayed(new Runnable() { // from class: com.android.server.hdmi.HdmiCecController.3
            @Override // java.lang.Runnable
            public void run() {
                HdmiCecController.this.handleAllocateLogicalAddress(i, i2, allocateAddressCallback);
            }
        }, this.mLogicalAddressAllocationDelay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @HdmiAnnotations.IoThreadOnly
    public void handleAllocateLogicalAddress(final int i, int i2, final AllocateAddressCallback allocateAddressCallback) {
        final int i3;
        boolean z;
        assertRunOnIoThread();
        ArrayList arrayList = new ArrayList();
        if (HdmiUtils.isEligibleAddressForDevice(i, i2)) {
            arrayList.add(Integer.valueOf(i2));
        }
        for (int i4 = 0; i4 < 16; i4++) {
            if (!arrayList.contains(Integer.valueOf(i4)) && HdmiUtils.isEligibleAddressForDevice(i, i4) && HdmiUtils.isEligibleAddressForCecVersion(this.mService.getCecVersion(), i4)) {
                arrayList.add(Integer.valueOf(i4));
            }
        }
        Iterator it = arrayList.iterator();
        while (true) {
            if (!it.hasNext()) {
                i3 = 15;
                break;
            }
            Integer num = (Integer) it.next();
            int i5 = 0;
            while (true) {
                if (i5 >= 3) {
                    z = false;
                    break;
                }
                z = true;
                if (sendPollMessage(num.intValue(), num.intValue(), 1)) {
                    break;
                } else {
                    i5++;
                }
            }
            if (!z) {
                i3 = num.intValue();
                break;
            }
        }
        HdmiLogger.debug("New logical address for device [%d]: [preferred:%d, assigned:%d]", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
        if (allocateAddressCallback != null) {
            runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiCecController.4
                @Override // java.lang.Runnable
                public void run() {
                    allocateAddressCallback.onAllocated(i, i3);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] buildBody(int i, byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length + 1];
        bArr2[0] = (byte) i;
        System.arraycopy(bArr, 0, bArr2, 1, bArr.length);
        return bArr2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiPortInfo[] getPortInfos() {
        return this.mNativeWrapperImpl.nativeGetPortInfos();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public int addLogicalAddress(int i) {
        assertRunOnServiceThread();
        if (HdmiUtils.isValidAddress(i)) {
            return this.mNativeWrapperImpl.nativeAddLogicalAddress(i);
        }
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void clearLogicalAddress() {
        assertRunOnServiceThread();
        this.mNativeWrapperImpl.nativeClearLogicalAddress();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public int getPhysicalAddress() {
        assertRunOnServiceThread();
        return this.mNativeWrapperImpl.nativeGetPhysicalAddress();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public int getVersion() {
        assertRunOnServiceThread();
        return this.mNativeWrapperImpl.nativeGetVersion();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public int getVendorId() {
        assertRunOnServiceThread();
        return this.mNativeWrapperImpl.nativeGetVendorId();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void enableWakeupByOtp(boolean z) {
        assertRunOnServiceThread();
        HdmiLogger.debug("enableWakeupByOtp: %b", Boolean.valueOf(z));
        this.mNativeWrapperImpl.enableWakeupByOtp(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void enableCec(boolean z) {
        assertRunOnServiceThread();
        HdmiLogger.debug("enableCec: %b", Boolean.valueOf(z));
        this.mNativeWrapperImpl.enableCec(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void enableSystemCecControl(boolean z) {
        assertRunOnServiceThread();
        HdmiLogger.debug("enableSystemCecControl: %b", Boolean.valueOf(z));
        this.mNativeWrapperImpl.enableSystemCecControl(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void setHpdSignalType(@Constants.HpdSignalType int i, int i2) {
        assertRunOnServiceThread();
        HdmiLogger.debug("setHpdSignalType: portId %b, signal %b", Integer.valueOf(i2), Integer.valueOf(i));
        this.mNativeWrapperImpl.nativeSetHpdSignalType(i, i2);
    }

    @HdmiAnnotations.ServiceThreadOnly
    @Constants.HpdSignalType
    int getHpdSignalType(int i) {
        assertRunOnServiceThread();
        HdmiLogger.debug("getHpdSignalType: portId %b ", Integer.valueOf(i));
        return this.mNativeWrapperImpl.nativeGetHpdSignalType(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void setLanguage(String str) {
        assertRunOnServiceThread();
        if (isLanguage(str)) {
            this.mNativeWrapperImpl.nativeSetLanguage(str);
        }
    }

    @VisibleForTesting
    void setLogicalAddressAllocationDelay(long j) {
        this.mLogicalAddressAllocationDelay = j;
    }

    @VisibleForTesting
    static boolean isLanguage(String str) {
        if (str != null && !str.isEmpty()) {
            try {
                new ULocale.Builder().setLanguage(str);
                return true;
            } catch (IllformedLocaleException unused) {
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void enableAudioReturnChannel(int i, boolean z) {
        assertRunOnServiceThread();
        this.mNativeWrapperImpl.nativeEnableAudioReturnChannel(i, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public boolean isConnected(int i) {
        assertRunOnServiceThread();
        return this.mNativeWrapperImpl.nativeIsConnected(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void pollDevices(HdmiControlService.DevicePollingCallback devicePollingCallback, int i, int i2, int i3) {
        assertRunOnServiceThread();
        runDevicePolling(i, pickPollCandidates(i2), i3, devicePollingCallback, new ArrayList());
    }

    private List<Integer> pickPollCandidates(int i) {
        Predicate<Integer> predicate;
        if ((i & 3) == 2) {
            predicate = this.mSystemAudioAddressPredicate;
        } else {
            predicate = this.mRemoteDeviceAddressPredicate;
        }
        int i2 = i & 196608;
        ArrayList arrayList = new ArrayList();
        if (i2 != 65536) {
            for (int i3 = 14; i3 >= 0; i3--) {
                if (predicate.test(Integer.valueOf(i3))) {
                    arrayList.add(Integer.valueOf(i3));
                }
            }
        } else {
            for (int i4 = 0; i4 <= 14; i4++) {
                if (predicate.test(Integer.valueOf(i4))) {
                    arrayList.add(Integer.valueOf(i4));
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @HdmiAnnotations.ServiceThreadOnly
    public void runDevicePolling(final int i, final List<Integer> list, final int i2, final HdmiControlService.DevicePollingCallback devicePollingCallback, final List<Integer> list2) {
        assertRunOnServiceThread();
        if (!list.isEmpty()) {
            final Integer remove = list.remove(0);
            runOnIoThread(new Runnable() { // from class: com.android.server.hdmi.HdmiCecController.5
                @Override // java.lang.Runnable
                public void run() {
                    if (HdmiCecController.this.sendPollMessage(i, remove.intValue(), i2)) {
                        list2.add(remove);
                    }
                    HdmiCecController.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiCecController.5.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AnonymousClass5 anonymousClass5 = AnonymousClass5.this;
                            HdmiCecController.this.runDevicePolling(i, list, i2, devicePollingCallback, list2);
                        }
                    });
                }
            });
        } else if (devicePollingCallback != null) {
            HdmiLogger.debug("[P]:AllocatedAddress=%s", list2.toString());
            devicePollingCallback.onPollingFinished(list2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @HdmiAnnotations.IoThreadOnly
    public boolean sendPollMessage(int i, int i2, int i3) {
        assertRunOnIoThread();
        for (int i4 = 0; i4 < i3; i4++) {
            int nativeSendCecCommand = this.mNativeWrapperImpl.nativeSendCecCommand(i, i2, EMPTY_BODY);
            if (nativeSendCecCommand == 0) {
                return true;
            }
            if (nativeSendCecCommand != 1) {
                HdmiLogger.warning("Failed to send a polling message(%d->%d) with return code %d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(nativeSendCecCommand));
            }
        }
        return false;
    }

    private void assertRunOnIoThread() {
        if (Looper.myLooper() != this.mIoHandler.getLooper()) {
            throw new IllegalStateException("Should run on io thread.");
        }
    }

    private void assertRunOnServiceThread() {
        if (Looper.myLooper() != this.mControlHandler.getLooper()) {
            throw new IllegalStateException("Should run on service thread.");
        }
    }

    @VisibleForTesting
    void runOnIoThread(Runnable runnable) {
        this.mIoHandler.post(new WorkSourceUidPreservingRunnable(runnable));
    }

    @VisibleForTesting
    void runOnServiceThread(Runnable runnable) {
        this.mControlHandler.post(new WorkSourceUidPreservingRunnable(runnable));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void flush(final Runnable runnable) {
        assertRunOnServiceThread();
        runOnIoThread(new Runnable() { // from class: com.android.server.hdmi.HdmiCecController.6
            @Override // java.lang.Runnable
            public void run() {
                HdmiCecController.this.runOnServiceThread(runnable);
            }
        });
    }

    private boolean isAcceptableAddress(int i) {
        if (i == 15) {
            return true;
        }
        return this.mService.getHdmiCecNetwork().isAllocatedLocalDeviceAddress(i);
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    void onReceiveCommand(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        if (!this.mService.isCecControlEnabled() && !HdmiCecMessage.isCecTransportMessage(hdmiCecMessage.getOpcode())) {
            HdmiLogger.warning("Message " + hdmiCecMessage + " received when cec disabled", new Object[0]);
        }
        if (!this.mService.isAddressAllocated() || isAcceptableAddress(hdmiCecMessage.getDestination())) {
            int handleCecCommand = this.mService.handleCecCommand(hdmiCecMessage);
            if (handleCecCommand == -2) {
                maySendFeatureAbortCommand(hdmiCecMessage, 0);
            } else if (handleCecCommand != -1) {
                maySendFeatureAbortCommand(hdmiCecMessage, handleCecCommand);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void maySendFeatureAbortCommand(HdmiCecMessage hdmiCecMessage, int i) {
        int opcode;
        assertRunOnServiceThread();
        int destination = hdmiCecMessage.getDestination();
        int source = hdmiCecMessage.getSource();
        if (destination == 15 || source == 15 || (opcode = hdmiCecMessage.getOpcode()) == 0) {
            return;
        }
        sendCommand(HdmiCecMessageBuilder.buildFeatureAbortCommand(destination, source, opcode, i));
    }

    @HdmiAnnotations.ServiceThreadOnly
    void sendCommand(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        sendCommand(hdmiCecMessage, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCallingUid() {
        int callingWorkSourceUid = Binder.getCallingWorkSourceUid();
        return callingWorkSourceUid == -1 ? Binder.getCallingUid() : callingWorkSourceUid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void sendCommand(final HdmiCecMessage hdmiCecMessage, final HdmiControlService.SendMessageCallback sendMessageCallback) {
        assertRunOnServiceThread();
        final ArrayList arrayList = new ArrayList();
        runOnIoThread(new Runnable() { // from class: com.android.server.hdmi.HdmiCecController.7
            @Override // java.lang.Runnable
            public void run() {
                final int nativeSendCecCommand;
                int i = 0;
                HdmiLogger.debug("[S]:" + hdmiCecMessage, new Object[0]);
                byte[] buildBody = HdmiCecController.buildBody(hdmiCecMessage.getOpcode(), hdmiCecMessage.getParams());
                while (true) {
                    nativeSendCecCommand = HdmiCecController.this.mNativeWrapperImpl.nativeSendCecCommand(hdmiCecMessage.getSource(), hdmiCecMessage.getDestination(), buildBody);
                    if (nativeSendCecCommand == 0) {
                        arrayList.add("ACK");
                    } else if (nativeSendCecCommand == 1) {
                        arrayList.add("NACK");
                    } else if (nativeSendCecCommand == 2) {
                        arrayList.add("BUSY");
                    } else if (nativeSendCecCommand == 3) {
                        arrayList.add("FAIL");
                    }
                    if (nativeSendCecCommand == 0) {
                        break;
                    }
                    int i2 = i + 1;
                    if (i >= 1) {
                        break;
                    } else {
                        i = i2;
                    }
                }
                if (nativeSendCecCommand != 0) {
                    Slog.w(HdmiCecController.TAG, "Failed to send " + hdmiCecMessage + " with errorCode=" + nativeSendCecCommand);
                }
                HdmiCecController.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiCecController.7.1
                    @Override // java.lang.Runnable
                    public void run() {
                        HdmiCecAtomWriter hdmiCecAtomWriter = HdmiCecController.this.mHdmiCecAtomWriter;
                        AnonymousClass7 anonymousClass7 = AnonymousClass7.this;
                        hdmiCecAtomWriter.messageReported(hdmiCecMessage, 2, HdmiCecController.this.getCallingUid(), nativeSendCecCommand);
                        HdmiControlService.SendMessageCallback sendMessageCallback2 = sendMessageCallback;
                        if (sendMessageCallback2 != null) {
                            sendMessageCallback2.onSendCompleted(nativeSendCecCommand);
                        }
                    }
                });
            }
        });
        addCecMessageToHistory(false, hdmiCecMessage, arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @HdmiAnnotations.ServiceThreadOnly
    public void handleIncomingCecCommand(int i, int i2, byte[] bArr) {
        assertRunOnServiceThread();
        if (bArr.length == 0) {
            Slog.e(TAG, "Message with empty body received.");
            return;
        }
        HdmiCecMessage build = HdmiCecMessage.build(i, i2, bArr[0], Arrays.copyOfRange(bArr, 1, bArr.length));
        if (build.getValidationResult() != 0) {
            Slog.e(TAG, "Invalid message received: " + build);
        }
        HdmiLogger.debug("[R]:" + build, new Object[0]);
        addCecMessageToHistory(true, build, null);
        this.mHdmiCecAtomWriter.messageReported(build, incomingMessageDirection(i, i2), getCallingUid());
        onReceiveCommand(build);
    }

    private int incomingMessageDirection(int i, int i2) {
        boolean z = false;
        boolean z2 = i2 == 15;
        Iterator<HdmiCecLocalDevice> it = this.mService.getHdmiCecNetwork().getLocalDeviceList().iterator();
        while (it.hasNext()) {
            int logicalAddress = it.next().getDeviceInfo().getLogicalAddress();
            if (logicalAddress == i) {
                z = true;
            }
            if (logicalAddress == i2) {
                z2 = true;
            }
        }
        if (z || !z2) {
            return (z && z2) ? 4 : 1;
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @HdmiAnnotations.ServiceThreadOnly
    public void handleHotplug(int i, boolean z) {
        assertRunOnServiceThread();
        HdmiLogger.debug("Hotplug event:[port:%d, connected:%b]", Integer.valueOf(i), Boolean.valueOf(z));
        addHotplugEventToHistory(i, z);
        this.mService.onHotplug(i, z);
    }

    @HdmiAnnotations.ServiceThreadOnly
    private void addHotplugEventToHistory(int i, boolean z) {
        assertRunOnServiceThread();
        addEventToHistory(new HotplugHistoryRecord(i, z));
    }

    @HdmiAnnotations.ServiceThreadOnly
    private void addCecMessageToHistory(boolean z, HdmiCecMessage hdmiCecMessage, List<String> list) {
        assertRunOnServiceThread();
        addEventToHistory(new MessageHistoryRecord(z, hdmiCecMessage, list));
    }

    private void addEventToHistory(Dumpable dumpable) {
        synchronized (this.mMessageHistoryLock) {
            if (!this.mMessageHistory.offer(dumpable)) {
                this.mMessageHistory.poll();
                this.mMessageHistory.offer(dumpable);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMessageHistorySize() {
        int size;
        synchronized (this.mMessageHistoryLock) {
            size = this.mMessageHistory.size() + this.mMessageHistory.remainingCapacity();
        }
        return size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setMessageHistorySize(int i) {
        if (i < INITIAL_HDMI_MESSAGE_HISTORY_SIZE) {
            return false;
        }
        ArrayBlockingQueue<Dumpable> arrayBlockingQueue = new ArrayBlockingQueue<>(i);
        synchronized (this.mMessageHistoryLock) {
            if (i < this.mMessageHistory.size()) {
                for (int i2 = 0; i2 < this.mMessageHistory.size() - i; i2++) {
                    this.mMessageHistory.poll();
                }
            }
            arrayBlockingQueue.addAll(this.mMessageHistory);
            this.mMessageHistory = arrayBlockingQueue;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("CEC message history:");
        indentingPrintWriter.increaseIndent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Iterator<Dumpable> it = this.mMessageHistory.iterator();
        while (it.hasNext()) {
            it.next().dump(indentingPrintWriter, simpleDateFormat);
        }
        indentingPrintWriter.decreaseIndent();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class NativeWrapperImplAidl implements NativeWrapper, IBinder.DeathRecipient {
        private HdmiCecCallback mCallback;
        private IHdmiCec mHdmiCec;
        private IHdmiConnection mHdmiConnection;
        private final Object mLock;

        private NativeWrapperImplAidl() {
            this.mLock = new Object();
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public String nativeInit() {
            if (!connectToHal()) {
                return null;
            }
            return this.mHdmiCec.toString() + " " + this.mHdmiConnection.toString();
        }

        boolean connectToHal() {
            IHdmiCec asInterface = IHdmiCec.Stub.asInterface(ServiceManager.getService(IHdmiCec.DESCRIPTOR + "/default"));
            this.mHdmiCec = asInterface;
            if (asInterface == null) {
                HdmiLogger.error("Could not initialize HDMI CEC AIDL HAL", new Object[0]);
                return false;
            }
            try {
                asInterface.asBinder().linkToDeath(this, 0);
            } catch (RemoteException e) {
                HdmiLogger.error("Couldn't link to death : ", e, new Object[0]);
            }
            IHdmiConnection asInterface2 = IHdmiConnection.Stub.asInterface(ServiceManager.getService(IHdmiConnection.DESCRIPTOR + "/default"));
            this.mHdmiConnection = asInterface2;
            if (asInterface2 == null) {
                HdmiLogger.error("Could not initialize HDMI Connection AIDL HAL", new Object[0]);
                return false;
            }
            try {
                asInterface2.asBinder().linkToDeath(this, 0);
                return true;
            } catch (RemoteException e2) {
                HdmiLogger.error("Couldn't link to death : ", e2, new Object[0]);
                return true;
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.mHdmiCec.asBinder().unlinkToDeath(this, 0);
            this.mHdmiConnection.asBinder().unlinkToDeath(this, 0);
            HdmiLogger.error("HDMI Connection or CEC service died, reconnecting", new Object[0]);
            connectToHal();
            HdmiCecCallback hdmiCecCallback = this.mCallback;
            if (hdmiCecCallback != null) {
                setCallback(hdmiCecCallback);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void setCallback(HdmiCecCallback hdmiCecCallback) {
            this.mCallback = hdmiCecCallback;
            try {
                this.mHdmiCec.setCallback(new HdmiCecCallbackAidl(hdmiCecCallback));
            } catch (RemoteException e) {
                HdmiLogger.error("Couldn't initialise tv.cec callback : ", e, new Object[0]);
            }
            try {
                this.mHdmiConnection.setCallback(new HdmiConnectionCallbackAidl(hdmiCecCallback));
            } catch (RemoteException e2) {
                HdmiLogger.error("Couldn't initialise tv.hdmi callback : ", e2, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeSendCecCommand(int i, int i2, byte[] bArr) {
            CecMessage cecMessage = new CecMessage();
            cecMessage.initiator = (byte) (i & 15);
            cecMessage.destination = (byte) (i2 & 15);
            cecMessage.body = bArr;
            try {
                return this.mHdmiCec.sendMessage(cecMessage);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to send CEC message : ", e, new Object[0]);
                return 3;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeAddLogicalAddress(int i) {
            try {
                return this.mHdmiCec.addLogicalAddress((byte) i);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to add a logical address : ", e, new Object[0]);
                return 2;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeClearLogicalAddress() {
            try {
                this.mHdmiCec.clearLogicalAddress();
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to clear logical address : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetPhysicalAddress() {
            try {
                return this.mHdmiCec.getPhysicalAddress();
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get physical address : ", e, new Object[0]);
                return 65535;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetVersion() {
            try {
                return this.mHdmiCec.getCecVersion();
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get cec version : ", e, new Object[0]);
                return 1;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetVendorId() {
            try {
                return this.mHdmiCec.getVendorId();
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get vendor id : ", e, new Object[0]);
                return 1;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableWakeupByOtp(boolean z) {
            try {
                this.mHdmiCec.enableWakeupByOtp(z);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed call to enableWakeupByOtp : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableCec(boolean z) {
            try {
                this.mHdmiCec.enableCec(z);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed call to enableCec : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableSystemCecControl(boolean z) {
            try {
                this.mHdmiCec.enableSystemCecControl(z);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed call to enableSystemCecControl : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeSetLanguage(String str) {
            try {
                this.mHdmiCec.setLanguage(str);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to set language : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeEnableAudioReturnChannel(int i, boolean z) {
            try {
                this.mHdmiCec.enableAudioReturnChannel(i, z);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to enable/disable ARC : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public HdmiPortInfo[] nativeGetPortInfos() {
            try {
                android.hardware.tv.hdmi.connection.HdmiPortInfo[] portInfo = this.mHdmiConnection.getPortInfo();
                HdmiPortInfo[] hdmiPortInfoArr = new HdmiPortInfo[portInfo.length];
                int i = 0;
                for (android.hardware.tv.hdmi.connection.HdmiPortInfo hdmiPortInfo : portInfo) {
                    hdmiPortInfoArr[i] = new HdmiPortInfo.Builder(hdmiPortInfo.portId, hdmiPortInfo.type, hdmiPortInfo.physicalAddress).setCecSupported(hdmiPortInfo.cecSupported).setMhlSupported(false).setArcSupported(hdmiPortInfo.arcSupported).setEarcSupported(hdmiPortInfo.eArcSupported).build();
                    i++;
                }
                return hdmiPortInfoArr;
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get port information : ", e, new Object[0]);
                return null;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public boolean nativeIsConnected(int i) {
            try {
                return this.mHdmiConnection.isConnected(i);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get connection info : ", e, new Object[0]);
                return false;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeSetHpdSignalType(int i, int i2) {
            try {
                this.mHdmiConnection.setHpdSignal((byte) i, i2);
            } catch (ServiceSpecificException e) {
                HdmiLogger.error("Could not set HPD signal type for portId " + i2 + " to " + i + ". Error: ", Integer.valueOf(e.errorCode));
            } catch (RemoteException e2) {
                HdmiLogger.error("Could not set HPD signal type for portId " + i2 + " to " + i + ". Exception: ", e2, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetHpdSignalType(int i) {
            try {
                return this.mHdmiConnection.getHpdSignal(i);
            } catch (RemoteException e) {
                HdmiLogger.error("Could not get HPD signal type for portId " + i + ". Exception: ", e, new Object[0]);
                return 0;
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class NativeWrapperImpl11 implements NativeWrapper, IHwBinder.DeathRecipient, IHdmiCec.getPhysicalAddressCallback {
        private HdmiCecCallback mCallback;
        private android.hardware.tv.cec.V1_1.IHdmiCec mHdmiCec;
        private final Object mLock;
        private int mPhysicalAddress;

        private NativeWrapperImpl11() {
            this.mLock = new Object();
            this.mPhysicalAddress = 65535;
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public String nativeInit() {
            if (connectToHal()) {
                return this.mHdmiCec.toString();
            }
            return null;
        }

        boolean connectToHal() {
            try {
                android.hardware.tv.cec.V1_1.IHdmiCec service = android.hardware.tv.cec.V1_1.IHdmiCec.getService(true);
                this.mHdmiCec = service;
                try {
                    service.linkToDeath(this, 353L);
                } catch (RemoteException e) {
                    HdmiLogger.error("Couldn't link to death : ", e, new Object[0]);
                }
                return true;
            } catch (RemoteException | NoSuchElementException e2) {
                HdmiLogger.error("Couldn't connect to cec@1.1", e2, new Object[0]);
                return false;
            }
        }

        public void onValues(int i, short s) {
            if (i == 0) {
                synchronized (this.mLock) {
                    this.mPhysicalAddress = new Short(s).intValue();
                }
            }
        }

        public void serviceDied(long j) {
            if (j == 353) {
                HdmiLogger.error("Service died cookie : " + j + "; reconnecting", new Object[0]);
                connectToHal();
                HdmiCecCallback hdmiCecCallback = this.mCallback;
                if (hdmiCecCallback != null) {
                    setCallback(hdmiCecCallback);
                }
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void setCallback(HdmiCecCallback hdmiCecCallback) {
            this.mCallback = hdmiCecCallback;
            try {
                this.mHdmiCec.setCallback_1_1(new HdmiCecCallback11(hdmiCecCallback));
            } catch (RemoteException e) {
                HdmiLogger.error("Couldn't initialise tv.cec callback : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeSendCecCommand(int i, int i2, byte[] bArr) {
            android.hardware.tv.cec.V1_1.CecMessage cecMessage = new android.hardware.tv.cec.V1_1.CecMessage();
            cecMessage.initiator = i;
            cecMessage.destination = i2;
            cecMessage.body = new ArrayList(bArr.length);
            for (byte b : bArr) {
                cecMessage.body.add(Byte.valueOf(b));
            }
            try {
                return this.mHdmiCec.sendMessage_1_1(cecMessage);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to send CEC message : ", e, new Object[0]);
                return 3;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeAddLogicalAddress(int i) {
            try {
                return this.mHdmiCec.addLogicalAddress_1_1(i);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to add a logical address : ", e, new Object[0]);
                return 2;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeClearLogicalAddress() {
            try {
                this.mHdmiCec.clearLogicalAddress();
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to clear logical address : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetPhysicalAddress() {
            try {
                this.mHdmiCec.getPhysicalAddress(this);
                return this.mPhysicalAddress;
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get physical address : ", e, new Object[0]);
                return 65535;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetVersion() {
            try {
                return this.mHdmiCec.getCecVersion();
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get cec version : ", e, new Object[0]);
                return 1;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetVendorId() {
            try {
                return this.mHdmiCec.getVendorId();
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get vendor id : ", e, new Object[0]);
                return 1;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public HdmiPortInfo[] nativeGetPortInfos() {
            try {
                ArrayList portInfo = this.mHdmiCec.getPortInfo();
                HdmiPortInfo[] hdmiPortInfoArr = new HdmiPortInfo[portInfo.size()];
                Iterator it = portInfo.iterator();
                int i = 0;
                while (it.hasNext()) {
                    android.hardware.tv.cec.V1_0.HdmiPortInfo hdmiPortInfo = (android.hardware.tv.cec.V1_0.HdmiPortInfo) it.next();
                    hdmiPortInfoArr[i] = new HdmiPortInfo.Builder(hdmiPortInfo.portId, hdmiPortInfo.type, hdmiPortInfo.physicalAddress).setCecSupported(hdmiPortInfo.cecSupported).setMhlSupported(false).setArcSupported(hdmiPortInfo.arcSupported).setEarcSupported(false).build();
                    i++;
                }
                return hdmiPortInfoArr;
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get port information : ", e, new Object[0]);
                return null;
            }
        }

        private void nativeSetOption(int i, boolean z) {
            try {
                this.mHdmiCec.setOption(i, z);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to set option : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableWakeupByOtp(boolean z) {
            nativeSetOption(1, z);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableCec(boolean z) {
            nativeSetOption(2, z);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableSystemCecControl(boolean z) {
            nativeSetOption(3, z);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeSetLanguage(String str) {
            try {
                this.mHdmiCec.setLanguage(str);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to set language : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeEnableAudioReturnChannel(int i, boolean z) {
            try {
                this.mHdmiCec.enableAudioReturnChannel(i, z);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to enable/disable ARC : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public boolean nativeIsConnected(int i) {
            try {
                return this.mHdmiCec.isConnected(i);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get connection info : ", e, new Object[0]);
                return false;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeSetHpdSignalType(int i, int i2) {
            HdmiLogger.error("Failed to set HPD signal type: not supported by HAL.", new Object[0]);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetHpdSignalType(int i) {
            HdmiLogger.error("Failed to get HPD signal type: not supported by HAL.", new Object[0]);
            return 0;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class NativeWrapperImpl implements NativeWrapper, IHwBinder.DeathRecipient, IHdmiCec.getPhysicalAddressCallback {
        private HdmiCecCallback mCallback;
        private android.hardware.tv.cec.V1_0.IHdmiCec mHdmiCec;
        private final Object mLock;
        private int mPhysicalAddress;

        private NativeWrapperImpl() {
            this.mLock = new Object();
            this.mPhysicalAddress = 65535;
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public String nativeInit() {
            if (connectToHal()) {
                return this.mHdmiCec.toString();
            }
            return null;
        }

        boolean connectToHal() {
            try {
                android.hardware.tv.cec.V1_0.IHdmiCec service = android.hardware.tv.cec.V1_0.IHdmiCec.getService(true);
                this.mHdmiCec = service;
                try {
                    service.linkToDeath(this, 353L);
                } catch (RemoteException e) {
                    HdmiLogger.error("Couldn't link to death : ", e, new Object[0]);
                }
                return true;
            } catch (RemoteException | NoSuchElementException e2) {
                HdmiLogger.error("Couldn't connect to cec@1.0", e2, new Object[0]);
                return false;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void setCallback(HdmiCecCallback hdmiCecCallback) {
            this.mCallback = hdmiCecCallback;
            try {
                this.mHdmiCec.setCallback(new HdmiCecCallback10(hdmiCecCallback));
            } catch (RemoteException e) {
                HdmiLogger.error("Couldn't initialise tv.cec callback : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeSendCecCommand(int i, int i2, byte[] bArr) {
            android.hardware.tv.cec.V1_0.CecMessage cecMessage = new android.hardware.tv.cec.V1_0.CecMessage();
            cecMessage.initiator = i;
            cecMessage.destination = i2;
            cecMessage.body = new ArrayList(bArr.length);
            for (byte b : bArr) {
                cecMessage.body.add(Byte.valueOf(b));
            }
            try {
                return this.mHdmiCec.sendMessage(cecMessage);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to send CEC message : ", e, new Object[0]);
                return 3;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeAddLogicalAddress(int i) {
            try {
                return this.mHdmiCec.addLogicalAddress(i);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to add a logical address : ", e, new Object[0]);
                return 2;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeClearLogicalAddress() {
            try {
                this.mHdmiCec.clearLogicalAddress();
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to clear logical address : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetPhysicalAddress() {
            try {
                this.mHdmiCec.getPhysicalAddress(this);
                return this.mPhysicalAddress;
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get physical address : ", e, new Object[0]);
                return 65535;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetVersion() {
            try {
                return this.mHdmiCec.getCecVersion();
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get cec version : ", e, new Object[0]);
                return 1;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetVendorId() {
            try {
                return this.mHdmiCec.getVendorId();
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get vendor id : ", e, new Object[0]);
                return 1;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public HdmiPortInfo[] nativeGetPortInfos() {
            try {
                ArrayList portInfo = this.mHdmiCec.getPortInfo();
                HdmiPortInfo[] hdmiPortInfoArr = new HdmiPortInfo[portInfo.size()];
                Iterator it = portInfo.iterator();
                int i = 0;
                while (it.hasNext()) {
                    android.hardware.tv.cec.V1_0.HdmiPortInfo hdmiPortInfo = (android.hardware.tv.cec.V1_0.HdmiPortInfo) it.next();
                    hdmiPortInfoArr[i] = new HdmiPortInfo.Builder(hdmiPortInfo.portId, hdmiPortInfo.type, hdmiPortInfo.physicalAddress).setCecSupported(hdmiPortInfo.cecSupported).setMhlSupported(false).setArcSupported(hdmiPortInfo.arcSupported).setEarcSupported(false).build();
                    i++;
                }
                return hdmiPortInfoArr;
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get port information : ", e, new Object[0]);
                return null;
            }
        }

        private void nativeSetOption(int i, boolean z) {
            try {
                this.mHdmiCec.setOption(i, z);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to set option : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableWakeupByOtp(boolean z) {
            nativeSetOption(1, z);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableCec(boolean z) {
            nativeSetOption(2, z);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableSystemCecControl(boolean z) {
            nativeSetOption(3, z);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeSetLanguage(String str) {
            try {
                this.mHdmiCec.setLanguage(str);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to set language : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeEnableAudioReturnChannel(int i, boolean z) {
            try {
                this.mHdmiCec.enableAudioReturnChannel(i, z);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to enable/disable ARC : ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public boolean nativeIsConnected(int i) {
            try {
                return this.mHdmiCec.isConnected(i);
            } catch (RemoteException e) {
                HdmiLogger.error("Failed to get connection info : ", e, new Object[0]);
                return false;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeSetHpdSignalType(int i, int i2) {
            HdmiLogger.error("Failed to set HPD signal type: not supported by HAL.", new Object[0]);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetHpdSignalType(int i) {
            HdmiLogger.error("Failed to get HPD signal type: not supported by HAL.", new Object[0]);
            return 0;
        }

        public void serviceDied(long j) {
            if (j == 353) {
                HdmiLogger.error("Service died cookie : " + j + "; reconnecting", new Object[0]);
                connectToHal();
                HdmiCecCallback hdmiCecCallback = this.mCallback;
                if (hdmiCecCallback != null) {
                    setCallback(hdmiCecCallback);
                }
            }
        }

        public void onValues(int i, short s) {
            if (i == 0) {
                synchronized (this.mLock) {
                    this.mPhysicalAddress = new Short(s).intValue();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class HdmiCecCallback {
        HdmiCecCallback() {
        }

        @VisibleForTesting
        public void onCecMessage(final int i, final int i2, final byte[] bArr) {
            HdmiCecController.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiCecController$HdmiCecCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    HdmiCecController.HdmiCecCallback.this.lambda$onCecMessage$0(i, i2, bArr);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCecMessage$0(int i, int i2, byte[] bArr) {
            HdmiCecController.this.handleIncomingCecCommand(i, i2, bArr);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onHotplugEvent$1(int i, boolean z) {
            HdmiCecController.this.handleHotplug(i, z);
        }

        @VisibleForTesting
        public void onHotplugEvent(final int i, final boolean z) {
            HdmiCecController.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiCecController$HdmiCecCallback$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    HdmiCecController.HdmiCecCallback.this.lambda$onHotplugEvent$1(i, z);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class HdmiCecCallback10 extends IHdmiCecCallback.Stub {
        private final HdmiCecCallback mHdmiCecCallback;

        HdmiCecCallback10(HdmiCecCallback hdmiCecCallback) {
            this.mHdmiCecCallback = hdmiCecCallback;
        }

        public void onCecMessage(android.hardware.tv.cec.V1_0.CecMessage cecMessage) throws RemoteException {
            byte[] bArr = new byte[cecMessage.body.size()];
            for (int i = 0; i < cecMessage.body.size(); i++) {
                bArr[i] = ((Byte) cecMessage.body.get(i)).byteValue();
            }
            this.mHdmiCecCallback.onCecMessage(cecMessage.initiator, cecMessage.destination, bArr);
        }

        public void onHotplugEvent(HotplugEvent hotplugEvent) throws RemoteException {
            this.mHdmiCecCallback.onHotplugEvent(hotplugEvent.portId, hotplugEvent.connected);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class HdmiCecCallback11 extends IHdmiCecCallback.Stub {
        private final HdmiCecCallback mHdmiCecCallback;

        HdmiCecCallback11(HdmiCecCallback hdmiCecCallback) {
            this.mHdmiCecCallback = hdmiCecCallback;
        }

        public void onCecMessage_1_1(android.hardware.tv.cec.V1_1.CecMessage cecMessage) throws RemoteException {
            byte[] bArr = new byte[cecMessage.body.size()];
            for (int i = 0; i < cecMessage.body.size(); i++) {
                bArr[i] = ((Byte) cecMessage.body.get(i)).byteValue();
            }
            this.mHdmiCecCallback.onCecMessage(cecMessage.initiator, cecMessage.destination, bArr);
        }

        public void onCecMessage(android.hardware.tv.cec.V1_0.CecMessage cecMessage) throws RemoteException {
            byte[] bArr = new byte[cecMessage.body.size()];
            for (int i = 0; i < cecMessage.body.size(); i++) {
                bArr[i] = ((Byte) cecMessage.body.get(i)).byteValue();
            }
            this.mHdmiCecCallback.onCecMessage(cecMessage.initiator, cecMessage.destination, bArr);
        }

        public void onHotplugEvent(HotplugEvent hotplugEvent) throws RemoteException {
            this.mHdmiCecCallback.onHotplugEvent(hotplugEvent.portId, hotplugEvent.connected);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class HdmiCecCallbackAidl extends IHdmiCecCallback.Stub {
        private final HdmiCecCallback mHdmiCecCallback;

        public int getInterfaceVersion() throws RemoteException {
            return 1;
        }

        HdmiCecCallbackAidl(HdmiCecCallback hdmiCecCallback) {
            this.mHdmiCecCallback = hdmiCecCallback;
        }

        public void onCecMessage(CecMessage cecMessage) throws RemoteException {
            this.mHdmiCecCallback.onCecMessage(cecMessage.initiator, cecMessage.destination, cecMessage.body);
        }

        public synchronized String getInterfaceHash() throws RemoteException {
            return "cd956e3a0c2e6ade71693c85e9f0aeffa221ea26";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class HdmiConnectionCallbackAidl extends IHdmiConnectionCallback.Stub {
        private final HdmiCecCallback mHdmiCecCallback;

        public int getInterfaceVersion() throws RemoteException {
            return 1;
        }

        HdmiConnectionCallbackAidl(HdmiCecCallback hdmiCecCallback) {
            this.mHdmiCecCallback = hdmiCecCallback;
        }

        public void onHotplugEvent(boolean z, int i) throws RemoteException {
            this.mHdmiCecCallback.onHotplugEvent(i, z);
        }

        public synchronized String getInterfaceHash() throws RemoteException {
            return "85c26fa47f3c3062aa93ffc8bb0897a85c8cb118";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class MessageHistoryRecord extends Dumpable {
        private final boolean mIsReceived;
        private final HdmiCecMessage mMessage;
        private final List<String> mSendResults;

        MessageHistoryRecord(boolean z, HdmiCecMessage hdmiCecMessage, List<String> list) {
            this.mIsReceived = z;
            this.mMessage = hdmiCecMessage;
            this.mSendResults = list;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.android.server.hdmi.HdmiCecController.Dumpable
        public void dump(IndentingPrintWriter indentingPrintWriter, SimpleDateFormat simpleDateFormat) {
            indentingPrintWriter.print(this.mIsReceived ? "[R]" : "[S]");
            indentingPrintWriter.print(" time=");
            indentingPrintWriter.print(simpleDateFormat.format(new Date(this.mTime)));
            indentingPrintWriter.print(" message=");
            indentingPrintWriter.print(this.mMessage);
            StringBuilder sb = new StringBuilder();
            if (!this.mIsReceived && this.mSendResults != null) {
                sb.append(" (");
                sb.append(String.join(", ", this.mSendResults));
                sb.append(")");
            }
            indentingPrintWriter.println(sb);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class HotplugHistoryRecord extends Dumpable {
        private final boolean mConnected;
        private final int mPort;

        HotplugHistoryRecord(int i, boolean z) {
            this.mPort = i;
            this.mConnected = z;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.android.server.hdmi.HdmiCecController.Dumpable
        public void dump(IndentingPrintWriter indentingPrintWriter, SimpleDateFormat simpleDateFormat) {
            indentingPrintWriter.print("[H]");
            indentingPrintWriter.print(" time=");
            indentingPrintWriter.print(simpleDateFormat.format(new Date(this.mTime)));
            indentingPrintWriter.print(" hotplug port=");
            indentingPrintWriter.print(this.mPort);
            indentingPrintWriter.print(" connected=");
            indentingPrintWriter.println(this.mConnected);
        }
    }
}
