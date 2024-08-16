package androidx.appcompat.widget;

/* compiled from: RtlSpacingHelper.java */
/* renamed from: androidx.appcompat.widget.a0, reason: use source file name */
/* loaded from: classes.dex */
class RtlSpacingHelper {

    /* renamed from: a, reason: collision with root package name */
    private int f1154a = 0;

    /* renamed from: b, reason: collision with root package name */
    private int f1155b = 0;

    /* renamed from: c, reason: collision with root package name */
    private int f1156c = Integer.MIN_VALUE;

    /* renamed from: d, reason: collision with root package name */
    private int f1157d = Integer.MIN_VALUE;

    /* renamed from: e, reason: collision with root package name */
    private int f1158e = 0;

    /* renamed from: f, reason: collision with root package name */
    private int f1159f = 0;

    /* renamed from: g, reason: collision with root package name */
    private boolean f1160g = false;

    /* renamed from: h, reason: collision with root package name */
    private boolean f1161h = false;

    public int a() {
        return this.f1160g ? this.f1154a : this.f1155b;
    }

    public int b() {
        return this.f1154a;
    }

    public int c() {
        return this.f1155b;
    }

    public int d() {
        return this.f1160g ? this.f1155b : this.f1154a;
    }

    public void e(int i10, int i11) {
        this.f1161h = false;
        if (i10 != Integer.MIN_VALUE) {
            this.f1158e = i10;
            this.f1154a = i10;
        }
        if (i11 != Integer.MIN_VALUE) {
            this.f1159f = i11;
            this.f1155b = i11;
        }
    }

    public void f(boolean z10) {
        if (z10 == this.f1160g) {
            return;
        }
        this.f1160g = z10;
        if (!this.f1161h) {
            this.f1154a = this.f1158e;
            this.f1155b = this.f1159f;
            return;
        }
        if (z10) {
            int i10 = this.f1157d;
            if (i10 == Integer.MIN_VALUE) {
                i10 = this.f1158e;
            }
            this.f1154a = i10;
            int i11 = this.f1156c;
            if (i11 == Integer.MIN_VALUE) {
                i11 = this.f1159f;
            }
            this.f1155b = i11;
            return;
        }
        int i12 = this.f1156c;
        if (i12 == Integer.MIN_VALUE) {
            i12 = this.f1158e;
        }
        this.f1154a = i12;
        int i13 = this.f1157d;
        if (i13 == Integer.MIN_VALUE) {
            i13 = this.f1159f;
        }
        this.f1155b = i13;
    }

    public void g(int i10, int i11) {
        this.f1156c = i10;
        this.f1157d = i11;
        this.f1161h = true;
        if (this.f1160g) {
            if (i11 != Integer.MIN_VALUE) {
                this.f1154a = i11;
            }
            if (i10 != Integer.MIN_VALUE) {
                this.f1155b = i10;
                return;
            }
            return;
        }
        if (i10 != Integer.MIN_VALUE) {
            this.f1154a = i10;
        }
        if (i11 != Integer.MIN_VALUE) {
            this.f1155b = i11;
        }
    }
}
