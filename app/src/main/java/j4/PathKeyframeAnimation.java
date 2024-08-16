package j4;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import java.util.List;
import t4.EffectiveValueCallback;
import t4.Keyframe;

/* compiled from: PathKeyframeAnimation.java */
/* renamed from: j4.j, reason: use source file name */
/* loaded from: classes.dex */
public class PathKeyframeAnimation extends KeyframeAnimation<PointF> {

    /* renamed from: i, reason: collision with root package name */
    private final PointF f12979i;

    /* renamed from: j, reason: collision with root package name */
    private final float[] f12980j;

    /* renamed from: k, reason: collision with root package name */
    private final PathMeasure f12981k;

    /* renamed from: l, reason: collision with root package name */
    private PathKeyframe f12982l;

    public PathKeyframeAnimation(List<? extends Keyframe<PointF>> list) {
        super(list);
        this.f12979i = new PointF();
        this.f12980j = new float[2];
        this.f12981k = new PathMeasure();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // j4.BaseKeyframeAnimation
    /* renamed from: p, reason: merged with bridge method [inline-methods] */
    public PointF i(Keyframe<PointF> keyframe, float f10) {
        PointF pointF;
        PathKeyframe pathKeyframe = (PathKeyframe) keyframe;
        Path j10 = pathKeyframe.j();
        if (j10 == null) {
            return keyframe.f18570b;
        }
        EffectiveValueCallback<A> effectiveValueCallback = this.f12954e;
        if (effectiveValueCallback != 0 && (pointF = (PointF) effectiveValueCallback.b(pathKeyframe.f18575g, pathKeyframe.f18576h.floatValue(), pathKeyframe.f18570b, pathKeyframe.f18571c, e(), f10, f())) != null) {
            return pointF;
        }
        if (this.f12982l != pathKeyframe) {
            this.f12981k.setPath(j10, false);
            this.f12982l = pathKeyframe;
        }
        PathMeasure pathMeasure = this.f12981k;
        pathMeasure.getPosTan(f10 * pathMeasure.getLength(), this.f12980j, null);
        PointF pointF2 = this.f12979i;
        float[] fArr = this.f12980j;
        pointF2.set(fArr[0], fArr[1]);
        return this.f12979i;
    }
}
