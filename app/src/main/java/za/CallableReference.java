package za;

import gb.KCallable;
import gb.KDeclarationContainer;
import gb.KParameter;
import gb.KType;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import xa.KotlinReflectionNotSupportedError;

/* compiled from: CallableReference.java */
/* renamed from: za.c, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class CallableReference implements KCallable, Serializable {

    /* renamed from: k, reason: collision with root package name */
    public static final Object f20349k = a.f20356e;

    /* renamed from: e, reason: collision with root package name */
    private transient KCallable f20350e;

    /* renamed from: f, reason: collision with root package name */
    protected final Object f20351f;

    /* renamed from: g, reason: collision with root package name */
    private final Class f20352g;

    /* renamed from: h, reason: collision with root package name */
    private final String f20353h;

    /* renamed from: i, reason: collision with root package name */
    private final String f20354i;

    /* renamed from: j, reason: collision with root package name */
    private final boolean f20355j;

    /* compiled from: CallableReference.java */
    /* renamed from: za.c$a */
    /* loaded from: classes2.dex */
    private static class a implements Serializable {

        /* renamed from: e, reason: collision with root package name */
        private static final a f20356e = new a();

        private a() {
        }

        private Object readResolve() {
            return f20356e;
        }
    }

    public CallableReference() {
        this(f20349k);
    }

    protected abstract KCallable A();

    public Object B() {
        return this.f20351f;
    }

    public KDeclarationContainer C() {
        Class cls = this.f20352g;
        if (cls == null) {
            return null;
        }
        return this.f20355j ? Reflection.c(cls) : Reflection.b(cls);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public KCallable D() {
        KCallable s7 = s();
        if (s7 != this) {
            return s7;
        }
        throw new KotlinReflectionNotSupportedError();
    }

    public String E() {
        return this.f20354i;
    }

    @Override // gb.KCallable
    public Object d(Object... objArr) {
        return D().d(objArr);
    }

    @Override // gb.KCallable
    public KType f() {
        return D().f();
    }

    @Override // gb.KCallable
    public String getName() {
        return this.f20353h;
    }

    @Override // gb.KCallable
    public List<KParameter> getParameters() {
        return D().getParameters();
    }

    @Override // gb.KAnnotatedElement
    public List<Annotation> i() {
        return D().i();
    }

    @Override // gb.KCallable
    public Object p(Map map) {
        return D().p(map);
    }

    public KCallable s() {
        KCallable kCallable = this.f20350e;
        if (kCallable != null) {
            return kCallable;
        }
        KCallable A = A();
        this.f20350e = A;
        return A;
    }

    protected CallableReference(Object obj) {
        this(obj, null, null, null, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CallableReference(Object obj, Class cls, String str, String str2, boolean z10) {
        this.f20351f = obj;
        this.f20352g = cls;
        this.f20353h = str;
        this.f20354i = str2;
        this.f20355j = z10;
    }
}
