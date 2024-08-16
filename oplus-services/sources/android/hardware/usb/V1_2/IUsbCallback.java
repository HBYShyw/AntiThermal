package android.hardware.usb.V1_2;

import android.hardware.biometrics.face.AcquiredInfo;
import android.hardware.usb.V1_0.PortRole;
import android.hardware.usb.V1_1.PortStatus_1_1;
import android.hidl.base.V1_0.DebugInfo;
import android.hidl.base.V1_0.IBase;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.NativeHandle;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IUsbCallback extends android.hardware.usb.V1_1.IUsbCallback {
    public static final String kInterfaceName = "android.hardware.usb@1.2::IUsbCallback";

    @Override // android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
    IHwBinder asBinder();

    @Override // android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    @Override // android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
    DebugInfo getDebugInfo() throws RemoteException;

    @Override // android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
    ArrayList<byte[]> getHashChain() throws RemoteException;

    @Override // android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
    ArrayList<String> interfaceChain() throws RemoteException;

    @Override // android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
    String interfaceDescriptor() throws RemoteException;

    @Override // android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    void notifyPortStatusChange_1_2(ArrayList<PortStatus> arrayList, int i) throws RemoteException;

    @Override // android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws RemoteException;

    @Override // android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
    void ping() throws RemoteException;

    @Override // android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws RemoteException;

    @Override // android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static IUsbCallback asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        IHwInterface queryLocalInterface = iHwBinder.queryLocalInterface(kInterfaceName);
        if (queryLocalInterface != null && (queryLocalInterface instanceof IUsbCallback)) {
            return (IUsbCallback) queryLocalInterface;
        }
        Proxy proxy = new Proxy(iHwBinder);
        try {
            Iterator<String> it = proxy.interfaceChain().iterator();
            while (it.hasNext()) {
                if (it.next().equals(kInterfaceName)) {
                    return proxy;
                }
            }
        } catch (RemoteException unused) {
        }
        return null;
    }

    static IUsbCallback castFrom(IHwInterface iHwInterface) {
        if (iHwInterface == null) {
            return null;
        }
        return asInterface(iHwInterface.asBinder());
    }

    static IUsbCallback getService(String str, boolean z) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str, z));
    }

    static IUsbCallback getService(boolean z) throws RemoteException {
        return getService("default", z);
    }

    @Deprecated
    static IUsbCallback getService(String str) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str));
    }

    @Deprecated
    static IUsbCallback getService() throws RemoteException {
        return getService("default");
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Proxy implements IUsbCallback {
        private IHwBinder mRemote;

        public Proxy(IHwBinder iHwBinder) {
            Objects.requireNonNull(iHwBinder);
            this.mRemote = iHwBinder;
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException unused) {
                return "[class or subclass of android.hardware.usb@1.2::IUsbCallback]@Proxy";
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // android.hardware.usb.V1_0.IUsbCallback
        public void notifyPortStatusChange(ArrayList<android.hardware.usb.V1_0.PortStatus> arrayList, int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.usb.V1_0.IUsbCallback.kInterfaceName);
            android.hardware.usb.V1_0.PortStatus.writeVectorToParcel(hwParcel, arrayList);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(1, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.usb.V1_0.IUsbCallback
        public void notifyRoleSwitchStatus(String str, PortRole portRole, int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.usb.V1_0.IUsbCallback.kInterfaceName);
            hwParcel.writeString(str);
            portRole.writeToParcel(hwParcel);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(2, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.usb.V1_1.IUsbCallback
        public void notifyPortStatusChange_1_1(ArrayList<PortStatus_1_1> arrayList, int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.usb.V1_1.IUsbCallback.kInterfaceName);
            PortStatus_1_1.writeVectorToParcel(hwParcel, arrayList);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(3, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback
        public void notifyPortStatusChange_1_2(ArrayList<PortStatus> arrayList, int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IUsbCallback.kInterfaceName);
            PortStatus.writeVectorToParcel(hwParcel, arrayList);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(4, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public ArrayList<String> interfaceChain() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256067662, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readStringVector();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IBase.kInterfaceName);
            hwParcel.writeNativeHandle(nativeHandle);
            hwParcel.writeStringVector(arrayList);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256131655, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public String interfaceDescriptor() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256136003, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readString();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public ArrayList<byte[]> getHashChain() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256398152, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                ArrayList<byte[]> arrayList = new ArrayList<>();
                HwBlob readBuffer = hwParcel2.readBuffer(16L);
                int int32 = readBuffer.getInt32(8L);
                HwBlob readEmbeddedBuffer = hwParcel2.readEmbeddedBuffer(int32 * 32, readBuffer.handle(), 0L, true);
                arrayList.clear();
                for (int i = 0; i < int32; i++) {
                    byte[] bArr = new byte[32];
                    readEmbeddedBuffer.copyToInt8Array(i * 32, bArr, 32);
                    arrayList.add(bArr);
                }
                return arrayList;
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public void setHALInstrumentation() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256462420, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException {
            return this.mRemote.linkToDeath(deathRecipient, j);
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public void ping() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256921159, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public DebugInfo getDebugInfo() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(257049926, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                DebugInfo debugInfo = new DebugInfo();
                debugInfo.readFromParcel(hwParcel2);
                return debugInfo;
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public void notifySyspropsChanged() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(257120595, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(deathRecipient);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends HwBinder implements IUsbCallback {
        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public IHwBinder asBinder() {
            return this;
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) {
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public final String interfaceDescriptor() {
            return IUsbCallback.kInterfaceName;
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) {
            return true;
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) {
            return true;
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(IUsbCallback.kInterfaceName, android.hardware.usb.V1_1.IUsbCallback.kInterfaceName, android.hardware.usb.V1_0.IUsbCallback.kInterfaceName, IBase.kInterfaceName));
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{70, -103, 108, -46, -95, -58, 98, 97, -89, 90, 31, 110, -54, -38, 119, -18, -75, -122, 30, -78, 100, -6, 57, -71, -106, 84, -113, -32, -89, -14, 45, -45}, new byte[]{AcquiredInfo.ROLL_TOO_EXTREME, -91, Byte.MIN_VALUE, -29, 90, -16, AcquiredInfo.TILT_TOO_EXTREME, 112, -95, -23, 119, 65, 119, -59, 29, -75, 29, -122, 114, -26, AcquiredInfo.ROLL_TOO_EXTREME, -101, -96, 8, 81, -26, 84, -26, -118, 77, 125, -1}, new byte[]{75, -25, -120, 30, 65, 27, -92, 39, -124, -65, 91, 115, 84, -63, 74, -32, -49, AcquiredInfo.SENSOR_DIRTY, AcquiredInfo.TOO_SIMILAR, 4, -45, -108, 51, -86, -20, -86, -80, -47, -98, -87, -109, 84}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, AcquiredInfo.ROLL_TOO_EXTREME, -109, 36, -72, 59, AcquiredInfo.FIRST_FRAME_RECEIVED, -54, 76}));
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public final DebugInfo getDebugInfo() {
            DebugInfo debugInfo = new DebugInfo();
            debugInfo.pid = HidlSupport.getPidIfSharable();
            debugInfo.ptr = 0L;
            debugInfo.arch = 0;
            return debugInfo;
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback, android.hardware.usb.V1_1.IUsbCallback, android.hardware.usb.V1_0.IUsbCallback, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        public IHwInterface queryLocalInterface(String str) {
            if (IUsbCallback.kInterfaceName.equals(str)) {
                return this;
            }
            return null;
        }

        public void registerAsService(String str) throws RemoteException {
            registerService(str);
        }

        public String toString() {
            return interfaceDescriptor() + "@Stub";
        }

        public void onTransact(int i, HwParcel hwParcel, HwParcel hwParcel2, int i2) throws RemoteException {
            if (i == 1) {
                hwParcel.enforceInterface(android.hardware.usb.V1_0.IUsbCallback.kInterfaceName);
                notifyPortStatusChange(android.hardware.usb.V1_0.PortStatus.readVectorFromParcel(hwParcel), hwParcel.readInt32());
                return;
            }
            if (i == 2) {
                hwParcel.enforceInterface(android.hardware.usb.V1_0.IUsbCallback.kInterfaceName);
                String readString = hwParcel.readString();
                PortRole portRole = new PortRole();
                portRole.readFromParcel(hwParcel);
                notifyRoleSwitchStatus(readString, portRole, hwParcel.readInt32());
                return;
            }
            if (i == 3) {
                hwParcel.enforceInterface(android.hardware.usb.V1_1.IUsbCallback.kInterfaceName);
                notifyPortStatusChange_1_1(PortStatus_1_1.readVectorFromParcel(hwParcel), hwParcel.readInt32());
                return;
            }
            if (i == 4) {
                hwParcel.enforceInterface(IUsbCallback.kInterfaceName);
                notifyPortStatusChange_1_2(PortStatus.readVectorFromParcel(hwParcel), hwParcel.readInt32());
                return;
            }
            switch (i) {
                case 256067662:
                    hwParcel.enforceInterface(IBase.kInterfaceName);
                    ArrayList<String> interfaceChain = interfaceChain();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeStringVector(interfaceChain);
                    hwParcel2.send();
                    return;
                case 256131655:
                    hwParcel.enforceInterface(IBase.kInterfaceName);
                    debug(hwParcel.readNativeHandle(), hwParcel.readStringVector());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 256136003:
                    hwParcel.enforceInterface(IBase.kInterfaceName);
                    String interfaceDescriptor = interfaceDescriptor();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeString(interfaceDescriptor);
                    hwParcel2.send();
                    return;
                case 256398152:
                    hwParcel.enforceInterface(IBase.kInterfaceName);
                    ArrayList<byte[]> hashChain = getHashChain();
                    hwParcel2.writeStatus(0);
                    HwBlob hwBlob = new HwBlob(16);
                    int size = hashChain.size();
                    hwBlob.putInt32(8L, size);
                    hwBlob.putBool(12L, false);
                    HwBlob hwBlob2 = new HwBlob(size * 32);
                    for (int i3 = 0; i3 < size; i3++) {
                        long j = i3 * 32;
                        byte[] bArr = hashChain.get(i3);
                        if (bArr == null || bArr.length != 32) {
                            throw new IllegalArgumentException("Array element is not of the expected length");
                        }
                        hwBlob2.putInt8Array(j, bArr);
                    }
                    hwBlob.putBlob(0L, hwBlob2);
                    hwParcel2.writeBuffer(hwBlob);
                    hwParcel2.send();
                    return;
                case 256462420:
                    hwParcel.enforceInterface(IBase.kInterfaceName);
                    setHALInstrumentation();
                    return;
                case 256921159:
                    hwParcel.enforceInterface(IBase.kInterfaceName);
                    ping();
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 257049926:
                    hwParcel.enforceInterface(IBase.kInterfaceName);
                    DebugInfo debugInfo = getDebugInfo();
                    hwParcel2.writeStatus(0);
                    debugInfo.writeToParcel(hwParcel2);
                    hwParcel2.send();
                    return;
                case 257120595:
                    hwParcel.enforceInterface(IBase.kInterfaceName);
                    notifySyspropsChanged();
                    return;
                default:
                    return;
            }
        }
    }
}
