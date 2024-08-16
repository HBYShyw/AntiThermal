package k4;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.oplus.anim.FontAssetDelegate;
import java.util.HashMap;
import java.util.Map;
import l4.MutablePair;
import s4.e;

/* compiled from: FontAssetManager.java */
/* renamed from: k4.a, reason: use source file name */
/* loaded from: classes.dex */
public class FontAssetManager {

    /* renamed from: d, reason: collision with root package name */
    private final AssetManager f14038d;

    /* renamed from: a, reason: collision with root package name */
    private final MutablePair<String> f14035a = new MutablePair<>();

    /* renamed from: b, reason: collision with root package name */
    private final Map<MutablePair<String>, Typeface> f14036b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    private final Map<String, Typeface> f14037c = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    private String f14039e = ".ttf";

    public FontAssetManager(Drawable.Callback callback, FontAssetDelegate fontAssetDelegate) {
        if (!(callback instanceof View)) {
            e.c("EffectiveAnimationDrawable must be inside of a view for images to work.");
            this.f14038d = null;
        } else {
            this.f14038d = ((View) callback).getContext().getAssets();
        }
    }

    private Typeface a(String str) {
        Typeface typeface = this.f14037c.get(str);
        if (typeface != null) {
            return typeface;
        }
        Typeface createFromAsset = Typeface.createFromAsset(this.f14038d, "fonts/" + str + this.f14039e);
        this.f14037c.put(str, createFromAsset);
        return createFromAsset;
    }

    private Typeface d(Typeface typeface, String str) {
        boolean contains = str.contains("Italic");
        boolean contains2 = str.contains("Bold");
        int i10 = (contains && contains2) ? 3 : contains ? 2 : contains2 ? 1 : 0;
        return typeface.getStyle() == i10 ? typeface : Typeface.create(typeface, i10);
    }

    public Typeface b(String str, String str2) {
        this.f14035a.b(str, str2);
        Typeface typeface = this.f14036b.get(this.f14035a);
        if (typeface != null) {
            return typeface;
        }
        Typeface d10 = d(a(str), str2);
        this.f14036b.put(this.f14035a, d10);
        return d10;
    }

    public void c(FontAssetDelegate fontAssetDelegate) {
    }
}
