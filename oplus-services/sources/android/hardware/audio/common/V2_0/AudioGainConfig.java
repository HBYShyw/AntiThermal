package android.hardware.audio.common.V2_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AudioGainConfig {
    public int index = 0;
    public int mode = 0;
    public int channelMask = 0;
    public int[] values = new int[32];
    public int rampDurationMs = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != AudioGainConfig.class) {
            return false;
        }
        AudioGainConfig audioGainConfig = (AudioGainConfig) obj;
        return this.index == audioGainConfig.index && this.mode == audioGainConfig.mode && this.channelMask == audioGainConfig.channelMask && HidlSupport.deepEquals(this.values, audioGainConfig.values) && this.rampDurationMs == audioGainConfig.rampDurationMs;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.index))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.mode))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.channelMask))), Integer.valueOf(HidlSupport.deepHashCode(this.values)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.rampDurationMs))));
    }

    public final String toString() {
        return "{.index = " + this.index + ", .mode = " + AudioGainMode.toString(this.mode) + ", .channelMask = " + AudioChannelMask.toString(this.channelMask) + ", .values = " + Arrays.toString(this.values) + ", .rampDurationMs = " + this.rampDurationMs + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(144L), 0L);
    }

    public static final ArrayList<AudioGainConfig> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<AudioGainConfig> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 144, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            AudioGainConfig audioGainConfig = new AudioGainConfig();
            audioGainConfig.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 144);
            arrayList.add(audioGainConfig);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.index = hwBlob.getInt32(0 + j);
        this.mode = hwBlob.getInt32(4 + j);
        this.channelMask = hwBlob.getInt32(8 + j);
        hwBlob.copyToInt32Array(12 + j, this.values, 32);
        this.rampDurationMs = hwBlob.getInt32(j + 140);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(144);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<AudioGainConfig> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 144);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 144);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(0 + j, this.index);
        hwBlob.putInt32(4 + j, this.mode);
        hwBlob.putInt32(8 + j, this.channelMask);
        long j2 = 12 + j;
        int[] iArr = this.values;
        if (iArr == null || iArr.length != 32) {
            throw new IllegalArgumentException("Array element is not of the expected length");
        }
        hwBlob.putInt32Array(j2, iArr);
        hwBlob.putInt32(j + 140, this.rampDurationMs);
    }
}
