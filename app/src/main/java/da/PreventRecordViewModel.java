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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import u9.StartupManager;
import z9.Record;

/* compiled from: PreventRecordViewModel.java */
/* renamed from: da.b, reason: use source file name */
/* loaded from: classes2.dex */
public class PreventRecordViewModel extends AndroidViewModel {

    /* renamed from: e, reason: collision with root package name */
    private LiveData<List<Record>> f10858e;

    /* renamed from: f, reason: collision with root package name */
    private MutableLiveData<Map<String, Drawable>> f10859f;

    /* renamed from: g, reason: collision with root package name */
    private Map<String, Drawable> f10860g;

    /* renamed from: h, reason: collision with root package name */
    private StartupManager f10861h;

    /* renamed from: i, reason: collision with root package name */
    private int f10862i;

    /* renamed from: j, reason: collision with root package name */
    private RecordDatabase f10863j;

    /* renamed from: k, reason: collision with root package name */
    private volatile boolean f10864k;

    /* renamed from: l, reason: collision with root package name */
    private Map<String, String> f10865l;

    /* compiled from: PreventRecordViewModel.java */
    /* renamed from: da.b$a */
    /* loaded from: classes2.dex */
    private class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private List<Record> f10866e;

        public a(List<Record> list) {
            this.f10866e = list;
        }

        @Override // java.lang.Runnable
        public void run() {
            PreventRecordViewModel.this.p(this.f10866e);
        }
    }

    public PreventRecordViewModel(Application application) {
        super(application);
        this.f10864k = false;
        this.f10865l = new ArrayMap();
        this.f10861h = StartupManager.i(application);
        this.f10863j = RecordDatabase.u(application);
    }

    private String k(String str, PackageManager packageManager) {
        if (!this.f10865l.containsKey(str)) {
            String p10 = this.f10861h.p(str, packageManager);
            this.f10865l.put(str, p10);
            return p10;
        }
        return this.f10865l.get(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void p(List<Record> list) {
        if (list != null && !list.isEmpty()) {
            for (int i10 = 0; i10 < list.size(); i10++) {
                Record record = list.get(i10);
                String str = record.f20324b;
                if (str != null) {
                    if (this.f10860g.get(str) == null) {
                        Map<String, Drawable> map = this.f10860g;
                        String str2 = record.f20324b;
                        map.put(str2, this.f10861h.d(str2));
                    }
                    if (i10 % 10 == 0) {
                        this.f10864k = true;
                        this.f10859f.k(this.f10860g);
                    }
                }
            }
            this.f10864k = true;
            this.f10859f.k(this.f10860g);
            return;
        }
        Log.d("StartupManager", "loadAppIconList: ---fail ---list is empty");
    }

    public Map<String, Drawable> h(List<Record> list) {
        if (this.f10860g == null) {
            this.f10860g = new ArrayMap();
        }
        p(list);
        new Thread(new a(list), "LoadIcon").start();
        return this.f10860g;
    }

    public void i(List<Record> list) {
        PackageManager packageManager = f().getPackageManager();
        Iterator<Record> it = list.iterator();
        while (it.hasNext()) {
            Record next = it.next();
            String k10 = k(next.f20324b, packageManager);
            next.f20326d = k10;
            if (TextUtils.isEmpty(k10)) {
                it.remove();
            } else if (this.f10862i == 1) {
                String k11 = k(next.f20325c, packageManager);
                next.f20327e = k11;
                if (TextUtils.isEmpty(k11)) {
                    it.remove();
                }
            }
        }
    }

    public List<String> j(List<Record> list, List<Record> list2) {
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap(list.size() + list2.size());
        Iterator<Record> it = list.iterator();
        while (it.hasNext()) {
            hashMap.put(it.next().f20324b, 1);
        }
        Iterator<Record> it2 = list2.iterator();
        while (it2.hasNext()) {
            hashMap.put(it2.next().f20324b, 2);
        }
        for (Map.Entry entry : hashMap.entrySet()) {
            if (((Integer) entry.getValue()).intValue() == 1) {
                arrayList.add((String) entry.getKey());
            }
        }
        return arrayList;
    }

    public LiveData<Map<String, Drawable>> l() {
        if (this.f10859f == null) {
            this.f10859f = new MutableLiveData<>();
        }
        return this.f10859f;
    }

    public LiveData<List<Record>> m() {
        if (this.f10862i == 0) {
            this.f10858e = this.f10863j.v().r();
        } else {
            this.f10858e = this.f10863j.v().p();
        }
        return this.f10858e;
    }

    public boolean n() {
        return this.f10864k;
    }

    public boolean o() {
        if (this.f10862i == 0) {
            List<Record> q10 = this.f10863j.v().q();
            return q10 == null || q10.isEmpty();
        }
        List<Record> o10 = this.f10863j.v().o();
        return o10 == null || o10.isEmpty();
    }

    public void q(int i10) {
        this.f10862i = i10;
    }

    public void r(List<String> list) {
        for (String str : list) {
            if (this.f10860g.get(str) != null) {
                this.f10860g.remove(str);
            }
        }
    }
}
