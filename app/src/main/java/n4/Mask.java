package n4;

import m4.AnimatableIntegerValue;
import m4.AnimatableShapeValue;

/* compiled from: Mask.java */
/* renamed from: n4.h, reason: use source file name */
/* loaded from: classes.dex */
public class Mask {

    /* renamed from: a, reason: collision with root package name */
    private final a f15786a;

    /* renamed from: b, reason: collision with root package name */
    private final AnimatableShapeValue f15787b;

    /* renamed from: c, reason: collision with root package name */
    private final AnimatableIntegerValue f15788c;

    /* renamed from: d, reason: collision with root package name */
    private final boolean f15789d;

    /* compiled from: Mask.java */
    /* renamed from: n4.h$a */
    /* loaded from: classes.dex */
    public enum a {
        MASK_MODE_ADD,
        MASK_MODE_SUBTRACT,
        MASK_MODE_INTERSECT,
        MASK_MODE_NONE
    }

    public Mask(a aVar, AnimatableShapeValue animatableShapeValue, AnimatableIntegerValue animatableIntegerValue, boolean z10) {
        this.f15786a = aVar;
        this.f15787b = animatableShapeValue;
        this.f15788c = animatableIntegerValue;
        this.f15789d = z10;
    }

    public a a() {
        return this.f15786a;
    }

    public AnimatableShapeValue b() {
        return this.f15787b;
    }

    public AnimatableIntegerValue c() {
        return this.f15788c;
    }

    public boolean d() {
        return this.f15789d;
    }
}
