package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import k.Easing;
import m.ConstraintWidget;
import m.HelperWidget;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: ConstraintSet.java */
/* renamed from: androidx.constraintlayout.widget.c, reason: use source file name */
/* loaded from: classes.dex */
public class ConstraintSet {

    /* renamed from: e, reason: collision with root package name */
    private static final int[] f1946e = {0, 4, 8};

    /* renamed from: f, reason: collision with root package name */
    private static SparseIntArray f1947f;

    /* renamed from: a, reason: collision with root package name */
    private boolean f1948a;

    /* renamed from: b, reason: collision with root package name */
    private HashMap<String, ConstraintAttribute> f1949b = new HashMap<>();

    /* renamed from: c, reason: collision with root package name */
    private boolean f1950c = true;

    /* renamed from: d, reason: collision with root package name */
    private HashMap<Integer, a> f1951d = new HashMap<>();

    /* compiled from: ConstraintSet.java */
    /* renamed from: androidx.constraintlayout.widget.c$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        int f1952a;

        /* renamed from: b, reason: collision with root package name */
        public final d f1953b = new d();

        /* renamed from: c, reason: collision with root package name */
        public final c f1954c = new c();

        /* renamed from: d, reason: collision with root package name */
        public final b f1955d = new b();

        /* renamed from: e, reason: collision with root package name */
        public final e f1956e = new e();

        /* renamed from: f, reason: collision with root package name */
        public HashMap<String, ConstraintAttribute> f1957f = new HashMap<>();

