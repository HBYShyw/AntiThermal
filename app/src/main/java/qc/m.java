package qc;

import java.io.IOException;

/* compiled from: LazyFieldLite.java */
/* loaded from: classes2.dex */
public class m {

    /* renamed from: a, reason: collision with root package name */
    private d f17321a;

    /* renamed from: b, reason: collision with root package name */
    private g f17322b;

    /* renamed from: c, reason: collision with root package name */
    private volatile boolean f17323c;

    /* renamed from: d, reason: collision with root package name */
    protected volatile q f17324d;

    protected void a(q qVar) {
        if (this.f17324d != null) {
            return;
        }
        synchronized (this) {
            if (this.f17324d != null) {
                return;
            }
            try {
                if (this.f17321a != null) {
                    this.f17324d = qVar.getParserForType().d(this.f17321a, this.f17322b);
                } else {
                    this.f17324d = qVar;
                }
            } catch (IOException unused) {
            }
        }
    }

    public int b() {
        if (this.f17323c) {
            return this.f17324d.getSerializedSize();
        }
        return this.f17321a.size();
    }

    public q c(q qVar) {
        a(qVar);
        return this.f17324d;
    }

    public q d(q qVar) {
        q qVar2 = this.f17324d;
        this.f17324d = qVar;
        this.f17321a = null;
        this.f17323c = true;
        return qVar2;
    }
}
