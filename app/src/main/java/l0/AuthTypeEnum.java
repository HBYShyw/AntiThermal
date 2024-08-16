package l0;

/* compiled from: AuthTypeEnum.java */
/* renamed from: l0.a, reason: use source file name */
/* loaded from: classes.dex */
public enum AuthTypeEnum {
    DEVICE_CERT(1),
    PSK(2),
    SIMPLE(3),
    REGISTER_PUB(4);


    /* renamed from: e, reason: collision with root package name */
    private final int f14572e;

    AuthTypeEnum(int i10) {
        this.f14572e = i10;
    }

    public int a() {
        return this.f14572e;
    }
}