        /* JADX INFO: Access modifiers changed from: private */
        public void i(int i10, ConstraintLayout.LayoutParams layoutParams) {
            this.f1952a = i10;
            b bVar = this.f1955d;
            bVar.f1973h = layoutParams.f1852d;
            bVar.f1975i = layoutParams.f1854e;
            bVar.f1977j = layoutParams.f1856f;
            bVar.f1979k = layoutParams.f1858g;
            bVar.f1980l = layoutParams.f1860h;
            bVar.f1981m = layoutParams.f1862i;
            bVar.f1982n = layoutParams.f1864j;
            bVar.f1983o = layoutParams.f1866k;
            bVar.f1984p = layoutParams.f1868l;
            bVar.f1985q = layoutParams.f1876p;
            bVar.f1986r = layoutParams.f1877q;
            bVar.f1987s = layoutParams.f1878r;
            bVar.f1988t = layoutParams.f1879s;
            bVar.f1989u = layoutParams.f1886z;
            bVar.f1990v = layoutParams.A;
            bVar.f1991w = layoutParams.B;
            bVar.f1992x = layoutParams.f1870m;
            bVar.f1993y = layoutParams.f1872n;
            bVar.f1994z = layoutParams.f1874o;
            bVar.A = layoutParams.Q;
            bVar.B = layoutParams.R;
            bVar.C = layoutParams.S;
            bVar.f1971g = layoutParams.f1850c;
            bVar.f1967e = layoutParams.f1846a;
            bVar.f1969f = layoutParams.f1848b;
            bVar.f1963c = ((ViewGroup.MarginLayoutParams) layoutParams).width;
            bVar.f1965d = ((ViewGroup.MarginLayoutParams) layoutParams).height;
            bVar.D = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
            bVar.E = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
            bVar.F = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
            bVar.G = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
            bVar.P = layoutParams.F;
            bVar.Q = layoutParams.E;
            bVar.S = layoutParams.H;
            bVar.R = layoutParams.G;
            bVar.f1974h0 = layoutParams.T;
            bVar.f1976i0 = layoutParams.U;
            bVar.T = layoutParams.I;
            bVar.U = layoutParams.J;
            bVar.V = layoutParams.M;
            bVar.W = layoutParams.N;
            bVar.X = layoutParams.K;
            bVar.Y = layoutParams.L;
            bVar.Z = layoutParams.O;
            bVar.f1960a0 = layoutParams.P;
            bVar.f1972g0 = layoutParams.V;
            bVar.K = layoutParams.f1881u;
            bVar.M = layoutParams.f1883w;
            bVar.J = layoutParams.f1880t;
            bVar.L = layoutParams.f1882v;
            bVar.O = layoutParams.f1884x;
            bVar.N = layoutParams.f1885y;
            bVar.H = layoutParams.getMarginEnd();
            this.f1955d.I = layoutParams.getMarginStart();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void j(int i10, Constraints.LayoutParams layoutParams) {
            i(i10, layoutParams);
            this.f1953b.f2006d = layoutParams.f1898p0;
            e eVar = this.f1956e;
            eVar.f2010b = layoutParams.f1901s0;
            eVar.f2011c = layoutParams.f1902t0;
            eVar.f2012d = layoutParams.f1903u0;
            eVar.f2013e = layoutParams.f1904v0;
            eVar.f2014f = layoutParams.f1905w0;
            eVar.f2015g = layoutParams.f1906x0;
            eVar.f2016h = layoutParams.f1907y0;
            eVar.f2017i = layoutParams.f1908z0;
            eVar.f2018j = layoutParams.A0;
            eVar.f2019k = layoutParams.B0;
            eVar.f2021m = layoutParams.f1900r0;
            eVar.f2020l = layoutParams.f1899q0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void k(ConstraintHelper constraintHelper, int i10, Constraints.LayoutParams layoutParams) {
            j(i10, layoutParams);
            if (constraintHelper instanceof Barrier) {
                b bVar = this.f1955d;
                bVar.f1966d0 = 1;
                Barrier barrier = (Barrier) constraintHelper;
                bVar.f1962b0 = barrier.getType();
                this.f1955d.f1968e0 = barrier.getReferencedIds();
                this.f1955d.f1964c0 = barrier.getMargin();
            }
        }

        private ConstraintAttribute l(String str, ConstraintAttribute.b bVar) {
            if (this.f1957f.containsKey(str)) {
                ConstraintAttribute constraintAttribute = this.f1957f.get(str);
                if (constraintAttribute.c() == bVar) {
                    return constraintAttribute;
                }
                throw new IllegalArgumentException("ConstraintAttribute is already a " + constraintAttribute.c().name());
            }
            ConstraintAttribute constraintAttribute2 = new ConstraintAttribute(str, bVar);
            this.f1957f.put(str, constraintAttribute2);
            return constraintAttribute2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void m(String str, int i10) {
            l(str, ConstraintAttribute.b.COLOR_TYPE).i(i10);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void n(String str, float f10) {
            l(str, ConstraintAttribute.b.FLOAT_TYPE).j(f10);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void o(String str, String str2) {
            l(str, ConstraintAttribute.b.STRING_TYPE).l(str2);
        }

        public void g(ConstraintLayout.LayoutParams layoutParams) {
            b bVar = this.f1955d;
            layoutParams.f1852d = bVar.f1973h;
            layoutParams.f1854e = bVar.f1975i;
            layoutParams.f1856f = bVar.f1977j;
            layoutParams.f1858g = bVar.f1979k;
            layoutParams.f1860h = bVar.f1980l;
            layoutParams.f1862i = bVar.f1981m;
            layoutParams.f1864j = bVar.f1982n;
            layoutParams.f1866k = bVar.f1983o;
            layoutParams.f1868l = bVar.f1984p;
            layoutParams.f1876p = bVar.f1985q;
            layoutParams.f1877q = bVar.f1986r;
            layoutParams.f1878r = bVar.f1987s;
            layoutParams.f1879s = bVar.f1988t;
            ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = bVar.D;
            ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = bVar.E;
            ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = bVar.F;
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = bVar.G;
            layoutParams.f1884x = bVar.O;
            layoutParams.f1885y = bVar.N;
            layoutParams.f1881u = bVar.K;
            layoutParams.f1883w = bVar.M;
            layoutParams.f1886z = bVar.f1989u;
            layoutParams.A = bVar.f1990v;
            layoutParams.f1870m = bVar.f1992x;
            layoutParams.f1872n = bVar.f1993y;
            layoutParams.f1874o = bVar.f1994z;
            layoutParams.B = bVar.f1991w;
            layoutParams.Q = bVar.A;
            layoutParams.R = bVar.B;
            layoutParams.F = bVar.P;
            layoutParams.E = bVar.Q;
            layoutParams.H = bVar.S;
            layoutParams.G = bVar.R;
            layoutParams.T = bVar.f1974h0;
            layoutParams.U = bVar.f1976i0;
            layoutParams.I = bVar.T;
            layoutParams.J = bVar.U;
            layoutParams.M = bVar.V;
            layoutParams.N = bVar.W;
            layoutParams.K = bVar.X;
            layoutParams.L = bVar.Y;
            layoutParams.O = bVar.Z;
            layoutParams.P = bVar.f1960a0;
            layoutParams.S = bVar.C;
            layoutParams.f1850c = bVar.f1971g;
            layoutParams.f1846a = bVar.f1967e;
            layoutParams.f1848b = bVar.f1969f;
            ((ViewGroup.MarginLayoutParams) layoutParams).width = bVar.f1963c;
            ((ViewGroup.MarginLayoutParams) layoutParams).height = bVar.f1965d;
            String str = bVar.f1972g0;
            if (str != null) {
                layoutParams.V = str;
            }
            layoutParams.setMarginStart(bVar.I);
            layoutParams.setMarginEnd(this.f1955d.H);
            layoutParams.c();
        }

        /* renamed from: h, reason: merged with bridge method [inline-methods] */
        public a clone() {
            a aVar = new a();
            aVar.f1955d.a(this.f1955d);
            aVar.f1954c.a(this.f1954c);
            aVar.f1953b.a(this.f1953b);
            aVar.f1956e.a(this.f1956e);
            aVar.f1952a = this.f1952a;
            return aVar;
        }
    }

    /* compiled from: ConstraintSet.java */
    /* renamed from: androidx.constraintlayout.widget.c$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: k0, reason: collision with root package name */
        private static SparseIntArray f1958k0;

        /* renamed from: c, reason: collision with root package name */
        public int f1963c;

        /* renamed from: d, reason: collision with root package name */
        public int f1965d;

        /* renamed from: e0, reason: collision with root package name */
        public int[] f1968e0;

        /* renamed from: f0, reason: collision with root package name */
        public String f1970f0;

        /* renamed from: g0, reason: collision with root package name */
        public String f1972g0;

        /* renamed from: a, reason: collision with root package name */
        public boolean f1959a = false;

        /* renamed from: b, reason: collision with root package name */
        public boolean f1961b = false;

        /* renamed from: e, reason: collision with root package name */
        public int f1967e = -1;

        /* renamed from: f, reason: collision with root package name */
        public int f1969f = -1;

        /* renamed from: g, reason: collision with root package name */
        public float f1971g = -1.0f;

        /* renamed from: h, reason: collision with root package name */
        public int f1973h = -1;

        /* renamed from: i, reason: collision with root package name */
        public int f1975i = -1;

        /* renamed from: j, reason: collision with root package name */
        public int f1977j = -1;

        /* renamed from: k, reason: collision with root package name */
        public int f1979k = -1;

        /* renamed from: l, reason: collision with root package name */
        public int f1980l = -1;

        /* renamed from: m, reason: collision with root package name */
        public int f1981m = -1;

        /* renamed from: n, reason: collision with root package name */
        public int f1982n = -1;

        /* renamed from: o, reason: collision with root package name */
        public int f1983o = -1;

        /* renamed from: p, reason: collision with root package name */
        public int f1984p = -1;

        /* renamed from: q, reason: collision with root package name */
        public int f1985q = -1;

        /* renamed from: r, reason: collision with root package name */
        public int f1986r = -1;

        /* renamed from: s, reason: collision with root package name */
        public int f1987s = -1;

        /* renamed from: t, reason: collision with root package name */
        public int f1988t = -1;

        /* renamed from: u, reason: collision with root package name */
        public float f1989u = 0.5f;

        /* renamed from: v, reason: collision with root package name */
        public float f1990v = 0.5f;

        /* renamed from: w, reason: collision with root package name */
        public String f1991w = null;

        /* renamed from: x, reason: collision with root package name */
        public int f1992x = -1;

        /* renamed from: y, reason: collision with root package name */
        public int f1993y = 0;

        /* renamed from: z, reason: collision with root package name */
        public float f1994z = 0.0f;
        public int A = -1;
        public int B = -1;
        public int C = -1;
        public int D = -1;
        public int E = -1;
        public int F = -1;
        public int G = -1;
        public int H = -1;
        public int I = -1;
        public int J = -1;
        public int K = -1;
        public int L = -1;
        public int M = -1;
        public int N = -1;
        public int O = -1;
        public float P = -1.0f;
        public float Q = -1.0f;
        public int R = 0;
        public int S = 0;
        public int T = 0;
        public int U = 0;
        public int V = -1;
        public int W = -1;
        public int X = -1;
        public int Y = -1;
        public float Z = 1.0f;

        /* renamed from: a0, reason: collision with root package name */
        public float f1960a0 = 1.0f;

        /* renamed from: b0, reason: collision with root package name */
        public int f1962b0 = -1;

        /* renamed from: c0, reason: collision with root package name */
        public int f1964c0 = 0;

        /* renamed from: d0, reason: collision with root package name */
        public int f1966d0 = -1;

        /* renamed from: h0, reason: collision with root package name */
        public boolean f1974h0 = false;

        /* renamed from: i0, reason: collision with root package name */
        public boolean f1976i0 = false;

        /* renamed from: j0, reason: collision with root package name */
        public boolean f1978j0 = true;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f1958k0 = sparseIntArray;
            sparseIntArray.append(R$styleable.Layout_layout_constraintLeft_toLeftOf, 24);
            f1958k0.append(R$styleable.Layout_layout_constraintLeft_toRightOf, 25);
            f1958k0.append(R$styleable.Layout_layout_constraintRight_toLeftOf, 28);
            f1958k0.append(R$styleable.Layout_layout_constraintRight_toRightOf, 29);
            f1958k0.append(R$styleable.Layout_layout_constraintTop_toTopOf, 35);
            f1958k0.append(R$styleable.Layout_layout_constraintTop_toBottomOf, 34);
            f1958k0.append(R$styleable.Layout_layout_constraintBottom_toTopOf, 4);
            f1958k0.append(R$styleable.Layout_layout_constraintBottom_toBottomOf, 3);
            f1958k0.append(R$styleable.Layout_layout_constraintBaseline_toBaselineOf, 1);
            f1958k0.append(R$styleable.Layout_layout_editor_absoluteX, 6);
            f1958k0.append(R$styleable.Layout_layout_editor_absoluteY, 7);
            f1958k0.append(R$styleable.Layout_layout_constraintGuide_begin, 17);
            f1958k0.append(R$styleable.Layout_layout_constraintGuide_end, 18);
            f1958k0.append(R$styleable.Layout_layout_constraintGuide_percent, 19);
            f1958k0.append(R$styleable.Layout_android_orientation, 26);
            f1958k0.append(R$styleable.Layout_layout_constraintStart_toEndOf, 31);
            f1958k0.append(R$styleable.Layout_layout_constraintStart_toStartOf, 32);
            f1958k0.append(R$styleable.Layout_layout_constraintEnd_toStartOf, 10);
            f1958k0.append(R$styleable.Layout_layout_constraintEnd_toEndOf, 9);
            f1958k0.append(R$styleable.Layout_layout_goneMarginLeft, 13);
            f1958k0.append(R$styleable.Layout_layout_goneMarginTop, 16);
            f1958k0.append(R$styleable.Layout_layout_goneMarginRight, 14);
            f1958k0.append(R$styleable.Layout_layout_goneMarginBottom, 11);
            f1958k0.append(R$styleable.Layout_layout_goneMarginStart, 15);
            f1958k0.append(R$styleable.Layout_layout_goneMarginEnd, 12);
            f1958k0.append(R$styleable.Layout_layout_constraintVertical_weight, 38);
            f1958k0.append(R$styleable.Layout_layout_constraintHorizontal_weight, 37);
            f1958k0.append(R$styleable.Layout_layout_constraintHorizontal_chainStyle, 39);
            f1958k0.append(R$styleable.Layout_layout_constraintVertical_chainStyle, 40);
            f1958k0.append(R$styleable.Layout_layout_constraintHorizontal_bias, 20);
            f1958k0.append(R$styleable.Layout_layout_constraintVertical_bias, 36);
            f1958k0.append(R$styleable.Layout_layout_constraintDimensionRatio, 5);
            f1958k0.append(R$styleable.Layout_layout_constraintLeft_creator, 76);
            f1958k0.append(R$styleable.Layout_layout_constraintTop_creator, 76);
            f1958k0.append(R$styleable.Layout_layout_constraintRight_creator, 76);
            f1958k0.append(R$styleable.Layout_layout_constraintBottom_creator, 76);
            f1958k0.append(R$styleable.Layout_layout_constraintBaseline_creator, 76);
            f1958k0.append(R$styleable.Layout_android_layout_marginLeft, 23);
            f1958k0.append(R$styleable.Layout_android_layout_marginRight, 27);
            f1958k0.append(R$styleable.Layout_android_layout_marginStart, 30);
            f1958k0.append(R$styleable.Layout_android_layout_marginEnd, 8);
            f1958k0.append(R$styleable.Layout_android_layout_marginTop, 33);
            f1958k0.append(R$styleable.Layout_android_layout_marginBottom, 2);
            f1958k0.append(R$styleable.Layout_android_layout_width, 22);
            f1958k0.append(R$styleable.Layout_android_layout_height, 21);
            f1958k0.append(R$styleable.Layout_layout_constraintCircle, 61);
            f1958k0.append(R$styleable.Layout_layout_constraintCircleRadius, 62);
            f1958k0.append(R$styleable.Layout_layout_constraintCircleAngle, 63);
            f1958k0.append(R$styleable.Layout_layout_constraintWidth_percent, 69);
            f1958k0.append(R$styleable.Layout_layout_constraintHeight_percent, 70);
            f1958k0.append(R$styleable.Layout_chainUseRtl, 71);
            f1958k0.append(R$styleable.Layout_barrierDirection, 72);
            f1958k0.append(R$styleable.Layout_barrierMargin, 73);
            f1958k0.append(R$styleable.Layout_constraint_referenced_ids, 74);
            f1958k0.append(R$styleable.Layout_barrierAllowsGoneWidgets, 75);
        }

        public void a(b bVar) {
            this.f1959a = bVar.f1959a;
            this.f1963c = bVar.f1963c;
            this.f1961b = bVar.f1961b;
            this.f1965d = bVar.f1965d;
            this.f1967e = bVar.f1967e;
            this.f1969f = bVar.f1969f;
            this.f1971g = bVar.f1971g;
            this.f1973h = bVar.f1973h;
            this.f1975i = bVar.f1975i;
            this.f1977j = bVar.f1977j;
            this.f1979k = bVar.f1979k;
            this.f1980l = bVar.f1980l;
            this.f1981m = bVar.f1981m;
            this.f1982n = bVar.f1982n;
            this.f1983o = bVar.f1983o;
            this.f1984p = bVar.f1984p;
            this.f1985q = bVar.f1985q;
            this.f1986r = bVar.f1986r;
            this.f1987s = bVar.f1987s;
            this.f1988t = bVar.f1988t;
            this.f1989u = bVar.f1989u;
            this.f1990v = bVar.f1990v;
            this.f1991w = bVar.f1991w;
            this.f1992x = bVar.f1992x;
            this.f1993y = bVar.f1993y;
            this.f1994z = bVar.f1994z;
            this.A = bVar.A;
            this.B = bVar.B;
            this.C = bVar.C;
            this.D = bVar.D;
            this.E = bVar.E;
            this.F = bVar.F;
            this.G = bVar.G;
            this.H = bVar.H;
            this.I = bVar.I;
            this.J = bVar.J;
            this.K = bVar.K;
            this.L = bVar.L;
            this.M = bVar.M;
            this.N = bVar.N;
            this.O = bVar.O;
            this.P = bVar.P;
            this.Q = bVar.Q;
            this.R = bVar.R;
            this.S = bVar.S;
            this.T = bVar.T;
            this.U = bVar.U;
            this.V = bVar.V;
            this.W = bVar.W;
            this.X = bVar.X;
            this.Y = bVar.Y;
            this.Z = bVar.Z;
            this.f1960a0 = bVar.f1960a0;
            this.f1962b0 = bVar.f1962b0;
            this.f1964c0 = bVar.f1964c0;
            this.f1966d0 = bVar.f1966d0;
            this.f1972g0 = bVar.f1972g0;
            int[] iArr = bVar.f1968e0;
            if (iArr != null) {
                this.f1968e0 = Arrays.copyOf(iArr, iArr.length);
            } else {
                this.f1968e0 = null;
            }
            this.f1970f0 = bVar.f1970f0;
            this.f1974h0 = bVar.f1974h0;
            this.f1976i0 = bVar.f1976i0;
            this.f1978j0 = bVar.f1978j0;
        }

        void b(Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Layout);
            this.f1961b = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                int i11 = f1958k0.get(index);
                if (i11 == 80) {
                    this.f1974h0 = obtainStyledAttributes.getBoolean(index, this.f1974h0);
                } else if (i11 != 81) {
                    switch (i11) {
                        case 1:
                            this.f1984p = ConstraintSet.z(obtainStyledAttributes, index, this.f1984p);
                            break;
                        case 2:
                            this.G = obtainStyledAttributes.getDimensionPixelSize(index, this.G);
                            break;
                        case 3:
                            this.f1983o = ConstraintSet.z(obtainStyledAttributes, index, this.f1983o);
                            break;
                        case 4:
                            this.f1982n = ConstraintSet.z(obtainStyledAttributes, index, this.f1982n);
                            break;
                        case 5:
                            this.f1991w = obtainStyledAttributes.getString(index);
                            break;
                        case 6:
                            this.A = obtainStyledAttributes.getDimensionPixelOffset(index, this.A);
                            break;
                        case 7:
                            this.B = obtainStyledAttributes.getDimensionPixelOffset(index, this.B);
                            break;
                        case 8:
                            this.H = obtainStyledAttributes.getDimensionPixelSize(index, this.H);
                            break;
                        case 9:
                            this.f1988t = ConstraintSet.z(obtainStyledAttributes, index, this.f1988t);
                            break;
                        case 10:
                            this.f1987s = ConstraintSet.z(obtainStyledAttributes, index, this.f1987s);
                            break;
                        case 11:
                            this.M = obtainStyledAttributes.getDimensionPixelSize(index, this.M);
                            break;
                        case 12:
                            this.N = obtainStyledAttributes.getDimensionPixelSize(index, this.N);
                            break;
                        case 13:
                            this.J = obtainStyledAttributes.getDimensionPixelSize(index, this.J);
                            break;
                        case 14:
                            this.L = obtainStyledAttributes.getDimensionPixelSize(index, this.L);
                            break;
                        case 15:
                            this.O = obtainStyledAttributes.getDimensionPixelSize(index, this.O);
                            break;
                        case 16:
                            this.K = obtainStyledAttributes.getDimensionPixelSize(index, this.K);
                            break;
                        case 17:
                            this.f1967e = obtainStyledAttributes.getDimensionPixelOffset(index, this.f1967e);
                            break;
                        case 18:
                            this.f1969f = obtainStyledAttributes.getDimensionPixelOffset(index, this.f1969f);
                            break;
                        case 19:
                            this.f1971g = obtainStyledAttributes.getFloat(index, this.f1971g);
                            break;
                        case 20:
                            this.f1989u = obtainStyledAttributes.getFloat(index, this.f1989u);
                            break;
                        case 21:
                            this.f1965d = obtainStyledAttributes.getLayoutDimension(index, this.f1965d);
                            break;
                        case 22:
                            this.f1963c = obtainStyledAttributes.getLayoutDimension(index, this.f1963c);
                            break;
                        case 23:
                            this.D = obtainStyledAttributes.getDimensionPixelSize(index, this.D);
                            break;
                        case 24:
                            this.f1973h = ConstraintSet.z(obtainStyledAttributes, index, this.f1973h);
                            break;
                        case 25:
                            this.f1975i = ConstraintSet.z(obtainStyledAttributes, index, this.f1975i);
                            break;
                        case 26:
                            this.C = obtainStyledAttributes.getInt(index, this.C);
                            break;
                        case 27:
                            this.E = obtainStyledAttributes.getDimensionPixelSize(index, this.E);
                            break;
                        case 28:
                            this.f1977j = ConstraintSet.z(obtainStyledAttributes, index, this.f1977j);
                            break;
                        case 29:
                            this.f1979k = ConstraintSet.z(obtainStyledAttributes, index, this.f1979k);
                            break;
                        case 30:
                            this.I = obtainStyledAttributes.getDimensionPixelSize(index, this.I);
                            break;
                        case 31:
                            this.f1985q = ConstraintSet.z(obtainStyledAttributes, index, this.f1985q);
                            break;
                        case 32:
                            this.f1986r = ConstraintSet.z(obtainStyledAttributes, index, this.f1986r);
                            break;
                        case 33:
                            this.F = obtainStyledAttributes.getDimensionPixelSize(index, this.F);
                            break;
                        case 34:
                            this.f1981m = ConstraintSet.z(obtainStyledAttributes, index, this.f1981m);
                            break;
                        case 35:
                            this.f1980l = ConstraintSet.z(obtainStyledAttributes, index, this.f1980l);
                            break;
                        case 36:
                            this.f1990v = obtainStyledAttributes.getFloat(index, this.f1990v);
                            break;
                        case 37:
                            this.Q = obtainStyledAttributes.getFloat(index, this.Q);
                            break;
                        case 38:
                            this.P = obtainStyledAttributes.getFloat(index, this.P);
                            break;
                        case 39:
                            this.R = obtainStyledAttributes.getInt(index, this.R);
                            break;
                        case 40:
                            this.S = obtainStyledAttributes.getInt(index, this.S);
                            break;
                        default:
                            switch (i11) {
                                case 54:
                                    this.T = obtainStyledAttributes.getInt(index, this.T);
                                    break;
                                case 55:
                                    this.U = obtainStyledAttributes.getInt(index, this.U);
                                    break;
                                case 56:
                                    this.V = obtainStyledAttributes.getDimensionPixelSize(index, this.V);
                                    break;
                                case 57:
                                    this.W = obtainStyledAttributes.getDimensionPixelSize(index, this.W);
                                    break;
                                case 58:
                                    this.X = obtainStyledAttributes.getDimensionPixelSize(index, this.X);
                                    break;
                                case 59:
                                    this.Y = obtainStyledAttributes.getDimensionPixelSize(index, this.Y);
                                    break;
                                default:
                                    switch (i11) {
                                        case 61:
                                            this.f1992x = ConstraintSet.z(obtainStyledAttributes, index, this.f1992x);
                                            break;
                                        case 62:
                                            this.f1993y = obtainStyledAttributes.getDimensionPixelSize(index, this.f1993y);
                                            break;
                                        case 63:
                                            this.f1994z = obtainStyledAttributes.getFloat(index, this.f1994z);
                                            break;
                                        default:
                                            switch (i11) {
                                                case 69:
                                                    this.Z = obtainStyledAttributes.getFloat(index, 1.0f);
                                                    break;
                                                case 70:
                                                    this.f1960a0 = obtainStyledAttributes.getFloat(index, 1.0f);
                                                    break;
                                                case 71:
                                                    Log.e("ConstraintSet", "CURRENTLY UNSUPPORTED");
                                                    break;
                                                case 72:
                                                    this.f1962b0 = obtainStyledAttributes.getInt(index, this.f1962b0);
                                                    break;
                                                case 73:
                                                    this.f1964c0 = obtainStyledAttributes.getDimensionPixelSize(index, this.f1964c0);
                                                    break;
                                                case 74:
                                                    this.f1970f0 = obtainStyledAttributes.getString(index);
                                                    break;
                                                case 75:
                                                    this.f1978j0 = obtainStyledAttributes.getBoolean(index, this.f1978j0);
                                                    break;
                                                case 76:
                                                    Log.w("ConstraintSet", "unused attribute 0x" + Integer.toHexString(index) + "   " + f1958k0.get(index));
                                                    break;
                                                case 77:
                                                    this.f1972g0 = obtainStyledAttributes.getString(index);
                                                    break;
                                                default:
                                                    Log.w("ConstraintSet", "Unknown attribute 0x" + Integer.toHexString(index) + "   " + f1958k0.get(index));
                                                    break;
                                            }
                                    }
                            }
                    }
                } else {
                    this.f1976i0 = obtainStyledAttributes.getBoolean(index, this.f1976i0);
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    /* compiled from: ConstraintSet.java */
    /* renamed from: androidx.constraintlayout.widget.c$c */
    /* loaded from: classes.dex */
    public static class c {

        /* renamed from: h, reason: collision with root package name */
        private static SparseIntArray f1995h;

        /* renamed from: a, reason: collision with root package name */
        public boolean f1996a = false;

        /* renamed from: b, reason: collision with root package name */
        public int f1997b = -1;

        /* renamed from: c, reason: collision with root package name */
        public String f1998c = null;

        /* renamed from: d, reason: collision with root package name */
        public int f1999d = -1;

        /* renamed from: e, reason: collision with root package name */
        public int f2000e = 0;

        /* renamed from: f, reason: collision with root package name */
        public float f2001f = Float.NaN;

        /* renamed from: g, reason: collision with root package name */
        public float f2002g = Float.NaN;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f1995h = sparseIntArray;
            sparseIntArray.append(R$styleable.Motion_motionPathRotate, 1);
            f1995h.append(R$styleable.Motion_pathMotionArc, 2);
            f1995h.append(R$styleable.Motion_transitionEasing, 3);
            f1995h.append(R$styleable.Motion_drawPath, 4);
            f1995h.append(R$styleable.Motion_animate_relativeTo, 5);
            f1995h.append(R$styleable.Motion_motionStagger, 6);
        }

        public void a(c cVar) {
            this.f1996a = cVar.f1996a;
            this.f1997b = cVar.f1997b;
            this.f1998c = cVar.f1998c;
            this.f1999d = cVar.f1999d;
            this.f2000e = cVar.f2000e;
            this.f2002g = cVar.f2002g;
            this.f2001f = cVar.f2001f;
        }

        void b(Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Motion);
            this.f1996a = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                switch (f1995h.get(index)) {
                    case 1:
                        this.f2002g = obtainStyledAttributes.getFloat(index, this.f2002g);
                        break;
                    case 2:
                        this.f1999d = obtainStyledAttributes.getInt(index, this.f1999d);
                        break;
                    case 3:
                        if (obtainStyledAttributes.peekValue(index).type == 3) {
                            this.f1998c = obtainStyledAttributes.getString(index);
                            break;
                        } else {
                            this.f1998c = Easing.f13959c[obtainStyledAttributes.getInteger(index, 0)];
                            break;
                        }
                    case 4:
                        this.f2000e = obtainStyledAttributes.getInt(index, 0);
                        break;
                    case 5:
                        this.f1997b = ConstraintSet.z(obtainStyledAttributes, index, this.f1997b);
                        break;
                    case 6:
                        this.f2001f = obtainStyledAttributes.getFloat(index, this.f2001f);
                        break;
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    /* compiled from: ConstraintSet.java */
    /* renamed from: androidx.constraintlayout.widget.c$d */
    /* loaded from: classes.dex */
    public static class d {

        /* renamed from: a, reason: collision with root package name */
        public boolean f2003a = false;

        /* renamed from: b, reason: collision with root package name */
        public int f2004b = 0;

        /* renamed from: c, reason: collision with root package name */
        public int f2005c = 0;

        /* renamed from: d, reason: collision with root package name */
        public float f2006d = 1.0f;

        /* renamed from: e, reason: collision with root package name */
        public float f2007e = Float.NaN;

        public void a(d dVar) {
            this.f2003a = dVar.f2003a;
            this.f2004b = dVar.f2004b;
            this.f2006d = dVar.f2006d;
            this.f2007e = dVar.f2007e;
            this.f2005c = dVar.f2005c;
        }

        void b(Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.PropertySet);
            this.f2003a = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.PropertySet_android_alpha) {
                    this.f2006d = obtainStyledAttributes.getFloat(index, this.f2006d);
                } else if (index == R$styleable.PropertySet_android_visibility) {
                    this.f2004b = obtainStyledAttributes.getInt(index, this.f2004b);
                    this.f2004b = ConstraintSet.f1946e[this.f2004b];
                } else if (index == R$styleable.PropertySet_visibilityMode) {
                    this.f2005c = obtainStyledAttributes.getInt(index, this.f2005c);
                } else if (index == R$styleable.PropertySet_motionProgress) {
                    this.f2007e = obtainStyledAttributes.getFloat(index, this.f2007e);
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    /* compiled from: ConstraintSet.java */
    /* renamed from: androidx.constraintlayout.widget.c$e */
    /* loaded from: classes.dex */
    public static class e {

        /* renamed from: n, reason: collision with root package name */
        private static SparseIntArray f2008n;

        /* renamed from: a, reason: collision with root package name */
        public boolean f2009a = false;

        /* renamed from: b, reason: collision with root package name */
        public float f2010b = 0.0f;

        /* renamed from: c, reason: collision with root package name */
        public float f2011c = 0.0f;

        /* renamed from: d, reason: collision with root package name */
        public float f2012d = 0.0f;

        /* renamed from: e, reason: collision with root package name */
        public float f2013e = 1.0f;

        /* renamed from: f, reason: collision with root package name */
        public float f2014f = 1.0f;

        /* renamed from: g, reason: collision with root package name */
        public float f2015g = Float.NaN;

        /* renamed from: h, reason: collision with root package name */
        public float f2016h = Float.NaN;

        /* renamed from: i, reason: collision with root package name */
        public float f2017i = 0.0f;

        /* renamed from: j, reason: collision with root package name */
        public float f2018j = 0.0f;

        /* renamed from: k, reason: collision with root package name */
        public float f2019k = 0.0f;

        /* renamed from: l, reason: collision with root package name */
        public boolean f2020l = false;

        /* renamed from: m, reason: collision with root package name */
        public float f2021m = 0.0f;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f2008n = sparseIntArray;
            sparseIntArray.append(R$styleable.Transform_android_rotation, 1);
            f2008n.append(R$styleable.Transform_android_rotationX, 2);
            f2008n.append(R$styleable.Transform_android_rotationY, 3);
            f2008n.append(R$styleable.Transform_android_scaleX, 4);
            f2008n.append(R$styleable.Transform_android_scaleY, 5);
            f2008n.append(R$styleable.Transform_android_transformPivotX, 6);
            f2008n.append(R$styleable.Transform_android_transformPivotY, 7);
            f2008n.append(R$styleable.Transform_android_translationX, 8);
            f2008n.append(R$styleable.Transform_android_translationY, 9);
            f2008n.append(R$styleable.Transform_android_translationZ, 10);
            f2008n.append(R$styleable.Transform_android_elevation, 11);
        }

        public void a(e eVar) {
            this.f2009a = eVar.f2009a;
            this.f2010b = eVar.f2010b;
            this.f2011c = eVar.f2011c;
            this.f2012d = eVar.f2012d;
            this.f2013e = eVar.f2013e;
            this.f2014f = eVar.f2014f;
            this.f2015g = eVar.f2015g;
            this.f2016h = eVar.f2016h;
            this.f2017i = eVar.f2017i;
            this.f2018j = eVar.f2018j;
            this.f2019k = eVar.f2019k;
            this.f2020l = eVar.f2020l;
            this.f2021m = eVar.f2021m;
        }

        void b(Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Transform);
            this.f2009a = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                switch (f2008n.get(index)) {
                    case 1:
                        this.f2010b = obtainStyledAttributes.getFloat(index, this.f2010b);
                        break;
                    case 2:
                        this.f2011c = obtainStyledAttributes.getFloat(index, this.f2011c);
                        break;
                    case 3:
                        this.f2012d = obtainStyledAttributes.getFloat(index, this.f2012d);
                        break;
                    case 4:
                        this.f2013e = obtainStyledAttributes.getFloat(index, this.f2013e);
                        break;
                    case 5:
                        this.f2014f = obtainStyledAttributes.getFloat(index, this.f2014f);
                        break;
                    case 6:
                        this.f2015g = obtainStyledAttributes.getDimension(index, this.f2015g);
                        break;
                    case 7:
                        this.f2016h = obtainStyledAttributes.getDimension(index, this.f2016h);
                        break;
                    case 8:
                        this.f2017i = obtainStyledAttributes.getDimension(index, this.f2017i);
                        break;
                    case 9:
                        this.f2018j = obtainStyledAttributes.getDimension(index, this.f2018j);
                        break;
                    case 10:
                        this.f2019k = obtainStyledAttributes.getDimension(index, this.f2019k);
                        break;
                    case 11:
                        this.f2020l = true;
                        this.f2021m = obtainStyledAttributes.getDimension(index, this.f2021m);
                        break;
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        f1947f = sparseIntArray;
        sparseIntArray.append(R$styleable.Constraint_layout_constraintLeft_toLeftOf, 25);
        f1947f.append(R$styleable.Constraint_layout_constraintLeft_toRightOf, 26);
        f1947f.append(R$styleable.Constraint_layout_constraintRight_toLeftOf, 29);
        f1947f.append(R$styleable.Constraint_layout_constraintRight_toRightOf, 30);
        f1947f.append(R$styleable.Constraint_layout_constraintTop_toTopOf, 36);
        f1947f.append(R$styleable.Constraint_layout_constraintTop_toBottomOf, 35);
        f1947f.append(R$styleable.Constraint_layout_constraintBottom_toTopOf, 4);
        f1947f.append(R$styleable.Constraint_layout_constraintBottom_toBottomOf, 3);
        f1947f.append(R$styleable.Constraint_layout_constraintBaseline_toBaselineOf, 1);
        f1947f.append(R$styleable.Constraint_layout_editor_absoluteX, 6);
        f1947f.append(R$styleable.Constraint_layout_editor_absoluteY, 7);
        f1947f.append(R$styleable.Constraint_layout_constraintGuide_begin, 17);
        f1947f.append(R$styleable.Constraint_layout_constraintGuide_end, 18);
        f1947f.append(R$styleable.Constraint_layout_constraintGuide_percent, 19);
        f1947f.append(R$styleable.Constraint_android_orientation, 27);
        f1947f.append(R$styleable.Constraint_layout_constraintStart_toEndOf, 32);
        f1947f.append(R$styleable.Constraint_layout_constraintStart_toStartOf, 33);
        f1947f.append(R$styleable.Constraint_layout_constraintEnd_toStartOf, 10);
        f1947f.append(R$styleable.Constraint_layout_constraintEnd_toEndOf, 9);
        f1947f.append(R$styleable.Constraint_layout_goneMarginLeft, 13);
        f1947f.append(R$styleable.Constraint_layout_goneMarginTop, 16);
        f1947f.append(R$styleable.Constraint_layout_goneMarginRight, 14);
        f1947f.append(R$styleable.Constraint_layout_goneMarginBottom, 11);
        f1947f.append(R$styleable.Constraint_layout_goneMarginStart, 15);
        f1947f.append(R$styleable.Constraint_layout_goneMarginEnd, 12);
        f1947f.append(R$styleable.Constraint_layout_constraintVertical_weight, 40);
        f1947f.append(R$styleable.Constraint_layout_constraintHorizontal_weight, 39);
        f1947f.append(R$styleable.Constraint_layout_constraintHorizontal_chainStyle, 41);
        f1947f.append(R$styleable.Constraint_layout_constraintVertical_chainStyle, 42);
        f1947f.append(R$styleable.Constraint_layout_constraintHorizontal_bias, 20);
        f1947f.append(R$styleable.Constraint_layout_constraintVertical_bias, 37);
        f1947f.append(R$styleable.Constraint_layout_constraintDimensionRatio, 5);
        f1947f.append(R$styleable.Constraint_layout_constraintLeft_creator, 82);
        f1947f.append(R$styleable.Constraint_layout_constraintTop_creator, 82);
        f1947f.append(R$styleable.Constraint_layout_constraintRight_creator, 82);
        f1947f.append(R$styleable.Constraint_layout_constraintBottom_creator, 82);
        f1947f.append(R$styleable.Constraint_layout_constraintBaseline_creator, 82);
        f1947f.append(R$styleable.Constraint_android_layout_marginLeft, 24);
        f1947f.append(R$styleable.Constraint_android_layout_marginRight, 28);
        f1947f.append(R$styleable.Constraint_android_layout_marginStart, 31);
        f1947f.append(R$styleable.Constraint_android_layout_marginEnd, 8);
        f1947f.append(R$styleable.Constraint_android_layout_marginTop, 34);
        f1947f.append(R$styleable.Constraint_android_layout_marginBottom, 2);
        f1947f.append(R$styleable.Constraint_android_layout_width, 23);
        f1947f.append(R$styleable.Constraint_android_layout_height, 21);
        f1947f.append(R$styleable.Constraint_android_visibility, 22);
        f1947f.append(R$styleable.Constraint_android_alpha, 43);
        f1947f.append(R$styleable.Constraint_android_elevation, 44);
        f1947f.append(R$styleable.Constraint_android_rotationX, 45);
        f1947f.append(R$styleable.Constraint_android_rotationY, 46);
        f1947f.append(R$styleable.Constraint_android_rotation, 60);
        f1947f.append(R$styleable.Constraint_android_scaleX, 47);
        f1947f.append(R$styleable.Constraint_android_scaleY, 48);
        f1947f.append(R$styleable.Constraint_android_transformPivotX, 49);
        f1947f.append(R$styleable.Constraint_android_transformPivotY, 50);
        f1947f.append(R$styleable.Constraint_android_translationX, 51);
        f1947f.append(R$styleable.Constraint_android_translationY, 52);
        f1947f.append(R$styleable.Constraint_android_translationZ, 53);
        f1947f.append(R$styleable.Constraint_layout_constraintWidth_default, 54);
        f1947f.append(R$styleable.Constraint_layout_constraintHeight_default, 55);
        f1947f.append(R$styleable.Constraint_layout_constraintWidth_max, 56);
        f1947f.append(R$styleable.Constraint_layout_constraintHeight_max, 57);
        f1947f.append(R$styleable.Constraint_layout_constraintWidth_min, 58);
        f1947f.append(R$styleable.Constraint_layout_constraintHeight_min, 59);
        f1947f.append(R$styleable.Constraint_layout_constraintCircle, 61);
        f1947f.append(R$styleable.Constraint_layout_constraintCircleRadius, 62);
        f1947f.append(R$styleable.Constraint_layout_constraintCircleAngle, 63);
        f1947f.append(R$styleable.Constraint_animate_relativeTo, 64);
        f1947f.append(R$styleable.Constraint_transitionEasing, 65);
        f1947f.append(R$styleable.Constraint_drawPath, 66);
        f1947f.append(R$styleable.Constraint_transitionPathRotate, 67);
        f1947f.append(R$styleable.Constraint_motionStagger, 79);
        f1947f.append(R$styleable.Constraint_android_id, 38);
        f1947f.append(R$styleable.Constraint_motionProgress, 68);
        f1947f.append(R$styleable.Constraint_layout_constraintWidth_percent, 69);
        f1947f.append(R$styleable.Constraint_layout_constraintHeight_percent, 70);
        f1947f.append(R$styleable.Constraint_chainUseRtl, 71);
        f1947f.append(R$styleable.Constraint_barrierDirection, 72);
        f1947f.append(R$styleable.Constraint_barrierMargin, 73);
        f1947f.append(R$styleable.Constraint_constraint_referenced_ids, 74);
        f1947f.append(R$styleable.Constraint_barrierAllowsGoneWidgets, 75);
        f1947f.append(R$styleable.Constraint_pathMotionArc, 76);
        f1947f.append(R$styleable.Constraint_layout_constraintTag, 77);
        f1947f.append(R$styleable.Constraint_visibilityMode, 78);
        f1947f.append(R$styleable.Constraint_layout_constrainedWidth, 80);
        f1947f.append(R$styleable.Constraint_layout_constrainedHeight, 81);
    }

    private void A(Context context, a aVar, TypedArray typedArray) {
        int indexCount = typedArray.getIndexCount();
        for (int i10 = 0; i10 < indexCount; i10++) {
            int index = typedArray.getIndex(i10);
            if (index != R$styleable.Constraint_android_id && R$styleable.Constraint_android_layout_marginStart != index && R$styleable.Constraint_android_layout_marginEnd != index) {
                aVar.f1954c.f1996a = true;
                aVar.f1955d.f1961b = true;
                aVar.f1953b.f2003a = true;
                aVar.f1956e.f2009a = true;
            }
            switch (f1947f.get(index)) {
                case 1:
                    b bVar = aVar.f1955d;
                    bVar.f1984p = z(typedArray, index, bVar.f1984p);
                    break;
                case 2:
                    b bVar2 = aVar.f1955d;
                    bVar2.G = typedArray.getDimensionPixelSize(index, bVar2.G);
                    break;
                case 3:
                    b bVar3 = aVar.f1955d;
                    bVar3.f1983o = z(typedArray, index, bVar3.f1983o);
                    break;
                case 4:
                    b bVar4 = aVar.f1955d;
                    bVar4.f1982n = z(typedArray, index, bVar4.f1982n);
                    break;
                case 5:
                    aVar.f1955d.f1991w = typedArray.getString(index);
                    break;
                case 6:
                    b bVar5 = aVar.f1955d;
                    bVar5.A = typedArray.getDimensionPixelOffset(index, bVar5.A);
                    break;
                case 7:
                    b bVar6 = aVar.f1955d;
                    bVar6.B = typedArray.getDimensionPixelOffset(index, bVar6.B);
                    break;
                case 8:
                    b bVar7 = aVar.f1955d;
                    bVar7.H = typedArray.getDimensionPixelSize(index, bVar7.H);
                    break;
                case 9:
                    b bVar8 = aVar.f1955d;
                    bVar8.f1988t = z(typedArray, index, bVar8.f1988t);
                    break;
                case 10:
                    b bVar9 = aVar.f1955d;
                    bVar9.f1987s = z(typedArray, index, bVar9.f1987s);
                    break;
                case 11:
                    b bVar10 = aVar.f1955d;
                    bVar10.M = typedArray.getDimensionPixelSize(index, bVar10.M);
                    break;
                case 12:
                    b bVar11 = aVar.f1955d;
                    bVar11.N = typedArray.getDimensionPixelSize(index, bVar11.N);
                    break;
                case 13:
                    b bVar12 = aVar.f1955d;
                    bVar12.J = typedArray.getDimensionPixelSize(index, bVar12.J);
                    break;
                case 14:
                    b bVar13 = aVar.f1955d;
                    bVar13.L = typedArray.getDimensionPixelSize(index, bVar13.L);
                    break;
                case 15:
                    b bVar14 = aVar.f1955d;
                    bVar14.O = typedArray.getDimensionPixelSize(index, bVar14.O);
                    break;
                case 16:
                    b bVar15 = aVar.f1955d;
                    bVar15.K = typedArray.getDimensionPixelSize(index, bVar15.K);
                    break;
                case 17:
                    b bVar16 = aVar.f1955d;
                    bVar16.f1967e = typedArray.getDimensionPixelOffset(index, bVar16.f1967e);
                    break;
                case 18:
                    b bVar17 = aVar.f1955d;
                    bVar17.f1969f = typedArray.getDimensionPixelOffset(index, bVar17.f1969f);
                    break;
                case 19:
                    b bVar18 = aVar.f1955d;
                    bVar18.f1971g = typedArray.getFloat(index, bVar18.f1971g);
                    break;
                case 20:
                    b bVar19 = aVar.f1955d;
                    bVar19.f1989u = typedArray.getFloat(index, bVar19.f1989u);
                    break;
                case 21:
                    b bVar20 = aVar.f1955d;
                    bVar20.f1965d = typedArray.getLayoutDimension(index, bVar20.f1965d);
                    break;
                case 22:
                    d dVar = aVar.f1953b;
                    dVar.f2004b = typedArray.getInt(index, dVar.f2004b);
                    d dVar2 = aVar.f1953b;
                    dVar2.f2004b = f1946e[dVar2.f2004b];
                    break;
                case 23:
                    b bVar21 = aVar.f1955d;
                    bVar21.f1963c = typedArray.getLayoutDimension(index, bVar21.f1963c);
                    break;
                case 24:
                    b bVar22 = aVar.f1955d;
                    bVar22.D = typedArray.getDimensionPixelSize(index, bVar22.D);
                    break;
                case 25:
                    b bVar23 = aVar.f1955d;
                    bVar23.f1973h = z(typedArray, index, bVar23.f1973h);
                    break;
                case 26:
                    b bVar24 = aVar.f1955d;
                    bVar24.f1975i = z(typedArray, index, bVar24.f1975i);
                    break;
                case 27:
                    b bVar25 = aVar.f1955d;
                    bVar25.C = typedArray.getInt(index, bVar25.C);
                    break;
                case 28:
                    b bVar26 = aVar.f1955d;
                    bVar26.E = typedArray.getDimensionPixelSize(index, bVar26.E);
                    break;
                case 29:
                    b bVar27 = aVar.f1955d;
                    bVar27.f1977j = z(typedArray, index, bVar27.f1977j);
                    break;
                case 30:
                    b bVar28 = aVar.f1955d;
                    bVar28.f1979k = z(typedArray, index, bVar28.f1979k);
                    break;
                case 31:
                    b bVar29 = aVar.f1955d;
                    bVar29.I = typedArray.getDimensionPixelSize(index, bVar29.I);
                    break;
                case 32:
                    b bVar30 = aVar.f1955d;
                    bVar30.f1985q = z(typedArray, index, bVar30.f1985q);
                    break;
                case 33:
                    b bVar31 = aVar.f1955d;
                    bVar31.f1986r = z(typedArray, index, bVar31.f1986r);
                    break;
                case 34:
                    b bVar32 = aVar.f1955d;
                    bVar32.F = typedArray.getDimensionPixelSize(index, bVar32.F);
                    break;
                case 35:
                    b bVar33 = aVar.f1955d;
                    bVar33.f1981m = z(typedArray, index, bVar33.f1981m);
                    break;
                case 36:
                    b bVar34 = aVar.f1955d;
                    bVar34.f1980l = z(typedArray, index, bVar34.f1980l);
                    break;
                case 37:
                    b bVar35 = aVar.f1955d;
                    bVar35.f1990v = typedArray.getFloat(index, bVar35.f1990v);
                    break;
                case 38:
                    aVar.f1952a = typedArray.getResourceId(index, aVar.f1952a);
                    break;
                case 39:
                    b bVar36 = aVar.f1955d;
                    bVar36.Q = typedArray.getFloat(index, bVar36.Q);
                    break;
                case 40:
                    b bVar37 = aVar.f1955d;
                    bVar37.P = typedArray.getFloat(index, bVar37.P);
                    break;
                case 41:
                    b bVar38 = aVar.f1955d;
                    bVar38.R = typedArray.getInt(index, bVar38.R);
                    break;
                case 42:
                    b bVar39 = aVar.f1955d;
                    bVar39.S = typedArray.getInt(index, bVar39.S);
                    break;
                case 43:
                    d dVar3 = aVar.f1953b;
                    dVar3.f2006d = typedArray.getFloat(index, dVar3.f2006d);
                    break;
                case 44:
                    e eVar = aVar.f1956e;
                    eVar.f2020l = true;
                    eVar.f2021m = typedArray.getDimension(index, eVar.f2021m);
                    break;
                case 45:
                    e eVar2 = aVar.f1956e;
                    eVar2.f2011c = typedArray.getFloat(index, eVar2.f2011c);
                    break;
                case 46:
                    e eVar3 = aVar.f1956e;
                    eVar3.f2012d = typedArray.getFloat(index, eVar3.f2012d);
                    break;
                case 47:
                    e eVar4 = aVar.f1956e;
                    eVar4.f2013e = typedArray.getFloat(index, eVar4.f2013e);
                    break;
                case 48:
                    e eVar5 = aVar.f1956e;
                    eVar5.f2014f = typedArray.getFloat(index, eVar5.f2014f);
                    break;
                case 49:
                    e eVar6 = aVar.f1956e;
                    eVar6.f2015g = typedArray.getDimension(index, eVar6.f2015g);
                    break;
                case 50:
                    e eVar7 = aVar.f1956e;
                    eVar7.f2016h = typedArray.getDimension(index, eVar7.f2016h);
                    break;
                case 51:
                    e eVar8 = aVar.f1956e;
                    eVar8.f2017i = typedArray.getDimension(index, eVar8.f2017i);
                    break;
                case 52:
                    e eVar9 = aVar.f1956e;
                    eVar9.f2018j = typedArray.getDimension(index, eVar9.f2018j);
                    break;
                case 53:
                    e eVar10 = aVar.f1956e;
                    eVar10.f2019k = typedArray.getDimension(index, eVar10.f2019k);
                    break;
                case 54:
                    b bVar40 = aVar.f1955d;
                    bVar40.T = typedArray.getInt(index, bVar40.T);
                    break;
                case 55:
                    b bVar41 = aVar.f1955d;
                    bVar41.U = typedArray.getInt(index, bVar41.U);
                    break;
                case 56:
                    b bVar42 = aVar.f1955d;
                    bVar42.V = typedArray.getDimensionPixelSize(index, bVar42.V);
                    break;
                case 57:
                    b bVar43 = aVar.f1955d;
                    bVar43.W = typedArray.getDimensionPixelSize(index, bVar43.W);
                    break;
                case 58:
                    b bVar44 = aVar.f1955d;
                    bVar44.X = typedArray.getDimensionPixelSize(index, bVar44.X);
                    break;
                case 59:
                    b bVar45 = aVar.f1955d;
                    bVar45.Y = typedArray.getDimensionPixelSize(index, bVar45.Y);
                    break;
                case 60:
                    e eVar11 = aVar.f1956e;
                    eVar11.f2010b = typedArray.getFloat(index, eVar11.f2010b);
                    break;
                case 61:
                    b bVar46 = aVar.f1955d;
                    bVar46.f1992x = z(typedArray, index, bVar46.f1992x);
                    break;
                case 62:
                    b bVar47 = aVar.f1955d;
                    bVar47.f1993y = typedArray.getDimensionPixelSize(index, bVar47.f1993y);
                    break;
                case 63:
                    b bVar48 = aVar.f1955d;
                    bVar48.f1994z = typedArray.getFloat(index, bVar48.f1994z);
                    break;
                case 64:
                    c cVar = aVar.f1954c;
                    cVar.f1997b = z(typedArray, index, cVar.f1997b);
                    break;
                case 65:
                    if (typedArray.peekValue(index).type == 3) {
                        aVar.f1954c.f1998c = typedArray.getString(index);
                        break;
                    } else {
                        aVar.f1954c.f1998c = Easing.f13959c[typedArray.getInteger(index, 0)];
                        break;
                    }
                case 66:
                    aVar.f1954c.f2000e = typedArray.getInt(index, 0);
                    break;
                case 67:
                    c cVar2 = aVar.f1954c;
                    cVar2.f2002g = typedArray.getFloat(index, cVar2.f2002g);
                    break;
                case 68:
                    d dVar4 = aVar.f1953b;
                    dVar4.f2007e = typedArray.getFloat(index, dVar4.f2007e);
                    break;
                case 69:
                    aVar.f1955d.Z = typedArray.getFloat(index, 1.0f);
                    break;
                case 70:
                    aVar.f1955d.f1960a0 = typedArray.getFloat(index, 1.0f);
                    break;
                case 71:
                    Log.e("ConstraintSet", "CURRENTLY UNSUPPORTED");
                    break;
                case 72:
                    b bVar49 = aVar.f1955d;
                    bVar49.f1962b0 = typedArray.getInt(index, bVar49.f1962b0);
                    break;
                case 73:
                    b bVar50 = aVar.f1955d;
                    bVar50.f1964c0 = typedArray.getDimensionPixelSize(index, bVar50.f1964c0);
                    break;
                case 74:
                    aVar.f1955d.f1970f0 = typedArray.getString(index);
                    break;
                case 75:
                    b bVar51 = aVar.f1955d;
                    bVar51.f1978j0 = typedArray.getBoolean(index, bVar51.f1978j0);
                    break;
                case 76:
                    c cVar3 = aVar.f1954c;
                    cVar3.f1999d = typedArray.getInt(index, cVar3.f1999d);
                    break;
                case 77:
                    aVar.f1955d.f1972g0 = typedArray.getString(index);
                    break;
                case 78:
                    d dVar5 = aVar.f1953b;
                    dVar5.f2005c = typedArray.getInt(index, dVar5.f2005c);
                    break;
                case 79:
                    c cVar4 = aVar.f1954c;
                    cVar4.f2001f = typedArray.getFloat(index, cVar4.f2001f);
                    break;
                case 80:
                    b bVar52 = aVar.f1955d;
                    bVar52.f1974h0 = typedArray.getBoolean(index, bVar52.f1974h0);
                    break;
                case 81:
                    b bVar53 = aVar.f1955d;
                    bVar53.f1976i0 = typedArray.getBoolean(index, bVar53.f1976i0);
                    break;
                case 82:
                    Log.w("ConstraintSet", "unused attribute 0x" + Integer.toHexString(index) + "   " + f1947f.get(index));
                    break;
                default:
                    Log.w("ConstraintSet", "Unknown attribute 0x" + Integer.toHexString(index) + "   " + f1947f.get(index));
                    break;
            }
        }
    }

    private String L(int i10) {
        switch (i10) {
            case 1:
                return "left";
            case 2:
                return "right";
            case 3:
                return "top";
            case 4:
                return "bottom";
            case 5:
                return "baseline";
            case 6:
                return "start";
            case 7:
                return "end";
            default:
                return "undefined";
        }
    }

    private int[] n(View view, String str) {
        int i10;
        Object p10;
        String[] split = str.split(",");
        Context context = view.getContext();
        int[] iArr = new int[split.length];
        int i11 = 0;
        int i12 = 0;
        while (i11 < split.length) {
            String trim = split[i11].trim();
            try {
                i10 = R$id.class.getField(trim).getInt(null);
            } catch (Exception unused) {
                i10 = 0;
            }
            if (i10 == 0) {
                i10 = context.getResources().getIdentifier(trim, "id", context.getPackageName());
            }
            if (i10 == 0 && view.isInEditMode() && (view.getParent() instanceof ConstraintLayout) && (p10 = ((ConstraintLayout) view.getParent()).p(0, trim)) != null && (p10 instanceof Integer)) {
                i10 = ((Integer) p10).intValue();
            }
            iArr[i12] = i10;
            i11++;
            i12++;
        }
        return i12 != split.length ? Arrays.copyOf(iArr, i12) : iArr;
    }

    private a o(Context context, AttributeSet attributeSet) {
        a aVar = new a();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Constraint);
        A(context, aVar, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        return aVar;
    }

    private a p(int i10) {
        if (!this.f1951d.containsKey(Integer.valueOf(i10))) {
            this.f1951d.put(Integer.valueOf(i10), new a());
        }
        return this.f1951d.get(Integer.valueOf(i10));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int z(TypedArray typedArray, int i10, int i11) {
        int resourceId = typedArray.getResourceId(i10, i11);
        return resourceId == -1 ? typedArray.getInt(i10, -1) : resourceId;
    }

    public void B(ConstraintLayout constraintLayout) {
        int childCount = constraintLayout.getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = constraintLayout.getChildAt(i10);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) childAt.getLayoutParams();
            int id2 = childAt.getId();
            if (this.f1950c && id2 == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.f1951d.containsKey(Integer.valueOf(id2))) {
                this.f1951d.put(Integer.valueOf(id2), new a());
            }
            a aVar = this.f1951d.get(Integer.valueOf(id2));
            if (!aVar.f1955d.f1961b) {
                aVar.i(id2, layoutParams);
                if (childAt instanceof ConstraintHelper) {
                    aVar.f1955d.f1968e0 = ((ConstraintHelper) childAt).getReferencedIds();
                    if (childAt instanceof Barrier) {
                        Barrier barrier = (Barrier) childAt;
                        aVar.f1955d.f1978j0 = barrier.v();
                        aVar.f1955d.f1962b0 = barrier.getType();
                        aVar.f1955d.f1964c0 = barrier.getMargin();
                    }
                }
                aVar.f1955d.f1961b = true;
            }
            d dVar = aVar.f1953b;
            if (!dVar.f2003a) {
                dVar.f2004b = childAt.getVisibility();
                aVar.f1953b.f2006d = childAt.getAlpha();
                aVar.f1953b.f2003a = true;
            }
            e eVar = aVar.f1956e;
            if (!eVar.f2009a) {
                eVar.f2009a = true;
                eVar.f2010b = childAt.getRotation();
                aVar.f1956e.f2011c = childAt.getRotationX();
                aVar.f1956e.f2012d = childAt.getRotationY();
                aVar.f1956e.f2013e = childAt.getScaleX();
                aVar.f1956e.f2014f = childAt.getScaleY();
                float pivotX = childAt.getPivotX();
                float pivotY = childAt.getPivotY();
                if (pivotX != UserProfileInfo.Constant.NA_LAT_LON || pivotY != UserProfileInfo.Constant.NA_LAT_LON) {
                    e eVar2 = aVar.f1956e;
                    eVar2.f2015g = pivotX;
                    eVar2.f2016h = pivotY;
                }
                aVar.f1956e.f2017i = childAt.getTranslationX();
                aVar.f1956e.f2018j = childAt.getTranslationY();
                aVar.f1956e.f2019k = childAt.getTranslationZ();
                e eVar3 = aVar.f1956e;
                if (eVar3.f2020l) {
                    eVar3.f2021m = childAt.getElevation();
                }
            }
        }
    }

    public void C(ConstraintSet constraintSet) {
        for (Integer num : constraintSet.f1951d.keySet()) {
            int intValue = num.intValue();
            a aVar = constraintSet.f1951d.get(num);
            if (!this.f1951d.containsKey(Integer.valueOf(intValue))) {
                this.f1951d.put(Integer.valueOf(intValue), new a());
            }
            a aVar2 = this.f1951d.get(Integer.valueOf(intValue));
            b bVar = aVar2.f1955d;
            if (!bVar.f1961b) {
                bVar.a(aVar.f1955d);
            }
            d dVar = aVar2.f1953b;
            if (!dVar.f2003a) {
                dVar.a(aVar.f1953b);
            }
            e eVar = aVar2.f1956e;
            if (!eVar.f2009a) {
                eVar.a(aVar.f1956e);
            }
            c cVar = aVar2.f1954c;
            if (!cVar.f1996a) {
                cVar.a(aVar.f1954c);
            }
            for (String str : aVar.f1957f.keySet()) {
                if (!aVar2.f1957f.containsKey(str)) {
                    aVar2.f1957f.put(str, aVar.f1957f.get(str));
                }
            }
        }
    }

    public void D(int i10, String str, int i11) {
        p(i10).m(str, i11);
    }

    public void E(int i10, String str, float f10) {
        p(i10).n(str, f10);
    }

    public void F(boolean z10) {
        this.f1950c = z10;
    }

    public void G(int i10, float f10) {
        p(i10).f1955d.f1989u = f10;
    }

    public void H(int i10, int i11, int i12) {
        a p10 = p(i10);
        switch (i11) {
            case 1:
                p10.f1955d.D = i12;
                return;
            case 2:
                p10.f1955d.E = i12;
                return;
            case 3:
                p10.f1955d.F = i12;
                return;
            case 4:
                p10.f1955d.G = i12;
                return;
            case 5:
                throw new IllegalArgumentException("baseline does not support margins");
            case 6:
                p10.f1955d.I = i12;
                return;
            case 7:
                p10.f1955d.H = i12;
                return;
            default:
                throw new IllegalArgumentException("unknown constraint");
        }
    }

    public void I(int i10, String str, String str2) {
        p(i10).o(str, str2);
    }

    public void J(boolean z10) {
        this.f1948a = z10;
    }

    public void K(int i10, int i11) {
        p(i10).f1953b.f2004b = i11;
    }

    public void c(ConstraintLayout constraintLayout) {
        int childCount = constraintLayout.getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = constraintLayout.getChildAt(i10);
            int id2 = childAt.getId();
            if (this.f1951d.containsKey(Integer.valueOf(id2))) {
                if (this.f1950c && id2 == -1) {
                    throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
                }
                if (this.f1951d.containsKey(Integer.valueOf(id2))) {
                    ConstraintAttribute.h(childAt, this.f1951d.get(Integer.valueOf(id2)).f1957f);
                }
            } else {
                Log.v("ConstraintSet", "id unknown " + androidx.constraintlayout.motion.widget.a.c(childAt));
            }
        }
    }

    public void d(ConstraintLayout constraintLayout) {
        f(constraintLayout, true);
        constraintLayout.setConstraintSet(null);
        constraintLayout.requestLayout();
    }

    public void e(ConstraintHelper constraintHelper, ConstraintWidget constraintWidget, ConstraintLayout.LayoutParams layoutParams, SparseArray<ConstraintWidget> sparseArray) {
        int id2 = constraintHelper.getId();
        if (this.f1951d.containsKey(Integer.valueOf(id2))) {
            a aVar = this.f1951d.get(Integer.valueOf(id2));
            if (constraintWidget instanceof HelperWidget) {
                constraintHelper.n(aVar, (HelperWidget) constraintWidget, layoutParams, sparseArray);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(ConstraintLayout constraintLayout, boolean z10) {
        int childCount = constraintLayout.getChildCount();
        HashSet hashSet = new HashSet(this.f1951d.keySet());
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = constraintLayout.getChildAt(i10);
            int id2 = childAt.getId();
            if (this.f1951d.containsKey(Integer.valueOf(id2))) {
                if (this.f1950c && id2 == -1) {
                    throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
                }
                if (id2 != -1) {
                    if (this.f1951d.containsKey(Integer.valueOf(id2))) {
                        hashSet.remove(Integer.valueOf(id2));
                        a aVar = this.f1951d.get(Integer.valueOf(id2));
                        if (childAt instanceof Barrier) {
                            aVar.f1955d.f1966d0 = 1;
                        }
                        int i11 = aVar.f1955d.f1966d0;
                        if (i11 != -1 && i11 == 1) {
                            Barrier barrier = (Barrier) childAt;
                            barrier.setId(id2);
                            barrier.setType(aVar.f1955d.f1962b0);
                            barrier.setMargin(aVar.f1955d.f1964c0);
                            barrier.setAllowsGoneWidget(aVar.f1955d.f1978j0);
                            b bVar = aVar.f1955d;
                            int[] iArr = bVar.f1968e0;
                            if (iArr != null) {
                                barrier.setReferencedIds(iArr);
                            } else {
                                String str = bVar.f1970f0;
                                if (str != null) {
                                    bVar.f1968e0 = n(barrier, str);
                                    barrier.setReferencedIds(aVar.f1955d.f1968e0);
                                }
                            }
                        }
                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) childAt.getLayoutParams();
                        layoutParams.c();
                        aVar.g(layoutParams);
                        if (z10) {
                            ConstraintAttribute.h(childAt, aVar.f1957f);
                        }
                        childAt.setLayoutParams(layoutParams);
                        d dVar = aVar.f1953b;
                        if (dVar.f2005c == 0) {
                            childAt.setVisibility(dVar.f2004b);
                        }
                        childAt.setAlpha(aVar.f1953b.f2006d);
                        childAt.setRotation(aVar.f1956e.f2010b);
                        childAt.setRotationX(aVar.f1956e.f2011c);
                        childAt.setRotationY(aVar.f1956e.f2012d);
                        childAt.setScaleX(aVar.f1956e.f2013e);
                        childAt.setScaleY(aVar.f1956e.f2014f);
                        if (!Float.isNaN(aVar.f1956e.f2015g)) {
                            childAt.setPivotX(aVar.f1956e.f2015g);
                        }
                        if (!Float.isNaN(aVar.f1956e.f2016h)) {
                            childAt.setPivotY(aVar.f1956e.f2016h);
                        }
                        childAt.setTranslationX(aVar.f1956e.f2017i);
                        childAt.setTranslationY(aVar.f1956e.f2018j);
                        childAt.setTranslationZ(aVar.f1956e.f2019k);
                        e eVar = aVar.f1956e;
                        if (eVar.f2020l) {
                            childAt.setElevation(eVar.f2021m);
                        }
                    } else {
                        Log.v("ConstraintSet", "WARNING NO CONSTRAINTS for view " + id2);
                    }
                }
            } else {
                Log.w("ConstraintSet", "id unknown " + androidx.constraintlayout.motion.widget.a.c(childAt));
            }
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            Integer num = (Integer) it.next();
            a aVar2 = this.f1951d.get(num);
            int i12 = aVar2.f1955d.f1966d0;
            if (i12 != -1 && i12 == 1) {
                Barrier barrier2 = new Barrier(constraintLayout.getContext());
                barrier2.setId(num.intValue());
                b bVar2 = aVar2.f1955d;
                int[] iArr2 = bVar2.f1968e0;
                if (iArr2 != null) {
                    barrier2.setReferencedIds(iArr2);
                } else {
                    String str2 = bVar2.f1970f0;
                    if (str2 != null) {
                        bVar2.f1968e0 = n(barrier2, str2);
                        barrier2.setReferencedIds(aVar2.f1955d.f1968e0);
                    }
                }
                barrier2.setType(aVar2.f1955d.f1962b0);
                barrier2.setMargin(aVar2.f1955d.f1964c0);
                ConstraintLayout.LayoutParams generateDefaultLayoutParams = constraintLayout.generateDefaultLayoutParams();
                barrier2.u();
                aVar2.g(generateDefaultLayoutParams);
                constraintLayout.addView(barrier2, generateDefaultLayoutParams);
            }
            if (aVar2.f1955d.f1959a) {
                View guideline = new Guideline(constraintLayout.getContext());
                guideline.setId(num.intValue());
                ConstraintLayout.LayoutParams generateDefaultLayoutParams2 = constraintLayout.generateDefaultLayoutParams();
                aVar2.g(generateDefaultLayoutParams2);
                constraintLayout.addView(guideline, generateDefaultLayoutParams2);
            }
        }
    }

    public void g(int i10, ConstraintLayout.LayoutParams layoutParams) {
        if (this.f1951d.containsKey(Integer.valueOf(i10))) {
            this.f1951d.get(Integer.valueOf(i10)).g(layoutParams);
        }
    }

    public void h(int i10, int i11) {
        if (this.f1951d.containsKey(Integer.valueOf(i10))) {
            a aVar = this.f1951d.get(Integer.valueOf(i10));
            switch (i11) {
                case 1:
                    b bVar = aVar.f1955d;
                    bVar.f1975i = -1;
                    bVar.f1973h = -1;
                    bVar.D = -1;
                    bVar.J = -1;
                    return;
                case 2:
                    b bVar2 = aVar.f1955d;
                    bVar2.f1979k = -1;
                    bVar2.f1977j = -1;
                    bVar2.E = -1;
                    bVar2.L = -1;
                    return;
                case 3:
                    b bVar3 = aVar.f1955d;
                    bVar3.f1981m = -1;
                    bVar3.f1980l = -1;
                    bVar3.F = -1;
                    bVar3.K = -1;
                    return;
                case 4:
                    b bVar4 = aVar.f1955d;
                    bVar4.f1982n = -1;
                    bVar4.f1983o = -1;
                    bVar4.G = -1;
                    bVar4.M = -1;
                    return;
                case 5:
                    aVar.f1955d.f1984p = -1;
                    return;
                case 6:
                    b bVar5 = aVar.f1955d;
                    bVar5.f1985q = -1;
                    bVar5.f1986r = -1;
                    bVar5.I = -1;
                    bVar5.O = -1;
                    return;
                case 7:
                    b bVar6 = aVar.f1955d;
                    bVar6.f1987s = -1;
                    bVar6.f1988t = -1;
                    bVar6.H = -1;
                    bVar6.N = -1;
                    return;
                default:
                    throw new IllegalArgumentException("unknown constraint");
            }
        }
    }

    public void i(Context context, int i10) {
        j((ConstraintLayout) LayoutInflater.from(context).inflate(i10, (ViewGroup) null));
    }

    public void j(ConstraintLayout constraintLayout) {
        int childCount = constraintLayout.getChildCount();
        this.f1951d.clear();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = constraintLayout.getChildAt(i10);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) childAt.getLayoutParams();
            int id2 = childAt.getId();
            if (this.f1950c && id2 == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.f1951d.containsKey(Integer.valueOf(id2))) {
                this.f1951d.put(Integer.valueOf(id2), new a());
            }
            a aVar = this.f1951d.get(Integer.valueOf(id2));
            aVar.f1957f = ConstraintAttribute.b(this.f1949b, childAt);
            aVar.i(id2, layoutParams);
            aVar.f1953b.f2004b = childAt.getVisibility();
            aVar.f1953b.f2006d = childAt.getAlpha();
            aVar.f1956e.f2010b = childAt.getRotation();
            aVar.f1956e.f2011c = childAt.getRotationX();
            aVar.f1956e.f2012d = childAt.getRotationY();
            aVar.f1956e.f2013e = childAt.getScaleX();
            aVar.f1956e.f2014f = childAt.getScaleY();
            float pivotX = childAt.getPivotX();
            float pivotY = childAt.getPivotY();
            if (pivotX != UserProfileInfo.Constant.NA_LAT_LON || pivotY != UserProfileInfo.Constant.NA_LAT_LON) {
                e eVar = aVar.f1956e;
                eVar.f2015g = pivotX;
                eVar.f2016h = pivotY;
            }
            aVar.f1956e.f2017i = childAt.getTranslationX();
            aVar.f1956e.f2018j = childAt.getTranslationY();
            aVar.f1956e.f2019k = childAt.getTranslationZ();
            e eVar2 = aVar.f1956e;
            if (eVar2.f2020l) {
                eVar2.f2021m = childAt.getElevation();
            }
            if (childAt instanceof Barrier) {
                Barrier barrier = (Barrier) childAt;
                aVar.f1955d.f1978j0 = barrier.v();
                aVar.f1955d.f1968e0 = barrier.getReferencedIds();
                aVar.f1955d.f1962b0 = barrier.getType();
                aVar.f1955d.f1964c0 = barrier.getMargin();
            }
        }
    }

    public void k(Constraints constraints) {
        int childCount = constraints.getChildCount();
        this.f1951d.clear();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = constraints.getChildAt(i10);
            Constraints.LayoutParams layoutParams = (Constraints.LayoutParams) childAt.getLayoutParams();
            int id2 = childAt.getId();
            if (this.f1950c && id2 == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.f1951d.containsKey(Integer.valueOf(id2))) {
                this.f1951d.put(Integer.valueOf(id2), new a());
            }
            a aVar = this.f1951d.get(Integer.valueOf(id2));
            if (childAt instanceof ConstraintHelper) {
                aVar.k((ConstraintHelper) childAt, id2, layoutParams);
            }
            aVar.j(id2, layoutParams);
        }
    }

    public void l(int i10, int i11, int i12, int i13) {
        if (!this.f1951d.containsKey(Integer.valueOf(i10))) {
            this.f1951d.put(Integer.valueOf(i10), new a());
        }
        a aVar = this.f1951d.get(Integer.valueOf(i10));
        switch (i11) {
            case 1:
                if (i13 == 1) {
                    b bVar = aVar.f1955d;
                    bVar.f1973h = i12;
                    bVar.f1975i = -1;
                    return;
                } else if (i13 == 2) {
                    b bVar2 = aVar.f1955d;
                    bVar2.f1975i = i12;
                    bVar2.f1973h = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("left to " + L(i13) + " undefined");
                }
            case 2:
                if (i13 == 1) {
                    b bVar3 = aVar.f1955d;
                    bVar3.f1977j = i12;
                    bVar3.f1979k = -1;
                    return;
                } else if (i13 == 2) {
                    b bVar4 = aVar.f1955d;
                    bVar4.f1979k = i12;
                    bVar4.f1977j = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("right to " + L(i13) + " undefined");
                }
            case 3:
                if (i13 == 3) {
                    b bVar5 = aVar.f1955d;
                    bVar5.f1980l = i12;
                    bVar5.f1981m = -1;
                    bVar5.f1984p = -1;
                    return;
                }
                if (i13 == 4) {
                    b bVar6 = aVar.f1955d;
                    bVar6.f1981m = i12;
                    bVar6.f1980l = -1;
                    bVar6.f1984p = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + L(i13) + " undefined");
            case 4:
                if (i13 == 4) {
                    b bVar7 = aVar.f1955d;
                    bVar7.f1983o = i12;
                    bVar7.f1982n = -1;
                    bVar7.f1984p = -1;
                    return;
                }
                if (i13 == 3) {
                    b bVar8 = aVar.f1955d;
                    bVar8.f1982n = i12;
                    bVar8.f1983o = -1;
                    bVar8.f1984p = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + L(i13) + " undefined");
            case 5:
                if (i13 == 5) {
                    b bVar9 = aVar.f1955d;
                    bVar9.f1984p = i12;
                    bVar9.f1983o = -1;
                    bVar9.f1982n = -1;
                    bVar9.f1980l = -1;
                    bVar9.f1981m = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + L(i13) + " undefined");
            case 6:
                if (i13 == 6) {
                    b bVar10 = aVar.f1955d;
                    bVar10.f1986r = i12;
                    bVar10.f1985q = -1;
                    return;
                } else if (i13 == 7) {
                    b bVar11 = aVar.f1955d;
                    bVar11.f1985q = i12;
                    bVar11.f1986r = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("right to " + L(i13) + " undefined");
                }
            case 7:
                if (i13 == 7) {
                    b bVar12 = aVar.f1955d;
                    bVar12.f1988t = i12;
                    bVar12.f1987s = -1;
                    return;
                } else if (i13 == 6) {
                    b bVar13 = aVar.f1955d;
                    bVar13.f1987s = i12;
                    bVar13.f1988t = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("right to " + L(i13) + " undefined");
                }
            default:
                throw new IllegalArgumentException(L(i11) + " to " + L(i13) + " unknown");
        }
    }

    public void m(int i10, int i11, int i12, float f10) {
        b bVar = p(i10).f1955d;
        bVar.f1992x = i11;
        bVar.f1993y = i12;
        bVar.f1994z = f10;
    }

    public a q(int i10) {
        if (this.f1951d.containsKey(Integer.valueOf(i10))) {
            return this.f1951d.get(Integer.valueOf(i10));
        }
        return null;
    }

    public int r(int i10) {
        return p(i10).f1955d.f1965d;
    }

    public int[] s() {
        Integer[] numArr = (Integer[]) this.f1951d.keySet().toArray(new Integer[0]);
        int length = numArr.length;
        int[] iArr = new int[length];
        for (int i10 = 0; i10 < length; i10++) {
            iArr[i10] = numArr[i10].intValue();
        }
        return iArr;
    }

    public a t(int i10) {
        return p(i10);
    }

    public int u(int i10) {
        return p(i10).f1953b.f2004b;
    }

    public int v(int i10) {
        return p(i10).f1953b.f2005c;
    }

    public int w(int i10) {
        return p(i10).f1955d.f1963c;
    }

    public void x(Context context, int i10) {
        XmlResourceParser xml = context.getResources().getXml(i10);
        try {
            for (int eventType = xml.getEventType(); eventType != 1; eventType = xml.next()) {
                if (eventType == 0) {
                    xml.getName();
                } else if (eventType == 2) {
                    String name = xml.getName();
                    a o10 = o(context, Xml.asAttributeSet(xml));
                    if (name.equalsIgnoreCase("Guideline")) {
                        o10.f1955d.f1959a = true;
                    }
                    this.f1951d.put(Integer.valueOf(o10.f1952a), o10);
                }
            }
        } catch (IOException e10) {
            e10.printStackTrace();
        } catch (XmlPullParserException e11) {
            e11.printStackTrace();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:63:0x0179, code lost:
    
        continue;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:27:0x0093. Please report as an issue. */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void y(Context context, XmlPullParser xmlPullParser) {
        a o10;
        try {
            int eventType = xmlPullParser.getEventType();
            a aVar = null;
            while (eventType != 1) {
                if (eventType != 0) {
                    char c10 = 3;
                    if (eventType == 2) {
                        String name = xmlPullParser.getName();
                        switch (name.hashCode()) {
                            case -2025855158:
                                if (name.equals("Layout")) {
                                    c10 = 5;
                                    break;
                                }
                                break;
                            case -1984451626:
                                if (name.equals("Motion")) {
                                    c10 = 6;
                                    break;
                                }
                                break;
                            case -1269513683:
                                if (name.equals("PropertySet")) {
                                    break;
                                }
                                break;
                            case -1238332596:
                                if (name.equals("Transform")) {
                                    c10 = 4;
                                    break;
                                }
                                break;
                            case -71750448:
                                if (name.equals("Guideline")) {
                                    c10 = 1;
                                    break;
                                }
                                break;
                            case 1331510167:
                                if (name.equals("Barrier")) {
                                    c10 = 2;
                                    break;
                                }
                                break;
                            case 1791837707:
                                if (name.equals("CustomAttribute")) {
                                    c10 = 7;
                                    break;
                                }
                                break;
                            case 1803088381:
                                if (name.equals("Constraint")) {
                                    c10 = 0;
                                    break;
                                }
                                break;
                        }
                        c10 = 65535;
                        switch (c10) {
                            case 0:
                                o10 = o(context, Xml.asAttributeSet(xmlPullParser));
                                aVar = o10;
                                break;
                            case 1:
                                o10 = o(context, Xml.asAttributeSet(xmlPullParser));
                                b bVar = o10.f1955d;
                                bVar.f1959a = true;
                                bVar.f1961b = true;
                                aVar = o10;
                                break;
                            case 2:
                                o10 = o(context, Xml.asAttributeSet(xmlPullParser));
                                o10.f1955d.f1966d0 = 1;
                                aVar = o10;
                                break;
                            case 3:
                                if (aVar != null) {
                                    aVar.f1953b.b(context, Xml.asAttributeSet(xmlPullParser));
                                    break;
                                } else {
                                    throw new RuntimeException("XML parser error must be within a Constraint " + xmlPullParser.getLineNumber());
                                }
                            case 4:
                                if (aVar != null) {
                                    aVar.f1956e.b(context, Xml.asAttributeSet(xmlPullParser));
                                    break;
                                } else {
                                    throw new RuntimeException("XML parser error must be within a Constraint " + xmlPullParser.getLineNumber());
                                }
                            case 5:
                                if (aVar != null) {
                                    aVar.f1955d.b(context, Xml.asAttributeSet(xmlPullParser));
                                    break;
                                } else {
                                    throw new RuntimeException("XML parser error must be within a Constraint " + xmlPullParser.getLineNumber());
                                }
                            case 6:
                                if (aVar != null) {
                                    aVar.f1954c.b(context, Xml.asAttributeSet(xmlPullParser));
                                    break;
                                } else {
                                    throw new RuntimeException("XML parser error must be within a Constraint " + xmlPullParser.getLineNumber());
                                }
                            case 7:
                                if (aVar != null) {
                                    ConstraintAttribute.g(context, xmlPullParser, aVar.f1957f);
                                    break;
                                } else {
                                    throw new RuntimeException("XML parser error must be within a Constraint " + xmlPullParser.getLineNumber());
                                }
                        }
                    } else if (eventType != 3) {
                        continue;
                    } else {
                        String name2 = xmlPullParser.getName();
                        if ("ConstraintSet".equals(name2)) {
                            return;
                        }
                        if (name2.equalsIgnoreCase("Constraint")) {
                            this.f1951d.put(Integer.valueOf(aVar.f1952a), aVar);
                            aVar = null;
                        }
                    }
                } else {
                    xmlPullParser.getName();
                }
                eventType = xmlPullParser.next();
            }
        } catch (IOException e10) {
            e10.printStackTrace();
        } catch (XmlPullParserException e11) {
            e11.printStackTrace();
        }
    }
}
