package j4;

import android.graphics.Path;
import android.graphics.PointF;
import com.oplus.anim.EffectiveAnimationComposition;
import t4.Keyframe;

/* compiled from: PathKeyframe.java */
/* renamed from: j4.i, reason: use source file name */
/* loaded from: classes.dex */
public class PathKeyframe extends Keyframe<PointF> {

    /* renamed from: q, reason: collision with root package name */
    private Path f12977q;

    /* renamed from: r, reason: collision with root package name */
    private final Keyframe<PointF> f12978r;

    public PathKeyframe(EffectiveAnimationComposition effectiveAnimationComposition, Keyframe<PointF> keyframe) {
        super(effectiveAnimationComposition, keyframe.f18570b, keyframe.f18571c, keyframe.f18572d, keyframe.f18573e, keyframe.f18574f, keyframe.f18575g, keyframe.f18576h);
        this.f12978r = keyframe;
        i();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void i() {
        T t7;
        T t10;
        T t11 = this.f18571c;
        boolean z10 = (t11 == 0 || (t10 = this.f18570b) == 0 || !((PointF) t10).equals(((PointF) t11).x, ((PointF) t11).y)) ? false : true;
        T t12 = this.f18570b;
        if (t12 == 0 || (t7 = this.f18571c) == 0 || z10) {
            return;
        }
        Keyframe<PointF> keyframe = this.f12978r;
        this.f12977q = s4.h.d((PointF) t12, (PointF) t7, keyframe.f18583o, keyframe.f18584p);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Path j() {
        return this.f12977q;
    }
}
