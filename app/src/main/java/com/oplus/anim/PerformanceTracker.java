package com.oplus.anim;

import androidx.core.util.Pair;
import j.ArraySet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import s4.MeanCalculator;

/* compiled from: PerformanceTracker.java */
/* renamed from: com.oplus.anim.n, reason: use source file name */
/* loaded from: classes.dex */
public class PerformanceTracker {

    /* renamed from: a, reason: collision with root package name */
    private boolean f9744a = false;

    /* renamed from: b, reason: collision with root package name */
    private final Set<b> f9745b = new ArraySet();

    /* renamed from: c, reason: collision with root package name */
    private final Map<String, MeanCalculator> f9746c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    private final Comparator<Pair<String, Float>> f9747d = new a();

    /* compiled from: PerformanceTracker.java */
    /* renamed from: com.oplus.anim.n$a */
    /* loaded from: classes.dex */
    class a implements Comparator<Pair<String, Float>> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(Pair<String, Float> pair, Pair<String, Float> pair2) {
            float floatValue = pair.f2306b.floatValue();
            float floatValue2 = pair2.f2306b.floatValue();
            if (floatValue2 > floatValue) {
                return 1;
            }
            return floatValue > floatValue2 ? -1 : 0;
        }
    }

    /* compiled from: PerformanceTracker.java */
    /* renamed from: com.oplus.anim.n$b */
    /* loaded from: classes.dex */
    public interface b {
        void a(float f10);
    }

    public void a(String str, float f10) {
        if (this.f9744a) {
            MeanCalculator meanCalculator = this.f9746c.get(str);
            if (meanCalculator == null) {
                meanCalculator = new MeanCalculator();
                this.f9746c.put(str, meanCalculator);
            }
            meanCalculator.a(f10);
            if (str.equals("__container")) {
                Iterator<b> it = this.f9745b.iterator();
                while (it.hasNext()) {
                    it.next().a(f10);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(boolean z10) {
        this.f9744a = z10;
    }
}
