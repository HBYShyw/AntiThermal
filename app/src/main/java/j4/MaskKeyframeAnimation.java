package j4;

import android.graphics.Path;
import java.util.ArrayList;
import java.util.List;
import n4.Mask;
import n4.ShapeData;

/* compiled from: MaskKeyframeAnimation.java */
/* renamed from: j4.h, reason: use source file name */
/* loaded from: classes.dex */
public class MaskKeyframeAnimation {

    /* renamed from: a, reason: collision with root package name */
    private final List<BaseKeyframeAnimation<ShapeData, Path>> f12974a;

    /* renamed from: b, reason: collision with root package name */
    private final List<BaseKeyframeAnimation<Integer, Integer>> f12975b;

    /* renamed from: c, reason: collision with root package name */
    private final List<Mask> f12976c;

    public MaskKeyframeAnimation(List<Mask> list) {
        this.f12976c = list;
        this.f12974a = new ArrayList(list.size());
        this.f12975b = new ArrayList(list.size());
        for (int i10 = 0; i10 < list.size(); i10++) {
            this.f12974a.add(list.get(i10).b().a());
            this.f12975b.add(list.get(i10).c().a());
        }
    }

    public List<BaseKeyframeAnimation<ShapeData, Path>> a() {
        return this.f12974a;
    }

    public List<Mask> b() {
        return this.f12976c;
    }

    public List<BaseKeyframeAnimation<Integer, Integer>> c() {
        return this.f12975b;
    }
}
