package r7;

import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import j.ArraySet;
import java.util.HashMap;
import java.util.Iterator;
import o7.Compat;
import o7.Vector;
import p7.Body;
import p7.World;
import q7.SpringDef;
import r7.ChoreographerCompat;

/* compiled from: PhysicalAnimator.java */
/* renamed from: r7.k, reason: use source file name */
/* loaded from: classes.dex */
public class PhysicalAnimator implements ChoreographerCompat.a {

    /* renamed from: a, reason: collision with root package name */
    private final Context f17586a;

    /* renamed from: g, reason: collision with root package name */
    private HashMap<c, AnimationListener> f17592g;

    /* renamed from: h, reason: collision with root package name */
    private HashMap<c, AnimationUpdateListener> f17593h;

    /* renamed from: j, reason: collision with root package name */
    private World f17595j;

    /* renamed from: k, reason: collision with root package name */
    private Body f17596k;

    /* renamed from: b, reason: collision with root package name */
    private final ArraySet<c> f17587b = new ArraySet<>(1);

    /* renamed from: c, reason: collision with root package name */
    private final ArraySet<c> f17588c = new ArraySet<>(1);

    /* renamed from: d, reason: collision with root package name */
    private boolean f17589d = true;

    /* renamed from: e, reason: collision with root package name */
    private boolean f17590e = false;

    /* renamed from: f, reason: collision with root package name */
    private boolean f17591f = false;

    /* renamed from: i, reason: collision with root package name */
    private ChoreographerCompat f17594i = null;

    private PhysicalAnimator(Context context) {
        this.f17586a = context;
        o();
    }

    private Body d(UIItem uIItem, int i10) {
        Body f10 = f(this.f17595j.f().d(Compat.d(uIItem.f17605e.f16270a), Compat.d(uIItem.f17605e.f16271b)), 1, i10, Compat.d(uIItem.f17601a), Compat.d(uIItem.f17602b), i(i10));
        f10.f16596e.f();
        f10.l(true);
        return f10;
    }

    public static PhysicalAnimator e(Context context) {
        return new PhysicalAnimator(context);
    }

    private void h() {
        this.f17595j = new World();
        this.f17596k = f(new Vector(), 0, 5, 0.0f, 0.0f, "Ground");
        if (o7.b.b()) {
            o7.b.c("createWorld : " + this);
        }
    }

    private static String i(int i10) {
        return i10 != 1 ? i10 != 2 ? i10 != 3 ? i10 != 4 ? "custom" : "alpha" : "rotation" : "scale" : "position";
    }

    private void o() {
        ChoreographerCompat choreographerCompat = new ChoreographerCompat();
        this.f17594i = choreographerCompat;
        choreographerCompat.e(this);
        p();
        h();
    }

    private void p() {
        Compat.e(this.f17586a.getResources().getDisplayMetrics().density);
        Display defaultDisplay = ((WindowManager) this.f17586a.getSystemService("window")).getDefaultDisplay();
        if (defaultDisplay != null) {
            Compat.f(1.0f / defaultDisplay.getRefreshRate());
        }
        if (o7.b.b()) {
            o7.b.c("initConfig : sPhysicalSizeToPixelsRatio =:" + Compat.f16265c + ",sSteadyAccuracy =:" + Compat.f16264b + ",sRefreshRate =:" + Compat.f16263a);
        }
    }

    private void q(c cVar) {
        AnimationListener animationListener;
        HashMap<c, AnimationListener> hashMap = this.f17592g;
        if (hashMap == null || (animationListener = hashMap.get(cVar)) == null) {
            return;
        }
        animationListener.a(cVar);
    }

    private void r(c cVar) {
        AnimationListener animationListener;
        HashMap<c, AnimationListener> hashMap = this.f17592g;
        if (hashMap == null || (animationListener = hashMap.get(cVar)) == null) {
            return;
        }
        animationListener.c(cVar);
    }

    private void s(c cVar) {
        AnimationUpdateListener animationUpdateListener;
        HashMap<c, AnimationUpdateListener> hashMap = this.f17593h;
        if (hashMap == null || (animationUpdateListener = hashMap.get(cVar)) == null) {
            return;
        }
        animationUpdateListener.b(cVar);
    }

    private void t() {
        if (this.f17590e) {
            this.f17594i.f();
            this.f17590e = false;
        }
    }

    private void v() {
        if (this.f17590e) {
            return;
        }
        this.f17594i.d();
        this.f17590e = true;
    }

    private void x() {
        this.f17595j.i(Compat.f16263a);
        z();
    }

