package cd;

import gd.Variance;
import jc.c;
import jc.q;
import jc.s;
import ma.NoWhenBranchMatchedException;
import pb.ClassKind;
import pb.Modality;

/* compiled from: ProtoEnumFlags.kt */
/* renamed from: cd.a0, reason: use source file name */
/* loaded from: classes2.dex */
public final class ProtoEnumFlags {

    /* renamed from: a, reason: collision with root package name */
    public static final ProtoEnumFlags f5188a = new ProtoEnumFlags();

    /* compiled from: ProtoEnumFlags.kt */
    /* renamed from: cd.a0$a */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f5189a;

        /* renamed from: b, reason: collision with root package name */
        public static final /* synthetic */ int[] f5190b;

        /* renamed from: c, reason: collision with root package name */
        public static final /* synthetic */ int[] f5191c;

        /* renamed from: d, reason: collision with root package name */
        public static final /* synthetic */ int[] f5192d;

        /* renamed from: e, reason: collision with root package name */
        public static final /* synthetic */ int[] f5193e;

        /* renamed from: f, reason: collision with root package name */
        public static final /* synthetic */ int[] f5194f;

        /* renamed from: g, reason: collision with root package name */
        public static final /* synthetic */ int[] f5195g;

        /* renamed from: h, reason: collision with root package name */
        public static final /* synthetic */ int[] f5196h;

        static {
            int[] iArr = new int[jc.k.values().length];
            try {
                iArr[jc.k.FINAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[jc.k.OPEN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[jc.k.ABSTRACT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[jc.k.SEALED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            f5189a = iArr;
            int[] iArr2 = new int[Modality.values().length];
            try {
                iArr2[Modality.FINAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr2[Modality.OPEN.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr2[Modality.ABSTRACT.ordinal()] = 3;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                iArr2[Modality.SEALED.ordinal()] = 4;
            } catch (NoSuchFieldError unused8) {
            }
            f5190b = iArr2;
            int[] iArr3 = new int[jc.x.values().length];
            try {
                iArr3[jc.x.INTERNAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                iArr3[jc.x.PRIVATE.ordinal()] = 2;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                iArr3[jc.x.PRIVATE_TO_THIS.ordinal()] = 3;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                iArr3[jc.x.PROTECTED.ordinal()] = 4;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                iArr3[jc.x.PUBLIC.ordinal()] = 5;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                iArr3[jc.x.LOCAL.ordinal()] = 6;
            } catch (NoSuchFieldError unused14) {
            }
            f5191c = iArr3;
            int[] iArr4 = new int[c.EnumC0065c.values().length];
            try {
                iArr4[c.EnumC0065c.CLASS.ordinal()] = 1;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                iArr4[c.EnumC0065c.INTERFACE.ordinal()] = 2;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                iArr4[c.EnumC0065c.ENUM_CLASS.ordinal()] = 3;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                iArr4[c.EnumC0065c.ENUM_ENTRY.ordinal()] = 4;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                iArr4[c.EnumC0065c.ANNOTATION_CLASS.ordinal()] = 5;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                iArr4[c.EnumC0065c.OBJECT.ordinal()] = 6;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                iArr4[c.EnumC0065c.COMPANION_OBJECT.ordinal()] = 7;
            } catch (NoSuchFieldError unused21) {
            }
            f5192d = iArr4;
            int[] iArr5 = new int[ClassKind.values().length];
            try {
                iArr5[ClassKind.CLASS.ordinal()] = 1;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                iArr5[ClassKind.INTERFACE.ordinal()] = 2;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                iArr5[ClassKind.ENUM_CLASS.ordinal()] = 3;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                iArr5[ClassKind.ENUM_ENTRY.ordinal()] = 4;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                iArr5[ClassKind.ANNOTATION_CLASS.ordinal()] = 5;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                iArr5[ClassKind.OBJECT.ordinal()] = 6;
            } catch (NoSuchFieldError unused27) {
            }
            f5193e = iArr5;
            int[] iArr6 = new int[s.c.values().length];
            try {
                iArr6[s.c.IN.ordinal()] = 1;
            } catch (NoSuchFieldError unused28) {
            }
            try {
                iArr6[s.c.OUT.ordinal()] = 2;
            } catch (NoSuchFieldError unused29) {
            }
            try {
                iArr6[s.c.INV.ordinal()] = 3;
            } catch (NoSuchFieldError unused30) {
            }
            f5194f = iArr6;
            int[] iArr7 = new int[q.b.c.values().length];
            try {
                iArr7[q.b.c.IN.ordinal()] = 1;
            } catch (NoSuchFieldError unused31) {
            }
            try {
                iArr7[q.b.c.OUT.ordinal()] = 2;
            } catch (NoSuchFieldError unused32) {
            }
            try {
                iArr7[q.b.c.INV.ordinal()] = 3;
            } catch (NoSuchFieldError unused33) {
            }
            try {
                iArr7[q.b.c.STAR.ordinal()] = 4;
            } catch (NoSuchFieldError unused34) {
            }
            f5195g = iArr7;
            int[] iArr8 = new int[Variance.values().length];
            try {
                iArr8[Variance.IN_VARIANCE.ordinal()] = 1;
            } catch (NoSuchFieldError unused35) {
            }
            try {
                iArr8[Variance.OUT_VARIANCE.ordinal()] = 2;
            } catch (NoSuchFieldError unused36) {
            }
            try {
                iArr8[Variance.INVARIANT.ordinal()] = 3;
            } catch (NoSuchFieldError unused37) {
            }
            f5196h = iArr8;
        }
    }

    private ProtoEnumFlags() {
    }

    public final ClassKind a(c.EnumC0065c enumC0065c) {
        switch (enumC0065c == null ? -1 : a.f5192d[enumC0065c.ordinal()]) {
            case 1:
                return ClassKind.CLASS;
            case 2:
                return ClassKind.INTERFACE;
            case 3:
                return ClassKind.ENUM_CLASS;
            case 4:
                return ClassKind.ENUM_ENTRY;
            case 5:
                return ClassKind.ANNOTATION_CLASS;
            case 6:
            case 7:
                return ClassKind.OBJECT;
            default:
                return ClassKind.CLASS;
        }
    }

    public final Modality b(jc.k kVar) {
        int i10 = kVar == null ? -1 : a.f5189a[kVar.ordinal()];
        if (i10 == 1) {
            return Modality.FINAL;
        }
        if (i10 == 2) {
            return Modality.OPEN;
        }
        if (i10 == 3) {
            return Modality.ABSTRACT;
        }
        if (i10 != 4) {
            return Modality.FINAL;
        }
        return Modality.SEALED;
    }

    public final Variance c(q.b.c cVar) {
        za.k.e(cVar, "projection");
        int i10 = a.f5195g[cVar.ordinal()];
        if (i10 == 1) {
            return Variance.IN_VARIANCE;
        }
        if (i10 == 2) {
            return Variance.OUT_VARIANCE;
        }
        if (i10 == 3) {
            return Variance.INVARIANT;
        }
        if (i10 != 4) {
            throw new NoWhenBranchMatchedException();
        }
        throw new IllegalArgumentException("Only IN, OUT and INV are supported. Actual argument: " + cVar);
    }

    public final Variance d(s.c cVar) {
        za.k.e(cVar, "variance");
        int i10 = a.f5194f[cVar.ordinal()];
        if (i10 == 1) {
            return Variance.IN_VARIANCE;
        }
        if (i10 == 2) {
            return Variance.OUT_VARIANCE;
        }
        if (i10 == 3) {
            return Variance.INVARIANT;
        }
        throw new NoWhenBranchMatchedException();
    }
}
