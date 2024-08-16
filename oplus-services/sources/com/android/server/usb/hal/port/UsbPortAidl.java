package com.android.server.usb.hal.port;

import android.hardware.usb.AltModeData;
import android.hardware.usb.DisplayPortAltModeInfo;
import android.hardware.usb.IUsb;
import android.hardware.usb.IUsbCallback;
import android.hardware.usb.IUsbOperationInternal;
import android.hardware.usb.PortRole;
import android.hardware.usb.PortStatus;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbPort;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.IntArray;
import android.util.LongSparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.usb.UsbPortManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UsbPortAidl implements UsbPortHal {
    public static final int AIDL_USB_DATA_STATUS_DISABLED_CONTAMINANT = 3;
    public static final int AIDL_USB_DATA_STATUS_DISABLED_DEBUG = 6;
    public static final int AIDL_USB_DATA_STATUS_DISABLED_DOCK = 4;
    public static final int AIDL_USB_DATA_STATUS_DISABLED_DOCK_DEVICE_MODE = 8;
    public static final int AIDL_USB_DATA_STATUS_DISABLED_DOCK_HOST_MODE = 7;
    public static final int AIDL_USB_DATA_STATUS_DISABLED_FORCE = 5;
    public static final int AIDL_USB_DATA_STATUS_DISABLED_OVERHEAT = 2;
    public static final int AIDL_USB_DATA_STATUS_ENABLED = 1;
    public static final int AIDL_USB_DATA_STATUS_UNKNOWN = 0;
    private static final String TAG = "UsbPortAidl";
    private static final String USB_AIDL_SERVICE = "android.hardware.usb.IUsb/default";
    private static final LongSparseArray<IUsbOperationInternal> sCallbacks = new LongSparseArray<>();
    private IBinder mBinder;
    private HALCallback mHALCallback;
    private final Object mLock = new Object();
    private UsbPortManager mPortManager;

    @GuardedBy({"mLock"})
    private IUsb mProxy;
    public IndentingPrintWriter mPw;
    private boolean mSystemReady;
    private long mTransactionId;

    @Override // com.android.server.usb.hal.port.UsbPortHal
    @UsbManager.UsbHalVersion
    public int getUsbHalVersion() throws RemoteException {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                throw new RemoteException("IUsb not initialized yet");
            }
        }
        UsbPortManager.logAndPrint(4, null, "USB HAL AIDL version: USB_HAL_V2_0");
        return 20;
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void systemReady() {
        this.mSystemReady = true;
    }

    public void serviceDied() {
        UsbPortManager.logAndPrint(6, this.mPw, "Usb AIDL hal service died");
        synchronized (this.mLock) {
            this.mProxy = null;
        }
        connectToProxy(null);
    }

    private void connectToProxy(IndentingPrintWriter indentingPrintWriter) {
        synchronized (this.mLock) {
            if (this.mProxy != null) {
                return;
            }
            try {
                IBinder waitForService = ServiceManager.waitForService(USB_AIDL_SERVICE);
                this.mBinder = waitForService;
                this.mProxy = IUsb.Stub.asInterface(waitForService);
                this.mBinder.linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.usb.hal.port.UsbPortAidl$$ExternalSyntheticLambda0
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        UsbPortAidl.this.serviceDied();
                    }
                }, 0);
                this.mProxy.setCallback(this.mHALCallback);
                IUsb iUsb = this.mProxy;
                long j = this.mTransactionId + 1;
                this.mTransactionId = j;
                iUsb.queryPortStatus(j);
            } catch (RemoteException e) {
                UsbPortManager.logAndPrintException(indentingPrintWriter, "connectToProxy: usb hal service not responding", e);
            } catch (NoSuchElementException e2) {
                UsbPortManager.logAndPrintException(indentingPrintWriter, "connectToProxy: usb hal service not found. Did the service fail to start?", e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isServicePresent(IndentingPrintWriter indentingPrintWriter) {
        try {
            return ServiceManager.isDeclared(USB_AIDL_SERVICE);
        } catch (NoSuchElementException e) {
            UsbPortManager.logAndPrintException(indentingPrintWriter, "connectToProxy: usb Aidl hal service not found.", e);
            return false;
        }
    }

    public UsbPortAidl(UsbPortManager usbPortManager, IndentingPrintWriter indentingPrintWriter) {
        Objects.requireNonNull(usbPortManager);
        this.mPortManager = usbPortManager;
        this.mPw = indentingPrintWriter;
        this.mHALCallback = new HALCallback(null, this.mPortManager, this);
        connectToProxy(this.mPw);
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void enableContaminantPresenceDetection(String str, boolean z, long j) {
        synchronized (this.mLock) {
            IUsb iUsb = this.mProxy;
            if (iUsb == null) {
                UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry ! opID: " + j);
                return;
            }
            try {
                iUsb.enableContaminantPresenceDetection(str, z, j);
            } catch (RemoteException e) {
                UsbPortManager.logAndPrintException(this.mPw, "Failed to set contaminant detection. opID:" + j, e);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void queryPortStatus(long j) {
        synchronized (this.mLock) {
            IUsb iUsb = this.mProxy;
            if (iUsb == null) {
                UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry ! opID:" + j);
                return;
            }
            try {
                iUsb.queryPortStatus(j);
            } catch (RemoteException e) {
                UsbPortManager.logAndPrintException(null, "ServiceStart: Failed to query port status. opID:" + j, e);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void switchMode(String str, int i, long j) {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry ! opID:" + j);
                return;
            }
            PortRole portRole = new PortRole();
            portRole.setMode((byte) i);
            try {
                this.mProxy.switchRole(str, portRole, j);
            } catch (RemoteException e) {
                UsbPortManager.logAndPrintException(this.mPw, "Failed to set the USB port mode: portId=" + str + ", newMode=" + UsbPort.modeToString(i) + "opID:" + j, e);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void switchPowerRole(String str, int i, long j) {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry ! opID:" + j);
                return;
            }
            PortRole portRole = new PortRole();
            portRole.setPowerRole((byte) i);
            try {
                this.mProxy.switchRole(str, portRole, j);
            } catch (RemoteException e) {
                UsbPortManager.logAndPrintException(this.mPw, "Failed to set the USB power role: portId=" + str + ", newPowerRole=" + UsbPort.powerRoleToString(i) + "opID:" + j, e);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void switchDataRole(String str, int i, long j) {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry ! opID:" + j);
                return;
            }
            PortRole portRole = new PortRole();
            portRole.setDataRole((byte) i);
            try {
                this.mProxy.switchRole(str, portRole, j);
            } catch (RemoteException e) {
                UsbPortManager.logAndPrintException(this.mPw, "Failed to set the USB data role: portId=" + str + ", newDataRole=" + UsbPort.dataRoleToString(i) + "opID:" + j, e);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void resetUsbPort(String str, long j, IUsbOperationInternal iUsbOperationInternal) {
        long j2;
        LongSparseArray<IUsbOperationInternal> longSparseArray;
        Objects.requireNonNull(str);
        Objects.requireNonNull(iUsbOperationInternal);
        synchronized (this.mLock) {
            try {
                if (this.mProxy == null) {
                    UsbPortManager.logAndPrint(6, this.mPw, "resetUsbPort: Proxy is null. Retry !opID:" + j);
                    iUsbOperationInternal.onOperationComplete(1);
                }
                j2 = j;
                while (true) {
                    try {
                        longSparseArray = sCallbacks;
                        if (longSparseArray.get(j2) == null) {
                            break;
                        } else {
                            j2 = ThreadLocalRandom.current().nextInt();
                        }
                    } catch (RemoteException e) {
                        e = e;
                        UsbPortManager.logAndPrintException(this.mPw, "resetUsbPort: Failed to call onOperationComplete portID=" + str + "opID:" + j, e);
                        sCallbacks.remove(j2);
                    }
                }
                if (j2 != j) {
                    UsbPortManager.logAndPrint(4, this.mPw, "resetUsbPort: operationID exists ! opID:" + j + " key:" + j2);
                }
                try {
                    longSparseArray.put(j2, iUsbOperationInternal);
                    this.mProxy.resetUsbPort(str, j2);
                } catch (RemoteException e2) {
                    UsbPortManager.logAndPrintException(this.mPw, "resetUsbPort: Failed to resetUsbPort: portID=" + str + "opId:" + j, e2);
                    iUsbOperationInternal.onOperationComplete(1);
                    sCallbacks.remove(j2);
                }
            } catch (RemoteException e3) {
                e = e3;
                j2 = j;
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public boolean enableUsbData(String str, boolean z, long j, IUsbOperationInternal iUsbOperationInternal) {
        long j2;
        LongSparseArray<IUsbOperationInternal> longSparseArray;
        Objects.requireNonNull(str);
        Objects.requireNonNull(iUsbOperationInternal);
        synchronized (this.mLock) {
            try {
                try {
                    if (this.mProxy == null) {
                        UsbPortManager.logAndPrint(6, this.mPw, "enableUsbData: Proxy is null. Retry !opID:" + j);
                        iUsbOperationInternal.onOperationComplete(1);
                        return false;
                    }
                    j2 = j;
                    while (true) {
                        try {
                            longSparseArray = sCallbacks;
                            if (longSparseArray.get(j2) == null) {
                                break;
                            }
                            j2 = ThreadLocalRandom.current().nextInt();
                        } catch (RemoteException e) {
                            e = e;
                            UsbPortManager.logAndPrintException(this.mPw, "enableUsbData: Failed to call onOperationComplete portID=" + str + "opID:" + j, e);
                            sCallbacks.remove(j2);
                            return false;
                        }
                    }
                    if (j2 != j) {
                        UsbPortManager.logAndPrint(4, this.mPw, "enableUsbData: operationID exists ! opID:" + j + " key:" + j2);
                    }
                    try {
                        longSparseArray.put(j2, iUsbOperationInternal);
                        this.mProxy.enableUsbData(str, z, j2);
                        return true;
                    } catch (RemoteException e2) {
                        UsbPortManager.logAndPrintException(this.mPw, "enableUsbData: Failed to invoke enableUsbData: portID=" + str + "opID:" + j, e2);
                        iUsbOperationInternal.onOperationComplete(1);
                        sCallbacks.remove(j2);
                        return false;
                    }
                } catch (RemoteException e3) {
                    e = e3;
                    j2 = j;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void enableLimitPowerTransfer(String str, boolean z, long j, IUsbOperationInternal iUsbOperationInternal) {
        LongSparseArray<IUsbOperationInternal> longSparseArray;
        Objects.requireNonNull(str);
        synchronized (this.mLock) {
            try {
            } catch (RemoteException e) {
                UsbPortManager.logAndPrintException(this.mPw, "enableLimitPowerTransfer: Failed to call onOperationComplete portID=" + str + " opID:" + j, e);
            }
            if (this.mProxy == null) {
                UsbPortManager.logAndPrint(6, this.mPw, "enableLimitPowerTransfer: Proxy is null. Retry !opID:" + j);
                iUsbOperationInternal.onOperationComplete(1);
                return;
            }
            long j2 = j;
            while (true) {
                longSparseArray = sCallbacks;
                if (longSparseArray.get(j2) == null) {
                    break;
                } else {
                    j2 = ThreadLocalRandom.current().nextInt();
                }
            }
            if (j2 != j) {
                UsbPortManager.logAndPrint(4, this.mPw, "enableUsbData: operationID exists ! opID:" + j + " key:" + j2);
            }
            try {
                longSparseArray.put(j2, iUsbOperationInternal);
                this.mProxy.limitPowerTransfer(str, z, j2);
            } catch (RemoteException e2) {
                UsbPortManager.logAndPrintException(this.mPw, "enableLimitPowerTransfer: Failed while invoking AIDL HAL portID=" + str + " opID:" + j, e2);
                if (iUsbOperationInternal != null) {
                    iUsbOperationInternal.onOperationComplete(1);
                }
                sCallbacks.remove(j2);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void enableUsbDataWhileDocked(String str, long j, IUsbOperationInternal iUsbOperationInternal) {
        LongSparseArray<IUsbOperationInternal> longSparseArray;
        Objects.requireNonNull(str);
        synchronized (this.mLock) {
            try {
            } catch (RemoteException e) {
                UsbPortManager.logAndPrintException(this.mPw, "enableUsbDataWhileDocked: Failed to call onOperationComplete portID=" + str + " opID:" + j, e);
            }
            if (this.mProxy == null) {
                UsbPortManager.logAndPrint(6, this.mPw, "enableUsbDataWhileDocked: Proxy is null. Retry !opID:" + j);
                iUsbOperationInternal.onOperationComplete(1);
                return;
            }
            long j2 = j;
            while (true) {
                longSparseArray = sCallbacks;
                if (longSparseArray.get(j2) == null) {
                    break;
                } else {
                    j2 = ThreadLocalRandom.current().nextInt();
                }
            }
            if (j2 != j) {
                UsbPortManager.logAndPrint(4, this.mPw, "enableUsbDataWhileDocked: operationID exists ! opID:" + j + " key:" + j2);
            }
            try {
                longSparseArray.put(j2, iUsbOperationInternal);
                this.mProxy.enableUsbDataWhileDocked(str, j2);
            } catch (RemoteException e2) {
                UsbPortManager.logAndPrintException(this.mPw, "enableUsbDataWhileDocked: error while invoking halportID=" + str + " opID:" + j, e2);
                if (iUsbOperationInternal != null) {
                    iUsbOperationInternal.onOperationComplete(1);
                }
                sCallbacks.remove(j2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class HALCallback extends IUsbCallback.Stub {
        public UsbPortManager mPortManager;
        public IndentingPrintWriter mPw;
        public UsbPortAidl mUsbPortAidl;

        private int toDisplayPortAltModeNumLanesInt(int i) {
            switch (i) {
                case 1:
                case 3:
                case 5:
                    return 4;
                case 2:
                case 4:
                case 6:
                    return 2;
                default:
                    return 0;
            }
        }

        public String getInterfaceHash() {
            return "6c1263ce61606107f41a9edb66be1783d5881f00";
        }

        public int getInterfaceVersion() {
            return 2;
        }

        HALCallback(IndentingPrintWriter indentingPrintWriter, UsbPortManager usbPortManager, UsbPortAidl usbPortAidl) {
            this.mPw = indentingPrintWriter;
            this.mPortManager = usbPortManager;
            this.mUsbPortAidl = usbPortAidl;
        }

        private int toPortMode(byte b) {
            if (b == 0) {
                return 0;
            }
            int i = 1;
            if (b != 1) {
                i = 2;
                if (b != 2) {
                    i = 3;
                    if (b != 3) {
                        i = 4;
                        if (b != 4) {
                            if (b == 5) {
                                return 8;
                            }
                            UsbPortManager.logAndPrint(6, this.mPw, "Unrecognized aidlPortMode:" + ((int) b));
                            return 0;
                        }
                    }
                }
            }
            return i;
        }

        private int toSupportedModes(byte[] bArr) {
            int i = 0;
            for (byte b : bArr) {
                i |= toPortMode(b);
            }
            return i;
        }

        private int toContaminantProtectionStatus(byte b) {
            if (b == 0) {
                return 0;
            }
            int i = 1;
            if (b != 1) {
                i = 2;
                if (b != 2) {
                    if (b == 3) {
                        return 4;
                    }
                    if (b == 4) {
                        return 8;
                    }
                    UsbPortManager.logAndPrint(6, this.mPw, "Unrecognized aidlContaminantProtection:" + ((int) b));
                    return 0;
                }
            }
            return i;
        }

        private int toSupportedContaminantProtectionModes(byte[] bArr) {
            int i = 0;
            for (byte b : bArr) {
                i |= toContaminantProtectionStatus(b);
            }
            return i;
        }

        private int toUsbDataStatusInt(byte[] bArr) {
            int i;
            int i2 = 0;
            for (byte b : bArr) {
                switch (b) {
                    case 1:
                        i2 |= 1;
                        continue;
                    case 2:
                        i2 |= 2;
                        continue;
                    case 3:
                        i2 |= 4;
                        continue;
                    case 4:
                        i2 = i2 | 8 | 64 | 128;
                        continue;
                    case 5:
                        i2 |= 16;
                        continue;
                    case 6:
                        i2 |= 32;
                        continue;
                    case 7:
                        i = i2 | 64;
                        break;
                    case 8:
                        i = i2 | 128;
                        break;
                    default:
                        i2 |= 0;
                        continue;
                }
                i2 = i | 8;
            }
            UsbPortManager.logAndPrint(4, this.mPw, "AIDL UsbDataStatus:" + i2);
            return i2;
        }

        private int[] formatComplianceWarnings(int[] iArr) {
            Objects.requireNonNull(iArr);
            IntArray intArray = new IntArray();
            Arrays.sort(iArr);
            for (int i : iArr) {
                if (intArray.indexOf(i) == -1 && i >= 1) {
                    if (i > 4) {
                        intArray.add(1);
                    } else {
                        intArray.add(i);
                    }
                }
            }
            return intArray.toArray();
        }

        private int toSupportedAltModesInt(AltModeData[] altModeDataArr) {
            int i = 0;
            for (AltModeData altModeData : altModeDataArr) {
                if (altModeData.getTag() == 0) {
                    i |= 1;
                }
            }
            return i;
        }

        private DisplayPortAltModeInfo formatDisplayPortAltModeInfo(AltModeData[] altModeDataArr) {
            for (AltModeData altModeData : altModeDataArr) {
                if (altModeData.getTag() == 0) {
                    AltModeData.DisplayPortAltModeData displayPortAltModeData = altModeData.getDisplayPortAltModeData();
                    return new DisplayPortAltModeInfo(displayPortAltModeData.partnerSinkStatus, displayPortAltModeData.cableStatus, toDisplayPortAltModeNumLanesInt(displayPortAltModeData.pinAssignment), displayPortAltModeData.hpd, displayPortAltModeData.linkTrainingStatus);
                }
            }
            return null;
        }

        public void notifyPortStatusChange(PortStatus[] portStatusArr, int i) {
            PortStatus[] portStatusArr2 = portStatusArr;
            if (this.mUsbPortAidl.mSystemReady) {
                if (i != 0) {
                    UsbPortManager.logAndPrint(6, this.mPw, "port status enquiry failed");
                    return;
                }
                ArrayList<RawPortInfo> arrayList = new ArrayList<>();
                int i2 = 0;
                for (int length = portStatusArr2.length; i2 < length; length = length) {
                    PortStatus portStatus = portStatusArr2[i2];
                    arrayList.add(new RawPortInfo(portStatus.portName, toSupportedModes(portStatus.supportedModes), toSupportedContaminantProtectionModes(portStatus.supportedContaminantProtectionModes), toPortMode(portStatus.currentMode), portStatus.canChangeMode, portStatus.currentPowerRole, portStatus.canChangePowerRole, portStatus.currentDataRole, portStatus.canChangeDataRole, portStatus.supportsEnableContaminantPresenceProtection, toContaminantProtectionStatus(portStatus.contaminantProtectionStatus), portStatus.supportsEnableContaminantPresenceDetection, portStatus.contaminantDetectionStatus, toUsbDataStatusInt(portStatus.usbDataStatus), portStatus.powerTransferLimited, portStatus.powerBrickStatus, portStatus.supportsComplianceWarnings, formatComplianceWarnings(portStatus.complianceWarnings), portStatus.plugOrientation, toSupportedAltModesInt(portStatus.supportedAltModes), formatDisplayPortAltModeInfo(portStatus.supportedAltModes)));
                    UsbPortManager.logAndPrint(4, this.mPw, "ClientCallback AIDL V1: " + portStatus.portName);
                    i2++;
                    portStatusArr2 = portStatusArr;
                }
                this.mPortManager.updatePorts(arrayList);
            }
        }

        public void notifyRoleSwitchStatus(String str, PortRole portRole, int i, long j) {
            if (i == 0) {
                UsbPortManager.logAndPrint(4, this.mPw, str + " role switch successful. opID:" + j);
                return;
            }
            UsbPortManager.logAndPrint(6, this.mPw, str + " role switch failed. err:" + i + "opID:" + j);
        }

        public void notifyQueryPortStatus(String str, int i, long j) {
            if (i == 0) {
                UsbPortManager.logAndPrint(4, this.mPw, str + ": opID:" + j + " successful");
                return;
            }
            UsbPortManager.logAndPrint(6, this.mPw, str + ": opID:" + j + " failed. err:" + i);
        }

        public void notifyEnableUsbDataStatus(String str, boolean z, int i, long j) {
            if (i == 0) {
                UsbPortManager.logAndPrint(4, this.mPw, "notifyEnableUsbDataStatus:" + str + ": opID:" + j + " enable:" + z);
            } else {
                UsbPortManager.logAndPrint(6, this.mPw, str + "notifyEnableUsbDataStatus: opID:" + j + " failed. err:" + i);
            }
            try {
                ((IUsbOperationInternal) UsbPortAidl.sCallbacks.get(j)).onOperationComplete(i == 0 ? 0 : 1);
            } catch (RemoteException e) {
                UsbPortManager.logAndPrintException(this.mPw, "notifyEnableUsbDataStatus: Failed to call onOperationComplete", e);
            }
        }

        public void notifyContaminantEnabledStatus(String str, boolean z, int i, long j) {
            if (i == 0) {
                UsbPortManager.logAndPrint(4, this.mPw, "notifyContaminantEnabledStatus:" + str + ": opID:" + j + " enable:" + z);
                return;
            }
            UsbPortManager.logAndPrint(6, this.mPw, str + "notifyContaminantEnabledStatus: opID:" + j + " failed. err:" + i);
        }

        public void notifyLimitPowerTransferStatus(String str, boolean z, int i, long j) {
            if (i == 0) {
                UsbPortManager.logAndPrint(4, this.mPw, str + ": opID:" + j + " successful");
            } else {
                UsbPortManager.logAndPrint(6, this.mPw, str + "notifyLimitPowerTransferStatus: opID:" + j + " failed. err:" + i);
            }
            try {
                if (((IUsbOperationInternal) UsbPortAidl.sCallbacks.get(j)) != null) {
                    ((IUsbOperationInternal) UsbPortAidl.sCallbacks.get(j)).onOperationComplete(i == 0 ? 0 : 1);
                }
            } catch (RemoteException e) {
                UsbPortManager.logAndPrintException(this.mPw, "enableLimitPowerTransfer: Failed to call onOperationComplete", e);
            }
        }

        public void notifyEnableUsbDataWhileDockedStatus(String str, int i, long j) {
            if (i == 0) {
                UsbPortManager.logAndPrint(4, this.mPw, str + ": opID:" + j + " successful");
            } else {
                UsbPortManager.logAndPrint(6, this.mPw, str + "notifyEnableUsbDataWhileDockedStatus: opID:" + j + " failed. err:" + i);
            }
            try {
                if (((IUsbOperationInternal) UsbPortAidl.sCallbacks.get(j)) != null) {
                    ((IUsbOperationInternal) UsbPortAidl.sCallbacks.get(j)).onOperationComplete(i == 0 ? 0 : 1);
                }
            } catch (RemoteException e) {
                UsbPortManager.logAndPrintException(this.mPw, "notifyEnableUsbDataWhileDockedStatus: Failed to call onOperationComplete", e);
            }
        }

        public void notifyResetUsbPortStatus(String str, int i, long j) {
            if (i == 0) {
                UsbPortManager.logAndPrint(4, this.mPw, "notifyResetUsbPortStatus:" + str + ": opID:" + j);
            } else {
                UsbPortManager.logAndPrint(6, this.mPw, str + "notifyEnableUsbDataStatus: opID:" + j + " failed. err:" + i);
            }
            try {
                ((IUsbOperationInternal) UsbPortAidl.sCallbacks.get(j)).onOperationComplete(i == 0 ? 0 : 1);
            } catch (RemoteException e) {
                UsbPortManager.logAndPrintException(this.mPw, "notifyResetUsbPortStatus: Failed to call onOperationComplete", e);
            }
        }
    }
}
