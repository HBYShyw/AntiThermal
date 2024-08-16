package n2;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import b2.COUILog;
import bb.MathJVM;
import com.coui.appcompat.grid.COUIPercentUtils;
import com.coui.appcompat.scanview.COUIFullscreenScanView;
import com.coui.appcompat.scanview.RotateLottieAnimationView;
import com.coui.responsiveui.config.ResponsiveUIConfig;
import com.coui.responsiveui.config.UIConfig;
import com.support.component.R$dimen;
import com.support.component.R$id;
import e2.COUIOrientationUtil;
import java.util.Objects;
import kotlin.Metadata;
import ma.Unit;
import n2.TorchTipGroup;
import ya.l;
import za.DefaultConstructorMarker;
import za.FunctionReferenceImpl;
import za.Lambda;

/* compiled from: ScanViewRotateHelper.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0082\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001WB\u000f\u0012\u0006\u0010T\u001a\u00020S¢\u0006\u0004\bU\u0010VJ\b\u0010\u0004\u001a\u00020\u0003H\u0002J\b\u0010\u0005\u001a\u00020\u0003H\u0002J\u001a\u0010\n\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\bH\u0002J\u0018\u0010\r\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u0006H\u0002J\b\u0010\u000f\u001a\u00020\u000eH\u0002J\u0010\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0006H\u0002J\u0010\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0006H\u0002J\u0010\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0006H\u0002J\b\u0010\u0015\u001a\u00020\u0003H\u0002J\u0014\u0010\u0018\u001a\u00020\u000e*\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0006H\u0002J\u0014\u0010\u001b\u001a\u00020\u0006*\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0006H\u0002J\u0014\u0010\u001c\u001a\u00020\u0006*\u00020\u00162\u0006\u0010\u001a\u001a\u00020\u0006H\u0002J\u0006\u0010\u001d\u001a\u00020\u0003J\u000f\u0010\u001e\u001a\u00020\u0003H\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u000f\u0010 \u001a\u00020\u0003H\u0000¢\u0006\u0004\b \u0010\u001fJ\u000f\u0010!\u001a\u00020\u0003H\u0000¢\u0006\u0004\b!\u0010\u001fJ\u001f\u0010#\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0000¢\u0006\u0004\b#\u0010$J\u0017\u0010%\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0000¢\u0006\u0004\b%\u0010&J\u0017\u0010(\u001a\u00020'2\u0006\u0010\u0007\u001a\u00020\u0006H\u0000¢\u0006\u0004\b(\u0010)J+\u0010,\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060*H\u0000¢\u0006\u0004\b,\u0010-J\u000f\u0010.\u001a\u00020\bH\u0000¢\u0006\u0004\b.\u0010/J\u0012\u00101\u001a\u00020\u00032\b\u00100\u001a\u0004\u0018\u00010\u0002H\u0016R#\u00108\u001a\n 3*\u0004\u0018\u000102028BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b4\u00105\u001a\u0004\b6\u00107R#\u0010;\u001a\n 3*\u0004\u0018\u000102028BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b9\u00105\u001a\u0004\b:\u00107R#\u0010@\u001a\n 3*\u0004\u0018\u00010<0<8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b=\u00105\u001a\u0004\b>\u0010?R#\u0010E\u001a\n 3*\u0004\u0018\u00010A0A8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\bB\u00105\u001a\u0004\bC\u0010DR#\u0010I\u001a\n 3*\u0004\u0018\u00010F0F8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\bG\u00105\u001a\u0004\b0\u0010HR\u001b\u0010N\u001a\u00020J8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\bK\u00105\u001a\u0004\bL\u0010MR\"\u0010\u0007\u001a\u00020\u00068\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0007\u0010\n\u001a\u0004\bO\u0010P\"\u0004\bQ\u0010R¨\u0006X"}, d2 = {"Ln2/h;", "Landroidx/lifecycle/v;", "Lcom/coui/responsiveui/config/UIConfig;", "Lma/f0;", "G", "H", "", "orientation", "", "hasAnimation", "I", "Landroid/view/ViewGroup;", "container", "C", "Landroid/util/Size;", "n", "totalWidth", "v", "p", "width", "w", "K", "Landroid/view/View;", "maxWidth", "q", "Landroid/content/Context;", "resId", "i", "j", "y", "D", "()V", "B", "F", "gridCount", "l", "(II)I", "m", "(I)I", "", "k", "(I)F", "Lkotlin/Function1;", "getGridCount", "x", "(ILya/l;)I", "z", "()Z", "t", "A", "Lcom/coui/appcompat/scanview/RotateLottieAnimationView;", "kotlin.jvm.PlatformType", "albumIcon$delegate", "Lma/h;", "g", "()Lcom/coui/appcompat/scanview/RotateLottieAnimationView;", "albumIcon", "torchIcon$delegate", "u", "torchIcon", "Landroid/widget/TextView;", "description$delegate", "h", "()Landroid/widget/TextView;", "description", "Landroid/widget/FrameLayout;", "finderHolder$delegate", "o", "()Landroid/widget/FrameLayout;", "finderHolder", "Landroidx/constraintlayout/widget/ConstraintLayout;", "rotateContentContainer$delegate", "()Landroidx/constraintlayout/widget/ConstraintLayout;", "rotateContentContainer", "Ln2/a;", "orientationListener$delegate", "s", "()Ln2/a;", "orientationListener", "r", "()I", "E", "(I)V", "Lcom/coui/appcompat/scanview/COUIFullscreenScanView;", "root", "<init>", "(Lcom/coui/appcompat/scanview/COUIFullscreenScanView;)V", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* renamed from: n2.h, reason: use source file name */
/* loaded from: classes.dex */
public final class ScanViewRotateHelper implements Observer<UIConfig> {

