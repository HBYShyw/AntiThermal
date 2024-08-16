package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.R$id;
import androidx.constraintlayout.widget.R$styleable;
import androidx.constraintlayout.widget.StateSet;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneFlightData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import k.Easing;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: MotionScene.java */
/* renamed from: androidx.constraintlayout.motion.widget.q, reason: use source file name */
/* loaded from: classes.dex */
public class MotionScene {

    /* renamed from: a, reason: collision with root package name */
    private final MotionLayout f1671a;

    /* renamed from: n, reason: collision with root package name */
    private MotionEvent f1684n;

    /* renamed from: p, reason: collision with root package name */
    private MotionLayout.f f1686p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f1687q;

    /* renamed from: r, reason: collision with root package name */
    float f1688r;

    /* renamed from: s, reason: collision with root package name */
    float f1689s;

    /* renamed from: b, reason: collision with root package name */
    StateSet f1672b = null;

    /* renamed from: c, reason: collision with root package name */
    b f1673c = null;

    /* renamed from: d, reason: collision with root package name */
    private boolean f1674d = false;

    /* renamed from: e, reason: collision with root package name */
    private ArrayList<b> f1675e = new ArrayList<>();

    /* renamed from: f, reason: collision with root package name */
    private b f1676f = null;

    /* renamed from: g, reason: collision with root package name */
    private ArrayList<b> f1677g = new ArrayList<>();

    /* renamed from: h, reason: collision with root package name */
    private SparseArray<ConstraintSet> f1678h = new SparseArray<>();

    /* renamed from: i, reason: collision with root package name */
    private HashMap<String, Integer> f1679i = new HashMap<>();

    /* renamed from: j, reason: collision with root package name */
    private SparseIntArray f1680j = new SparseIntArray();

    /* renamed from: k, reason: collision with root package name */
    private boolean f1681k = false;

    /* renamed from: l, reason: collision with root package name */
    private int f1682l = SceneFlightData.INVALID_LATITUDE_LONGITUDE;

    /* renamed from: m, reason: collision with root package name */
    private int f1683m = 0;

    /* renamed from: o, reason: collision with root package name */
    private boolean f1685o = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MotionScene.java */
    /* renamed from: androidx.constraintlayout.motion.widget.q$a */
    /* loaded from: classes.dex */
    public class a implements Interpolator {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Easing f1690a;

        a(Easing easing) {
            this.f1690a = easing;
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f10) {
            return (float) this.f1690a.a(f10);
        }
    }

    public MotionScene(MotionLayout motionLayout) {
        this.f1671a = motionLayout;
    }

    private boolean C(int i10) {
        int i11 = this.f1680j.get(i10);
        int size = this.f1680j.size();
        while (i11 > 0) {
            if (i11 == i10) {
                return true;
            }
            int i12 = size - 1;
            if (size < 0) {
                return true;
            }
            i11 = this.f1680j.get(i11);
            size = i12;
        }
        return false;
    }

    private boolean D() {
        return this.f1686p != null;
    }

    private void E(Context context, int i10) {
        XmlResourceParser xml = context.getResources().getXml(i10);
        b bVar = null;
        try {
            int eventType = xml.getEventType();
            while (true) {
                char c10 = 1;
                if (eventType == 1) {
                    return;
                }
                if (eventType == 0) {
                    xml.getName();
                } else if (eventType == 2) {
                    String name = xml.getName();
                    if (this.f1681k) {
                        System.out.println("parsing = " + name);
                    }
                    switch (name.hashCode()) {
                        case -1349929691:
                            if (name.equals("ConstraintSet")) {
                                c10 = 5;
                                break;
                            }
                            break;
                        case -1239391468:
                            if (name.equals("KeyFrameSet")) {
                                c10 = 6;
                                break;
                            }
                            break;
                        case 269306229:
                            if (name.equals("Transition")) {
                                break;
                            }
                            break;
                        case 312750793:
                            if (name.equals("OnClick")) {
                                c10 = 3;
                                break;
                            }
                            break;
                        case 327855227:
                            if (name.equals("OnSwipe")) {
                                c10 = 2;
                                break;
                            }
                            break;
                        case 793277014:
                            if (name.equals("MotionScene")) {
                                c10 = 0;
                                break;
                            }
                            break;
                        case 1382829617:
                            if (name.equals("StateSet")) {
                                c10 = 4;
                                break;
                            }
                            break;
                    }
                    c10 = 65535;
                    switch (c10) {
                        case 0:
                            G(context, xml);
                            break;
                        case 1:
                            ArrayList<b> arrayList = this.f1675e;
                            b bVar2 = new b(this, context, xml);
                            arrayList.add(bVar2);
                            if (this.f1673c == null && !bVar2.f1693b) {
                                this.f1673c = bVar2;
                                if (bVar2.f1703l != null) {
                                    this.f1673c.f1703l.p(this.f1687q);
                                }
                            }
                            if (bVar2.f1693b) {
                                if (bVar2.f1694c == -1) {
                                    this.f1676f = bVar2;
                                } else {
                                    this.f1677g.add(bVar2);
                                }
                                this.f1675e.remove(bVar2);
                            }
                            bVar = bVar2;
                            break;
                        case 2:
                            if (bVar == null) {
                                Log.v("MotionScene", " OnSwipe (" + context.getResources().getResourceEntryName(i10) + ".xml:" + xml.getLineNumber() + ")");
                            }
                            bVar.f1703l = new TouchResponse(context, this.f1671a, xml);
                            break;
                        case 3:
                            bVar.t(context, xml);
                            break;
                        case 4:
                            this.f1672b = new StateSet(context, xml);
                            break;
                        case 5:
                            F(context, xml);
                            break;
                        case 6:
                            bVar.f1702k.add(new KeyFrames(context, xml));
                            break;
                        default:
                            Log.v("MotionScene", "WARNING UNKNOWN ATTRIBUTE " + name);
                            break;
                    }
                }
                eventType = xml.next();
            }
        } catch (IOException e10) {
            e10.printStackTrace();
        } catch (XmlPullParserException e11) {
            e11.printStackTrace();
        }
    }

