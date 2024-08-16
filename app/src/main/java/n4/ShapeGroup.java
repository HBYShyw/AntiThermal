package n4;

import com.oplus.anim.EffectiveAnimationDrawable;
import i4.Content;
import i4.ContentGroup;
import java.util.Arrays;
import java.util.List;
import o4.BaseLayer;

/* compiled from: ShapeGroup.java */
/* renamed from: n4.o, reason: use source file name */
/* loaded from: classes.dex */
public class ShapeGroup implements ContentModel {

    /* renamed from: a, reason: collision with root package name */
    private final String f15837a;

    /* renamed from: b, reason: collision with root package name */
    private final List<ContentModel> f15838b;

    /* renamed from: c, reason: collision with root package name */
    private final boolean f15839c;

    public ShapeGroup(String str, List<ContentModel> list, boolean z10) {
        this.f15837a = str;
        this.f15838b = list;
        this.f15839c = z10;
    }

    @Override // n4.ContentModel
    public Content a(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer) {
        return new ContentGroup(effectiveAnimationDrawable, baseLayer, this);
    }

    public List<ContentModel> b() {
        return this.f15838b;
    }

    public String c() {
        return this.f15837a;
    }

    public boolean d() {
        return this.f15839c;
    }

    public String toString() {
        return "ShapeGroup{name='" + this.f15837a + "' Shapes: " + Arrays.toString(this.f15838b.toArray()) + '}';
    }
}
