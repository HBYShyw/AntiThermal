package androidx.window.sidecar;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import androidx.window.core.Bounds;
import androidx.window.core.SpecificationComputer;
import androidx.window.sidecar.FoldingFeature;
import androidx.window.sidecar.HardwareFoldingFeature;
import androidx.window.sidecar.SidecarDeviceState;
import androidx.window.sidecar.SidecarDisplayFeature;
import androidx.window.sidecar.SidecarWindowLayoutInfo;
import com.oplus.thermalcontrol.ThermalControlConfig;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.r;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: SidecarAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0000\u0018\u0000 \b2\u00020\u0001:\u0001\u001dB\u0011\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u0018¢\u0006\u0004\b\u001b\u0010\u001cJ(\u0010\u0007\u001a\u00020\u00062\u000e\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u00022\u000e\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u0002H\u0002J\u001c\u0010\b\u001a\u00020\u00062\b\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003H\u0002J\"\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u00022\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\u00022\u0006\u0010\u000b\u001a\u00020\nJ\u0018\u0010\u0012\u001a\u00020\u00112\b\u0010\u000f\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0010\u001a\u00020\nJ\u001a\u0010\u0013\u001a\u00020\u00062\b\u0010\u0004\u001a\u0004\u0018\u00010\n2\b\u0010\u0005\u001a\u0004\u0018\u00010\nJ\u001a\u0010\u0014\u001a\u00020\u00062\b\u0010\u0004\u001a\u0004\u0018\u00010\u000e2\b\u0010\u0005\u001a\u0004\u0018\u00010\u000eJ!\u0010\u0016\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\nH\u0000¢\u0006\u0004\b\u0016\u0010\u0017R\u0014\u0010\u001a\u001a\u00020\u00188\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0013\u0010\u0019¨\u0006\u001e"}, d2 = {"Landroidx/window/layout/SidecarAdapter;", "", "", "Landroidx/window/sidecar/SidecarDisplayFeature;", "first", "second", "", "c", "b", "sidecarDisplayFeatures", "Landroidx/window/sidecar/SidecarDeviceState;", "deviceState", "Landroidx/window/layout/DisplayFeature;", "f", "Landroidx/window/sidecar/SidecarWindowLayoutInfo;", "extensionInfo", "state", "Landroidx/window/layout/WindowLayoutInfo;", "e", "a", "d", ThermalControlConfig.CONFIG_TYPE_FEATURE, "g", "(Landroidx/window/sidecar/SidecarDisplayFeature;Landroidx/window/sidecar/SidecarDeviceState;)Landroidx/window/layout/DisplayFeature;", "Landroidx/window/core/SpecificationComputer$VerificationMode;", "Landroidx/window/core/SpecificationComputer$VerificationMode;", "verificationMode", "<init>", "(Landroidx/window/core/SpecificationComputer$VerificationMode;)V", "Companion", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class SidecarAdapter {

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    /* renamed from: c, reason: collision with root package name */
    private static final String f4452c = SidecarAdapter.class.getSimpleName();

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final SpecificationComputer.VerificationMode verificationMode;

    /* compiled from: SidecarAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0014\u0010\u0015J\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0007J\u0017\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\u0007H\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u0010\u0010\f\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\u0007H\u0007J\u0018\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\tH\u0007R\u001c\u0010\u0012\u001a\n \u0011*\u0004\u0018\u00010\u00100\u00108\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0012\u0010\u0013¨\u0006\u0016"}, d2 = {"Landroidx/window/layout/SidecarAdapter$Companion;", "", "Landroidx/window/sidecar/SidecarWindowLayoutInfo;", "info", "", "Landroidx/window/sidecar/SidecarDisplayFeature;", "c", "Landroidx/window/sidecar/SidecarDeviceState;", "sidecarDeviceState", "", "b", "(Landroidx/window/sidecar/SidecarDeviceState;)I", "a", "posture", "Lma/f0;", "d", "", "kotlin.jvm.PlatformType", "TAG", "Ljava/lang/String;", "<init>", "()V", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @SuppressLint({"BanUncheckedReflection"})
        public final int a(SidecarDeviceState sidecarDeviceState) {
            k.e(sidecarDeviceState, "sidecarDeviceState");
            try {
                return sidecarDeviceState.posture;
            } catch (NoSuchFieldError unused) {
                try {
                    Object invoke = SidecarDeviceState.class.getMethod("getPosture", new Class[0]).invoke(sidecarDeviceState, new Object[0]);
                    if (invoke != null) {
                        return ((Integer) invoke).intValue();
                    }
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException unused2) {
                    return 0;
                }
            }
        }

        public final int b(SidecarDeviceState sidecarDeviceState) {
            k.e(sidecarDeviceState, "sidecarDeviceState");
            int a10 = a(sidecarDeviceState);
            if (a10 < 0 || a10 > 4) {
                return 0;
            }
            return a10;
        }

        @SuppressLint({"BanUncheckedReflection"})
        public final List<SidecarDisplayFeature> c(SidecarWindowLayoutInfo info) {
            List<SidecarDisplayFeature> j10;
            List<SidecarDisplayFeature> j11;
            k.e(info, "info");
            try {
                try {
                    List<SidecarDisplayFeature> list = info.displayFeatures;
                    if (list != null) {
                        return list;
                    }
                    j11 = r.j();
                    return j11;
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException unused) {
                    j10 = r.j();
                    return j10;
                }
            } catch (NoSuchFieldError unused2) {
                Object invoke = SidecarWindowLayoutInfo.class.getMethod("getDisplayFeatures", new Class[0]).invoke(info, new Object[0]);
                if (invoke != null) {
                    return (List) invoke;
                }
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.List<androidx.window.sidecar.SidecarDisplayFeature>");
            }
        }

        @SuppressLint({"BanUncheckedReflection"})
        public final void d(SidecarDeviceState sidecarDeviceState, int i10) {
            k.e(sidecarDeviceState, "sidecarDeviceState");
            try {
                try {
                    sidecarDeviceState.posture = i10;
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException unused) {
                }
            } catch (NoSuchFieldError unused2) {
                SidecarDeviceState.class.getMethod("setPosture", Integer.TYPE).invoke(sidecarDeviceState, Integer.valueOf(i10));
            }
        }
    }

    public SidecarAdapter() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public SidecarAdapter(SpecificationComputer.VerificationMode verificationMode) {
        k.e(verificationMode, "verificationMode");
        this.verificationMode = verificationMode;
    }

    private final boolean b(SidecarDisplayFeature first, SidecarDisplayFeature second) {
        if (k.a(first, second)) {
            return true;
        }
        if (first == null || second == null || first.getType() != second.getType()) {
            return false;
        }
        return k.a(first.getRect(), second.getRect());
    }

    private final boolean c(List<SidecarDisplayFeature> first, List<SidecarDisplayFeature> second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null || first.size() != second.size()) {
            return false;
        }
        int size = first.size();
        for (int i10 = 0; i10 < size; i10++) {
            if (!b(first.get(i10), second.get(i10))) {
                return false;
            }
        }
        return true;
    }

    public final boolean a(SidecarDeviceState first, SidecarDeviceState second) {
        if (k.a(first, second)) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        Companion companion = INSTANCE;
        return companion.b(first) == companion.b(second);
    }

    public final boolean d(SidecarWindowLayoutInfo first, SidecarWindowLayoutInfo second) {
        if (k.a(first, second)) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        Companion companion = INSTANCE;
        return c(companion.c(first), companion.c(second));
    }

    public final WindowLayoutInfo e(SidecarWindowLayoutInfo extensionInfo, SidecarDeviceState state) {
        List j10;
        k.e(state, "state");
        if (extensionInfo == null) {
            j10 = r.j();
            return new WindowLayoutInfo(j10);
        }
        SidecarDeviceState sidecarDeviceState = new SidecarDeviceState();
        Companion companion = INSTANCE;
        companion.d(sidecarDeviceState, companion.b(state));
        return new WindowLayoutInfo(f(companion.c(extensionInfo), sidecarDeviceState));
    }

    public final List<DisplayFeature> f(List<SidecarDisplayFeature> sidecarDisplayFeatures, SidecarDeviceState deviceState) {
        k.e(sidecarDisplayFeatures, "sidecarDisplayFeatures");
        k.e(deviceState, "deviceState");
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = sidecarDisplayFeatures.iterator();
        while (it.hasNext()) {
            DisplayFeature g6 = g((SidecarDisplayFeature) it.next(), deviceState);
            if (g6 != null) {
                arrayList.add(g6);
            }
        }
        return arrayList;
    }

    public final DisplayFeature g(SidecarDisplayFeature feature, SidecarDeviceState deviceState) {
        HardwareFoldingFeature.Type a10;
        FoldingFeature.State state;
        k.e(feature, ThermalControlConfig.CONFIG_TYPE_FEATURE);
        k.e(deviceState, "deviceState");
        SpecificationComputer.Companion companion = SpecificationComputer.INSTANCE;
        String str = f4452c;
        k.d(str, "TAG");
        SidecarDisplayFeature sidecarDisplayFeature = (SidecarDisplayFeature) SpecificationComputer.Companion.b(companion, feature, str, this.verificationMode, null, 4, null).c("Type must be either TYPE_FOLD or TYPE_HINGE", SidecarDisplayFeature.f4454e).c("Feature bounds must not be 0", C0129SidecarAdapter$translate$checkedFeature$2.f4455e).c("TYPE_FOLD must have 0 area", C0130SidecarAdapter$translate$checkedFeature$3.f4456e).c("Feature be pinned to either left or top", C0131SidecarAdapter$translate$checkedFeature$4.f4457e).a();
        if (sidecarDisplayFeature == null) {
            return null;
        }
        int type = sidecarDisplayFeature.getType();
        if (type == 1) {
            a10 = HardwareFoldingFeature.Type.INSTANCE.a();
        } else {
            if (type != 2) {
                return null;
            }
            a10 = HardwareFoldingFeature.Type.INSTANCE.b();
        }
        int b10 = INSTANCE.b(deviceState);
        if (b10 == 0 || b10 == 1) {
            return null;
        }
        if (b10 == 2) {
            state = FoldingFeature.State.f4435d;
        } else if (b10 == 3) {
            state = FoldingFeature.State.f4434c;
        } else {
            if (b10 == 4) {
                return null;
            }
            state = FoldingFeature.State.f4434c;
        }
        Rect rect = feature.getRect();
        k.d(rect, "feature.rect");
        return new HardwareFoldingFeature(new Bounds(rect), a10, state);
    }

    public /* synthetic */ SidecarAdapter(SpecificationComputer.VerificationMode verificationMode, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? SpecificationComputer.VerificationMode.QUIET : verificationMode);
    }
}
