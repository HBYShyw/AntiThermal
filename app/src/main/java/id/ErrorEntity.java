package id;

/* compiled from: ErrorEntity.kt */
/* renamed from: id.b, reason: use source file name */
/* loaded from: classes2.dex */
public enum ErrorEntity {
    ERROR_CLASS("<Error class: %s>"),
    ERROR_FUNCTION("<Error function>"),
    ERROR_SCOPE("<Error scope>"),
    ERROR_MODULE("<Error module>"),
    ERROR_PROPERTY("<Error property>"),
    ERROR_TYPE("[Error type: %s]"),
    PARENT_OF_ERROR_SCOPE("<Fake parent for error lexical scope>");


    /* renamed from: e, reason: collision with root package name */
    private final String f12752e;

    ErrorEntity(String str) {
        this.f12752e = str;
    }

    public final String b() {
        return this.f12752e;
    }
}
