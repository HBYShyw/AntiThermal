package u5;

import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.MapsJVM;
import ma.u;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: Action.kt */
@Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\b\b\b\u0086\b\u0018\u00002\u00020\u0001:\u0001\u000bB-\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0007\u0012\u0006\u0010\t\u001a\u00020\u0004\u0012\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\r¢\u0006\u0004\b\u0013\u0010\u0014J\t\u0010\u0003\u001a\u00020\u0002HÖ\u0001J\t\u0010\u0005\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\b\u001a\u00020\u00072\b\u0010\u0006\u001a\u0004\u0018\u00010\u0001HÖ\u0003R\u0017\u0010\t\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\fR#\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\r8\u0006¢\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u0015"}, d2 = {"Lu5/a;", "", "", "toString", "", "hashCode", "other", "", "equals", "action", "I", "a", "()I", "", "extraParams", "Ljava/util/Map;", "b", "()Ljava/util/Map;", "shouldForceFetch", "<init>", "(ZILjava/util/Map;)V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: u5.a, reason: from toString */
/* loaded from: classes.dex */
public final /* data */ class Action {

    /* renamed from: d, reason: collision with root package name */
    private static final Action f18857d;

    /* renamed from: e, reason: collision with root package name */
    private static final Action f18858e;

    /* renamed from: f, reason: collision with root package name */
    private static final Action f18859f;

    /* renamed from: g, reason: collision with root package name */
    private static final Action f18860g;

    /* renamed from: h, reason: collision with root package name */
    private static final Action f18861h;

    /* renamed from: i, reason: collision with root package name */
    private static final Action f18862i;

    /* renamed from: j, reason: collision with root package name */
    public static final C0108a f18863j = new C0108a(null);

    /* renamed from: a, reason: collision with root package name and from toString */
    private final boolean shouldForceFetch;

    /* renamed from: b, reason: collision with root package name and from toString */
    private final int action;

    /* renamed from: c, reason: collision with root package name and from toString */
    private final Map<String, String> extraParams;

    /* compiled from: Action.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0011\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0011\u0010\u0012R\u0014\u0010\u0003\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R\u0014\u0010\u0005\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0005\u0010\u0004R\u0014\u0010\u0006\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0006\u0010\u0004R\u0014\u0010\u0007\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0007\u0010\u0004R\u0014\u0010\b\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\b\u0010\u0004R\u0014\u0010\t\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\t\u0010\u0004R\u0014\u0010\n\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\n\u0010\u0004R\u0014\u0010\u000b\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000b\u0010\u0004R\u0014\u0010\f\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\f\u0010\u0004R\u0014\u0010\r\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\r\u0010\u0004R\u0014\u0010\u000e\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000e\u0010\u0004R\u0014\u0010\u000f\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000f\u0010\u0004R\u0014\u0010\u0010\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0010\u0010\u0004¨\u0006\u0013"}, d2 = {"Lu5/a$a;", "", "", "CLICK_ID_KEY", "Ljava/lang/String;", "CONFIGURATION_LIST_KEY", "EXPOSED_STATE_KEY", "EXPOSED_STATE_VALUE_EXPOSED", "EXPOSED_STATE_VALUE_HIDDEN", "LIFE_CIRCLE_KEY", "LIFE_CIRCLE_VALUE_CREATE", "LIFE_CIRCLE_VALUE_DESTROY", "LIFE_CIRCLE_VALUE_PAUSE", "LIFE_CIRCLE_VALUE_RESUME", "LIFE_CIRCLE_VALUE_START", "LIFE_CIRCLE_VALUE_STOP", "PUBLISH_CONFIGURATION_LIST", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* renamed from: u5.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static final class C0108a {
        private C0108a() {
        }

        public /* synthetic */ C0108a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static {
        Map f10;
        Map f11;
        Map f12;
        Map f13;
        Map f14;
        Map f15;
        f10 = MapsJVM.f(u.a("life_circle", "create"));
        boolean z10 = false;
        int i10 = 2;
        int i11 = 1;
        DefaultConstructorMarker defaultConstructorMarker = null;
        f18857d = new Action(z10, i10, f10, i11, defaultConstructorMarker);
        f11 = MapsJVM.f(u.a("life_circle", "destroy"));
        f18858e = new Action(false, 2, f11, 1, null);
        f12 = MapsJVM.f(u.a("life_circle", "create"));
        f18859f = new Action(z10, i10, f12, i11, defaultConstructorMarker);
        f13 = MapsJVM.f(u.a("life_circle", "stop"));
        boolean z11 = false;
        int i12 = 2;
        int i13 = 1;
        DefaultConstructorMarker defaultConstructorMarker2 = null;
        f18860g = new Action(z11, i12, f13, i13, defaultConstructorMarker2);
        f14 = MapsJVM.f(u.a("life_circle", "resume"));
        f18861h = new Action(z10, i10, f14, i11, defaultConstructorMarker);
        f15 = MapsJVM.f(u.a("life_circle", "pause"));
        f18862i = new Action(z11, i12, f15, i13, defaultConstructorMarker2);
    }

    public Action(boolean z10, int i10, Map<String, String> map) {
        k.e(map, "extraParams");
        this.shouldForceFetch = z10;
        this.action = i10;
        this.extraParams = map;
    }

    /* renamed from: a, reason: from getter */
    public final int getAction() {
        return this.action;
    }

    public final Map<String, String> b() {
        return this.extraParams;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Action)) {
            return false;
        }
        Action action = (Action) other;
        return this.shouldForceFetch == action.shouldForceFetch && this.action == action.action && k.a(this.extraParams, action.extraParams);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [int] */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r0v7 */
    public int hashCode() {
        boolean z10 = this.shouldForceFetch;
        ?? r02 = z10;
        if (z10) {
            r02 = 1;
        }
        int hashCode = ((r02 * 31) + Integer.hashCode(this.action)) * 31;
        Map<String, String> map = this.extraParams;
        return hashCode + (map != null ? map.hashCode() : 0);
    }

    public String toString() {
        return "Action(shouldForceFetch=" + this.shouldForceFetch + ", action=" + this.action + ", extraParams=" + this.extraParams + ")";
    }

    public /* synthetic */ Action(boolean z10, int i10, Map map, int i11, DefaultConstructorMarker defaultConstructorMarker) {
        this((i11 & 1) != 0 ? false : z10, i10, map);
    }
}
