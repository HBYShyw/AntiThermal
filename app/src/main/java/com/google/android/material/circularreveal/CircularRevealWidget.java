package com.google.android.material.circularreveal;

import android.animation.TypeEvaluator;
import android.graphics.drawable.Drawable;
import android.util.Property;
import com.google.android.material.circularreveal.CircularRevealHelper;

/* compiled from: CircularRevealWidget.java */
/* renamed from: com.google.android.material.circularreveal.c, reason: use source file name */
/* loaded from: classes.dex */
public interface CircularRevealWidget extends CircularRevealHelper.a {

    /* compiled from: CircularRevealWidget.java */
    /* renamed from: com.google.android.material.circularreveal.c$b */
    /* loaded from: classes.dex */
    public static class b implements TypeEvaluator<e> {

        /* renamed from: b, reason: collision with root package name */
        public static final TypeEvaluator<e> f8631b = new b();

        /* renamed from: a, reason: collision with root package name */
        private final e f8632a = new e();

        @Override // android.animation.TypeEvaluator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public e evaluate(float f10, e eVar, e eVar2) {
            this.f8632a.b(w3.a.d(eVar.f8635a, eVar2.f8635a, f10), w3.a.d(eVar.f8636b, eVar2.f8636b, f10), w3.a.d(eVar.f8637c, eVar2.f8637c, f10));
            return this.f8632a;
        }
    }

    /* compiled from: CircularRevealWidget.java */
    /* renamed from: com.google.android.material.circularreveal.c$c */
    /* loaded from: classes.dex */
    public static class c extends Property<CircularRevealWidget, e> {

        /* renamed from: a, reason: collision with root package name */
        public static final Property<CircularRevealWidget, e> f8633a = new c("circularReveal");

        private c(String str) {
            super(e.class, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public e get(CircularRevealWidget circularRevealWidget) {
            return circularRevealWidget.getRevealInfo();
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(CircularRevealWidget circularRevealWidget, e eVar) {
            circularRevealWidget.setRevealInfo(eVar);
        }
    }

    /* compiled from: CircularRevealWidget.java */
    /* renamed from: com.google.android.material.circularreveal.c$d */
    /* loaded from: classes.dex */
    public static class d extends Property<CircularRevealWidget, Integer> {

        /* renamed from: a, reason: collision with root package name */
        public static final Property<CircularRevealWidget, Integer> f8634a = new d("circularRevealScrimColor");

        private d(String str) {
            super(Integer.class, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Integer get(CircularRevealWidget circularRevealWidget) {
            return Integer.valueOf(circularRevealWidget.getCircularRevealScrimColor());
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(CircularRevealWidget circularRevealWidget, Integer num) {
            circularRevealWidget.setCircularRevealScrimColor(num.intValue());
        }
    }

    /* compiled from: CircularRevealWidget.java */
    /* renamed from: com.google.android.material.circularreveal.c$e */
    /* loaded from: classes.dex */
    public static class e {

        /* renamed from: a, reason: collision with root package name */
        public float f8635a;

        /* renamed from: b, reason: collision with root package name */
        public float f8636b;

        /* renamed from: c, reason: collision with root package name */
        public float f8637c;

        private e() {
        }

        public boolean a() {
            return this.f8637c == Float.MAX_VALUE;
        }

        public void b(float f10, float f11, float f12) {
            this.f8635a = f10;
            this.f8636b = f11;
            this.f8637c = f12;
        }

        public void c(e eVar) {
            b(eVar.f8635a, eVar.f8636b, eVar.f8637c);
        }

        public e(float f10, float f11, float f12) {
            this.f8635a = f10;
            this.f8636b = f11;
            this.f8637c = f12;
        }

        public e(e eVar) {
            this(eVar.f8635a, eVar.f8636b, eVar.f8637c);
        }
    }

    void a();

    void b();

    int getCircularRevealScrimColor();

    e getRevealInfo();

    void setCircularRevealOverlayDrawable(Drawable drawable);

    void setCircularRevealScrimColor(int i10);

    void setRevealInfo(e eVar);
}
