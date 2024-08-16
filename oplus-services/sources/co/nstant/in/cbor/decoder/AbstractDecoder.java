package co.nstant.in.cbor.decoder;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.AdditionalInformation;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class AbstractDecoder<T> {
    protected static final int INFINITY = -1;
    protected final CborDecoder decoder;
    protected final InputStream inputStream;

    public abstract T decode(int i) throws CborException;

    public AbstractDecoder(CborDecoder cborDecoder, InputStream inputStream) {
        this.decoder = cborDecoder;
        this.inputStream = inputStream;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int nextSymbol() throws CborException {
        try {
            int read = this.inputStream.read();
            if (read != -1) {
                return read;
            }
            throw new IOException("Unexpected end of stream");
        } catch (IOException e) {
            throw new CborException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: co.nstant.in.cbor.decoder.AbstractDecoder$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$co$nstant$in$cbor$model$AdditionalInformation;

        static {
            int[] iArr = new int[AdditionalInformation.values().length];
            $SwitchMap$co$nstant$in$cbor$model$AdditionalInformation = iArr;
            try {
                iArr[AdditionalInformation.DIRECT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$AdditionalInformation[AdditionalInformation.ONE_BYTE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$AdditionalInformation[AdditionalInformation.TWO_BYTES.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$AdditionalInformation[AdditionalInformation.FOUR_BYTES.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$AdditionalInformation[AdditionalInformation.EIGHT_BYTES.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$AdditionalInformation[AdditionalInformation.INDEFINITE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$AdditionalInformation[AdditionalInformation.RESERVED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long getLength(int i) throws CborException {
        switch (AnonymousClass1.$SwitchMap$co$nstant$in$cbor$model$AdditionalInformation[AdditionalInformation.ofByte(i).ordinal()]) {
            case 1:
                return i & 31;
            case 2:
                return nextSymbol();
            case 3:
                return (nextSymbol() << 0) | (nextSymbol() << 8) | 0;
            case 4:
                return (nextSymbol() << 0) | (nextSymbol() << 16) | 0 | (nextSymbol() << 24) | (nextSymbol() << 8);
            case 5:
                return (nextSymbol() << 0) | (nextSymbol() << 16) | 0 | (nextSymbol() << 56) | (nextSymbol() << 48) | (nextSymbol() << 40) | (nextSymbol() << 32) | (nextSymbol() << 24) | (nextSymbol() << 8);
            case 6:
                return -1L;
            default:
                throw new CborException("Reserved additional information");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BigInteger getLengthAsBigInteger(int i) throws CborException {
        switch (AnonymousClass1.$SwitchMap$co$nstant$in$cbor$model$AdditionalInformation[AdditionalInformation.ofByte(i).ordinal()]) {
            case 1:
                return BigInteger.valueOf(i & 31);
            case 2:
                return BigInteger.valueOf(nextSymbol());
            case 3:
                return BigInteger.valueOf((nextSymbol() << 0) | (nextSymbol() << 8) | 0);
            case 4:
                return BigInteger.valueOf((nextSymbol() << 0) | (nextSymbol() << 16) | (nextSymbol() << 24) | 0 | (nextSymbol() << 8));
            case 5:
                return BigInteger.ZERO.or(BigInteger.valueOf(nextSymbol()).shiftLeft(56)).or(BigInteger.valueOf(nextSymbol()).shiftLeft(48)).or(BigInteger.valueOf(nextSymbol()).shiftLeft(40)).or(BigInteger.valueOf(nextSymbol()).shiftLeft(32)).or(BigInteger.valueOf(nextSymbol()).shiftLeft(24)).or(BigInteger.valueOf(nextSymbol()).shiftLeft(16)).or(BigInteger.valueOf(nextSymbol()).shiftLeft(8)).or(BigInteger.valueOf(nextSymbol()).shiftLeft(0));
            case 6:
                return BigInteger.valueOf(-1L);
            default:
                throw new CborException("Reserved additional information");
        }
    }
}
