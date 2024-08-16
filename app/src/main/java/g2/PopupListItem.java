package g2;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;

/* compiled from: PopupListItem.java */
/* renamed from: g2.e, reason: use source file name */
/* loaded from: classes.dex */
public class PopupListItem {

    /* renamed from: a, reason: collision with root package name */
    private int f11551a;

    /* renamed from: b, reason: collision with root package name */
    private Drawable f11552b;

    /* renamed from: c, reason: collision with root package name */
    private String f11553c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f11554d;

    /* renamed from: e, reason: collision with root package name */
    private boolean f11555e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f11556f;

    /* renamed from: g, reason: collision with root package name */
    private int f11557g;

    /* renamed from: h, reason: collision with root package name */
    private ArrayList<PopupListItem> f11558h;

    /* renamed from: i, reason: collision with root package name */
    private ColorStateList f11559i;

    /* renamed from: j, reason: collision with root package name */
    private int f11560j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f11561k;

    public PopupListItem(String str, boolean z10) {
        this(null, str, z10);
    }

    public Drawable a() {
        return this.f11552b;
    }

    public int b() {
        return this.f11551a;
    }

    public int c() {
        return this.f11557g;
    }

    public ArrayList<PopupListItem> d() {
        return this.f11558h;
    }

    public String e() {
        return this.f11553c;
    }

    public int f() {
        return this.f11560j;
    }

    public ColorStateList g() {
        return this.f11559i;
    }

    public boolean h() {
        ArrayList<PopupListItem> arrayList = this.f11558h;
        return (arrayList != null && arrayList.size() > 0) || this.f11561k;
    }

    public boolean i() {
        return this.f11555e;
    }

    public boolean j() {
        return this.f11556f;
    }

    public boolean k() {
        return this.f11554d;
    }

    public void l(boolean z10) {
        this.f11555e = z10;
    }

    public void m(boolean z10) {
        this.f11556f = z10;
    }

    public void n(boolean z10) {
        this.f11561k = z10;
    }

    public PopupListItem(Drawable drawable, String str, boolean z10) {
        this(drawable, str, z10, -1);
    }

    public PopupListItem(Drawable drawable, String str, boolean z10, int i10) {
        this(drawable, str, false, false, i10, z10);
    }

    public PopupListItem(Drawable drawable, String str, boolean z10, boolean z11, boolean z12) {
        this(drawable, str, z10, z11, -1, z12);
    }

    public PopupListItem(Drawable drawable, String str, boolean z10, boolean z11, int i10, boolean z12) {
        this(drawable, str, z10, z11, i10, z12, null);
    }

    public PopupListItem(Drawable drawable, String str, boolean z10, boolean z11, int i10, boolean z12, ArrayList<PopupListItem> arrayList) {
        this.f11560j = -1;
        this.f11552b = drawable;
        this.f11553c = str;
        this.f11555e = z10;
        this.f11556f = z11;
        this.f11554d = z12;
        this.f11557g = i10;
        this.f11558h = arrayList;
    }
}
