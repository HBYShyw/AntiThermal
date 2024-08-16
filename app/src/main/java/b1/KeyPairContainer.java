package b1;

import e1.DateUtil;
import java.security.KeyPair;
import java.util.Date;

/* compiled from: KeyPairContainer.java */
/* renamed from: b1.b, reason: use source file name */
/* loaded from: classes.dex */
public class KeyPairContainer {

    /* renamed from: a, reason: collision with root package name */
    private final String f4538a;

    /* renamed from: b, reason: collision with root package name */
    private final KeyPair f4539b;

    /* renamed from: c, reason: collision with root package name */
    private final Date f4540c;

    /* renamed from: d, reason: collision with root package name */
    private String f4541d = null;

    public KeyPairContainer(String str, KeyPair keyPair, Date date) {
        this.f4538a = str;
        this.f4539b = keyPair;
        this.f4540c = date;
    }

    public KeyPair a() {
        return this.f4539b;
    }

    public boolean b() {
        return this.f4540c != null && DateUtil.a() > this.f4540c.getTime();
    }

    public void c(String str) {
        this.f4541d = str;
    }
}
