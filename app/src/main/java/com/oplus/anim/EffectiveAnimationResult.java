package com.oplus.anim;

import java.util.Arrays;

/* compiled from: EffectiveAnimationResult.java */
/* renamed from: com.oplus.anim.e, reason: use source file name */
/* loaded from: classes.dex */
public final class EffectiveAnimationResult<V> {

    /* renamed from: a, reason: collision with root package name */
    private final V f9701a;

    /* renamed from: b, reason: collision with root package name */
    private final Throwable f9702b;

    public EffectiveAnimationResult(V v7) {
        this.f9701a = v7;
        this.f9702b = null;
    }

    public Throwable a() {
        return this.f9702b;
    }

    public V b() {
        return this.f9701a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EffectiveAnimationResult)) {
            return false;
        }
        EffectiveAnimationResult effectiveAnimationResult = (EffectiveAnimationResult) obj;
        if (b() != null && b().equals(effectiveAnimationResult.b())) {
            return true;
        }
        if (a() == null || effectiveAnimationResult.a() == null) {
            return false;
        }
        return a().toString().equals(a().toString());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{b(), a()});
    }

    public EffectiveAnimationResult(Throwable th) {
        this.f9702b = th;
        this.f9701a = null;
    }
}
