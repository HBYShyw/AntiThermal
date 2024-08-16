package k4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import com.oplus.anim.EffectiveImageAsset;
import com.oplus.anim.ImageAssetDelegate;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import s4.e;
import s4.h;

/* compiled from: ImageAssetManager.java */
/* renamed from: k4.b, reason: use source file name */
/* loaded from: classes.dex */
public class ImageAssetManager {

    /* renamed from: e, reason: collision with root package name */
    private static final Object f14040e = new Object();

    /* renamed from: a, reason: collision with root package name */
    private final Context f14041a;

    /* renamed from: b, reason: collision with root package name */
    private final String f14042b;

    /* renamed from: c, reason: collision with root package name */
    private ImageAssetDelegate f14043c;

    /* renamed from: d, reason: collision with root package name */
    private final Map<String, EffectiveImageAsset> f14044d;

    public ImageAssetManager(Drawable.Callback callback, String str, ImageAssetDelegate imageAssetDelegate, Map<String, EffectiveImageAsset> map) {
        if (!TextUtils.isEmpty(str) && str.charAt(str.length() - 1) != '/') {
            this.f14042b = str + '/';
        } else {
            this.f14042b = str;
        }
        if (!(callback instanceof View)) {
            e.c("EffectiveAnimationDrawable must be inside of a view for images to work.");
            this.f14044d = new HashMap();
            this.f14041a = null;
        } else {
            this.f14041a = ((View) callback).getContext();
            this.f14044d = map;
            d(imageAssetDelegate);
        }
    }

    private Bitmap c(String str, Bitmap bitmap) {
        synchronized (f14040e) {
            e.c("putBitmap key = " + str);
            this.f14044d.get(str).f(bitmap);
        }
        return bitmap;
    }

    public Bitmap a(String str) {
        EffectiveImageAsset effectiveImageAsset = this.f14044d.get(str);
        if (effectiveImageAsset == null) {
            return null;
        }
        Bitmap a10 = effectiveImageAsset.a();
        if (a10 != null) {
            return a10;
        }
        ImageAssetDelegate imageAssetDelegate = this.f14043c;
        if (imageAssetDelegate != null) {
            Bitmap a11 = imageAssetDelegate.a(effectiveImageAsset);
            if (a11 != null) {
                c(str, a11);
            }
            return a11;
        }
        String b10 = effectiveImageAsset.b();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inDensity = 160;
        if (b10.startsWith("data:") && b10.indexOf("base64,") > 0) {
            try {
                byte[] decode = Base64.decode(b10.substring(b10.indexOf(44) + 1), 0);
                return c(str, BitmapFactory.decodeByteArray(decode, 0, decode.length, options));
            } catch (IllegalArgumentException e10) {
                e.d("data URL did not have correct base64 format.", e10);
                return null;
            }
        }
        try {
            if (!TextUtils.isEmpty(this.f14042b)) {
                e.c("bitmapForId filename = " + b10 + ";imagesFolder = " + this.f14042b);
                try {
                    return c(str, h.m(BitmapFactory.decodeStream(this.f14041a.getAssets().open(this.f14042b + b10), null, options), effectiveImageAsset.e(), effectiveImageAsset.c()));
                } catch (IllegalArgumentException e11) {
                    e.d("Unable to decode image.", e11);
                    return null;
                }
            }
            throw new IllegalStateException("You must set an images folder before loading an image. Set it with EffectiveAnimationComposition#setImagesFolder or EffectiveAnimationDrawable#setImagesFolder");
        } catch (IOException e12) {
            e.d("Unable to open asset.", e12);
            return null;
        }
    }

    public boolean b(Context context) {
        return (context == null && this.f14041a == null) || this.f14041a.equals(context);
    }

    public void d(ImageAssetDelegate imageAssetDelegate) {
        this.f14043c = imageAssetDelegate;
    }
}
