package androidx.core.view.accessibility;

import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.View;

/* compiled from: AccessibilityClickableSpanCompat.java */
/* renamed from: androidx.core.view.accessibility.a, reason: use source file name */
/* loaded from: classes.dex */
public final class AccessibilityClickableSpanCompat extends ClickableSpan {

    /* renamed from: e, reason: collision with root package name */
    private final int f2311e;

    /* renamed from: f, reason: collision with root package name */
    private final AccessibilityNodeInfoCompat f2312f;

    /* renamed from: g, reason: collision with root package name */
    private final int f2313g;

    public AccessibilityClickableSpanCompat(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, int i11) {
        this.f2311e = i10;
        this.f2312f = accessibilityNodeInfoCompat;
        this.f2313g = i11;
    }

    @Override // android.text.style.ClickableSpan
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("ACCESSIBILITY_CLICKABLE_SPAN_ID", this.f2311e);
        this.f2312f.M(this.f2313g, bundle);
    }
}
