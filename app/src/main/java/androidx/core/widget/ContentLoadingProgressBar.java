package androidx.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/* loaded from: classes.dex */
public class ContentLoadingProgressBar extends ProgressBar {

    /* renamed from: e, reason: collision with root package name */
    long f2432e;

    /* renamed from: f, reason: collision with root package name */
    boolean f2433f;

    /* renamed from: g, reason: collision with root package name */
    boolean f2434g;

    /* renamed from: h, reason: collision with root package name */
    boolean f2435h;

    /* renamed from: i, reason: collision with root package name */
    private final Runnable f2436i;

    /* renamed from: j, reason: collision with root package name */
    private final Runnable f2437j;

    public ContentLoadingProgressBar(Context context) {
        this(context, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void c() {
        this.f2433f = false;
        this.f2432e = -1L;
        setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d() {
        this.f2434g = false;
        if (this.f2435h) {
            return;
        }
        this.f2432e = System.currentTimeMillis();
        setVisibility(0);
    }

    private void e() {
        removeCallbacks(this.f2436i);
        removeCallbacks(this.f2437j);
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        e();
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        e();
    }

    public ContentLoadingProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        this.f2432e = -1L;
        this.f2433f = false;
        this.f2434g = false;
        this.f2435h = false;
        this.f2436i = new Runnable() { // from class: androidx.core.widget.d
            @Override // java.lang.Runnable
            public final void run() {
                ContentLoadingProgressBar.this.c();
            }
        };
        this.f2437j = new Runnable() { // from class: androidx.core.widget.e
            @Override // java.lang.Runnable
            public final void run() {
                ContentLoadingProgressBar.this.d();
            }
        };
    }
}
