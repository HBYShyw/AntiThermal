package i4;

import android.graphics.Path;
import com.oplus.anim.EffectiveAnimationDrawable;
import j4.BaseKeyframeAnimation;
import java.util.List;
import n4.ShapeData;
import n4.ShapeTrimPath;
import o4.BaseLayer;

/* compiled from: ShapeContent.java */
/* renamed from: i4.q, reason: use source file name */
/* loaded from: classes.dex */
public class ShapeContent implements PathContent, BaseKeyframeAnimation.b {

    /* renamed from: b, reason: collision with root package name */
    private final String f12626b;

    /* renamed from: c, reason: collision with root package name */
    private final boolean f12627c;

    /* renamed from: d, reason: collision with root package name */
    private final EffectiveAnimationDrawable f12628d;

    /* renamed from: e, reason: collision with root package name */
    private final BaseKeyframeAnimation<?, Path> f12629e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f12630f;

    /* renamed from: a, reason: collision with root package name */
    private final Path f12625a = new Path();

    /* renamed from: g, reason: collision with root package name */
    private final CompoundTrimPathContent f12631g = new CompoundTrimPathContent();

    public ShapeContent(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer, n4.p pVar) {
        this.f12626b = pVar.b();
        this.f12627c = pVar.d();
        this.f12628d = effectiveAnimationDrawable;
        BaseKeyframeAnimation<ShapeData, Path> a10 = pVar.c().a();
        this.f12629e = a10;
        baseLayer.h(a10);
        a10.a(this);
    }

    private void c() {
        this.f12630f = false;
        this.f12628d.invalidateSelf();
    }

    @Override // j4.BaseKeyframeAnimation.b
    public void a() {
        c();
    }

    @Override // i4.Content
    public void b(List<Content> list, List<Content> list2) {
        for (int i10 = 0; i10 < list.size(); i10++) {
            Content content = list.get(i10);
            if (content instanceof TrimPathContent) {
                TrimPathContent trimPathContent = (TrimPathContent) content;
                if (trimPathContent.i() == ShapeTrimPath.a.SIMULTANEOUSLY) {
                    this.f12631g.a(trimPathContent);
                    trimPathContent.c(this);
                }
            }
        }
    }

    @Override // i4.PathContent
    public Path getPath() {
        if (this.f12630f) {
            return this.f12625a;
        }
        this.f12625a.reset();
        if (this.f12627c) {
            this.f12630f = true;
            return this.f12625a;
        }
        Path h10 = this.f12629e.h();
        if (h10 == null) {
            return this.f12625a;
        }
        this.f12625a.set(h10);
        this.f12625a.setFillType(Path.FillType.EVEN_ODD);
        this.f12631g.b(this.f12625a);
        this.f12630f = true;
        return this.f12625a;
    }
}
