package ea;

import android.content.Context;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import b6.LocalLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: RegisterStateStrategy.java */
/* renamed from: ea.i, reason: use source file name */
/* loaded from: classes2.dex */
public class RegisterStateStrategy {

    /* renamed from: a, reason: collision with root package name */
    private Context f11011a;

    /* renamed from: b, reason: collision with root package name */
    private final Map<a, PackageRecord> f11012b = new ArrayMap();

    /* renamed from: c, reason: collision with root package name */
    private final Map<j, CopyOnWriteArrayList<PackageRecord>> f11013c = new ArrayMap();

    /* compiled from: RegisterStateStrategy.java */
    /* renamed from: ea.i$a */
    /* loaded from: classes2.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        String f11014a;

        /* renamed from: b, reason: collision with root package name */
        int f11015b;

        public a(String str, int i10) {
            this.f11014a = str;
            this.f11015b = i10;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }
            a aVar = (a) obj;
            return this.f11014a.equals(aVar.f11014a) && this.f11015b == aVar.f11015b;
        }

        public int hashCode() {
            return Objects.hash(this.f11014a, Integer.valueOf(this.f11015b));
        }
    }

    public RegisterStateStrategy(Context context) {
        this.f11011a = context;
    }

    public static int d(int i10) {
        try {
            return UserHandle.getUserId(i10);
        } catch (Exception e10) {
            e10.printStackTrace();
            return -1;
        }
    }

    void a(int i10, String str, int i11) {
        PackageRecord packageRecord;
        a aVar = new a(str, d(i11));
        synchronized (this.f11012b) {
            packageRecord = this.f11012b.get(aVar);
            if (packageRecord == null) {
                packageRecord = new PackageRecord(this.f11011a, str, i11);
                this.f11012b.put(aVar, packageRecord);
            }
            packageRecord.a(i10);
        }
        j d10 = j.d(i10);
        synchronized (this.f11013c) {
            CopyOnWriteArrayList<PackageRecord> copyOnWriteArrayList = this.f11013c.get(d10);
            if (copyOnWriteArrayList == null) {
                copyOnWriteArrayList = new CopyOnWriteArrayList<>();
                this.f11013c.put(d10, copyOnWriteArrayList);
            }
            copyOnWriteArrayList.add(packageRecord);
        }
    }

    public List<PackageRecord> b(int i10) {
        CopyOnWriteArrayList<PackageRecord> orDefault;
        synchronized (this.f11013c) {
            orDefault = this.f11013c.getOrDefault(j.d(i10), new CopyOnWriteArrayList<>());
        }
        return orDefault;
    }

    public List<j> c(String str, int i10) {
        synchronized (this.f11012b) {
            PackageRecord packageRecord = this.f11012b.get(new a(str, i10));
            if (packageRecord == null) {
                return new ArrayList();
            }
            return packageRecord.c();
        }
    }

    public void e(int i10, String str, int i11, int i12) {
        if (j.c(i10) && !TextUtils.isEmpty(str) && i11 >= 0) {
            if (i12 == 1) {
                a(i10, str, i11);
                return;
            }
            if (i12 == 0) {
                f(i10, str, i11);
                return;
            }
            LocalLog.l("StateManager", "unrecognized state " + i12);
        }
    }

    void f(int i10, String str, int i11) {
        PackageRecord packageRecord;
        a aVar = new a(str, d(i11));
        synchronized (this.f11012b) {
            packageRecord = this.f11012b.get(aVar);
        }
        if (packageRecord == null) {
            LocalLog.b("StateManager", "packageName:" + str + ", uid:" + i11 + " not contain state:" + i10);
            return;
        }
        if (packageRecord.b(i10)) {
            packageRecord.h(i10);
        } else {
            LocalLog.b("StateManager", "packageName:" + str + ", uid:" + i11 + " not contain state:" + i10);
        }
        j d10 = j.d(i10);
        synchronized (this.f11013c) {
            CopyOnWriteArrayList<PackageRecord> copyOnWriteArrayList = this.f11013c.get(d10);
            if (copyOnWriteArrayList == null) {
                LocalLog.b("StateManager", "Aha, stateValue:" + i10 + " not in list!!!");
                return;
            }
            copyOnWriteArrayList.remove(packageRecord);
        }
    }
}
