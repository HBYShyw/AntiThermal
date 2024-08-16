package androidx.window.embedding;

import android.graphics.Rect;
import android.view.WindowMetrics;
import androidx.window.core.ExperimentalWindowApi;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: SplitRule.kt */
@ExperimentalWindowApi
@Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0007\n\u0002\b\u0010\b\u0017\u0018\u0000 \u001d2\u00020\u0001:\u0004\u001e\u001f !B1\b\u0000\u0012\b\b\u0002\u0010\u000e\u001a\u00020\t\u0012\b\b\u0002\u0010\u0011\u001a\u00020\t\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u001a\u001a\u00020\t¢\u0006\u0004\b\u001b\u0010\u001cJ\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u0013\u0010\b\u001a\u00020\u00042\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0096\u0002J\b\u0010\n\u001a\u00020\tH\u0016R\u0017\u0010\u000e\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\u0005\u0010\u000b\u001a\u0004\b\f\u0010\rR\u0017\u0010\u0011\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\u000f\u0010\u000b\u001a\u0004\b\u0010\u0010\rR\u0017\u0010\u0017\u001a\u00020\u00128\u0006¢\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0015\u0010\u0016R\u0017\u0010\u001a\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\u0018\u0010\u000b\u001a\u0004\b\u0019\u0010\r¨\u0006\""}, d2 = {"Landroidx/window/embedding/SplitRule;", "Landroidx/window/embedding/EmbeddingRule;", "Landroid/view/WindowMetrics;", "parentMetrics", "", "a", "", "other", "equals", "", "hashCode", "I", "getMinWidth", "()I", "minWidth", "b", "getMinSmallestWidth", "minSmallestWidth", "", "c", "F", "getSplitRatio", "()F", "splitRatio", "d", "getLayoutDirection", "layoutDirection", "<init>", "(IIFI)V", "e", "Api30Impl", "Companion", "LayoutDir", "SplitFinishBehavior", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public class SplitRule extends EmbeddingRule {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final int minWidth;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final int minSmallestWidth;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private final float splitRatio;

    /* renamed from: d, reason: collision with root package name and from kotlin metadata */
    private final int layoutDirection;

    /* compiled from: SplitRule.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bÁ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0007¨\u0006\b"}, d2 = {"Landroidx/window/embedding/SplitRule$Api30Impl;", "", "Landroid/view/WindowMetrics;", "windowMetrics", "Landroid/graphics/Rect;", "a", "<init>", "()V", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class Api30Impl {

        /* renamed from: a, reason: collision with root package name */
        public static final Api30Impl f4403a = new Api30Impl();

        private Api30Impl() {
        }

        public final Rect a(WindowMetrics windowMetrics) {
            k.e(windowMetrics, "windowMetrics");
            Rect bounds = windowMetrics.getBounds();
            k.d(bounds, "windowMetrics.bounds");
            return bounds;
        }
    }

    /* compiled from: SplitRule.kt */
    @Retention(RetentionPolicy.SOURCE)
    @Metadata(d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0081\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, d2 = {"Landroidx/window/embedding/SplitRule$LayoutDir;", "", "window_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public @interface LayoutDir {
    }

    /* compiled from: SplitRule.kt */
    @Retention(RetentionPolicy.SOURCE)
    @Metadata(d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0081\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, d2 = {"Landroidx/window/embedding/SplitRule$SplitFinishBehavior;", "", "window_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public @interface SplitFinishBehavior {
    }

    public SplitRule() {
        this(0, 0, 0.0f, 0, 15, null);
    }

    public /* synthetic */ SplitRule(int i10, int i11, float f10, int i12, int i13, DefaultConstructorMarker defaultConstructorMarker) {
        this((i13 & 1) != 0 ? 0 : i10, (i13 & 2) != 0 ? 0 : i11, (i13 & 4) != 0 ? 0.5f : f10, (i13 & 8) != 0 ? 3 : i12);
    }

    public final boolean a(WindowMetrics parentMetrics) {
        k.e(parentMetrics, "parentMetrics");
        Rect a10 = Api30Impl.f4403a.a(parentMetrics);
        return (this.minWidth == 0 || a10.width() >= this.minWidth) && (this.minSmallestWidth == 0 || Math.min(a10.width(), a10.height()) >= this.minSmallestWidth);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SplitRule)) {
            return false;
        }
        SplitRule splitRule = (SplitRule) other;
        if (this.minWidth == splitRule.minWidth && this.minSmallestWidth == splitRule.minSmallestWidth) {
            return ((this.splitRatio > splitRule.splitRatio ? 1 : (this.splitRatio == splitRule.splitRatio ? 0 : -1)) == 0) && this.layoutDirection == splitRule.layoutDirection;
        }
        return false;
    }

    public int hashCode() {
        return (((((this.minWidth * 31) + this.minSmallestWidth) * 31) + Float.hashCode(this.splitRatio)) * 31) + this.layoutDirection;
    }

    public SplitRule(int i10, int i11, float f10, int i12) {
        this.minWidth = i10;
        this.minSmallestWidth = i11;
        this.splitRatio = f10;
        this.layoutDirection = i12;
    }
}
