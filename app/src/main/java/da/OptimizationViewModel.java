package da;

import android.app.Application;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.oplus.startupapp.data.database.RecordDatabase;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import u9.StartupManager;
import z9.AppToShow;

/* compiled from: OptimizationViewModel.java */
/* renamed from: da.a, reason: use source file name */
/* loaded from: classes2.dex */
public class OptimizationViewModel extends AndroidViewModel {

    /* renamed from: e, reason: collision with root package name */
    public LiveData<List<AppToShow>> f10850e;

    /* renamed from: f, reason: collision with root package name */
    public Map<String, Drawable> f10851f;

    /* renamed from: g, reason: collision with root package name */
    public MutableLiveData<Map<String, Drawable>> f10852g;

    /* renamed from: h, reason: collision with root package name */
    public StartupManager f10853h;

    /* renamed from: i, reason: collision with root package name */
    public RecordDatabase f10854i;

    /* renamed from: j, reason: collision with root package name */
    private Map<String, String> f10855j;

    /* compiled from: OptimizationViewModel.java */
    /* renamed from: da.a$a */
    /* loaded from: classes2.dex */
    private class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private List<AppToShow> f10856e;

        public a(List<AppToShow> list) {
            this.f10856e = list;
        }

        @Override // java.lang.Runnable
        public void run() {
            OptimizationViewModel.this.l(this.f10856e);
        }
    }

    public OptimizationViewModel(Application application) {
        super(application);
        this.f10851f = new ArrayMap();
        this.f10852g = new MutableLiveData<>();
        this.f10855j = new ArrayMap();
        this.f10854i = RecordDatabase.u(application);
        this.f10853h = StartupManager.i(application.getApplicationContext());
        this.f10850e = this.f10854i.v().t(true, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l(List<AppToShow> list) {
        Map<String, Drawable> map;
        if (list != null && !list.isEmpty()) {
            if (this.f10851f == null) {
                this.f10851f = new ArrayMap();
            }
            for (int i10 = 0; i10 < list.size(); i10++) {
                if (list.get(i10).f20305b == null || (map = this.f10851f) == null || map.get(list.get(i10).f20305b) == null) {
                    this.f10851f.put(list.get(i10).f20305b, this.f10853h.d(list.get(i10).f20305b));
                }
                if (i10 % 10 == 0) {
                    this.f10852g.k(this.f10851f);
                }
            }
            this.f10852g.k(this.f10851f);
            return;
        }
        Log.d("StartupManager", "loadAppIconList: ---fail ---list is empty");
    }

    public void h(List<AppToShow> list) {
        new Thread(new a(list), "LoadIcon").start();
    }

    public void i(List<AppToShow> list) {
        PackageManager packageManager = f().getPackageManager();
        Iterator<AppToShow> it = list.iterator();
        while (it.hasNext()) {
            AppToShow next = it.next();
            if (!this.f10855j.containsKey(next.f20305b)) {
                String p10 = this.f10853h.p(next.f20305b, packageManager);
                next.f20306c = p10;
                this.f10855j.put(next.f20305b, p10);
            } else {
                next.f20306c = this.f10855j.get(next.f20305b);
            }
            if (TextUtils.isEmpty(next.f20306c)) {
                it.remove();
            }
        }
    }

    public LiveData<Map<String, Drawable>> j() {
        return this.f10852g;
    }

    public LiveData<List<AppToShow>> k() {
        return this.f10850e;
    }

    public void m(Map<String, Boolean> map) {
        this.f10853h.y(map);
    }
}
