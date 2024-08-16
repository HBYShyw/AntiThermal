package s0;

/* compiled from: CertUpgradeCycleEnum.java */
/* renamed from: s0.c, reason: use source file name */
/* loaded from: classes.dex */
public enum CertUpgradeCycleEnum {
    TWO_WEEKS(1209600),
    THREE_WEEKS(1814400),
    ONE_MONTH(2419200),
    TWO_MONTHS(4838400);


    /* renamed from: e, reason: collision with root package name */
    private final int f17947e;

    CertUpgradeCycleEnum(int i10) {
        this.f17947e = i10;
    }

    public int a() {
        return this.f17947e;
    }
}