    /* renamed from: n, reason: collision with root package name */
    public static final a f15674n = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final COUIFullscreenScanView f15675a;

    /* renamed from: b, reason: collision with root package name */
    private int f15676b;

    /* renamed from: c, reason: collision with root package name */
    private final ma.h f15677c;

    /* renamed from: d, reason: collision with root package name */
    private final ma.h f15678d;

    /* renamed from: e, reason: collision with root package name */
    private final ma.h f15679e;

    /* renamed from: f, reason: collision with root package name */
    private final ma.h f15680f;

    /* renamed from: g, reason: collision with root package name */
    private final ma.h f15681g;

    /* renamed from: h, reason: collision with root package name */
    private final ResponsiveUIConfig f15682h;

    /* renamed from: i, reason: collision with root package name */
    private final TorchTipGroup f15683i;

    /* renamed from: j, reason: collision with root package name */
    private final Context f15684j;

    /* renamed from: k, reason: collision with root package name */
    private final int f15685k;

    /* renamed from: l, reason: collision with root package name */
    private final ma.h f15686l;

    /* renamed from: m, reason: collision with root package name */
    private UIConfig.WindowType f15687m;

    /* compiled from: ScanViewRotateHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\r\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0003\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R\u0014\u0010\u0005\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0005\u0010\u0004R\u0014\u0010\u0006\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0006\u0010\u0004R\u0014\u0010\u0007\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0007\u0010\u0004R\u0014\u0010\b\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\b\u0010\u0004R\u0014\u0010\t\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\t\u0010\u0004R\u0014\u0010\u000b\u001a\u00020\n8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\r\u0010\u0004R\u0014\u0010\u000e\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000e\u0010\u0004R\u0014\u0010\u000f\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000f\u0010\u0004R\u0014\u0010\u0010\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0010\u0010\u0004R\u0014\u0010\u0011\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0011\u0010\u0004R\u0014\u0010\u0012\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0012\u0010\u0004R\u0014\u0010\u0013\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0013\u0010\u0004R\u0014\u0010\u0014\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0014\u0010\u0004¨\u0006\u0017"}, d2 = {"Ln2/h$a;", "", "", "FINDER_VIEW_GRID_COUNT_LARGE", "I", "FINDER_VIEW_GRID_COUNT_MEDIUM", "FINDER_VIEW_GRID_COUNT_SMALL", "PERCENT_UTILS_FLAG_WITHOUT_PADDING", "SCREEN_TYPE_WIDTH_THRESH_MEDIUM", "SCREEN_TYPE_WIDTH_THRESH_SMALL", "", "TAG", "Ljava/lang/String;", "TORCH_TIP_GRID_COUNT_MEDIUM", "TORCH_TIP_GRID_COUNT_SMALL", "TOTAL_GRID_COUNT_SCREEN_TYPE_LARGE", "TOTAL_GRID_COUNT_SCREEN_TYPE_MEDIUM", "TOTAL_GRID_COUNT_SCREEN_TYPE_SMALL", "TWO_ICON_GRID_COUNT_LARGE", "TWO_ICON_GRID_COUNT_MEDIUM", "TWO_ICON_GRID_COUNT_SMALL", "<init>", "()V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* renamed from: n2.h$a */
    /* loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: ScanViewRotateHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n"}, d2 = {"Lcom/coui/appcompat/scanview/RotateLottieAnimationView;", "kotlin.jvm.PlatformType", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* renamed from: n2.h$b */
    /* loaded from: classes.dex */
    static final class b extends Lambda implements ya.a<RotateLottieAnimationView> {
        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final RotateLottieAnimationView invoke() {
            return (RotateLottieAnimationView) ScanViewRotateHelper.this.f15675a.findViewById(R$id.coui_component_scan_view_album);
        }
    }

