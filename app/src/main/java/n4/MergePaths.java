package n4;

import com.oplus.anim.EffectiveAnimationDrawable;
import i4.Content;
import i4.MergePathsContent;
import o4.BaseLayer;

/* compiled from: MergePaths.java */
/* renamed from: n4.i, reason: use source file name */
/* loaded from: classes.dex */
public class MergePaths implements ContentModel {

    /* renamed from: a, reason: collision with root package name */
    private final String f15795a;

    /* renamed from: b, reason: collision with root package name */
    private final a f15796b;

    /* renamed from: c, reason: collision with root package name */
    private final boolean f15797c;

    /* compiled from: MergePaths.java */
    /* renamed from: n4.i$a */
    /* loaded from: classes.dex */
    public enum a {
        MERGE,
        ADD,
        SUBTRACT,
        INTERSECT,
        EXCLUDE_INTERSECTIONS;

        public static a a(int i10) {
            if (i10 == 1) {
                return MERGE;
            }
            if (i10 == 2) {
                return ADD;
            }
            if (i10 == 3) {
                return SUBTRACT;
            }
            if (i10 == 4) {
                return INTERSECT;
            }
            if (i10 != 5) {
                return MERGE;
            }
            return EXCLUDE_INTERSECTIONS;
        }
    }

    public MergePaths(String str, a aVar, boolean z10) {
        this.f15795a = str;
        this.f15796b = aVar;
        this.f15797c = z10;
    }

    @Override // n4.ContentModel
    public Content a(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer) {
        if (!effectiveAnimationDrawable.o()) {
            s4.e.c("Animation contains merge paths but they are disabled.");
            return null;
        }
        return new MergePathsContent(this);
    }

    public a b() {
        return this.f15796b;
    }

    public String c() {
        return this.f15795a;
    }

    public boolean d() {
        return this.f15797c;
    }

    public String toString() {
        return "MergePaths{mode=" + this.f15796b + '}';
    }
}
