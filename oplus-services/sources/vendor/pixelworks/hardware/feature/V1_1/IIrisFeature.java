package vendor.pixelworks.hardware.feature.V1_1;

import android.hidl.base.V1_0.DebugInfo;
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
import vendor.pixelworks.hardware.feature.V1_0.IIrisFeature;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IIrisFeature extends vendor.pixelworks.hardware.feature.V1_0.IIrisFeature {
    public static final String kInterfaceName = "vendor.pixelworks.hardware.feature@1.1::IIrisFeature";

    @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
    IHwBinder asBinder();

    @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
    DebugInfo getDebugInfo() throws RemoteException;

    @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
    ArrayList<byte[]> getHashChain() throws RemoteException;

    @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
    ArrayList<String> interfaceChain() throws RemoteException;

    @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
    String interfaceDescriptor() throws RemoteException;

    @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
    void notifySyspropsChanged() throws RemoteException;

    @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
    void ping() throws RemoteException;

    @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
    void setHALInstrumentation() throws RemoteException;

    @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static IIrisFeature asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        IIrisFeature queryLocalInterface = iHwBinder.queryLocalInterface(kInterfaceName);
        if (queryLocalInterface != null && (queryLocalInterface instanceof IIrisFeature)) {
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

    static IIrisFeature castFrom(IHwInterface iHwInterface) {
        if (iHwInterface == null) {
            return null;
        }
        return asInterface(iHwInterface.asBinder());
    }

    static IIrisFeature getService(String str, boolean z) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str, z));
    }

    static IIrisFeature getService(boolean z) throws RemoteException {
        return getService("default", z);
    }

    @Deprecated
    static IIrisFeature getService(String str) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str));
    }

    @Deprecated
    static IIrisFeature getService() throws RemoteException {
        return getService("default");
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class Proxy implements IIrisFeature {
        private IHwBinder mRemote;

        public Proxy(IHwBinder iHwBinder) {
            Objects.requireNonNull(iHwBinder);
            this.mRemote = iHwBinder;
        }

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException unused) {
                return "[class or subclass of vendor.pixelworks.hardware.feature@1.1::IIrisFeature]@Proxy";
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public void getFeature(IIrisFeature.getFeatureCallback getfeaturecallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.feature.V1_0.IIrisFeature.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(1, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                getfeaturecallback.onValues(hwParcel2.readInt32(), hwParcel2.readInt32());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
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

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
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

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
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

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
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

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
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

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException {
            return this.mRemote.linkToDeath(deathRecipient, j);
        }

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
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

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
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

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
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

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(deathRecipient);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends HwBinder implements IIrisFeature {
        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) {
        }

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public final String interfaceDescriptor() {
            return IIrisFeature.kInterfaceName;
        }

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public final boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) {
            return true;
        }

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public final void ping() {
        }

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public final void setHALInstrumentation() {
        }

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) {
            return true;
        }

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(IIrisFeature.kInterfaceName, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature.kInterfaceName, "android.hidl.base@1.0::IBase"));
        }

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{34, 52, -73, -65, 42, -8, 8, -75, -108, -95, 109, 82, 97, 38, 2, -47, -75, 46, 77, -7, 36, -3, 17, 79, 114, -118, -27, 4, Byte.MIN_VALUE, 57, -41, 96}, new byte[]{125, -17, 122, -16, 20, -124, 28, 56, -48, 89, 1, -124, 3, -22, 10, -93, -65, 14, -31, -65, -35, 41, -23, -5, -24, 5, 122, -11, 62, -100, 70, -69}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public final DebugInfo getDebugInfo() {
            DebugInfo debugInfo = new DebugInfo();
            debugInfo.pid = HidlSupport.getPidIfSharable();
            debugInfo.ptr = 0L;
            debugInfo.arch = 0;
            return debugInfo;
        }

        @Override // vendor.pixelworks.hardware.feature.V1_1.IIrisFeature, vendor.pixelworks.hardware.feature.V1_0.IIrisFeature
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        public IHwInterface queryLocalInterface(String str) {
            if (IIrisFeature.kInterfaceName.equals(str)) {
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
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.feature.V1_0.IIrisFeature.kInterfaceName);
                    getFeature(new IIrisFeature.getFeatureCallback() { // from class: vendor.pixelworks.hardware.feature.V1_1.IIrisFeature.Stub.1
                        @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature.getFeatureCallback
                        public void onValues(int i3, int i4) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt32(i3);
                            hwParcel2.writeInt32(i4);
                            hwParcel2.send();
                        }
                    });
                    return;
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
