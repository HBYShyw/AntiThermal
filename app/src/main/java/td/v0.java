package td;

import kotlin.Metadata;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: JobSupport.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0005\u001a\u00020\u0004¢\u0006\u0004\b\r\u0010\u000eJ\b\u0010\u0003\u001a\u00020\u0002H\u0016R\u001a\u0010\u0005\u001a\u00020\u00048\u0016X\u0096\u0004¢\u0006\f\n\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\bR\u0016\u0010\f\u001a\u0004\u0018\u00010\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b¨\u0006\u000f"}, d2 = {"Ltd/v0;", "Ltd/d1;", "", "toString", "", "isActive", "Z", "b", "()Z", "Ltd/t1;", "f", "()Ltd/t1;", "list", "<init>", "(Z)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class v0 implements d1 {

    /* renamed from: e, reason: collision with root package name */
    private final boolean f18801e;

    public v0(boolean z10) {
        this.f18801e = z10;
    }

    @Override // td.d1
    /* renamed from: b, reason: from getter */
    public boolean getF18801e() {
        return this.f18801e;
    }

    @Override // td.d1
    /* renamed from: f */
    public t1 getF18726e() {
        return null;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Empty{");
        sb2.append(getF18801e() ? "Active" : "New");
        sb2.append('}');
        return sb2.toString();
    }
}
