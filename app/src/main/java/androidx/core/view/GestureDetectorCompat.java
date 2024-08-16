package androidx.core.view;

import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;

/* compiled from: GestureDetectorCompat.java */
/* renamed from: androidx.core.view.d, reason: use source file name */
/* loaded from: classes.dex */
public final class GestureDetectorCompat {

    /* renamed from: a, reason: collision with root package name */
    private final a f2357a;

    /* compiled from: GestureDetectorCompat.java */
    /* renamed from: androidx.core.view.d$a */
    /* loaded from: classes.dex */
    interface a {
        boolean a(MotionEvent motionEvent);
    }

    /* compiled from: GestureDetectorCompat.java */
    /* renamed from: androidx.core.view.d$b */
    /* loaded from: classes.dex */
    static class b implements a {

        /* renamed from: a, reason: collision with root package name */
        private final GestureDetector f2358a;

        b(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
            this.f2358a = new GestureDetector(context, onGestureListener, handler);
        }

        @Override // androidx.core.view.GestureDetectorCompat.a
        public boolean a(MotionEvent motionEvent) {
            return this.f2358a.onTouchEvent(motionEvent);
        }
    }

    public GestureDetectorCompat(Context context, GestureDetector.OnGestureListener onGestureListener) {
        this(context, onGestureListener, null);
    }

    public boolean a(MotionEvent motionEvent) {
        return this.f2357a.a(motionEvent);
    }

    public GestureDetectorCompat(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
        this.f2357a = new b(context, onGestureListener, handler);
    }
}
