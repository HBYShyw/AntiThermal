package da;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.ArrayMap;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import b6.LocalLog;
import com.oplus.startupapp.data.database.RecordDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import u9.StartupManager;
import w9.AppNameComparator;
import z9.AppToShow;

/* compiled from: StartupViewModel.java */
/* renamed from: da.c, reason: use source file name */
/* loaded from: classes2.dex */
public class StartupViewModel extends ViewModel {

    /* renamed from: j, reason: collision with root package name */
    public RecordDatabase f10874j;

    /* renamed from: m, reason: collision with root package name */
    private StartupManager f10877m;

    /* renamed from: d, reason: collision with root package name */
    public Map<String, Drawable> f10868d = new ArrayMap();

    /* renamed from: e, reason: collision with root package name */
    public Map<String, Drawable> f10869e = new ArrayMap();

    /* renamed from: f, reason: collision with root package name */
    public MutableLiveData<List<AppToShow>> f10870f = new MutableLiveData<>();

    /* renamed from: g, reason: collision with root package name */
    public MutableLiveData<List<AppToShow>> f10871g = new MutableLiveData<>();

    /* renamed from: h, reason: collision with root package name */
    public MutableLiveData<Map<String, Drawable>> f10872h = new MutableLiveData<>();

    /* renamed from: i, reason: collision with root package name */
    public MutableLiveData<Map<String, Drawable>> f10873i = new MutableLiveData<>();

    /* renamed from: k, reason: collision with root package name */
    private Object f10875k = new Object();

    /* renamed from: l, reason: collision with root package name */
    private Object f10876l = new Object();

    /* renamed from: n, reason: collision with root package name */
    private int f10878n = 8;

    /* renamed from: o, reason: collision with root package name */
    private int f10879o = 0;

    /* renamed from: p, reason: collision with root package name */
    private int f10880p = 0;

    /* renamed from: q, reason: collision with root package name */
    private volatile boolean f10881q = false;

    /* renamed from: r, reason: collision with root package name */
    private volatile boolean f10882r = false;

    /* renamed from: s, reason: collision with root package name */
    private String f10883s = Locale.getDefault().getLanguage();

    /* renamed from: t, reason: collision with root package name */
    private volatile boolean f10884t = true;

    /* compiled from: StartupViewModel.java */
    /* renamed from: da.c$a */
    /* loaded from: classes2.dex */
    private class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private List<AppToShow> f10885e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f10886f;

        public a(List<AppToShow> list, boolean z10) {
            this.f10885e = list;
            this.f10886f = z10;
        }

