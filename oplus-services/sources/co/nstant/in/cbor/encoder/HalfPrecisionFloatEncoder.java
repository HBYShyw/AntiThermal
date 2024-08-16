package co.nstant.in.cbor.encoder;

import android.hardware.audio.common.V2_0.AudioDevice;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.HalfPrecisionFloat;
import java.io.OutputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class HalfPrecisionFloatEncoder extends AbstractEncoder<HalfPrecisionFloat> {
    public HalfPrecisionFloatEncoder(CborEncoder cborEncoder, OutputStream outputStream) {
        super(cborEncoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(HalfPrecisionFloat halfPrecisionFloat) throws CborException {
        write(249);
        int fromFloat = fromFloat(halfPrecisionFloat.getValue());
        write((fromFloat >> 8) & 255);
        write((fromFloat >> 0) & 255);
    }

    public static int fromFloat(float f) {
        int floatToIntBits = Float.floatToIntBits(f);
        int i = (floatToIntBits >>> 16) & 32768;
        int i2 = (floatToIntBits + 4096) & Integer.MAX_VALUE;
        if (i2 >= 1199570944) {
            if ((Integer.MAX_VALUE & floatToIntBits) < 1199570944) {
                return i | 31743;
            }
            if (i2 < 2139095040) {
                return i | 31744;
            }
            return ((floatToIntBits & 8388607) >>> 13) | i | 31744;
        }
        if (i2 >= 947912704) {
            return ((i2 - 939524096) >>> 13) | i;
        }
        if (i2 < 855638016) {
            return i;
        }
        int i3 = (floatToIntBits & Integer.MAX_VALUE) >>> 23;
        return ((((floatToIntBits & 8388607) | AudioDevice.OUT_IP) + (AudioDevice.OUT_IP >>> (i3 - 102))) >>> (126 - i3)) | i;
    }
}
