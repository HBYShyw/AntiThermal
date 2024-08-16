package androidx.core.graphics.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

/* loaded from: classes.dex */
public class IconCompat extends CustomVersionedParcelable {

    /* renamed from: k, reason: collision with root package name */
    static final PorterDuff.Mode f2194k = PorterDuff.Mode.SRC_IN;

    /* renamed from: b, reason: collision with root package name */
    Object f2196b;

    /* renamed from: j, reason: collision with root package name */
    public String f2204j;

    /* renamed from: a, reason: collision with root package name */
    public int f2195a = -1;

    /* renamed from: c, reason: collision with root package name */
    public byte[] f2197c = null;

    /* renamed from: d, reason: collision with root package name */
    public Parcelable f2198d = null;

    /* renamed from: e, reason: collision with root package name */
    public int f2199e = 0;

    /* renamed from: f, reason: collision with root package name */
    public int f2200f = 0;

    /* renamed from: g, reason: collision with root package name */
    public ColorStateList f2201g = null;

    /* renamed from: h, reason: collision with root package name */
    PorterDuff.Mode f2202h = f2194k;

    /* renamed from: i, reason: collision with root package name */
    public String f2203i = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class a {
        static int a(Object obj) {
            return c.a(obj);
        }

        static String b(Object obj) {
            return c.b(obj);
        }

        static Uri c(Object obj) {
            return c.d(obj);
        }

        static Drawable d(Icon icon, Context context) {
            return icon.loadDrawable(context);
        }

        static Icon e(IconCompat iconCompat, Context context) {
            Icon createWithBitmap;
            switch (iconCompat.f2195a) {
                case -1:
                    return (Icon) iconCompat.f2196b;
                case 0:
                default:
                    throw new IllegalArgumentException("Unknown type");
                case 1:
                    createWithBitmap = Icon.createWithBitmap((Bitmap) iconCompat.f2196b);
                    break;
                case 2:
                    createWithBitmap = Icon.createWithResource(iconCompat.b(), iconCompat.f2199e);
                    break;
                case 3:
                    createWithBitmap = Icon.createWithData((byte[]) iconCompat.f2196b, iconCompat.f2199e, iconCompat.f2200f);
                    break;
                case 4:
                    createWithBitmap = Icon.createWithContentUri((String) iconCompat.f2196b);
                    break;
                case 5:
                    createWithBitmap = b.b((Bitmap) iconCompat.f2196b);
                    break;
                case 6:
                    createWithBitmap = d.a(iconCompat.c());
                    break;
            }
            ColorStateList colorStateList = iconCompat.f2201g;
            if (colorStateList != null) {
                createWithBitmap.setTintList(colorStateList);
            }
            PorterDuff.Mode mode = iconCompat.f2202h;
            if (mode != IconCompat.f2194k) {
                createWithBitmap.setTintMode(mode);
            }
            return createWithBitmap;
        }
    }

    /* loaded from: classes.dex */
    static class b {
        static Drawable a(Drawable drawable, Drawable drawable2) {
            return new AdaptiveIconDrawable(drawable, drawable2);
        }

