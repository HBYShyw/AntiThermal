package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: ConstraintLayoutStates.java */
/* renamed from: androidx.constraintlayout.widget.b, reason: use source file name */
/* loaded from: classes.dex */
public class ConstraintLayoutStates {

    /* renamed from: a, reason: collision with root package name */
    private final ConstraintLayout f1930a;

    /* renamed from: b, reason: collision with root package name */
    ConstraintSet f1931b;

    /* renamed from: c, reason: collision with root package name */
    int f1932c = -1;

    /* renamed from: d, reason: collision with root package name */
    int f1933d = -1;

    /* renamed from: e, reason: collision with root package name */
    private SparseArray<a> f1934e = new SparseArray<>();

    /* renamed from: f, reason: collision with root package name */
    private SparseArray<ConstraintSet> f1935f = new SparseArray<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ConstraintLayoutStates.java */
    /* renamed from: androidx.constraintlayout.widget.b$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        int f1936a;

        /* renamed from: b, reason: collision with root package name */
        ArrayList<b> f1937b = new ArrayList<>();

        /* renamed from: c, reason: collision with root package name */
        int f1938c;

        /* renamed from: d, reason: collision with root package name */
        ConstraintSet f1939d;

        public a(Context context, XmlPullParser xmlPullParser) {
            this.f1938c = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), R$styleable.State);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.State_android_id) {
                    this.f1936a = obtainStyledAttributes.getResourceId(index, this.f1936a);
                } else if (index == R$styleable.State_constraints) {
                    this.f1938c = obtainStyledAttributes.getResourceId(index, this.f1938c);
                    String resourceTypeName = context.getResources().getResourceTypeName(this.f1938c);
                    context.getResources().getResourceName(this.f1938c);
                    if ("layout".equals(resourceTypeName)) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        this.f1939d = constraintSet;
                        constraintSet.i(context, this.f1938c);
                    }
                }
            }
            obtainStyledAttributes.recycle();
        }

        void a(b bVar) {
            this.f1937b.add(bVar);
        }

        public int b(float f10, float f11) {
            for (int i10 = 0; i10 < this.f1937b.size(); i10++) {
                if (this.f1937b.get(i10).a(f10, f11)) {
                    return i10;
                }
            }
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ConstraintLayoutStates.java */
    /* renamed from: androidx.constraintlayout.widget.b$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        float f1940a;

        /* renamed from: b, reason: collision with root package name */
        float f1941b;

        /* renamed from: c, reason: collision with root package name */
        float f1942c;

        /* renamed from: d, reason: collision with root package name */
        float f1943d;

        /* renamed from: e, reason: collision with root package name */
        int f1944e;

        /* renamed from: f, reason: collision with root package name */
        ConstraintSet f1945f;

        public b(Context context, XmlPullParser xmlPullParser) {
            this.f1940a = Float.NaN;
            this.f1941b = Float.NaN;
            this.f1942c = Float.NaN;
            this.f1943d = Float.NaN;
            this.f1944e = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), R$styleable.Variant);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.Variant_constraints) {
                    this.f1944e = obtainStyledAttributes.getResourceId(index, this.f1944e);
                    String resourceTypeName = context.getResources().getResourceTypeName(this.f1944e);
                    context.getResources().getResourceName(this.f1944e);
                    if ("layout".equals(resourceTypeName)) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        this.f1945f = constraintSet;
                        constraintSet.i(context, this.f1944e);
                    }
                } else if (index == R$styleable.Variant_region_heightLessThan) {
                    this.f1943d = obtainStyledAttributes.getDimension(index, this.f1943d);
                } else if (index == R$styleable.Variant_region_heightMoreThan) {
                    this.f1941b = obtainStyledAttributes.getDimension(index, this.f1941b);
                } else if (index == R$styleable.Variant_region_widthLessThan) {
                    this.f1942c = obtainStyledAttributes.getDimension(index, this.f1942c);
                } else if (index == R$styleable.Variant_region_widthMoreThan) {
                    this.f1940a = obtainStyledAttributes.getDimension(index, this.f1940a);
                } else {
                    Log.v("ConstraintLayoutStates", "Unknown tag");
                }
            }
            obtainStyledAttributes.recycle();
        }

        boolean a(float f10, float f11) {
            if (!Float.isNaN(this.f1940a) && f10 < this.f1940a) {
                return false;
            }
            if (!Float.isNaN(this.f1941b) && f11 < this.f1941b) {
                return false;
            }
            if (Float.isNaN(this.f1942c) || f10 <= this.f1942c) {
                return Float.isNaN(this.f1943d) || f11 <= this.f1943d;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConstraintLayoutStates(Context context, ConstraintLayout constraintLayout, int i10) {
        this.f1930a = constraintLayout;
        a(context, i10);
    }

    private void a(Context context, int i10) {
        XmlResourceParser xml = context.getResources().getXml(i10);
        a aVar = null;
        try {
            for (int eventType = xml.getEventType(); eventType != 1; eventType = xml.next()) {
                if (eventType == 0) {
                    xml.getName();
                } else if (eventType == 2) {
                    String name = xml.getName();
                    char c10 = 65535;
                    switch (name.hashCode()) {
                        case -1349929691:
                            if (name.equals("ConstraintSet")) {
                                c10 = 4;
                                break;
                            }
                            break;
                        case 80204913:
                            if (name.equals("State")) {
                                c10 = 2;
                                break;
                            }
                            break;
                        case 1382829617:
                            if (name.equals("StateSet")) {
                                c10 = 1;
                                break;
                            }
                            break;
                        case 1657696882:
                            if (name.equals("layoutDescription")) {
                                c10 = 0;
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
                            aVar = new a(context, xml);
                            this.f1934e.put(aVar.f1936a, aVar);
                        } else if (c10 == 3) {
                            b bVar = new b(context, xml);
                            if (aVar != null) {
                                aVar.a(bVar);
                            }
                        } else if (c10 != 4) {
                            Log.v("ConstraintLayoutStates", "unknown tag " + name);
                        } else {
                            b(context, xml);
                        }
                    }
                }
            }
        } catch (IOException e10) {
            e10.printStackTrace();
        } catch (XmlPullParserException e11) {
            e11.printStackTrace();
        }
    }

    private void b(Context context, XmlPullParser xmlPullParser) {
        ConstraintSet constraintSet = new ConstraintSet();
        int attributeCount = xmlPullParser.getAttributeCount();
        for (int i10 = 0; i10 < attributeCount; i10++) {
            if ("id".equals(xmlPullParser.getAttributeName(i10))) {
                String attributeValue = xmlPullParser.getAttributeValue(i10);
                int identifier = attributeValue.contains("/") ? context.getResources().getIdentifier(attributeValue.substring(attributeValue.indexOf(47) + 1), "id", context.getPackageName()) : -1;
                if (identifier == -1) {
                    if (attributeValue.length() > 1) {
                        identifier = Integer.parseInt(attributeValue.substring(1));
                    } else {
                        Log.e("ConstraintLayoutStates", "error in parsing id");
                    }
                }
                constraintSet.y(context, xmlPullParser);
                this.f1935f.put(identifier, constraintSet);
                return;
            }
        }
    }

    public void c(ConstraintsChangedListener constraintsChangedListener) {
    }

    public void d(int i10, float f10, float f11) {
        ConstraintSet constraintSet;
        a aVar;
        int b10;
        ConstraintSet constraintSet2;
        int i11 = this.f1932c;
        if (i11 == i10) {
            if (i10 == -1) {
                aVar = this.f1934e.valueAt(0);
            } else {
                aVar = this.f1934e.get(i11);
            }
            int i12 = this.f1933d;
            if ((i12 == -1 || !aVar.f1937b.get(i12).a(f10, f11)) && this.f1933d != (b10 = aVar.b(f10, f11))) {
                if (b10 == -1) {
                    constraintSet2 = this.f1931b;
                } else {
                    constraintSet2 = aVar.f1937b.get(b10).f1945f;
                }
                if (b10 != -1) {
                    int i13 = aVar.f1937b.get(b10).f1944e;
                }
                if (constraintSet2 == null) {
                    return;
                }
                this.f1933d = b10;
                constraintSet2.d(this.f1930a);
                return;
            }
            return;
        }
        this.f1932c = i10;
        a aVar2 = this.f1934e.get(i10);
        int b11 = aVar2.b(f10, f11);
        if (b11 == -1) {
            constraintSet = aVar2.f1939d;
        } else {
            constraintSet = aVar2.f1937b.get(b11).f1945f;
        }
        if (b11 != -1) {
            int i14 = aVar2.f1937b.get(b11).f1944e;
        }
        if (constraintSet == null) {
            Log.v("ConstraintLayoutStates", "NO Constraint set found ! id=" + i10 + ", dim =" + f10 + ", " + f11);
            return;
        }
        this.f1933d = b11;
        constraintSet.d(this.f1930a);
    }
}
