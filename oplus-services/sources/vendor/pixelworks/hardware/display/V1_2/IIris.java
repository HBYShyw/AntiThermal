package vendor.pixelworks.hardware.display.V1_2;

import android.hidl.base.V1_0.DebugInfo;
import android.os.HidlMemory;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.NativeHandle;
import android.os.RemoteException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import vendor.pixelworks.hardware.display.V1_0.IIris;
import vendor.pixelworks.hardware.display.V1_1.BufferInfo;
import vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo;
import vendor.pixelworks.hardware.display.V1_1.HwcRect;
import vendor.pixelworks.hardware.display.V1_1.IIris;
import vendor.pixelworks.hardware.display.V1_1.ISoftIrisClient;
import vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig;
import vendor.pixelworks.hardware.display.V1_1.LayerStack;
import vendor.pixelworks.hardware.display.V1_1.LutData;
import vendor.pixelworks.hardware.display.V1_2.IIris;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IIris extends vendor.pixelworks.hardware.display.V1_1.IIris {
    public static final String kInterfaceName = "vendor.pixelworks.hardware.display@1.2::IIris";

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface getCurrentConfig_1_2Callback {
        void onValues(int i, IrisFixedConfig_1_2 irisFixedConfig_1_2);
    }

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
    IHwBinder asBinder();

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    void getCurrentConfig_1_2(long j, getCurrentConfig_1_2Callback getcurrentconfig_1_2callback) throws RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
    DebugInfo getDebugInfo() throws RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
    ArrayList<byte[]> getHashChain() throws RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
    ArrayList<String> interfaceChain() throws RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
    String interfaceDescriptor() throws RemoteException;

    int irisConfigureBuffer(int i, long j, NativeHandle nativeHandle, int i2) throws RemoteException;

    int irisConfigureMemory(int i, long j, HidlMemory hidlMemory) throws RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
    void notifySyspropsChanged() throws RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
    void ping() throws RemoteException;

    void setActiveConfig_1_2(long j, DisplayConfigVariableInfo_1_2 displayConfigVariableInfo_1_2) throws RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
    void setHALInstrumentation() throws RemoteException;

    void setLayerBuffer_1_2(long j, long j2, NativeHandle nativeHandle, int i, BufferInfo_1_2 bufferInfo_1_2) throws RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static IIris asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        IIris queryLocalInterface = iHwBinder.queryLocalInterface(kInterfaceName);
        if (queryLocalInterface != null && (queryLocalInterface instanceof IIris)) {
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

    static IIris castFrom(IHwInterface iHwInterface) {
        if (iHwInterface == null) {
            return null;
        }
        return asInterface(iHwInterface.asBinder());
    }

    static IIris getService(String str, boolean z) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str, z));
    }

    static IIris getService(boolean z) throws RemoteException {
        return getService("default", z);
    }

    @Deprecated
    static IIris getService(String str) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str));
    }

    @Deprecated
    static IIris getService() throws RemoteException {
        return getService("default");
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class Proxy implements IIris {
        private IHwBinder mRemote;

        public Proxy(IHwBinder iHwBinder) {
            Objects.requireNonNull(iHwBinder);
            this.mRemote = iHwBinder;
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException unused) {
                return "[class or subclass of vendor.pixelworks.hardware.display@1.2::IIris]@Proxy";
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris
        public int irisConfigureSet(int i, ArrayList<Integer> arrayList) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32Vector(arrayList);
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

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris
        public void irisConfigureGet(int i, ArrayList<Integer> arrayList, IIris.irisConfigureGetCallback irisconfiguregetcallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32Vector(arrayList);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(2, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                irisconfiguregetcallback.onValues(hwParcel2.readInt32(), hwParcel2.readInt32Vector());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris
        public void registerCallback(vendor.pixelworks.hardware.display.V1_0.IIrisCallback iIrisCallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
            hwParcel.writeStrongBinder(iIrisCallback == null ? null : iIrisCallback.asBinder());
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(3, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris
        public void registerCallback2(long j, vendor.pixelworks.hardware.display.V1_0.IIrisCallback iIrisCallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeStrongBinder(iIrisCallback == null ? null : iIrisCallback.asBinder());
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(4, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris
        public void panelReadWrite(boolean z, byte b, byte b2, boolean z2, ArrayList<Byte> arrayList, byte b3, IIris.panelReadWriteCallback panelreadwritecallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
            hwParcel.writeBool(z);
            hwParcel.writeInt8(b);
            hwParcel.writeInt8(b2);
            hwParcel.writeBool(z2);
            hwParcel.writeInt8Vector(arrayList);
            hwParcel.writeInt8(b3);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(5, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                panelreadwritecallback.onValues(hwParcel2.readInt32(), hwParcel2.readInt8Vector());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris
        public void irisConfigureBatch(int i, String str, IIris.irisConfigureBatchCallback irisconfigurebatchcallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
            hwParcel.writeInt32(i);
            hwParcel.writeString(str);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(6, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                irisconfigurebatchcallback.onValues(hwParcel2.readInt32(), hwParcel2.readString());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void initialize(DisplayConfigVariableInfo displayConfigVariableInfo) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            displayConfigVariableInfo.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(7, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void registerCallback_1_1(long j, vendor.pixelworks.hardware.display.V1_1.IIrisCallback iIrisCallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeStrongBinder(iIrisCallback == null ? null : iIrisCallback.asBinder());
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(8, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void registerSoftIrisClient(ISoftIrisClient iSoftIrisClient) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeStrongBinder(iSoftIrisClient == null ? null : iSoftIrisClient.asBinder());
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(9, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void createLayer(long j, long j2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt64(j2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(10, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void destroyLayer(long j, long j2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt64(j2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(11, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void buildLayerStack(long j, LayerStack layerStack) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            layerStack.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(12, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setActiveConfig(long j, DisplayConfigVariableInfo displayConfigVariableInfo) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            displayConfigVariableInfo.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(13, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void getLayerToneMappingLut(long j, int i, IIris.getLayerToneMappingLutCallback getlayertonemappinglutcallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(14, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                int readInt32 = hwParcel2.readInt32();
                LutData lutData = new LutData();
                lutData.readFromParcel(hwParcel2);
                getlayertonemappinglutcallback.onValues(readInt32, lutData);
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int commitLayerStack(long j, int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(15, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void updateDisplayBrightness(long j, int i, ArrayList<Integer> arrayList, IIris.updateDisplayBrightnessCallback updatedisplaybrightnesscallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32Vector(arrayList);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(16, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                updatedisplaybrightnesscallback.onValues(hwParcel2.readInt32(), hwParcel2.readInt32Vector());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerProperty(long j, int i, long j2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            hwParcel.writeInt64(j2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(17, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int setPowerMode(long j, int i, boolean z, boolean z2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            hwParcel.writeBool(z);
            hwParcel.writeBool(z2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(18, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int handleDisplayEvent(long j, int i, int i2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32(i2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(19, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public String getDumpString(long j) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(20, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readString();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setClientTarget(long j, int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(21, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerBuffer(long j, long j2, NativeHandle nativeHandle, int i, BufferInfo bufferInfo) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt64(j2);
            hwParcel.writeNativeHandle(nativeHandle);
            hwParcel.writeInt32(i);
            bufferInfo.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(22, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerCompositionType(long j, long j2, int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt64(j2);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(23, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerDisplayFrame(long j, long j2, HwcRect hwcRect) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt64(j2);
            hwcRect.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(24, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerSourceCrop(long j, long j2, HwcRect hwcRect) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt64(j2);
            hwcRect.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(25, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerTransform(long j, long j2, int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt64(j2);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(26, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerZOrder(long j, long j2, int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt64(j2);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(27, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void changeLayerType(long j, long j2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt64(j2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(28, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void isHDR10Plus(boolean z) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeBool(z);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(29, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int setColorModeWithRenderIntent(long j, int i, int i2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt32(i);
            hwParcel.writeInt32(i2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(30, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerSetEmpty(long j, boolean z) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeBool(z);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(31, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setDisplayConnected(long j, boolean z) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeBool(z);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(32, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int setOsdAutoRefresh(int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(33, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int configureIrisMaxcll(int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(34, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void getCurrentConfig(IIris.getCurrentConfigCallback getcurrentconfigcallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(35, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                int readInt32 = hwParcel2.readInt32();
                IrisFixedConfig irisFixedConfig = new IrisFixedConfig();
                irisFixedConfig.readFromParcel(hwParcel2);
                getcurrentconfigcallback.onValues(readInt32, irisFixedConfig);
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void reportDualChannelStatus(int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(36, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int getOsdStatus(int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(37, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int presentDisplay(long j) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(38, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int present(long j) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(39, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void enableSecondaryDisplay(boolean z) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            hwParcel.writeBool(z);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(40, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris
        public int irisConfigureBuffer(int i, long j, NativeHandle nativeHandle, int i2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IIris.kInterfaceName);
            hwParcel.writeInt32(i);
            hwParcel.writeInt64(j);
            hwParcel.writeNativeHandle(nativeHandle);
            hwParcel.writeInt32(i2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(41, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris
        public int irisConfigureMemory(int i, long j, HidlMemory hidlMemory) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IIris.kInterfaceName);
            hwParcel.writeInt32(i);
            hwParcel.writeInt64(j);
            hwParcel.writeHidlMemory(hidlMemory);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(42, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris
        public void setActiveConfig_1_2(long j, DisplayConfigVariableInfo_1_2 displayConfigVariableInfo_1_2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            displayConfigVariableInfo_1_2.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(43, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris
        public void setLayerBuffer_1_2(long j, long j2, NativeHandle nativeHandle, int i, BufferInfo_1_2 bufferInfo_1_2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            hwParcel.writeInt64(j2);
            hwParcel.writeNativeHandle(nativeHandle);
            hwParcel.writeInt32(i);
            bufferInfo_1_2.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(44, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris
        public void getCurrentConfig_1_2(long j, getCurrentConfig_1_2Callback getcurrentconfig_1_2callback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IIris.kInterfaceName);
            hwParcel.writeInt64(j);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(45, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                int readInt32 = hwParcel2.readInt32();
                IrisFixedConfig_1_2 irisFixedConfig_1_2 = new IrisFixedConfig_1_2();
                irisFixedConfig_1_2.readFromParcel(hwParcel2);
                getcurrentconfig_1_2callback.onValues(readInt32, irisFixedConfig_1_2);
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
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

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
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

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
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

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
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

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
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

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
        public boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException {
            return this.mRemote.linkToDeath(deathRecipient, j);
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
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

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
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

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
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

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
        public boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(deathRecipient);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends HwBinder implements IIris {
        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
        public IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
        public void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) {
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
        public final String interfaceDescriptor() {
            return IIris.kInterfaceName;
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
        public final boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) {
            return true;
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
        public final void ping() {
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
        public final void setHALInstrumentation() {
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) {
            return true;
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(IIris.kInterfaceName, vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName, vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName, "android.hidl.base@1.0::IBase"));
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{-8, 59, 69, 33, -122, -112, -99, -32, -118, -67, 122, 34, 76, 48, 114, -21, -118, 85, -43, -67, 73, 5, -71, 45, 74, 119, 58, -46, -46, 36, -1, 35}, new byte[]{-125, 68, 46, -104, 90, -121, -2, -62, -126, 39, -110, 45, 69, -5, 35, -22, -39, -26, -98, 68, 74, -113, 36, 72, -86, -8, 70, 2, 92, 48, -86, 7}, new byte[]{126, 28, 77, -7, 102, 35, -100, 73, 26, 9, -56, 86, -88, 29, -123, -88, 111, -68, 13, 43, 1, 11, -19, 105, -111, 85, 122, 16, -99, -55, 104, -123}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
        public final DebugInfo getDebugInfo() {
            DebugInfo debugInfo = new DebugInfo();
            debugInfo.pid = HidlSupport.getPidIfSharable();
            debugInfo.ptr = 0L;
            debugInfo.arch = 0;
            return debugInfo;
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        public IHwInterface queryLocalInterface(String str) {
            if (IIris.kInterfaceName.equals(str)) {
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

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ HidlMemory lambda$onTransact$0(HwParcel hwParcel) {
            try {
                return hwParcel.readHidlMemory().dup();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void onTransact(int i, HwParcel hwParcel, final HwParcel hwParcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
                    int irisConfigureSet = irisConfigureSet(hwParcel.readInt32(), hwParcel.readInt32Vector());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(irisConfigureSet);
                    hwParcel2.send();
                    return;
                case 2:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
                    irisConfigureGet(hwParcel.readInt32(), hwParcel.readInt32Vector(), new IIris.irisConfigureGetCallback() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris.Stub.1
                        @Override // vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureGetCallback
                        public void onValues(int i3, ArrayList<Integer> arrayList) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt32(i3);
                            hwParcel2.writeInt32Vector(arrayList);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 3:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
                    registerCallback(vendor.pixelworks.hardware.display.V1_0.IIrisCallback.asInterface(hwParcel.readStrongBinder()));
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 4:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
                    registerCallback2(hwParcel.readInt64(), vendor.pixelworks.hardware.display.V1_0.IIrisCallback.asInterface(hwParcel.readStrongBinder()));
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 5:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
                    panelReadWrite(hwParcel.readBool(), hwParcel.readInt8(), hwParcel.readInt8(), hwParcel.readBool(), hwParcel.readInt8Vector(), hwParcel.readInt8(), new IIris.panelReadWriteCallback() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris.Stub.2
                        @Override // vendor.pixelworks.hardware.display.V1_0.IIris.panelReadWriteCallback
                        public void onValues(int i3, ArrayList<Byte> arrayList) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt32(i3);
                            hwParcel2.writeInt8Vector(arrayList);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 6:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
                    irisConfigureBatch(hwParcel.readInt32(), hwParcel.readString(), new IIris.irisConfigureBatchCallback() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris.Stub.3
                        @Override // vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureBatchCallback
                        public void onValues(int i3, String str) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt32(i3);
                            hwParcel2.writeString(str);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 7:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    DisplayConfigVariableInfo displayConfigVariableInfo = new DisplayConfigVariableInfo();
                    displayConfigVariableInfo.readFromParcel(hwParcel);
                    initialize(displayConfigVariableInfo);
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 8:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    registerCallback_1_1(hwParcel.readInt64(), vendor.pixelworks.hardware.display.V1_1.IIrisCallback.asInterface(hwParcel.readStrongBinder()));
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 9:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    registerSoftIrisClient(ISoftIrisClient.asInterface(hwParcel.readStrongBinder()));
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 10:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    createLayer(hwParcel.readInt64(), hwParcel.readInt64());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 11:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    destroyLayer(hwParcel.readInt64(), hwParcel.readInt64());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 12:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long readInt64 = hwParcel.readInt64();
                    LayerStack layerStack = new LayerStack();
                    layerStack.readFromParcel(hwParcel);
                    buildLayerStack(readInt64, layerStack);
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 13:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long readInt642 = hwParcel.readInt64();
                    DisplayConfigVariableInfo displayConfigVariableInfo2 = new DisplayConfigVariableInfo();
                    displayConfigVariableInfo2.readFromParcel(hwParcel);
                    setActiveConfig(readInt642, displayConfigVariableInfo2);
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 14:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    getLayerToneMappingLut(hwParcel.readInt64(), hwParcel.readInt32(), new IIris.getLayerToneMappingLutCallback() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris.Stub.4
                        @Override // vendor.pixelworks.hardware.display.V1_1.IIris.getLayerToneMappingLutCallback
                        public void onValues(int i3, LutData lutData) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt32(i3);
                            lutData.writeToParcel(hwParcel2);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 15:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    int commitLayerStack = commitLayerStack(hwParcel.readInt64(), hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(commitLayerStack);
                    hwParcel2.send();
                    return;
                case 16:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    updateDisplayBrightness(hwParcel.readInt64(), hwParcel.readInt32(), hwParcel.readInt32Vector(), new IIris.updateDisplayBrightnessCallback() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris.Stub.5
                        @Override // vendor.pixelworks.hardware.display.V1_1.IIris.updateDisplayBrightnessCallback
                        public void onValues(int i3, ArrayList<Integer> arrayList) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt32(i3);
                            hwParcel2.writeInt32Vector(arrayList);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 17:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    setLayerProperty(hwParcel.readInt64(), hwParcel.readInt32(), hwParcel.readInt64());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 18:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    int powerMode = setPowerMode(hwParcel.readInt64(), hwParcel.readInt32(), hwParcel.readBool(), hwParcel.readBool());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(powerMode);
                    hwParcel2.send();
                    return;
                case 19:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    int handleDisplayEvent = handleDisplayEvent(hwParcel.readInt64(), hwParcel.readInt32(), hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(handleDisplayEvent);
                    hwParcel2.send();
                    return;
                case 20:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    String dumpString = getDumpString(hwParcel.readInt64());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeString(dumpString);
                    hwParcel2.send();
                    return;
                case 21:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    setClientTarget(hwParcel.readInt64(), hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 22:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long readInt643 = hwParcel.readInt64();
                    long readInt644 = hwParcel.readInt64();
                    NativeHandle readNativeHandle = hwParcel.readNativeHandle();
                    int readInt32 = hwParcel.readInt32();
                    BufferInfo bufferInfo = new BufferInfo();
                    bufferInfo.readFromParcel(hwParcel);
                    setLayerBuffer(readInt643, readInt644, readNativeHandle, readInt32, bufferInfo);
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 23:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    setLayerCompositionType(hwParcel.readInt64(), hwParcel.readInt64(), hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 24:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long readInt645 = hwParcel.readInt64();
                    long readInt646 = hwParcel.readInt64();
                    HwcRect hwcRect = new HwcRect();
                    hwcRect.readFromParcel(hwParcel);
                    setLayerDisplayFrame(readInt645, readInt646, hwcRect);
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 25:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long readInt647 = hwParcel.readInt64();
                    long readInt648 = hwParcel.readInt64();
                    HwcRect hwcRect2 = new HwcRect();
                    hwcRect2.readFromParcel(hwParcel);
                    setLayerSourceCrop(readInt647, readInt648, hwcRect2);
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 26:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    setLayerTransform(hwParcel.readInt64(), hwParcel.readInt64(), hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 27:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    setLayerZOrder(hwParcel.readInt64(), hwParcel.readInt64(), hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 28:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    changeLayerType(hwParcel.readInt64(), hwParcel.readInt64());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 29:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    isHDR10Plus(hwParcel.readBool());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 30:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    int colorModeWithRenderIntent = setColorModeWithRenderIntent(hwParcel.readInt64(), hwParcel.readInt32(), hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(colorModeWithRenderIntent);
                    hwParcel2.send();
                    return;
                case 31:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    setLayerSetEmpty(hwParcel.readInt64(), hwParcel.readBool());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 32:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    setDisplayConnected(hwParcel.readInt64(), hwParcel.readBool());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 33:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    int osdAutoRefresh = setOsdAutoRefresh(hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(osdAutoRefresh);
                    hwParcel2.send();
                    return;
                case 34:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    int configureIrisMaxcll = configureIrisMaxcll(hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(configureIrisMaxcll);
                    hwParcel2.send();
                    return;
                case 35:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    getCurrentConfig(new IIris.getCurrentConfigCallback() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris.Stub.6
                        @Override // vendor.pixelworks.hardware.display.V1_1.IIris.getCurrentConfigCallback
                        public void onValues(int i3, IrisFixedConfig irisFixedConfig) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt32(i3);
                            irisFixedConfig.writeToParcel(hwParcel2);
                            hwParcel2.send();
                        }
                    });
                    return;
                case 36:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    reportDualChannelStatus(hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 37:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    int osdStatus = getOsdStatus(hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(osdStatus);
                    hwParcel2.send();
                    return;
                case 38:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    int presentDisplay = presentDisplay(hwParcel.readInt64());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(presentDisplay);
                    hwParcel2.send();
                    return;
                case 39:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    int present = present(hwParcel.readInt64());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(present);
                    hwParcel2.send();
                    return;
                case 40:
                    hwParcel.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    enableSecondaryDisplay(hwParcel.readBool());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 41:
                    hwParcel.enforceInterface(IIris.kInterfaceName);
                    int irisConfigureBuffer = irisConfigureBuffer(hwParcel.readInt32(), hwParcel.readInt64(), hwParcel.readNativeHandle(), hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(irisConfigureBuffer);
                    hwParcel2.send();
                    return;
                case 42:
                    hwParcel.enforceInterface(IIris.kInterfaceName);
                    int irisConfigureMemory = irisConfigureMemory(hwParcel.readInt32(), hwParcel.readInt64(), (HidlMemory) new Function() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris$Stub$$ExternalSyntheticLambda0
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            HidlMemory lambda$onTransact$0;
                            lambda$onTransact$0 = IIris.Stub.lambda$onTransact$0((HwParcel) obj);
                            return lambda$onTransact$0;
                        }
                    }.apply(hwParcel));
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(irisConfigureMemory);
                    hwParcel2.send();
                    return;
                case 43:
                    hwParcel.enforceInterface(IIris.kInterfaceName);
                    long readInt649 = hwParcel.readInt64();
                    DisplayConfigVariableInfo_1_2 displayConfigVariableInfo_1_2 = new DisplayConfigVariableInfo_1_2();
                    displayConfigVariableInfo_1_2.readFromParcel(hwParcel);
                    setActiveConfig_1_2(readInt649, displayConfigVariableInfo_1_2);
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 44:
                    hwParcel.enforceInterface(IIris.kInterfaceName);
                    long readInt6410 = hwParcel.readInt64();
                    long readInt6411 = hwParcel.readInt64();
                    NativeHandle readNativeHandle2 = hwParcel.readNativeHandle();
                    int readInt322 = hwParcel.readInt32();
                    BufferInfo_1_2 bufferInfo_1_2 = new BufferInfo_1_2();
                    bufferInfo_1_2.readFromParcel(hwParcel);
                    setLayerBuffer_1_2(readInt6410, readInt6411, readNativeHandle2, readInt322, bufferInfo_1_2);
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 45:
                    hwParcel.enforceInterface(IIris.kInterfaceName);
                    getCurrentConfig_1_2(hwParcel.readInt64(), new getCurrentConfig_1_2Callback() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris.Stub.7
                        @Override // vendor.pixelworks.hardware.display.V1_2.IIris.getCurrentConfig_1_2Callback
                        public void onValues(int i3, IrisFixedConfig_1_2 irisFixedConfig_1_2) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt32(i3);
                            irisFixedConfig_1_2.writeToParcel(hwParcel2);
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
