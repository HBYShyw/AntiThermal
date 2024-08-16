package yb;

import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import java.util.List;
import java.util.Map;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.m0;
import oc.FqName;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: Jsr305Settings.kt */
/* renamed from: yb.z, reason: use source file name */
/* loaded from: classes2.dex */
public final class Jsr305Settings {

    /* renamed from: a, reason: collision with root package name */
    private final ReportLevel f20155a;

    /* renamed from: b, reason: collision with root package name */
    private final ReportLevel f20156b;

    /* renamed from: c, reason: collision with root package name */
    private final Map<FqName, ReportLevel> f20157c;

    /* renamed from: d, reason: collision with root package name */
    private final ma.h f20158d;

    /* renamed from: e, reason: collision with root package name */
    private final boolean f20159e;

    /* compiled from: Jsr305Settings.kt */
    /* renamed from: yb.z$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<String[]> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final String[] invoke() {
            List c10;
            List a10;
            Jsr305Settings jsr305Settings = Jsr305Settings.this;
            c10 = CollectionsJVM.c();
            c10.add(jsr305Settings.a().b());
            ReportLevel b10 = jsr305Settings.b();
            if (b10 != null) {
                c10.add("under-migration:" + b10.b());
            }
            for (Map.Entry<FqName, ReportLevel> entry : jsr305Settings.c().entrySet()) {
                c10.add('@' + entry.getKey() + COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR + entry.getValue().b());
            }
            a10 = CollectionsJVM.a(c10);
            return (String[]) a10.toArray(new String[0]);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Jsr305Settings(ReportLevel reportLevel, ReportLevel reportLevel2, Map<FqName, ? extends ReportLevel> map) {
        ma.h b10;
        za.k.e(reportLevel, "globalLevel");
        za.k.e(map, "userDefinedLevelForSpecificAnnotation");
        this.f20155a = reportLevel;
        this.f20156b = reportLevel2;
        this.f20157c = map;
        b10 = ma.j.b(new a());
        this.f20158d = b10;
        ReportLevel reportLevel3 = ReportLevel.IGNORE;
        this.f20159e = reportLevel == reportLevel3 && reportLevel2 == reportLevel3 && map.isEmpty();
    }

    public final ReportLevel a() {
        return this.f20155a;
    }

    public final ReportLevel b() {
        return this.f20156b;
    }

    public final Map<FqName, ReportLevel> c() {
        return this.f20157c;
    }

    public final boolean d() {
        return this.f20159e;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Jsr305Settings)) {
            return false;
        }
        Jsr305Settings jsr305Settings = (Jsr305Settings) obj;
        return this.f20155a == jsr305Settings.f20155a && this.f20156b == jsr305Settings.f20156b && za.k.a(this.f20157c, jsr305Settings.f20157c);
    }

    public int hashCode() {
        int hashCode = this.f20155a.hashCode() * 31;
        ReportLevel reportLevel = this.f20156b;
        return ((hashCode + (reportLevel == null ? 0 : reportLevel.hashCode())) * 31) + this.f20157c.hashCode();
    }

    public String toString() {
        return "Jsr305Settings(globalLevel=" + this.f20155a + ", migrationLevel=" + this.f20156b + ", userDefinedLevelForSpecificAnnotation=" + this.f20157c + ')';
    }

    public /* synthetic */ Jsr305Settings(ReportLevel reportLevel, ReportLevel reportLevel2, Map map, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(reportLevel, (i10 & 2) != 0 ? null : reportLevel2, (i10 & 4) != 0 ? m0.i() : map);
    }
}
