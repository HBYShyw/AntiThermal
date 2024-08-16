package androidx.lifecycle;

import android.app.Application;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import kotlin.Metadata;
import w.a;
import za.DefaultConstructorMarker;

/* compiled from: ViewModelProvider.kt */
@Metadata(bv = {}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0016\u0018\u00002\u00020\u0001:\u0004\u0006\n\u001a\u001bB#\b\u0007\u0012\u0006\u0010\u000e\u001a\u00020\f\u0012\u0006\u0010\u0011\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0012¢\u0006\u0004\b\u0014\u0010\u0015B\u0011\b\u0016\u0012\u0006\u0010\u0017\u001a\u00020\u0016¢\u0006\u0004\b\u0014\u0010\u0018B\u0019\b\u0016\u0012\u0006\u0010\u0017\u001a\u00020\u0016\u0012\u0006\u0010\u0011\u001a\u00020\u000f¢\u0006\u0004\b\u0014\u0010\u0019J(\u0010\u0006\u001a\u00028\u0000\"\b\b\u0000\u0010\u0003*\u00020\u00022\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0097\u0002¢\u0006\u0004\b\u0006\u0010\u0007J0\u0010\n\u001a\u00028\u0000\"\b\b\u0000\u0010\u0003*\u00020\u00022\u0006\u0010\t\u001a\u00020\b2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0097\u0002¢\u0006\u0004\b\n\u0010\u000bR\u0014\u0010\u000e\u001a\u00020\f8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0006\u0010\rR\u0014\u0010\u0011\u001a\u00020\u000f8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\n\u0010\u0010¨\u0006\u001c"}, d2 = {"Landroidx/lifecycle/h0;", "", "Landroidx/lifecycle/g0;", "T", "Ljava/lang/Class;", "modelClass", "a", "(Ljava/lang/Class;)Landroidx/lifecycle/g0;", "", "key", "b", "(Ljava/lang/String;Ljava/lang/Class;)Landroidx/lifecycle/g0;", "Landroidx/lifecycle/j0;", "Landroidx/lifecycle/j0;", "store", "Landroidx/lifecycle/h0$b;", "Landroidx/lifecycle/h0$b;", "factory", "Lw/a;", "defaultCreationExtras", "<init>", "(Landroidx/lifecycle/j0;Landroidx/lifecycle/h0$b;Lw/a;)V", "Landroidx/lifecycle/k0;", "owner", "(Landroidx/lifecycle/k0;)V", "(Landroidx/lifecycle/k0;Landroidx/lifecycle/h0$b;)V", "c", "d", "lifecycle-viewmodel_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public class h0 {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final ViewModelStore store;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final b factory;

    /* renamed from: c, reason: collision with root package name */
    private final w.a f3193c;

    /* compiled from: ViewModelProvider.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u0000 \u00062\u00020\u0001:\u0001\u0006J'\u0010\u0006\u001a\u00028\u0000\"\b\b\u0000\u0010\u0003*\u00020\u00022\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0016¢\u0006\u0004\b\u0006\u0010\u0007J/\u0010\n\u001a\u00028\u0000\"\b\b\u0000\u0010\u0003*\u00020\u00022\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u00042\u0006\u0010\t\u001a\u00020\bH\u0016¢\u0006\u0004\b\n\u0010\u000bø\u0001\u0000\u0082\u0002\u0006\n\u0004\b!0\u0001¨\u0006\fÀ\u0006\u0001"}, d2 = {"Landroidx/lifecycle/h0$b;", "", "Landroidx/lifecycle/g0;", "T", "Ljava/lang/Class;", "modelClass", "a", "(Ljava/lang/Class;)Landroidx/lifecycle/g0;", "Lw/a;", "extras", "b", "(Ljava/lang/Class;Lw/a;)Landroidx/lifecycle/g0;", "lifecycle-viewmodel_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public interface b {

        /* renamed from: a, reason: collision with root package name and from kotlin metadata */
        public static final Companion INSTANCE = Companion.f3200a;

        /* compiled from: ViewModelProvider.kt */
        @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003¨\u0006\u0004"}, d2 = {"Landroidx/lifecycle/h0$b$a;", "", "<init>", "()V", "lifecycle-viewmodel_release"}, k = 1, mv = {1, 6, 0})
        /* renamed from: androidx.lifecycle.h0$b$a, reason: from kotlin metadata */
        /* loaded from: classes.dex */
        public static final class Companion {

            /* renamed from: a, reason: collision with root package name */
            static final /* synthetic */ Companion f3200a = new Companion();

            private Companion() {
            }
        }

        default <T extends ViewModel> T a(Class<T> modelClass) {
            za.k.e(modelClass, "modelClass");
            throw new UnsupportedOperationException("Factory.create(String) is unsupported.  This Factory requires `CreationExtras` to be passed into `create` method.");
        }

        default <T extends ViewModel> T b(Class<T> modelClass, w.a extras) {
            za.k.e(modelClass, "modelClass");
            za.k.e(extras, "extras");
            return (T) a(modelClass);
        }
    }

    /* compiled from: ViewModelProvider.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0016\u0018\u0000 \n2\u00020\u0001:\u0001\u0006B\u0007¢\u0006\u0004\b\b\u0010\tJ'\u0010\u0006\u001a\u00028\u0000\"\b\b\u0000\u0010\u0003*\u00020\u00022\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0016¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\u000b"}, d2 = {"Landroidx/lifecycle/h0$c;", "Landroidx/lifecycle/h0$b;", "Landroidx/lifecycle/g0;", "T", "Ljava/lang/Class;", "modelClass", "a", "(Ljava/lang/Class;)Landroidx/lifecycle/g0;", "<init>", "()V", "b", "lifecycle-viewmodel_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static class c implements b {

        /* renamed from: c, reason: collision with root package name */
        private static c f3202c;

        /* renamed from: b, reason: collision with root package name and from kotlin metadata */
        public static final Companion INSTANCE = new Companion(null);

        /* renamed from: d, reason: collision with root package name */
        public static final a.b<String> f3203d = Companion.C0007a.f3204a;

        /* compiled from: ViewModelProvider.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\u0003B\t\b\u0002¢\u0006\u0004\b\u000e\u0010\u0006R\u001a\u0010\u0007\u001a\u00020\u00028GX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0003\u0010\u0004R\u001a\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\b8\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\n\u0010\u000bR\u0018\u0010\f\u001a\u0004\u0018\u00010\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\f\u0010\r¨\u0006\u000f"}, d2 = {"Landroidx/lifecycle/h0$c$a;", "", "Landroidx/lifecycle/h0$c;", "a", "()Landroidx/lifecycle/h0$c;", "getInstance$annotations", "()V", "instance", "Lw/a$b;", "", "VIEW_MODEL_KEY", "Lw/a$b;", "sInstance", "Landroidx/lifecycle/h0$c;", "<init>", "lifecycle-viewmodel_release"}, k = 1, mv = {1, 6, 0})
        /* renamed from: androidx.lifecycle.h0$c$a, reason: from kotlin metadata */
        /* loaded from: classes.dex */
        public static final class Companion {

            /* compiled from: ViewModelProvider.kt */
            @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\bÂ\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0003\u0010\u0004¨\u0006\u0005"}, d2 = {"Landroidx/lifecycle/h0$c$a$a;", "Lw/a$b;", "", "<init>", "()V", "lifecycle-viewmodel_release"}, k = 1, mv = {1, 6, 0})
            /* renamed from: androidx.lifecycle.h0$c$a$a, reason: collision with other inner class name */
            /* loaded from: classes.dex */
            private static final class C0007a implements a.b<String> {

                /* renamed from: a, reason: collision with root package name */
                public static final C0007a f3204a = new C0007a();

                private C0007a() {
                }
            }

            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public final c a() {
                if (c.f3202c == null) {
                    c.f3202c = new c();
                }
                c cVar = c.f3202c;
                za.k.b(cVar);
                return cVar;
            }
        }

        @Override // androidx.lifecycle.h0.b
        public <T extends ViewModel> T a(Class<T> modelClass) {
            za.k.e(modelClass, "modelClass");
            try {
                T newInstance = modelClass.newInstance();
                za.k.d(newInstance, "{\n                modelC…wInstance()\n            }");
                return newInstance;
            } catch (IllegalAccessException e10) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e10);
            } catch (InstantiationException e11) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e11);
            }
        }
    }

    /* compiled from: ViewModelProvider.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0017\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0006\u0010\u0007J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\b"}, d2 = {"Landroidx/lifecycle/h0$d;", "", "Landroidx/lifecycle/g0;", "viewModel", "Lma/f0;", "c", "<init>", "()V", "lifecycle-viewmodel_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static class d {
        public void c(ViewModel viewModel) {
            za.k.e(viewModel, "viewModel");
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public h0(ViewModelStore viewModelStore, b bVar) {
        this(viewModelStore, bVar, null, 4, null);
        za.k.e(viewModelStore, "store");
        za.k.e(bVar, "factory");
    }

    public h0(ViewModelStore viewModelStore, b bVar, w.a aVar) {
        za.k.e(viewModelStore, "store");
        za.k.e(bVar, "factory");
        za.k.e(aVar, "defaultCreationExtras");
        this.store = viewModelStore;
        this.factory = bVar;
        this.f3193c = aVar;
    }

    public <T extends ViewModel> T a(Class<T> modelClass) {
        za.k.e(modelClass, "modelClass");
        String canonicalName = modelClass.getCanonicalName();
        if (canonicalName != null) {
            return (T) b("androidx.lifecycle.ViewModelProvider.DefaultKey:" + canonicalName, modelClass);
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    public <T extends ViewModel> T b(String key, Class<T> modelClass) {
        T t7;
        za.k.e(key, "key");
        za.k.e(modelClass, "modelClass");
        T t10 = (T) this.store.b(key);
        if (modelClass.isInstance(t10)) {
            Object obj = this.factory;
            d dVar = obj instanceof d ? (d) obj : null;
            if (dVar != null) {
                za.k.d(t10, "viewModel");
                dVar.c(t10);
            }
            Objects.requireNonNull(t10, "null cannot be cast to non-null type T of androidx.lifecycle.ViewModelProvider.get");
            return t10;
        }
        w.d dVar2 = new w.d(this.f3193c);
        dVar2.c(c.f3203d, key);
        try {
            t7 = (T) this.factory.b(modelClass, dVar2);
        } catch (AbstractMethodError unused) {
            t7 = (T) this.factory.a(modelClass);
        }
        this.store.d(key, t7);
        return t7;
    }

    /* compiled from: ViewModelProvider.kt */
    @Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0007\b\u0016\u0018\u0000 \u00192\u00020\u0001:\u0001\u000eB\u001b\b\u0002\u0012\b\u0010\u0012\u001a\u0004\u0018\u00010\u0006\u0012\u0006\u0010\u0014\u001a\u00020\u0013¢\u0006\u0004\b\u0015\u0010\u0016B\t\b\u0016¢\u0006\u0004\b\u0015\u0010\u0017B\u0011\b\u0016\u0012\u0006\u0010\u0012\u001a\u00020\u0006¢\u0006\u0004\b\u0015\u0010\u0018J/\u0010\b\u001a\u00028\u0000\"\b\b\u0000\u0010\u0003*\u00020\u00022\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0004\b\b\u0010\tJ/\u0010\f\u001a\u00028\u0000\"\b\b\u0000\u0010\u0003*\u00020\u00022\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u00042\u0006\u0010\u000b\u001a\u00020\nH\u0016¢\u0006\u0004\b\f\u0010\rJ'\u0010\u000e\u001a\u00028\u0000\"\b\b\u0000\u0010\u0003*\u00020\u00022\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0016¢\u0006\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0012\u001a\u0004\u0018\u00010\u00068\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0010\u0010\u0011¨\u0006\u001a"}, d2 = {"Landroidx/lifecycle/h0$a;", "Landroidx/lifecycle/h0$c;", "Landroidx/lifecycle/g0;", "T", "Ljava/lang/Class;", "modelClass", "Landroid/app/Application;", "app", "g", "(Ljava/lang/Class;Landroid/app/Application;)Landroidx/lifecycle/g0;", "Lw/a;", "extras", "b", "(Ljava/lang/Class;Lw/a;)Landroidx/lifecycle/g0;", "a", "(Ljava/lang/Class;)Landroidx/lifecycle/g0;", "e", "Landroid/app/Application;", "application", "", "unused", "<init>", "(Landroid/app/Application;I)V", "()V", "(Landroid/app/Application;)V", "f", "lifecycle-viewmodel_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static class a extends c {

        /* renamed from: g, reason: collision with root package name */
        private static a f3195g;

        /* renamed from: e, reason: collision with root package name and from kotlin metadata */
        private final Application application;

        /* renamed from: f, reason: collision with root package name and from kotlin metadata */
        public static final Companion INSTANCE = new Companion(null);

        /* renamed from: h, reason: collision with root package name */
        public static final a.b<Application> f3196h = Companion.C0006a.f3198a;

        /* compiled from: ViewModelProvider.kt */
        @Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\u0005B\t\b\u0002¢\u0006\u0004\b\u0013\u0010\u0014J\u0017\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0000¢\u0006\u0004\b\u0005\u0010\u0006J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\u0007H\u0007R\u001a\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\u000b8\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\f\u0010\rR\u0014\u0010\u000f\u001a\u00020\u000e8\u0000X\u0080T¢\u0006\u0006\n\u0004\b\u000f\u0010\u0010R\u0018\u0010\u0011\u001a\u0004\u0018\u00010\t8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0011\u0010\u0012¨\u0006\u0015"}, d2 = {"Landroidx/lifecycle/h0$a$a;", "", "Landroidx/lifecycle/k0;", "owner", "Landroidx/lifecycle/h0$b;", "a", "(Landroidx/lifecycle/k0;)Landroidx/lifecycle/h0$b;", "Landroid/app/Application;", "application", "Landroidx/lifecycle/h0$a;", "b", "Lw/a$b;", "APPLICATION_KEY", "Lw/a$b;", "", "DEFAULT_KEY", "Ljava/lang/String;", "sInstance", "Landroidx/lifecycle/h0$a;", "<init>", "()V", "lifecycle-viewmodel_release"}, k = 1, mv = {1, 6, 0})
        /* renamed from: androidx.lifecycle.h0$a$a, reason: collision with other inner class name and from kotlin metadata */
        /* loaded from: classes.dex */
        public static final class Companion {

            /* compiled from: ViewModelProvider.kt */
            @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\bÂ\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0003\u0010\u0004¨\u0006\u0005"}, d2 = {"Landroidx/lifecycle/h0$a$a$a;", "Lw/a$b;", "Landroid/app/Application;", "<init>", "()V", "lifecycle-viewmodel_release"}, k = 1, mv = {1, 6, 0})
            /* renamed from: androidx.lifecycle.h0$a$a$a, reason: collision with other inner class name */
            /* loaded from: classes.dex */
            private static final class C0006a implements a.b<Application> {

                /* renamed from: a, reason: collision with root package name */
                public static final C0006a f3198a = new C0006a();

                private C0006a() {
                }
            }

            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public final b a(ViewModelStoreOwner owner) {
                za.k.e(owner, "owner");
                if (!(owner instanceof HasDefaultViewModelProviderFactory)) {
                    return c.INSTANCE.a();
                }
                b defaultViewModelProviderFactory = ((HasDefaultViewModelProviderFactory) owner).getDefaultViewModelProviderFactory();
                za.k.d(defaultViewModelProviderFactory, "owner.defaultViewModelProviderFactory");
                return defaultViewModelProviderFactory;
            }

            public final a b(Application application) {
                za.k.e(application, "application");
                if (a.f3195g == null) {
                    a.f3195g = new a(application);
                }
                a aVar = a.f3195g;
                za.k.b(aVar);
                return aVar;
            }
        }

        private a(Application application, int i10) {
            this.application = application;
        }

        private final <T extends ViewModel> T g(Class<T> modelClass, Application app) {
            if (AndroidViewModel.class.isAssignableFrom(modelClass)) {
                try {
                    T newInstance = modelClass.getConstructor(Application.class).newInstance(app);
                    za.k.d(newInstance, "{\n                try {\n…          }\n            }");
                    return newInstance;
                } catch (IllegalAccessException e10) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e10);
                } catch (InstantiationException e11) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e11);
                } catch (NoSuchMethodException e12) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e12);
                } catch (InvocationTargetException e13) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e13);
                }
            }
            return (T) super.a(modelClass);
        }

        @Override // androidx.lifecycle.h0.c, androidx.lifecycle.h0.b
        public <T extends ViewModel> T a(Class<T> modelClass) {
            za.k.e(modelClass, "modelClass");
            Application application = this.application;
            if (application != null) {
                return (T) g(modelClass, application);
            }
            throw new UnsupportedOperationException("AndroidViewModelFactory constructed with empty constructor works only with create(modelClass: Class<T>, extras: CreationExtras).");
        }

        @Override // androidx.lifecycle.h0.b
        public <T extends ViewModel> T b(Class<T> modelClass, w.a extras) {
            za.k.e(modelClass, "modelClass");
            za.k.e(extras, "extras");
            if (this.application != null) {
                return (T) a(modelClass);
            }
            Application application = (Application) extras.a(f3196h);
            if (application != null) {
                return (T) g(modelClass, application);
            }
            if (!AndroidViewModel.class.isAssignableFrom(modelClass)) {
                return (T) super.a(modelClass);
            }
            throw new IllegalArgumentException("CreationExtras must have an application by `APPLICATION_KEY`");
        }

        public a() {
            this(null, 0);
        }

        /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
        public a(Application application) {
            this(application, 0);
            za.k.e(application, "application");
        }
    }

    public /* synthetic */ h0(ViewModelStore viewModelStore, b bVar, w.a aVar, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(viewModelStore, bVar, (i10 & 4) != 0 ? a.C0113a.f19305b : aVar);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public h0(ViewModelStoreOwner viewModelStoreOwner) {
        this(r0, a.INSTANCE.a(viewModelStoreOwner), i0.a(viewModelStoreOwner));
        za.k.e(viewModelStoreOwner, "owner");
        ViewModelStore viewModelStore = viewModelStoreOwner.getViewModelStore();
        za.k.d(viewModelStore, "owner.viewModelStore");
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public h0(ViewModelStoreOwner viewModelStoreOwner, b bVar) {
        this(r0, bVar, i0.a(viewModelStoreOwner));
        za.k.e(viewModelStoreOwner, "owner");
        za.k.e(bVar, "factory");
        ViewModelStore viewModelStore = viewModelStoreOwner.getViewModelStore();
        za.k.d(viewModelStore, "owner.viewModelStore");
    }
}
