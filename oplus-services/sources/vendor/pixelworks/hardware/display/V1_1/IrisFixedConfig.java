package vendor.pixelworks.hardware.display.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class IrisFixedConfig {
    public int hdrFormal = 0;
    public ArrayList<Integer> hdrLut = new ArrayList<>();
    public int memcEnable = 0;
    public int memcLevel = 0;
    public int dualChannel = 0;
    public int movingLayer = 0;
    public int memcVideoLayer = 0;
    public int inMemcState = 0;
    public int videoFps = 0;
    public int videoInMemory = 0;
    public int gameMode = 0;
    public int captureDisable = 0;
    public int dualPrepare = 0;
    public int inOsdSwitch = 0;
    public int dualPreload = 0;
    public int metadataDone = 0;
    public ArrayList<Integer> reserved = new ArrayList<>();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != IrisFixedConfig.class) {
            return false;
        }
        IrisFixedConfig irisFixedConfig = (IrisFixedConfig) obj;
        return this.hdrFormal == irisFixedConfig.hdrFormal && HidlSupport.deepEquals(this.hdrLut, irisFixedConfig.hdrLut) && this.memcEnable == irisFixedConfig.memcEnable && this.memcLevel == irisFixedConfig.memcLevel && this.dualChannel == irisFixedConfig.dualChannel && this.movingLayer == irisFixedConfig.movingLayer && this.memcVideoLayer == irisFixedConfig.memcVideoLayer && this.inMemcState == irisFixedConfig.inMemcState && this.videoFps == irisFixedConfig.videoFps && this.videoInMemory == irisFixedConfig.videoInMemory && this.gameMode == irisFixedConfig.gameMode && this.captureDisable == irisFixedConfig.captureDisable && this.dualPrepare == irisFixedConfig.dualPrepare && this.inOsdSwitch == irisFixedConfig.inOsdSwitch && this.dualPreload == irisFixedConfig.dualPreload && this.metadataDone == irisFixedConfig.metadataDone && HidlSupport.deepEquals(this.reserved, irisFixedConfig.reserved);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.hdrFormal))), Integer.valueOf(HidlSupport.deepHashCode(this.hdrLut)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.memcEnable))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.memcLevel))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.dualChannel))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.movingLayer))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.memcVideoLayer))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.inMemcState))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.videoFps))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.videoInMemory))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.gameMode))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.captureDisable))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.dualPrepare))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.inOsdSwitch))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.dualPreload))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.metadataDone))), Integer.valueOf(HidlSupport.deepHashCode(this.reserved)));
    }

    public final String toString() {
        return "{.hdrFormal = " + this.hdrFormal + ", .hdrLut = " + this.hdrLut + ", .memcEnable = " + this.memcEnable + ", .memcLevel = " + this.memcLevel + ", .dualChannel = " + this.dualChannel + ", .movingLayer = " + this.movingLayer + ", .memcVideoLayer = " + this.memcVideoLayer + ", .inMemcState = " + this.inMemcState + ", .videoFps = " + this.videoFps + ", .videoInMemory = " + this.videoInMemory + ", .gameMode = " + this.gameMode + ", .captureDisable = " + this.captureDisable + ", .dualPrepare = " + this.dualPrepare + ", .inOsdSwitch = " + this.inOsdSwitch + ", .dualPreload = " + this.dualPreload + ", .metadataDone = " + this.metadataDone + ", .reserved = " + this.reserved + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(96L), 0L);
    }

    public static final ArrayList<IrisFixedConfig> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<IrisFixedConfig> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 96, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            IrisFixedConfig irisFixedConfig = new IrisFixedConfig();
            irisFixedConfig.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 96);
            arrayList.add(irisFixedConfig);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.hdrFormal = hwBlob.getInt32(j + 0);
        long j2 = j + 8;
        int int32 = hwBlob.getInt32(j2 + 8);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 4, hwBlob.handle(), j2 + 0, true);
        this.hdrLut.clear();
        for (int i = 0; i < int32; i++) {
            this.hdrLut.add(Integer.valueOf(readEmbeddedBuffer.getInt32(i * 4)));
        }
        this.memcEnable = hwBlob.getInt32(j + 24);
        this.memcLevel = hwBlob.getInt32(j + 28);
        this.dualChannel = hwBlob.getInt32(j + 32);
        this.movingLayer = hwBlob.getInt32(j + 36);
        this.memcVideoLayer = hwBlob.getInt32(j + 40);
        this.inMemcState = hwBlob.getInt32(j + 44);
        this.videoFps = hwBlob.getInt32(j + 48);
        this.videoInMemory = hwBlob.getInt32(j + 52);
        this.gameMode = hwBlob.getInt32(j + 56);
        this.captureDisable = hwBlob.getInt32(j + 60);
        this.dualPrepare = hwBlob.getInt32(j + 64);
        this.inOsdSwitch = hwBlob.getInt32(j + 68);
        this.dualPreload = hwBlob.getInt32(j + 72);
        this.metadataDone = hwBlob.getInt32(j + 76);
        long j3 = j + 80;
        int int322 = hwBlob.getInt32(8 + j3);
        HwBlob readEmbeddedBuffer2 = hwParcel.readEmbeddedBuffer(int322 * 4, hwBlob.handle(), j3 + 0, true);
        this.reserved.clear();
        for (int i2 = 0; i2 < int322; i2++) {
            this.reserved.add(Integer.valueOf(readEmbeddedBuffer2.getInt32(i2 * 4)));
        }
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(96);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<IrisFixedConfig> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 96);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 96);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(j + 0, this.hdrFormal);
        int size = this.hdrLut.size();
        long j2 = j + 8;
        hwBlob.putInt32(j2 + 8, size);
        hwBlob.putBool(j2 + 12, false);
        HwBlob hwBlob2 = new HwBlob(size * 4);
        for (int i = 0; i < size; i++) {
            hwBlob2.putInt32(i * 4, this.hdrLut.get(i).intValue());
        }
        hwBlob.putBlob(j2 + 0, hwBlob2);
        hwBlob.putInt32(j + 24, this.memcEnable);
        hwBlob.putInt32(j + 28, this.memcLevel);
        hwBlob.putInt32(j + 32, this.dualChannel);
        hwBlob.putInt32(j + 36, this.movingLayer);
        hwBlob.putInt32(j + 40, this.memcVideoLayer);
        hwBlob.putInt32(j + 44, this.inMemcState);
        hwBlob.putInt32(j + 48, this.videoFps);
        hwBlob.putInt32(j + 52, this.videoInMemory);
        hwBlob.putInt32(j + 56, this.gameMode);
        hwBlob.putInt32(j + 60, this.captureDisable);
        hwBlob.putInt32(j + 64, this.dualPrepare);
        hwBlob.putInt32(j + 68, this.inOsdSwitch);
        hwBlob.putInt32(j + 72, this.dualPreload);
        hwBlob.putInt32(j + 76, this.metadataDone);
        int size2 = this.reserved.size();
        long j3 = j + 80;
        hwBlob.putInt32(8 + j3, size2);
        hwBlob.putBool(12 + j3, false);
        HwBlob hwBlob3 = new HwBlob(size2 * 4);
        for (int i2 = 0; i2 < size2; i2++) {
            hwBlob3.putInt32(i2 * 4, this.reserved.get(i2).intValue());
        }
        hwBlob.putBlob(j3 + 0, hwBlob3);
    }
}