        @Override // java.lang.Runnable
        public void run() {
            StartupViewModel.this.u(this.f10885e, this.f10886f);
        }
    }

    /* compiled from: StartupViewModel.java */
    /* renamed from: da.c$b */
    /* loaded from: classes2.dex */
    private class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        List<AppToShow> f10888e;

        /* renamed from: f, reason: collision with root package name */
        boolean f10889f;

        b(List<AppToShow> list, boolean z10) {
            this.f10888e = list;
            this.f10889f = z10;
        }

        @Override // java.lang.Runnable
        public void run() {
            StartupViewModel.this.v(this.f10888e, this.f10889f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void u(List<AppToShow> list, boolean z10) {
        if (list != null && !list.isEmpty()) {
            PackageManager k10 = this.f10877m.k();
            ArrayMap arrayMap = new ArrayMap();
            for (ApplicationInfo applicationInfo : k10.getInstalledApplications(128)) {
                arrayMap.put(applicationInfo.packageName, applicationInfo);
            }
            Object obj = z10 ? this.f10875k : this.f10876l;
            Map<String, Drawable> map = z10 ? this.f10868d : this.f10869e;
            for (int i10 = 0; i10 < list.size(); i10++) {
                String str = list.get(i10).f20305b;
                if (!TextUtils.isEmpty(str)) {
                    if (!this.f10884t) {
                        LocalLog.l("StartupManager", " stop async work when loadIcon: " + this);
                        return;
                    }
                    synchronized (obj) {
                        if (map.get(str) == null) {
                            map.put(str, this.f10877m.c(k10, (ApplicationInfo) arrayMap.get(str)));
                        }
                    }
                    if (i10 <= 0 || (i10 + 1) % this.f10878n != 0) {
                        if (this.f10881q && !this.f10882r && !z10 && this.f10879o + i10 + 1 >= this.f10878n) {
                            this.f10882r = true;
                            this.f10873i.k(this.f10869e);
                            LocalLog.l("StartupManager", " ready for first page: isAutoStart:" + this.f10880p);
                        }
                    } else if (z10) {
                        this.f10872h.k(this.f10868d);
                        this.f10881q = true;
                        this.f10879o = i10;
                    } else {
                        this.f10882r = true;
                        this.f10873i.k(this.f10869e);
                    }
                }
            }
            if (z10) {
                this.f10881q = true;
                this.f10872h.k(this.f10868d);
                this.f10879o = list.size();
                return;
            } else {
                this.f10882r = true;
                this.f10873i.k(this.f10869e);
                return;
            }
        }
        LocalLog.l("StartupManager", "loadAppIconList: failed for empty list");
        if (z10) {
            this.f10881q = true;
            this.f10879o = 0;
        } else {
            this.f10882r = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.lifecycle.ViewModel
    public void d() {
        super.d();
        LocalLog.l("StartupManager", " stop and clear async work " + this);
        this.f10884t = false;
    }

    public Map<String, Drawable> g(List<AppToShow> list, boolean z10) {
        this.f10877m.b(new a(list, z10));
        return z10 ? this.f10868d : this.f10869e;
    }

    public void h(List<AppToShow> list, boolean z10) {
        this.f10884t = true;
        this.f10877m.b(new b(list, z10));
    }

    public int i(boolean z10) {
        return this.f10877m.f(z10);
    }

    public List<String> j(List<AppToShow> list, List<AppToShow> list2) {
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap(list.size() + list2.size());
        Iterator<AppToShow> it = list.iterator();
        while (it.hasNext()) {
            hashMap.put(it.next().f20305b, 1);
        }
        Iterator<AppToShow> it2 = list2.iterator();
        while (it2.hasNext()) {
            hashMap.put(it2.next().f20305b, 2);
        }
        for (Map.Entry entry : hashMap.entrySet()) {
            if (((Integer) entry.getValue()).intValue() == 1) {
                arrayList.add((String) entry.getKey());
            }
        }
        return arrayList;
    }

    public int k() {
        return this.f10877m.j();
    }

    public LiveData<List<AppToShow>> l() {
        return this.f10871g;
    }

    public LiveData<Map<String, Drawable>> m() {
        return this.f10873i;
    }

    public LiveData<List<AppToShow>> n() {
        if (this.f10880p == 0) {
            return this.f10874j.v().l(true, false);
        }
        return this.f10874j.v().l(false, false);
    }

    public LiveData<List<AppToShow>> o() {
        return this.f10870f;
    }

    public LiveData<Map<String, Drawable>> p() {
        return this.f10872h;
    }

    public LiveData<List<AppToShow>> q() {
        if (this.f10880p == 0) {
            return this.f10874j.v().l(true, true);
        }
        return this.f10874j.v().l(false, true);
    }

    public void r(Application application) {
        this.f10874j = RecordDatabase.u(application);
        this.f10877m = StartupManager.i(application);
    }

    public boolean s(boolean z10) {
        return (z10 ? this.f10868d : this.f10869e).isEmpty();
    }

    public boolean t() {
        return this.f10881q && this.f10882r;
    }

    public void v(List<AppToShow> list, boolean z10) {
        PackageManager k10 = this.f10877m.k();
        ArrayMap arrayMap = new ArrayMap();
        for (ApplicationInfo applicationInfo : k10.getInstalledApplications(128)) {
            arrayMap.put(applicationInfo.packageName, applicationInfo);
        }
        Iterator<AppToShow> it = list.iterator();
        while (it.hasNext()) {
            if (!this.f10884t) {
                LocalLog.l("StartupManager", " stop async work when loadLabel: " + this);
                return;
            }
            AppToShow next = it.next();
            this.f10877m.q((ApplicationInfo) arrayMap.get(next.f20305b), k10, next, this.f10883s);
            if (TextUtils.isEmpty(next.f20306c)) {
                it.remove();
            }
        }
        Collections.sort(list, AppNameComparator.f19408g);
        if (z10) {
            this.f10870f.k(list);
        } else {
            this.f10871g.k(list);
        }
    }

    public void w() {
        this.f10883s = Locale.getDefault().getLanguage();
        this.f10884t = true;
        LocalLog.l("StartupManager", " refreshCreateConfig language: " + this.f10883s);
    }

    public void x(int i10) {
        this.f10880p = i10;
    }

    public void y(List<String> list, boolean z10) {
        for (String str : list) {
            if (z10) {
                synchronized (this.f10875k) {
                    if (this.f10868d.get(str) != null) {
                        this.f10868d.remove(str);
                    }
                }
            } else {
                synchronized (this.f10876l) {
                    if (this.f10869e.get(str) != null) {
                        this.f10869e.remove(str);
                    }
                }
            }
        }
    }

    public void z(String str, boolean z10) {
        this.f10877m.A(str, z10, this.f10880p == 0);
    }
}
