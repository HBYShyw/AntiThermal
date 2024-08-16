package gb;

/* compiled from: KParameter.kt */
/* renamed from: gb.k, reason: use source file name */
/* loaded from: classes2.dex */
public interface KParameter extends KAnnotatedElement {

    /* compiled from: KParameter.kt */
    /* renamed from: gb.k$a */
    /* loaded from: classes2.dex */
    public enum a {
        INSTANCE,
        EXTENSION_RECEIVER,
        VALUE
    }

    boolean a();

    a getKind();

    String getName();

    KType getType();

    int j();

    boolean z();
}
