package t1;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;

/* compiled from: BaseCardInstructionAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\r\n\u0002\b\t\n\u0002\u0010!\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u000f\b\u0016\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\t\u001a\u00020\u0002¢\u0006\u0004\b!\u0010\"R\"\u0010\u0003\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\"\u0010\t\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\t\u0010\u0004\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001d\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\f8\u0006¢\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\u0010\u0010\u0011R\u001d\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\r0\f8\u0006¢\u0006\f\n\u0004\b\u0012\u0010\u000f\u001a\u0004\b\u0013\u0010\u0011R\"\u0010\u0015\u001a\u00020\u00148\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\"\u0010\u001b\u001a\u00020\u00148\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u001b\u0010\u0016\u001a\u0004\b\u001c\u0010\u0018\"\u0004\b\u001d\u0010\u001aR\"\u0010\u001e\u001a\u00020\u00148\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u001e\u0010\u0016\u001a\u0004\b\u001f\u0010\u0018\"\u0004\b \u0010\u001a¨\u0006#"}, d2 = {"Lt1/d;", "", "", "title", "Ljava/lang/CharSequence;", "g", "()Ljava/lang/CharSequence;", "setTitle", "(Ljava/lang/CharSequence;)V", "summary", "f", "setSummary", "", "", "choices", "Ljava/util/List;", "d", "()Ljava/util/List;", "animTitles", "b", "", "selectedIndex", "I", "e", "()I", "setSelectedIndex", "(I)V", "animHeight", "a", "setAnimHeight", "animWidth", "c", "setAnimWidth", "<init>", "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public class d {

    /* renamed from: a, reason: collision with root package name */
    private CharSequence f18522a;

    /* renamed from: b, reason: collision with root package name */
    private CharSequence f18523b;

    /* renamed from: c, reason: collision with root package name */
    private final List<String> f18524c;

    /* renamed from: d, reason: collision with root package name */
    private final List<String> f18525d;

    /* renamed from: e, reason: collision with root package name */
    private int f18526e;

    /* renamed from: f, reason: collision with root package name */
    private int f18527f;

    /* renamed from: g, reason: collision with root package name */
    private int f18528g;

    public d(CharSequence charSequence, CharSequence charSequence2) {
        za.k.e(charSequence, "title");
        za.k.e(charSequence2, "summary");
        this.f18522a = charSequence;
        this.f18523b = charSequence2;
        this.f18524c = new ArrayList();
        this.f18525d = new ArrayList();
    }

    /* renamed from: a, reason: from getter */
    public final int getF18527f() {
        return this.f18527f;
    }

    public final List<String> b() {
        return this.f18525d;
    }

    /* renamed from: c, reason: from getter */
    public final int getF18528g() {
        return this.f18528g;
    }

    public final List<String> d() {
        return this.f18524c;
    }

    /* renamed from: e, reason: from getter */
    public final int getF18526e() {
        return this.f18526e;
    }

    /* renamed from: f, reason: from getter */
    public final CharSequence getF18523b() {
        return this.f18523b;
    }

    /* renamed from: g, reason: from getter */
    public final CharSequence getF18522a() {
        return this.f18522a;
    }
}
