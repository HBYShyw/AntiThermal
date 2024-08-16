package d1;

/* compiled from: CertType.java */
/* renamed from: d1.b, reason: use source file name */
/* loaded from: classes.dex */
public enum CertType {
    ROOT_CA_CERT_LABEL(1),
    DEVICE_CA_CERT_LABEL(2),
    SERVICE_CA_CERT_LABEL(3),
    DEVICE_EE_CERT_LABEL(4),
    ALL_CA_CERT_LABEL(5);


    /* renamed from: e, reason: collision with root package name */
    private final int f10681e;

    CertType(int i10) {
        this.f10681e = i10;
    }

    public int a() {
        return this.f10681e;
    }
}
