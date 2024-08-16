package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.core.content.res.ResourcesCompat;
import c.AppCompatResources;

/* compiled from: TintTypedArray.java */
/* renamed from: androidx.appcompat.widget.h0, reason: use source file name */
/* loaded from: classes.dex */
public class TintTypedArray {

    /* renamed from: a, reason: collision with root package name */
    private final Context f1227a;

    /* renamed from: b, reason: collision with root package name */
    private final TypedArray f1228b;

    /* renamed from: c, reason: collision with root package name */
    private TypedValue f1229c;

    private TintTypedArray(Context context, TypedArray typedArray) {
        this.f1227a = context;
        this.f1228b = typedArray;
    }

    public static TintTypedArray u(Context context, int i10, int[] iArr) {
        return new TintTypedArray(context, context.obtainStyledAttributes(i10, iArr));
    }

    public static TintTypedArray v(Context context, AttributeSet attributeSet, int[] iArr) {
        return new TintTypedArray(context, context.obtainStyledAttributes(attributeSet, iArr));
    }

    public static TintTypedArray w(Context context, AttributeSet attributeSet, int[] iArr, int i10, int i11) {
        return new TintTypedArray(context, context.obtainStyledAttributes(attributeSet, iArr, i10, i11));
    }

    public boolean a(int i10, boolean z10) {
        return this.f1228b.getBoolean(i10, z10);
    }

    public int b(int i10, int i11) {
        return this.f1228b.getColor(i10, i11);
    }

    public ColorStateList c(int i10) {
        int resourceId;
        ColorStateList a10;
        return (!this.f1228b.hasValue(i10) || (resourceId = this.f1228b.getResourceId(i10, 0)) == 0 || (a10 = AppCompatResources.a(this.f1227a, resourceId)) == null) ? this.f1228b.getColorStateList(i10) : a10;
    }

    public float d(int i10, float f10) {
        return this.f1228b.getDimension(i10, f10);
    }

    public int e(int i10, int i11) {
        return this.f1228b.getDimensionPixelOffset(i10, i11);
    }

    public int f(int i10, int i11) {
        return this.f1228b.getDimensionPixelSize(i10, i11);
    }

    public Drawable g(int i10) {
        int resourceId;
        if (this.f1228b.hasValue(i10) && (resourceId = this.f1228b.getResourceId(i10, 0)) != 0) {
            return AppCompatResources.b(this.f1227a, resourceId);
        }
        return this.f1228b.getDrawable(i10);
    }

    public Drawable h(int i10) {
        int resourceId;
        if (!this.f1228b.hasValue(i10) || (resourceId = this.f1228b.getResourceId(i10, 0)) == 0) {
            return null;
        }
        return AppCompatDrawableManager.b().d(this.f1227a, resourceId, true);
    }

    public float i(int i10, float f10) {
        return this.f1228b.getFloat(i10, f10);
    }

    public Typeface j(int i10, int i11, ResourcesCompat.e eVar) {
        int resourceId = this.f1228b.getResourceId(i10, 0);
        if (resourceId == 0) {
            return null;
        }
        if (this.f1229c == null) {
            this.f1229c = new TypedValue();
        }
        return ResourcesCompat.i(this.f1227a, resourceId, this.f1229c, i11, eVar);
    }

    public int k(int i10, int i11) {
        return this.f1228b.getInt(i10, i11);
    }

    public int l(int i10, int i11) {
        return this.f1228b.getInteger(i10, i11);
    }

    public int m(int i10, int i11) {
        return this.f1228b.getLayoutDimension(i10, i11);
    }

    public int n(int i10, int i11) {
        return this.f1228b.getResourceId(i10, i11);
    }

    public String o(int i10) {
        return this.f1228b.getString(i10);
    }

    public CharSequence p(int i10) {
        return this.f1228b.getText(i10);
    }

    public CharSequence[] q(int i10) {
        return this.f1228b.getTextArray(i10);
    }

    public TypedArray r() {
        return this.f1228b;
    }

    public boolean s(int i10) {
        return this.f1228b.hasValue(i10);
    }

    public int t() {
        return this.f1228b.length();
    }

    public void x() {
        this.f1228b.recycle();
    }
}
