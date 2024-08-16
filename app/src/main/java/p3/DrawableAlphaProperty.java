package p3;

import android.graphics.drawable.Drawable;
import android.util.Property;
import java.util.WeakHashMap;

/* compiled from: DrawableAlphaProperty.java */
/* renamed from: p3.e, reason: use source file name */
/* loaded from: classes.dex */
public class DrawableAlphaProperty extends Property<Drawable, Integer> {

    /* renamed from: b, reason: collision with root package name */
    public static final Property<Drawable, Integer> f16562b = new DrawableAlphaProperty();

    /* renamed from: a, reason: collision with root package name */
    private final WeakHashMap<Drawable, Integer> f16563a;

    private DrawableAlphaProperty() {
        super(Integer.class, "drawableAlphaCompat");
        this.f16563a = new WeakHashMap<>();
    }

    @Override // android.util.Property
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public Integer get(Drawable drawable) {
        return Integer.valueOf(drawable.getAlpha());
    }

    @Override // android.util.Property
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public void set(Drawable drawable, Integer num) {
        drawable.setAlpha(num.intValue());
    }
}
