package com.oplus.powermanager.fuelgaue.basic.customized;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import com.coui.appcompat.picker.COUITimeLimitPicker;

/* loaded from: classes.dex */
public class CustomTimePicker extends COUITimeLimitPicker {
    private boolean A;
    private a B;

    /* loaded from: classes.dex */
    public interface a {
        void b(CustomTimePicker customTimePicker);
    }

    public CustomTimePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.A = false;
        } else if (action == 1 || action == 3 || action == 4) {
            this.A = true;
            a aVar = this.B;
            if (aVar != null) {
                aVar.b(this);
            }
        }
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public boolean k() {
        return this.A;
    }

    public void setOnTouchEndListener(a aVar) {
        this.B = aVar;
    }

    public CustomTimePicker(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.A = true;
    }
}
