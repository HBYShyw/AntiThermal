package com.coui.component.responsiveui.status;

import android.util.Log;
import android.view.ComponentActivity;
import androidx.lifecycle.RepeatOnLifecycleKt;
import androidx.lifecycle.h;
import androidx.window.sidecar.DisplayFeature;
import androidx.window.sidecar.FoldingFeature;
import androidx.window.sidecar.WindowInfoTracker;
import androidx.window.sidecar.WindowLayoutInfo;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import kotlin.Metadata;
import ma.Unit;
import ma.q;
import qa.d;
import sa.f;
import sa.k;
import td.h0;
import wd.FlowCollector;
import wd.b;
import ya.p;

/* compiled from: WindowFeatureUtil.kt */
@Metadata(bv = {}, d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Ltd/h0;", "Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 5, 1})
@f(c = "com.coui.component.responsiveui.status.WindowFeatureUtil$trackWindowFeature$1", f = "WindowFeatureUtil.kt", l = {44}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class WindowFeatureUtil$trackWindowFeature$1 extends k implements p<h0, d<? super Unit>, Object> {

    /* renamed from: i, reason: collision with root package name */
    int f8204i;

    /* renamed from: j, reason: collision with root package name */
    final /* synthetic */ ComponentActivity f8205j;

    /* renamed from: k, reason: collision with root package name */
    final /* synthetic */ Consumer<WindowFeature> f8206k;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: WindowFeatureUtil.kt */
    @Metadata(bv = {}, d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Ltd/h0;", "Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    @f(c = "com.coui.component.responsiveui.status.WindowFeatureUtil$trackWindowFeature$1$1", f = "WindowFeatureUtil.kt", l = {52}, m = "invokeSuspend")
    /* renamed from: com.coui.component.responsiveui.status.WindowFeatureUtil$trackWindowFeature$1$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends k implements p<h0, d<? super Unit>, Object> {

        /* renamed from: i, reason: collision with root package name */
        int f8207i;

        /* renamed from: j, reason: collision with root package name */
        final /* synthetic */ ComponentActivity f8208j;

        /* renamed from: k, reason: collision with root package name */
        final /* synthetic */ Consumer<WindowFeature> f8209k;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(ComponentActivity componentActivity, Consumer<WindowFeature> consumer, d<? super AnonymousClass1> dVar) {
            super(2, dVar);
            this.f8208j = componentActivity;
            this.f8209k = consumer;
        }

        @Override // sa.a
        public final d<Unit> create(Object obj, d<?> dVar) {
            return new AnonymousClass1(this.f8208j, this.f8209k, dVar);
        }

        @Override // ya.p
        public final Object invoke(h0 h0Var, d<? super Unit> dVar) {
            return ((AnonymousClass1) create(h0Var, dVar)).invokeSuspend(Unit.f15173a);
        }

        @Override // sa.a
        public final Object invokeSuspend(Object obj) {
            Object c10;
            c10 = ra.d.c();
            int i10 = this.f8207i;
            if (i10 == 0) {
                q.b(obj);
                final b<WindowLayoutInfo> a10 = WindowInfoTracker.INSTANCE.d(this.f8208j).a(this.f8208j);
                b<WindowFeature> bVar = new b<WindowFeature>() { // from class: com.coui.component.responsiveui.status.WindowFeatureUtil$trackWindowFeature$1$1$invokeSuspend$$inlined$map$1

                    /* compiled from: Emitters.kt */
                    @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0006\u001a\u00020\u0003\"\u0004\b\u0000\u0010\u0000\"\u0004\b\u0001\u0010\u00012\u0006\u0010\u0002\u001a\u00028\u0000H\u008a@¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"T", "R", ThermalBaseConfig.Item.ATTR_VALUE, "Lma/f0;", "emit", "(Ljava/lang/Object;Lqa/d;)Ljava/lang/Object;", "<anonymous>"}, k = 3, mv = {1, 5, 1})
                    /* renamed from: com.coui.component.responsiveui.status.WindowFeatureUtil$trackWindowFeature$1$1$invokeSuspend$$inlined$map$1$2, reason: invalid class name */
                    /* loaded from: classes.dex */
                    public static final class AnonymousClass2<T> implements FlowCollector {

                        /* renamed from: e, reason: collision with root package name */
                        final /* synthetic */ FlowCollector f8200e;

                        @Metadata(k = 3, mv = {1, 5, 1}, xi = 176)
                        @f(c = "com.coui.component.responsiveui.status.WindowFeatureUtil$trackWindowFeature$1$1$invokeSuspend$$inlined$map$1$2", f = "WindowFeatureUtil.kt", l = {224}, m = "emit")
                        /* renamed from: com.coui.component.responsiveui.status.WindowFeatureUtil$trackWindowFeature$1$1$invokeSuspend$$inlined$map$1$2$1, reason: invalid class name */
                        /* loaded from: classes.dex */
                        public static final class AnonymousClass1 extends sa.d {

                            /* renamed from: h, reason: collision with root package name */
                            /* synthetic */ Object f8201h;

                            /* renamed from: i, reason: collision with root package name */
                            int f8202i;

                            public AnonymousClass1(d dVar) {
                                super(dVar);
                            }

                            @Override // sa.a
                            public final Object invokeSuspend(Object obj) {
                                this.f8201h = obj;
                                this.f8202i |= Integer.MIN_VALUE;
                                return AnonymousClass2.this.emit(null, this);
                            }
                        }

                        public AnonymousClass2(FlowCollector flowCollector) {
                            this.f8200e = flowCollector;
                        }

                        /* JADX WARN: Removed duplicated region for block: B:15:0x0031  */
                        /* JADX WARN: Removed duplicated region for block: B:8:0x0023  */
                        @Override // wd.FlowCollector
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                        */
                        public final Object emit(Object obj, d dVar) {
                            AnonymousClass1 anonymousClass1;
                            Object c10;
                            int i10;
                            if (dVar instanceof AnonymousClass1) {
                                anonymousClass1 = (AnonymousClass1) dVar;
                                int i11 = anonymousClass1.f8202i;
                                if ((i11 & Integer.MIN_VALUE) != 0) {
                                    anonymousClass1.f8202i = i11 - Integer.MIN_VALUE;
                                    Object obj2 = anonymousClass1.f8201h;
                                    c10 = ra.d.c();
                                    i10 = anonymousClass1.f8202i;
                                    if (i10 == 0) {
                                        if (i10 != 1) {
                                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                        }
                                        q.b(obj2);
                                    } else {
                                        q.b(obj2);
                                        FlowCollector flowCollector = this.f8200e;
                                        List<DisplayFeature> a10 = ((WindowLayoutInfo) obj).a();
                                        ArrayList arrayList = new ArrayList();
                                        for (T t7 : a10) {
                                            if (t7 instanceof FoldingFeature) {
                                                arrayList.add(t7);
                                            }
                                        }
                                        WindowFeature windowFeature = new WindowFeature(a10, arrayList);
                                        anonymousClass1.f8202i = 1;
                                        if (flowCollector.emit(windowFeature, anonymousClass1) == c10) {
                                            return c10;
                                        }
                                    }
                                    return Unit.f15173a;
                                }
                            }
                            anonymousClass1 = new AnonymousClass1(dVar);
                            Object obj22 = anonymousClass1.f8201h;
                            c10 = ra.d.c();
                            i10 = anonymousClass1.f8202i;
                            if (i10 == 0) {
                            }
                            return Unit.f15173a;
                        }
                    }

                    @Override // wd.b
                    public Object collect(FlowCollector<? super WindowFeature> flowCollector, d dVar) {
                        Object c11;
                        Object collect = b.this.collect(new AnonymousClass2(flowCollector), dVar);
                        c11 = ra.d.c();
                        return collect == c11 ? collect : Unit.f15173a;
                    }
                };
                final Consumer<WindowFeature> consumer = this.f8209k;
                FlowCollector<? super WindowFeature> flowCollector = new FlowCollector() { // from class: com.coui.component.responsiveui.status.WindowFeatureUtil.trackWindowFeature.1.1.2
                    @Override // wd.FlowCollector
                    public /* bridge */ /* synthetic */ Object emit(Object obj2, d dVar) {
                        return emit((WindowFeature) obj2, (d<? super Unit>) dVar);
                    }

                    public final Object emit(WindowFeature windowFeature, d<? super Unit> dVar) {
                        consumer.accept(windowFeature);
                        Log.d("WindowFeatureUtil", za.k.l("[trackWindowFeature] windowFeature = ", windowFeature));
                        return Unit.f15173a;
                    }
                };
                this.f8207i = 1;
                if (bVar.collect(flowCollector, this) == c10) {
                    return c10;
                }
            } else {
                if (i10 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                q.b(obj);
            }
            return Unit.f15173a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public WindowFeatureUtil$trackWindowFeature$1(ComponentActivity componentActivity, Consumer<WindowFeature> consumer, d<? super WindowFeatureUtil$trackWindowFeature$1> dVar) {
        super(2, dVar);
        this.f8205j = componentActivity;
        this.f8206k = consumer;
    }

    @Override // sa.a
    public final d<Unit> create(Object obj, d<?> dVar) {
        return new WindowFeatureUtil$trackWindowFeature$1(this.f8205j, this.f8206k, dVar);
    }

    @Override // ya.p
    public final Object invoke(h0 h0Var, d<? super Unit> dVar) {
        return ((WindowFeatureUtil$trackWindowFeature$1) create(h0Var, dVar)).invokeSuspend(Unit.f15173a);
    }

    @Override // sa.a
    public final Object invokeSuspend(Object obj) {
        Object c10;
        c10 = ra.d.c();
        int i10 = this.f8204i;
        if (i10 == 0) {
            q.b(obj);
            h lifecycle = this.f8205j.getLifecycle();
            za.k.d(lifecycle, "activity.lifecycle");
            h.c cVar = h.c.STARTED;
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this.f8205j, this.f8206k, null);
            this.f8204i = 1;
            if (RepeatOnLifecycleKt.a(lifecycle, cVar, anonymousClass1, this) == c10) {
                return c10;
            }
        } else {
            if (i10 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            q.b(obj);
        }
        return Unit.f15173a;
    }
}
