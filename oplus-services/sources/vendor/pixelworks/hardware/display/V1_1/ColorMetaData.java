package vendor.pixelworks.hardware.display.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class ColorMetaData {
    public int colorPrimaries = 0;
    public int range = 0;
    public int transfer = 0;
    public boolean lightLevelSEIEnabled = false;
    public int maxContentLightLevel = 0;
    public int minPicAverageLightLevel = 0;
    public boolean dynamicMetaDataValid = false;
    public int dynamicMetaDataLen = 0;
    public ArrayList<Byte> dynamicMetaDataPayload = new ArrayList<>();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != ColorMetaData.class) {
            return false;
        }
        ColorMetaData colorMetaData = (ColorMetaData) obj;
        return this.colorPrimaries == colorMetaData.colorPrimaries && this.range == colorMetaData.range && this.transfer == colorMetaData.transfer && this.lightLevelSEIEnabled == colorMetaData.lightLevelSEIEnabled && this.maxContentLightLevel == colorMetaData.maxContentLightLevel && this.minPicAverageLightLevel == colorMetaData.minPicAverageLightLevel && this.dynamicMetaDataValid == colorMetaData.dynamicMetaDataValid && this.dynamicMetaDataLen == colorMetaData.dynamicMetaDataLen && HidlSupport.deepEquals(this.dynamicMetaDataPayload, colorMetaData.dynamicMetaDataPayload);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.colorPrimaries))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.range))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.transfer))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.lightLevelSEIEnabled))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.maxContentLightLevel))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.minPicAverageLightLevel))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.dynamicMetaDataValid))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.dynamicMetaDataLen))), Integer.valueOf(HidlSupport.deepHashCode(this.dynamicMetaDataPayload)));
    }

    public final String toString() {
        return "{.colorPrimaries = " + this.colorPrimaries + ", .range = " + this.range + ", .transfer = " + this.transfer + ", .lightLevelSEIEnabled = " + this.lightLevelSEIEnabled + ", .maxContentLightLevel = " + this.maxContentLightLevel + ", .minPicAverageLightLevel = " + this.minPicAverageLightLevel + ", .dynamicMetaDataValid = " + this.dynamicMetaDataValid + ", .dynamicMetaDataLen = " + this.dynamicMetaDataLen + ", .dynamicMetaDataPayload = " + this.dynamicMetaDataPayload + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(48L), 0L);
    }

    public static final ArrayList<ColorMetaData> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<ColorMetaData> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 48, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            ColorMetaData colorMetaData = new ColorMetaData();
            colorMetaData.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 48);
            arrayList.add(colorMetaData);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.colorPrimaries = hwBlob.getInt32(j + 0);
        this.range = hwBlob.getInt32(j + 4);
        this.transfer = hwBlob.getInt32(j + 8);
        this.lightLevelSEIEnabled = hwBlob.getBool(j + 12);
        this.maxContentLightLevel = hwBlob.getInt32(j + 16);
        this.minPicAverageLightLevel = hwBlob.getInt32(j + 20);
        this.dynamicMetaDataValid = hwBlob.getBool(j + 24);
        this.dynamicMetaDataLen = hwBlob.getInt32(j + 28);
        long j2 = j + 32;
        int int32 = hwBlob.getInt32(8 + j2);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 1, hwBlob.handle(), j2 + 0, true);
        this.dynamicMetaDataPayload.clear();
        for (int i = 0; i < int32; i++) {
            this.dynamicMetaDataPayload.add(Byte.valueOf(readEmbeddedBuffer.getInt8(i * 1)));
        }
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(48);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<ColorMetaData> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 48);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 48);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(j + 0, this.colorPrimaries);
        hwBlob.putInt32(4 + j, this.range);
        hwBlob.putInt32(j + 8, this.transfer);
        hwBlob.putBool(j + 12, this.lightLevelSEIEnabled);
        hwBlob.putInt32(16 + j, this.maxContentLightLevel);
        hwBlob.putInt32(20 + j, this.minPicAverageLightLevel);
        hwBlob.putBool(24 + j, this.dynamicMetaDataValid);
        hwBlob.putInt32(28 + j, this.dynamicMetaDataLen);
        int size = this.dynamicMetaDataPayload.size();
        long j2 = j + 32;
        hwBlob.putInt32(8 + j2, size);
        hwBlob.putBool(12 + j2, false);
        HwBlob hwBlob2 = new HwBlob(size * 1);
        for (int i = 0; i < size; i++) {
            hwBlob2.putInt8(i * 1, this.dynamicMetaDataPayload.get(i).byteValue());
        }
        hwBlob.putBlob(j2 + 0, hwBlob2);
    }
}
