package z0;

/* compiled from: HandshakePattern.java */
/* renamed from: z0.c, reason: use source file name */
/* loaded from: classes.dex */
public class HandshakePattern {

    /* renamed from: a, reason: collision with root package name */
    private static final short[] f20171a = {-1, -1, 2, 255, 2, 3};

    /* renamed from: b, reason: collision with root package name */
    private static final short[] f20172b = {-1, 1, 2, 4, 255, 2, 3};

    /* renamed from: c, reason: collision with root package name */
    private static final short[] f20173c = {1, 1, 2, 4, 6, 255, 2, 3, 5};

    /* renamed from: d, reason: collision with root package name */
    private static final short[] f20174d = {-1, 1, 2, 4, 1, 6, 255, 2, 3, 5};

    /* renamed from: e, reason: collision with root package name */
    private static final short[] f20175e = {-1, -1, 2, 1, 255, 2, 3, 5, 1, 4};

    /* renamed from: f, reason: collision with root package name */
    private static final short[] f20176f = {-1, -1, 15, 2, 255, 2, 3};

    /* compiled from: HandshakePattern.java */
    /* renamed from: z0.c$a */
    /* loaded from: classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f20177a;

        static {
            int[] iArr = new int[NoiseHandshakeEnum.values().length];
            f20177a = iArr;
            try {
                iArr[NoiseHandshakeEnum.NN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f20177a[NoiseHandshakeEnum.NK.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f20177a[NoiseHandshakeEnum.KK.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f20177a[NoiseHandshakeEnum.IK.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f20177a[NoiseHandshakeEnum.IX.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f20177a[NoiseHandshakeEnum.NNpsk0.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    public static boolean a(NoiseHandshakeEnum noiseHandshakeEnum) {
        return noiseHandshakeEnum.name().contains("psk");
    }

    public static short[] b(NoiseHandshakeEnum noiseHandshakeEnum) {
        switch (a.f20177a[noiseHandshakeEnum.ordinal()]) {
            case 1:
                return f20171a;
            case 2:
                return f20172b;
            case 3:
                return f20173c;
            case 4:
                return f20174d;
            case 5:
                return f20175e;
            case 6:
                return f20176f;
            default:
                return null;
        }
    }
}
