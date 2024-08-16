package qc;

import java.util.List;

/* compiled from: UninitializedMessageException.java */
/* loaded from: classes2.dex */
public class w extends RuntimeException {

    /* renamed from: e, reason: collision with root package name */
    private final List<String> f17368e;

    public w(q qVar) {
        super("Message was missing required fields.  (Lite runtime could not determine which fields were missing).");
        this.f17368e = null;
    }

    public k a() {
        return new k(getMessage());
    }
}
