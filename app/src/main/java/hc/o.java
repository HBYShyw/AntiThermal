package hc;

import hc.m;
import ma.NoWhenBranchMatchedException;
import mb.PrimitiveType;
import xc.JvmClassName;
import xc.JvmPrimitiveType;

/* compiled from: methodSignatureMapping.kt */
/* loaded from: classes2.dex */
final class o implements n<m> {

    /* renamed from: a, reason: collision with root package name */
    public static final o f12196a = new o();

    /* compiled from: methodSignatureMapping.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f12197a;

        static {
            int[] iArr = new int[PrimitiveType.values().length];
            try {
                iArr[PrimitiveType.BOOLEAN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[PrimitiveType.CHAR.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[PrimitiveType.BYTE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[PrimitiveType.SHORT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[PrimitiveType.INT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr[PrimitiveType.FLOAT.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr[PrimitiveType.LONG.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                iArr[PrimitiveType.DOUBLE.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            f12197a = iArr;
        }
    }

    private o() {
    }

    @Override // hc.n
    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public m e(m mVar) {
        za.k.e(mVar, "possiblyPrimitiveType");
        if (!(mVar instanceof m.d)) {
            return mVar;
        }
        m.d dVar = (m.d) mVar;
        if (dVar.i() == null) {
            return mVar;
        }
        String f10 = JvmClassName.c(dVar.i().g()).f();
        za.k.d(f10, "byFqNameWithoutInnerClas…apperFqName).internalName");
        return d(f10);
    }

    @Override // hc.n
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public m c(String str) {
        JvmPrimitiveType jvmPrimitiveType;
        za.k.e(str, "representation");
        str.length();
        char charAt = str.charAt(0);
        JvmPrimitiveType[] values = JvmPrimitiveType.values();
        int length = values.length;
        int i10 = 0;
        while (true) {
            if (i10 >= length) {
                jvmPrimitiveType = null;
                break;
            }
            jvmPrimitiveType = values[i10];
            if (jvmPrimitiveType.d().charAt(0) == charAt) {
                break;
            }
            i10++;
        }
        if (jvmPrimitiveType != null) {
            return new m.d(jvmPrimitiveType);
        }
        if (charAt == 'V') {
            return new m.d(null);
        }
        if (charAt == '[') {
            String substring = str.substring(1);
            za.k.d(substring, "this as java.lang.String).substring(startIndex)");
            return new m.a(c(substring));
        }
        if (charAt == 'L') {
            sd.v.L(str, ';', false, 2, null);
        }
        String substring2 = str.substring(1, str.length() - 1);
        za.k.d(substring2, "this as java.lang.String…ing(startIndex, endIndex)");
        return new m.c(substring2);
    }

    @Override // hc.n
    /* renamed from: i, reason: merged with bridge method [inline-methods] */
    public m.c d(String str) {
        za.k.e(str, "internalName");
        return new m.c(str);
    }

    @Override // hc.n
    /* renamed from: j, reason: merged with bridge method [inline-methods] */
    public m b(PrimitiveType primitiveType) {
        za.k.e(primitiveType, "primitiveType");
        switch (a.f12197a[primitiveType.ordinal()]) {
            case 1:
                return m.f12184a.a();
            case 2:
                return m.f12184a.c();
            case 3:
                return m.f12184a.b();
            case 4:
                return m.f12184a.h();
            case 5:
                return m.f12184a.f();
            case 6:
                return m.f12184a.e();
            case 7:
                return m.f12184a.g();
            case 8:
                return m.f12184a.d();
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    @Override // hc.n
    /* renamed from: k, reason: merged with bridge method [inline-methods] */
    public m f() {
        return d("java/lang/Class");
    }

    @Override // hc.n
    /* renamed from: l, reason: merged with bridge method [inline-methods] */
    public String a(m mVar) {
        String d10;
        za.k.e(mVar, "type");
        if (mVar instanceof m.a) {
            return '[' + a(((m.a) mVar).i());
        }
        if (mVar instanceof m.d) {
            JvmPrimitiveType i10 = ((m.d) mVar).i();
            return (i10 == null || (d10 = i10.d()) == null) ? "V" : d10;
        }
        if (!(mVar instanceof m.c)) {
            throw new NoWhenBranchMatchedException();
        }
        return 'L' + ((m.c) mVar).i() + ';';
    }
}
