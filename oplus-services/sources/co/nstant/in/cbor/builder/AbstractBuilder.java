package co.nstant.in.cbor.builder;

import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.decoder.HalfPrecisionFloatDecoder;
import co.nstant.in.cbor.encoder.HalfPrecisionFloatEncoder;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.DoublePrecisionFloat;
import co.nstant.in.cbor.model.HalfPrecisionFloat;
import co.nstant.in.cbor.model.NegativeInteger;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SinglePrecisionFloat;
import co.nstant.in.cbor.model.Tag;
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class AbstractBuilder<T> {
    private final T parent;

    public AbstractBuilder(T t) {
        this.parent = t;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public T getParent() {
        return this.parent;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addChunk(DataItem dataItem) {
        throw new IllegalStateException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DataItem convert(long j) {
        if (j >= 0) {
            return new UnsignedInteger(j);
        }
        return new NegativeInteger(j);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DataItem convert(BigInteger bigInteger) {
        if (bigInteger.signum() == -1) {
            return new NegativeInteger(bigInteger);
        }
        return new UnsignedInteger(bigInteger);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DataItem convert(boolean z) {
        if (z) {
            return SimpleValue.TRUE;
        }
        return SimpleValue.FALSE;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DataItem convert(byte[] bArr) {
        return new ByteString(bArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DataItem convert(String str) {
        return new UnicodeString(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DataItem convert(float f) {
        if (isHalfPrecisionEnough(f)) {
            return new HalfPrecisionFloat(f);
        }
        return new SinglePrecisionFloat(f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DataItem convert(double d) {
        return new DoublePrecisionFloat(d);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Tag tag(long j) {
        return new Tag(j);
    }

    private boolean isHalfPrecisionEnough(float f) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            getHalfPrecisionFloatEncoder(byteArrayOutputStream).encode(new HalfPrecisionFloat(f));
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            HalfPrecisionFloatDecoder halfPrecisionFloatDecoder = getHalfPrecisionFloatDecoder(byteArrayInputStream);
            if (byteArrayInputStream.read() != -1) {
                return f == halfPrecisionFloatDecoder.decode(0).getValue();
            }
            throw new CborException("unexpected end of stream");
        } catch (CborException unused) {
            return false;
        }
    }

    protected HalfPrecisionFloatEncoder getHalfPrecisionFloatEncoder(OutputStream outputStream) {
        return new HalfPrecisionFloatEncoder(null, outputStream);
    }

    protected HalfPrecisionFloatDecoder getHalfPrecisionFloatDecoder(InputStream inputStream) {
        return new HalfPrecisionFloatDecoder(null, inputStream);
    }
}
