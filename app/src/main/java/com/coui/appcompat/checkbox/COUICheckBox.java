package com.coui.appcompat.checkbox;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Checkable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.n0;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUICheckBox extends AppCompatButton implements Checkable {

    /* renamed from: o, reason: collision with root package name */
    private static final int[] f5604o = {R$attr.coui_state_allSelect};

    /* renamed from: p, reason: collision with root package name */
    private static final int[] f5605p = {R$attr.coui_state_partSelect};

    /* renamed from: h, reason: collision with root package name */
    private int f5606h;

    /* renamed from: i, reason: collision with root package name */
    private int f5607i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f5608j;

    /* renamed from: k, reason: collision with root package name */
    private Drawable f5609k;

    /* renamed from: l, reason: collision with root package name */
    private b f5610l;

    /* renamed from: m, reason: collision with root package name */
    private int f5611m;

    /* renamed from: n, reason: collision with root package name */
    private AccessibilityManager f5612n;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        int f5613e;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        public String toString() {
            return "CompoundButton.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " state=" + this.f5613e + "}";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeValue(Integer.valueOf(this.f5613e));
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
            this.f5613e = 0;
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.f5613e = 0;
            this.f5613e = ((Integer) parcel.readValue(null)).intValue();
        }
    }

    /* loaded from: classes.dex */
    public interface b {
        void a(COUICheckBox cOUICheckBox, int i10);
    }

    public COUICheckBox(Context context) {
        this(context, null);
    }

    private void a() {
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
        if (this.f5612n.isEnabled()) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain();
            obtain.setEventType(2048);
            obtain.setContentChangeTypes(64);
            sendAccessibilityEventUnchecked(obtain);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatButton, android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.f5609k != null) {
            this.f5609k.setState(getDrawableState());
            invalidate();
        }
    }

    @Override // android.widget.TextView
    public int getCompoundPaddingLeft() {
        Drawable drawable;
        int compoundPaddingLeft = super.getCompoundPaddingLeft();
        return (n0.b(this) || (drawable = this.f5609k) == null) ? compoundPaddingLeft : compoundPaddingLeft + drawable.getIntrinsicWidth();
    }

    @Override // android.widget.TextView
    public int getCompoundPaddingRight() {
        Drawable drawable;
        int compoundPaddingRight = super.getCompoundPaddingRight();
        return (!n0.b(this) || (drawable = this.f5609k) == null) ? compoundPaddingRight : compoundPaddingRight + drawable.getIntrinsicWidth();
    }

    @ViewDebug.ExportedProperty
    public int getState() {
        return this.f5606h;
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return getState() == 2;
    }

    @Override // android.widget.TextView, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.f5609k;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected int[] onCreateDrawableState(int i10) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i10 + 1);
        if (getState() == 1) {
            Button.mergeDrawableStates(onCreateDrawableState, f5605p);
        }
        if (getState() == 2) {
            Button.mergeDrawableStates(onCreateDrawableState, f5604o);
        }
        return onCreateDrawableState;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        int height;
        super.onDraw(canvas);
        Drawable drawable = this.f5609k;
        if (drawable != null) {
            int gravity = getGravity() & 112;
            int intrinsicHeight = drawable.getIntrinsicHeight();
            int intrinsicWidth = drawable.getIntrinsicWidth();
            if (gravity != 16) {
                height = gravity != 80 ? 0 : getHeight() - intrinsicHeight;
            } else {
                height = (getHeight() - intrinsicHeight) / 2;
            }
            int i10 = intrinsicHeight + height;
            int width = n0.b(this) ? getWidth() - intrinsicWidth : 0;
            if (n0.b(this)) {
                intrinsicWidth = getWidth();
            }
            drawable.setBounds(width, height, intrinsicWidth, i10);
            drawable.draw(canvas);
            Drawable background = getBackground();
            if (background != null) {
                background.setHotspotBounds(width, height, intrinsicWidth, i10);
            }
        }
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(COUICheckBox.class.getName());
        if (this.f5606h == 2) {
            accessibilityEvent.setChecked(true);
        } else {
            accessibilityEvent.setChecked(false);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatButton, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(COUICheckBox.class.getName());
        accessibilityNodeInfo.setCheckable(true);
        if (this.f5606h == 2) {
            accessibilityNodeInfo.setChecked(true);
        } else {
            accessibilityNodeInfo.setChecked(false);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setState(savedState.f5613e);
        requestLayout();
    }

    @Override // android.widget.TextView, android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f5613e = getState();
        return savedState;
    }

    @Override // android.view.View
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    public void setButtonDrawable(int i10) {
        if (i10 == 0 || i10 != this.f5607i) {
            this.f5607i = i10;
            setButtonDrawable(i10 != 0 ? getResources().getDrawable(this.f5607i) : null);
        }
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z10) {
        if (z10) {
            setState(2);
        } else {
            setState(0);
        }
    }

    public void setOnStateChangeListener(b bVar) {
        this.f5610l = bVar;
    }

    public void setState(int i10) {
        if (this.f5606h != i10) {
            this.f5606h = i10;
            refreshDrawableState();
            if (this.f5608j) {
                return;
            }
            this.f5608j = true;
            b bVar = this.f5610l;
            if (bVar != null) {
                bVar.a(this, this.f5606h);
            }
            this.f5608j = false;
            a();
        }
    }

    @Override // android.widget.Checkable
    public void toggle() {
        setState(this.f5606h >= 2 ? 0 : 2);
    }

    @Override // android.widget.TextView, android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.f5609k;
    }

    public COUICheckBox(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiCheckBoxStyle);
    }

    public COUICheckBox(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.f5611m = attributeSet.getStyleAttribute();
        } else {
            this.f5611m = i10;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUICheckBox, i10, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.COUICheckBox_couiButton);
        if (drawable != null) {
            setButtonDrawable(drawable);
        }
        this.f5612n = (AccessibilityManager) getContext().getSystemService("accessibility");
        setState(obtainStyledAttributes.getInteger(R$styleable.COUICheckBox_couiCheckBoxState, 0));
        obtainStyledAttributes.recycle();
        if (attributeSet != null) {
            int styleAttribute = attributeSet.getStyleAttribute();
            this.f5611m = styleAttribute;
            if (styleAttribute == 0) {
                this.f5611m = i10;
            }
        } else {
            this.f5611m = i10;
        }
        setBackground(null);
    }

    public void setButtonDrawable(Drawable drawable) {
        if (drawable != null) {
            Drawable drawable2 = this.f5609k;
            if (drawable2 != null) {
                drawable2.setCallback(null);
                unscheduleDrawable(this.f5609k);
            }
            drawable.setCallback(this);
            drawable.setState(getDrawableState());
            drawable.setVisible(getVisibility() == 0, false);
            this.f5609k = drawable;
            drawable.setState(null);
            setMinHeight(this.f5609k.getIntrinsicHeight());
        }
        refreshDrawableState();
    }
}
