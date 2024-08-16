package androidx.window.sidecar;

import androidx.window.core.Bounds;
import androidx.window.sidecar.FoldingFeature;
import java.util.Objects;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: HardwareFoldingFeature.kt */
@Metadata(bv = {}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\b\u0000\u0018\u0000 \u001f2\u00020\u0001:\u0002 !B\u001f\u0012\u0006\u0010\r\u001a\u00020\n\u0012\u0006\u0010\u0013\u001a\u00020\u000e\u0012\u0006\u0010\u0019\u001a\u00020\u0014¢\u0006\u0004\b\u001d\u0010\u001eJ\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u0013\u0010\u0007\u001a\u00020\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0096\u0002J\b\u0010\t\u001a\u00020\bH\u0016R\u0014\u0010\r\u001a\u00020\n8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u000b\u0010\fR\u001a\u0010\u0013\u001a\u00020\u000e8\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0019\u001a\u00020\u00148\u0016X\u0096\u0004¢\u0006\f\n\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018R\u0014\u0010\u001c\u001a\u00020\u001a8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\u001b¨\u0006\""}, d2 = {"Landroidx/window/layout/HardwareFoldingFeature;", "Landroidx/window/layout/FoldingFeature;", "", "toString", "", "other", "", "equals", "", "hashCode", "Landroidx/window/core/Bounds;", "a", "Landroidx/window/core/Bounds;", "featureBounds", "Landroidx/window/layout/HardwareFoldingFeature$Type;", "b", "Landroidx/window/layout/HardwareFoldingFeature$Type;", "getType$window_release", "()Landroidx/window/layout/HardwareFoldingFeature$Type;", "type", "Landroidx/window/layout/FoldingFeature$State;", "c", "Landroidx/window/layout/FoldingFeature$State;", "getState", "()Landroidx/window/layout/FoldingFeature$State;", "state", "Landroidx/window/layout/FoldingFeature$Orientation;", "()Landroidx/window/layout/FoldingFeature$Orientation;", "orientation", "<init>", "(Landroidx/window/core/Bounds;Landroidx/window/layout/HardwareFoldingFeature$Type;Landroidx/window/layout/FoldingFeature$State;)V", "d", "Companion", "Type", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class HardwareFoldingFeature implements FoldingFeature {

    /* renamed from: d, reason: collision with root package name and from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final Bounds featureBounds;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata and from toString */
    private final Type type;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private final FoldingFeature.State state;

    /* compiled from: HardwareFoldingFeature.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0080\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0007\u0010\bJ\u0017\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0000¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\t"}, d2 = {"Landroidx/window/layout/HardwareFoldingFeature$Companion;", "", "Landroidx/window/core/Bounds;", "bounds", "Lma/f0;", "a", "(Landroidx/window/core/Bounds;)V", "<init>", "()V", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void a(Bounds bounds) {
            k.e(bounds, "bounds");
            if ((bounds.d() == 0 && bounds.a() == 0) ? false : true) {
                if (!(bounds.getLeft() == 0 || bounds.getTop() == 0)) {
                    throw new IllegalArgumentException("Bounding rectangle must start at the top or left window edge for folding features".toString());
                }
                return;
            }
            throw new IllegalArgumentException("Bounds must be non zero".toString());
        }
    }

    /* compiled from: HardwareFoldingFeature.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\t\b\u0000\u0018\u0000 \t2\u00020\u0001:\u0001\nB\u0011\b\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0002¢\u0006\u0004\b\u0007\u0010\bJ\b\u0010\u0003\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0004\u0010\u0005¨\u0006\u000b"}, d2 = {"Landroidx/window/layout/HardwareFoldingFeature$Type;", "", "", "toString", "a", "Ljava/lang/String;", "description", "<init>", "(Ljava/lang/String;)V", "b", "Companion", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class Type {

        /* renamed from: b, reason: collision with root package name and from kotlin metadata */
        public static final Companion INSTANCE = new Companion(null);

        /* renamed from: c, reason: collision with root package name */
        private static final Type f4442c = new Type("FOLD");

        /* renamed from: d, reason: collision with root package name */
        private static final Type f4443d = new Type("HINGE");

        /* renamed from: a, reason: collision with root package name and from kotlin metadata */
        private final String description;

        /* compiled from: HardwareFoldingFeature.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0080\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\t\u0010\nR\u0017\u0010\u0003\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006R\u0017\u0010\u0007\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\u0007\u0010\u0004\u001a\u0004\b\b\u0010\u0006¨\u0006\u000b"}, d2 = {"Landroidx/window/layout/HardwareFoldingFeature$Type$Companion;", "", "Landroidx/window/layout/HardwareFoldingFeature$Type;", "FOLD", "Landroidx/window/layout/HardwareFoldingFeature$Type;", "a", "()Landroidx/window/layout/HardwareFoldingFeature$Type;", "HINGE", "b", "<init>", "()V", "window_release"}, k = 1, mv = {1, 6, 0})
        /* loaded from: classes.dex */
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public final Type a() {
                return Type.f4442c;
            }

            public final Type b() {
                return Type.f4443d;
            }
        }

        private Type(String str) {
            this.description = str;
        }

        /* renamed from: toString, reason: from getter */
        public String getDescription() {
            return this.description;
        }
    }

    public HardwareFoldingFeature(Bounds bounds, Type type, FoldingFeature.State state) {
        k.e(bounds, "featureBounds");
        k.e(type, "type");
        k.e(state, "state");
        this.featureBounds = bounds;
        this.type = type;
        this.state = state;
        INSTANCE.a(bounds);
    }

    @Override // androidx.window.sidecar.FoldingFeature
    public FoldingFeature.Orientation a() {
        if (this.featureBounds.d() > this.featureBounds.a()) {
            return FoldingFeature.Orientation.f4431d;
        }
        return FoldingFeature.Orientation.f4430c;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!k.a(HardwareFoldingFeature.class, other != null ? other.getClass() : null)) {
            return false;
        }
        Objects.requireNonNull(other, "null cannot be cast to non-null type androidx.window.layout.HardwareFoldingFeature");
        HardwareFoldingFeature hardwareFoldingFeature = (HardwareFoldingFeature) other;
        return k.a(this.featureBounds, hardwareFoldingFeature.featureBounds) && k.a(this.type, hardwareFoldingFeature.type) && k.a(getState(), hardwareFoldingFeature.getState());
    }

    @Override // androidx.window.sidecar.FoldingFeature
    public FoldingFeature.State getState() {
        return this.state;
    }

    public int hashCode() {
        return (((this.featureBounds.hashCode() * 31) + this.type.hashCode()) * 31) + getState().hashCode();
    }

    public String toString() {
        return HardwareFoldingFeature.class.getSimpleName() + " { " + this.featureBounds + ", type=" + this.type + ", state=" + getState() + " }";
    }
}
