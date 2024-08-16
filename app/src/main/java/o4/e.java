package o4;

import com.oplus.anim.EffectiveAnimationComposition;
import java.util.List;
import java.util.Locale;
import m4.AnimatableFloatValue;
import m4.AnimatableTextFrame;
import m4.AnimatableTextProperties;
import m4.AnimatableTransform;
import n4.BlurEffect;
import n4.ContentModel;
import n4.Mask;
import q4.DropShadowEffect;
import t4.Keyframe;

/* compiled from: Layer.java */
/* loaded from: classes.dex */
public class e {

    /* renamed from: a, reason: collision with root package name */
    private final List<ContentModel> f16215a;

    /* renamed from: b, reason: collision with root package name */
    private final EffectiveAnimationComposition f16216b;

    /* renamed from: c, reason: collision with root package name */
    private final String f16217c;

    /* renamed from: d, reason: collision with root package name */
    private final long f16218d;

    /* renamed from: e, reason: collision with root package name */
    private final a f16219e;

    /* renamed from: f, reason: collision with root package name */
    private final long f16220f;

    /* renamed from: g, reason: collision with root package name */
    private final String f16221g;

    /* renamed from: h, reason: collision with root package name */
    private final List<Mask> f16222h;

    /* renamed from: i, reason: collision with root package name */
    private final AnimatableTransform f16223i;

    /* renamed from: j, reason: collision with root package name */
    private final int f16224j;

    /* renamed from: k, reason: collision with root package name */
    private final int f16225k;

    /* renamed from: l, reason: collision with root package name */
    private final int f16226l;

    /* renamed from: m, reason: collision with root package name */
    private final float f16227m;

    /* renamed from: n, reason: collision with root package name */
    private final float f16228n;

    /* renamed from: o, reason: collision with root package name */
    private final int f16229o;

    /* renamed from: p, reason: collision with root package name */
    private final int f16230p;

    /* renamed from: q, reason: collision with root package name */
    private final AnimatableTextFrame f16231q;

    /* renamed from: r, reason: collision with root package name */
    private final AnimatableTextProperties f16232r;

    /* renamed from: s, reason: collision with root package name */
    private final AnimatableFloatValue f16233s;

    /* renamed from: t, reason: collision with root package name */
    private final List<Keyframe<Float>> f16234t;

    /* renamed from: u, reason: collision with root package name */
    private final b f16235u;

    /* renamed from: v, reason: collision with root package name */
    private final boolean f16236v;

    /* renamed from: w, reason: collision with root package name */
    private final BlurEffect f16237w;

    /* renamed from: x, reason: collision with root package name */
    private final DropShadowEffect f16238x;

    /* compiled from: Layer.java */
    /* loaded from: classes.dex */
    public enum a {
        PRE_COMP,
        SOLID,
        IMAGE,
        NULL,
        SHAPE,
        TEXT,
        UNKNOWN
    }

    /* compiled from: Layer.java */
    /* loaded from: classes.dex */
    public enum b {
        NONE,
        ADD,
        INVERT,
        LUMA,
        LUMA_INVERTED,
        UNKNOWN
    }

    public e(List<ContentModel> list, EffectiveAnimationComposition effectiveAnimationComposition, String str, long j10, a aVar, long j11, String str2, List<Mask> list2, AnimatableTransform animatableTransform, int i10, int i11, int i12, float f10, float f11, int i13, int i14, AnimatableTextFrame animatableTextFrame, AnimatableTextProperties animatableTextProperties, List<Keyframe<Float>> list3, b bVar, AnimatableFloatValue animatableFloatValue, boolean z10, BlurEffect blurEffect, DropShadowEffect dropShadowEffect) {
        this.f16215a = list;
        this.f16216b = effectiveAnimationComposition;
        this.f16217c = str;
        this.f16218d = j10;
        this.f16219e = aVar;
        this.f16220f = j11;
        this.f16221g = str2;
        this.f16222h = list2;
        this.f16223i = animatableTransform;
        this.f16224j = i10;
        this.f16225k = i11;
        this.f16226l = i12;
        this.f16227m = f10;
        this.f16228n = f11;
        this.f16229o = i13;
        this.f16230p = i14;
        this.f16231q = animatableTextFrame;
        this.f16232r = animatableTextProperties;
        this.f16234t = list3;
        this.f16235u = bVar;
        this.f16233s = animatableFloatValue;
        this.f16236v = z10;
        this.f16237w = blurEffect;
        this.f16238x = dropShadowEffect;
    }

    public BlurEffect a() {
        return this.f16237w;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EffectiveAnimationComposition b() {
        return this.f16216b;
    }

    public DropShadowEffect c() {
        return this.f16238x;
    }

    public long d() {
        return this.f16218d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Keyframe<Float>> e() {
        return this.f16234t;
    }

    public a f() {
        return this.f16219e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Mask> g() {
        return this.f16222h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b h() {
        return this.f16235u;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String i() {
        return this.f16217c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long j() {
        return this.f16220f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int k() {
        return this.f16230p;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int l() {
        return this.f16229o;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String m() {
        return this.f16221g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<ContentModel> n() {
        return this.f16215a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int o() {
        return this.f16226l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int p() {
        return this.f16225k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int q() {
        return this.f16224j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float r() {
        return this.f16228n / this.f16216b.f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnimatableTextFrame s() {
        return this.f16231q;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnimatableTextProperties t() {
        return this.f16232r;
    }

    public String toString() {
        return y("");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnimatableFloatValue u() {
        return this.f16233s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float v() {
        return this.f16227m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnimatableTransform w() {
        return this.f16223i;
    }

    public boolean x() {
        return this.f16236v;
    }

    public String y(String str) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(i());
        sb2.append("\n");
        e u7 = this.f16216b.u(j());
        if (u7 != null) {
            sb2.append("\t\tParents: ");
            sb2.append(u7.i());
            e u10 = this.f16216b.u(u7.j());
            while (u10 != null) {
                sb2.append("->");
                sb2.append(u10.i());
                u10 = this.f16216b.u(u10.j());
            }
            sb2.append(str);
            sb2.append("\n");
        }
        if (!g().isEmpty()) {
            sb2.append(str);
            sb2.append("\tMasks: ");
            sb2.append(g().size());
            sb2.append("\n");
        }
        if (q() != 0 && p() != 0) {
            sb2.append(str);
            sb2.append("\tBackground: ");
            sb2.append(String.format(Locale.US, "%dx%d %X\n", Integer.valueOf(q()), Integer.valueOf(p()), Integer.valueOf(o())));
        }
        if (!this.f16215a.isEmpty()) {
            sb2.append(str);
            sb2.append("\tShapes:\n");
            for (ContentModel contentModel : this.f16215a) {
                sb2.append(str);
                sb2.append("\t\t");
                sb2.append(contentModel);
                sb2.append("\n");
            }
        }
        return sb2.toString();
    }
}
