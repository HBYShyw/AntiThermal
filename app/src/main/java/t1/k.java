package t1;

import kotlin.Metadata;

/* compiled from: BaseCardInstructionAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\r\n\u0002\b\u0005\u0018\u00002\u00020\u0001B)\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0002\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\b\b\u0002\u0010\n\u001a\u00020\b¢\u0006\u0004\b\u000b\u0010\fR\u001d\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u00028\u0006¢\u0006\f\n\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0006\u0010\u0007¨\u0006\r"}, d2 = {"Lt1/k;", "Lt1/d;", "", "", "imageResources", "[Ljava/lang/Integer;", "h", "()[Ljava/lang/Integer;", "", "title", "summary", "<init>", "([Ljava/lang/Integer;Ljava/lang/CharSequence;Ljava/lang/CharSequence;)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class k extends d {

    /* renamed from: h, reason: collision with root package name */
    private final Integer[] f18555h;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public k(Integer[] numArr, CharSequence charSequence, CharSequence charSequence2) {
        super(charSequence, charSequence2);
        za.k.e(numArr, "imageResources");
        za.k.e(charSequence, "title");
        za.k.e(charSequence2, "summary");
        this.f18555h = numArr;
    }

    /* renamed from: h, reason: from getter */
    public final Integer[] getF18555h() {
        return this.f18555h;
    }
}
