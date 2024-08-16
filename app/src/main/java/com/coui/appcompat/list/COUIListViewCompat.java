package com.coui.appcompat.list;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class COUIListViewCompat extends ListView {

    /* renamed from: l, reason: collision with root package name */
    private static final int[] f6279l = {0};

    /* renamed from: e, reason: collision with root package name */
    final Rect f6280e;

    /* renamed from: f, reason: collision with root package name */
    int f6281f;

    /* renamed from: g, reason: collision with root package name */
    int f6282g;

    /* renamed from: h, reason: collision with root package name */
    int f6283h;

    /* renamed from: i, reason: collision with root package name */
    int f6284i;

    /* renamed from: j, reason: collision with root package name */
    private Field f6285j;

    /* renamed from: k, reason: collision with root package name */
    private a f6286k;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class a extends DrawableWrapper {

        /* renamed from: a, reason: collision with root package name */
        private boolean f6287a;

        public a(Drawable drawable) {
            super(drawable);
            this.f6287a = true;
        }

        void a(boolean z10) {
            this.f6287a = z10;
        }
    }

    public COUIListViewCompat(Context context) {
        this(context, null);
    }

    protected void a(Canvas canvas) {
        Drawable selector;
        if (this.f6280e.isEmpty() || (selector = getSelector()) == null) {
            return;
        }
        selector.setBounds(this.f6280e);
        selector.draw(canvas);
    }

    protected boolean b() {
        return c() && isPressed();
    }

    protected boolean c() {
        return false;
    }

    protected void d() {
        Drawable selector = getSelector();
        if (selector == null || !b()) {
            return;
        }
        selector.setState(getDrawableState());
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        a(canvas);
        super.dispatchDraw(canvas);
    }

    @Override // android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        setSelectorEnabled(true);
        d();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.coui.appcompat.list.COUIListViewCompat$a, android.graphics.drawable.Drawable] */
    /* JADX WARN: Type inference failed for: r0v4 */
    @Override // android.widget.AbsListView
    public void setSelector(Drawable drawable) {
        ?? aVar = drawable != null ? new a(drawable) : 0;
        this.f6286k = aVar;
        super.setSelector((Drawable) aVar);
        Rect rect = new Rect();
        if (drawable != null) {
            drawable.getPadding(rect);
        }
        this.f6281f = rect.left;
        this.f6282g = rect.top;
        this.f6283h = rect.right;
        this.f6284i = rect.bottom;
    }

    protected void setSelectorEnabled(boolean z10) {
        a aVar = this.f6286k;
        if (aVar != null) {
            aVar.a(z10);
        }
    }

    public COUIListViewCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIListViewCompat(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f6280e = new Rect();
        this.f6281f = 0;
        this.f6282g = 0;
        this.f6283h = 0;
        this.f6284i = 0;
        try {
            Field declaredField = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
            this.f6285j = declaredField;
            declaredField.setAccessible(true);
        } catch (NoSuchFieldException e10) {
            e10.printStackTrace();
        }
    }
}
