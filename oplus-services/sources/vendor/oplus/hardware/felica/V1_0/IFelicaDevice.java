package vendor.oplus.hardware.felica.V1_0;

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
public interface IFelicaDevice extends IBase {
    public static final String kInterfaceName = "vendor.oplus.hardware.felica@1.0::IFelicaDevice";

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface getFelicaLockKeyCallback {
        void onValues(ArrayList<Byte> arrayList, byte b);
    }

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface getFelicaLockStatusCallback {
        void onValues(boolean z, byte b);
    }

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface oplusEngineerNxpPnscrCurrentCallback {
        void onValues(ArrayList<Byte> arrayList, byte b);
    }

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface oplusEngineerNxpPnscrFreqCallback {
        void onValues(ArrayList<Byte> arrayList, byte b);
    }

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface oplusEngineer_NxpPnscrEse_3Callback {
        void onValues(ArrayList<Byte> arrayList, byte b);
    }

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface oplusEngineer_NxpPnscrSpcCallback {
        void onValues(ArrayList<Byte> arrayList, byte b);
    }

    IHwBinder asBinder();

    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    byte eraseFelicaLockData() throws RemoteException;

    DebugInfo getDebugInfo() throws RemoteException;

    void getFelicaLockKey(getFelicaLockKeyCallback getfelicalockkeycallback) throws RemoteException;

    void getFelicaLockStatus(getFelicaLockStatusCallback getfelicalockstatuscallback) throws RemoteException;

    ArrayList<byte[]> getHashChain() throws RemoteException;

    ArrayList<String> interfaceChain() throws RemoteException;

    String interfaceDescriptor() throws RemoteException;

    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    void notifySyspropsChanged() throws RemoteException;

    void oplusEngineerNxpPnscrCurrent(oplusEngineerNxpPnscrCurrentCallback oplusengineernxppnscrcurrentcallback) throws RemoteException;

    void oplusEngineerNxpPnscrFreq(oplusEngineerNxpPnscrFreqCallback oplusengineernxppnscrfreqcallback) throws RemoteException;

    void oplusEngineer_NxpPnscrEse_3(oplusEngineer_NxpPnscrEse_3Callback oplusengineer_nxppnscrese_3callback) throws RemoteException;

    void oplusEngineer_NxpPnscrSpc(oplusEngineer_NxpPnscrSpcCallback oplusengineer_nxppnscrspccallback) throws RemoteException;

    void ping() throws RemoteException;

    byte setFelicaLockKey(ArrayList<Byte> arrayList) throws RemoteException;

    byte setFelicaLockStatus(boolean z) throws RemoteException;

