package ea;

import android.content.Context;
import android.os.UserHandle;
import android.util.ArraySet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/* compiled from: PackageRecord.java */
/* renamed from: ea.h, reason: use source file name */
/* loaded from: classes2.dex */
public class PackageRecord {

    /* renamed from: a, reason: collision with root package name */
    private Context f11005a;

    /* renamed from: b, reason: collision with root package name */
    private String f11006b;

    /* renamed from: c, reason: collision with root package name */
    private int f11007c;

    /* renamed from: d, reason: collision with root package name */
    private int f11008d;

    /* renamed from: e, reason: collision with root package name */
    private int f11009e;

    /* renamed from: f, reason: collision with root package name */
    private Set<j> f11010f = new ArraySet();

    public PackageRecord(Context context, String str, int i10) {
        this.f11005a = context;
        this.f11006b = str;
        this.f11007c = i10;
        this.f11009e = StateManager.e(context, str);
        this.f11008d = g(i10);
    }

    public static int g(int i10) {
        try {
            return UserHandle.getUserId(i10);
        } catch (Exception e10) {
            e10.printStackTrace();
            return -1;
        }
    }

    public void a(int i10) {
        this.f11010f.add(j.d(i10));
    }

    public boolean b(int i10) {
        return this.f11010f.contains(j.d(i10));
    }

    public List<j> c() {
        return new ArrayList(this.f11010f);
    }

    public int d() {
        return this.f11009e;
    }

    public String e() {
        return this.f11006b;
    }

    public int f() {
        return this.f11007c;
    }

    public void h(int i10) {
        this.f11010f.remove(j.d(i10));
    }
}
