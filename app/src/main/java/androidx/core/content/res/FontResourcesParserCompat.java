package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Base64;
import android.util.Xml;
import androidx.core.R$styleable;
import androidx.core.provider.FontRequest;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneFlightData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: FontResourcesParserCompat.java */
/* renamed from: androidx.core.content.res.d, reason: use source file name */
/* loaded from: classes.dex */
public class FontResourcesParserCompat {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FontResourcesParserCompat.java */
    /* renamed from: androidx.core.content.res.d$a */
    /* loaded from: classes.dex */
    public static class a {
        static int a(TypedArray typedArray, int i10) {
            return typedArray.getType(i10);
        }
    }

    /* compiled from: FontResourcesParserCompat.java */
    /* renamed from: androidx.core.content.res.d$b */
    /* loaded from: classes.dex */
    public interface b {
    }

    /* compiled from: FontResourcesParserCompat.java */
    /* renamed from: androidx.core.content.res.d$c */
    /* loaded from: classes.dex */
    public static final class c implements b {

        /* renamed from: a, reason: collision with root package name */
        private final d[] f2149a;

        public c(d[] dVarArr) {
            this.f2149a = dVarArr;
        }

        public d[] a() {
            return this.f2149a;
        }
    }

    /* compiled from: FontResourcesParserCompat.java */
    /* renamed from: androidx.core.content.res.d$d */
    /* loaded from: classes.dex */
    public static final class d {

        /* renamed from: a, reason: collision with root package name */
        private final String f2150a;

        /* renamed from: b, reason: collision with root package name */
        private final int f2151b;

        /* renamed from: c, reason: collision with root package name */
        private final boolean f2152c;

        /* renamed from: d, reason: collision with root package name */
        private final String f2153d;

        /* renamed from: e, reason: collision with root package name */
        private final int f2154e;

        /* renamed from: f, reason: collision with root package name */
        private final int f2155f;

        public d(String str, int i10, boolean z10, String str2, int i11, int i12) {
            this.f2150a = str;
            this.f2151b = i10;
            this.f2152c = z10;
            this.f2153d = str2;
            this.f2154e = i11;
            this.f2155f = i12;
        }

        public int a() {
            return this.f2155f;
        }

        public int b() {
            return this.f2154e;
        }

        public String c() {
            return this.f2153d;
        }

        public int d() {
            return this.f2151b;
        }

        public boolean e() {
            return this.f2152c;
        }
    }

    /* compiled from: FontResourcesParserCompat.java */
    /* renamed from: androidx.core.content.res.d$e */
    /* loaded from: classes.dex */
    public static final class e implements b {

        /* renamed from: a, reason: collision with root package name */
        private final FontRequest f2156a;

        /* renamed from: b, reason: collision with root package name */
        private final int f2157b;

        /* renamed from: c, reason: collision with root package name */
        private final int f2158c;

        /* renamed from: d, reason: collision with root package name */
        private final String f2159d;

        public e(FontRequest fontRequest, int i10, int i11, String str) {
            this.f2156a = fontRequest;
            this.f2158c = i10;
            this.f2157b = i11;
            this.f2159d = str;
        }

        public int a() {
            return this.f2158c;
        }

        public FontRequest b() {
            return this.f2156a;
        }

        public String c() {
            return this.f2159d;
        }

        public int d() {
            return this.f2157b;
        }
    }

    private static int a(TypedArray typedArray, int i10) {
        return a.a(typedArray, i10);
    }

    public static b b(XmlPullParser xmlPullParser, Resources resources) {
        int next;
        do {
            next = xmlPullParser.next();
            if (next == 2) {
                break;
            }
        } while (next != 1);
        if (next == 2) {
            return d(xmlPullParser, resources);
        }
        throw new XmlPullParserException("No start tag found");
    }

    public static List<List<byte[]>> c(Resources resources, int i10) {
        if (i10 == 0) {
            return Collections.emptyList();
        }
        TypedArray obtainTypedArray = resources.obtainTypedArray(i10);
        try {
            if (obtainTypedArray.length() == 0) {
                return Collections.emptyList();
            }
            ArrayList arrayList = new ArrayList();
            if (a(obtainTypedArray, 0) == 1) {
                for (int i11 = 0; i11 < obtainTypedArray.length(); i11++) {
                    int resourceId = obtainTypedArray.getResourceId(i11, 0);
                    if (resourceId != 0) {
                        arrayList.add(h(resources.getStringArray(resourceId)));
                    }
                }
            } else {
                arrayList.add(h(resources.getStringArray(i10)));
            }
            return arrayList;
        } finally {
            obtainTypedArray.recycle();
        }
    }

