package androidx.window.sidecar;

import kotlin.Metadata;

/* compiled from: FoldingFeature.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001:\u0003\n\u000b\fR\u0014\u0010\u0005\u001a\u00020\u00028&X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004R\u0014\u0010\t\u001a\u00020\u00068&X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\r"}, d2 = {"Landroidx/window/layout/FoldingFeature;", "Landroidx/window/layout/DisplayFeature;", "Landroidx/window/layout/FoldingFeature$Orientation;", "a", "()Landroidx/window/layout/FoldingFeature$Orientation;", "orientation", "Landroidx/window/layout/FoldingFeature$State;", "getState", "()Landroidx/window/layout/FoldingFeature$State;", "state", "OcclusionType", "Orientation", "State", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public interface FoldingFeature extends DisplayFeature {

    /* compiled from: FoldingFeature.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\t\u0018\u0000 \t2\u00020\u0001:\u0001\nB\u0011\b\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0002¢\u0006\u0004\b\u0007\u0010\bJ\b\u0010\u0003\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0004\u0010\u0005¨\u0006\u000b"}, d2 = {"Landroidx/window/layout/FoldingFeature$OcclusionType;", "", "", "toString", "a", "Ljava/lang/String;", "description", "<init>", "(Ljava/lang/String;)V", "b", "Companion", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class OcclusionType {

        /* renamed from: c, reason: collision with root package name */
        public static final OcclusionType f4426c = new OcclusionType("NONE");

        /* renamed from: d, reason: collision with root package name */
        public static final OcclusionType f4427d = new OcclusionType("FULL");

        /* renamed from: a, reason: collision with root package name and from kotlin metadata */
        private final String description;

        private OcclusionType(String str) {
            this.description = str;
        }

        /* renamed from: toString, reason: from getter */
        public String getDescription() {
            return this.description;
        }
    }

    /* compiled from: FoldingFeature.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\t\u0018\u0000 \t2\u00020\u0001:\u0001\nB\u0011\b\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0002¢\u0006\u0004\b\u0007\u0010\bJ\b\u0010\u0003\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0004\u0010\u0005¨\u0006\u000b"}, d2 = {"Landroidx/window/layout/FoldingFeature$Orientation;", "", "", "toString", "a", "Ljava/lang/String;", "description", "<init>", "(Ljava/lang/String;)V", "b", "Companion", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class Orientation {

        /* renamed from: c, reason: collision with root package name */
        public static final Orientation f4430c = new Orientation("VERTICAL");

        /* renamed from: d, reason: collision with root package name */
        public static final Orientation f4431d = new Orientation("HORIZONTAL");

        /* renamed from: a, reason: collision with root package name and from kotlin metadata */
        private final String description;

        private Orientation(String str) {
            this.description = str;
        }

        /* renamed from: toString, reason: from getter */
        public String getDescription() {
            return this.description;
        }
    }

    /* compiled from: FoldingFeature.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\t\u0018\u0000 \t2\u00020\u0001:\u0001\nB\u0011\b\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0002¢\u0006\u0004\b\u0007\u0010\bJ\b\u0010\u0003\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0004\u0010\u0005¨\u0006\u000b"}, d2 = {"Landroidx/window/layout/FoldingFeature$State;", "", "", "toString", "a", "Ljava/lang/String;", "description", "<init>", "(Ljava/lang/String;)V", "b", "Companion", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class State {

        /* renamed from: c, reason: collision with root package name */
        public static final State f4434c = new State("FLAT");

        /* renamed from: d, reason: collision with root package name */
        public static final State f4435d = new State("HALF_OPENED");

        /* renamed from: a, reason: collision with root package name and from kotlin metadata */
        private final String description;

        private State(String str) {
            this.description = str;
        }

        /* renamed from: toString, reason: from getter */
        public String getDescription() {
            return this.description;
        }
    }

    Orientation a();

    State getState();
}