    void setHALInstrumentation() throws RemoteException;

    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static IFelicaDevice asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        IFelicaDevice queryLocalInterface = iHwBinder.queryLocalInterface(kInterfaceName);
        if (queryLocalInterface != null && (queryLocalInterface instanceof IFelicaDevice)) {
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

    static IFelicaDevice castFrom(IHwInterface iHwInterface) {
        if (iHwInterface == null) {
            return null;
        }
        return asInterface(iHwInterface.asBinder());
    }

    static IFelicaDevice getService(String str, boolean z) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str, z));
    }

    static IFelicaDevice getService(boolean z) throws RemoteException {
        return getService("default", z);
    }

    @Deprecated
    static IFelicaDevice getService(String str) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str));
    }

    @Deprecated
    static IFelicaDevice getService() throws RemoteException {
        return getService("default");
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class Proxy implements IFelicaDevice {
        private IHwBinder mRemote;

        public Proxy(IHwBinder iHwBinder) {
            Objects.requireNonNull(iHwBinder);
            this.mRemote = iHwBinder;
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException unused) {
                return "[class or subclass of vendor.oplus.hardware.felica@1.0::IFelicaDevice]@Proxy";
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public void getFelicaLockStatus(getFelicaLockStatusCallback getfelicalockstatuscallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IFelicaDevice.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(1, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                getfelicalockstatuscallback.onValues(hwParcel2.readBool(), hwParcel2.readInt8());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public byte setFelicaLockStatus(boolean z) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IFelicaDevice.kInterfaceName);
            hwParcel.writeBool(z);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(2, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt8();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public void getFelicaLockKey(getFelicaLockKeyCallback getfelicalockkeycallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IFelicaDevice.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(3, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                getfelicalockkeycallback.onValues(hwParcel2.readInt8Vector(), hwParcel2.readInt8());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public byte setFelicaLockKey(ArrayList<Byte> arrayList) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IFelicaDevice.kInterfaceName);
            hwParcel.writeInt8Vector(arrayList);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(4, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt8();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public byte eraseFelicaLockData() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IFelicaDevice.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(5, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt8();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public void oplusEngineer_NxpPnscrSpc(oplusEngineer_NxpPnscrSpcCallback oplusengineer_nxppnscrspccallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IFelicaDevice.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(6, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                oplusengineer_nxppnscrspccallback.onValues(hwParcel2.readInt8Vector(), hwParcel2.readInt8());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public void oplusEngineer_NxpPnscrEse_3(oplusEngineer_NxpPnscrEse_3Callback oplusengineer_nxppnscrese_3callback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IFelicaDevice.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(7, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                oplusengineer_nxppnscrese_3callback.onValues(hwParcel2.readInt8Vector(), hwParcel2.readInt8());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public void oplusEngineerNxpPnscrFreq(oplusEngineerNxpPnscrFreqCallback oplusengineernxppnscrfreqcallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IFelicaDevice.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(8, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                oplusengineernxppnscrfreqcallback.onValues(hwParcel2.readInt8Vector(), hwParcel2.readInt8());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public void oplusEngineerNxpPnscrCurrent(oplusEngineerNxpPnscrCurrentCallback oplusengineernxppnscrcurrentcallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IFelicaDevice.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(9, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                oplusengineernxppnscrcurrentcallback.onValues(hwParcel2.readInt8Vector(), hwParcel2.readInt8());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException {
            return this.mRemote.linkToDeath(deathRecipient, j);
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(deathRecipient);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends HwBinder implements IFelicaDevice {
        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) {
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public final String interfaceDescriptor() {
            return IFelicaDevice.kInterfaceName;
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public final boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) {
            return true;
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public final void ping() {
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public final void setHALInstrumentation() {
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) {
            return true;
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(IFelicaDevice.kInterfaceName, "android.hidl.base@1.0::IBase"));
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{104, 112, -115, -31, -53, 33, 1, 82, 9, -63, 117, 82, -56, -93, 11, -20, 46, 113, -44, 38, -16, 43, -99, 59, 111, 59, 108, -28, 107, -47, 50, 107}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public final DebugInfo getDebugInfo() {
            DebugInfo debugInfo = new DebugInfo();
            debugInfo.pid = HidlSupport.getPidIfSharable();
            debugInfo.ptr = 0L;
            debugInfo.arch = 0;
            return debugInfo;
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        public IHwInterface queryLocalInterface(String str) {
            if (IFelicaDevice.kInterfaceName.equals(str)) {
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

        public void onTransact(int i, HwParcel hwParcel, final HwParcel hwParcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    hwParcel.enforceInterface(IFelicaDevice.kInterfaceName);
                    getFelicaLockStatus(new getFelicaLockStatusCallback() { // from class: vendor.oplus.hardware.felica.V1_0.IFelicaDevice.Stub.1
                        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice.getFelicaLockStatusCallback
                        public void onValues(boolean z, byte b) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeBool(z);
                            hwParcel2.writeInt8(b);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 2:
                    hwParcel.enforceInterface(IFelicaDevice.kInterfaceName);
                    byte felicaLockStatus = setFelicaLockStatus(hwParcel.readBool());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt8(felicaLockStatus);
                    hwParcel2.send();
                    return;
                case 3:
                    hwParcel.enforceInterface(IFelicaDevice.kInterfaceName);
                    getFelicaLockKey(new getFelicaLockKeyCallback() { // from class: vendor.oplus.hardware.felica.V1_0.IFelicaDevice.Stub.2
                        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice.getFelicaLockKeyCallback
                        public void onValues(ArrayList<Byte> arrayList, byte b) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt8Vector(arrayList);
                            hwParcel2.writeInt8(b);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 4:
                    hwParcel.enforceInterface(IFelicaDevice.kInterfaceName);
                    byte felicaLockKey = setFelicaLockKey(hwParcel.readInt8Vector());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt8(felicaLockKey);
                    hwParcel2.send();
                    return;
                case 5:
                    hwParcel.enforceInterface(IFelicaDevice.kInterfaceName);
                    byte eraseFelicaLockData = eraseFelicaLockData();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt8(eraseFelicaLockData);
                    hwParcel2.send();
                    return;
                case 6:
                    hwParcel.enforceInterface(IFelicaDevice.kInterfaceName);
                    oplusEngineer_NxpPnscrSpc(new oplusEngineer_NxpPnscrSpcCallback() { // from class: vendor.oplus.hardware.felica.V1_0.IFelicaDevice.Stub.3
                        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineer_NxpPnscrSpcCallback
                        public void onValues(ArrayList<Byte> arrayList, byte b) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt8Vector(arrayList);
                            hwParcel2.writeInt8(b);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 7:
                    hwParcel.enforceInterface(IFelicaDevice.kInterfaceName);
                    oplusEngineer_NxpPnscrEse_3(new oplusEngineer_NxpPnscrEse_3Callback() { // from class: vendor.oplus.hardware.felica.V1_0.IFelicaDevice.Stub.4
                        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineer_NxpPnscrEse_3Callback
                        public void onValues(ArrayList<Byte> arrayList, byte b) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt8Vector(arrayList);
                            hwParcel2.writeInt8(b);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 8:
                    hwParcel.enforceInterface(IFelicaDevice.kInterfaceName);
                    oplusEngineerNxpPnscrFreq(new oplusEngineerNxpPnscrFreqCallback() { // from class: vendor.oplus.hardware.felica.V1_0.IFelicaDevice.Stub.5
                        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineerNxpPnscrFreqCallback
                        public void onValues(ArrayList<Byte> arrayList, byte b) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt8Vector(arrayList);
                            hwParcel2.writeInt8(b);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 9:
                    hwParcel.enforceInterface(IFelicaDevice.kInterfaceName);
                    oplusEngineerNxpPnscrCurrent(new oplusEngineerNxpPnscrCurrentCallback() { // from class: vendor.oplus.hardware.felica.V1_0.IFelicaDevice.Stub.6
                        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineerNxpPnscrCurrentCallback
                        public void onValues(ArrayList<Byte> arrayList, byte b) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt8Vector(arrayList);
                            hwParcel2.writeInt8(b);
                            hwParcel2.send();
                        }
                    });
                    return;
                default:
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
}
