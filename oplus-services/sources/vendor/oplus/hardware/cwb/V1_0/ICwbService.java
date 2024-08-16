package vendor.oplus.hardware.cwb.V1_0;

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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ICwbService extends IBase {
    public static final String kInterfaceName = "vendor.oplus.hardware.cwb@1.0::ICwbService";

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface getHistogramValueCallback {
        void onValues(int i, int[][] iArr);
    }

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface getLumasValueCallback {
        void onValues(int i, float f);
    }

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface getRGBValueCallback {
        void onValues(int i, byte[] bArr);
    }

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface getRGBValuesCallback {
        void onValues(int i, ArrayList<byte[]> arrayList);
    }

    IHwBinder asBinder();

    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    boolean disable() throws RemoteException;

    boolean enable() throws RemoteException;

    int getCwbBuffer(CwbRect cwbRect, oplus_cwb_buffer oplus_cwb_bufferVar) throws RemoteException;

    boolean getCwbPostProcessStatus() throws RemoteException;

    DebugInfo getDebugInfo() throws RemoteException;

    ArrayList<byte[]> getHashChain() throws RemoteException;

    void getHistogramValue(CwbRect cwbRect, getHistogramValueCallback gethistogramvaluecallback) throws RemoteException;

    void getLumasValue(CwbRect cwbRect, getLumasValueCallback getlumasvaluecallback) throws RemoteException;

    void getRGBValue(CwbRect cwbRect, getRGBValueCallback getrgbvaluecallback) throws RemoteException;

    void getRGBValues(ArrayList<CwbRect> arrayList, getRGBValuesCallback getrgbvaluescallback) throws RemoteException;

    ArrayList<String> interfaceChain() throws RemoteException;

    String interfaceDescriptor() throws RemoteException;

    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    void notifySyspropsChanged() throws RemoteException;

    void ping() throws RemoteException;

    int registerCallback(ICwbCallback iCwbCallback) throws RemoteException;

    int setCwbPostProcessStatus(boolean z) throws RemoteException;

    int setDebug(ArrayList<Integer> arrayList) throws RemoteException;

    void setHALInstrumentation() throws RemoteException;

    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    int unregisterCallback(ICwbCallback iCwbCallback) throws RemoteException;

    static ICwbService asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        ICwbService queryLocalInterface = iHwBinder.queryLocalInterface(kInterfaceName);
        if (queryLocalInterface != null && (queryLocalInterface instanceof ICwbService)) {
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

    static ICwbService castFrom(IHwInterface iHwInterface) {
        if (iHwInterface == null) {
            return null;
        }
        return asInterface(iHwInterface.asBinder());
    }

    static ICwbService getService(String str, boolean z) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str, z));
    }

    static ICwbService getService(boolean z) throws RemoteException {
        return getService("default", z);
    }

    @Deprecated
    static ICwbService getService(String str) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str));
    }

    @Deprecated
    static ICwbService getService() throws RemoteException {
        return getService("default");
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class Proxy implements ICwbService {
        private IHwBinder mRemote;

        public Proxy(IHwBinder iHwBinder) {
            Objects.requireNonNull(iHwBinder);
            this.mRemote = iHwBinder;
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException unused) {
                return "[class or subclass of vendor.oplus.hardware.cwb@1.0::ICwbService]@Proxy";
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public void getRGBValue(CwbRect cwbRect, getRGBValueCallback getrgbvaluecallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ICwbService.kInterfaceName);
            cwbRect.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(1, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                int readInt32 = hwParcel2.readInt32();
                byte[] bArr = new byte[3];
                hwParcel2.readBuffer(3L).copyToInt8Array(0L, bArr, 3);
                getrgbvaluecallback.onValues(readInt32, bArr);
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public void getRGBValues(ArrayList<CwbRect> arrayList, getRGBValuesCallback getrgbvaluescallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ICwbService.kInterfaceName);
            CwbRect.writeVectorToParcel(hwParcel, arrayList);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(2, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                int readInt32 = hwParcel2.readInt32();
                ArrayList<byte[]> arrayList2 = new ArrayList<>();
                HwBlob readBuffer = hwParcel2.readBuffer(16L);
                int int32 = readBuffer.getInt32(8L);
                HwBlob readEmbeddedBuffer = hwParcel2.readEmbeddedBuffer(int32 * 3, readBuffer.handle(), 0L, true);
                arrayList2.clear();
                for (int i = 0; i < int32; i++) {
                    byte[] bArr = new byte[3];
                    readEmbeddedBuffer.copyToInt8Array(i * 3, bArr, 3);
                    arrayList2.add(bArr);
                }
                getrgbvaluescallback.onValues(readInt32, arrayList2);
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public void getLumasValue(CwbRect cwbRect, getLumasValueCallback getlumasvaluecallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ICwbService.kInterfaceName);
            cwbRect.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(3, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                getlumasvaluecallback.onValues(hwParcel2.readInt32(), hwParcel2.readFloat());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public void getHistogramValue(CwbRect cwbRect, getHistogramValueCallback gethistogramvaluecallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ICwbService.kInterfaceName);
            cwbRect.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(4, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                int readInt32 = hwParcel2.readInt32();
                int[][] iArr = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 256, 3);
                HwBlob readBuffer = hwParcel2.readBuffer(3072L);
                long j = 0;
                for (int i = 0; i < 256; i++) {
                    readBuffer.copyToInt32Array(j, iArr[i], 3);
                    j += 12;
                }
                gethistogramvaluecallback.onValues(readInt32, iArr);
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public int getCwbBuffer(CwbRect cwbRect, oplus_cwb_buffer oplus_cwb_bufferVar) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ICwbService.kInterfaceName);
            cwbRect.writeToParcel(hwParcel);
            oplus_cwb_bufferVar.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(5, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public boolean getCwbPostProcessStatus() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ICwbService.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(6, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readBool();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public int setCwbPostProcessStatus(boolean z) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ICwbService.kInterfaceName);
            hwParcel.writeBool(z);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(7, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public int registerCallback(ICwbCallback iCwbCallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ICwbService.kInterfaceName);
            hwParcel.writeStrongBinder(iCwbCallback == null ? null : iCwbCallback.asBinder());
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(8, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public int unregisterCallback(ICwbCallback iCwbCallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ICwbService.kInterfaceName);
            hwParcel.writeStrongBinder(iCwbCallback == null ? null : iCwbCallback.asBinder());
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(9, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public boolean enable() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ICwbService.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(10, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readBool();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public boolean disable() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ICwbService.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(11, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readBool();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public int setDebug(ArrayList<Integer> arrayList) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ICwbService.kInterfaceName);
            hwParcel.writeInt32Vector(arrayList);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(12, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException {
            return this.mRemote.linkToDeath(deathRecipient, j);
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
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

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(deathRecipient);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends HwBinder implements ICwbService {
        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) {
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public final String interfaceDescriptor() {
            return ICwbService.kInterfaceName;
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public final boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) {
            return true;
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public final void ping() {
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public final void setHALInstrumentation() {
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) {
            return true;
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(ICwbService.kInterfaceName, "android.hidl.base@1.0::IBase"));
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{-55, 1, -17, 6, 26, 56, -27, 20, 60, -98, -91, -1, -4, 98, 68, 7, 68, 77, -31, -29, 6, -118, 106, -29, -31, -16, -117, -101, 109, -58, -82, 67}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public final DebugInfo getDebugInfo() {
            DebugInfo debugInfo = new DebugInfo();
            debugInfo.pid = HidlSupport.getPidIfSharable();
            debugInfo.ptr = 0L;
            debugInfo.arch = 0;
            return debugInfo;
        }

        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        public IHwInterface queryLocalInterface(String str) {
            if (ICwbService.kInterfaceName.equals(str)) {
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
                    hwParcel.enforceInterface(ICwbService.kInterfaceName);
                    CwbRect cwbRect = new CwbRect();
                    cwbRect.readFromParcel(hwParcel);
                    getRGBValue(cwbRect, new getRGBValueCallback() { // from class: vendor.oplus.hardware.cwb.V1_0.ICwbService.Stub.1
                        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService.getRGBValueCallback
                        public void onValues(int i3, byte[] bArr) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt32(i3);
                            HwBlob hwBlob = new HwBlob(3);
                            if (bArr == null || bArr.length != 3) {
                                throw new IllegalArgumentException("Array element is not of the expected length");
                            }
                            hwBlob.putInt8Array(0L, bArr);
                            hwParcel2.writeBuffer(hwBlob);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 2:
                    hwParcel.enforceInterface(ICwbService.kInterfaceName);
                    getRGBValues(CwbRect.readVectorFromParcel(hwParcel), new getRGBValuesCallback() { // from class: vendor.oplus.hardware.cwb.V1_0.ICwbService.Stub.2
                        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService.getRGBValuesCallback
                        public void onValues(int i3, ArrayList<byte[]> arrayList) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt32(i3);
                            HwBlob hwBlob = new HwBlob(16);
                            int size = arrayList.size();
                            hwBlob.putInt32(8L, size);
                            hwBlob.putBool(12L, false);
                            HwBlob hwBlob2 = new HwBlob(size * 3);
                            for (int i4 = 0; i4 < size; i4++) {
                                long j = i4 * 3;
                                byte[] bArr = arrayList.get(i4);
                                if (bArr == null || bArr.length != 3) {
                                    throw new IllegalArgumentException("Array element is not of the expected length");
                                }
                                hwBlob2.putInt8Array(j, bArr);
                            }
                            hwBlob.putBlob(0L, hwBlob2);
                            hwParcel2.writeBuffer(hwBlob);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 3:
                    hwParcel.enforceInterface(ICwbService.kInterfaceName);
                    CwbRect cwbRect2 = new CwbRect();
                    cwbRect2.readFromParcel(hwParcel);
                    getLumasValue(cwbRect2, new getLumasValueCallback() { // from class: vendor.oplus.hardware.cwb.V1_0.ICwbService.Stub.3
                        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService.getLumasValueCallback
                        public void onValues(int i3, float f) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt32(i3);
                            hwParcel2.writeFloat(f);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 4:
                    hwParcel.enforceInterface(ICwbService.kInterfaceName);
                    CwbRect cwbRect3 = new CwbRect();
                    cwbRect3.readFromParcel(hwParcel);
                    getHistogramValue(cwbRect3, new getHistogramValueCallback() { // from class: vendor.oplus.hardware.cwb.V1_0.ICwbService.Stub.4
                        @Override // vendor.oplus.hardware.cwb.V1_0.ICwbService.getHistogramValueCallback
                        public void onValues(int i3, int[][] iArr) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt32(i3);
                            HwBlob hwBlob = new HwBlob(3072);
                            long j = 0;
                            for (int i4 = 0; i4 < 256; i4++) {
                                int[] iArr2 = iArr[i4];
                                if (iArr2 == null || iArr2.length != 3) {
                                    throw new IllegalArgumentException("Array element is not of the expected length");
                                }
                                hwBlob.putInt32Array(j, iArr2);
                                j += 12;
                            }
                            hwParcel2.writeBuffer(hwBlob);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 5:
                    hwParcel.enforceInterface(ICwbService.kInterfaceName);
                    CwbRect cwbRect4 = new CwbRect();
                    cwbRect4.readFromParcel(hwParcel);
                    oplus_cwb_buffer oplus_cwb_bufferVar = new oplus_cwb_buffer();
                    oplus_cwb_bufferVar.readFromParcel(hwParcel);
                    int cwbBuffer = getCwbBuffer(cwbRect4, oplus_cwb_bufferVar);
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(cwbBuffer);
                    hwParcel2.send();
                    return;
                case 6:
                    hwParcel.enforceInterface(ICwbService.kInterfaceName);
                    boolean cwbPostProcessStatus = getCwbPostProcessStatus();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeBool(cwbPostProcessStatus);
                    hwParcel2.send();
                    return;
                case 7:
                    hwParcel.enforceInterface(ICwbService.kInterfaceName);
                    int cwbPostProcessStatus2 = setCwbPostProcessStatus(hwParcel.readBool());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(cwbPostProcessStatus2);
                    hwParcel2.send();
                    return;
                case 8:
                    hwParcel.enforceInterface(ICwbService.kInterfaceName);
                    int registerCallback = registerCallback(ICwbCallback.asInterface(hwParcel.readStrongBinder()));
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(registerCallback);
                    hwParcel2.send();
                    return;
                case 9:
                    hwParcel.enforceInterface(ICwbService.kInterfaceName);
                    int unregisterCallback = unregisterCallback(ICwbCallback.asInterface(hwParcel.readStrongBinder()));
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(unregisterCallback);
                    hwParcel2.send();
                    return;
                case 10:
                    hwParcel.enforceInterface(ICwbService.kInterfaceName);
                    boolean enable = enable();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeBool(enable);
                    hwParcel2.send();
                    return;
                case 11:
                    hwParcel.enforceInterface(ICwbService.kInterfaceName);
                    boolean disable = disable();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeBool(disable);
                    hwParcel2.send();
                    return;
                case 12:
                    hwParcel.enforceInterface(ICwbService.kInterfaceName);
                    int debug = setDebug(hwParcel.readInt32Vector());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(debug);
                    hwParcel2.send();
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