    private void z() {
        if (o7.b.a()) {
            o7.b.d("PhysicsWorld-Frame", "syncMoverChanging start ===========> mCurrentRunningBehaviors =:" + this.f17587b.size());
        }
        Iterator<c> it = this.f17587b.iterator();
        while (it.hasNext()) {
            c next = it.next();
            if (next != null) {
                next.m();
                A(next);
                s(next);
                if (o7.b.a()) {
                    o7.b.d("PhysicsWorld-Frame", "updateBehavior : " + next);
                }
                if (next.s()) {
                    if (o7.b.b()) {
                        o7.b.c("syncMoverChanging : behavior is steady");
                    }
                    next.B();
                }
            }
        }
        this.f17589d = this.f17587b.isEmpty();
        if (o7.b.a()) {
            o7.b.d("PhysicsWorld-Frame", "syncMoverChanging end ===========> mCurrentRunningBehaviors =:" + this.f17587b.size());
        }
        if (this.f17589d) {
            t();
        } else {
            this.f17594i.d();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A(c cVar) {
        cVar.D();
    }

    public void a(c cVar, AnimationListener animationListener) {
        if (this.f17592g == null) {
            this.f17592g = new HashMap<>(1);
        }
        this.f17592g.put(cVar, animationListener);
    }

    public void b(c cVar, AnimationUpdateListener animationUpdateListener) {
        if (this.f17593h == null) {
            this.f17593h = new HashMap<>(1);
        }
        this.f17593h.put(cVar, animationUpdateListener);
    }

    public <T extends c> T c(T t7) {
        Object obj;
        Object obj2;
        t7.c(this);
        int i10 = 0;
        while (i10 < this.f17588c.size()) {
            c k10 = this.f17588c.k(i10);
            if (k10 != null && (obj = k10.f17558n) != null && (obj2 = t7.f17558n) != null && obj == obj2 && k10.q() == t7.q() && u(k10)) {
                i10--;
            }
            i10++;
        }
        this.f17588c.add(t7);
        if (o7.b.b()) {
            o7.b.c("addBehavior behavior =:" + t7 + ",mAllBehaviors.size =:" + this.f17588c.size());
        }
        return t7;
    }

    @Override // r7.ChoreographerCompat.a
    public void doFrame(long j10) {
        if (this.f17591f) {
            return;
        }
        x();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Body f(Vector vector, int i10, int i11, float f10, float f11, String str) {
        return this.f17595j.a(vector, i10, i11, f10, f11, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public q7.b g(SpringDef springDef) {
        return this.f17595j.b(springDef);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean j(Body body) {
        if (body == null) {
            return false;
        }
        this.f17595j.c(body);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean k(q7.b bVar) {
        this.f17595j.d(bVar);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Body l() {
        return this.f17596k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Body m(UIItem uIItem, int i10) {
        Body body;
        if (o7.b.b()) {
            o7.b.c("getOrCreatePropertyBody : uiItem =:" + uIItem + ",propertyType =:" + i10);
        }
        Iterator<c> it = this.f17588c.iterator();
        while (it.hasNext()) {
            c next = it.next();
            UIItem uIItem2 = next.f17554j;
            if (uIItem2 != null && uIItem2 == uIItem && (body = next.f17555k) != null && body.g() == i10) {
                return next.f17555k;
            }
        }
        return d(uIItem, i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UIItem n(Object obj) {
        Object obj2;
        if (o7.b.b()) {
            o7.b.c("getOrCreateUIItem : target =:" + obj);
        }
        Iterator<c> it = this.f17588c.iterator();
        while (it.hasNext()) {
            UIItem uIItem = it.next().f17554j;
            if (uIItem != null && (obj2 = uIItem.f17603c) != null && obj != null && obj2 == obj) {
                return uIItem;
            }
        }
        if (obj instanceof View) {
            View view = (View) obj;
            UIItem b10 = new UIItem(obj).b(view.getMeasuredWidth(), view.getMeasuredHeight());
            b10.c(view.getX(), view.getY());
            b10.d(view.getScaleX(), view.getScaleY());
            return b10;
        }
        if (obj instanceof UIItem) {
            return (UIItem) obj;
        }
        return new UIItem().b(0.0f, 0.0f);
    }

    public boolean u(c cVar) {
        if (cVar == null) {
            return false;
        }
        boolean remove = this.f17588c.remove(cVar);
        if (o7.b.b()) {
            o7.b.c("removeBehavior behavior =:" + cVar + ",removed =:" + remove);
        }
        if (remove) {
            cVar.y();
        }
        return remove;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w(c cVar) {
        Object obj;
        Object obj2;
        Body body;
        Body body2;
        if (this.f17591f) {
            return;
        }
        if (this.f17587b.contains(cVar) && this.f17590e) {
            return;
        }
        if (o7.b.b()) {
            o7.b.c("startBehavior behavior =:" + cVar);
        }
        int i10 = 0;
        while (i10 < this.f17587b.size()) {
            c k10 = this.f17587b.k(i10);
            if (k10 != null && (obj = k10.f17558n) != null && (obj2 = cVar.f17558n) != null && obj == obj2 && (body = k10.f17555k) != null && (body2 = cVar.f17555k) != null && body == body2 && k10.B()) {
                i10--;
            }
            i10++;
        }
        this.f17587b.add(cVar);
        this.f17589d = false;
        v();
        r(cVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y(c cVar) {
        this.f17587b.remove(cVar);
        if (o7.b.b()) {
            o7.b.c("stopBehavior behavior =:" + cVar + ",mCurrentRunningBehaviors.size() =:" + this.f17587b.size());
        }
        q(cVar);
    }
}
