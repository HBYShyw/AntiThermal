package com.oplus.anim;

import android.graphics.Bitmap;

/* compiled from: EffectiveImageAsset.java */
/* renamed from: com.oplus.anim.h, reason: use source file name */
/* loaded from: classes.dex */
public class EffectiveImageAsset {

    /* renamed from: a, reason: collision with root package name */
    private final int f9727a;

    /* renamed from: b, reason: collision with root package name */
    private final int f9728b;

    /* renamed from: c, reason: collision with root package name */
    private final String f9729c;

    /* renamed from: d, reason: collision with root package name */
    private final String f9730d;

    /* renamed from: e, reason: collision with root package name */
    private final String f9731e;

    /* renamed from: f, reason: collision with root package name */
    private Bitmap f9732f;

    public EffectiveImageAsset(int i10, int i11, String str, String str2, String str3) {
        this.f9727a = i10;
        this.f9728b = i11;
        this.f9729c = str;
        this.f9730d = str2;
        this.f9731e = str3;
    }

    public Bitmap a() {
        return this.f9732f;
    }

    public String b() {
        return this.f9730d;
    }

    public int c() {
        return this.f9728b;
    }

    public String d() {
        return this.f9729c;
    }

    public int e() {
        return this.f9727a;
    }

    public void f(Bitmap bitmap) {
        Bitmap bitmap2 = this.f9732f;
        if (bitmap2 != null && bitmap == null) {
            bitmap2.recycle();
        }
        this.f9732f = bitmap;
    }
}
