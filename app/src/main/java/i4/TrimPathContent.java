package i4;

import j4.BaseKeyframeAnimation;
import java.util.ArrayList;
import java.util.List;
import n4.ShapeTrimPath;
import o4.BaseLayer;

/* compiled from: TrimPathContent.java */
/* renamed from: i4.s, reason: use source file name */
/* loaded from: classes.dex */
public class TrimPathContent implements Content, BaseKeyframeAnimation.b {

    /* renamed from: a, reason: collision with root package name */
    private final String f12637a;

    /* renamed from: b, reason: collision with root package name */
    private final boolean f12638b;

    /* renamed from: c, reason: collision with root package name */
    private final List<BaseKeyframeAnimation.b> f12639c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    private final ShapeTrimPath.a f12640d;

    /* renamed from: e, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, Float> f12641e;

    /* renamed from: f, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, Float> f12642f;

    /* renamed from: g, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, Float> f12643g;

    public TrimPathContent(BaseLayer baseLayer, ShapeTrimPath shapeTrimPath) {
        this.f12637a = shapeTrimPath.c();
        this.f12638b = shapeTrimPath.g();
        this.f12640d = shapeTrimPath.f();
        BaseKeyframeAnimation<Float, Float> a10 = shapeTrimPath.e().a();
        this.f12641e = a10;
        BaseKeyframeAnimation<Float, Float> a11 = shapeTrimPath.b().a();
        this.f12642f = a11;
        BaseKeyframeAnimation<Float, Float> a12 = shapeTrimPath.d().a();
        this.f12643g = a12;
        baseLayer.h(a10);
        baseLayer.h(a11);
        baseLayer.h(a12);
        a10.a(this);
        a11.a(this);
        a12.a(this);
    }

    @Override // j4.BaseKeyframeAnimation.b
    public void a() {
        for (int i10 = 0; i10 < this.f12639c.size(); i10++) {
            this.f12639c.get(i10).a();
        }
    }

    @Override // i4.Content
    public void b(List<Content> list, List<Content> list2) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(BaseKeyframeAnimation.b bVar) {
        this.f12639c.add(bVar);
    }

    public BaseKeyframeAnimation<?, Float> d() {
        return this.f12642f;
    }

    public BaseKeyframeAnimation<?, Float> f() {
        return this.f12643g;
    }

    public BaseKeyframeAnimation<?, Float> h() {
        return this.f12641e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShapeTrimPath.a i() {
        return this.f12640d;
    }

    public boolean j() {
        return this.f12638b;
    }
}
