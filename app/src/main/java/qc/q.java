package qc;

/* compiled from: MessageLite.java */
/* loaded from: classes2.dex */
public interface q extends r {

    /* compiled from: MessageLite.java */
    /* loaded from: classes2.dex */
    public interface a extends Cloneable, r {
        q build();

        a m(e eVar, g gVar);
    }

    void a(f fVar);

    s<? extends q> getParserForType();

    int getSerializedSize();

    a newBuilderForType();

    a toBuilder();
}
