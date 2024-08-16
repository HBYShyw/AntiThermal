package q0;

import s0.CryptoParameters;
import s0.EncryptEnum;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'h' uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:451)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByRegister(EnumVisitor.java:395)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:324)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:262)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:151)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* compiled from: DigitalEnvelopeCipherEnum.java */
/* renamed from: q0.a, reason: use source file name */
/* loaded from: classes.dex */
public final class DigitalEnvelopeCipherEnum {

    /* renamed from: h, reason: collision with root package name */
    public static final DigitalEnvelopeCipherEnum f16760h;

    /* renamed from: i, reason: collision with root package name */
    public static final DigitalEnvelopeCipherEnum f16761i;

    /* renamed from: j, reason: collision with root package name */
    public static final DigitalEnvelopeCipherEnum f16762j;

    /* renamed from: k, reason: collision with root package name */
    public static final DigitalEnvelopeCipherEnum f16763k;

    /* renamed from: l, reason: collision with root package name */
    public static final DigitalEnvelopeCipherEnum f16764l;

    /* renamed from: m, reason: collision with root package name */
    public static final DigitalEnvelopeCipherEnum f16765m;

    /* renamed from: n, reason: collision with root package name */
    private static final /* synthetic */ DigitalEnvelopeCipherEnum[] f16766n;

    /* renamed from: e, reason: collision with root package name */
    private final int f16767e;

    /* renamed from: f, reason: collision with root package name */
    private final CryptoParameters.b f16768f;

    /* renamed from: g, reason: collision with root package name */
    private final EncryptEnum f16769g;

    static {
        CryptoParameters.b bVar = CryptoParameters.b.f17973e;
        EncryptEnum encryptEnum = EncryptEnum.AES;
        DigitalEnvelopeCipherEnum digitalEnvelopeCipherEnum = new DigitalEnvelopeCipherEnum("AES128GCM", 0, 16, bVar, encryptEnum);
        f16760h = digitalEnvelopeCipherEnum;
        DigitalEnvelopeCipherEnum digitalEnvelopeCipherEnum2 = new DigitalEnvelopeCipherEnum("AES256GCM", 1, 32, bVar, encryptEnum);
        f16761i = digitalEnvelopeCipherEnum2;
        CryptoParameters.b bVar2 = CryptoParameters.b.f17974f;
        DigitalEnvelopeCipherEnum digitalEnvelopeCipherEnum3 = new DigitalEnvelopeCipherEnum("AES128CTR", 2, 16, bVar2, encryptEnum);
        f16762j = digitalEnvelopeCipherEnum3;
        DigitalEnvelopeCipherEnum digitalEnvelopeCipherEnum4 = new DigitalEnvelopeCipherEnum("AES256CTR", 3, 32, bVar2, encryptEnum);
        f16763k = digitalEnvelopeCipherEnum4;
        CryptoParameters.b bVar3 = CryptoParameters.b.f17975g;
        DigitalEnvelopeCipherEnum digitalEnvelopeCipherEnum5 = new DigitalEnvelopeCipherEnum("AES128CBC", 4, 16, bVar3, encryptEnum);
        f16764l = digitalEnvelopeCipherEnum5;
        DigitalEnvelopeCipherEnum digitalEnvelopeCipherEnum6 = new DigitalEnvelopeCipherEnum("AES256CBC", 5, 32, bVar3, encryptEnum);
        f16765m = digitalEnvelopeCipherEnum6;
        f16766n = new DigitalEnvelopeCipherEnum[]{digitalEnvelopeCipherEnum, digitalEnvelopeCipherEnum2, digitalEnvelopeCipherEnum3, digitalEnvelopeCipherEnum4, digitalEnvelopeCipherEnum5, digitalEnvelopeCipherEnum6};
    }

    private DigitalEnvelopeCipherEnum(String str, int i10, int i11, CryptoParameters.b bVar, EncryptEnum encryptEnum) {
        this.f16767e = i11;
        this.f16768f = bVar;
        this.f16769g = encryptEnum;
    }

    public static DigitalEnvelopeCipherEnum valueOf(String str) {
        return (DigitalEnvelopeCipherEnum) Enum.valueOf(DigitalEnvelopeCipherEnum.class, str);
    }

    public static DigitalEnvelopeCipherEnum[] values() {
        return (DigitalEnvelopeCipherEnum[]) f16766n.clone();
    }

    public CryptoParameters.b a() {
        return this.f16768f;
    }

    public int b() {
        return this.f16767e;
    }

    public EncryptEnum c() {
        return this.f16769g;
    }
}
