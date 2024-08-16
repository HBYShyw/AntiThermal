package mb;

import fd.StorageManager;
import java.util.ServiceLoader;
import kotlin.collections._Collections;
import pb.ModuleDescriptor;
import pb.m0;
import rb.AdditionalClassPartsProvider;
import rb.ClassDescriptorFactory;
import za.Lambda;

/* compiled from: BuiltInsLoader.kt */
/* renamed from: mb.a, reason: use source file name */
/* loaded from: classes2.dex */
public interface BuiltInsLoader {

    /* renamed from: a, reason: collision with root package name */
    public static final a f15209a = a.f15210a;

    /* compiled from: BuiltInsLoader.kt */
    /* renamed from: mb.a$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ a f15210a = new a();

        /* renamed from: b, reason: collision with root package name */
        private static final ma.h<BuiltInsLoader> f15211b;

        /* compiled from: BuiltInsLoader.kt */
        /* renamed from: mb.a$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0075a extends Lambda implements ya.a<BuiltInsLoader> {

            /* renamed from: e, reason: collision with root package name */
            public static final C0075a f15212e = new C0075a();

            C0075a() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final BuiltInsLoader invoke() {
                Object U;
                ServiceLoader load = ServiceLoader.load(BuiltInsLoader.class, BuiltInsLoader.class.getClassLoader());
                za.k.d(load, "implementations");
                U = _Collections.U(load);
                BuiltInsLoader builtInsLoader = (BuiltInsLoader) U;
                if (builtInsLoader != null) {
                    return builtInsLoader;
                }
                throw new IllegalStateException("No BuiltInsLoader implementation was found. Please ensure that the META-INF/services/ is not stripped from your application and that the Java virtual machine is not running under a security manager");
            }
        }

        static {
            ma.h<BuiltInsLoader> a10;
            a10 = ma.j.a(ma.l.PUBLICATION, C0075a.f15212e);
            f15211b = a10;
        }

        private a() {
        }

        public final BuiltInsLoader a() {
            return f15211b.getValue();
        }
    }

    m0 a(StorageManager storageManager, ModuleDescriptor moduleDescriptor, Iterable<? extends ClassDescriptorFactory> iterable, rb.c cVar, AdditionalClassPartsProvider additionalClassPartsProvider, boolean z10);
}
