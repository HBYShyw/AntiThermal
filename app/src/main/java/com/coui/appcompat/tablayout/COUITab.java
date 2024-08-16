package com.coui.appcompat.tablayout;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.ResourcesCompat;

/* compiled from: COUITab.java */
/* renamed from: com.coui.appcompat.tablayout.c, reason: use source file name */
/* loaded from: classes.dex */
public class COUITab {

    /* renamed from: a, reason: collision with root package name */
    COUITabLayout f7839a;

    /* renamed from: b, reason: collision with root package name */
    COUITabView f7840b;

    /* renamed from: c, reason: collision with root package name */
    private Object f7841c;

    /* renamed from: d, reason: collision with root package name */
    private Drawable f7842d;

    /* renamed from: e, reason: collision with root package name */
    private CharSequence f7843e;

    /* renamed from: f, reason: collision with root package name */
    private CharSequence f7844f;

    /* renamed from: h, reason: collision with root package name */
    private View f7846h;

    /* renamed from: g, reason: collision with root package name */
    private int f7845g = -1;

    /* renamed from: i, reason: collision with root package name */
    private boolean f7847i = true;

    public CharSequence a() {
        return this.f7844f;
    }

    public View b() {
        return this.f7846h;
    }

    public Drawable c() {
        return this.f7842d;
    }

    public int d() {
        return this.f7845g;
    }

    public CharSequence e() {
        return this.f7843e;
    }

    public boolean f() {
        COUITabLayout cOUITabLayout = this.f7839a;
        if (cOUITabLayout != null) {
            return cOUITabLayout.getSelectedTabPosition() == this.f7845g;
        }
        throw new IllegalArgumentException("Tab not attached to a COUITabLayout");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g() {
        this.f7839a = null;
        this.f7840b = null;
        this.f7841c = null;
        this.f7842d = null;
        this.f7843e = null;
        this.f7844f = null;
        this.f7845g = -1;
        this.f7846h = null;
    }

    public void h() {
        COUITabLayout cOUITabLayout = this.f7839a;
        if (cOUITabLayout != null) {
            cOUITabLayout.a0(this);
            return;
        }
        throw new IllegalArgumentException("Tab not attached to a COUITabLayout");
    }

    public COUITab i(CharSequence charSequence) {
        this.f7844f = charSequence;
        o();
        return this;
    }

    public COUITab j(int i10) {
        COUITabLayout cOUITabLayout = this.f7839a;
        if (cOUITabLayout != null) {
            this.f7846h = LayoutInflater.from(cOUITabLayout.getContext()).inflate(i10, (ViewGroup) this.f7839a, false);
            return this;
        }
        throw new IllegalArgumentException("Tab not attached to a COUITabLayout");
    }

    public COUITab k(int i10) {
        COUITabLayout cOUITabLayout = this.f7839a;
        if (cOUITabLayout != null) {
            return l(ResourcesCompat.f(cOUITabLayout.getResources(), i10, null));
        }
        throw new IllegalArgumentException("Tab not attached to a TabLayout");
    }

    public COUITab l(Drawable drawable) {
        this.f7842d = drawable;
        o();
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(int i10) {
        this.f7845g = i10;
    }

    public COUITab n(CharSequence charSequence) {
        this.f7843e = charSequence;
        o();
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o() {
        COUITabView cOUITabView = this.f7840b;
        if (cOUITabView != null) {
            cOUITabView.e();
        }
    }
}
