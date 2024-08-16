package com.coui.appcompat.emptyview;

import a3.COUITextViewCompatUtil;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Size;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import bb.MathJVM;
import com.coui.appcompat.emptyview.COUIEmptyStateView;
import com.coui.appcompat.statement.COUIMaxHeightScrollView;
import com.oplus.anim.EffectiveAnimationView;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import com.support.component.R$dimen;
import com.support.component.R$id;
import com.support.component.R$layout;
import com.support.component.R$styleable;
import fb._Ranges;
import java.util.Objects;
import kotlin.Metadata;
import ma.h;
import ma.j;
import ya.a;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.k;

/* compiled from: COUIEmptyStateView.kt */
@Metadata(bv = {}, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b>\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 \u0081\u00012\u00020\u0001:\u0002\u0082\u0001B2\b\u0007\u0012\u0006\u0010z\u001a\u00020y\u0012\n\b\u0002\u0010|\u001a\u0004\u0018\u00010{\u0012\b\b\u0002\u0010}\u001a\u00020\u0002\u0012\b\b\u0002\u0010~\u001a\u00020\u0002¢\u0006\u0005\b\u007f\u0010\u0080\u0001J\u001c\u0010\u0005\u001a\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00022\b\b\u0002\u0010\u0004\u001a\u00020\u0002H\u0002J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0002H\u0002J\b\u0010\n\u001a\u00020\tH\u0002J\u0010\u0010\f\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u0002H\u0002J\u0010\u0010\u000e\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\u0002H\u0002J\u0016\u0010\u0012\u001a\u00020\t*\u00020\u000f2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0010H\u0002J\u0014\u0010\u0015\u001a\u00020\t*\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0002H\u0002J\u0014\u0010\u0017\u001a\u00020\t*\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u0010H\u0002J\u0014\u0010\u0019\u001a\u00020\t*\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u0002H\u0002J\u0014\u0010\u001c\u001a\u00020\u0002*\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0002H\u0002J\u0018\u0010\u001f\u001a\u00020\t2\u0006\u0010\u001d\u001a\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u0002H\u0014J0\u0010%\u001a\u00020\t2\u0006\u0010!\u001a\u00020 2\u0006\u0010\u0015\u001a\u00020\u00022\u0006\u0010\"\u001a\u00020\u00022\u0006\u0010#\u001a\u00020\u00022\u0006\u0010$\u001a\u00020\u0002H\u0014J\b\u0010&\u001a\u00020\tH\u0014J\u0018\u0010*\u001a\u00020\t2\u000e\u0010)\u001a\n\u0012\u0004\u0012\u00020(\u0018\u00010'H\u0014J\u0010\u0010,\u001a\u00020\t2\b\b\u0001\u0010+\u001a\u00020\u0002J\u0010\u0010-\u001a\u00020\t2\b\b\u0001\u0010+\u001a\u00020\u0002J\u0010\u0010.\u001a\u00020\t2\b\b\u0001\u0010+\u001a\u00020\u0002J\u000e\u0010/\u001a\u00020\t2\u0006\u0010\u001b\u001a\u00020\u0002J\u000e\u00102\u001a\u00020\t2\u0006\u00101\u001a\u000200R*\u0010:\u001a\u00020\u00022\u0006\u00103\u001a\u00020\u00028\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b4\u00105\u001a\u0004\b6\u00107\"\u0004\b8\u00109R*\u0010\u0011\u001a\u00020\u00102\u0006\u00103\u001a\u00020\u00108\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b;\u0010<\u001a\u0004\b=\u0010>\"\u0004\b.\u0010?R*\u0010A\u001a\u00020\u00102\u0006\u00103\u001a\u00020\u00108\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\b\u0010<\u001a\u0004\b@\u0010>\"\u0004\b,\u0010?R*\u0010D\u001a\u00020\u00102\u0006\u00103\u001a\u00020\u00108\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\u001c\u0010<\u001a\u0004\bB\u0010>\"\u0004\bC\u0010?R*\u0010\u0014\u001a\u00020\u00022\u0006\u00103\u001a\u00020\u00028\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\u0005\u00105\u001a\u0004\bE\u00107\"\u0004\bF\u00109R*\u0010\u0016\u001a\u00020\u00102\u0006\u00103\u001a\u00020\u00108\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\bG\u0010<\u001a\u0004\bH\u0010>\"\u0004\bI\u0010?R*\u0010\u0018\u001a\u00020\u00022\u0006\u00103\u001a\u00020\u00028\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\f\u00105\u001a\u0004\bJ\u00107\"\u0004\bK\u00109R\"\u0010Q\u001a\u00020 8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0015\u0010L\u001a\u0004\bM\u0010N\"\u0004\bO\u0010PR\"\u0010T\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0017\u00105\u001a\u0004\bR\u00107\"\u0004\bS\u00109R\"\u0010W\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0012\u00105\u001a\u0004\bU\u00107\"\u0004\bV\u00109R\u0014\u0010X\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0019\u00105R\u0014\u0010Z\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bY\u00105R\u0014\u0010\\\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b[\u00105R\u0014\u0010]\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b#\u00105R\u0014\u0010_\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b^\u00105R\u001b\u0010d\u001a\u00020\u00018BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b`\u0010a\u001a\u0004\bb\u0010cR\u001b\u0010h\u001a\u00020\u000f8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\be\u0010a\u001a\u0004\bf\u0010gR\u001b\u0010k\u001a\u00020\u000f8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\bi\u0010a\u001a\u0004\bj\u0010gR\u001b\u0010n\u001a\u00020\u000f8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\bl\u0010a\u001a\u0004\bm\u0010gR\u001b\u0010s\u001a\u00020o8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\bp\u0010a\u001a\u0004\bq\u0010rR\u001b\u0010x\u001a\u00020t8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\bu\u0010a\u001a\u0004\bv\u0010w¨\u0006\u0083\u0001"}, d2 = {"Lcom/coui/appcompat/emptyview/COUIEmptyStateView;", "Landroid/widget/LinearLayout;", "", "width", "height", "i", "sizeType", "Landroid/util/Size;", "g", "Lma/f0;", "d", "", "k", "groupHeight", "c", "Landroid/widget/TextView;", "", "actionText", "n", "Lcom/oplus/anim/EffectiveAnimationView;", "rawAnimRes", "l", "animFileName", "m", "imageRes", "o", "Landroid/view/View;", "resId", "h", "widthMeasureSpec", "heightMeasureSpec", "onMeasure", "", "changed", "t", "r", "b", "onLayout", "onAttachedToWindow", "Landroid/util/SparseArray;", "Landroid/os/Parcelable;", "container", "dispatchRestoreInstanceState", "res", "setTitleText", "setSubtitle", "setActionText", "setAnimRes", "Landroid/view/View$OnClickListener;", "onClickListener", "setOnButtonClickListener", ThermalBaseConfig.Item.ATTR_VALUE, "e", "I", "getEmptyViewSizeType", "()I", "setEmptyViewSizeType", "(I)V", "emptyViewSizeType", "f", "Ljava/lang/String;", "getActionText", "()Ljava/lang/String;", "(Ljava/lang/String;)V", "getTitleText", "titleText", "getSubtitleText", "setSubtitleText", "subtitleText", "getRawAnimRes", "setRawAnimRes", "j", "getAnimFileName", "setAnimFileName", "getImageRes", "setImageRes", "Z", "getAutoPlay", "()Z", "setAutoPlay", "(Z)V", "autoPlay", "getAnimHeight", "setAnimHeight", "animHeight", "getAnimWidth", "setAnimWidth", "animWidth", "defaultAnimHeight", "p", "defaultAnimWidth", "q", "widthThresholdMedium", "heightThresholdMedium", "s", "heightThresholdSmall", "emptyStateGroup$delegate", "Lma/h;", "getEmptyStateGroup", "()Landroid/widget/LinearLayout;", "emptyStateGroup", "title$delegate", "getTitle", "()Landroid/widget/TextView;", "title", "subTitle$delegate", "getSubTitle", "subTitle", "actionBt$delegate", "getActionBt", "actionBt", "Lcom/coui/appcompat/emptyview/EmptyStateAnimView;", "animView$delegate", "getAnimView", "()Lcom/coui/appcompat/emptyview/EmptyStateAnimView;", "animView", "Lcom/coui/appcompat/statement/COUIMaxHeightScrollView;", "textContent$delegate", "getTextContent", "()Lcom/coui/appcompat/statement/COUIMaxHeightScrollView;", "textContent", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "defStyleAttr", "defStyleRes", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "z", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class COUIEmptyStateView extends LinearLayout {

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private int emptyViewSizeType;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private String actionText;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    private String titleText;

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    private String subtitleText;

    /* renamed from: i, reason: collision with root package name and from kotlin metadata */
    private int rawAnimRes;

    /* renamed from: j, reason: collision with root package name and from kotlin metadata */
    private String animFileName;

    /* renamed from: k, reason: collision with root package name and from kotlin metadata */
    private int imageRes;

    /* renamed from: l, reason: collision with root package name and from kotlin metadata */
    private boolean autoPlay;

    /* renamed from: m, reason: collision with root package name and from kotlin metadata */
    private int animHeight;

    /* renamed from: n, reason: collision with root package name and from kotlin metadata */
    private int animWidth;

    /* renamed from: o, reason: collision with root package name and from kotlin metadata */
    private final int defaultAnimHeight;

    /* renamed from: p, reason: collision with root package name and from kotlin metadata */
    private final int defaultAnimWidth;

    /* renamed from: q, reason: collision with root package name and from kotlin metadata */
    private final int widthThresholdMedium;

    /* renamed from: r, reason: collision with root package name and from kotlin metadata */
    private final int heightThresholdMedium;

    /* renamed from: s, reason: collision with root package name and from kotlin metadata */
    private final int heightThresholdSmall;

    /* renamed from: t, reason: collision with root package name */
    private final h f5863t;

    /* renamed from: u, reason: collision with root package name */
    private final h f5864u;

    /* renamed from: v, reason: collision with root package name */
    private final h f5865v;

    /* renamed from: w, reason: collision with root package name */
    private final h f5866w;

    /* renamed from: x, reason: collision with root package name */
    private final h f5867x;

    /* renamed from: y, reason: collision with root package name */
    private final h f5868y;

    /* compiled from: COUIEmptyStateView.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n"}, d2 = {"Landroid/widget/TextView;", "kotlin.jvm.PlatformType", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    static final class b extends Lambda implements a<TextView> {
        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final TextView invoke() {
            return (TextView) COUIEmptyStateView.this.findViewById(R$id.empty_view_action);
        }
    }

    /* compiled from: COUIEmptyStateView.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n"}, d2 = {"Lcom/coui/appcompat/emptyview/EmptyStateAnimView;", "kotlin.jvm.PlatformType", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    static final class c extends Lambda implements a<EmptyStateAnimView> {
        c() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final EmptyStateAnimView invoke() {
            return (EmptyStateAnimView) COUIEmptyStateView.this.findViewById(R$id.empty_view_anim);
        }
    }

    /* compiled from: COUIEmptyStateView.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0006\n\u0002\u0018\u0002\n\u0000\u0010\u0001\u001a\u00020\u0000H\n"}, d2 = {"Landroid/widget/LinearLayout;", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    static final class d extends Lambda implements a<LinearLayout> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Context f5871e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(Context context) {
            super(0);
            this.f5871e = context;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final LinearLayout invoke() {
            View inflate = View.inflate(this.f5871e, R$layout.coui_component_empty_state, null);
            Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.widget.LinearLayout");
            return (LinearLayout) inflate;
        }
    }

    /* compiled from: COUIEmptyStateView.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n"}, d2 = {"Landroid/widget/TextView;", "kotlin.jvm.PlatformType", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    static final class e extends Lambda implements a<TextView> {
        e() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final TextView invoke() {
            return (TextView) COUIEmptyStateView.this.findViewById(R$id.empty_view_subtitle);
        }
    }

    /* compiled from: COUIEmptyStateView.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n"}, d2 = {"Lcom/coui/appcompat/statement/COUIMaxHeightScrollView;", "kotlin.jvm.PlatformType", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    static final class f extends Lambda implements a<COUIMaxHeightScrollView> {
        f() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final COUIMaxHeightScrollView invoke() {
            return (COUIMaxHeightScrollView) COUIEmptyStateView.this.findViewById(R$id.empty_view_content);
        }
    }

    /* compiled from: COUIEmptyStateView.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n"}, d2 = {"Landroid/widget/TextView;", "kotlin.jvm.PlatformType", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    static final class g extends Lambda implements a<TextView> {
        g() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final TextView invoke() {
            return (TextView) COUIEmptyStateView.this.findViewById(R$id.empty_view_title);
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIEmptyStateView(Context context) {
        this(context, null, 0, 0, 14, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIEmptyStateView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIEmptyStateView(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0, 8, null);
        k.e(context, "context");
    }

    public /* synthetic */ COUIEmptyStateView(Context context, AttributeSet attributeSet, int i10, int i11, int i12, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i12 & 2) != 0 ? null : attributeSet, (i12 & 4) != 0 ? 0 : i10, (i12 & 8) != 0 ? 0 : i11);
    }

    private final int c(int groupHeight) {
        int b10;
        int c10;
        b10 = MathJVM.b((getMeasuredHeight() - groupHeight) * k(j(this, 0, 0, 3, null)));
        c10 = _Ranges.c(b10, 0);
        return c10;
    }

    private final void d() {
        getTextContent().post(new Runnable() { // from class: a2.b
            @Override // java.lang.Runnable
            public final void run() {
                COUIEmptyStateView.e(COUIEmptyStateView.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void e(COUIEmptyStateView cOUIEmptyStateView) {
        k.e(cOUIEmptyStateView, "this$0");
        if (cOUIEmptyStateView.getTextContent().getHeight() < cOUIEmptyStateView.getTextContent().getMaxHeight()) {
            cOUIEmptyStateView.getTextContent().setOnTouchListener(new View.OnTouchListener() { // from class: a2.a
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    boolean f10;
                    f10 = COUIEmptyStateView.f(view, motionEvent);
                    return f10;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean f(View view, MotionEvent motionEvent) {
        return true;
    }

    private final Size g(int sizeType) {
        float f10 = sizeType != 1 ? sizeType != 2 ? 1.0f : 0.6f : 0.0f;
        return new Size((int) (this.animWidth * f10), (int) (this.animHeight * f10));
    }

    private final TextView getActionBt() {
        Object value = this.f5866w.getValue();
        k.d(value, "<get-actionBt>(...)");
        return (TextView) value;
    }

    private final EmptyStateAnimView getAnimView() {
        Object value = this.f5867x.getValue();
        k.d(value, "<get-animView>(...)");
        return (EmptyStateAnimView) value;
    }

    private final LinearLayout getEmptyStateGroup() {
        return (LinearLayout) this.f5863t.getValue();
    }

    private final TextView getSubTitle() {
        Object value = this.f5865v.getValue();
        k.d(value, "<get-subTitle>(...)");
        return (TextView) value;
    }

    private final COUIMaxHeightScrollView getTextContent() {
        Object value = this.f5868y.getValue();
        k.d(value, "<get-textContent>(...)");
        return (COUIMaxHeightScrollView) value;
    }

    private final TextView getTitle() {
        Object value = this.f5864u.getValue();
        k.d(value, "<get-title>(...)");
        return (TextView) value;
    }

    private final int h(View view, int i10) {
        return view.getContext().getResources().getDimensionPixelSize(i10);
    }

    private final int i(int width, int height) {
        int i10 = this.emptyViewSizeType;
        if (i10 != 0) {
            return i10;
        }
        if (height < this.heightThresholdSmall) {
            return 1;
        }
        return (width < this.widthThresholdMedium || height < this.heightThresholdMedium) ? 2 : 3;
    }

    static /* synthetic */ int j(COUIEmptyStateView cOUIEmptyStateView, int i10, int i11, int i12, Object obj) {
        if ((i12 & 1) != 0) {
            i10 = cOUIEmptyStateView.getMeasuredWidth();
        }
        if ((i12 & 2) != 0) {
            i11 = cOUIEmptyStateView.getMeasuredHeight();
        }
        return cOUIEmptyStateView.i(i10, i11);
    }

    private final float k(int sizeType) {
        return sizeType == 1 ? 0.5f : 0.45f;
    }

    private final void l(EffectiveAnimationView effectiveAnimationView, int i10) {
        if (i10 > 0) {
            effectiveAnimationView.setAnimation(i10);
        }
    }

    private final void m(EffectiveAnimationView effectiveAnimationView, String str) {
        if (str.length() > 0) {
            effectiveAnimationView.setAnimation(str);
        }
    }

    private final void n(TextView textView, String str) {
        textView.setText(str);
        textView.setVisibility((str == null || str.length() == 0) ^ true ? 0 : 8);
    }

    private final void o(EffectiveAnimationView effectiveAnimationView, int i10) {
        if (i10 > 0) {
            effectiveAnimationView.setImageResource(i10);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
    }

    public final String getActionText() {
        return this.actionText;
    }

    public final String getAnimFileName() {
        return this.animFileName;
    }

    public final int getAnimHeight() {
        return this.animHeight;
    }

    public final int getAnimWidth() {
        return this.animWidth;
    }

    public final boolean getAutoPlay() {
        return this.autoPlay;
    }

    public final int getEmptyViewSizeType() {
        return this.emptyViewSizeType;
    }

    public final int getImageRes() {
        return this.imageRes;
    }

    public final int getRawAnimRes() {
        return this.rawAnimRes;
    }

    public final String getSubtitleText() {
        return this.subtitleText;
    }

    public final String getTitleText() {
        return this.titleText;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.autoPlay) {
            if (getAnimView().getVisibility() == 4) {
                return;
            }
            getAnimView().u();
        }
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        int paddingTop = getPaddingTop() + c(getEmptyStateGroup().getMeasuredHeight());
        int measuredHeight = getEmptyStateGroup().getMeasuredHeight() + paddingTop;
        int measuredWidth = (getMeasuredWidth() - getEmptyStateGroup().getMeasuredWidth()) / 2;
        getEmptyStateGroup().layout(measuredWidth, paddingTop, getEmptyStateGroup().getMeasuredWidth() + measuredWidth, measuredHeight);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        int mode = View.MeasureSpec.getMode(i11);
        getAnimView().setAnimSize(g(i(View.MeasureSpec.getSize(i10), View.MeasureSpec.getSize(i11))));
        measureChild(getEmptyStateGroup(), i10, i11);
        if (mode != 1073741824) {
            i11 = View.MeasureSpec.makeMeasureSpec(getEmptyStateGroup().getMeasuredHeight(), mode);
        }
        setMeasuredDimension(i10, i11);
    }

    public final void setActionText(String str) {
        k.e(str, ThermalBaseConfig.Item.ATTR_VALUE);
        n(getActionBt(), str);
        this.actionText = str;
    }

    public final void setAnimFileName(String str) {
        k.e(str, ThermalBaseConfig.Item.ATTR_VALUE);
        if (k.a(str, this.animFileName)) {
            return;
        }
        m(getAnimView(), str);
        this.animFileName = str;
    }

    public final void setAnimHeight(int i10) {
        this.animHeight = i10;
    }

    public final void setAnimRes(int i10) {
        l(getAnimView(), i10);
    }

    public final void setAnimWidth(int i10) {
        this.animWidth = i10;
    }

    public final void setAutoPlay(boolean z10) {
        this.autoPlay = z10;
    }

    public final void setEmptyViewSizeType(int i10) {
        if (i10 != this.emptyViewSizeType) {
            getAnimView().requestLayout();
            this.emptyViewSizeType = i10;
        }
    }

    public final void setImageRes(int i10) {
        if (i10 != this.imageRes) {
            o(getAnimView(), i10);
            this.imageRes = i10;
        }
    }

    public final void setOnButtonClickListener(View.OnClickListener onClickListener) {
        k.e(onClickListener, "onClickListener");
        getActionBt().setOnClickListener(onClickListener);
    }

    public final void setRawAnimRes(int i10) {
        if (i10 != this.rawAnimRes) {
            l(getAnimView(), i10);
            this.rawAnimRes = i10;
        }
    }

    public final void setSubtitle(int i10) {
        TextView subTitle = getSubTitle();
        subTitle.setText(i10);
        CharSequence text = subTitle.getText();
        subTitle.setVisibility((text == null || text.length() == 0) ^ true ? 0 : 8);
    }

    public final void setSubtitleText(String str) {
        k.e(str, ThermalBaseConfig.Item.ATTR_VALUE);
        n(getSubTitle(), str);
        this.subtitleText = str;
    }

    public final void setTitleText(String str) {
        k.e(str, ThermalBaseConfig.Item.ATTR_VALUE);
        n(getTitle(), str);
        this.titleText = str;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIEmptyStateView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        h b10;
        h b11;
        h b12;
        h b13;
        h b14;
        h b15;
        k.e(context, "context");
        this.actionText = "";
        this.titleText = "";
        this.subtitleText = "";
        this.rawAnimRes = -1;
        this.animFileName = "";
        this.imageRes = -1;
        int h10 = h(this, R$dimen.coui_component_empty_anim_view_height_normal);
        this.defaultAnimHeight = h10;
        int h11 = h(this, R$dimen.coui_component_empty_anim_view_width_normal);
        this.defaultAnimWidth = h11;
        this.widthThresholdMedium = h(this, R$dimen.coui_component_width_threshold_medium);
        this.heightThresholdMedium = h(this, R$dimen.coui_component_height_threshold_medium);
        this.heightThresholdSmall = h(this, R$dimen.coui_component_height_threshold_small);
        b10 = j.b(new d(context));
        this.f5863t = b10;
        b11 = j.b(new g());
        this.f5864u = b11;
        b12 = j.b(new e());
        this.f5865v = b12;
        b13 = j.b(new b());
        this.f5866w = b13;
        b14 = j.b(new c());
        this.f5867x = b14;
        b15 = j.b(new f());
        this.f5868y = b15;
        setOverScrollMode(0);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        addView(getEmptyStateGroup(), new LinearLayout.LayoutParams(-1, -2));
        COUITextViewCompatUtil.b(getActionBt());
        d();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIEmptyStateView, i10, i11);
        setAutoPlay(obtainStyledAttributes.getBoolean(R$styleable.COUIEmptyStateView_anim_autoPlay, false));
        String string = obtainStyledAttributes.getString(R$styleable.COUIEmptyStateView_actionText);
        setActionText(string == null ? "" : string);
        String string2 = obtainStyledAttributes.getString(R$styleable.COUIEmptyStateView_titleText);
        setTitleText(string2 == null ? "" : string2);
        String string3 = obtainStyledAttributes.getString(R$styleable.COUIEmptyStateView_subtitleText);
        setSubtitleText(string3 == null ? "" : string3);
        setRawAnimRes(obtainStyledAttributes.getResourceId(R$styleable.COUIEmptyStateView_anim_rawRes, -1));
        String string4 = obtainStyledAttributes.getString(R$styleable.COUIEmptyStateView_anim_fileName);
        setAnimFileName(string4 != null ? string4 : "");
        setImageRes(obtainStyledAttributes.getResourceId(R$styleable.COUIEmptyStateView_android_src, -1));
        setAnimHeight(obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIEmptyStateView_animHeight, h10));
        setAnimWidth(obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIEmptyStateView_animWidth, h11));
        setEmptyViewSizeType(obtainStyledAttributes.getInteger(R$styleable.COUIEmptyStateView_emptyViewSizeType, 0));
        obtainStyledAttributes.recycle();
    }

    public final void setActionText(int i10) {
        getActionBt().setText(i10);
    }

    public final void setTitleText(int i10) {
        TextView title = getTitle();
        title.setText(i10);
        CharSequence text = title.getText();
        title.setVisibility((text == null || text.length() == 0) ^ true ? 0 : 8);
    }
}
