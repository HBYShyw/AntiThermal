package android.hardware.biometrics.fingerprint.V2_2;

import android.hardware.biometrics.face.AcquiredInfo;
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
public interface IBiometricsFingerprintClientCallback extends android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback {
    public static final String kInterfaceName = "android.hardware.biometrics.fingerprint@2.2::IBiometricsFingerprintClientCallback";

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    IHwBinder asBinder();

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    DebugInfo getDebugInfo() throws RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    ArrayList<byte[]> getHashChain() throws RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    ArrayList<String> interfaceChain() throws RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    String interfaceDescriptor() throws RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws RemoteException;

    void onAcquired_2_2(long j, int i, int i2) throws RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    void ping() throws RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static IBiometricsFingerprintClientCallback asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        IHwInterface queryLocalInterface = iHwBinder.queryLocalInterface(kInterfaceName);
        if (queryLocalInterface != null && (queryLocalInterface instanceof IBiometricsFingerprintClientCallback)) {
            return (IBiometricsFingerprintClientCallback) queryLocalInterface;
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

    static IBiometricsFingerprintClientCallback castFrom(IHwInterface iHwInterface) {
        if (iHwInterface == null) {
            return null;
        }
        return asInterface(iHwInterface.asBinder());
    }

    static IBiometricsFingerprintClientCallback getService(String str, boolean z) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str, z));
    }

    static IBiometricsFingerprintClientCallback getService(boolean z) throws RemoteException {
        return getService("default", z);
    }

    @Deprecated
    static IBiometricsFingerprintClientCallback getService(String str) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str));
    }

    @Deprecated
    static IBiometricsFingerprintClientCallback getService() throws RemoteException {
        return getService("default");
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Proxy implements IBiometricsFingerprintClientCallback {
        private IHwBinder mRemote;

        public Proxy(IHwBinder iHwBinder) {
            Objects.requireNonNull(iHwBinder);
            this.mRemote = iHwBinder;
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException unused) {
                return "[class or subclass of android.hardware.biometrics.fingerprint@2.2::IBiometricsFingerprintClientCallback]@Proxy";
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
        public void onEnrollResult(long j, int i, int i2, int i3) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32(i2);
            hwParcel.writeInt32(i3);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(1, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
        public void onAcquired(long j, int i, int i2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32(i2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(2, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
        public void onAuthenticated(long j, int i, int i2, ArrayList<Byte> arrayList) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32(i2);
            hwParcel.writeInt8Vector(arrayList);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(3, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
        public void onError(long j, int i, int i2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32(i2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(4, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
        public void onRemoved(long j, int i, int i2, int i3) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32(i2);
            hwParcel.writeInt32(i3);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(5, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
        public void onEnumerate(long j, int i, int i2, int i3) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32(i2);
            hwParcel.writeInt32(i3);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(6, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback
        public void onAcquired_2_2(long j, int i, int i2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IBiometricsFingerprintClientCallback.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32(i2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(7, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException {
            return this.mRemote.linkToDeath(deathRecipient, j);
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(deathRecipient);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends HwBinder implements IBiometricsFingerprintClientCallback {
        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public IHwBinder asBinder() {
            return this;
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) {
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final String interfaceDescriptor() {
            return IBiometricsFingerprintClientCallback.kInterfaceName;
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) {
            return true;
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) {
            return true;
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(IBiometricsFingerprintClientCallback.kInterfaceName, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName, IBase.kInterfaceName));
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{-126, -54, -39, -97, 95, -21, 46, -87, -68, -44, 87, -112, 85, -19, -12, -81, -113, -21, -97, -58, 2, -90, -28, -126, 125, -35, 114, 125, 37, 77, 73, -111}, new byte[]{-86, -69, 92, 60, 88, 85, -110, -41, 30, -27, 123, 119, 41, -116, AcquiredInfo.FACE_OBSCURED, -103, 61, 119, -111, 77, -34, -86, 100, -78, -59, AcquiredInfo.TOO_SIMILAR, -102, 96, 43, 2, -22, 71}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, AcquiredInfo.ROLL_TOO_EXTREME, -109, 36, -72, 59, AcquiredInfo.FIRST_FRAME_RECEIVED, -54, 76}));
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final DebugInfo getDebugInfo() {
            DebugInfo debugInfo = new DebugInfo();
            debugInfo.pid = HidlSupport.getPidIfSharable();
            debugInfo.ptr = 0L;
            debugInfo.arch = 0;
            return debugInfo;
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        public IHwInterface queryLocalInterface(String str) {
            if (IBiometricsFingerprintClientCallback.kInterfaceName.equals(str)) {
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
            switch (i) {
                case 1:
                    hwParcel.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
                    onEnrollResult(hwParcel.readInt64(), hwParcel.readInt32(), hwParcel.readInt32(), hwParcel.readInt32());
                    return;
                case 2:
                    hwParcel.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
                    onAcquired(hwParcel.readInt64(), hwParcel.readInt32(), hwParcel.readInt32());
                    return;
                case 3:
                    hwParcel.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
                    onAuthenticated(hwParcel.readInt64(), hwParcel.readInt32(), hwParcel.readInt32(), hwParcel.readInt8Vector());
                    return;
                case 4:
                    hwParcel.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
                    onError(hwParcel.readInt64(), hwParcel.readInt32(), hwParcel.readInt32());
                    return;
                case 5:
                    hwParcel.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
                    onRemoved(hwParcel.readInt64(), hwParcel.readInt32(), hwParcel.readInt32(), hwParcel.readInt32());
                    return;
                case 6:
                    hwParcel.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
                    onEnumerate(hwParcel.readInt64(), hwParcel.readInt32(), hwParcel.readInt32(), hwParcel.readInt32());
                    return;
                case 7:
                    hwParcel.enforceInterface(IBiometricsFingerprintClientCallback.kInterfaceName);
                    onAcquired_2_2(hwParcel.readInt64(), hwParcel.readInt32(), hwParcel.readInt32());
                    return;
                default:
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
}
