package vendor.oplus.hardware.misc.V1_0;

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

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IOplusMisc extends IBase {
    public static final String kInterfaceName = "vendor.oplus.hardware.misc@1.0::IOplusMisc";

    IHwBinder asBinder();

    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    DebugInfo getDebugInfo() throws RemoteException;

    ArrayList<byte[]> getHashChain() throws RemoteException;

    int initialize() throws RemoteException;

    ArrayList<String> interfaceChain() throws RemoteException;

    String interfaceDescriptor() throws RemoteException;

    int isMiscNodeSupport(int i, int i2) throws RemoteException;

    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    String miscReadNodeFile(int i, int i2) throws RemoteException;

    int miscWriteNodeFile(int i, int i2, String str) throws RemoteException;

    void notifySyspropsChanged() throws RemoteException;

    void ping() throws RemoteException;

    boolean saveCalibrationData(int i, ArrayList<Byte> arrayList, int i2) throws RemoteException;

    void setHALInstrumentation() throws RemoteException;

    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static IOplusMisc asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        IOplusMisc queryLocalInterface = iHwBinder.queryLocalInterface(kInterfaceName);
        if (queryLocalInterface != null && (queryLocalInterface instanceof IOplusMisc)) {
            return queryLocalInterface;
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

    static IOplusMisc castFrom(IHwInterface iHwInterface) {
        if (iHwInterface == null) {
            return null;
        }
        return asInterface(iHwInterface.asBinder());
    }

    static IOplusMisc getService(String str, boolean z) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str, z));
    }

    static IOplusMisc getService(boolean z) throws RemoteException {
        return getService("default", z);
    }

    @Deprecated
    static IOplusMisc getService(String str) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str));
    }

    @Deprecated
    static IOplusMisc getService() throws RemoteException {
        return getService("default");
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class Proxy implements IOplusMisc {
        private IHwBinder mRemote;

        public Proxy(IHwBinder iHwBinder) {
            Objects.requireNonNull(iHwBinder);
            this.mRemote = iHwBinder;
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException unused) {
                return "[class or subclass of vendor.oplus.hardware.misc@1.0::IOplusMisc]@Proxy";
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public int initialize() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IOplusMisc.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(1, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public int miscWriteNodeFile(int i, int i2, String str) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IOplusMisc.kInterfaceName);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32(i2);
            hwParcel.writeString(str);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(2, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public String miscReadNodeFile(int i, int i2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IOplusMisc.kInterfaceName);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32(i2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(3, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readString();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public int isMiscNodeSupport(int i, int i2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IOplusMisc.kInterfaceName);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32(i2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(4, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public boolean saveCalibrationData(int i, ArrayList<Byte> arrayList, int i2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IOplusMisc.kInterfaceName);
            hwParcel.writeInt32(i);
            hwParcel.writeInt8Vector(arrayList);
            hwParcel.writeInt32(i2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(5, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readBool();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public ArrayList<String> interfaceChain() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
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

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
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

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public String interfaceDescriptor() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
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

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public ArrayList<byte[]> getHashChain() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
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

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public void setHALInstrumentation() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256462420, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException {
            return this.mRemote.linkToDeath(deathRecipient, j);
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public void ping() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256921159, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public DebugInfo getDebugInfo() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
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

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public void notifySyspropsChanged() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(257120595, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(deathRecipient);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends HwBinder implements IOplusMisc {
        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) {
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public final String interfaceDescriptor() {
            return IOplusMisc.kInterfaceName;
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public final boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) {
            return true;
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public final void ping() {
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public final void setHALInstrumentation() {
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) {
            return true;
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(IOplusMisc.kInterfaceName, "android.hidl.base@1.0::IBase"));
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{-74, 110, 29, 52, 51, 24, -4, 119, 47, -76, 54, 8, 71, 121, 32, -19, -57, 126, -23, 45, 3, 120, 76, 82, -98, -119, -103, 119, -124, -61, 8, 98}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public final DebugInfo getDebugInfo() {
            DebugInfo debugInfo = new DebugInfo();
            debugInfo.pid = HidlSupport.getPidIfSharable();
            debugInfo.ptr = 0L;
            debugInfo.arch = 0;
            return debugInfo;
        }

        @Override // vendor.oplus.hardware.misc.V1_0.IOplusMisc
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        public IHwInterface queryLocalInterface(String str) {
            if (IOplusMisc.kInterfaceName.equals(str)) {
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
                hwParcel.enforceInterface(IOplusMisc.kInterfaceName);
                int initialize = initialize();
                hwParcel2.writeStatus(0);
                hwParcel2.writeInt32(initialize);
                hwParcel2.send();
                return;
            }
            if (i == 2) {
                hwParcel.enforceInterface(IOplusMisc.kInterfaceName);
                int miscWriteNodeFile = miscWriteNodeFile(hwParcel.readInt32(), hwParcel.readInt32(), hwParcel.readString());
                hwParcel2.writeStatus(0);
                hwParcel2.writeInt32(miscWriteNodeFile);
                hwParcel2.send();
                return;
            }
            if (i == 3) {
                hwParcel.enforceInterface(IOplusMisc.kInterfaceName);
                String miscReadNodeFile = miscReadNodeFile(hwParcel.readInt32(), hwParcel.readInt32());
                hwParcel2.writeStatus(0);
                hwParcel2.writeString(miscReadNodeFile);
                hwParcel2.send();
                return;
            }
            if (i == 4) {
                hwParcel.enforceInterface(IOplusMisc.kInterfaceName);
                int isMiscNodeSupport = isMiscNodeSupport(hwParcel.readInt32(), hwParcel.readInt32());
                hwParcel2.writeStatus(0);
                hwParcel2.writeInt32(isMiscNodeSupport);
                hwParcel2.send();
                return;
            }
            if (i == 5) {
                hwParcel.enforceInterface(IOplusMisc.kInterfaceName);
                boolean saveCalibrationData = saveCalibrationData(hwParcel.readInt32(), hwParcel.readInt8Vector(), hwParcel.readInt32());
                hwParcel2.writeStatus(0);
                hwParcel2.writeBool(saveCalibrationData);
                hwParcel2.send();
                return;
            }
            switch (i) {
                case 256067662:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    ArrayList<String> interfaceChain = interfaceChain();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeStringVector(interfaceChain);
                    hwParcel2.send();
                    return;
                case 256131655:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    debug(hwParcel.readNativeHandle(), hwParcel.readStringVector());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 256136003:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    String interfaceDescriptor = interfaceDescriptor();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeString(interfaceDescriptor);
                    hwParcel2.send();
                    return;
                case 256398152:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
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
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    setHALInstrumentation();
                    return;
                case 256921159:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    ping();
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 257049926:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    DebugInfo debugInfo = getDebugInfo();
                    hwParcel2.writeStatus(0);
                    debugInfo.writeToParcel(hwParcel2);
                    hwParcel2.send();
                    return;
                case 257120595:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    notifySyspropsChanged();
                    return;
                default:
                    return;
            }
        }
    }
}
