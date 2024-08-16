package j4;

import android.graphics.Path;
import java.util.List;
import n4.ShapeData;
import s4.MiscUtils;
import t4.Keyframe;

/* compiled from: ShapeKeyframeAnimation.java */
/* renamed from: j4.m, reason: use source file name */
/* loaded from: classes.dex */
public class ShapeKeyframeAnimation extends BaseKeyframeAnimation<ShapeData, Path> {

    /* renamed from: i, reason: collision with root package name */
    private final ShapeData f12985i;

    /* renamed from: j, reason: collision with root package name */
    private final Path f12986j;

    public ShapeKeyframeAnimation(List<Keyframe<ShapeData>> list) {
        super(list);
        this.f12985i = new ShapeData();
        this.f12986j = new Path();
    }

    @Override // j4.BaseKeyframeAnimation
    /* renamed from: p, reason: merged with bridge method [inline-methods] */
    public Path i(Keyframe<ShapeData> keyframe, float f10) {
        this.f12985i.c(keyframe.f18570b, keyframe.f18571c, f10);
        MiscUtils.i(this.f12985i, this.f12986j);
        return this.f12986j;
    }
}
