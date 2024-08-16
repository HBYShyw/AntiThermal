package w8;

import android.animation.Animator;
import android.content.Context;
import b6.LocalLog;
import com.oplus.anim.EffectiveAnimationView;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import h8.IPowerInspectPresenter;
import java.util.LinkedList;
import java.util.Queue;
import w1.COUIDarkModeUtil;

/* compiled from: OneKeyAnimation.java */
/* renamed from: w8.a, reason: use source file name */
/* loaded from: classes2.dex */
public class OneKeyAnimation implements Animator.AnimatorListener {

    /* renamed from: d, reason: collision with root package name */
    private EffectiveAnimationView f19395d;

    /* renamed from: e, reason: collision with root package name */
    private EffectiveAnimationView f19396e;

    /* renamed from: f, reason: collision with root package name */
    private EffectiveAnimationView f19397f;

    /* renamed from: g, reason: collision with root package name */
    private EffectiveAnimationView f19398g;

    /* renamed from: h, reason: collision with root package name */
    private EffectiveAnimationView f19399h;

    /* renamed from: k, reason: collision with root package name */
    private Context f19402k;

    /* renamed from: l, reason: collision with root package name */
    private IPowerInspectPresenter f19403l;

    /* renamed from: a, reason: collision with root package name */
    private final String f19392a = "OneKeyAnimation";

    /* renamed from: b, reason: collision with root package name */
    private int f19393b = -1;

    /* renamed from: c, reason: collision with root package name */
    private int f19394c = -1;

    /* renamed from: i, reason: collision with root package name */
    private Queue<Integer> f19400i = new LinkedList();

