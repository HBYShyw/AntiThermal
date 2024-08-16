package com.oplus.anim;

import android.graphics.Rect;
import j.LongSparseArray;
import j.SparseArrayCompat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import l4.Font;
import l4.FontCharacter;
import l4.Marker;
import s4.MiscUtils;

/* compiled from: EffectiveAnimationComposition.java */
/* renamed from: com.oplus.anim.a, reason: use source file name */
/* loaded from: classes.dex */
public class EffectiveAnimationComposition {

    /* renamed from: c, reason: collision with root package name */
    private Map<String, List<o4.e>> f9612c;

    /* renamed from: d, reason: collision with root package name */
    private Map<String, EffectiveImageAsset> f9613d;

    /* renamed from: e, reason: collision with root package name */
    private Map<String, Font> f9614e;

    /* renamed from: f, reason: collision with root package name */
    private List<Marker> f9615f;

    /* renamed from: g, reason: collision with root package name */
    private SparseArrayCompat<FontCharacter> f9616g;

    /* renamed from: h, reason: collision with root package name */
    private LongSparseArray<o4.e> f9617h;

    /* renamed from: i, reason: collision with root package name */
    private List<o4.e> f9618i;

    /* renamed from: j, reason: collision with root package name */
    private Rect f9619j;

    /* renamed from: k, reason: collision with root package name */
    private float f9620k;

    /* renamed from: l, reason: collision with root package name */
    private float f9621l;

    /* renamed from: m, reason: collision with root package name */
    private float f9622m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f9623n;

    /* renamed from: a, reason: collision with root package name */
    private final PerformanceTracker f9610a = new PerformanceTracker();

    /* renamed from: b, reason: collision with root package name */
    private final HashSet<String> f9611b = new HashSet<>();

    /* renamed from: o, reason: collision with root package name */
    private int f9624o = 0;

    /* renamed from: p, reason: collision with root package name */
    private float f9625p = 3.0f;

    public void a(String str) {
        s4.e.c(str);
        this.f9611b.add(str);
    }

    public Rect b() {
        return this.f9619j;
    }

    public SparseArrayCompat<FontCharacter> c() {
        return this.f9616g;
    }

    public float d() {
        return this.f9625p;
    }

    public float e() {
        return (f() / this.f9622m) * 1000.0f;
    }

    public float f() {
        return this.f9621l - this.f9620k;
    }

    public float g() {
        return this.f9621l;
    }

    public Map<String, Font> h() {
        return this.f9614e;
    }

    public float i(float f10) {
        return MiscUtils.k(this.f9620k, this.f9621l, f10);
    }

    public float j() {
        return this.f9622m;
    }

    public Map<String, EffectiveImageAsset> k() {
        return this.f9613d;
    }

    public List<o4.e> l() {
        return this.f9618i;
    }

    public Marker m(String str) {
        int size = this.f9615f.size();
        for (int i10 = 0; i10 < size; i10++) {
            Marker marker = this.f9615f.get(i10);
            if (marker.a(str)) {
                return marker;
            }
        }
        return null;
    }

    public int n() {
        return this.f9624o;
    }

    public PerformanceTracker o() {
        return this.f9610a;
    }

    public List<o4.e> p(String str) {
        return this.f9612c.get(str);
    }

    public float q() {
        return this.f9620k;
    }

    public boolean r() {
        return this.f9623n;
    }

    public void s(int i10) {
        this.f9624o += i10;
    }

    public void t(Rect rect, float f10, float f11, float f12, List<o4.e> list, LongSparseArray<o4.e> longSparseArray, Map<String, List<o4.e>> map, Map<String, EffectiveImageAsset> map2, SparseArrayCompat<FontCharacter> sparseArrayCompat, Map<String, Font> map3, List<Marker> list2, float f13) {
        this.f9619j = rect;
        this.f9620k = f10;
        this.f9621l = f11;
        this.f9622m = f12;
        this.f9618i = list;
        this.f9617h = longSparseArray;
        this.f9612c = map;
        this.f9613d = map2;
        this.f9616g = sparseArrayCompat;
        this.f9614e = map3;
        this.f9615f = list2;
        this.f9625p = f13;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder("EffectiveAnimationComposition:\n");
        Iterator<o4.e> it = this.f9618i.iterator();
        while (it.hasNext()) {
            sb2.append(it.next().y("\t"));
        }
        return sb2.toString();
    }

    public o4.e u(long j10) {
        return this.f9617h.e(j10);
    }

    public void v(boolean z10) {
        this.f9623n = z10;
    }

    public void w(boolean z10) {
        this.f9610a.b(z10);
    }
}
