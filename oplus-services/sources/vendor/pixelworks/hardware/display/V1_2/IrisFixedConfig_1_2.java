package vendor.pixelworks.hardware.display.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;
import vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class IrisFixedConfig_1_2 {
    public IrisFixedConfig base = new IrisFixedConfig();
    public int memcToPt = 0;
    public int clientCompRequest = 0;
    public int hdrRequest = 0;
    public int motionLayerIdUsing = 0;
    public int testOption = 0;
    public int activeTask = 0;
    public int emvMvdId = 0;
    public int emvGameId = 0;
    public int pqSwitchType = 0;
    public ArrayList<Integer> reserved = new ArrayList<>();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != IrisFixedConfig_1_2.class) {
            return false;
        }
        IrisFixedConfig_1_2 irisFixedConfig_1_2 = (IrisFixedConfig_1_2) obj;
        return HidlSupport.deepEquals(this.base, irisFixedConfig_1_2.base) && this.memcToPt == irisFixedConfig_1_2.memcToPt && this.clientCompRequest == irisFixedConfig_1_2.clientCompRequest && this.hdrRequest == irisFixedConfig_1_2.hdrRequest && this.motionLayerIdUsing == irisFixedConfig_1_2.motionLayerIdUsing && this.testOption == irisFixedConfig_1_2.testOption && this.activeTask == irisFixedConfig_1_2.activeTask && this.emvMvdId == irisFixedConfig_1_2.emvMvdId && this.emvGameId == irisFixedConfig_1_2.emvGameId && this.pqSwitchType == irisFixedConfig_1_2.pqSwitchType && HidlSupport.deepEquals(this.reserved, irisFixedConfig_1_2.reserved);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.base)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.memcToPt))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.clientCompRequest))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.hdrRequest))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.motionLayerIdUsing))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.testOption))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.activeTask))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.emvMvdId))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.emvGameId))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.pqSwitchType))), Integer.valueOf(HidlSupport.deepHashCode(this.reserved)));
    }

    public final String toString() {
        return "{.base = " + this.base + ", .memcToPt = " + this.memcToPt + ", .clientCompRequest = " + this.clientCompRequest + ", .hdrRequest = " + this.hdrRequest + ", .motionLayerIdUsing = " + this.motionLayerIdUsing + ", .testOption = " + this.testOption + ", .activeTask = " + this.activeTask + ", .emvMvdId = " + this.emvMvdId + ", .emvGameId = " + this.emvGameId + ", .pqSwitchType = " + this.pqSwitchType + ", .reserved = " + this.reserved + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(152L), 0L);
    }

    public static final ArrayList<IrisFixedConfig_1_2> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<IrisFixedConfig_1_2> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 152, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            IrisFixedConfig_1_2 irisFixedConfig_1_2 = new IrisFixedConfig_1_2();
            irisFixedConfig_1_2.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 152);
            arrayList.add(irisFixedConfig_1_2);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.base.readEmbeddedFromParcel(hwParcel, hwBlob, j + 0);
        this.memcToPt = hwBlob.getInt32(j + 96);
        this.clientCompRequest = hwBlob.getInt32(j + 100);
        this.hdrRequest = hwBlob.getInt32(j + 104);
        this.motionLayerIdUsing = hwBlob.getInt32(j + 108);
        this.testOption = hwBlob.getInt32(j + 112);
        this.activeTask = hwBlob.getInt32(j + 116);
        this.emvMvdId = hwBlob.getInt32(j + 120);
        this.emvGameId = hwBlob.getInt32(j + 124);
        this.pqSwitchType = hwBlob.getInt32(j + 128);
        long j2 = j + 136;
        int int32 = hwBlob.getInt32(8 + j2);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 4, hwBlob.handle(), j2 + 0, true);
        this.reserved.clear();
        for (int i = 0; i < int32; i++) {
            this.reserved.add(Integer.valueOf(readEmbeddedBuffer.getInt32(i * 4)));
        }
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(152);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<IrisFixedConfig_1_2> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 152);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 152);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        this.base.writeEmbeddedToBlob(hwBlob, j + 0);
        hwBlob.putInt32(96 + j, this.memcToPt);
        hwBlob.putInt32(100 + j, this.clientCompRequest);
        hwBlob.putInt32(104 + j, this.hdrRequest);
        hwBlob.putInt32(108 + j, this.motionLayerIdUsing);
        hwBlob.putInt32(112 + j, this.testOption);
        hwBlob.putInt32(116 + j, this.activeTask);
        hwBlob.putInt32(120 + j, this.emvMvdId);
        hwBlob.putInt32(124 + j, this.emvGameId);
        hwBlob.putInt32(128 + j, this.pqSwitchType);
        int size = this.reserved.size();
        long j2 = j + 136;
        hwBlob.putInt32(8 + j2, size);
        hwBlob.putBool(12 + j2, false);
        HwBlob hwBlob2 = new HwBlob(size * 4);
        for (int i = 0; i < size; i++) {
            hwBlob2.putInt32(i * 4, this.reserved.get(i).intValue());
        }
        hwBlob.putBlob(j2 + 0, hwBlob2);
    }
}
