package t1;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import za.DefaultConstructorMarker;

/* compiled from: BaseCardInstructionAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u001b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u000b¢\u0006\u0004\b\u000e\u0010\u000fR\u001d\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u00028\u0006¢\u0006\f\n\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0006\u0010\u0007R\u001d\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\u00028\u0006¢\u0006\f\n\u0004\b\t\u0010\u0005\u001a\u0004\b\n\u0010\u0007¨\u0006\u0010"}, d2 = {"Lt1/a;", "Lt1/d;", "", "", "animResources", "Ljava/util/List;", "i", "()Ljava/util/List;", "", "animAssets", "h", "", "title", "summary", "<init>", "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class a extends d {

    /* renamed from: h, reason: collision with root package name */
    private final List<Integer> f18515h;

    /* renamed from: i, reason: collision with root package name */
    private final List<String> f18516i;

    public a() {
        this(null, 0 == true ? 1 : 0, 3, 0 == true ? 1 : 0);
    }

    public /* synthetic */ a(String str, String str2, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? "" : str, (i10 & 2) != 0 ? "" : str2);
    }

    public final List<String> h() {
        return this.f18516i;
    }

    public final List<Integer> i() {
        return this.f18515h;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(CharSequence charSequence, CharSequence charSequence2) {
        super(charSequence, charSequence2);
        za.k.e(charSequence, "title");
        za.k.e(charSequence2, "summary");
        this.f18515h = new ArrayList();
        this.f18516i = new ArrayList();
    }
}
