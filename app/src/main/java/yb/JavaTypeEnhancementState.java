package yb;

import gb.KDeclarationContainer;
import oc.FqName;
import za.DefaultConstructorMarker;
import za.FunctionReference;
import za.Reflection;

/* compiled from: JavaTypeEnhancementState.kt */
/* renamed from: yb.x, reason: use source file name */
/* loaded from: classes2.dex */
public final class JavaTypeEnhancementState {

    /* renamed from: d, reason: collision with root package name */
    public static final b f20148d = new b(null);

    /* renamed from: e, reason: collision with root package name */
    private static final JavaTypeEnhancementState f20149e = new JavaTypeEnhancementState(v.b(null, 1, null), a.f20153n);

    /* renamed from: a, reason: collision with root package name */
    private final Jsr305Settings f20150a;

    /* renamed from: b, reason: collision with root package name */
    private final ya.l<FqName, ReportLevel> f20151b;

    /* renamed from: c, reason: collision with root package name */
    private final boolean f20152c;

    /* compiled from: JavaTypeEnhancementState.kt */
    /* renamed from: yb.x$a */
    /* loaded from: classes2.dex */
    /* synthetic */ class a extends FunctionReference implements ya.l<FqName, ReportLevel> {

        /* renamed from: n, reason: collision with root package name */
        public static final a f20153n = new a();

        a() {
            super(1);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.d(v.class, "compiler.common.jvm");
        }

        @Override // za.CallableReference
        public final String E() {
            return "getDefaultReportLevelForAnnotation(Lorg/jetbrains/kotlin/name/FqName;)Lorg/jetbrains/kotlin/load/java/ReportLevel;";
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final ReportLevel invoke(FqName fqName) {
            za.k.e(fqName, "p0");
            return v.d(fqName);
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "getDefaultReportLevelForAnnotation";
        }
    }

    /* compiled from: JavaTypeEnhancementState.kt */
    /* renamed from: yb.x$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final JavaTypeEnhancementState a() {
            return JavaTypeEnhancementState.f20149e;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public JavaTypeEnhancementState(Jsr305Settings jsr305Settings, ya.l<? super FqName, ? extends ReportLevel> lVar) {
        za.k.e(jsr305Settings, "jsr305");
        za.k.e(lVar, "getReportLevelForAnnotation");
        this.f20150a = jsr305Settings;
        this.f20151b = lVar;
        this.f20152c = jsr305Settings.d() || lVar.invoke(v.e()) == ReportLevel.IGNORE;
    }

    public final boolean b() {
        return this.f20152c;
    }

    public final ya.l<FqName, ReportLevel> c() {
        return this.f20151b;
    }

    public final Jsr305Settings d() {
        return this.f20150a;
    }

    public String toString() {
        return "JavaTypeEnhancementState(jsr305=" + this.f20150a + ", getReportLevelForAnnotation=" + this.f20151b + ')';
    }
}
