package n2;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import com.coui.appcompat.scanview.RotateLottieAnimationView;
import kotlin.Metadata;
import ma.Unit;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.k;

/* compiled from: IconRotateHelper.kt */
@Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\u0018\u00002\u00020\u0001:\u0001\u001aB\u0007¢\u0006\u0004\b\u0018\u0010\u0019J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u0010\u0010\t\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0002H\u0002J\u0018\u0010\n\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u001e\u0010\r\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004J\u0018\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u000fH\u0007R\"\u0010\u0013\u001a\u00020\u00128\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0013\u0010\u0015\"\u0004\b\u0016\u0010\u0017¨\u0006\u001b"}, d2 = {"Ln2/g;", "", "Lcom/coui/appcompat/scanview/RotateLottieAnimationView;", "torchIv", "Ln2/b;", "onTorchStateChangeListener", "Lma/f0;", "j", "animationView", "i", "f", "Ln2/j;", "torchTipGroup", "h", "ivAlbum", "Landroid/view/View$OnClickListener;", "onClickAlbumAction", "d", "", "isTorchOn", "Z", "()Z", "k", "(Z)V", "<init>", "()V", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class g {

    /* renamed from: b, reason: collision with root package name */
    public static final a f15660b = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private boolean f15661a;

    /* compiled from: IconRotateHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0007\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0006\u0010\u0007R\u0014\u0010\u0003\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R\u0014\u0010\u0005\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0005\u0010\u0004¨\u0006\b"}, d2 = {"Ln2/g$a;", "", "", "LOTTIE_PROGRESS_MAX", "F", "LOTTIE_PROGRESS_MIN", "<init>", "()V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: IconRotateHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0006\n\u0002\u0018\u0002\n\u0000\u0010\u0001\u001a\u00020\u0000H\n"}, d2 = {"Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    static final class b extends Lambda implements ya.a<Unit> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ TorchTipGroup f15662e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ PressFeedbackHelper f15663f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(TorchTipGroup torchTipGroup, PressFeedbackHelper pressFeedbackHelper) {
            super(0);
            this.f15662e = torchTipGroup;
            this.f15663f = pressFeedbackHelper;
        }

        public final void a() {
            View i10 = this.f15662e.i();
            if (i10 == null) {
                return;
            }
            PressFeedbackHelper.d(this.f15663f, true, i10, null, 4, null);
        }

        @Override // ya.a
        public /* bridge */ /* synthetic */ Unit invoke() {
            a();
            return Unit.f15173a;
        }
    }

    /* compiled from: IconRotateHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0006\n\u0002\u0018\u0002\n\u0000\u0010\u0001\u001a\u00020\u0000H\n"}, d2 = {"Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    static final class c extends Lambda implements ya.a<Unit> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ TorchTipGroup f15664e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ PressFeedbackHelper f15665f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ RotateLottieAnimationView f15666g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ PressFeedbackHelper f15667h;

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ g f15668i;

        /* renamed from: j, reason: collision with root package name */
        final /* synthetic */ n2.b f15669j;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: IconRotateHelper.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0006\n\u0002\u0018\u0002\n\u0000\u0010\u0001\u001a\u00020\u0000H\n"}, d2 = {"Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 5, 1})
        /* loaded from: classes.dex */
        public static final class a extends Lambda implements ya.a<Unit> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ PressFeedbackHelper f15670e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ RotateLottieAnimationView f15671f;

            /* renamed from: g, reason: collision with root package name */
            final /* synthetic */ g f15672g;

            /* renamed from: h, reason: collision with root package name */
            final /* synthetic */ n2.b f15673h;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(PressFeedbackHelper pressFeedbackHelper, RotateLottieAnimationView rotateLottieAnimationView, g gVar, n2.b bVar) {
                super(0);
                this.f15670e = pressFeedbackHelper;
                this.f15671f = rotateLottieAnimationView;
                this.f15672g = gVar;
                this.f15673h = bVar;
            }

            public final void a() {
                PressFeedbackHelper.d(this.f15670e, false, this.f15671f, null, 4, null);
                this.f15672g.j(this.f15671f, this.f15673h);
            }

            @Override // ya.a
            public /* bridge */ /* synthetic */ Unit invoke() {
                a();
                return Unit.f15173a;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(TorchTipGroup torchTipGroup, PressFeedbackHelper pressFeedbackHelper, RotateLottieAnimationView rotateLottieAnimationView, PressFeedbackHelper pressFeedbackHelper2, g gVar, n2.b bVar) {
            super(0);
            this.f15664e = torchTipGroup;
            this.f15665f = pressFeedbackHelper;
            this.f15666g = rotateLottieAnimationView;
            this.f15667h = pressFeedbackHelper2;
            this.f15668i = gVar;
            this.f15669j = bVar;
        }

        public final void a() {
            View i10 = this.f15664e.i();
            if (i10 != null) {
                PressFeedbackHelper.d(this.f15667h, false, i10, null, 4, null);
            }
            PressFeedbackHelper pressFeedbackHelper = this.f15665f;
            RotateLottieAnimationView rotateLottieAnimationView = this.f15666g;
            pressFeedbackHelper.c(true, rotateLottieAnimationView, new a(pressFeedbackHelper, rotateLottieAnimationView, this.f15668i, this.f15669j));
        }

        @Override // ya.a
        public /* bridge */ /* synthetic */ Unit invoke() {
            a();
            return Unit.f15173a;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean e(PressFeedbackHelper pressFeedbackHelper, RotateLottieAnimationView rotateLottieAnimationView, View.OnClickListener onClickListener, View view, MotionEvent motionEvent) {
        k.e(pressFeedbackHelper, "$pressFeedbackHelper");
        k.e(rotateLottieAnimationView, "$ivAlbum");
        k.e(onClickListener, "$onClickAlbumAction");
        int action = motionEvent.getAction();
        if (action == 0) {
            PressFeedbackHelper.d(pressFeedbackHelper, true, rotateLottieAnimationView, null, 4, null);
            return true;
        }
        if (action != 1) {
            return false;
        }
        PressFeedbackHelper.d(pressFeedbackHelper, false, rotateLottieAnimationView, null, 4, null);
        onClickListener.onClick(rotateLottieAnimationView);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean g(PressFeedbackHelper pressFeedbackHelper, RotateLottieAnimationView rotateLottieAnimationView, g gVar, n2.b bVar, View view, MotionEvent motionEvent) {
        k.e(pressFeedbackHelper, "$pressFeedbackHelper");
        k.e(rotateLottieAnimationView, "$torchIv");
        k.e(gVar, "this$0");
        k.e(bVar, "$onTorchStateChangeListener");
        int action = motionEvent.getAction();
        if (action == 0) {
            PressFeedbackHelper.d(pressFeedbackHelper, true, rotateLottieAnimationView, null, 4, null);
            return true;
        }
        if (action != 1) {
            return false;
        }
        PressFeedbackHelper.d(pressFeedbackHelper, false, rotateLottieAnimationView, null, 4, null);
        gVar.j(rotateLottieAnimationView, bVar);
        return false;
    }

    private final void i(RotateLottieAnimationView rotateLottieAnimationView) {
        if (this.f15661a) {
            rotateLottieAnimationView.u();
        } else {
            rotateLottieAnimationView.t();
            rotateLottieAnimationView.setFrame(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void j(RotateLottieAnimationView rotateLottieAnimationView, n2.b bVar) {
        if (bVar.a(!this.f15661a)) {
            this.f15661a = !this.f15661a;
            i(rotateLottieAnimationView);
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public final void d(final RotateLottieAnimationView rotateLottieAnimationView, final View.OnClickListener onClickListener) {
        k.e(rotateLottieAnimationView, "ivAlbum");
        k.e(onClickListener, "onClickAlbumAction");
        final PressFeedbackHelper pressFeedbackHelper = new PressFeedbackHelper();
        rotateLottieAnimationView.setOnTouchListener(new View.OnTouchListener() { // from class: n2.e
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean e10;
                e10 = g.e(PressFeedbackHelper.this, rotateLottieAnimationView, onClickListener, view, motionEvent);
                return e10;
            }
        });
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public final void f(final RotateLottieAnimationView rotateLottieAnimationView, final n2.b bVar) {
        k.e(rotateLottieAnimationView, "torchIv");
        k.e(bVar, "onTorchStateChangeListener");
        final PressFeedbackHelper pressFeedbackHelper = new PressFeedbackHelper();
        rotateLottieAnimationView.setOnTouchListener(new View.OnTouchListener() { // from class: n2.f
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean g6;
                g6 = g.g(PressFeedbackHelper.this, rotateLottieAnimationView, this, bVar, view, motionEvent);
                return g6;
            }
        });
    }

    public final void h(TorchTipGroup torchTipGroup, RotateLottieAnimationView rotateLottieAnimationView, n2.b bVar) {
        k.e(torchTipGroup, "torchTipGroup");
        k.e(rotateLottieAnimationView, "torchIv");
        k.e(bVar, "onTorchStateChangeListener");
        PressFeedbackHelper pressFeedbackHelper = new PressFeedbackHelper();
        torchTipGroup.d(new b(torchTipGroup, pressFeedbackHelper), new c(torchTipGroup, new PressFeedbackHelper(), rotateLottieAnimationView, pressFeedbackHelper, this, bVar));
    }

    public final void k(boolean z10) {
        this.f15661a = z10;
    }
}
