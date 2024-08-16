package com.coui.appcompat.button;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import com.support.nearx.R$styleable;
import i3.COUIDisplayUtil;
import kotlin.Metadata;
import ma.Unit;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: COUIIconButton.kt */
@Metadata(bv = {}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u001d\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0016\u0018\u0000 F2\u00020\u0001:\u0001GB'\b\u0007\u0012\u0006\u0010@\u001a\u00020?\u0012\n\b\u0002\u0010B\u001a\u0004\u0018\u00010A\u0012\b\b\u0002\u0010C\u001a\u00020\u001c¢\u0006\u0004\bD\u0010EJ(\u0010\n\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0006H\u0002J\b\u0010\u000b\u001a\u00020\u0006H\u0002J\b\u0010\f\u001a\u00020\u0006H\u0002J0\u0010\u0011\u001a\u00020\t2\b\u0010\r\u001a\u0004\u0018\u00010\u00022\b\u0010\u000e\u001a\u0004\u0018\u00010\u00022\b\u0010\u000f\u001a\u0004\u0018\u00010\u00022\b\u0010\u0010\u001a\u0004\u0018\u00010\u0002H\u0016J\u0010\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u0004H\u0014J\u0010\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0002H\u0016J\u0010\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0002H\u0016J\u0010\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0002H\u0016J\u0010\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0002H\u0016J\u0010\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0002H\u0016J\u0010\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0002H\u0016J\u0010\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0002H\u0016J\u0010\u0010\u001b\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0002H\u0016J\b\u0010\u001d\u001a\u00020\u001cH\u0016J\b\u0010\u001e\u001a\u00020\u001cH\u0016J\b\u0010\u001f\u001a\u00020\u001cH\u0016J\b\u0010 \u001a\u00020\u001cH\u0016R \u0010$\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0018\u00010!8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\"\u0010#R*\u0010,\u001a\u00020\u001c2\u0006\u0010%\u001a\u00020\u001c8\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b&\u0010'\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R*\u00100\u001a\u00020\u001c2\u0006\u0010%\u001a\u00020\u001c8\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b-\u0010'\u001a\u0004\b.\u0010)\"\u0004\b/\u0010+R*\u00104\u001a\u00020\u001c2\u0006\u0010%\u001a\u00020\u001c8\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b1\u0010'\u001a\u0004\b2\u0010)\"\u0004\b3\u0010+R*\u00107\u001a\u00020\u001c2\u0006\u0010%\u001a\u00020\u001c8\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b'\u0010'\u001a\u0004\b5\u0010)\"\u0004\b6\u0010+R*\u0010;\u001a\u00020\u001c2\u0006\u0010%\u001a\u00020\u001c8\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b8\u0010'\u001a\u0004\b9\u0010)\"\u0004\b:\u0010+R\u0019\u0010>\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020!8F¢\u0006\u0006\u001a\u0004\b<\u0010=¨\u0006H"}, d2 = {"Lcom/coui/appcompat/button/COUIIconButton;", "Lcom/coui/appcompat/button/COUIButton;", "Landroid/graphics/drawable/Drawable;", "icon", "Landroid/graphics/Canvas;", "canvas", "", "positionX", "positionY", "Lma/f0;", "g", "getTextMaxWidth", "getTextMinLeft", "left", "top", "right", "bottom", "setCompoundDrawables", "onDraw", "drawable", "h", "i", "n", "o", "l", "m", "j", "k", "", "getCompoundPaddingLeft", "getCompoundPaddingRight", "getCompoundPaddingTop", "getCompoundPaddingBottom", "", "E", "[Landroid/graphics/drawable/Drawable;", "_mShowing", ThermalBaseConfig.Item.ATTR_VALUE, "F", "I", "getIconPadding", "()I", "setIconPadding", "(I)V", "iconPadding", "G", "getIconPaddingLeft", "setIconPaddingLeft", "iconPaddingLeft", "H", "getIconPaddingRight", "setIconPaddingRight", "iconPaddingRight", "getIconPaddingTop", "setIconPaddingTop", "iconPaddingTop", "J", "getIconPaddingBottom", "setIconPaddingBottom", "iconPaddingBottom", "getMShowing", "()[Landroid/graphics/drawable/Drawable;", "mShowing", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "defStyleAttr", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "K", "a", "coui-support-nearx_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public class COUIIconButton extends COUIButton {
    private static final int L;

    /* renamed from: E, reason: from kotlin metadata */
    private Drawable[] _mShowing;

    /* renamed from: F, reason: from kotlin metadata */
    private int iconPadding;

    /* renamed from: G, reason: from kotlin metadata */
    private int iconPaddingLeft;

    /* renamed from: H, reason: from kotlin metadata */
    private int iconPaddingRight;

    /* renamed from: I, reason: from kotlin metadata */
    private int iconPaddingTop;

    /* renamed from: J, reason: from kotlin metadata */
    private int iconPaddingBottom;

    static {
        COUIDisplayUtil cOUIDisplayUtil = COUIDisplayUtil.f12488a;
        L = COUIDisplayUtil.a(8);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIIconButton(Context context) {
        this(context, null, 0, 6, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIIconButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        k.e(context, "context");
    }

    public /* synthetic */ COUIIconButton(Context context, AttributeSet attributeSet, int i10, int i11, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i11 & 2) != 0 ? null : attributeSet, (i11 & 4) != 0 ? R.attr.buttonStyle : i10);
    }

    private final void g(Drawable drawable, Canvas canvas, float f10, float f11) {
        canvas.save();
        canvas.translate(f10, f11);
        drawable.draw(canvas);
        canvas.restore();
    }

    private final float getTextMaxWidth() {
        int i10 = 0;
        float lineWidth = getLayout().getLineWidth(0);
        int lineCount = getLayout().getLineCount();
        if (lineCount > 0) {
            while (true) {
                int i11 = i10 + 1;
                lineWidth = Math.max(lineWidth, getLayout().getLineWidth(i10));
                if (i11 >= lineCount) {
                    break;
                }
                i10 = i11;
            }
        }
        return lineWidth;
    }

    private final float getTextMinLeft() {
        int i10 = 0;
        float lineLeft = getLayout().getLineLeft(0);
        int lineCount = getLayout().getLineCount();
        if (lineCount > 0) {
            while (true) {
                int i11 = i10 + 1;
                lineLeft = Math.min(lineLeft, getLayout().getLineLeft(i10));
                if (i11 >= lineCount) {
                    break;
                }
                i10 = i11;
            }
        }
        return lineLeft;
    }

    @Override // android.widget.TextView
    public int getCompoundPaddingBottom() {
        Drawable drawable = getMShowing()[3];
        Integer valueOf = drawable == null ? null : Integer.valueOf(getPaddingBottom() + getCompoundDrawablePadding() + drawable.getBounds().height());
        if (valueOf == null) {
            return getPaddingBottom();
        }
        return valueOf.intValue();
    }

    @Override // android.widget.TextView
    public int getCompoundPaddingLeft() {
        Drawable drawable = getMShowing()[0];
        Integer valueOf = drawable == null ? null : Integer.valueOf(getPaddingLeft() + getCompoundDrawablePadding() + drawable.getBounds().width());
        if (valueOf == null) {
            return getPaddingLeft();
        }
        return valueOf.intValue();
    }

    @Override // android.widget.TextView
    public int getCompoundPaddingRight() {
        Drawable drawable = getMShowing()[2];
        Integer valueOf = drawable == null ? null : Integer.valueOf(getPaddingRight() + getCompoundDrawablePadding() + drawable.getBounds().width());
        if (valueOf == null) {
            return getPaddingRight();
        }
        return valueOf.intValue();
    }

    @Override // android.widget.TextView
    public int getCompoundPaddingTop() {
        Drawable drawable = getMShowing()[1];
        Integer valueOf = drawable == null ? null : Integer.valueOf(getPaddingTop() + getCompoundDrawablePadding() + drawable.getBounds().height());
        if (valueOf == null) {
            return getPaddingTop();
        }
        return valueOf.intValue();
    }

    public final int getIconPadding() {
        return this.iconPadding;
    }

    public final int getIconPaddingBottom() {
        return this.iconPaddingBottom;
    }

    public final int getIconPaddingLeft() {
        return this.iconPaddingLeft;
    }

    public final int getIconPaddingRight() {
        return this.iconPaddingRight;
    }

    public final int getIconPaddingTop() {
        return this.iconPaddingTop;
    }

    public final Drawable[] getMShowing() {
        Drawable[] drawableArr = this._mShowing;
        k.b(drawableArr);
        return drawableArr;
    }

    public float h(Drawable drawable) {
        int width;
        k.e(drawable, "drawable");
        int right = ((getRight() - getLeft()) - getCompoundPaddingRight()) - getCompoundPaddingLeft();
        int scrollX = getScrollX() + getCompoundPaddingLeft();
        int gravity = getGravity() & 7;
        if (gravity == 3) {
            width = drawable.getBounds().width() >> 1;
        } else if (gravity != 5) {
            width = (right - drawable.getBounds().width()) >> 1;
        } else {
            width = right - (((int) (drawable.getBounds().width() + getTextMaxWidth())) >> 1);
        }
        return scrollX + width;
    }

    public float i(Drawable drawable) {
        int height;
        k.e(drawable, "drawable");
        int bottom = ((getBottom() - getTop()) - getCompoundPaddingBottom()) - getCompoundPaddingTop();
        int scrollY = (((getScrollY() + getBottom()) - getTop()) - getPaddingBottom()) - getLayout().getHeight();
        int gravity = getGravity() & 112;
        if (gravity != 48) {
            height = gravity != 80 ? (bottom - getLayout().getHeight()) >> 1 : 0;
        } else {
            height = bottom - getLayout().getHeight();
        }
        return (scrollY - height) + getIconPaddingBottom();
    }

    public float j(Drawable drawable) {
        k.e(drawable, "drawable");
        if (TextUtils.isEmpty(getText())) {
            return (getWidth() / 2) - (drawable.getIntrinsicWidth() / 2);
        }
        return ((getScrollX() + getPaddingLeft()) + getTextMinLeft()) - this.iconPaddingLeft;
    }

    public float k(Drawable drawable) {
        int i10;
        k.e(drawable, "drawable");
        int bottom = ((getBottom() - getTop()) - getCompoundPaddingBottom()) - getCompoundPaddingTop();
        int scrollY = getScrollY() + getCompoundPaddingTop();
        int gravity = getGravity() & 112;
        if (gravity == 48) {
            i10 = 0;
        } else if (gravity != 80) {
            i10 = (bottom - drawable.getBounds().height()) >> 1;
        } else {
            i10 = bottom - drawable.getBounds().height();
        }
        return scrollY + i10;
    }

    public float l(Drawable drawable) {
        float floatValue;
        k.e(drawable, "drawable");
        Drawable drawable2 = getMShowing()[0];
        Float valueOf = drawable2 == null ? null : Float.valueOf(j(drawable2) + drawable2.getBounds().width() + getCompoundDrawablePadding() + getIconPaddingLeft());
        if (valueOf == null) {
            floatValue = getScrollX() + getPaddingLeft() + getTextMinLeft();
        } else {
            floatValue = valueOf.floatValue();
        }
        return floatValue + getTextMaxWidth() + getCompoundDrawablePadding() + this.iconPaddingRight;
    }

    public float m(Drawable drawable) {
        int i10;
        k.e(drawable, "drawable");
        int bottom = ((getBottom() - getTop()) - getCompoundPaddingBottom()) - getCompoundPaddingTop();
        int scrollY = getScrollY() + getCompoundPaddingTop();
        int gravity = getGravity() & 112;
        if (gravity == 48) {
            i10 = 0;
        } else if (gravity != 80) {
            i10 = (bottom - drawable.getBounds().height()) >> 1;
        } else {
            i10 = bottom - drawable.getBounds().height();
        }
        return scrollY + i10;
    }

    public float n(Drawable drawable) {
        int width;
        k.e(drawable, "drawable");
        int right = ((getRight() - getLeft()) - getCompoundPaddingRight()) - getCompoundPaddingLeft();
        int scrollX = getScrollX() + getCompoundPaddingLeft();
        int gravity = getGravity() & 7;
        if (gravity == 3) {
            width = drawable.getBounds().width() >> 1;
        } else if (gravity != 5) {
            width = (right - drawable.getBounds().width()) >> 1;
        } else {
            width = right - (((int) (drawable.getBounds().width() + getTextMaxWidth())) >> 1);
        }
        return scrollX + width;
    }

    public float o(Drawable drawable) {
        int i10;
        k.e(drawable, "drawable");
        int bottom = ((getBottom() - getTop()) - getCompoundPaddingBottom()) - getCompoundPaddingTop();
        int scrollY = getScrollY() + getPaddingTop();
        int gravity = getGravity() & 112;
        if (gravity == 48) {
            i10 = 0;
        } else if (gravity != 80) {
            i10 = (bottom - getLayout().getHeight()) >> 1;
        } else {
            i10 = bottom - getLayout().getHeight();
        }
        return (scrollY + i10) - getIconPaddingTop();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.button.COUIButton, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        k.e(canvas, "canvas");
        super.onDraw(canvas);
        Drawable drawable = getMShowing()[0];
        if (drawable != null) {
            g(drawable, canvas, j(drawable), k(drawable));
        }
        Drawable drawable2 = getMShowing()[2];
        if (drawable2 != null) {
            g(drawable2, canvas, l(drawable2), m(drawable2));
        }
        Drawable drawable3 = getMShowing()[1];
        if (drawable3 != null) {
            g(drawable3, canvas, n(drawable3), o(drawable3));
        }
        Drawable drawable4 = getMShowing()[3];
        if (drawable4 == null) {
            return;
        }
        g(drawable4, canvas, h(drawable4), i(drawable4));
    }

    @Override // android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (this._mShowing == null) {
            this._mShowing = new Drawable[4];
        }
        for (Drawable drawable5 : getMShowing()) {
            if (drawable5 != null) {
                drawable5.setCallback(null);
            }
        }
        Drawable[] mShowing = getMShowing();
        if (drawable == null) {
            drawable = null;
        } else {
            drawable.setState(getDrawableState());
            drawable.setCallback(this);
            Unit unit = Unit.f15173a;
        }
        mShowing[0] = drawable;
        Drawable[] mShowing2 = getMShowing();
        if (drawable2 == null) {
            drawable2 = null;
        } else {
            drawable2.setState(getDrawableState());
            drawable2.setCallback(this);
            Unit unit2 = Unit.f15173a;
        }
        mShowing2[1] = drawable2;
        Drawable[] mShowing3 = getMShowing();
        if (drawable3 == null) {
            drawable3 = null;
        } else {
            drawable3.setState(getDrawableState());
            drawable3.setCallback(this);
            Unit unit3 = Unit.f15173a;
        }
        mShowing3[2] = drawable3;
        Drawable[] mShowing4 = getMShowing();
        if (drawable4 == null) {
            drawable4 = null;
        } else {
            drawable4.setState(getDrawableState());
            drawable4.setCallback(this);
            Unit unit4 = Unit.f15173a;
        }
        mShowing4[3] = drawable4;
        invalidate();
        requestLayout();
    }

    public final void setIconPadding(int i10) {
        setIconPaddingLeft(i10);
        setIconPaddingRight(i10);
        setIconPaddingTop(i10);
        setIconPaddingBottom(i10);
        this.iconPadding = i10;
    }

    public final void setIconPaddingBottom(int i10) {
        this.iconPaddingBottom = i10;
        postInvalidate();
    }

    public final void setIconPaddingLeft(int i10) {
        this.iconPaddingLeft = i10;
        postInvalidate();
    }

    public final void setIconPaddingRight(int i10) {
        this.iconPaddingRight = i10;
        postInvalidate();
    }

    public final void setIconPaddingTop(int i10) {
        this.iconPaddingTop = i10;
        postInvalidate();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIIconButton(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        k.e(context, "context");
        int i11 = L;
        this.iconPadding = i11;
        this.iconPaddingLeft = i11;
        this.iconPaddingRight = i11;
        this.iconPaddingTop = i11;
        this.iconPaddingBottom = i11;
        setSingleLine(false);
        setEllipsize(null);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIIconButton);
        int i12 = R$styleable.COUIIconButton_iconPadding;
        boolean hasValue = obtainStyledAttributes.hasValue(i12);
        if (hasValue) {
            setIconPadding(obtainStyledAttributes.getDimensionPixelSize(i12, i11));
        }
        setIconPaddingLeft(obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIIconButton_iconPaddingLeft, hasValue ? getIconPadding() : i11));
        setIconPaddingRight(obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIIconButton_iconPaddingRight, hasValue ? getIconPadding() : i11));
        setIconPaddingTop(obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIIconButton_iconPaddingTop, hasValue ? getIconPadding() : i11));
        setIconPaddingBottom(obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIIconButton_iconPaddingBottom, hasValue ? getIconPadding() : i11));
        obtainStyledAttributes.recycle();
    }
}
