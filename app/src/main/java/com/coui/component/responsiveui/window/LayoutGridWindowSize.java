package com.coui.component.responsiveui.window;

import android.content.Context;
import com.coui.component.responsiveui.unit.Dp;
import kotlin.Metadata;
import za.Reflection;
import za.k;

/* compiled from: LayoutGridWindowSize.kt */
@Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u000b\u001a\u00020\u0005\u0012\u0006\u0010\f\u001a\u00020\u0005¢\u0006\u0004\b\u0017\u0010\u0018B\u0011\b\u0016\u0012\u0006\u0010\u0019\u001a\u00020\u0000¢\u0006\u0004\b\u0017\u0010\u001aB!\b\u0016\u0012\u0006\u0010\u001c\u001a\u00020\u001b\u0012\u0006\u0010\u000b\u001a\u00020\u001d\u0012\u0006\u0010\f\u001a\u00020\u001d¢\u0006\u0004\b\u0017\u0010\u001eJ\u0013\u0010\u0004\u001a\u00020\u00032\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0006\u001a\u00020\u0005H\u0016J\b\u0010\b\u001a\u00020\u0007H\u0016J\t\u0010\t\u001a\u00020\u0005HÆ\u0003J\t\u0010\n\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\u0005HÆ\u0001R\"\u0010\u000b\u001a\u00020\u00058\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\"\u0010\f\u001a\u00020\u00058\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0014\u0010\u000f\u001a\u0004\b\u0015\u0010\u0011\"\u0004\b\u0016\u0010\u0013¨\u0006\u001f"}, d2 = {"Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;", "", "other", "", "equals", "", "hashCode", "", "toString", "component1", "component2", "width", "height", "copy", "a", "I", "getWidth", "()I", "setWidth", "(I)V", "b", "getHeight", "setHeight", "<init>", "(II)V", "windowSize", "(Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;)V", "Landroid/content/Context;", "context", "Lcom/coui/component/responsiveui/unit/Dp;", "(Landroid/content/Context;Lcom/coui/component/responsiveui/unit/Dp;Lcom/coui/component/responsiveui/unit/Dp;)V", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final /* data */ class LayoutGridWindowSize {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private int width;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private int height;

    public LayoutGridWindowSize(int i10, int i11) {
        this.width = i10;
        this.height = i11;
    }

    public static /* synthetic */ LayoutGridWindowSize copy$default(LayoutGridWindowSize layoutGridWindowSize, int i10, int i11, int i12, Object obj) {
        if ((i12 & 1) != 0) {
            i10 = layoutGridWindowSize.width;
        }
        if ((i12 & 2) != 0) {
            i11 = layoutGridWindowSize.height;
        }
        return layoutGridWindowSize.copy(i10, i11);
    }

    /* renamed from: component1, reason: from getter */
    public final int getWidth() {
        return this.width;
    }

    /* renamed from: component2, reason: from getter */
    public final int getHeight() {
        return this.height;
    }

    public final LayoutGridWindowSize copy(int width, int height) {
        return new LayoutGridWindowSize(width, height);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other != null && k.a(Reflection.b(LayoutGridWindowSize.class), Reflection.b(other.getClass()))) {
            LayoutGridWindowSize layoutGridWindowSize = (LayoutGridWindowSize) other;
            if (this.width == layoutGridWindowSize.width && this.height == layoutGridWindowSize.height) {
                return true;
            }
        }
        return false;
    }

    public final int getHeight() {
        return this.height;
    }

    public final int getWidth() {
        return this.width;
    }

    public int hashCode() {
        return (Integer.hashCode(this.width) * 31) + Integer.hashCode(this.height);
    }

    public final void setHeight(int i10) {
        this.height = i10;
    }

    public final void setWidth(int i10) {
        this.width = i10;
    }

    public String toString() {
        return "(width = " + this.width + ", height = " + this.height + ')';
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public LayoutGridWindowSize(LayoutGridWindowSize layoutGridWindowSize) {
        this(layoutGridWindowSize.width, layoutGridWindowSize.height);
        k.e(layoutGridWindowSize, "windowSize");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public LayoutGridWindowSize(Context context, Dp dp, Dp dp2) {
        this((int) dp.toPixel(context), (int) dp2.toPixel(context));
        k.e(context, "context");
        k.e(dp, "width");
        k.e(dp2, "height");
    }
}