    /* renamed from: j, reason: collision with root package name */
    private boolean f19401j = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OneKeyAnimation.java */
    /* renamed from: w8.a$a */
    /* loaded from: classes2.dex */
    public class a implements Animator.AnimatorListener {
        a() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            OneKeyAnimation.this.f19395d.v();
            OneKeyAnimation.this.v();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OneKeyAnimation.java */
    /* renamed from: w8.a$b */
    /* loaded from: classes2.dex */
    public class b implements Animator.AnimatorListener {
        b() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            OneKeyAnimation.this.f19396e.v();
            if ((OneKeyAnimation.this.f19400i.peek() != null ? ((Integer) OneKeyAnimation.this.f19400i.peek()).intValue() : -1) == 204) {
                OneKeyAnimation.this.l(animator);
            } else {
                OneKeyAnimation.this.s();
            }
            if (OneKeyAnimation.this.f19403l != null) {
                OneKeyAnimation.this.f19403l.V();
            } else {
                LocalLog.b("OneKeyAnimation", "presenter is null");
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OneKeyAnimation.java */
    /* renamed from: w8.a$c */
    /* loaded from: classes2.dex */
    public class c implements Animator.AnimatorListener {
        c() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            OneKeyAnimation.this.f19397f.v();
            OneKeyAnimation.this.x();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OneKeyAnimation.java */
    /* renamed from: w8.a$d */
    /* loaded from: classes2.dex */
    public class d implements Animator.AnimatorListener {
        d() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            OneKeyAnimation.this.f19398g.v();
            if ((OneKeyAnimation.this.f19400i.peek() != null ? ((Integer) OneKeyAnimation.this.f19400i.peek()).intValue() : -1) == 202) {
                OneKeyAnimation.this.l(animator);
            } else {
                OneKeyAnimation.this.t();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l(Animator animator) {
        Integer poll = this.f19400i.poll();
        if (poll == null) {
            LocalLog.a("OneKeyAnimation", "handleAnimationQueue: no nextAnimate");
            return;
        }
        animator.removeListener(this);
        LocalLog.a("OneKeyAnimation", "onAnimation handleAnimationQueue" + poll);
        switch (poll.intValue()) {
            case EventType.SCENE_MODE_LOCATION /* 201 */:
                t();
                return;
            case EventType.SCENE_MODE_AUDIO_OUT /* 202 */:
                u();
                return;
            case EventType.SCENE_MODE_AUDIO_IN /* 203 */:
                s();
                return;
            case EventType.SCENE_MODE_CAMERA /* 204 */:
                w();
                return;
            default:
                return;
        }
    }

    private void o(EffectiveAnimationView effectiveAnimationView, int i10) {
        boolean a10 = COUIDarkModeUtil.a(this.f19402k);
        switch (i10) {
            case EventType.SCENE_MODE_LOCATION /* 201 */:
                effectiveAnimationView.s(this.f19400i.size() == 0);
                effectiveAnimationView.setAnimation(a10 ? "one_key_power_save_initial_dark.json" : "one_key_power_save_initial_light.json");
                break;
            case EventType.SCENE_MODE_AUDIO_OUT /* 202 */:
                effectiveAnimationView.s(false);
                effectiveAnimationView.setAnimation(a10 ? "one_key_power_save_optimizing_dark.json" : "one_key_power_save_optimizing_light.json");
                break;
            case EventType.SCENE_MODE_AUDIO_IN /* 203 */:
                effectiveAnimationView.s(this.f19400i.size() == 0);
                effectiveAnimationView.setAnimation(a10 ? "one_key_power_save_finished_dark.json" : "one_key_power_save_finished_light.json");
                break;
            case EventType.SCENE_MODE_CAMERA /* 204 */:
                effectiveAnimationView.s(false);
                effectiveAnimationView.setAnimation(a10 ? "one_key_power_save_reset_dark.json" : "one_key_power_save_reset_light.json");
                break;
        }
        effectiveAnimationView.j();
        LocalLog.a("OneKeyAnimation", "setAnimation");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s() {
        if (this.f19401j) {
            this.f19393b = EventType.SCENE_MODE_AUDIO_IN;
            EffectiveAnimationView effectiveAnimationView = this.f19397f;
            this.f19399h = effectiveAnimationView;
            effectiveAnimationView.h(this);
            this.f19397f.s(this.f19400i.size() == 0);
            this.f19397f.setSpeed(this.f19400i.size() == 0 ? 1.0f : 2.0f);
            this.f19397f.setVisibility(0);
            this.f19397f.u();
            this.f19395d.setVisibility(4);
            this.f19396e.setVisibility(4);
            this.f19398g.setVisibility(4);
            this.f19395d.j();
            this.f19396e.j();
            this.f19398g.j();
            LocalLog.a("OneKeyAnimation", "startFinishedAnimation");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void t() {
        if (this.f19401j && !this.f19395d.r()) {
            if (this.f19393b == 203) {
                w();
                return;
            }
            this.f19393b = EventType.SCENE_MODE_LOCATION;
            EffectiveAnimationView effectiveAnimationView = this.f19395d;
            this.f19399h = effectiveAnimationView;
            effectiveAnimationView.h(this);
            this.f19395d.s(this.f19400i.size() == 0);
            this.f19395d.setSpeed(this.f19400i.size() == 0 ? 1.0f : 2.0f);
            this.f19395d.setVisibility(0);
            this.f19395d.u();
            this.f19396e.setVisibility(4);
            this.f19397f.setVisibility(4);
            this.f19398g.setVisibility(4);
            this.f19396e.j();
            this.f19397f.j();
            this.f19398g.j();
            LocalLog.a("OneKeyAnimation", "startInitialAnimation");
            return;
        }
        LocalLog.a("OneKeyAnimation", "startInitialAnimation: init=" + this.f19401j + " " + this.f19395d.r());
    }

    private void u() {
        if (this.f19401j && !this.f19396e.r()) {
            if (this.f19393b == 203) {
                s();
                return;
            }
            EffectiveAnimationView effectiveAnimationView = this.f19395d;
            if (effectiveAnimationView != null && effectiveAnimationView.r()) {
                this.f19395d.h(new a());
            } else {
                v();
            }
            this.f19395d.s(false);
            this.f19395d.setSpeed(2.0f);
            this.f19393b = EventType.SCENE_MODE_AUDIO_OUT;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void v() {
        this.f19396e.h(new b());
        this.f19396e.s(false);
        this.f19396e.setSpeed(1.0f);
        this.f19396e.setVisibility(0);
        this.f19396e.u();
        this.f19395d.setVisibility(4);
        this.f19397f.setVisibility(4);
        this.f19398g.setVisibility(4);
        this.f19395d.j();
        this.f19397f.j();
        this.f19398g.j();
        this.f19399h = this.f19396e;
        LocalLog.a("OneKeyAnimation", "startOptimizingAnimation");
    }

    private void w() {
        if (this.f19401j && !this.f19398g.r()) {
            if (this.f19393b == 201) {
                t();
                return;
            }
            EffectiveAnimationView effectiveAnimationView = this.f19397f;
            if (effectiveAnimationView != null && effectiveAnimationView.r()) {
                this.f19397f.h(new c());
            } else {
                x();
            }
            this.f19397f.s(false);
            this.f19397f.setSpeed(2.0f);
            this.f19393b = EventType.SCENE_MODE_CAMERA;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void x() {
        this.f19398g.h(new d());
        this.f19398g.s(false);
        this.f19398g.setVisibility(0);
        this.f19398g.setSpeed(1.0f);
        this.f19398g.u();
        this.f19395d.setVisibility(4);
        this.f19396e.setVisibility(4);
        this.f19397f.setVisibility(4);
        this.f19395d.j();
        this.f19396e.j();
        this.f19397f.j();
        this.f19393b = EventType.SCENE_MODE_CAMERA;
        this.f19399h = this.f19398g;
        LocalLog.a("OneKeyAnimation", "startResetAnimation");
    }

    public void m(EffectiveAnimationView effectiveAnimationView, EffectiveAnimationView effectiveAnimationView2, EffectiveAnimationView effectiveAnimationView3, EffectiveAnimationView effectiveAnimationView4) {
        if (this.f19402k == null) {
            LocalLog.b("OneKeyAnimation", "context is null");
            return;
        }
        this.f19395d = effectiveAnimationView;
        o(effectiveAnimationView, EventType.SCENE_MODE_LOCATION);
        this.f19396e = effectiveAnimationView2;
        o(effectiveAnimationView2, EventType.SCENE_MODE_AUDIO_OUT);
        this.f19397f = effectiveAnimationView3;
        o(effectiveAnimationView3, EventType.SCENE_MODE_AUDIO_IN);
        this.f19398g = effectiveAnimationView4;
        o(effectiveAnimationView4, EventType.SCENE_MODE_CAMERA);
        this.f19401j = true;
    }

    public void n() {
        this.f19393b = -1;
        this.f19394c = -1;
        this.f19400i.clear();
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animator) {
        LocalLog.a("OneKeyAnimation", "onAnimationCancel: ");
        animator.removeListener(this);
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        l(animator);
        LocalLog.a("OneKeyAnimation", "onAnimationEnd: ");
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animator) {
        l(animator);
        LocalLog.a("OneKeyAnimation", "onAnimationRepeat: ");
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
        LocalLog.a("OneKeyAnimation", "onAnimationStart: ");
    }

    public void p(Context context) {
        this.f19402k = context;
    }

    public void q(IPowerInspectPresenter iPowerInspectPresenter) {
        this.f19403l = iPowerInspectPresenter;
    }

    public void r(int i10) {
        EffectiveAnimationView effectiveAnimationView;
        if (this.f19393b != -1 && (effectiveAnimationView = this.f19399h) != null && effectiveAnimationView.r()) {
            int intValue = this.f19400i.peek() != null ? this.f19400i.peek().intValue() : -1;
            if (intValue != i10 && this.f19394c != i10) {
                this.f19400i.offer(Integer.valueOf(i10));
                this.f19399h.setSpeed(2.0f);
                this.f19399h.s(false);
                this.f19394c = i10;
            } else {
                LocalLog.a("OneKeyAnimation", "startAnimation: ignore " + i10 + " pending:" + intValue + " last:" + this.f19394c);
            }
        } else if (i10 == 202) {
            LocalLog.a("OneKeyAnimation", "start first: finished");
            s();
        } else {
            LocalLog.a("OneKeyAnimation", "start first: initial");
            t();
        }
        LocalLog.a("OneKeyAnimation", "startAnimation: " + i10);
    }
}
