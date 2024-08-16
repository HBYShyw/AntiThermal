package com.coui.appcompat.stepper;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.GestureDetectorCompat;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: LongPressProxy.java */
/* renamed from: com.coui.appcompat.stepper.b, reason: use source file name */
/* loaded from: classes.dex */
public class LongPressProxy {

    /* renamed from: a, reason: collision with root package name */
    private View f7733a;

    /* renamed from: b, reason: collision with root package name */
    private Runnable f7734b;

    /* renamed from: c, reason: collision with root package name */
    private GestureDetectorCompat f7735c;

    /* renamed from: e, reason: collision with root package name */
    private Handler f7737e = new b(Looper.getMainLooper());

    /* renamed from: d, reason: collision with root package name */
    private GestureDetector.OnGestureListener f7736d = new a();

    /* compiled from: LongPressProxy.java */
    /* renamed from: com.coui.appcompat.stepper.b$a */
    /* loaded from: classes.dex */
    class a extends GestureDetector.SimpleOnGestureListener {
        a() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            super.onLongPress(motionEvent);
            LongPressProxy.this.f7737e.sendEmptyMessage(2);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            LongPressProxy.this.f7737e.sendEmptyMessage(1);
            return true;
        }
    }

    /* compiled from: LongPressProxy.java */
    /* renamed from: com.coui.appcompat.stepper.b$b */
    /* loaded from: classes.dex */
    private class b extends Handler {
        public b(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i10 = message.what;
            if (i10 != 1) {
                if (i10 == 2 && LongPressProxy.this.f7733a.isEnabled()) {
                    LongPressProxy.this.f7734b.run();
                    sendEmptyMessageDelayed(2, 100L);
                    return;
                }
                return;
            }
            LongPressProxy.this.f7734b.run();
        }
    }

    public LongPressProxy(View view, Runnable runnable) {
        this.f7733a = view;
        this.f7734b = runnable;
        this.f7735c = new GestureDetectorCompat(this.f7733a.getContext(), this.f7736d);
        e();
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void e() {
        this.f7733a.setOnTouchListener(new View.OnTouchListener() { // from class: com.coui.appcompat.stepper.a
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean f10;
                f10 = LongPressProxy.this.f(view, motionEvent);
                return f10;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean f(View view, MotionEvent motionEvent) {
        this.f7735c.a(motionEvent);
        if (motionEvent.getActionMasked() == 3 || motionEvent.getActionMasked() == 1) {
            this.f7737e.removeMessages(2);
        }
        return true;
    }

    public void g() {
        this.f7737e.removeCallbacksAndMessages(null);
        this.f7737e = null;
        View view = this.f7733a;
        if (view != null) {
            view.setOnTouchListener(null);
            this.f7733a.removeCallbacks(this.f7734b);
            this.f7733a = null;
        }
        this.f7734b = null;
    }
}