    private static b d(XmlPullParser xmlPullParser, Resources resources) {
        xmlPullParser.require(2, null, "font-family");
        if (xmlPullParser.getName().equals("font-family")) {
            return e(xmlPullParser, resources);
        }
        g(xmlPullParser);
        return null;
    }

    private static b e(XmlPullParser xmlPullParser, Resources resources) {
        TypedArray obtainAttributes = resources.obtainAttributes(Xml.asAttributeSet(xmlPullParser), R$styleable.FontFamily);
        String string = obtainAttributes.getString(R$styleable.FontFamily_fontProviderAuthority);
        String string2 = obtainAttributes.getString(R$styleable.FontFamily_fontProviderPackage);
        String string3 = obtainAttributes.getString(R$styleable.FontFamily_fontProviderQuery);
        int resourceId = obtainAttributes.getResourceId(R$styleable.FontFamily_fontProviderCerts, 0);
        int integer = obtainAttributes.getInteger(R$styleable.FontFamily_fontProviderFetchStrategy, 1);
        int integer2 = obtainAttributes.getInteger(R$styleable.FontFamily_fontProviderFetchTimeout, 500);
        String string4 = obtainAttributes.getString(R$styleable.FontFamily_fontProviderSystemFontFamily);
        obtainAttributes.recycle();
        if (string != null && string2 != null && string3 != null) {
            while (xmlPullParser.next() != 3) {
                g(xmlPullParser);
            }
            return new e(new FontRequest(string, string2, string3, c(resources, resourceId)), integer, integer2, string4);
        }
        ArrayList arrayList = new ArrayList();
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("font")) {
                    arrayList.add(f(xmlPullParser, resources));
                } else {
                    g(xmlPullParser);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return new c((d[]) arrayList.toArray(new d[0]));
    }

    private static d f(XmlPullParser xmlPullParser, Resources resources) {
        TypedArray obtainAttributes = resources.obtainAttributes(Xml.asAttributeSet(xmlPullParser), R$styleable.FontFamilyFont);
        int i10 = R$styleable.FontFamilyFont_fontWeight;
        if (!obtainAttributes.hasValue(i10)) {
            i10 = R$styleable.FontFamilyFont_android_fontWeight;
        }
        int i11 = obtainAttributes.getInt(i10, SceneFlightData.INVALID_LATITUDE_LONGITUDE);
        int i12 = R$styleable.FontFamilyFont_fontStyle;
        if (!obtainAttributes.hasValue(i12)) {
            i12 = R$styleable.FontFamilyFont_android_fontStyle;
        }
        boolean z10 = 1 == obtainAttributes.getInt(i12, 0);
        int i13 = R$styleable.FontFamilyFont_ttcIndex;
        if (!obtainAttributes.hasValue(i13)) {
            i13 = R$styleable.FontFamilyFont_android_ttcIndex;
        }
        int i14 = R$styleable.FontFamilyFont_fontVariationSettings;
        if (!obtainAttributes.hasValue(i14)) {
            i14 = R$styleable.FontFamilyFont_android_fontVariationSettings;
        }
        String string = obtainAttributes.getString(i14);
        int i15 = obtainAttributes.getInt(i13, 0);
        int i16 = R$styleable.FontFamilyFont_font;
        if (!obtainAttributes.hasValue(i16)) {
            i16 = R$styleable.FontFamilyFont_android_font;
        }
        int resourceId = obtainAttributes.getResourceId(i16, 0);
        String string2 = obtainAttributes.getString(i16);
        obtainAttributes.recycle();
        while (xmlPullParser.next() != 3) {
            g(xmlPullParser);
        }
        return new d(string2, i11, z10, string, i15, resourceId);
    }

    private static void g(XmlPullParser xmlPullParser) {
        int i10 = 1;
        while (i10 > 0) {
            int next = xmlPullParser.next();
            if (next == 2) {
                i10++;
            } else if (next == 3) {
                i10--;
            }
        }
    }

    private static List<byte[]> h(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            arrayList.add(Base64.decode(str, 0));
        }
        return arrayList;
    }
}
