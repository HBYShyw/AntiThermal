package z9;

import android.text.TextUtils;
import w9.AppNameComparator;

/* compiled from: AppToShow.java */
/* renamed from: z9.a, reason: use source file name */
/* loaded from: classes2.dex */
public class AppToShow implements AppNameComparator.a {

    /* renamed from: a, reason: collision with root package name */
    public long f20304a;

    /* renamed from: b, reason: collision with root package name */
    public String f20305b;

    /* renamed from: c, reason: collision with root package name */
    public String f20306c;

    /* renamed from: d, reason: collision with root package name */
    public boolean f20307d;

    /* renamed from: e, reason: collision with root package name */
    public boolean f20308e;

    /* renamed from: f, reason: collision with root package name */
    public boolean f20309f;

    /* renamed from: g, reason: collision with root package name */
    public boolean f20310g;

    @Override // w9.AppNameComparator.a
    public int a() {
        w9.b e10 = w9.b.e();
        String str = this.f20306c;
        if (str == null) {
            str = this.f20305b;
        }
        return e10.b(str);
    }

    @Override // w9.AppNameComparator.a
    public String b() {
        return this.f20306c;
    }

    @Override // w9.AppNameComparator.a
    public char c() {
        w9.b e10 = w9.b.e();
        int a10 = a();
        String str = this.f20306c;
        if (str == null) {
            str = this.f20305b;
        }
        String d10 = e10.d(a10, str);
        if (TextUtils.isEmpty(d10)) {
            return '0';
        }
        return d10.charAt(0);
    }
}
