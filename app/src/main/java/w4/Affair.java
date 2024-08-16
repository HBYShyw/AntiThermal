package w4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.ArrayMap;
import android.util.ArraySet;
import b6.LocalLog;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* compiled from: Affair.java */
/* renamed from: w4.a, reason: use source file name */
/* loaded from: classes.dex */
public class Affair {

    /* renamed from: b, reason: collision with root package name */
    private static final String f19335b = "a";

    /* renamed from: c, reason: collision with root package name */
    private static final Map<Integer, Set<IAffairCallback>> f19336c = new ArrayMap();

    /* renamed from: d, reason: collision with root package name */
    private static final Map<Integer, Intent> f19337d = new ArrayMap();

    /* renamed from: a, reason: collision with root package name */
    private Handler f19338a;

    /* compiled from: Affair.java */
    /* renamed from: w4.a$a */
    /* loaded from: classes.dex */
    class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f19339e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Intent f19340f;

        a(int i10, Intent intent) {
            this.f19339e = i10;
            this.f19340f = intent;
        }

        @Override // java.lang.Runnable
        public void run() {
            ArraySet arraySet = new ArraySet();
            synchronized (Affair.f19336c) {
                Set set = Affair.f19336c.containsKey(Integer.valueOf(this.f19339e)) ? (Set) Affair.f19336c.get(Integer.valueOf(this.f19339e)) : null;
                if (set != null) {
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        arraySet.add((IAffairCallback) it.next());
                    }
                }
            }
            Iterator it2 = arraySet.iterator();
            while (it2.hasNext()) {
                ((IAffairCallback) it2.next()).execute(this.f19339e, this.f19340f);
            }
            arraySet.clear();
        }
    }

    /* compiled from: Affair.java */
    /* renamed from: w4.a$b */
    /* loaded from: classes.dex */
    class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f19342e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Bundle f19343f;

        b(int i10, Bundle bundle) {
            this.f19342e = i10;
            this.f19343f = bundle;
        }

        @Override // java.lang.Runnable
        public void run() {
            ArraySet arraySet = new ArraySet();
            synchronized (Affair.f19336c) {
                Set set = Affair.f19336c.containsKey(Integer.valueOf(this.f19342e)) ? (Set) Affair.f19336c.get(Integer.valueOf(this.f19342e)) : null;
                if (set != null) {
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        arraySet.add((IAffairCallback) it.next());
                    }
                }
            }
            Iterator it2 = arraySet.iterator();
            while (it2.hasNext()) {
                ((IAffairCallback) it2.next()).execute(this.f19342e, this.f19343f);
            }
            arraySet.clear();
        }
    }

    /* compiled from: Affair.java */
    /* renamed from: w4.a$c */
    /* loaded from: classes.dex */
    private static class c implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        IAffairCallback f19345e;

        /* renamed from: f, reason: collision with root package name */
        Intent f19346f;

        /* renamed from: g, reason: collision with root package name */
        int f19347g;

        public c(IAffairCallback iAffairCallback, Intent intent, int i10) {
            this.f19345e = iAffairCallback;
            this.f19346f = intent;
            this.f19347g = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f19345e.execute(this.f19347g, this.f19346f);
            LocalLog.a(Affair.f19335b, "execute after registerAffairListener. affairType=" + this.f19347g + ", callback" + this.f19345e);
        }
    }

    /* compiled from: Affair.java */
    /* renamed from: w4.a$d */
    /* loaded from: classes.dex */
    private static class d {

        /* renamed from: a, reason: collision with root package name */
        private static final Affair f19348a = new Affair(null);
    }

    /* synthetic */ Affair(a aVar) {
        this();
    }

    public static Affair f() {
        return d.f19348a;
    }

    public void c() {
        Map<Integer, Set<IAffairCallback>> map = f19336c;
        synchronized (map) {
            map.clear();
        }
    }

    public void d(int i10, Intent intent) {
        a aVar = new a(i10, new Intent(intent));
        Handler handler = this.f19338a;
        if (handler != null) {
            handler.post(aVar);
            if (i10 == 201 || i10 == 202) {
                LocalLog.l(f19335b, "post action finally act = " + i10);
            }
        }
    }

    public void e(int i10, Bundle bundle) {
        b bVar = new b(i10, bundle);
        Handler handler = this.f19338a;
        if (handler != null) {
            handler.post(bVar);
        }
    }

    public void g(IAffairCallback iAffairCallback, int i10) {
        Intent intent;
        if (i10 < 0) {
            LocalLog.l(f19335b, "sceneType is " + i10 + " less than 0, do not to register");
            return;
        }
        Map<Integer, Set<IAffairCallback>> map = f19336c;
        synchronized (map) {
            Set<IAffairCallback> set = map.containsKey(Integer.valueOf(i10)) ? map.get(Integer.valueOf(i10)) : null;
            if (set == null) {
                set = new ArraySet<>();
                map.put(Integer.valueOf(i10), set);
            }
            set.add(iAffairCallback);
        }
        if (this.f19338a == null) {
            return;
        }
        Map<Integer, Intent> map2 = f19337d;
        synchronized (map2) {
            intent = map2.containsKey(Integer.valueOf(i10)) ? map2.get(Integer.valueOf(i10)) : null;
        }
        if (intent == null) {
            return;
        }
        this.f19338a.post(new c(iAffairCallback, intent, i10));
    }

    public void h(int i10, Intent intent) {
        Map<Integer, Intent> map = f19337d;
        synchronized (map) {
            map.put(Integer.valueOf(i10), intent);
        }
    }

    public void i(IAffairCallback iAffairCallback, int i10) {
        Map<Integer, Set<IAffairCallback>> map = f19336c;
        synchronized (map) {
            Set<IAffairCallback> set = map.containsKey(Integer.valueOf(i10)) ? map.get(Integer.valueOf(i10)) : null;
            if (set != null) {
                set.remove(iAffairCallback);
            }
        }
    }

    private Affair() {
        HandlerThread handlerThread = new HandlerThread("AffairDispatcher");
        handlerThread.start();
        this.f19338a = new Handler(handlerThread.getLooper());
    }
}