        static Icon b(Bitmap bitmap) {
            return Icon.createWithAdaptiveBitmap(bitmap);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class c {
        static int a(Object obj) {
            return ((Icon) obj).getResId();
        }

        static String b(Object obj) {
            return ((Icon) obj).getResPackage();
        }

        static int c(Object obj) {
            return ((Icon) obj).getType();
        }

        static Uri d(Object obj) {
            return ((Icon) obj).getUri();
        }
    }

    /* loaded from: classes.dex */
    static class d {
        static Icon a(Uri uri) {
            return Icon.createWithAdaptiveBitmapContentUri(uri);
        }
    }

    private static String f(int i10) {
        switch (i10) {
            case 1:
                return "BITMAP";
            case 2:
                return "RESOURCE";
            case 3:
                return "DATA";
            case 4:
                return "URI";
            case 5:
                return "BITMAP_MASKABLE";
            case 6:
                return "URI_MASKABLE";
            default:
                return "UNKNOWN";
        }
    }

    public int a() {
        int i10 = this.f2195a;
        if (i10 == -1) {
            return a.a(this.f2196b);
        }
        if (i10 == 2) {
            return this.f2199e;
        }
        throw new IllegalStateException("called getResId() on " + this);
    }

    public String b() {
        int i10 = this.f2195a;
        if (i10 == -1) {
            return a.b(this.f2196b);
        }
        if (i10 == 2) {
            String str = this.f2204j;
            if (str != null && !TextUtils.isEmpty(str)) {
                return this.f2204j;
            }
            return ((String) this.f2196b).split(":", -1)[0];
        }
        throw new IllegalStateException("called getResPackage() on " + this);
    }

    public Uri c() {
        int i10 = this.f2195a;
        if (i10 == -1) {
            return a.c(this.f2196b);
        }
        if (i10 != 4 && i10 != 6) {
            throw new IllegalStateException("called getUri() on " + this);
        }
        return Uri.parse((String) this.f2196b);
    }

    public void d() {
        this.f2202h = PorterDuff.Mode.valueOf(this.f2203i);
        switch (this.f2195a) {
            case -1:
                Parcelable parcelable = this.f2198d;
                if (parcelable != null) {
                    this.f2196b = parcelable;
                    return;
                }
                throw new IllegalArgumentException("Invalid icon");
            case 0:
            default:
                return;
            case 1:
            case 5:
                Parcelable parcelable2 = this.f2198d;
                if (parcelable2 != null) {
                    this.f2196b = parcelable2;
                    return;
                }
                byte[] bArr = this.f2197c;
                this.f2196b = bArr;
                this.f2195a = 3;
                this.f2199e = 0;
                this.f2200f = bArr.length;
                return;
            case 2:
            case 4:
            case 6:
                String str = new String(this.f2197c, Charset.forName("UTF-16"));
                this.f2196b = str;
                if (this.f2195a == 2 && this.f2204j == null) {
                    this.f2204j = str.split(":", -1)[0];
                    return;
                }
                return;
            case 3:
                this.f2196b = this.f2197c;
                return;
        }
    }

    public void e(boolean z10) {
        this.f2203i = this.f2202h.name();
        switch (this.f2195a) {
            case -1:
                if (!z10) {
                    this.f2198d = (Parcelable) this.f2196b;
                    return;
                }
                throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
            case 0:
            default:
                return;
            case 1:
            case 5:
                if (z10) {
                    Bitmap bitmap = (Bitmap) this.f2196b;
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
                    this.f2197c = byteArrayOutputStream.toByteArray();
                    return;
                }
                this.f2198d = (Parcelable) this.f2196b;
                return;
            case 2:
                this.f2197c = ((String) this.f2196b).getBytes(Charset.forName("UTF-16"));
                return;
            case 3:
                this.f2197c = (byte[]) this.f2196b;
                return;
            case 4:
            case 6:
                this.f2197c = this.f2196b.toString().getBytes(Charset.forName("UTF-16"));
                return;
        }
    }

    public String toString() {
        if (this.f2195a == -1) {
            return String.valueOf(this.f2196b);
        }
        StringBuilder sb2 = new StringBuilder("Icon(typ=");
        sb2.append(f(this.f2195a));
        switch (this.f2195a) {
            case 1:
            case 5:
                sb2.append(" size=");
                sb2.append(((Bitmap) this.f2196b).getWidth());
                sb2.append("x");
                sb2.append(((Bitmap) this.f2196b).getHeight());
                break;
            case 2:
                sb2.append(" pkg=");
                sb2.append(this.f2204j);
                sb2.append(" id=");
                sb2.append(String.format("0x%08x", Integer.valueOf(a())));
                break;
            case 3:
                sb2.append(" len=");
                sb2.append(this.f2199e);
                if (this.f2200f != 0) {
                    sb2.append(" off=");
                    sb2.append(this.f2200f);
                    break;
                }
                break;
            case 4:
            case 6:
                sb2.append(" uri=");
                sb2.append(this.f2196b);
                break;
        }
        if (this.f2201g != null) {
            sb2.append(" tint=");
            sb2.append(this.f2201g);
        }
        if (this.f2202h != f2194k) {
            sb2.append(" mode=");
            sb2.append(this.f2202h);
        }
        sb2.append(")");
        return sb2.toString();
    }
}
