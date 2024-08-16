package co.nstant.in.cbor.encoder;

import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DoublePrecisionFloat;
import co.nstant.in.cbor.model.HalfPrecisionFloat;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.SinglePrecisionFloat;
import co.nstant.in.cbor.model.Special;
import co.nstant.in.cbor.model.SpecialType;
import com.android.internal.util.FrameworkStatsLog;
import java.io.OutputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SpecialEncoder extends AbstractEncoder<Special> {
    private final DoublePrecisionFloatEncoder doublePrecisionFloatEncoder;
    private final HalfPrecisionFloatEncoder halfPrecisionFloatEncoder;
    private final SinglePrecisionFloatEncoder singlePrecisionFloatEncoder;

    public SpecialEncoder(CborEncoder cborEncoder, OutputStream outputStream) {
        super(cborEncoder, outputStream);
        this.halfPrecisionFloatEncoder = new HalfPrecisionFloatEncoder(cborEncoder, outputStream);
        this.singlePrecisionFloatEncoder = new SinglePrecisionFloatEncoder(cborEncoder, outputStream);
        this.doublePrecisionFloatEncoder = new DoublePrecisionFloatEncoder(cborEncoder, outputStream);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: co.nstant.in.cbor.encoder.SpecialEncoder$1, reason: invalid class name */
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
                $SwitchMap$co$nstant$in$cbor$model$SpecialType[SpecialType.UNALLOCATED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SpecialType[SpecialType.IEEE_754_HALF_PRECISION_FLOAT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SpecialType[SpecialType.IEEE_754_SINGLE_PRECISION_FLOAT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SpecialType[SpecialType.IEEE_754_DOUBLE_PRECISION_FLOAT.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SpecialType[SpecialType.SIMPLE_VALUE_NEXT_BYTE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            int[] iArr2 = new int[SimpleValueType.values().length];
            $SwitchMap$co$nstant$in$cbor$model$SimpleValueType = iArr2;
            try {
                iArr2[SimpleValueType.FALSE.ordinal()] = 1;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SimpleValueType[SimpleValueType.NULL.ordinal()] = 2;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$co$nstant$in$cbor$model$SimpleValueType[SimpleValueType.TRUE.ordinal()] = 3;
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

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(Special special) throws CborException {
        switch (AnonymousClass1.$SwitchMap$co$nstant$in$cbor$model$SpecialType[special.getSpecialType().ordinal()]) {
            case 1:
                write(255);
                return;
            case 2:
                SimpleValue simpleValue = (SimpleValue) special;
                int i = AnonymousClass1.$SwitchMap$co$nstant$in$cbor$model$SimpleValueType[simpleValue.getSimpleValueType().ordinal()];
                if (i == 1 || i == 2 || i == 3 || i == 4) {
                    write(simpleValue.getSimpleValueType().getValue() | 224);
                    return;
                } else {
                    if (i != 5) {
                        return;
                    }
                    write(simpleValue.getValue() | 224);
                    return;
                }
            case 3:
                throw new CborException("Unallocated special type");
            case 4:
                if (!(special instanceof HalfPrecisionFloat)) {
                    throw new CborException("Wrong data item type");
                }
                this.halfPrecisionFloatEncoder.encode((HalfPrecisionFloat) special);
                return;
            case 5:
                if (!(special instanceof SinglePrecisionFloat)) {
                    throw new CborException("Wrong data item type");
                }
                this.singlePrecisionFloatEncoder.encode((SinglePrecisionFloat) special);
                return;
            case 6:
                if (!(special instanceof DoublePrecisionFloat)) {
                    throw new CborException("Wrong data item type");
                }
                this.doublePrecisionFloatEncoder.encode((DoublePrecisionFloat) special);
                return;
            case 7:
                if (!(special instanceof SimpleValue)) {
                    throw new CborException("Wrong data item type");
                }
                write(FrameworkStatsLog.INTEGRITY_RULES_PUSHED);
                write(((SimpleValue) special).getValue());
                return;
            default:
                return;
        }
    }
}
