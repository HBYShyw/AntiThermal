package s0;

import java.util.Objects;

/* compiled from: BizPublicKeyConfig.java */
/* renamed from: s0.a, reason: use source file name */
/* loaded from: classes.dex */
public class BizPublicKeyConfig {

    /* renamed from: a, reason: collision with root package name */
    private String f17909a;

    /* renamed from: b, reason: collision with root package name */
    private String f17910b;

    /* renamed from: c, reason: collision with root package name */
    private long f17911c;

    public String a() {
        return this.f17910b;
    }

    public long b() {
        return this.f17911c;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BizPublicKeyConfig bizPublicKeyConfig = (BizPublicKeyConfig) obj;
        return this.f17911c == bizPublicKeyConfig.f17911c && Objects.equals(this.f17909a, bizPublicKeyConfig.f17909a) && Objects.equals(this.f17910b, bizPublicKeyConfig.f17910b);
    }

    public int hashCode() {
        return Objects.hash(this.f17909a, this.f17910b, Long.valueOf(this.f17911c));
    }
}
