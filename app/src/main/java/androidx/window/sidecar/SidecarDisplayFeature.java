package androidx.window.sidecar;

import kotlin.Metadata;
import ya.l;
import za.Lambda;
import za.k;

/* compiled from: SidecarAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"Landroidx/window/sidecar/SidecarDisplayFeature;", "", "a", "(Landroidx/window/sidecar/SidecarDisplayFeature;)Ljava/lang/Boolean;"}, k = 3, mv = {1, 6, 0})
/* renamed from: androidx.window.layout.SidecarAdapter$translate$checkedFeature$1, reason: from Kotlin metadata */
/* loaded from: classes.dex */
final class SidecarDisplayFeature extends Lambda implements l<androidx.window.sidecar.SidecarDisplayFeature, Boolean> {

    /* renamed from: e, reason: collision with root package name */
    public static final SidecarDisplayFeature f4454e = new SidecarDisplayFeature();

    SidecarDisplayFeature() {
        super(1);
    }

    @Override // ya.l
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Boolean invoke(androidx.window.sidecar.SidecarDisplayFeature sidecarDisplayFeature) {
        k.e(sidecarDisplayFeature, "$this$require");
        boolean z10 = true;
        if (sidecarDisplayFeature.getType() != 1 && sidecarDisplayFeature.getType() != 2) {
            z10 = false;
        }
        return Boolean.valueOf(z10);
    }
}
