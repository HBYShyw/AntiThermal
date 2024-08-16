package p3;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.Property;
import j.SimpleArrayMap;
import java.util.ArrayList;
import java.util.List;

/* compiled from: MotionSpec.java */
/* renamed from: p3.h, reason: use source file name */
/* loaded from: classes.dex */
public class MotionSpec {

    /* renamed from: a, reason: collision with root package name */
    private final SimpleArrayMap<String, MotionTiming> f16568a = new SimpleArrayMap<>();

    /* renamed from: b, reason: collision with root package name */
    private final SimpleArrayMap<String, PropertyValuesHolder[]> f16569b = new SimpleArrayMap<>();

    private static void a(MotionSpec motionSpec, Animator animator) {
        if (animator instanceof ObjectAnimator) {
            ObjectAnimator objectAnimator = (ObjectAnimator) animator;
            motionSpec.l(objectAnimator.getPropertyName(), objectAnimator.getValues());
            motionSpec.m(objectAnimator.getPropertyName(), MotionTiming.b(objectAnimator));
        } else {
            throw new IllegalArgumentException("Animator must be an ObjectAnimator: " + animator);
        }
    }

    private PropertyValuesHolder[] b(PropertyValuesHolder[] propertyValuesHolderArr) {
        PropertyValuesHolder[] propertyValuesHolderArr2 = new PropertyValuesHolder[propertyValuesHolderArr.length];
        for (int i10 = 0; i10 < propertyValuesHolderArr.length; i10++) {
            propertyValuesHolderArr2[i10] = propertyValuesHolderArr[i10].clone();
        }
        return propertyValuesHolderArr2;
    }

    public static MotionSpec c(Context context, TypedArray typedArray, int i10) {
        int resourceId;
        if (!typedArray.hasValue(i10) || (resourceId = typedArray.getResourceId(i10, 0)) == 0) {
            return null;
        }
        return d(context, resourceId);
    }

    public static MotionSpec d(Context context, int i10) {
        try {
            Animator loadAnimator = AnimatorInflater.loadAnimator(context, i10);
            if (loadAnimator instanceof AnimatorSet) {
                return e(((AnimatorSet) loadAnimator).getChildAnimations());
            }
            if (loadAnimator == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(loadAnimator);
            return e(arrayList);
        } catch (Exception e10) {
            Log.w("MotionSpec", "Can't load animation resource ID #0x" + Integer.toHexString(i10), e10);
            return null;
        }
    }

    private static MotionSpec e(List<Animator> list) {
        MotionSpec motionSpec = new MotionSpec();
        int size = list.size();
        for (int i10 = 0; i10 < size; i10++) {
            a(motionSpec, list.get(i10));
        }
        return motionSpec;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MotionSpec) {
            return this.f16568a.equals(((MotionSpec) obj).f16568a);
        }
        return false;
    }

    public <T> ObjectAnimator f(String str, T t7, Property<T, ?> property) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(t7, g(str));
        ofPropertyValuesHolder.setProperty(property);
        h(str).a(ofPropertyValuesHolder);
        return ofPropertyValuesHolder;
    }

    public PropertyValuesHolder[] g(String str) {
        if (j(str)) {
            return b(this.f16569b.get(str));
        }
        throw new IllegalArgumentException();
    }

    public MotionTiming h(String str) {
        if (k(str)) {
            return this.f16568a.get(str);
        }
        throw new IllegalArgumentException();
    }

    public int hashCode() {
        return this.f16568a.hashCode();
    }

    public long i() {
        int size = this.f16568a.size();
        long j10 = 0;
        for (int i10 = 0; i10 < size; i10++) {
            MotionTiming n10 = this.f16568a.n(i10);
            j10 = Math.max(j10, n10.c() + n10.d());
        }
        return j10;
    }

    public boolean j(String str) {
        return this.f16569b.get(str) != null;
    }

    public boolean k(String str) {
        return this.f16568a.get(str) != null;
    }

    public void l(String str, PropertyValuesHolder[] propertyValuesHolderArr) {
        this.f16569b.put(str, propertyValuesHolderArr);
    }

    public void m(String str, MotionTiming motionTiming) {
        this.f16568a.put(str, motionTiming);
    }

    public String toString() {
        return '\n' + getClass().getName() + '{' + Integer.toHexString(System.identityHashCode(this)) + " timings: " + this.f16568a + "}\n";
    }
}
