package g2;

import android.view.MotionEvent;
import android.view.View;

/* compiled from: PreciseClickHelper.java */
/* renamed from: g2.f, reason: use source file name */
/* loaded from: classes.dex */
public class PreciseClickHelper {

    /* renamed from: a, reason: collision with root package name */
    private View f11562a;

    /* renamed from: b, reason: collision with root package name */
    private c f11563b;

    /* renamed from: c, reason: collision with root package name */
    private Float[] f11564c = new Float[2];

    /* renamed from: d, reason: collision with root package name */
    private View.OnTouchListener f11565d = new a();

    /* renamed from: e, reason: collision with root package name */
    private View.OnClickListener f11566e = new b();

    /* compiled from: PreciseClickHelper.java */
    /* renamed from: g2.f$a */
    /* loaded from: classes.dex */
    class a implements View.OnTouchListener {
        a() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() == 0) {
                PreciseClickHelper.this.f11564c[0] = Float.valueOf(motionEvent.getX());
                PreciseClickHelper.this.f11564c[1] = Float.valueOf(motionEvent.getY());
            }
            return false;
        }
    }

    /* compiled from: PreciseClickHelper.java */
    /* renamed from: g2.f$b */
    /* loaded from: classes.dex */
    class b implements View.OnClickListener {
        b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (!i1.a.c(view.getContext()) && PreciseClickHelper.this.f11564c.length > 0 && PreciseClickHelper.this.f11564c[0] != null) {
                PreciseClickHelper.this.f11563b.a(view, PreciseClickHelper.this.f11564c[0].intValue(), PreciseClickHelper.this.f11564c[1].intValue());
                return;
            }
            PreciseClickHelper.this.f11563b.a(view, view.getWidth() / 2, view.getHeight() / 2);
        }
    }

    /* compiled from: PreciseClickHelper.java */
    /* renamed from: g2.f$c */
    /* loaded from: classes.dex */
    public interface c {
        void a(View view, int i10, int i11);
    }

    public PreciseClickHelper(View view, c cVar) {
        this.f11562a = view;
        this.f11563b = cVar;
    }

    public void c() {
        this.f11562a.setOnTouchListener(this.f11565d);
        this.f11562a.setOnClickListener(this.f11566e);
    }

    public void d() {
        this.f11562a.setOnClickListener(null);
        this.f11562a.setOnTouchListener(null);
    }
}
