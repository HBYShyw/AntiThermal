package yb;

import java.util.Map;
import kotlin.collections.m0;
import oc.FqName;

/* compiled from: JavaNullabilityAnnotationSettings.kt */
/* loaded from: classes2.dex */
public interface d0<T> {

    /* renamed from: a, reason: collision with root package name */
    public static final a f20063a = a.f20064a;

    /* compiled from: JavaNullabilityAnnotationSettings.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ a f20064a = new a();

        /* renamed from: b, reason: collision with root package name */
        private static final d0 f20065b;

        static {
            Map i10;
            i10 = m0.i();
            f20065b = new e0(i10);
        }

        private a() {
        }

        public final d0 a() {
            return f20065b;
        }
    }

    T a(FqName fqName);
}