    private void F(Context context, XmlPullParser xmlPullParser) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.F(false);
        int attributeCount = xmlPullParser.getAttributeCount();
        int i10 = -1;
        int i11 = -1;
        for (int i12 = 0; i12 < attributeCount; i12++) {
            String attributeName = xmlPullParser.getAttributeName(i12);
            String attributeValue = xmlPullParser.getAttributeValue(i12);
            if (this.f1681k) {
                System.out.println("id string = " + attributeValue);
            }
            attributeName.hashCode();
            if (attributeName.equals("deriveConstraintsFrom")) {
                i11 = p(context, attributeValue);
            } else if (attributeName.equals("id")) {
                i10 = p(context, attributeValue);
                this.f1679i.put(S(attributeValue), Integer.valueOf(i10));
            }
        }
        if (i10 != -1) {
            if (this.f1671a.f1397a0 != 0) {
                constraintSet.J(true);
            }
            constraintSet.y(context, xmlPullParser);
            if (i11 != -1) {
                this.f1680j.put(i10, i11);
            }
            this.f1678h.put(i10, constraintSet);
        }
    }

    private void G(Context context, XmlPullParser xmlPullParser) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), R$styleable.MotionScene);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i10 = 0; i10 < indexCount; i10++) {
            int index = obtainStyledAttributes.getIndex(i10);
            if (index == R$styleable.MotionScene_defaultDuration) {
                this.f1682l = obtainStyledAttributes.getInt(index, this.f1682l);
            } else if (index == R$styleable.MotionScene_layoutDuringTransition) {
                this.f1683m = obtainStyledAttributes.getInteger(index, 0);
            }
        }
        obtainStyledAttributes.recycle();
    }

    private void K(int i10) {
        int i11 = this.f1680j.get(i10);
        if (i11 > 0) {
            K(this.f1680j.get(i10));
            ConstraintSet constraintSet = this.f1678h.get(i10);
            ConstraintSet constraintSet2 = this.f1678h.get(i11);
            if (constraintSet2 == null) {
                Log.e("MotionScene", "ERROR! invalid deriveConstraintsFrom: @id/" + androidx.constraintlayout.motion.widget.a.b(this.f1671a.getContext(), i11));
                return;
            }
            constraintSet.C(constraintSet2);
            this.f1680j.put(i10, -1);
        }
    }

    public static String S(String str) {
        if (str == null) {
            return "";
        }
        int indexOf = str.indexOf(47);
        return indexOf < 0 ? str : str.substring(indexOf + 1);
    }

    private int p(Context context, String str) {
        int i10;
        if (str.contains("/")) {
            i10 = context.getResources().getIdentifier(str.substring(str.indexOf(47) + 1), "id", context.getPackageName());
            if (this.f1681k) {
                System.out.println("id getMap res = " + i10);
            }
        } else {
            i10 = -1;
        }
        if (i10 != -1) {
            return i10;
        }
        if (str.length() > 1) {
            return Integer.parseInt(str.substring(1));
        }
        Log.e("MotionScene", "error in parsing id");
        return i10;
    }

    private int q(b bVar) {
        int i10 = bVar.f1692a;
        if (i10 != -1) {
            for (int i11 = 0; i11 < this.f1675e.size(); i11++) {
                if (this.f1675e.get(i11).f1692a == i10) {
                    return i11;
                }
            }
            return -1;
        }
        throw new IllegalArgumentException("The transition must have an id");
    }

    private int x(int i10) {
        int c10;
        StateSet stateSet = this.f1672b;
        return (stateSet == null || (c10 = stateSet.c(i10, -1, -1)) == -1) ? i10 : c10;
    }

    public b A(int i10) {
        Iterator<b> it = this.f1675e.iterator();
        while (it.hasNext()) {
            b next = it.next();
            if (next.f1692a == i10) {
                return next;
            }
        }
        return null;
    }

    public List<b> B(int i10) {
        int x10 = x(i10);
        ArrayList arrayList = new ArrayList();
        Iterator<b> it = this.f1675e.iterator();
        while (it.hasNext()) {
            b next = it.next();
            if (next.f1695d == x10 || next.f1694c == x10) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void H(float f10, float f11) {
        b bVar = this.f1673c;
        if (bVar == null || bVar.f1703l == null) {
            return;
        }
        this.f1673c.f1703l.m(f10, f11);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void I(float f10, float f11) {
        b bVar = this.f1673c;
        if (bVar == null || bVar.f1703l == null) {
            return;
        }
        this.f1673c.f1703l.n(f10, f11);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J(MotionEvent motionEvent, int i10, MotionLayout motionLayout) {
        MotionLayout.f fVar;
        MotionEvent motionEvent2;
        RectF rectF = new RectF();
        if (this.f1686p == null) {
            this.f1686p = this.f1671a.p0();
        }
        this.f1686p.b(motionEvent);
        if (i10 != -1) {
            int action = motionEvent.getAction();
            boolean z10 = false;
            if (action == 0) {
                this.f1688r = motionEvent.getRawX();
                this.f1689s = motionEvent.getRawY();
                this.f1684n = motionEvent;
                if (this.f1673c.f1703l != null) {
                    RectF e10 = this.f1673c.f1703l.e(this.f1671a, rectF);
                    if (e10 != null && !e10.contains(this.f1684n.getX(), this.f1684n.getY())) {
                        this.f1684n = null;
                        return;
                    }
                    RectF j10 = this.f1673c.f1703l.j(this.f1671a, rectF);
                    if (j10 != null && !j10.contains(this.f1684n.getX(), this.f1684n.getY())) {
                        this.f1685o = true;
                    } else {
                        this.f1685o = false;
                    }
                    this.f1673c.f1703l.o(this.f1688r, this.f1689s);
                    return;
                }
                return;
            }
            if (action == 2) {
                float rawY = motionEvent.getRawY() - this.f1689s;
                float rawX = motionEvent.getRawX() - this.f1688r;
                if ((rawX == UserProfileInfo.Constant.NA_LAT_LON && rawY == UserProfileInfo.Constant.NA_LAT_LON) || (motionEvent2 = this.f1684n) == null) {
                    return;
                }
                b h10 = h(i10, rawX, rawY, motionEvent2);
                if (h10 != null) {
                    motionLayout.setTransition(h10);
                    RectF j11 = this.f1673c.f1703l.j(this.f1671a, rectF);
                    if (j11 != null && !j11.contains(this.f1684n.getX(), this.f1684n.getY())) {
                        z10 = true;
                    }
                    this.f1685o = z10;
                    this.f1673c.f1703l.q(this.f1688r, this.f1689s);
                }
            }
        }
        b bVar = this.f1673c;
        if (bVar != null && bVar.f1703l != null && !this.f1685o) {
            this.f1673c.f1703l.l(motionEvent, this.f1686p, i10, this);
        }
        this.f1688r = motionEvent.getRawX();
        this.f1689s = motionEvent.getRawY();
        if (motionEvent.getAction() != 1 || (fVar = this.f1686p) == null) {
            return;
        }
        fVar.a();
        this.f1686p = null;
        int i11 = motionLayout.F;
        if (i11 != -1) {
            g(motionLayout, i11);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void L(MotionLayout motionLayout) {
        for (int i10 = 0; i10 < this.f1678h.size(); i10++) {
            int keyAt = this.f1678h.keyAt(i10);
            if (C(keyAt)) {
                Log.e("MotionScene", "Cannot be derived from yourself");
                return;
            }
            K(keyAt);
        }
        for (int i11 = 0; i11 < this.f1678h.size(); i11++) {
            this.f1678h.valueAt(i11).B(motionLayout);
        }
    }

    public void M(int i10, ConstraintSet constraintSet) {
        this.f1678h.put(i10, constraintSet);
    }

    public void N(int i10) {
        b bVar = this.f1673c;
        if (bVar != null) {
            bVar.E(i10);
        } else {
            this.f1682l = i10;
        }
    }

    public void O(boolean z10) {
        this.f1687q = z10;
        b bVar = this.f1673c;
        if (bVar == null || bVar.f1703l == null) {
            return;
        }
        this.f1673c.f1703l.p(this.f1687q);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0013, code lost:
    
        if (r2 != (-1)) goto L13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void P(int i10, int i11) {
        int i12;
        int i13;
        StateSet stateSet = this.f1672b;
        if (stateSet != null) {
            i12 = stateSet.c(i10, -1, -1);
            if (i12 == -1) {
                i12 = i10;
            }
            i13 = this.f1672b.c(i11, -1, -1);
        } else {
            i12 = i10;
        }
        i13 = i11;
        Iterator<b> it = this.f1675e.iterator();
        while (it.hasNext()) {
            b next = it.next();
            if ((next.f1694c == i13 && next.f1695d == i12) || (next.f1694c == i11 && next.f1695d == i10)) {
                this.f1673c = next;
                if (next == null || next.f1703l == null) {
                    return;
                }
                this.f1673c.f1703l.p(this.f1687q);
                return;
            }
        }
        b bVar = this.f1676f;
        Iterator<b> it2 = this.f1677g.iterator();
        while (it2.hasNext()) {
            b next2 = it2.next();
            if (next2.f1694c == i11) {
                bVar = next2;
            }
        }
        b bVar2 = new b(this, bVar);
        bVar2.f1695d = i12;
        bVar2.f1694c = i13;
        if (i12 != -1) {
            this.f1675e.add(bVar2);
        }
        this.f1673c = bVar2;
    }

    public void Q(b bVar) {
        this.f1673c = bVar;
        if (bVar == null || bVar.f1703l == null) {
            return;
        }
        this.f1673c.f1703l.p(this.f1687q);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void R() {
        b bVar = this.f1673c;
        if (bVar == null || bVar.f1703l == null) {
            return;
        }
        this.f1673c.f1703l.r();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean T() {
        Iterator<b> it = this.f1675e.iterator();
        while (it.hasNext()) {
            if (it.next().f1703l != null) {
                return true;
            }
        }
        b bVar = this.f1673c;
        return (bVar == null || bVar.f1703l == null) ? false : true;
    }

    public void e(MotionLayout motionLayout, int i10) {
        Iterator<b> it = this.f1675e.iterator();
        while (it.hasNext()) {
            b next = it.next();
            if (next.f1704m.size() > 0) {
                Iterator it2 = next.f1704m.iterator();
                while (it2.hasNext()) {
                    ((b.a) it2.next()).c(motionLayout);
                }
            }
        }
        Iterator<b> it3 = this.f1677g.iterator();
        while (it3.hasNext()) {
            b next2 = it3.next();
            if (next2.f1704m.size() > 0) {
                Iterator it4 = next2.f1704m.iterator();
                while (it4.hasNext()) {
                    ((b.a) it4.next()).c(motionLayout);
                }
            }
        }
        Iterator<b> it5 = this.f1675e.iterator();
        while (it5.hasNext()) {
            b next3 = it5.next();
            if (next3.f1704m.size() > 0) {
                Iterator it6 = next3.f1704m.iterator();
                while (it6.hasNext()) {
                    ((b.a) it6.next()).a(motionLayout, i10, next3);
                }
            }
        }
        Iterator<b> it7 = this.f1677g.iterator();
        while (it7.hasNext()) {
            b next4 = it7.next();
            if (next4.f1704m.size() > 0) {
                Iterator it8 = next4.f1704m.iterator();
                while (it8.hasNext()) {
                    ((b.a) it8.next()).a(motionLayout, i10, next4);
                }
            }
        }
    }

    public void f(b bVar) {
        int q10 = q(bVar);
        if (q10 == -1) {
            this.f1675e.add(bVar);
        } else {
            this.f1675e.set(q10, bVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean g(MotionLayout motionLayout, int i10) {
        if (D() || this.f1674d) {
            return false;
        }
        Iterator<b> it = this.f1675e.iterator();
        while (it.hasNext()) {
            b next = it.next();
            if (next.f1705n != 0) {
                if (i10 == next.f1695d && (next.f1705n == 4 || next.f1705n == 2)) {
                    MotionLayout.j jVar = MotionLayout.j.FINISHED;
                    motionLayout.setState(jVar);
                    motionLayout.setTransition(next);
                    if (next.f1705n == 4) {
                        motionLayout.y0();
                        motionLayout.setState(MotionLayout.j.SETUP);
                        motionLayout.setState(MotionLayout.j.MOVING);
                    } else {
                        motionLayout.setProgress(1.0f);
                        motionLayout.d0(true);
                        motionLayout.setState(MotionLayout.j.SETUP);
                        motionLayout.setState(MotionLayout.j.MOVING);
                        motionLayout.setState(jVar);
                    }
                    return true;
                }
                if (i10 == next.f1694c && (next.f1705n == 3 || next.f1705n == 1)) {
                    MotionLayout.j jVar2 = MotionLayout.j.FINISHED;
                    motionLayout.setState(jVar2);
                    motionLayout.setTransition(next);
                    if (next.f1705n == 3) {
                        motionLayout.z0();
                        motionLayout.setState(MotionLayout.j.SETUP);
                        motionLayout.setState(MotionLayout.j.MOVING);
                    } else {
                        motionLayout.setProgress(0.0f);
                        motionLayout.d0(true);
                        motionLayout.setState(MotionLayout.j.SETUP);
                        motionLayout.setState(MotionLayout.j.MOVING);
                        motionLayout.setState(jVar2);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public b h(int i10, float f10, float f11, MotionEvent motionEvent) {
        if (i10 != -1) {
            List<b> B = B(i10);
            float f12 = 0.0f;
            b bVar = null;
            RectF rectF = new RectF();
            for (b bVar2 : B) {
                if (!bVar2.f1706o && bVar2.f1703l != null) {
                    bVar2.f1703l.p(this.f1687q);
                    RectF j10 = bVar2.f1703l.j(this.f1671a, rectF);
                    if (j10 == null || motionEvent == null || j10.contains(motionEvent.getX(), motionEvent.getY())) {
                        RectF j11 = bVar2.f1703l.j(this.f1671a, rectF);
                        if (j11 == null || motionEvent == null || j11.contains(motionEvent.getX(), motionEvent.getY())) {
                            float a10 = bVar2.f1703l.a(f10, f11) * (bVar2.f1694c == i10 ? -1.0f : 1.1f);
                            if (a10 > f12) {
                                bVar = bVar2;
                                f12 = a10;
                            }
                        }
                    }
                }
            }
            return bVar;
        }
        return this.f1673c;
    }

    public int i() {
        b bVar = this.f1673c;
        if (bVar != null) {
            return bVar.f1707p;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConstraintSet j(int i10) {
        return k(i10, -1, -1);
    }

    ConstraintSet k(int i10, int i11, int i12) {
        int c10;
        if (this.f1681k) {
            System.out.println("id " + i10);
            System.out.println("size " + this.f1678h.size());
        }
        StateSet stateSet = this.f1672b;
        if (stateSet != null && (c10 = stateSet.c(i10, i11, i12)) != -1) {
            i10 = c10;
        }
        if (this.f1678h.get(i10) == null) {
            Log.e("MotionScene", "Warning could not find ConstraintSet id/" + androidx.constraintlayout.motion.widget.a.b(this.f1671a.getContext(), i10) + " In MotionScene");
            SparseArray<ConstraintSet> sparseArray = this.f1678h;
            return sparseArray.get(sparseArray.keyAt(0));
        }
        return this.f1678h.get(i10);
    }

    public int[] l() {
        int size = this.f1678h.size();
        int[] iArr = new int[size];
        for (int i10 = 0; i10 < size; i10++) {
            iArr[i10] = this.f1678h.keyAt(i10);
        }
        return iArr;
    }

    public ArrayList<b> m() {
        return this.f1675e;
    }

    public int n() {
        b bVar = this.f1673c;
        if (bVar != null) {
            return bVar.f1699h;
        }
        return this.f1682l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int o() {
        b bVar = this.f1673c;
        if (bVar == null) {
            return -1;
        }
        return bVar.f1694c;
    }

    public Interpolator r() {
        int i10 = this.f1673c.f1696e;
        if (i10 == -2) {
            return AnimationUtils.loadInterpolator(this.f1671a.getContext(), this.f1673c.f1698g);
        }
        if (i10 == -1) {
            return new a(Easing.c(this.f1673c.f1697f));
        }
        if (i10 == 0) {
            return new AccelerateDecelerateInterpolator();
        }
        if (i10 == 1) {
            return new AccelerateInterpolator();
        }
        if (i10 == 2) {
            return new DecelerateInterpolator();
        }
        if (i10 == 4) {
            return new AnticipateInterpolator();
        }
        if (i10 != 5) {
            return null;
        }
        return new BounceInterpolator();
    }

    public void s(MotionController motionController) {
        b bVar = this.f1673c;
        if (bVar == null) {
            b bVar2 = this.f1676f;
            if (bVar2 != null) {
                Iterator it = bVar2.f1702k.iterator();
                while (it.hasNext()) {
                    ((KeyFrames) it.next()).a(motionController);
                }
                return;
            }
            return;
        }
        Iterator it2 = bVar.f1702k.iterator();
        while (it2.hasNext()) {
            ((KeyFrames) it2.next()).a(motionController);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float t() {
        b bVar = this.f1673c;
        if (bVar == null || bVar.f1703l == null) {
            return 0.0f;
        }
        return this.f1673c.f1703l.f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float u() {
        b bVar = this.f1673c;
        if (bVar == null || bVar.f1703l == null) {
            return 0.0f;
        }
        return this.f1673c.f1703l.g();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean v() {
        b bVar = this.f1673c;
        if (bVar == null || bVar.f1703l == null) {
            return false;
        }
        return this.f1673c.f1703l.h();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float w(float f10, float f11) {
        b bVar = this.f1673c;
        if (bVar == null || bVar.f1703l == null) {
            return 0.0f;
        }
        return this.f1673c.f1703l.i(f10, f11);
    }

    public float y() {
        b bVar = this.f1673c;
        if (bVar != null) {
            return bVar.f1700i;
        }
        return 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int z() {
        b bVar = this.f1673c;
        if (bVar == null) {
            return -1;
        }
        return bVar.f1695d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MotionScene(Context context, MotionLayout motionLayout, int i10) {
        this.f1671a = motionLayout;
        E(context, i10);
        SparseArray<ConstraintSet> sparseArray = this.f1678h;
        int i11 = R$id.motion_base;
        sparseArray.put(i11, new ConstraintSet());
        this.f1679i.put("motion_base", Integer.valueOf(i11));
    }

    /* compiled from: MotionScene.java */
    /* renamed from: androidx.constraintlayout.motion.widget.q$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        private int f1692a;

        /* renamed from: b, reason: collision with root package name */
        private boolean f1693b;

        /* renamed from: c, reason: collision with root package name */
        private int f1694c;

        /* renamed from: d, reason: collision with root package name */
        private int f1695d;

        /* renamed from: e, reason: collision with root package name */
        private int f1696e;

        /* renamed from: f, reason: collision with root package name */
        private String f1697f;

        /* renamed from: g, reason: collision with root package name */
        private int f1698g;

        /* renamed from: h, reason: collision with root package name */
        private int f1699h;

        /* renamed from: i, reason: collision with root package name */
        private float f1700i;

        /* renamed from: j, reason: collision with root package name */
        private final MotionScene f1701j;

        /* renamed from: k, reason: collision with root package name */
        private ArrayList<KeyFrames> f1702k;

        /* renamed from: l, reason: collision with root package name */
        private TouchResponse f1703l;

        /* renamed from: m, reason: collision with root package name */
        private ArrayList<a> f1704m;

        /* renamed from: n, reason: collision with root package name */
        private int f1705n;

        /* renamed from: o, reason: collision with root package name */
        private boolean f1706o;

        /* renamed from: p, reason: collision with root package name */
        private int f1707p;

        /* renamed from: q, reason: collision with root package name */
        private int f1708q;

        /* renamed from: r, reason: collision with root package name */
        private int f1709r;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: MotionScene.java */
        /* renamed from: androidx.constraintlayout.motion.widget.q$b$a */
        /* loaded from: classes.dex */
        public static class a implements View.OnClickListener {

            /* renamed from: e, reason: collision with root package name */
            private final b f1710e;

            /* renamed from: f, reason: collision with root package name */
            int f1711f;

            /* renamed from: g, reason: collision with root package name */
            int f1712g;

            public a(Context context, b bVar, XmlPullParser xmlPullParser) {
                this.f1711f = -1;
                this.f1712g = 17;
                this.f1710e = bVar;
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), R$styleable.OnClick);
                int indexCount = obtainStyledAttributes.getIndexCount();
                for (int i10 = 0; i10 < indexCount; i10++) {
                    int index = obtainStyledAttributes.getIndex(i10);
                    if (index == R$styleable.OnClick_targetId) {
                        this.f1711f = obtainStyledAttributes.getResourceId(index, this.f1711f);
                    } else if (index == R$styleable.OnClick_clickAction) {
                        this.f1712g = obtainStyledAttributes.getInt(index, this.f1712g);
                    }
                }
                obtainStyledAttributes.recycle();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r7v4, types: [android.view.View] */
            public void a(MotionLayout motionLayout, int i10, b bVar) {
                int i11 = this.f1711f;
                MotionLayout motionLayout2 = motionLayout;
                if (i11 != -1) {
                    motionLayout2 = motionLayout.findViewById(i11);
                }
                if (motionLayout2 == null) {
                    Log.e("MotionScene", "OnClick could not find id " + this.f1711f);
                    return;
                }
                int i12 = bVar.f1695d;
                int i13 = bVar.f1694c;
                if (i12 == -1) {
                    motionLayout2.setOnClickListener(this);
                    return;
                }
                int i14 = this.f1712g;
                boolean z10 = false;
                boolean z11 = ((i14 & 1) != 0 && i10 == i12) | ((i14 & 1) != 0 && i10 == i12) | ((i14 & 256) != 0 && i10 == i12) | ((i14 & 16) != 0 && i10 == i13);
                if ((i14 & 4096) != 0 && i10 == i13) {
                    z10 = true;
                }
                if (z11 || z10) {
                    motionLayout2.setOnClickListener(this);
                }
            }

            boolean b(b bVar, MotionLayout motionLayout) {
                b bVar2 = this.f1710e;
                if (bVar2 == bVar) {
                    return true;
                }
                int i10 = bVar2.f1694c;
                int i11 = this.f1710e.f1695d;
                if (i11 == -1) {
                    return motionLayout.F != i10;
                }
                int i12 = motionLayout.F;
                return i12 == i11 || i12 == i10;
            }

            public void c(MotionLayout motionLayout) {
                int i10 = this.f1711f;
                if (i10 == -1) {
                    return;
                }
                View findViewById = motionLayout.findViewById(i10);
                if (findViewById == null) {
                    Log.e("MotionScene", " (*)  could not find id " + this.f1711f);
                    return;
                }
                findViewById.setOnClickListener(null);
            }

            /* JADX WARN: Removed duplicated region for block: B:37:0x00a3  */
            /* JADX WARN: Removed duplicated region for block: B:59:? A[RETURN, SYNTHETIC] */
            @Override // android.view.View.OnClickListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void onClick(View view) {
                MotionLayout motionLayout = this.f1710e.f1701j.f1671a;
                if (!motionLayout.o0()) {
                    return;
                }
                if (this.f1710e.f1695d == -1) {
                    int currentState = motionLayout.getCurrentState();
                    if (currentState == -1) {
                        motionLayout.A0(this.f1710e.f1694c);
                        return;
                    }
                    b bVar = new b(this.f1710e.f1701j, this.f1710e);
                    bVar.f1695d = currentState;
                    bVar.f1694c = this.f1710e.f1694c;
                    motionLayout.setTransition(bVar);
                    motionLayout.y0();
                    return;
                }
                b bVar2 = this.f1710e.f1701j.f1673c;
                int i10 = this.f1712g;
                boolean z10 = false;
                boolean z11 = ((i10 & 1) == 0 && (i10 & 256) == 0) ? false : true;
                boolean z12 = ((i10 & 16) == 0 && (i10 & 4096) == 0) ? false : true;
                if (z11 && z12) {
                    b bVar3 = this.f1710e.f1701j.f1673c;
                    b bVar4 = this.f1710e;
                    if (bVar3 != bVar4) {
                        motionLayout.setTransition(bVar4);
                    }
                    if (motionLayout.getCurrentState() != motionLayout.getEndState() && motionLayout.getProgress() <= 0.5f) {
                        z12 = false;
                    }
                    if (b(bVar2, motionLayout)) {
                        return;
                    }
                    if (z10 && (this.f1712g & 1) != 0) {
                        motionLayout.setTransition(this.f1710e);
                        motionLayout.y0();
                        return;
                    }
                    if (z12 && (this.f1712g & 16) != 0) {
                        motionLayout.setTransition(this.f1710e);
                        motionLayout.z0();
                        return;
                    } else if (z10 && (this.f1712g & 256) != 0) {
                        motionLayout.setTransition(this.f1710e);
                        motionLayout.setProgress(1.0f);
                        return;
                    } else {
                        if (!z12 || (this.f1712g & 4096) == 0) {
                            return;
                        }
                        motionLayout.setTransition(this.f1710e);
                        motionLayout.setProgress(0.0f);
                        return;
                    }
                }
                z10 = z11;
                if (b(bVar2, motionLayout)) {
                }
            }
        }

        b(MotionScene motionScene, b bVar) {
            this.f1692a = -1;
            this.f1693b = false;
            this.f1694c = -1;
            this.f1695d = -1;
            this.f1696e = 0;
            this.f1697f = null;
            this.f1698g = -1;
            this.f1699h = SceneFlightData.INVALID_LATITUDE_LONGITUDE;
            this.f1700i = 0.0f;
            this.f1702k = new ArrayList<>();
            this.f1703l = null;
            this.f1704m = new ArrayList<>();
            this.f1705n = 0;
            this.f1706o = false;
            this.f1707p = -1;
            this.f1708q = 0;
            this.f1709r = 0;
            this.f1701j = motionScene;
            if (bVar != null) {
                this.f1707p = bVar.f1707p;
                this.f1696e = bVar.f1696e;
                this.f1697f = bVar.f1697f;
                this.f1698g = bVar.f1698g;
                this.f1699h = bVar.f1699h;
                this.f1702k = bVar.f1702k;
                this.f1700i = bVar.f1700i;
                this.f1708q = bVar.f1708q;
            }
        }

        private void v(MotionScene motionScene, Context context, TypedArray typedArray) {
            int indexCount = typedArray.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = typedArray.getIndex(i10);
                if (index == R$styleable.Transition_constraintSetEnd) {
                    this.f1694c = typedArray.getResourceId(index, this.f1694c);
                    if ("layout".equals(context.getResources().getResourceTypeName(this.f1694c))) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.x(context, this.f1694c);
                        motionScene.f1678h.append(this.f1694c, constraintSet);
                    }
                } else if (index == R$styleable.Transition_constraintSetStart) {
                    this.f1695d = typedArray.getResourceId(index, this.f1695d);
                    if ("layout".equals(context.getResources().getResourceTypeName(this.f1695d))) {
                        ConstraintSet constraintSet2 = new ConstraintSet();
                        constraintSet2.x(context, this.f1695d);
                        motionScene.f1678h.append(this.f1695d, constraintSet2);
                    }
                } else if (index == R$styleable.Transition_motionInterpolator) {
                    int i11 = typedArray.peekValue(index).type;
                    if (i11 == 1) {
                        int resourceId = typedArray.getResourceId(index, -1);
                        this.f1698g = resourceId;
                        if (resourceId != -1) {
                            this.f1696e = -2;
                        }
                    } else if (i11 == 3) {
                        String string = typedArray.getString(index);
                        this.f1697f = string;
                        if (string.indexOf("/") > 0) {
                            this.f1698g = typedArray.getResourceId(index, -1);
                            this.f1696e = -2;
                        } else {
                            this.f1696e = -1;
                        }
                    } else {
                        this.f1696e = typedArray.getInteger(index, this.f1696e);
                    }
                } else if (index == R$styleable.Transition_duration) {
                    this.f1699h = typedArray.getInt(index, this.f1699h);
                } else if (index == R$styleable.Transition_staggered) {
                    this.f1700i = typedArray.getFloat(index, this.f1700i);
                } else if (index == R$styleable.Transition_autoTransition) {
                    this.f1705n = typedArray.getInteger(index, this.f1705n);
                } else if (index == R$styleable.Transition_android_id) {
                    this.f1692a = typedArray.getResourceId(index, this.f1692a);
                } else if (index == R$styleable.Transition_transitionDisable) {
                    this.f1706o = typedArray.getBoolean(index, this.f1706o);
                } else if (index == R$styleable.Transition_pathMotionArc) {
                    this.f1707p = typedArray.getInteger(index, -1);
                } else if (index == R$styleable.Transition_layoutDuringTransition) {
                    this.f1708q = typedArray.getInteger(index, 0);
                } else if (index == R$styleable.Transition_transitionFlags) {
                    this.f1709r = typedArray.getInteger(index, 0);
                }
            }
            if (this.f1695d == -1) {
                this.f1693b = true;
            }
        }

        private void w(MotionScene motionScene, Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Transition);
            v(motionScene, context, obtainStyledAttributes);
            obtainStyledAttributes.recycle();
        }

        public int A() {
            return this.f1695d;
        }

        public TouchResponse B() {
            return this.f1703l;
        }

        public boolean C() {
            return !this.f1706o;
        }

        public boolean D(int i10) {
            return (this.f1709r & i10) != 0;
        }

        public void E(int i10) {
            this.f1699h = i10;
        }

        public void t(Context context, XmlPullParser xmlPullParser) {
            this.f1704m.add(new a(context, this, xmlPullParser));
        }

        public String u(Context context) {
            String resourceEntryName = this.f1695d == -1 ? "null" : context.getResources().getResourceEntryName(this.f1695d);
            if (this.f1694c == -1) {
                return resourceEntryName + " -> null";
            }
            return resourceEntryName + " -> " + context.getResources().getResourceEntryName(this.f1694c);
        }

        public int x() {
            return this.f1699h;
        }

        public int y() {
            return this.f1694c;
        }

        public int z() {
            return this.f1708q;
        }

        public b(int i10, MotionScene motionScene, int i11, int i12) {
            this.f1692a = -1;
            this.f1693b = false;
            this.f1694c = -1;
            this.f1695d = -1;
            this.f1696e = 0;
            this.f1697f = null;
            this.f1698g = -1;
            this.f1699h = SceneFlightData.INVALID_LATITUDE_LONGITUDE;
            this.f1700i = 0.0f;
            this.f1702k = new ArrayList<>();
            this.f1703l = null;
            this.f1704m = new ArrayList<>();
            this.f1705n = 0;
            this.f1706o = false;
            this.f1707p = -1;
            this.f1708q = 0;
            this.f1709r = 0;
            this.f1692a = i10;
            this.f1701j = motionScene;
            this.f1695d = i11;
            this.f1694c = i12;
            this.f1699h = motionScene.f1682l;
            this.f1708q = motionScene.f1683m;
        }

        b(MotionScene motionScene, Context context, XmlPullParser xmlPullParser) {
            this.f1692a = -1;
            this.f1693b = false;
            this.f1694c = -1;
            this.f1695d = -1;
            this.f1696e = 0;
            this.f1697f = null;
            this.f1698g = -1;
            this.f1699h = SceneFlightData.INVALID_LATITUDE_LONGITUDE;
            this.f1700i = 0.0f;
            this.f1702k = new ArrayList<>();
            this.f1703l = null;
            this.f1704m = new ArrayList<>();
            this.f1705n = 0;
            this.f1706o = false;
            this.f1707p = -1;
            this.f1708q = 0;
            this.f1709r = 0;
            this.f1699h = motionScene.f1682l;
            this.f1708q = motionScene.f1683m;
            this.f1701j = motionScene;
            w(motionScene, context, Xml.asAttributeSet(xmlPullParser));
        }
    }
}
