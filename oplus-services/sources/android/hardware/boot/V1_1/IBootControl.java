package android.hardware.boot.V1_1;

import android.hardware.biometrics.face.AcquiredInfo;
import android.hardware.boot.V1_0.CommandResult;
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
public interface IBootControl extends android.hardware.boot.V1_0.IBootControl {
    public static final String kInterfaceName = "android.hardware.boot@1.1::IBootControl";

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    IHwBinder asBinder();

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    DebugInfo getDebugInfo() throws RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    ArrayList<byte[]> getHashChain() throws RemoteException;

    int getSnapshotMergeStatus() throws RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    ArrayList<String> interfaceChain() throws RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    String interfaceDescriptor() throws RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    void ping() throws RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws RemoteException;

    boolean setSnapshotMergeStatus(int i) throws RemoteException;

    @Override // android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static IBootControl asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        IHwInterface queryLocalInterface = iHwBinder.queryLocalInterface(kInterfaceName);
        if (queryLocalInterface != null && (queryLocalInterface instanceof IBootControl)) {
            return (IBootControl) queryLocalInterface;
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

    static IBootControl castFrom(IHwInterface iHwInterface) {
        if (iHwInterface == null) {
            return null;
        }
        return asInterface(iHwInterface.asBinder());
    }

    static IBootControl getService(String str, boolean z) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str, z));
    }

    static IBootControl getService(boolean z) throws RemoteException {
        return getService("default", z);
    }

    @Deprecated
    static IBootControl getService(String str) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str));
    }

    @Deprecated
    static IBootControl getService() throws RemoteException {
        return getService("default");
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Proxy implements IBootControl {
        private IHwBinder mRemote;

        public Proxy(IHwBinder iHwBinder) {
            Objects.requireNonNull(iHwBinder);
            this.mRemote = iHwBinder;
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException unused) {
                return "[class or subclass of android.hardware.boot@1.1::IBootControl]@Proxy";
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // android.hardware.boot.V1_0.IBootControl
        public int getNumberSlots() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
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

        @Override // android.hardware.boot.V1_0.IBootControl
        public int getCurrentSlot() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
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

        @Override // android.hardware.boot.V1_0.IBootControl
        public CommandResult markBootSuccessful() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(3, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                CommandResult commandResult = new CommandResult();
                commandResult.readFromParcel(hwParcel2);
                return commandResult;
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.boot.V1_0.IBootControl
        public CommandResult setActiveBootSlot(int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(4, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                CommandResult commandResult = new CommandResult();
                commandResult.readFromParcel(hwParcel2);
                return commandResult;
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.boot.V1_0.IBootControl
        public CommandResult setSlotAsUnbootable(int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(5, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                CommandResult commandResult = new CommandResult();
                commandResult.readFromParcel(hwParcel2);
                return commandResult;
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.boot.V1_0.IBootControl
        public int isSlotBootable(int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(6, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.boot.V1_0.IBootControl
        public int isSlotMarkedSuccessful(int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
            hwParcel.writeInt32(i);
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

        @Override // android.hardware.boot.V1_0.IBootControl
        public String getSuffix(int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(8, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readString();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.boot.V1_1.IBootControl
        public boolean setSnapshotMergeStatus(int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IBootControl.kInterfaceName);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(9, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readBool();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.boot.V1_1.IBootControl
        public int getSnapshotMergeStatus() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IBootControl.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(10, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException {
            return this.mRemote.linkToDeath(deathRecipient, j);
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(deathRecipient);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends HwBinder implements IBootControl {
        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public IHwBinder asBinder() {
            return this;
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) {
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final String interfaceDescriptor() {
            return IBootControl.kInterfaceName;
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) {
            return true;
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) {
            return true;
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(IBootControl.kInterfaceName, android.hardware.boot.V1_0.IBootControl.kInterfaceName, IBase.kInterfaceName));
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{7, -48, -94, 82, -78, -40, -6, 53, -120, 121, 8, -87, -106, -70, 57, 92, -13, -110, -106, -125, -107, -4, 48, -81, -85, 121, 31, 70, -32, -62, 42, 82}, new byte[]{113, -110, -41, 86, -82, -70, 0, -85, -93, 47, 69, 4, -104, 29, -8, AcquiredInfo.VENDOR, 47, -4, -88, 62, 33, 12, 72, 56, -38, -65, 41, 94, 83, -23, 53, -112}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, AcquiredInfo.ROLL_TOO_EXTREME, -109, 36, -72, 59, AcquiredInfo.FIRST_FRAME_RECEIVED, -54, 76}));
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final DebugInfo getDebugInfo() {
            DebugInfo debugInfo = new DebugInfo();
            debugInfo.pid = HidlSupport.getPidIfSharable();
            debugInfo.ptr = 0L;
            debugInfo.arch = 0;
            return debugInfo;
        }

        @Override // android.hardware.boot.V1_1.IBootControl, android.hardware.boot.V1_0.IBootControl, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        public IHwInterface queryLocalInterface(String str) {
            if (IBootControl.kInterfaceName.equals(str)) {
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
                    hwParcel.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    int numberSlots = getNumberSlots();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(numberSlots);
                    hwParcel2.send();
                    return;
                case 2:
                    hwParcel.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    int currentSlot = getCurrentSlot();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(currentSlot);
                    hwParcel2.send();
                    return;
                case 3:
                    hwParcel.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    CommandResult markBootSuccessful = markBootSuccessful();
                    hwParcel2.writeStatus(0);
                    markBootSuccessful.writeToParcel(hwParcel2);
                    hwParcel2.send();
                    return;
                case 4:
                    hwParcel.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    CommandResult activeBootSlot = setActiveBootSlot(hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    activeBootSlot.writeToParcel(hwParcel2);
                    hwParcel2.send();
                    return;
                case 5:
                    hwParcel.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    CommandResult slotAsUnbootable = setSlotAsUnbootable(hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    slotAsUnbootable.writeToParcel(hwParcel2);
                    hwParcel2.send();
                    return;
                case 6:
                    hwParcel.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    int isSlotBootable = isSlotBootable(hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(isSlotBootable);
                    hwParcel2.send();
                    return;
                case 7:
                    hwParcel.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    int isSlotMarkedSuccessful = isSlotMarkedSuccessful(hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(isSlotMarkedSuccessful);
                    hwParcel2.send();
                    return;
                case 8:
                    hwParcel.enforceInterface(android.hardware.boot.V1_0.IBootControl.kInterfaceName);
                    String suffix = getSuffix(hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeString(suffix);
                    hwParcel2.send();
                    return;
                case 9:
                    hwParcel.enforceInterface(IBootControl.kInterfaceName);
                    boolean snapshotMergeStatus = setSnapshotMergeStatus(hwParcel.readInt32());
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeBool(snapshotMergeStatus);
                    hwParcel2.send();
                    return;
                case 10:
                    hwParcel.enforceInterface(IBootControl.kInterfaceName);
                    int snapshotMergeStatus2 = getSnapshotMergeStatus();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(snapshotMergeStatus2);
                    hwParcel2.send();
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
