package com.coui.appcompat.scanview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import com.support.component.R$dimen;
import com.support.component.R$id;
import com.support.component.R$layout;
import com.support.component.R$styleable;
import java.util.Objects;
import kotlin.Metadata;
import ma.Unit;
import ma.h;
import ma.j;
import n2.ScanViewRotateHelper;
import n2.TorchTipGroup;
import n2.g;
import w1.COUIDarkModeUtil;
import ya.a;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.k;

/* compiled from: COUIFullscreenScanView.kt */
@Metadata(bv = {}, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b&\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 r2\u00020\u0001:\u0001sB1\b\u0007\u0012\u0006\u0010k\u001a\u00020j\u0012\n\b\u0002\u0010m\u001a\u0004\u0018\u00010l\u0012\b\b\u0002\u0010n\u001a\u00020\n\u0012\b\b\u0002\u0010o\u001a\u00020\n¢\u0006\u0004\bp\u0010qJ\u0016\u0010\u0006\u001a\u00020\u0005*\u00020\u00022\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u0002J\u001e\u0010\t\u001a\u00020\u0005*\u00020\u00022\b\u0010\u0004\u001a\u0004\u0018\u00010\u00032\u0006\u0010\b\u001a\u00020\u0007H\u0002J\u0014\u0010\f\u001a\u00020\n*\u00020\u00072\u0006\u0010\u000b\u001a\u00020\nH\u0002J\b\u0010\r\u001a\u00020\u0005H\u0014J\b\u0010\u000e\u001a\u00020\u0005H\u0014J\u0010\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\nH\u0014J\u000e\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0011J\u000e\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0007J\u000e\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0007J\u000e\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u0018\u001a\u00020\u0011J\u000e\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0003J\u0010\u0010\u001a\u001a\u00020\u00052\b\b\u0001\u0010\u000b\u001a\u00020\nJ\u000e\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0003J\u0010\u0010\u001b\u001a\u00020\u00052\b\b\u0001\u0010\u000b\u001a\u00020\nJ\u000e\u0010\u001c\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0003J\u0010\u0010\u001c\u001a\u00020\u00052\b\b\u0001\u0010\u000b\u001a\u00020\nJ\u000e\u0010\u001f\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u001dJ\u000f\u0010 \u001a\u00020\u0005H\u0000¢\u0006\u0004\b \u0010!R.\u0010)\u001a\u0004\u0018\u00010\u001d2\b\u0010\"\u001a\u0004\u0018\u00010\u001d8\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b#\u0010$\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R*\u00100\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020\u00118\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b*\u0010+\u001a\u0004\b,\u0010-\"\u0004\b.\u0010/R*\u00103\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020\u00118\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b \u0010+\u001a\u0004\b1\u0010-\"\u0004\b2\u0010/R*\u00104\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020\u00118\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\f\u0010+\u001a\u0004\b4\u0010-\"\u0004\b5\u0010/R*\u00106\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020\u00118\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\u0006\u0010+\u001a\u0004\b6\u0010-\"\u0004\b7\u0010/R\u0016\u00109\u001a\u00020\u00118\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b8\u0010+R\u0014\u0010<\u001a\u00020\u00018\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b:\u0010;R\u0014\u0010?\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b=\u0010>R\u0014\u0010A\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b@\u0010>R\u0014\u0010C\u001a\u00020\u00018\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bB\u0010;R\u0014\u0010G\u001a\u00020D8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bE\u0010FR\u0014\u0010I\u001a\u00020D8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bH\u0010FR\u0014\u0010M\u001a\u00020J8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bK\u0010LR\u0014\u0010P\u001a\u00020\u00078\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bN\u0010OR\u0014\u0010\u0016\u001a\u00020Q8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bR\u0010SR\u001b\u0010Y\u001a\u00020T8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\bU\u0010V\u001a\u0004\bW\u0010XR\u001b\u0010]\u001a\u00020J8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\bZ\u0010V\u001a\u0004\b[\u0010\\R.\u0010_\u001a\u0004\u0018\u00010^2\b\u0010\"\u001a\u0004\u0018\u00010^8\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b_\u0010`\u001a\u0004\ba\u0010b\"\u0004\bc\u0010dR\u001a\u0010f\u001a\u00020e8\u0000X\u0080\u0004¢\u0006\f\n\u0004\bf\u0010g\u001a\u0004\bh\u0010i¨\u0006t"}, d2 = {"Lcom/coui/appcompat/scanview/COUIFullscreenScanView;", "Landroidx/constraintlayout/widget/ConstraintLayout;", "Landroid/widget/TextView;", "", "content", "Lma/f0;", "G", "Landroid/view/View;", "view", "H", "", "resId", "F", "onAttachedToWindow", "onDetachedFromWindow", "visibility", "onWindowVisibilityChanged", "", "isOn", "setTorchState", "previewView", "setPreviewView", "finderView", "setFinderView", "shouldShow", "setShowFinderView", "setTitle", "setDescription", "setTorchTip", "Landroid/view/View$OnClickListener;", "onClickListener", "setOnExitClickListener", "E", "()V", ThermalBaseConfig.Item.ATTR_VALUE, "C", "Landroid/view/View$OnClickListener;", "getOnClickAlbumListener", "()Landroid/view/View$OnClickListener;", "setOnClickAlbumListener", "(Landroid/view/View$OnClickListener;)V", "onClickAlbumListener", "D", "Z", "getShowTorchTip", "()Z", "setShowTorchTip", "(Z)V", "showTorchTip", "getShouldShowFinderView", "setShouldShowFinderView", "shouldShowFinderView", "isAlbumVisible", "setAlbumVisible", "isTorchVisible", "setTorchVisible", "I", "isInitialized", "L", "Landroidx/constraintlayout/widget/ConstraintLayout;", "rotateContentContainer", "M", "Landroid/widget/TextView;", "title", "N", "description", "O", "iconContainer", "Lcom/coui/appcompat/scanview/RotateLottieAnimationView;", "P", "Lcom/coui/appcompat/scanview/RotateLottieAnimationView;", "albumIcon", "Q", "torchIcon", "Landroid/widget/FrameLayout;", "R", "Landroid/widget/FrameLayout;", "previewHolder", "S", "Landroid/view/View;", "cancelIcon", "Lcom/coui/appcompat/scanview/FinderView;", "U", "Lcom/coui/appcompat/scanview/FinderView;", "Ln2/h;", "scanViewRotateHelper$delegate", "Lma/h;", "getScanViewRotateHelper", "()Ln2/h;", "scanViewRotateHelper", "finderHolder$delegate", "getFinderHolder", "()Landroid/widget/FrameLayout;", "finderHolder", "Ln2/b;", "onTorchStateChangeListener", "Ln2/b;", "getOnTorchStateChangeListener", "()Ln2/b;", "setOnTorchStateChangeListener", "(Ln2/b;)V", "Ln2/j;", "torchTipGroup", "Ln2/j;", "getTorchTipGroup$coui_support_component_release", "()Ln2/j;", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "defStyleAttr", "defStyleRes", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "V", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class COUIFullscreenScanView extends ConstraintLayout {
    private n2.b B;

    /* renamed from: C, reason: from kotlin metadata */
    private View.OnClickListener onClickAlbumListener;

    /* renamed from: D, reason: from kotlin metadata */
    private boolean showTorchTip;

    /* renamed from: E, reason: from kotlin metadata */
    private boolean shouldShowFinderView;

    /* renamed from: F, reason: from kotlin metadata */
    private boolean isAlbumVisible;

    /* renamed from: G, reason: from kotlin metadata */
    private boolean isTorchVisible;
    private final TorchTipGroup H;

    /* renamed from: I, reason: from kotlin metadata */
    private boolean isInitialized;
    private final h J;
    private final g K;

    /* renamed from: L, reason: from kotlin metadata */
    private final ConstraintLayout rotateContentContainer;

    /* renamed from: M, reason: from kotlin metadata */
    private final TextView title;

    /* renamed from: N, reason: from kotlin metadata */
    private final TextView description;

    /* renamed from: O, reason: from kotlin metadata */
    private final ConstraintLayout iconContainer;

    /* renamed from: P, reason: from kotlin metadata */
    private final RotateLottieAnimationView albumIcon;

    /* renamed from: Q, reason: from kotlin metadata */
    private final RotateLottieAnimationView torchIcon;

    /* renamed from: R, reason: from kotlin metadata */
    private final FrameLayout previewHolder;

    /* renamed from: S, reason: from kotlin metadata */
    private final View cancelIcon;
    private final h T;

    /* renamed from: U, reason: from kotlin metadata */
    private final FinderView finderView;

    /* compiled from: COUIFullscreenScanView.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n"}, d2 = {"Landroid/widget/FrameLayout;", "kotlin.jvm.PlatformType", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    static final class b extends Lambda implements a<FrameLayout> {
        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final FrameLayout invoke() {
            return (FrameLayout) COUIFullscreenScanView.this.rotateContentContainer.findViewById(R$id.coui_component_scan_view_finder_holder);
        }
    }

    /* compiled from: COUIFullscreenScanView.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0006\n\u0002\u0018\u0002\n\u0000\u0010\u0001\u001a\u00020\u0000H\n"}, d2 = {"Ln2/h;", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    static final class c extends Lambda implements a<ScanViewRotateHelper> {
        c() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ScanViewRotateHelper invoke() {
            return new ScanViewRotateHelper(COUIFullscreenScanView.this);
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIFullscreenScanView(Context context) {
        this(context, null, 0, 0, 14, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIFullscreenScanView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIFullscreenScanView(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0, 8, null);
        k.e(context, "context");
    }

    public /* synthetic */ COUIFullscreenScanView(Context context, AttributeSet attributeSet, int i10, int i11, int i12, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i12 & 2) != 0 ? null : attributeSet, (i12 & 4) != 0 ? 0 : i10, (i12 & 8) != 0 ? 0 : i11);
    }

    private final int F(View view, int i10) {
        return view.getContext().getResources().getDimensionPixelSize(i10);
    }

    private final void G(TextView textView, CharSequence charSequence) {
        H(textView, charSequence, textView);
    }

    private final void H(TextView textView, CharSequence charSequence, View view) {
        if (charSequence == null || charSequence.length() == 0) {
            view.setVisibility(8);
        } else {
            view.setVisibility(0);
            textView.setText(charSequence);
        }
    }

    private final FrameLayout getFinderHolder() {
        Object value = this.T.getValue();
        k.d(value, "<get-finderHolder>(...)");
        return (FrameLayout) value;
    }

    private final ScanViewRotateHelper getScanViewRotateHelper() {
        return (ScanViewRotateHelper) this.J.getValue();
    }

    public final void E() {
        if (this.isInitialized) {
            ConstraintLayout constraintLayout = this.iconContainer;
            ViewGroup.LayoutParams layoutParams = constraintLayout.getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
            ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) layoutParams;
            ScanViewRotateHelper scanViewRotateHelper = getScanViewRotateHelper();
            ((ViewGroup.MarginLayoutParams) layoutParams2).width = ((int) scanViewRotateHelper.k(scanViewRotateHelper.getF15676b())) - (F(this, R$dimen.coui_component_scan_view_icon_margin_horizontal) * 2);
            constraintLayout.setLayoutParams(layoutParams2);
            if (this.isAlbumVisible && this.isTorchVisible) {
                RotateLottieAnimationView rotateLottieAnimationView = this.albumIcon;
                ViewGroup.LayoutParams layoutParams3 = rotateLottieAnimationView.getLayoutParams();
                Objects.requireNonNull(layoutParams3, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
                ConstraintLayout.LayoutParams layoutParams4 = (ConstraintLayout.LayoutParams) layoutParams3;
                layoutParams4.f1879s = -1;
                rotateLottieAnimationView.setLayoutParams(layoutParams4);
                RotateLottieAnimationView rotateLottieAnimationView2 = this.torchIcon;
                ViewGroup.LayoutParams layoutParams5 = rotateLottieAnimationView2.getLayoutParams();
                Objects.requireNonNull(layoutParams5, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
                ConstraintLayout.LayoutParams layoutParams6 = (ConstraintLayout.LayoutParams) layoutParams5;
                layoutParams6.f1877q = -1;
                rotateLottieAnimationView2.setLayoutParams(layoutParams6);
            }
            if (this.isAlbumVisible ^ this.isTorchVisible) {
                RotateLottieAnimationView rotateLottieAnimationView3 = this.albumIcon;
                ViewGroup.LayoutParams layoutParams7 = rotateLottieAnimationView3.getLayoutParams();
                Objects.requireNonNull(layoutParams7, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
                ConstraintLayout.LayoutParams layoutParams8 = (ConstraintLayout.LayoutParams) layoutParams7;
                layoutParams8.f1879s = 0;
                rotateLottieAnimationView3.setLayoutParams(layoutParams8);
                RotateLottieAnimationView rotateLottieAnimationView4 = this.torchIcon;
                ViewGroup.LayoutParams layoutParams9 = rotateLottieAnimationView4.getLayoutParams();
                Objects.requireNonNull(layoutParams9, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
                ConstraintLayout.LayoutParams layoutParams10 = (ConstraintLayout.LayoutParams) layoutParams9;
                layoutParams10.f1877q = 0;
                rotateLottieAnimationView4.setLayoutParams(layoutParams10);
            }
        }
    }

    public final View.OnClickListener getOnClickAlbumListener() {
        return this.onClickAlbumListener;
    }

    /* renamed from: getOnTorchStateChangeListener, reason: from getter */
    public final n2.b getB() {
        return this.B;
    }

    public final boolean getShouldShowFinderView() {
        return this.shouldShowFinderView;
    }

    public final boolean getShowTorchTip() {
        return this.showTorchTip;
    }

    /* renamed from: getTorchTipGroup$coui_support_component_release, reason: from getter */
    public final TorchTipGroup getH() {
        return this.H;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getScanViewRotateHelper().B();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getScanViewRotateHelper().F();
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i10) {
        super.onWindowVisibilityChanged(i10);
        if (i10 == 0) {
            getScanViewRotateHelper().B();
        } else {
            getScanViewRotateHelper().F();
        }
    }

    public final void setAlbumVisible(boolean z10) {
        this.isAlbumVisible = z10;
        this.albumIcon.setVisibility(z10 ? 0 : 8);
        E();
    }

    public final void setDescription(CharSequence charSequence) {
        k.e(charSequence, "content");
        this.description.setText(charSequence);
        getScanViewRotateHelper().y();
    }

    public final void setFinderView(View view) {
        k.e(view, "finderView");
        getFinderHolder().removeAllViews();
        FrameLayout finderHolder = getFinderHolder();
        view.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        Unit unit = Unit.f15173a;
        finderHolder.addView(view);
    }

    public final void setOnClickAlbumListener(View.OnClickListener onClickListener) {
        this.onClickAlbumListener = onClickListener;
        if (onClickListener != null) {
            this.K.d(this.albumIcon, onClickListener);
        }
    }

    public final void setOnExitClickListener(View.OnClickListener onClickListener) {
        k.e(onClickListener, "onClickListener");
        this.cancelIcon.setOnClickListener(onClickListener);
    }

    public final void setOnTorchStateChangeListener(n2.b bVar) {
        this.B = bVar;
        if (bVar != null) {
            this.K.f(this.torchIcon, bVar);
            this.K.h(this.H, this.torchIcon, bVar);
        }
    }

    public final void setPreviewView(View view) {
        k.e(view, "previewView");
        this.previewHolder.removeAllViews();
        FrameLayout frameLayout = this.previewHolder;
        view.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        Unit unit = Unit.f15173a;
        frameLayout.addView(view);
    }

    public final void setShouldShowFinderView(boolean z10) {
        this.shouldShowFinderView = z10;
        setShowFinderView(z10);
    }

    public final void setShowFinderView(boolean z10) {
        if (z10) {
            setFinderView(this.finderView);
        } else {
            getFinderHolder().removeAllViews();
        }
    }

    public final void setShowTorchTip(boolean z10) {
        this.showTorchTip = z10;
        this.H.k(z10);
        if (z10) {
            return;
        }
        this.H.g();
    }

    public final void setTitle(CharSequence charSequence) {
        k.e(charSequence, "content");
        this.title.setText(charSequence);
    }

    public final void setTorchState(boolean z10) {
        this.K.k(z10);
        this.torchIcon.setProgress(z10 ? 1.0f : 0.0f);
    }

    public final void setTorchTip(CharSequence charSequence) {
        k.e(charSequence, "content");
        this.H.m(charSequence);
        getScanViewRotateHelper().y();
    }

    public final void setTorchVisible(boolean z10) {
        this.isTorchVisible = z10;
        this.torchIcon.setVisibility(z10 ? 0 : 8);
        E();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIFullscreenScanView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        h b10;
        h b11;
        int F;
        k.e(context, "context");
        this.isAlbumVisible = true;
        this.isTorchVisible = true;
        this.H = new TorchTipGroup(context);
        b10 = j.b(new c());
        this.J = b10;
        this.K = new g();
        View inflate = ViewGroup.inflate(context, R$layout.coui_component_scan_fullscreen_rotate_container, null);
        inflate.setLayoutParams(new ConstraintLayout.LayoutParams(-1, -1));
        Unit unit = Unit.f15173a;
        ConstraintLayout constraintLayout = (ConstraintLayout) inflate;
        this.rotateContentContainer = constraintLayout;
        View findViewById = constraintLayout.findViewById(R$id.coui_component_scan_view_title);
        k.d(findViewById, "rotateContentContainer.findViewById(R.id.coui_component_scan_view_title)");
        TextView textView = (TextView) findViewById;
        this.title = textView;
        int i12 = R$id.coui_component_scan_view_description;
        View findViewById2 = constraintLayout.findViewById(i12);
        k.d(findViewById2, "rotateContentContainer.findViewById(R.id.coui_component_scan_view_description)");
        TextView textView2 = (TextView) findViewById2;
        this.description = textView2;
        int i13 = R$id.coui_component_scan_view_icon_container;
        View findViewById3 = constraintLayout.findViewById(i13);
        k.d(findViewById3, "rotateContentContainer.findViewById(R.id.coui_component_scan_view_icon_container)");
        this.iconContainer = (ConstraintLayout) findViewById3;
        View findViewById4 = constraintLayout.findViewById(R$id.coui_component_scan_view_album);
        k.d(findViewById4, "rotateContentContainer.findViewById(R.id.coui_component_scan_view_album)");
        this.albumIcon = (RotateLottieAnimationView) findViewById4;
        View findViewById5 = constraintLayout.findViewById(R$id.coui_component_scan_view_torch);
        k.d(findViewById5, "rotateContentContainer\n        .findViewById(R.id.coui_component_scan_view_torch)");
        this.torchIcon = (RotateLottieAnimationView) findViewById5;
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setId(R$id.coui_component_scan_view_preview_holder);
        frameLayout.setLayoutParams(new ConstraintLayout.LayoutParams(-1, -1));
        this.previewHolder = frameLayout;
        View findViewById6 = constraintLayout.findViewById(R$id.coui_component_scan_cancel);
        k.d(findViewById6, "rotateContentContainer.findViewById(R.id.coui_component_scan_cancel)");
        this.cancelIcon = findViewById6;
        b11 = j.b(new b());
        this.T = b11;
        this.finderView = new FinderView(context);
        COUIDarkModeUtil.b(this, false);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIFullscreenScanView, i10, i11);
        setAlbumVisible(obtainStyledAttributes.getBoolean(R$styleable.COUIFullscreenScanView_isAlbumIconVisible, true));
        setTorchVisible(obtainStyledAttributes.getBoolean(R$styleable.COUIFullscreenScanView_isTorchIconVisible, true));
        setShouldShowFinderView(obtainStyledAttributes.getBoolean(R$styleable.COUIFullscreenScanView_showFinderView, false));
        String string = obtainStyledAttributes.getString(R$styleable.COUIFullscreenScanView_title);
        CharSequence string2 = obtainStyledAttributes.getString(R$styleable.COUIFullscreenScanView_description);
        String string3 = obtainStyledAttributes.getString(R$styleable.COUIFullscreenScanView_torchTip);
        string3 = string3 == null ? "" : string3;
        setShowTorchTip(obtainStyledAttributes.getBoolean(R$styleable.COUIFullscreenScanView_showTorchTip, false));
        getH().m(string3);
        textView.setText(string);
        G(textView2, string2);
        obtainStyledAttributes.recycle();
        FrameLayout finderHolder = getFinderHolder();
        ViewGroup.LayoutParams layoutParams = finderHolder.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) layoutParams;
        if (textView2.getVisibility() == 0) {
            layoutParams2.f1864j = i12;
            F = F(finderHolder, R$dimen.coui_component_full_screen_scan_view_finder_margin_bottom);
        } else {
            layoutParams2.f1864j = i13;
            F = F(finderHolder, R$dimen.coui_component_full_screen_scan_view_finder_margin_bottom_without_description);
        }
        ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin = F;
        finderHolder.setLayoutParams(layoutParams2);
        addView(frameLayout);
        getH().c(constraintLayout);
        addView(constraintLayout);
        this.isInitialized = true;
        getScanViewRotateHelper().y();
        getScanViewRotateHelper().D();
    }

    public final void setTitle(int i10) {
        this.title.setText(i10);
    }

    public final void setDescription(int i10) {
        this.description.setText(i10);
        getScanViewRotateHelper().y();
    }

    public final void setTorchTip(int i10) {
        this.H.l(i10);
        getScanViewRotateHelper().y();
    }
}
