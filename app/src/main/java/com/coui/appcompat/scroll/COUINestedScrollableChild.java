package com.coui.appcompat.scroll;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import o2.COUIScrollViewProxy;

/* loaded from: classes.dex */
public class COUINestedScrollableChild extends COUINestedScrollableHost {
    public COUINestedScrollableChild(Context context) {
        super(context);
    }

    @Override // com.coui.appcompat.scroll.COUINestedScrollableHost
    protected void c() {
        this.f7327i = a(this.f7329k);
    }

    @Override // com.coui.appcompat.scroll.COUINestedScrollableHost
    protected void f(MotionEvent motionEvent) {
        COUIScrollViewProxy<?> cOUIScrollViewProxy = this.f7327i;
        if (cOUIScrollViewProxy == null) {
            return;
        }
        int a10 = cOUIScrollViewProxy.a();
        if (this.f7327i.b(a10, -1) || this.f7327i.b(a10, 1)) {
            if (motionEvent.getAction() == 0) {
                this.f7324f.x = motionEvent.getX();
                this.f7324f.y = motionEvent.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                return;
            }
            if (motionEvent.getAction() == 2) {
                this.f7325g.x = motionEvent.getX();
                this.f7325g.y = motionEvent.getY();
                PointF pointF = this.f7325g;
                float f10 = pointF.x;
                PointF pointF2 = this.f7324f;
                float f11 = f10 - pointF2.x;
                float f12 = pointF.y - pointF2.y;
                boolean z10 = a10 == 0;
                float abs = Math.abs(f11) * (z10 ? 0.5f : 1.0f);
                float abs2 = Math.abs(f12) * (z10 ? 1.0f : 0.5f);
                int i10 = this.f7323e;
                if (abs > i10 || abs2 > i10) {
                    if (z10 != (abs > abs2)) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        return;
                    }
                    if (this.f7327i.b(a10, z10 ? (int) f11 : (int) f12)) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
            }
        }
    }

    @Override // com.coui.appcompat.scroll.COUINestedScrollableHost, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        f(motionEvent);
        return super.onInterceptTouchEvent(motionEvent);
    }

    public COUINestedScrollableChild(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public COUINestedScrollableChild(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }
}
