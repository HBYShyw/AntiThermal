package pb;

import fd.StorageManager;
import gd.TypeConstructor;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;
import zc.h;

/* compiled from: ScopesHolderForClass.kt */
/* renamed from: pb.y0, reason: use source file name */
/* loaded from: classes2.dex */
public final class ScopesHolderForClass<T extends zc.h> {

    /* renamed from: a, reason: collision with root package name */
    private final ClassDescriptor f16751a;

    /* renamed from: b, reason: collision with root package name */
    private final ya.l<hd.g, T> f16752b;

    /* renamed from: c, reason: collision with root package name */
    private final hd.g f16753c;

    /* renamed from: d, reason: collision with root package name */
    private final fd.i f16754d;

    /* renamed from: f, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f16750f = {Reflection.g(new PropertyReference1Impl(Reflection.b(ScopesHolderForClass.class), "scopeForOwnerModule", "getScopeForOwnerModule()Lorg/jetbrains/kotlin/resolve/scopes/MemberScope;"))};

    /* renamed from: e, reason: collision with root package name */
    public static final a f16749e = new a(null);

    /* compiled from: ScopesHolderForClass.kt */
    /* renamed from: pb.y0$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final <T extends zc.h> ScopesHolderForClass<T> a(ClassDescriptor classDescriptor, StorageManager storageManager, hd.g gVar, ya.l<? super hd.g, ? extends T> lVar) {
            za.k.e(classDescriptor, "classDescriptor");
            za.k.e(storageManager, "storageManager");
            za.k.e(gVar, "kotlinTypeRefinerForOwnerModule");
            za.k.e(lVar, "scopeFactory");
            return new ScopesHolderForClass<>(classDescriptor, storageManager, lVar, gVar, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ScopesHolderForClass.kt */
    /* renamed from: pb.y0$b */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.a<T> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ScopesHolderForClass<T> f16755e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ hd.g f16756f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(ScopesHolderForClass<T> scopesHolderForClass, hd.g gVar) {
            super(0);
            this.f16755e = scopesHolderForClass;
            this.f16756f = gVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final T invoke() {
            return (T) ((ScopesHolderForClass) this.f16755e).f16752b.invoke(this.f16756f);
        }
    }

    /* compiled from: ScopesHolderForClass.kt */
    /* renamed from: pb.y0$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<T> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ScopesHolderForClass<T> f16757e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(ScopesHolderForClass<T> scopesHolderForClass) {
            super(0);
            this.f16757e = scopesHolderForClass;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final T invoke() {
            return (T) ((ScopesHolderForClass) this.f16757e).f16752b.invoke(((ScopesHolderForClass) this.f16757e).f16753c);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ScopesHolderForClass(ClassDescriptor classDescriptor, StorageManager storageManager, ya.l<? super hd.g, ? extends T> lVar, hd.g gVar) {
        this.f16751a = classDescriptor;
        this.f16752b = lVar;
        this.f16753c = gVar;
        this.f16754d = storageManager.g(new c(this));
    }

    public /* synthetic */ ScopesHolderForClass(ClassDescriptor classDescriptor, StorageManager storageManager, ya.l lVar, hd.g gVar, DefaultConstructorMarker defaultConstructorMarker) {
        this(classDescriptor, storageManager, lVar, gVar);
    }

    private final T d() {
        return (T) fd.m.a(this.f16754d, this, f16750f[0]);
    }

    public final T c(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        if (!gVar.d(wc.c.p(this.f16751a))) {
            return d();
        }
        TypeConstructor n10 = this.f16751a.n();
        za.k.d(n10, "classDescriptor.typeConstructor");
        return !gVar.e(n10) ? d() : (T) gVar.c(this.f16751a, new b(this, gVar));
    }
}
