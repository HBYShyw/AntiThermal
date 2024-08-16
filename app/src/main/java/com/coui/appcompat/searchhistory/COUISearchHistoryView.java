package com.coui.appcompat.searchhistory;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewGroup;
import com.coui.appcompat.searchhistory.COUIFlowLayout;
import com.coui.appcompat.searchhistory.COUISearchHistoryView;
import com.coui.appcompat.textview.COUITextView;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$style;
import com.support.component.R$dimen;
import com.support.component.R$drawable;
import com.support.component.R$id;
import com.support.component.R$styleable;
import java.util.List;
import java.util.Objects;
import kotlin.Metadata;
import m1.COUIMoveEaseInterpolator;
import ma.Unit;
import rd._Sequences;
import v1.COUIContextUtil;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: COUISearchHistoryView.kt */
@Metadata(bv = {}, d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0016\u0018\u0000 \b2\u00020\u0001:\u0001DB1\b\u0007\u0012\u0006\u0010=\u001a\u00020<\u0012\n\b\u0002\u0010?\u001a\u0004\u0018\u00010>\u0012\b\b\u0002\u0010@\u001a\u00020\u0006\u0012\b\b\u0002\u0010A\u001a\u00020\u0006¢\u0006\u0004\bB\u0010CJ\b\u0010\u0003\u001a\u00020\u0002H\u0002J\b\u0010\u0004\u001a\u00020\u0002H\u0002J\u0014\u0010\b\u001a\u00020\u0006*\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0006H\u0002J\u000e\u0010\u000b\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\tJ\u0010\u0010\u000b\u001a\u00020\u00022\b\b\u0001\u0010\u0007\u001a\u00020\u0006J\u0010\u0010\f\u001a\u00020\u00022\b\u0010\n\u001a\u0004\u0018\u00010\tJ\u000e\u0010\u000f\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\rJ\u000e\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\rJ\u000e\u0010\u0014\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0012J\u000e\u0010\u0017\u001a\u00020\u00022\u0006\u0010\u0016\u001a\u00020\u0015J\u0012\u0010\u001a\u001a\u00020\u00022\b\u0010\u0019\u001a\u0004\u0018\u00010\u0018H\u0014J\u0014\u0010\u001e\u001a\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bJ\b\u0010 \u001a\u0004\u0018\u00010\u001fR*\u0010(\u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00068\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\"\u0010#\u001a\u0004\b$\u0010%\"\u0004\b&\u0010'R\u0014\u0010,\u001a\u00020)8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b*\u0010+R\u0017\u00102\u001a\u00020-8\u0006¢\u0006\f\n\u0004\b.\u0010/\u001a\u0004\b0\u00101R\u0014\u00105\u001a\u0002038\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0003\u00104R\u0017\u00109\u001a\u00020\u001f8\u0006¢\u0006\f\n\u0004\b\u0004\u00106\u001a\u0004\b7\u00108R\u0014\u0010;\u001a\u00020\u00068BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b:\u0010%¨\u0006E"}, d2 = {"Lcom/coui/appcompat/searchhistory/COUISearchHistoryView;", "Landroidx/constraintlayout/widget/ConstraintLayout;", "Lma/f0;", "E", "F", "Landroid/view/View;", "", "resId", "G", "", "content", "setTitle", "setDeleteIconDescription", "", "isExpand", "setExpandState", "expandable", "setExpandable", "Landroid/view/View$OnClickListener;", "onClickListener", "setOnDeleteClickListener", "Lcom/coui/appcompat/searchhistory/COUIFlowLayout$d;", "onItemClickListener", "setOnItemClickListener", "Landroid/content/res/Configuration;", "newConfig", "onConfigurationChanged", "", "Lcom/coui/appcompat/searchhistory/COUIFlowLayout$b;", "items", "setItems", "Lcom/coui/appcompat/searchhistory/COUIFlowLayout;", "getCOUIFlowLayout", ThermalBaseConfig.Item.ATTR_VALUE, "B", "I", "getMaxRowFolded", "()I", "setMaxRowFolded", "(I)V", "maxRowFolded", "Lcom/coui/appcompat/textview/COUITextView;", "C", "Lcom/coui/appcompat/textview/COUITextView;", "title", "Landroidx/appcompat/widget/AppCompatImageView;", "D", "Landroidx/appcompat/widget/AppCompatImageView;", "getDeleteIcon", "()Landroidx/appcompat/widget/AppCompatImageView;", "deleteIcon", "Landroid/widget/RelativeLayout;", "Landroid/widget/RelativeLayout;", "titleBar", "Lcom/coui/appcompat/searchhistory/COUIFlowLayout;", "getFlowContainer", "()Lcom/coui/appcompat/searchhistory/COUIFlowLayout;", "flowContainer", "getDeleteIconMarginEnd", "deleteIconMarginEnd", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "defStyleAttr", "defStyleRes", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public class COUISearchHistoryView extends ConstraintLayout {

    /* renamed from: B, reason: from kotlin metadata */
    private int maxRowFolded;

    /* renamed from: C, reason: from kotlin metadata */
    private final COUITextView title;

    /* renamed from: D, reason: from kotlin metadata */
    private final AppCompatImageView deleteIcon;

    /* renamed from: E, reason: from kotlin metadata */
    private final RelativeLayout titleBar;

    /* renamed from: F, reason: from kotlin metadata */
    private final COUIFlowLayout flowContainer;

    /* compiled from: Animator.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\t"}, d2 = {"androidx/core/animation/AnimatorKt$addListener$listener$1", "Landroid/animation/Animator$AnimatorListener;", "Landroid/animation/Animator;", "animator", "Lma/f0;", "onAnimationRepeat", "onAnimationEnd", "onAnimationCancel", "onAnimationStart", "core-ktx_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public static final class b implements Animator.AnimatorListener {
        public b() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            k.e(animator, "animator");
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            k.e(animator, "animator");
            COUISearchHistoryView.this.getFlowContainer().m();
            COUISearchHistoryView.this.removeAllViews();
            COUISearchHistoryView.this.setAlpha(1.0f);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            k.e(animator, "animator");
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            k.e(animator, "animator");
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISearchHistoryView(Context context) {
        this(context, null, 0, 0, 14, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISearchHistoryView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUISearchHistoryView(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0, 8, null);
        k.e(context, "context");
    }

    public /* synthetic */ COUISearchHistoryView(Context context, AttributeSet attributeSet, int i10, int i11, int i12, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i12 & 2) != 0 ? null : attributeSet, (i12 & 4) != 0 ? 0 : i10, (i12 & 8) != 0 ? 0 : i11);
    }

    private final void E() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, (Property<COUISearchHistoryView, Float>) View.ALPHA, 1.0f, 0.0f);
        ofFloat.setInterpolator(new COUIMoveEaseInterpolator());
        ofFloat.setDuration(300L);
        k.d(ofFloat, "");
        ofFloat.addListener(new b());
        ofFloat.start();
    }

    private final void F() {
        boolean j10;
        boolean j11;
        j10 = _Sequences.j(ViewGroup.b(this), this.titleBar);
        if (!j10) {
            addView(this.titleBar);
        }
        j11 = _Sequences.j(ViewGroup.b(this), this.flowContainer);
        if (j11) {
            return;
        }
        addView(this.flowContainer);
    }

    private final int G(View view, int i10) {
        return view.getContext().getResources().getDimensionPixelSize(i10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void H(COUISearchHistoryView cOUISearchHistoryView, View.OnClickListener onClickListener, View view) {
        k.e(cOUISearchHistoryView, "this$0");
        k.e(onClickListener, "$onClickListener");
        cOUISearchHistoryView.E();
        onClickListener.onClick(view);
    }

    private final int getDeleteIconMarginEnd() {
        return G(this, R$dimen.coui_component_search_history_delete_icon_end_margin);
    }

    /* renamed from: getCOUIFlowLayout, reason: from getter */
    public final COUIFlowLayout getFlowContainer() {
        return this.flowContainer;
    }

    public final AppCompatImageView getDeleteIcon() {
        return this.deleteIcon;
    }

    public final COUIFlowLayout getFlowContainer() {
        return this.flowContainer;
    }

    public final int getMaxRowFolded() {
        return this.maxRowFolded;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        AppCompatImageView appCompatImageView = this.deleteIcon;
        ViewGroup.LayoutParams layoutParams = appCompatImageView.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.widget.RelativeLayout.LayoutParams");
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) layoutParams;
        layoutParams2.setMarginEnd(getDeleteIconMarginEnd());
        appCompatImageView.setLayoutParams(layoutParams2);
    }

    public final void setDeleteIconDescription(CharSequence charSequence) {
        this.deleteIcon.setContentDescription(charSequence);
    }

    public final void setExpandState(boolean z10) {
        this.flowContainer.setExpand(z10);
    }

    public final void setExpandable(boolean z10) {
        this.flowContainer.setExpandable(z10);
    }

    public final void setItems(List<? extends COUIFlowLayout.b> list) {
        k.e(list, "items");
        F();
        this.flowContainer.setItems(list);
    }

    public final void setMaxRowFolded(int i10) {
        this.maxRowFolded = i10;
        this.flowContainer.setMaxRowFolded(i10);
    }

    public final void setOnDeleteClickListener(final View.OnClickListener onClickListener) {
        k.e(onClickListener, "onClickListener");
        this.deleteIcon.setOnClickListener(new View.OnClickListener() { // from class: r2.j
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                COUISearchHistoryView.H(COUISearchHistoryView.this, onClickListener, view);
            }
        });
    }

    public final void setOnItemClickListener(COUIFlowLayout.d dVar) {
        k.e(dVar, "onItemClickListener");
        this.flowContainer.setOnItemClickListener(dVar);
    }

    public final void setTitle(CharSequence charSequence) {
        k.e(charSequence, "content");
        this.title.setText(charSequence);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUISearchHistoryView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        k.e(context, "context");
        this.maxRowFolded = Integer.MAX_VALUE;
        COUITextView cOUITextView = new COUITextView(context);
        cOUITextView.setId(R$id.coui_component_search_history_title);
        cOUITextView.setTextAppearance(R$style.couiTextAppearanceButton);
        cOUITextView.setTextColor(COUIContextUtil.a(context, R$attr.couiColorPrimaryNeutral));
        cOUITextView.setTextAlignment(4);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(20);
        Unit unit = Unit.f15173a;
        cOUITextView.setLayoutParams(layoutParams);
        this.title = cOUITextView;
        AppCompatImageView appCompatImageView = new AppCompatImageView(context);
        appCompatImageView.setId(R$id.coui_component_search_history_delete_icon);
        appCompatImageView.setImageResource(R$drawable.coui_component_search_history_delete);
        appCompatImageView.setBackgroundResource(R$drawable.coui_component_icon_bg);
        int i12 = R$dimen.coui_component_search_history_delete_icon_size;
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(G(appCompatImageView, i12), G(appCompatImageView, i12));
        layoutParams2.addRule(21);
        layoutParams2.setMarginEnd(getDeleteIconMarginEnd());
        appCompatImageView.setLayoutParams(layoutParams2);
        this.deleteIcon = appCompatImageView;
        RelativeLayout relativeLayout = new RelativeLayout(context);
        int i13 = R$id.coui_component_search_history_title_bar;
        relativeLayout.setId(i13);
        ConstraintLayout.LayoutParams layoutParams3 = new ConstraintLayout.LayoutParams(-1, -2);
        relativeLayout.setGravity(16);
        layoutParams3.f1860h = 0;
        relativeLayout.setLayoutParams(layoutParams3);
        relativeLayout.setClipChildren(false);
        relativeLayout.setClipToPadding(false);
        int i14 = R$dimen.coui_component_search_history_title_margin_vertical;
        relativeLayout.setPadding(0, G(relativeLayout, i14), 0, G(relativeLayout, i14));
        this.titleBar = relativeLayout;
        COUIFlowLayout cOUIFlowLayout = new COUIFlowLayout(context, null, 0, 0, 14, null);
        cOUIFlowLayout.setMaxRowUnfolded(8);
        ConstraintLayout.LayoutParams layoutParams4 = new ConstraintLayout.LayoutParams(-1, -2);
        layoutParams4.f1862i = i13;
        ((ViewGroup.MarginLayoutParams) layoutParams4).topMargin = G(cOUIFlowLayout, R$dimen.coui_component_search_history_flow_margin_top);
        cOUIFlowLayout.setLayoutParams(layoutParams4);
        this.flowContainer = cOUIFlowLayout;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUISearchHistoryView, i10, i11);
        setExpandable(obtainStyledAttributes.getBoolean(R$styleable.COUISearchHistoryView_expandable, true));
        CharSequence string = obtainStyledAttributes.getString(R$styleable.COUISearchHistoryView_title);
        setTitle(string == null ? new String() : string);
        setDeleteIconDescription(obtainStyledAttributes.getString(R$styleable.COUISearchHistoryView_deleteIconDescription));
        setMaxRowFolded(obtainStyledAttributes.getInteger(R$styleable.COUISearchHistoryView_maxRowFolded, Integer.MAX_VALUE));
        obtainStyledAttributes.recycle();
        setClipChildren(false);
        setClipToPadding(false);
        setPaddingRelative(G(this, R$dimen.coui_component_search_history_padding_start), G(this, R$dimen.coui_component_search_history_padding_top), G(this, R$dimen.coui_component_search_history_padding_end), G(this, R$dimen.coui_component_search_history_padding_bottom));
        cOUIFlowLayout.setItemSpacing(G(cOUIFlowLayout, R$dimen.coui_component_search_history_flow_item_spacing));
        cOUIFlowLayout.setLineSpacing(G(cOUIFlowLayout, R$dimen.coui_component_search_history_flow_line_spacing));
        relativeLayout.addView(cOUITextView);
        relativeLayout.addView(appCompatImageView);
        addView(relativeLayout);
        addView(cOUIFlowLayout);
    }

    public final void setTitle(int i10) {
        this.title.setText(i10);
    }
}
