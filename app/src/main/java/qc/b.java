package qc;

import java.io.IOException;
import java.io.InputStream;
import qc.a;
import qc.q;

/* compiled from: AbstractParser.java */
/* loaded from: classes2.dex */
public abstract class b<MessageType extends q> implements s<MessageType> {

    /* renamed from: a, reason: collision with root package name */
    private static final g f17253a = g.c();

    private MessageType e(MessageType messagetype) {
        if (messagetype == null || messagetype.isInitialized()) {
            return messagetype;
        }
        throw f(messagetype).a().i(messagetype);
    }

    private w f(MessageType messagetype) {
        if (messagetype instanceof a) {
            return ((a) messagetype).b();
        }
        return new w(messagetype);
    }

    @Override // qc.s
    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public MessageType b(InputStream inputStream, g gVar) {
        return e(j(inputStream, gVar));
    }

    @Override // qc.s
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public MessageType c(InputStream inputStream, g gVar) {
        return e(k(inputStream, gVar));
    }

    @Override // qc.s
    /* renamed from: i, reason: merged with bridge method [inline-methods] */
    public MessageType d(d dVar, g gVar) {
        return e(l(dVar, gVar));
    }

    public MessageType j(InputStream inputStream, g gVar) {
        try {
            int read = inputStream.read();
            if (read == -1) {
                return null;
            }
            return k(new a.AbstractC0092a.C0093a(inputStream, e.B(read, inputStream)), gVar);
        } catch (IOException e10) {
            throw new k(e10.getMessage());
        }
    }

    public MessageType k(InputStream inputStream, g gVar) {
        e g6 = e.g(inputStream);
        MessageType messagetype = (MessageType) a(g6, gVar);
        try {
            g6.a(0);
            return messagetype;
        } catch (k e10) {
            throw e10.i(messagetype);
        }
    }

    public MessageType l(d dVar, g gVar) {
        try {
            e p10 = dVar.p();
            MessageType messagetype = (MessageType) a(p10, gVar);
            try {
                p10.a(0);
                return messagetype;
            } catch (k e10) {
                throw e10.i(messagetype);
            }
        } catch (k e11) {
            throw e11;
        }
    }
}
