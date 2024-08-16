package co.nstant.in.cbor.decoder;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.Special;
import co.nstant.in.cbor.model.SpecialType;
import java.io.InputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SpecialDecoder extends AbstractDecoder<Special> {
    private final DoublePrecisionFloatDecoder doublePrecisionFloatDecoder;
    private final HalfPrecisionFloatDecoder halfPrecisionFloatDecoder;
    private final SinglePrecisionFloatDecoder singlePrecisionFloatDecoder;

    public SpecialDecoder(CborDecoder cborDecoder, InputStream inputStream) {
        super(cborDecoder, inputStream);
        this.halfPrecisionFloatDecoder = new HalfPrecisionFloatDecoder(cborDecoder, inputStream);
        this.singlePrecisionFloatDecoder = new SinglePrecisionFloatDecoder(cborDecoder, inputStream);
        this.doublePrecisionFloatDecoder = new DoublePrecisionFloatDecoder(cborDecoder, inputStream);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: co.nstant.in.cbor.decoder.SpecialDecoder$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$co$nstant$in$cbor$model$SimpleValueType;
        static final /* synthetic */ int[] $SwitchMap$co$nstant$in$cbor$model$SpecialType;

        static {
            int[] iArr = new int[SpecialType.values().length];
            $SwitchMap$co$nstant$in$cbor$model$SpecialType = iArr;
            try {
                iArr[SpecialType.BREAK.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SpecialType[SpecialType.SIMPLE_VALUE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SpecialType[SpecialType.IEEE_754_HALF_PRECISION_FLOAT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SpecialType[SpecialType.IEEE_754_SINGLE_PRECISION_FLOAT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SpecialType[SpecialType.IEEE_754_DOUBLE_PRECISION_FLOAT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SpecialType[SpecialType.SIMPLE_VALUE_NEXT_BYTE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SpecialType[SpecialType.UNALLOCATED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            int[] iArr2 = new int[SimpleValueType.values().length];
            $SwitchMap$co$nstant$in$cbor$model$SimpleValueType = iArr2;
            try {
                iArr2[SimpleValueType.FALSE.ordinal()] = 1;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SimpleValueType[SimpleValueType.TRUE.ordinal()] = 2;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SimpleValueType[SimpleValueType.NULL.ordinal()] = 3;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SimpleValueType[SimpleValueType.UNDEFINED.ordinal()] = 4;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SimpleValueType[SimpleValueType.UNALLOCATED.ordinal()] = 5;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SimpleValueType[SimpleValueType.RESERVED.ordinal()] = 6;
            } catch (NoSuchFieldError unused13) {
            }
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public Special decode(int i) throws CborException {
        switch (AnonymousClass1.$SwitchMap$co$nstant$in$cbor$model$SpecialType[SpecialType.ofByte(i).ordinal()]) {
            case 1:
                return Special.BREAK;
            case 2:
                int i2 = AnonymousClass1.$SwitchMap$co$nstant$in$cbor$model$SimpleValueType[SimpleValueType.ofByte(i).ordinal()];
                if (i2 == 1) {
                    return SimpleValue.FALSE;
                }
                if (i2 == 2) {
                    return SimpleValue.TRUE;
                }
                if (i2 == 3) {
                    return SimpleValue.NULL;
                }
                if (i2 == 4) {
                    return SimpleValue.UNDEFINED;
                }
                if (i2 == 5) {
                    return new SimpleValue(i & 31);
                }
                throw new CborException("Not implemented");
            case 3:
                return this.halfPrecisionFloatDecoder.decode(i);
            case 4:
                return this.singlePrecisionFloatDecoder.decode(i);
            case 5:
                return this.doublePrecisionFloatDecoder.decode(i);
            case 6:
                return new SimpleValue(nextSymbol());
            default:
                throw new CborException("Not implemented");
        }
    }
}
