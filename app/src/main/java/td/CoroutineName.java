package td;

import kotlin.Metadata;
import qa.g;
import za.DefaultConstructorMarker;

/* compiled from: CoroutineName.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0086\b\u0018\u00002\u00020\u0001:\u0001\rB\u000f\u0012\u0006\u0010\n\u001a\u00020\u0002¢\u0006\u0004\b\u000b\u0010\fJ\b\u0010\u0003\u001a\u00020\u0002H\u0016J\t\u0010\u0005\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\t\u001a\u00020\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006HÖ\u0003¨\u0006\u000e"}, d2 = {"Ltd/g0;", "Lqa/a;", "", "toString", "", "hashCode", "", "other", "", "equals", "name", "<init>", "(Ljava/lang/String;)V", "a", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: td.g0, reason: use source file name and from toString */
/* loaded from: classes2.dex */
public final /* data */ class CoroutineName extends qa.a {

    /* renamed from: g, reason: collision with root package name */
    public static final a f18739g = new a(null);

    /* renamed from: f, reason: collision with root package name */
    private final String f18740f;

    /* compiled from: CoroutineName.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0003\u0010\u0004¨\u0006\u0005"}, d2 = {"Ltd/g0$a;", "Lqa/g$c;", "Ltd/g0;", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* renamed from: td.g0$a */
    /* loaded from: classes2.dex */
    public static final class a implements g.c<CoroutineName> {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public CoroutineName(String str) {
        super(f18739g);
        this.f18740f = str;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof CoroutineName) && za.k.a(this.f18740f, ((CoroutineName) other).f18740f);
    }

    public int hashCode() {
        return this.f18740f.hashCode();
    }

    public String toString() {
        return "CoroutineName(" + this.f18740f + ')';
    }
}
