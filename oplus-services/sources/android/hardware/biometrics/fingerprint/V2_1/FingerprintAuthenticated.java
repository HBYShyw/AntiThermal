package android.hardware.biometrics.fingerprint.V2_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class FingerprintAuthenticated {
    public FingerprintFingerId finger = new FingerprintFingerId();
    public byte[] hat = new byte[69];

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != FingerprintAuthenticated.class) {
            return false;
        }
        FingerprintAuthenticated fingerprintAuthenticated = (FingerprintAuthenticated) obj;
        return HidlSupport.deepEquals(this.finger, fingerprintAuthenticated.finger) && HidlSupport.deepEquals(this.hat, fingerprintAuthenticated.hat);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.finger)), Integer.valueOf(HidlSupport.deepHashCode(this.hat)));
    }

    public final String toString() {
        return "{.finger = " + this.finger + ", .hat = " + Arrays.toString(this.hat) + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(80L), 0L);
    }

    public static final ArrayList<FingerprintAuthenticated> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<FingerprintAuthenticated> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 80, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            FingerprintAuthenticated fingerprintAuthenticated = new FingerprintAuthenticated();
            fingerprintAuthenticated.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 80);
            arrayList.add(fingerprintAuthenticated);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.finger.readEmbeddedFromParcel(hwParcel, hwBlob, 0 + j);
        hwBlob.copyToInt8Array(j + 8, this.hat, 69);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(80);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<FingerprintAuthenticated> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 80);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 80);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        this.finger.writeEmbeddedToBlob(hwBlob, 0 + j);
        long j2 = j + 8;
        byte[] bArr = this.hat;
        if (bArr == null || bArr.length != 69) {
            throw new IllegalArgumentException("Array element is not of the expected length");
        }
        hwBlob.putInt8Array(j2, bArr);
    }
}
