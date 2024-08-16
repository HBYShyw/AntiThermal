package pb;

/* compiled from: ClassKind.kt */
/* renamed from: pb.f, reason: use source file name */
/* loaded from: classes2.dex */
public enum ClassKind {
    CLASS("class"),
    INTERFACE("interface"),
    ENUM_CLASS("enum class"),
    ENUM_ENTRY(null),
    ANNOTATION_CLASS("annotation class"),
    OBJECT("object");


    /* renamed from: e, reason: collision with root package name */
    private final String f16689e;

    ClassKind(String str) {
        this.f16689e = str;
    }

    public final boolean b() {
        return this == OBJECT || this == ENUM_ENTRY;
    }
}
