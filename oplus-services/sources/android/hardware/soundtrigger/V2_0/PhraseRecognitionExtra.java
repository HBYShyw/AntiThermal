package android.hardware.soundtrigger.V2_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class PhraseRecognitionExtra {
    public int id = 0;
    public int recognitionModes = 0;
    public int confidenceLevel = 0;
    public ArrayList<ConfidenceLevel> levels = new ArrayList<>();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != PhraseRecognitionExtra.class) {
            return false;
        }
        PhraseRecognitionExtra phraseRecognitionExtra = (PhraseRecognitionExtra) obj;
        return this.id == phraseRecognitionExtra.id && this.recognitionModes == phraseRecognitionExtra.recognitionModes && this.confidenceLevel == phraseRecognitionExtra.confidenceLevel && HidlSupport.deepEquals(this.levels, phraseRecognitionExtra.levels);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.id))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.recognitionModes))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.confidenceLevel))), Integer.valueOf(HidlSupport.deepHashCode(this.levels)));
    }

    public final String toString() {
        return "{.id = " + this.id + ", .recognitionModes = " + this.recognitionModes + ", .confidenceLevel = " + this.confidenceLevel + ", .levels = " + this.levels + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(32L), 0L);
    }

    public static final ArrayList<PhraseRecognitionExtra> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<PhraseRecognitionExtra> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 32, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            PhraseRecognitionExtra phraseRecognitionExtra = new PhraseRecognitionExtra();
            phraseRecognitionExtra.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 32);
            arrayList.add(phraseRecognitionExtra);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.id = hwBlob.getInt32(j + 0);
        this.recognitionModes = hwBlob.getInt32(j + 4);
        this.confidenceLevel = hwBlob.getInt32(j + 8);
        long j2 = j + 16;
        int int32 = hwBlob.getInt32(8 + j2);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 8, hwBlob.handle(), j2 + 0, true);
        this.levels.clear();
        for (int i = 0; i < int32; i++) {
            ConfidenceLevel confidenceLevel = new ConfidenceLevel();
            confidenceLevel.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 8);
            this.levels.add(confidenceLevel);
        }
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(32);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<PhraseRecognitionExtra> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 32);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 32);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(j + 0, this.id);
        hwBlob.putInt32(4 + j, this.recognitionModes);
        hwBlob.putInt32(j + 8, this.confidenceLevel);
        int size = this.levels.size();
        long j2 = j + 16;
        hwBlob.putInt32(8 + j2, size);
        hwBlob.putBool(12 + j2, false);
        HwBlob hwBlob2 = new HwBlob(size * 8);
        for (int i = 0; i < size; i++) {
            this.levels.get(i).writeEmbeddedToBlob(hwBlob2, i * 8);
        }
        hwBlob.putBlob(j2 + 0, hwBlob2);
    }
}
