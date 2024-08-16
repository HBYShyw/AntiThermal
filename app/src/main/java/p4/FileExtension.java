package p4;

/* compiled from: FileExtension.java */
/* renamed from: p4.f, reason: use source file name */
/* loaded from: classes.dex */
public enum FileExtension {
    JSON(".json"),
    ZIP(".zip");


    /* renamed from: e, reason: collision with root package name */
    public final String f16582e;

    FileExtension(String str) {
        this.f16582e = str;
    }

    public String a() {
        return ".temp" + this.f16582e;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.f16582e;
    }
}
