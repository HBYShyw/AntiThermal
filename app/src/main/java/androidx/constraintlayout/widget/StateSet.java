package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: StateSet.java */
/* renamed from: androidx.constraintlayout.widget.e, reason: use source file name */
/* loaded from: classes.dex */
public class StateSet {

    /* renamed from: a, reason: collision with root package name */
    int f2022a = -1;

    /* renamed from: b, reason: collision with root package name */
    int f2023b = -1;

    /* renamed from: c, reason: collision with root package name */
    int f2024c = -1;

    /* renamed from: d, reason: collision with root package name */
    private SparseArray<a> f2025d = new SparseArray<>();

    /* renamed from: e, reason: collision with root package name */
    private SparseArray<ConstraintSet> f2026e = new SparseArray<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StateSet.java */
    /* renamed from: androidx.constraintlayout.widget.e$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        int f2027a;

        /* renamed from: b, reason: collision with root package name */
        ArrayList<b> f2028b = new ArrayList<>();

        /* renamed from: c, reason: collision with root package name */
        int f2029c;

        /* renamed from: d, reason: collision with root package name */
        boolean f2030d;

        public a(Context context, XmlPullParser xmlPullParser) {
            this.f2029c = -1;
            this.f2030d = false;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), R$styleable.State);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.State_android_id) {
                    this.f2027a = obtainStyledAttributes.getResourceId(index, this.f2027a);
                } else if (index == R$styleable.State_constraints) {
                    this.f2029c = obtainStyledAttributes.getResourceId(index, this.f2029c);
                    String resourceTypeName = context.getResources().getResourceTypeName(this.f2029c);
                    context.getResources().getResourceName(this.f2029c);
                    if ("layout".equals(resourceTypeName)) {
                        this.f2030d = true;
                    }
                }
            }
            obtainStyledAttributes.recycle();
        }

        void a(b bVar) {
            this.f2028b.add(bVar);
        }

        public int b(float f10, float f11) {
            for (int i10 = 0; i10 < this.f2028b.size(); i10++) {
                if (this.f2028b.get(i10).a(f10, f11)) {
                    return i10;
                }
            }
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StateSet.java */
    /* renamed from: androidx.constraintlayout.widget.e$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        float f2031a;

        /* renamed from: b, reason: collision with root package name */
        float f2032b;

        /* renamed from: c, reason: collision with root package name */
        float f2033c;

        /* renamed from: d, reason: collision with root package name */
        float f2034d;

        /* renamed from: e, reason: collision with root package name */
        int f2035e;

        /* renamed from: f, reason: collision with root package name */
        boolean f2036f;

        public b(Context context, XmlPullParser xmlPullParser) {
            this.f2031a = Float.NaN;
            this.f2032b = Float.NaN;
            this.f2033c = Float.NaN;
            this.f2034d = Float.NaN;
            this.f2035e = -1;
            this.f2036f = false;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), R$styleable.Variant);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.Variant_constraints) {
                    this.f2035e = obtainStyledAttributes.getResourceId(index, this.f2035e);
                    String resourceTypeName = context.getResources().getResourceTypeName(this.f2035e);
                    context.getResources().getResourceName(this.f2035e);
                    if ("layout".equals(resourceTypeName)) {
                        this.f2036f = true;
                    }
                } else if (index == R$styleable.Variant_region_heightLessThan) {
                    this.f2034d = obtainStyledAttributes.getDimension(index, this.f2034d);
                } else if (index == R$styleable.Variant_region_heightMoreThan) {
                    this.f2032b = obtainStyledAttributes.getDimension(index, this.f2032b);
                } else if (index == R$styleable.Variant_region_widthLessThan) {
                    this.f2033c = obtainStyledAttributes.getDimension(index, this.f2033c);
                } else if (index == R$styleable.Variant_region_widthMoreThan) {
                    this.f2031a = obtainStyledAttributes.getDimension(index, this.f2031a);
                } else {
                    Log.v("ConstraintLayoutStates", "Unknown tag");
                }
            }
            obtainStyledAttributes.recycle();
        }

        boolean a(float f10, float f11) {
            if (!Float.isNaN(this.f2031a) && f10 < this.f2031a) {
                return false;
            }
            if (!Float.isNaN(this.f2032b) && f11 < this.f2032b) {
                return false;
            }
            if (Float.isNaN(this.f2033c) || f10 <= this.f2033c) {
                return Float.isNaN(this.f2034d) || f11 <= this.f2034d;
            }
            return false;
        }
    }

    public StateSet(Context context, XmlPullParser xmlPullParser) {
        b(context, xmlPullParser);
    }

    private void b(Context context, XmlPullParser xmlPullParser) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), R$styleable.StateSet);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i10 = 0; i10 < indexCount; i10++) {
            int index = obtainStyledAttributes.getIndex(i10);
            if (index == R$styleable.StateSet_defaultState) {
                this.f2022a = obtainStyledAttributes.getResourceId(index, this.f2022a);
            }
        }
        a aVar = null;
        try {
            int eventType = xmlPullParser.getEventType();
            while (eventType != 1) {
                if (eventType == 0) {
                    xmlPullParser.getName();
                } else if (eventType == 2) {
                    String name = xmlPullParser.getName();
                    char c10 = 65535;
                    switch (name.hashCode()) {
                        case 80204913:
                            if (name.equals("State")) {
                                c10 = 2;
                                break;
                            }
                            break;
                        case 1301459538:
                            if (name.equals("LayoutDescription")) {
                                c10 = 0;
                                break;
                            }
                            break;
                        case 1382829617:
                            if (name.equals("StateSet")) {
                                c10 = 1;
                                break;
                            }
                            break;
                        case 1901439077:
                            if (name.equals("Variant")) {
                                c10 = 3;
                                break;
                            }
                            break;
                    }
                    if (c10 != 0 && c10 != 1) {
                        if (c10 == 2) {
                            aVar = new a(context, xmlPullParser);
                            this.f2025d.put(aVar.f2027a, aVar);
                        } else if (c10 != 3) {
                            Log.v("ConstraintLayoutStates", "unknown tag " + name);
                        } else {
                            b bVar = new b(context, xmlPullParser);
                            if (aVar != null) {
                                aVar.a(bVar);
                            }
                        }
                    }
                } else if (eventType != 3) {
                    continue;
                } else if ("StateSet".equals(xmlPullParser.getName())) {
                    return;
                }
                eventType = xmlPullParser.next();
            }
        } catch (IOException e10) {
            e10.printStackTrace();
        } catch (XmlPullParserException e11) {
            e11.printStackTrace();
        }
    }

    public int a(int i10, int i11, float f10, float f11) {
        a aVar = this.f2025d.get(i11);
        if (aVar == null) {
            return i11;
        }
        if (f10 != -1.0f && f11 != -1.0f) {
            b bVar = null;
            Iterator<b> it = aVar.f2028b.iterator();
            while (it.hasNext()) {
                b next = it.next();
                if (next.a(f10, f11)) {
                    if (i10 == next.f2035e) {
                        return i10;
                    }
                    bVar = next;
                }
            }
            if (bVar != null) {
                return bVar.f2035e;
            }
            return aVar.f2029c;
        }
        if (aVar.f2029c == i10) {
            return i10;
        }
        Iterator<b> it2 = aVar.f2028b.iterator();
        while (it2.hasNext()) {
            if (i10 == it2.next().f2035e) {
                return i10;
            }
        }
        return aVar.f2029c;
    }

    public int c(int i10, int i11, int i12) {
        return d(-1, i10, i11, i12);
    }

    public int d(int i10, int i11, float f10, float f11) {
        a aVar;
        int b10;
        if (i10 != i11) {
            a aVar2 = this.f2025d.get(i11);
            if (aVar2 == null) {
                return -1;
            }
            int b11 = aVar2.b(f10, f11);
            return b11 == -1 ? aVar2.f2029c : aVar2.f2028b.get(b11).f2035e;
        }
        if (i11 == -1) {
            aVar = this.f2025d.valueAt(0);
        } else {
            aVar = this.f2025d.get(this.f2023b);
        }
        if (aVar == null) {
            return -1;
        }
        return ((this.f2024c == -1 || !aVar.f2028b.get(i10).a(f10, f11)) && i10 != (b10 = aVar.b(f10, f11))) ? b10 == -1 ? aVar.f2029c : aVar.f2028b.get(b10).f2035e : i10;
    }
}