    /* compiled from: ScanViewRotateHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n"}, d2 = {"Landroid/widget/TextView;", "kotlin.jvm.PlatformType", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* renamed from: n2.h$c */
    /* loaded from: classes.dex */
    static final class c extends Lambda implements ya.a<TextView> {
        c() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final TextView invoke() {
            return (TextView) ScanViewRotateHelper.this.f15675a.findViewById(R$id.coui_component_scan_view_description);
        }
    }

    /* compiled from: ScanViewRotateHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n"}, d2 = {"Landroid/widget/FrameLayout;", "kotlin.jvm.PlatformType", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* renamed from: n2.h$d */
    /* loaded from: classes.dex */
    static final class d extends Lambda implements ya.a<FrameLayout> {
        d() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final FrameLayout invoke() {
            return (FrameLayout) ScanViewRotateHelper.this.f15675a.findViewById(R$id.coui_component_scan_view_finder_holder);
        }
    }

    /* compiled from: ScanViewRotateHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0007\n\u0002\b\u0002*\u0001\u0000\u0010\u0001\u001a\u00020\u0000H\n"}, d2 = {"n2/h$e$a", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* renamed from: n2.h$e */
    /* loaded from: classes.dex */
    static final class e extends Lambda implements ya.a<a> {

        /* compiled from: ScanViewRotateHelper.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0006"}, d2 = {"n2/h$e$a", "Ln2/a;", "", "orientation", "Lma/f0;", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
        /* renamed from: n2.h$e$a */
        /* loaded from: classes.dex */
        public static final class a extends CameraOrientationListener {

            /* renamed from: c, reason: collision with root package name */
            final /* synthetic */ ScanViewRotateHelper f15692c;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(ScanViewRotateHelper scanViewRotateHelper, Context context) {
                super(context);
                this.f15692c = scanViewRotateHelper;
                za.k.d(context, "context");
            }

            @Override // n2.CameraOrientationListener
            public void a(int i10) {
                if (this.f15692c.getF15676b() == i10) {
                    return;
                }
                COUILog.a("ScanViewRotateHelper", "[onDirectionChanged] newOrientation=" + i10 + " oldOrientation=" + this.f15692c.getF15676b() + " width=" + this.f15692c.m(i10));
                ScanViewRotateHelper.J(this.f15692c, i10, false, 2, null);
                this.f15692c.E(i10);
            }
        }

        e() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final a invoke() {
            return new a(ScanViewRotateHelper.this, ScanViewRotateHelper.this.f15684j);
        }
    }

    /* compiled from: ScanViewRotateHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n"}, d2 = {"Landroidx/constraintlayout/widget/ConstraintLayout;", "kotlin.jvm.PlatformType", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* renamed from: n2.h$f */
    /* loaded from: classes.dex */
    static final class f extends Lambda implements ya.a<ConstraintLayout> {
        f() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ConstraintLayout invoke() {
            return (ConstraintLayout) ScanViewRotateHelper.this.f15675a.findViewById(R$id.coui_component_scan_view_rotate_container);
        }
    }

