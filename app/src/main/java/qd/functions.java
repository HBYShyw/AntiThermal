package qd;

import ma.Unit;
import ya.l;
import ya.p;
import ya.q;
import za.Lambda;

/* compiled from: functions.kt */
/* renamed from: qd.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class functions {

    /* renamed from: a, reason: collision with root package name */
    private static final l<Object, Object> f17414a = f.f17425e;

    /* renamed from: b, reason: collision with root package name */
    private static final l<Object, Boolean> f17415b = b.f17421e;

    /* renamed from: c, reason: collision with root package name */
    private static final l<Object, Object> f17416c = a.f17420e;

    /* renamed from: d, reason: collision with root package name */
    private static final l<Object, Unit> f17417d = c.f17422e;

    /* renamed from: e, reason: collision with root package name */
    private static final p<Object, Object, Unit> f17418e = d.f17423e;

    /* renamed from: f, reason: collision with root package name */
    private static final q<Object, Object, Object, Unit> f17419f = e.f17424e;

    /* compiled from: functions.kt */
    /* renamed from: qd.d$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements l {

        /* renamed from: e, reason: collision with root package name */
        public static final a f17420e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Void invoke(Object obj) {
            return null;
        }
    }

    /* compiled from: functions.kt */
    /* renamed from: qd.d$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements l<Object, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f17421e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(Object obj) {
            return Boolean.TRUE;
        }
    }

    /* compiled from: functions.kt */
    /* renamed from: qd.d$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements l<Object, Unit> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f17422e = new c();

        c() {
            super(1);
        }

        public final void a(Object obj) {
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(Object obj) {
            a(obj);
            return Unit.f15173a;
        }
    }

    /* compiled from: functions.kt */
    /* renamed from: qd.d$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements p<Object, Object, Unit> {

        /* renamed from: e, reason: collision with root package name */
        public static final d f17423e = new d();

        d() {
            super(2);
        }

        public final void a(Object obj, Object obj2) {
        }

        @Override // ya.p
        public /* bridge */ /* synthetic */ Unit invoke(Object obj, Object obj2) {
            a(obj, obj2);
            return Unit.f15173a;
        }
    }

    /* compiled from: functions.kt */
    /* renamed from: qd.d$e */
    /* loaded from: classes2.dex */
    static final class e extends Lambda implements q<Object, Object, Object, Unit> {

        /* renamed from: e, reason: collision with root package name */
        public static final e f17424e = new e();

        e() {
            super(3);
        }

        public final void a(Object obj, Object obj2, Object obj3) {
        }

        @Override // ya.q
        public /* bridge */ /* synthetic */ Unit g(Object obj, Object obj2, Object obj3) {
            a(obj, obj2, obj3);
            return Unit.f15173a;
        }
    }

    /* compiled from: functions.kt */
    /* renamed from: qd.d$f */
    /* loaded from: classes2.dex */
    static final class f extends Lambda implements l<Object, Object> {

        /* renamed from: e, reason: collision with root package name */
        public static final f f17425e = new f();

        f() {
            super(1);
        }

        @Override // ya.l
        public final Object invoke(Object obj) {
            return obj;
        }
    }

    public static final <T> l<T, Boolean> a() {
        return (l<T, Boolean>) f17415b;
    }

    public static final q<Object, Object, Object, Unit> b() {
        return f17419f;
    }
}
