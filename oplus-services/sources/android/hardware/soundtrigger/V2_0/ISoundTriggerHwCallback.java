package android.hardware.soundtrigger.V2_0;

import android.hardware.audio.common.V2_0.AudioConfig;
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
import com.android.internal.util.FrameworkStatsLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ISoundTriggerHwCallback extends IBase {
    public static final String kInterfaceName = "android.hardware.soundtrigger@2.0::ISoundTriggerHwCallback";

    @Override // android.hidl.base.V1_0.IBase
    IHwBinder asBinder();

    @Override // android.hidl.base.V1_0.IBase
    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    DebugInfo getDebugInfo() throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    ArrayList<byte[]> getHashChain() throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    ArrayList<String> interfaceChain() throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    String interfaceDescriptor() throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws RemoteException;

    void phraseRecognitionCallback(PhraseRecognitionEvent phraseRecognitionEvent, int i) throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void ping() throws RemoteException;

    void recognitionCallback(RecognitionEvent recognitionEvent, int i) throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws RemoteException;

    void soundModelCallback(ModelEvent modelEvent, int i) throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static ISoundTriggerHwCallback asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        IHwInterface queryLocalInterface = iHwBinder.queryLocalInterface(kInterfaceName);
        if (queryLocalInterface != null && (queryLocalInterface instanceof ISoundTriggerHwCallback)) {
            return (ISoundTriggerHwCallback) queryLocalInterface;
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

    static ISoundTriggerHwCallback castFrom(IHwInterface iHwInterface) {
        if (iHwInterface == null) {
            return null;
        }
        return asInterface(iHwInterface.asBinder());
    }

    static ISoundTriggerHwCallback getService(String str, boolean z) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str, z));
    }

    static ISoundTriggerHwCallback getService(boolean z) throws RemoteException {
        return getService("default", z);
    }

    @Deprecated
    static ISoundTriggerHwCallback getService(String str) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str));
    }

    @Deprecated
    static ISoundTriggerHwCallback getService() throws RemoteException {
        return getService("default");
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class RecognitionStatus {
        public static final int ABORT = 1;
        public static final int FAILURE = 2;
        public static final int SUCCESS = 0;

        public static final String toString(int i) {
            if (i == 0) {
                return "SUCCESS";
            }
            if (i == 1) {
                return "ABORT";
            }
            if (i == 2) {
                return "FAILURE";
            }
            return "0x" + Integer.toHexString(i);
        }

        public static final String dumpBitfield(int i) {
            ArrayList arrayList = new ArrayList();
            arrayList.add("SUCCESS");
            int i2 = 1;
            if ((i & 1) == 1) {
                arrayList.add("ABORT");
            } else {
                i2 = 0;
            }
            if ((i & 2) == 2) {
                arrayList.add("FAILURE");
                i2 |= 2;
            }
            if (i != i2) {
                arrayList.add("0x" + Integer.toHexString(i & (~i2)));
            }
            return String.join(" | ", arrayList);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class SoundModelStatus {
        public static final int UPDATED = 0;

        public static final String toString(int i) {
            if (i == 0) {
                return "UPDATED";
            }
            return "0x" + Integer.toHexString(i);
        }

        public static final String dumpBitfield(int i) {
            ArrayList arrayList = new ArrayList();
            arrayList.add("UPDATED");
            if (i != 0) {
                arrayList.add("0x" + Integer.toHexString(i & (-1)));
            }
            return String.join(" | ", arrayList);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class RecognitionEvent {
        public int status = 0;
        public int type = 0;
        public int model = 0;
        public boolean captureAvailable = false;
        public int captureSession = 0;
        public int captureDelayMs = 0;
        public int capturePreambleMs = 0;
        public boolean triggerInData = false;
        public AudioConfig audioConfig = new AudioConfig();
        public ArrayList<Byte> data = new ArrayList<>();

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || obj.getClass() != RecognitionEvent.class) {
                return false;
            }
            RecognitionEvent recognitionEvent = (RecognitionEvent) obj;
            return this.status == recognitionEvent.status && this.type == recognitionEvent.type && this.model == recognitionEvent.model && this.captureAvailable == recognitionEvent.captureAvailable && this.captureSession == recognitionEvent.captureSession && this.captureDelayMs == recognitionEvent.captureDelayMs && this.capturePreambleMs == recognitionEvent.capturePreambleMs && this.triggerInData == recognitionEvent.triggerInData && HidlSupport.deepEquals(this.audioConfig, recognitionEvent.audioConfig) && HidlSupport.deepEquals(this.data, recognitionEvent.data);
        }

        public final int hashCode() {
            return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.status))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.type))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.model))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.captureAvailable))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.captureSession))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.captureDelayMs))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.capturePreambleMs))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.triggerInData))), Integer.valueOf(HidlSupport.deepHashCode(this.audioConfig)), Integer.valueOf(HidlSupport.deepHashCode(this.data)));
        }

        public final String toString() {
            return "{.status = " + RecognitionStatus.toString(this.status) + ", .type = " + SoundModelType.toString(this.type) + ", .model = " + this.model + ", .captureAvailable = " + this.captureAvailable + ", .captureSession = " + this.captureSession + ", .captureDelayMs = " + this.captureDelayMs + ", .capturePreambleMs = " + this.capturePreambleMs + ", .triggerInData = " + this.triggerInData + ", .audioConfig = " + this.audioConfig + ", .data = " + this.data + "}";
        }

        public final void readFromParcel(HwParcel hwParcel) {
            readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(120L), 0L);
        }

        public static final ArrayList<RecognitionEvent> readVectorFromParcel(HwParcel hwParcel) {
            ArrayList<RecognitionEvent> arrayList = new ArrayList<>();
            HwBlob readBuffer = hwParcel.readBuffer(16L);
            int int32 = readBuffer.getInt32(8L);
            HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 120, readBuffer.handle(), 0L, true);
            arrayList.clear();
            for (int i = 0; i < int32; i++) {
                RecognitionEvent recognitionEvent = new RecognitionEvent();
                recognitionEvent.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 120);
                arrayList.add(recognitionEvent);
            }
            return arrayList;
        }

        public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
            this.status = hwBlob.getInt32(j + 0);
            this.type = hwBlob.getInt32(j + 4);
            this.model = hwBlob.getInt32(j + 8);
            this.captureAvailable = hwBlob.getBool(j + 12);
            this.captureSession = hwBlob.getInt32(j + 16);
            this.captureDelayMs = hwBlob.getInt32(j + 20);
            this.capturePreambleMs = hwBlob.getInt32(j + 24);
            this.triggerInData = hwBlob.getBool(j + 28);
            this.audioConfig.readEmbeddedFromParcel(hwParcel, hwBlob, j + 32);
            long j2 = j + 104;
            int int32 = hwBlob.getInt32(8 + j2);
            HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 1, hwBlob.handle(), j2 + 0, true);
            this.data.clear();
            for (int i = 0; i < int32; i++) {
                this.data.add(Byte.valueOf(readEmbeddedBuffer.getInt8(i * 1)));
            }
        }

        public final void writeToParcel(HwParcel hwParcel) {
            HwBlob hwBlob = new HwBlob(120);
            writeEmbeddedToBlob(hwBlob, 0L);
            hwParcel.writeBuffer(hwBlob);
        }

        public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<RecognitionEvent> arrayList) {
            HwBlob hwBlob = new HwBlob(16);
            int size = arrayList.size();
            hwBlob.putInt32(8L, size);
            hwBlob.putBool(12L, false);
            HwBlob hwBlob2 = new HwBlob(size * 120);
            for (int i = 0; i < size; i++) {
                arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 120);
            }
            hwBlob.putBlob(0L, hwBlob2);
            hwParcel.writeBuffer(hwBlob);
        }

        public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
            hwBlob.putInt32(j + 0, this.status);
            hwBlob.putInt32(4 + j, this.type);
            hwBlob.putInt32(j + 8, this.model);
            hwBlob.putBool(j + 12, this.captureAvailable);
            hwBlob.putInt32(16 + j, this.captureSession);
            hwBlob.putInt32(20 + j, this.captureDelayMs);
            hwBlob.putInt32(24 + j, this.capturePreambleMs);
            hwBlob.putBool(28 + j, this.triggerInData);
            this.audioConfig.writeEmbeddedToBlob(hwBlob, 32 + j);
            int size = this.data.size();
            long j2 = j + 104;
            hwBlob.putInt32(8 + j2, size);
            hwBlob.putBool(12 + j2, false);
            HwBlob hwBlob2 = new HwBlob(size * 1);
            for (int i = 0; i < size; i++) {
                hwBlob2.putInt8(i * 1, this.data.get(i).byteValue());
            }
            hwBlob.putBlob(j2 + 0, hwBlob2);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class PhraseRecognitionEvent {
        public RecognitionEvent common = new RecognitionEvent();
        public ArrayList<PhraseRecognitionExtra> phraseExtras = new ArrayList<>();

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || obj.getClass() != PhraseRecognitionEvent.class) {
                return false;
            }
            PhraseRecognitionEvent phraseRecognitionEvent = (PhraseRecognitionEvent) obj;
            return HidlSupport.deepEquals(this.common, phraseRecognitionEvent.common) && HidlSupport.deepEquals(this.phraseExtras, phraseRecognitionEvent.phraseExtras);
        }

        public final int hashCode() {
            return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.common)), Integer.valueOf(HidlSupport.deepHashCode(this.phraseExtras)));
        }

        public final String toString() {
            return "{.common = " + this.common + ", .phraseExtras = " + this.phraseExtras + "}";
        }

        public final void readFromParcel(HwParcel hwParcel) {
            readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(136L), 0L);
        }

        public static final ArrayList<PhraseRecognitionEvent> readVectorFromParcel(HwParcel hwParcel) {
            ArrayList<PhraseRecognitionEvent> arrayList = new ArrayList<>();
            HwBlob readBuffer = hwParcel.readBuffer(16L);
            int int32 = readBuffer.getInt32(8L);
            HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MANAGED_PROFILE_MAXIMUM_TIME_OFF, readBuffer.handle(), 0L, true);
            arrayList.clear();
            for (int i = 0; i < int32; i++) {
                PhraseRecognitionEvent phraseRecognitionEvent = new PhraseRecognitionEvent();
                phraseRecognitionEvent.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MANAGED_PROFILE_MAXIMUM_TIME_OFF);
                arrayList.add(phraseRecognitionEvent);
            }
            return arrayList;
        }

        public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
            this.common.readEmbeddedFromParcel(hwParcel, hwBlob, j + 0);
            long j2 = j + 120;
            int int32 = hwBlob.getInt32(8 + j2);
            HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 32, hwBlob.handle(), j2 + 0, true);
            this.phraseExtras.clear();
            for (int i = 0; i < int32; i++) {
                PhraseRecognitionExtra phraseRecognitionExtra = new PhraseRecognitionExtra();
                phraseRecognitionExtra.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 32);
                this.phraseExtras.add(phraseRecognitionExtra);
            }
        }

        public final void writeToParcel(HwParcel hwParcel) {
            HwBlob hwBlob = new HwBlob(FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MANAGED_PROFILE_MAXIMUM_TIME_OFF);
            writeEmbeddedToBlob(hwBlob, 0L);
            hwParcel.writeBuffer(hwBlob);
        }

        public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<PhraseRecognitionEvent> arrayList) {
            HwBlob hwBlob = new HwBlob(16);
            int size = arrayList.size();
            hwBlob.putInt32(8L, size);
            hwBlob.putBool(12L, false);
            HwBlob hwBlob2 = new HwBlob(size * FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MANAGED_PROFILE_MAXIMUM_TIME_OFF);
            for (int i = 0; i < size; i++) {
                arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MANAGED_PROFILE_MAXIMUM_TIME_OFF);
            }
            hwBlob.putBlob(0L, hwBlob2);
            hwParcel.writeBuffer(hwBlob);
        }

        public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
            this.common.writeEmbeddedToBlob(hwBlob, j + 0);
            int size = this.phraseExtras.size();
            long j2 = j + 120;
            hwBlob.putInt32(8 + j2, size);
            hwBlob.putBool(12 + j2, false);
            HwBlob hwBlob2 = new HwBlob(size * 32);
            for (int i = 0; i < size; i++) {
                this.phraseExtras.get(i).writeEmbeddedToBlob(hwBlob2, i * 32);
            }
            hwBlob.putBlob(j2 + 0, hwBlob2);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class ModelEvent {
        public int status = 0;
        public int model = 0;
        public ArrayList<Byte> data = new ArrayList<>();

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || obj.getClass() != ModelEvent.class) {
                return false;
            }
            ModelEvent modelEvent = (ModelEvent) obj;
            return this.status == modelEvent.status && this.model == modelEvent.model && HidlSupport.deepEquals(this.data, modelEvent.data);
        }

        public final int hashCode() {
            return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.status))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.model))), Integer.valueOf(HidlSupport.deepHashCode(this.data)));
        }

        public final String toString() {
            return "{.status = " + SoundModelStatus.toString(this.status) + ", .model = " + this.model + ", .data = " + this.data + "}";
        }

        public final void readFromParcel(HwParcel hwParcel) {
            readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(24L), 0L);
        }

        public static final ArrayList<ModelEvent> readVectorFromParcel(HwParcel hwParcel) {
            ArrayList<ModelEvent> arrayList = new ArrayList<>();
            HwBlob readBuffer = hwParcel.readBuffer(16L);
            int int32 = readBuffer.getInt32(8L);
            HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 24, readBuffer.handle(), 0L, true);
            arrayList.clear();
            for (int i = 0; i < int32; i++) {
                ModelEvent modelEvent = new ModelEvent();
                modelEvent.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 24);
                arrayList.add(modelEvent);
            }
            return arrayList;
        }

        public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
            this.status = hwBlob.getInt32(j + 0);
            this.model = hwBlob.getInt32(j + 4);
            long j2 = j + 8;
            int int32 = hwBlob.getInt32(8 + j2);
            HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 1, hwBlob.handle(), j2 + 0, true);
            this.data.clear();
            for (int i = 0; i < int32; i++) {
                this.data.add(Byte.valueOf(readEmbeddedBuffer.getInt8(i * 1)));
            }
        }

        public final void writeToParcel(HwParcel hwParcel) {
            HwBlob hwBlob = new HwBlob(24);
            writeEmbeddedToBlob(hwBlob, 0L);
            hwParcel.writeBuffer(hwBlob);
        }

        public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<ModelEvent> arrayList) {
            HwBlob hwBlob = new HwBlob(16);
            int size = arrayList.size();
            hwBlob.putInt32(8L, size);
            hwBlob.putBool(12L, false);
            HwBlob hwBlob2 = new HwBlob(size * 24);
            for (int i = 0; i < size; i++) {
                arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 24);
            }
            hwBlob.putBlob(0L, hwBlob2);
            hwParcel.writeBuffer(hwBlob);
        }

        public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
            hwBlob.putInt32(j + 0, this.status);
            hwBlob.putInt32(4 + j, this.model);
            int size = this.data.size();
            long j2 = j + 8;
            hwBlob.putInt32(8 + j2, size);
            hwBlob.putBool(12 + j2, false);
            HwBlob hwBlob2 = new HwBlob(size * 1);
            for (int i = 0; i < size; i++) {
                hwBlob2.putInt8(i * 1, this.data.get(i).byteValue());
            }
            hwBlob.putBlob(j2 + 0, hwBlob2);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Proxy implements ISoundTriggerHwCallback {
        private IHwBinder mRemote;

        public Proxy(IHwBinder iHwBinder) {
            Objects.requireNonNull(iHwBinder);
            this.mRemote = iHwBinder;
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException unused) {
                return "[class or subclass of android.hardware.soundtrigger@2.0::ISoundTriggerHwCallback]@Proxy";
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback
        public void recognitionCallback(RecognitionEvent recognitionEvent, int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ISoundTriggerHwCallback.kInterfaceName);
            recognitionEvent.writeToParcel(hwParcel);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(1, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback
        public void phraseRecognitionCallback(PhraseRecognitionEvent phraseRecognitionEvent, int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ISoundTriggerHwCallback.kInterfaceName);
            phraseRecognitionEvent.writeToParcel(hwParcel);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(2, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback
        public void soundModelCallback(ModelEvent modelEvent, int i) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(ISoundTriggerHwCallback.kInterfaceName);
            modelEvent.writeToParcel(hwParcel);
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(3, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException {
            return this.mRemote.linkToDeath(deathRecipient, j);
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(deathRecipient);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends HwBinder implements ISoundTriggerHwCallback {
        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public IHwBinder asBinder() {
            return this;
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) {
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final String interfaceDescriptor() {
            return ISoundTriggerHwCallback.kInterfaceName;
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) {
            return true;
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) {
            return true;
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(ISoundTriggerHwCallback.kInterfaceName, IBase.kInterfaceName));
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{AcquiredInfo.MOUTH_COVERING_DETECTED, 110, 43, -46, -119, -14, 41, 49, -59, 38, -78, AcquiredInfo.DARK_GLASSES_DETECTED, AcquiredInfo.SENSOR_DIRTY, -111, 15, 29, 76, 67, 107, 122, -53, -107, 86, -28, 36, 61, -28, -50, -114, 108, -62, -28}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, AcquiredInfo.ROLL_TOO_EXTREME, -109, 36, -72, 59, AcquiredInfo.FIRST_FRAME_RECEIVED, -54, 76}));
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final DebugInfo getDebugInfo() {
            DebugInfo debugInfo = new DebugInfo();
            debugInfo.pid = HidlSupport.getPidIfSharable();
            debugInfo.ptr = 0L;
            debugInfo.arch = 0;
            return debugInfo;
        }

        @Override // android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        public IHwInterface queryLocalInterface(String str) {
            if (ISoundTriggerHwCallback.kInterfaceName.equals(str)) {
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
                hwParcel.enforceInterface(ISoundTriggerHwCallback.kInterfaceName);
                RecognitionEvent recognitionEvent = new RecognitionEvent();
                recognitionEvent.readFromParcel(hwParcel);
                recognitionCallback(recognitionEvent, hwParcel.readInt32());
                hwParcel2.writeStatus(0);
                hwParcel2.send();
                return;
            }
            if (i == 2) {
                hwParcel.enforceInterface(ISoundTriggerHwCallback.kInterfaceName);
                PhraseRecognitionEvent phraseRecognitionEvent = new PhraseRecognitionEvent();
                phraseRecognitionEvent.readFromParcel(hwParcel);
                phraseRecognitionCallback(phraseRecognitionEvent, hwParcel.readInt32());
                hwParcel2.writeStatus(0);
                hwParcel2.send();
                return;
            }
            if (i == 3) {
                hwParcel.enforceInterface(ISoundTriggerHwCallback.kInterfaceName);
                ModelEvent modelEvent = new ModelEvent();
                modelEvent.readFromParcel(hwParcel);
                soundModelCallback(modelEvent, hwParcel.readInt32());
                hwParcel2.writeStatus(0);
                hwParcel2.send();
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