    /* compiled from: ScanViewRotateHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n"}, d2 = {"Lcom/coui/appcompat/scanview/RotateLottieAnimationView;", "kotlin.jvm.PlatformType", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* renamed from: n2.h$g */
    /* loaded from: classes.dex */
    static final class g extends Lambda implements ya.a<RotateLottieAnimationView> {
        g() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final RotateLottieAnimationView invoke() {
            return (RotateLottieAnimationView) ScanViewRotateHelper.this.f15675a.findViewById(R$id.coui_component_scan_view_torch);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ScanViewRotateHelper.kt */
    @Metadata(k = 3, mv = {1, 5, 1}, xi = 48)
    /* renamed from: n2.h$h */
    /* loaded from: classes.dex */
    public /* synthetic */ class h extends FunctionReferenceImpl implements l<Integer, Integer> {
        h(ScanViewRotateHelper scanViewRotateHelper) {
            super(1, scanViewRotateHelper, ScanViewRotateHelper.class, "getTorchTipGridNumber", "getTorchTipGridNumber(I)I", 0);
        }

        public final int G(int i10) {
            return ((ScanViewRotateHelper) this.f20351f).v(i10);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Integer invoke(Integer num) {
            return Integer.valueOf(G(num.intValue()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ScanViewRotateHelper.kt */
    @Metadata(k = 3, mv = {1, 5, 1}, xi = 48)
    /* renamed from: n2.h$i */
    /* loaded from: classes.dex */
    public /* synthetic */ class i extends FunctionReferenceImpl implements l<Integer, Integer> {
        i(ScanViewRotateHelper scanViewRotateHelper) {
            super(1, scanViewRotateHelper, ScanViewRotateHelper.class, "getFinderViewGridNumber", "getFinderViewGridNumber(I)I", 0);
        }

        public final int G(int i10) {
            return ((ScanViewRotateHelper) this.f20351f).p(i10);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Integer invoke(Integer num) {
            return Integer.valueOf(G(num.intValue()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ScanViewRotateHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0006\n\u0002\u0018\u0002\n\u0000\u0010\u0001\u001a\u00020\u0000H\n"}, d2 = {"Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* renamed from: n2.h$j */
    /* loaded from: classes.dex */
    public static final class j extends Lambda implements ya.a<Unit> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ ConstraintLayout f15696f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ int f15697g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        j(ConstraintLayout constraintLayout, int i10) {
            super(0);
            this.f15696f = constraintLayout;
            this.f15697g = i10;
        }

        public final void a() {
            ScanViewRotateHelper scanViewRotateHelper = ScanViewRotateHelper.this;
            ConstraintLayout constraintLayout = this.f15696f;
            za.k.d(constraintLayout, "this");
            scanViewRotateHelper.C(constraintLayout, this.f15697g);
            ScanViewRotateHelper.this.y();
            TorchTipGroup.a aVar = TorchTipGroup.f15700i;
            ConstraintLayout constraintLayout2 = this.f15696f;
            za.k.d(constraintLayout2, "this");
            TorchTipGroup.a.e(aVar, constraintLayout2, null, 2, null);
        }

        @Override // ya.a
        public /* bridge */ /* synthetic */ Unit invoke() {
            a();
            return Unit.f15173a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ScanViewRotateHelper.kt */
    @Metadata(k = 3, mv = {1, 5, 1}, xi = 48)
    /* renamed from: n2.h$k */
    /* loaded from: classes.dex */
    public /* synthetic */ class k extends FunctionReferenceImpl implements l<Integer, Integer> {
        k(ScanViewRotateHelper scanViewRotateHelper) {
            super(1, scanViewRotateHelper, ScanViewRotateHelper.class, "getTorchTipGridNumber", "getTorchTipGridNumber(I)I", 0);
        }

        public final int G(int i10) {
            return ((ScanViewRotateHelper) this.f20351f).v(i10);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Integer invoke(Integer num) {
            return Integer.valueOf(G(num.intValue()));
        }
    }

    public ScanViewRotateHelper(COUIFullscreenScanView cOUIFullscreenScanView) {
        ma.h b10;
        ma.h b11;
        ma.h b12;
        ma.h b13;
        ma.h b14;
        ma.h b15;
        za.k.e(cOUIFullscreenScanView, "root");
        this.f15675a = cOUIFullscreenScanView;
        b10 = ma.j.b(new b());
        this.f15677c = b10;
        b11 = ma.j.b(new g());
        this.f15678d = b11;
        b12 = ma.j.b(new c());
        this.f15679e = b12;
        b13 = ma.j.b(new d());
        this.f15680f = b13;
        b14 = ma.j.b(new f());
        this.f15681g = b14;
        ResponsiveUIConfig responsiveUIConfig = ResponsiveUIConfig.getDefault(cOUIFullscreenScanView.getContext());
        this.f15682h = responsiveUIConfig;
        this.f15683i = cOUIFullscreenScanView.getH();
        Context context = cOUIFullscreenScanView.getContext();
        this.f15684j = context;
        za.k.d(context, "context");
        this.f15685k = i(context, R$dimen.coui_component_scan_view_torch_tip_margin);
        b15 = ma.j.b(new e());
        this.f15686l = b15;
        UIConfig.WindowType screenType = responsiveUIConfig.getScreenType();
        za.k.d(screenType, "responsiveUIConfig.screenType");
        this.f15687m = screenType;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void C(ViewGroup viewGroup, int i10) {
        if (TorchTipGroup.f15700i.c(i10)) {
            viewGroup.setRotation(-i10);
            viewGroup.setTranslationX(0.0f);
            viewGroup.setTranslationY(0.0f);
            ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
            ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) layoutParams;
            ((ViewGroup.MarginLayoutParams) layoutParams2).height = -1;
            ((ViewGroup.MarginLayoutParams) layoutParams2).width = -1;
            viewGroup.setLayoutParams(layoutParams2);
            return;
        }
        viewGroup.setRotation(-i10);
        viewGroup.setTranslationX((this.f15675a.getWidth() - this.f15675a.getHeight()) / 2);
        viewGroup.setTranslationY((this.f15675a.getHeight() - this.f15675a.getWidth()) / 2);
        ViewGroup.LayoutParams layoutParams3 = viewGroup.getLayoutParams();
        Objects.requireNonNull(layoutParams3, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
        ConstraintLayout.LayoutParams layoutParams4 = (ConstraintLayout.LayoutParams) layoutParams3;
        ((ViewGroup.MarginLayoutParams) layoutParams4).height = this.f15675a.getWidth();
        ((ViewGroup.MarginLayoutParams) layoutParams4).width = this.f15675a.getHeight();
        viewGroup.setLayoutParams(layoutParams4);
    }

    private final void G() {
        int x10 = x(this.f15676b, new h(this));
        TextView h10 = h();
        za.k.d(h10, "description");
        Size q10 = q(h10, x10);
        TextView h11 = h();
        za.k.d(h11, "description");
        ViewGroup.LayoutParams layoutParams = h11.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) layoutParams;
        ((ViewGroup.MarginLayoutParams) layoutParams2).width = q10.getWidth() - ((z() ? j(this.f15675a, R$dimen.coui_component_scan_view_icon_margin_horizontal) : 0) * 2);
        h11.setLayoutParams(layoutParams2);
    }

    private final void H() {
        int x10 = x(this.f15676b, new i(this));
        FrameLayout o10 = o();
        za.k.d(o10, "finderHolder");
        ViewGroup.LayoutParams layoutParams = o10.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) layoutParams;
        ((ViewGroup.MarginLayoutParams) layoutParams2).width = x10;
        o10.setLayoutParams(layoutParams2);
    }

    private final void I(int i10, boolean z10) {
        ConstraintLayout t7 = t();
        if (z()) {
            if (((int) t7.getRotation()) != 0) {
                za.k.d(t7, "this");
                C(t7, 0);
                return;
            }
            return;
        }
        if (z10) {
            TorchTipGroup.a aVar = TorchTipGroup.f15700i;
            za.k.d(t7, "this");
            aVar.a(t7, new j(t7, i10));
        } else {
            za.k.d(t7, "this");
            C(t7, i10);
        }
    }

    static /* synthetic */ void J(ScanViewRotateHelper scanViewRotateHelper, int i10, boolean z10, int i11, Object obj) {
        if ((i11 & 2) != 0) {
            z10 = true;
        }
        scanViewRotateHelper.I(i10, z10);
    }

    private final void K() {
        int i10 = this.f15676b;
        TorchTipGroup torchTipGroup = this.f15683i;
        Size q10 = q(torchTipGroup.getF15704c(), x(i10, new k(this)));
        LinearLayout f15704c = torchTipGroup.getF15704c();
        ViewGroup.LayoutParams layoutParams = f15704c.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) layoutParams;
        ((ViewGroup.MarginLayoutParams) layoutParams2).width = q10.getWidth();
        layoutParams2.f1864j = R$id.coui_component_scan_view_description;
        layoutParams2.f1877q = 0;
        layoutParams2.f1879s = 0;
        ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin = this.f15685k;
        f15704c.setLayoutParams(layoutParams2);
    }

    private final RotateLottieAnimationView g() {
        return (RotateLottieAnimationView) this.f15677c.getValue();
    }

    private final TextView h() {
        return (TextView) this.f15679e.getValue();
    }

    private final int i(Context context, int i10) {
        return context.getResources().getDimensionPixelSize(i10);
    }

    private final int j(View view, int i10) {
        return view.getContext().getResources().getDimensionPixelSize(i10);
    }

    private final Size n() {
        int b10;
        int b11;
        DisplayMetrics displayMetrics = this.f15684j.getResources().getDisplayMetrics();
        float f10 = COUIOrientationUtil.a(this.f15684j).x;
        float f11 = displayMetrics.density;
        b10 = MathJVM.b(f10 / f11);
        b11 = MathJVM.b(r2.y / f11);
        return new Size(b10, b11);
    }

    private final FrameLayout o() {
        return (FrameLayout) this.f15680f.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int p(int totalWidth) {
        if (totalWidth < 600) {
            return 5;
        }
        return totalWidth < 840 ? 6 : 8;
    }

    private final Size q(View view, int i10) {
        view.measure(View.MeasureSpec.makeMeasureSpec(i10, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, 0));
        return new Size(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    private final CameraOrientationListener s() {
        return (CameraOrientationListener) this.f15686l.getValue();
    }

    private final ConstraintLayout t() {
        return (ConstraintLayout) this.f15681g.getValue();
    }

    private final RotateLottieAnimationView u() {
        return (RotateLottieAnimationView) this.f15678d.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int v(int totalWidth) {
        return totalWidth >= 600 ? 6 : 5;
    }

    private final int w(int width) {
        if (width < 600) {
            return 4;
        }
        return width < 840 ? 8 : 12;
    }

    @Override // androidx.lifecycle.Observer
    /* renamed from: A, reason: merged with bridge method [inline-methods] */
    public void a(UIConfig uIConfig) {
        COUILog.a("ScanViewRotateHelper", "[onChanged] lastScreenType=" + this.f15687m + " currentScreenType=" + this.f15682h.getScreenType());
        if (this.f15687m == this.f15682h.getScreenType()) {
            return;
        }
        UIConfig.WindowType screenType = this.f15682h.getScreenType();
        za.k.d(screenType, "responsiveUIConfig.screenType");
        this.f15687m = screenType;
        D();
        I(this.f15676b, false);
        y();
    }

    public final void B() {
        this.f15682h.getUiConfig().h(this);
        s().enable();
    }

    public final void D() {
        if (z()) {
            u().B();
            g().B();
            u().setOrientation(this.f15676b);
            g().setOrientation(this.f15676b);
            return;
        }
        u().A();
        u().C();
        g().A();
        g().C();
    }

    public final void E(int i10) {
        this.f15676b = i10;
    }

    public final void F() {
        this.f15682h.getUiConfig().l(this);
        s().disable();
    }

    public final float k(int orientation) {
        int i10;
        Point a10 = COUIOrientationUtil.a(this.f15684j);
        if (z()) {
            return a10.x;
        }
        if (TorchTipGroup.f15700i.c(orientation)) {
            i10 = a10.x;
        } else {
            i10 = a10.y;
        }
        return i10;
    }

    public final int l(int gridCount, int orientation) {
        int b10;
        b10 = MathJVM.b(COUIPercentUtils.a(k(orientation), gridCount, w(m(orientation)), 0, this.f15684j));
        return b10;
    }

    public final int m(int orientation) {
        Size n10 = n();
        if (z()) {
            return n10.getWidth();
        }
        if (TorchTipGroup.f15700i.c(orientation)) {
            return n10.getWidth();
        }
        return n10.getHeight();
    }

    /* renamed from: r, reason: from getter */
    public final int getF15676b() {
        return this.f15676b;
    }

    public final int x(int orientation, l<? super Integer, Integer> getGridCount) {
        za.k.e(getGridCount, "getGridCount");
        return l(getGridCount.invoke(Integer.valueOf(m(orientation))).intValue(), orientation);
    }

    public final void y() {
        K();
        G();
        H();
        this.f15675a.E();
    }

    public final boolean z() {
        return this.f15682h.getScreenType() == UIConfig.WindowType.SMALL;
    }
}
