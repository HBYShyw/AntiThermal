package s0;

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
/* compiled from: EncryptAlgorithmEnum.java */
/* renamed from: s0.h, reason: use source file name */
/* loaded from: classes.dex */
public final class EncryptAlgorithmEnum {

    /* renamed from: h, reason: collision with root package name */
    public static final EncryptAlgorithmEnum f17984h;

    /* renamed from: i, reason: collision with root package name */
    public static final EncryptAlgorithmEnum f17985i;

    /* renamed from: j, reason: collision with root package name */
    public static final EncryptAlgorithmEnum f17986j;

    /* renamed from: k, reason: collision with root package name */
    public static final EncryptAlgorithmEnum f17987k;

    /* renamed from: l, reason: collision with root package name */
    public static final EncryptAlgorithmEnum f17988l;

    /* renamed from: m, reason: collision with root package name */
    public static final EncryptAlgorithmEnum f17989m;

    /* renamed from: n, reason: collision with root package name */
    public static final EncryptAlgorithmEnum f17990n;

    /* renamed from: o, reason: collision with root package name */
    public static final EncryptAlgorithmEnum f17991o;

    /* renamed from: p, reason: collision with root package name */
    public static final EncryptAlgorithmEnum f17992p;

    /* renamed from: q, reason: collision with root package name */
    private static final /* synthetic */ EncryptAlgorithmEnum[] f17993q;

    /* renamed from: e, reason: collision with root package name */
    private final String f17994e;

    /* renamed from: f, reason: collision with root package name */
    private final int f17995f;

    /* renamed from: g, reason: collision with root package name */
    private final EncryptEnum f17996g;

    static {
        EncryptEnum encryptEnum = EncryptEnum.AES;
        EncryptAlgorithmEnum encryptAlgorithmEnum = new EncryptAlgorithmEnum("AES_CTR_NOPADDING_256", 0, "AES/CTR/NoPadding", 256, encryptEnum);
        f17984h = encryptAlgorithmEnum;
        EncryptAlgorithmEnum encryptAlgorithmEnum2 = new EncryptAlgorithmEnum("AES_CTR_NOPADDING_192", 1, "AES/CTR/NoPadding", 192, encryptEnum);
        f17985i = encryptAlgorithmEnum2;
        EncryptAlgorithmEnum encryptAlgorithmEnum3 = new EncryptAlgorithmEnum("AES_CTR_NOPADDING_128", 2, "AES/CTR/NoPadding", 128, encryptEnum);
        f17986j = encryptAlgorithmEnum3;
        EncryptAlgorithmEnum encryptAlgorithmEnum4 = new EncryptAlgorithmEnum("AES_CBC_PKCS5PADDING_256", 3, "AES/CBC/PKCS5Padding", 256, encryptEnum);
        f17987k = encryptAlgorithmEnum4;
        EncryptAlgorithmEnum encryptAlgorithmEnum5 = new EncryptAlgorithmEnum("AES_CBC_PKCS5PADDING_192", 4, "AES/CBC/PKCS5Padding", 192, encryptEnum);
        f17988l = encryptAlgorithmEnum5;
        EncryptAlgorithmEnum encryptAlgorithmEnum6 = new EncryptAlgorithmEnum("AES_CBC_PKCS5PADDING_128", 5, "AES/CBC/PKCS5Padding", 128, encryptEnum);
        f17989m = encryptAlgorithmEnum6;
        EncryptAlgorithmEnum encryptAlgorithmEnum7 = new EncryptAlgorithmEnum("AES_GCM_NOPADDING_256", 6, "AES/GCM/NoPadding", 256, encryptEnum);
        f17990n = encryptAlgorithmEnum7;
        EncryptAlgorithmEnum encryptAlgorithmEnum8 = new EncryptAlgorithmEnum("AES_GCM_NOPADDING_192", 7, "AES/GCM/NoPadding", 192, encryptEnum);
        f17991o = encryptAlgorithmEnum8;
        EncryptAlgorithmEnum encryptAlgorithmEnum9 = new EncryptAlgorithmEnum("AES_GCM_NOPADDING_128", 8, "AES/GCM/NoPadding", 128, encryptEnum);
        f17992p = encryptAlgorithmEnum9;
        f17993q = new EncryptAlgorithmEnum[]{encryptAlgorithmEnum, encryptAlgorithmEnum2, encryptAlgorithmEnum3, encryptAlgorithmEnum4, encryptAlgorithmEnum5, encryptAlgorithmEnum6, encryptAlgorithmEnum7, encryptAlgorithmEnum8, encryptAlgorithmEnum9};
    }

    private EncryptAlgorithmEnum(String str, int i10, String str2, int i11, EncryptEnum encryptEnum) {
        this.f17994e = str2;
        this.f17995f = i11;
        this.f17996g = encryptEnum;
    }

    public static EncryptAlgorithmEnum d(String str) {
        str.hashCode();
        char c10 = 65535;
        switch (str.hashCode()) {
            case -1256555478:
                if (str.equals("AES_GCM_NOPADDING_128")) {
                    c10 = 0;
                    break;
                }
                break;
            case -1256555267:
                if (str.equals("AES_GCM_NOPADDING_192")) {
                    c10 = 1;
                    break;
                }
                break;
            case -1256554426:
                if (str.equals("AES_GCM_NOPADDING_256")) {
                    c10 = 2;
                    break;
                }
                break;
            case 778818362:
                if (str.equals("AES_CTR_NOPADDING_128")) {
                    c10 = 3;
                    break;
                }
                break;
            case 778818573:
                if (str.equals("AES_CTR_NOPADDING_192")) {
                    c10 = 4;
                    break;
                }
                break;
            case 778819414:
                if (str.equals("AES_CTR_NOPADDING_256")) {
                    c10 = 5;
                    break;
                }
                break;
            case 1300101482:
                if (str.equals("AES_CBC_PKCS5PADDING_128")) {
                    c10 = 6;
                    break;
                }
                break;
            case 1300101693:
                if (str.equals("AES_CBC_PKCS5PADDING_192")) {
                    c10 = 7;
                    break;
                }
                break;
            case 1300102534:
                if (str.equals("AES_CBC_PKCS5PADDING_256")) {
                    c10 = '\b';
                    break;
                }
                break;
        }
        switch (c10) {
            case 0:
                return f17992p;
            case 1:
                return f17991o;
            case 2:
                return f17990n;
            case 3:
                return f17986j;
            case 4:
                return f17985i;
            case 5:
                return f17984h;
            case 6:
                return f17989m;
            case 7:
                return f17988l;
            case '\b':
                return f17987k;
            default:
                throw new IllegalStateException("Unexpected value: " + str);
        }
    }

    public static EncryptAlgorithmEnum valueOf(String str) {
        return (EncryptAlgorithmEnum) Enum.valueOf(EncryptAlgorithmEnum.class, str);
    }

    public static EncryptAlgorithmEnum[] values() {
        return (EncryptAlgorithmEnum[]) f17993q.clone();
    }

    public EncryptEnum a() {
        return this.f17996g;
    }

    public int b() {
        return this.f17995f;
    }

    public String c() {
        return this.f17994e;
    }
}
