package ob;

import fd.StorageManager;
import fd.m;
import java.util.List;
import kotlin.collections._Collections;
import mb.KotlinBuiltIns;
import pb.ModuleDescriptor;
import rb.AdditionalClassPartsProvider;
import rb.ClassDescriptorFactory;
import sb.x;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;

/* compiled from: JvmBuiltIns.kt */
/* renamed from: ob.f, reason: use source file name */
/* loaded from: classes2.dex */
public final class JvmBuiltIns extends KotlinBuiltIns {

    /* renamed from: k, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f16371k = {Reflection.g(new PropertyReference1Impl(Reflection.b(JvmBuiltIns.class), "customizer", "getCustomizer()Lorg/jetbrains/kotlin/builtins/jvm/JvmBuiltInsCustomizer;"))};

    /* renamed from: h, reason: collision with root package name */
    private final a f16372h;

    /* renamed from: i, reason: collision with root package name */
    private ya.a<b> f16373i;

    /* renamed from: j, reason: collision with root package name */
    private final fd.i f16374j;

    /* compiled from: JvmBuiltIns.kt */
    /* renamed from: ob.f$a */
    /* loaded from: classes2.dex */
    public enum a {
        FROM_DEPENDENCIES,
        FROM_CLASS_LOADER,
        FALLBACK
    }

    /* compiled from: JvmBuiltIns.kt */
    /* renamed from: ob.f$b */
    /* loaded from: classes2.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private final ModuleDescriptor f16379a;

        /* renamed from: b, reason: collision with root package name */
        private final boolean f16380b;

        public b(ModuleDescriptor moduleDescriptor, boolean z10) {
            za.k.e(moduleDescriptor, "ownerModuleDescriptor");
            this.f16379a = moduleDescriptor;
            this.f16380b = z10;
        }

        public final ModuleDescriptor a() {
            return this.f16379a;
        }

        public final boolean b() {
            return this.f16380b;
        }
    }

    /* compiled from: JvmBuiltIns.kt */
    /* renamed from: ob.f$c */
    /* loaded from: classes2.dex */
    public /* synthetic */ class c {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f16381a;

        static {
            int[] iArr = new int[a.values().length];
            try {
                iArr[a.FROM_DEPENDENCIES.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[a.FROM_CLASS_LOADER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[a.FALLBACK.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f16381a = iArr;
        }
    }

    /* compiled from: JvmBuiltIns.kt */
    /* renamed from: ob.f$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.a<i> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ StorageManager f16383f;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: JvmBuiltIns.kt */
        /* renamed from: ob.f$d$a */
        /* loaded from: classes2.dex */
        public static final class a extends Lambda implements ya.a<b> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ JvmBuiltIns f16384e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(JvmBuiltIns jvmBuiltIns) {
                super(0);
                this.f16384e = jvmBuiltIns;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final b invoke() {
                ya.a aVar = this.f16384e.f16373i;
                if (aVar != null) {
                    b bVar = (b) aVar.invoke();
                    this.f16384e.f16373i = null;
                    return bVar;
                }
                throw new AssertionError("JvmBuiltins instance has not been initialized properly");
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(StorageManager storageManager) {
            super(0);
            this.f16383f = storageManager;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final i invoke() {
            x r10 = JvmBuiltIns.this.r();
            za.k.d(r10, "builtInsModule");
            return new i(r10, this.f16383f, new a(JvmBuiltIns.this));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: JvmBuiltIns.kt */
    /* renamed from: ob.f$e */
    /* loaded from: classes2.dex */
    public static final class e extends Lambda implements ya.a<b> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ModuleDescriptor f16385e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ boolean f16386f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        e(ModuleDescriptor moduleDescriptor, boolean z10) {
            super(0);
            this.f16385e = moduleDescriptor;
            this.f16386f = z10;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final b invoke() {
            return new b(this.f16385e, this.f16386f);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public JvmBuiltIns(StorageManager storageManager, a aVar) {
        super(storageManager);
        za.k.e(storageManager, "storageManager");
        za.k.e(aVar, "kind");
        this.f16372h = aVar;
        this.f16374j = storageManager.g(new d(storageManager));
        int i10 = c.f16381a[aVar.ordinal()];
        if (i10 == 2) {
            f(false);
        } else {
            if (i10 != 3) {
                return;
            }
            f(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // mb.KotlinBuiltIns
    /* renamed from: G0, reason: merged with bridge method [inline-methods] */
    public List<ClassDescriptorFactory> v() {
        List<ClassDescriptorFactory> l02;
        Iterable<ClassDescriptorFactory> v7 = super.v();
        za.k.d(v7, "super.getClassDescriptorFactories()");
        StorageManager U = U();
        za.k.d(U, "storageManager");
        x r10 = r();
        za.k.d(r10, "builtInsModule");
        l02 = _Collections.l0(v7, new JvmBuiltInClassDescriptorFactory(U, r10, null, 4, null));
        return l02;
    }

    public final i H0() {
        return (i) m.a(this.f16374j, this, f16371k[0]);
    }

    public final void I0(ModuleDescriptor moduleDescriptor, boolean z10) {
        za.k.e(moduleDescriptor, "moduleDescriptor");
        J0(new e(moduleDescriptor, z10));
    }

    public final void J0(ya.a<b> aVar) {
        za.k.e(aVar, "computation");
        this.f16373i = aVar;
    }

    @Override // mb.KotlinBuiltIns
    protected rb.c M() {
        return H0();
    }

    @Override // mb.KotlinBuiltIns
    protected AdditionalClassPartsProvider g() {
        return H0();
    }
}
