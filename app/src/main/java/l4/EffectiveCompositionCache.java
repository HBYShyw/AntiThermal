package l4;

import com.oplus.anim.EffectiveAnimationComposition;
import j.LruCache;

/* compiled from: EffectiveCompositionCache.java */
/* renamed from: l4.c, reason: use source file name */
/* loaded from: classes.dex */
public class EffectiveCompositionCache {

    /* renamed from: b, reason: collision with root package name */
    private static final EffectiveCompositionCache f14610b = new EffectiveCompositionCache();

    /* renamed from: a, reason: collision with root package name */
    private final LruCache<String, EffectiveAnimationComposition> f14611a = new LruCache<>(20);

    EffectiveCompositionCache() {
    }

    public static EffectiveCompositionCache b() {
        return f14610b;
    }

    public EffectiveAnimationComposition a(String str) {
        if (str == null) {
            return null;
        }
        return this.f14611a.c(str);
    }

    public void c(String str, EffectiveAnimationComposition effectiveAnimationComposition) {
        if (str == null) {
            return;
        }
        this.f14611a.d(str, effectiveAnimationComposition);
    }
}
