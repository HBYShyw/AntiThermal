package androidx.window.sidecar;

import androidx.window.sidecar.SidecarDisplayFeature;
import kotlin.Metadata;
import ya.l;
import za.Lambda;
import za.k;

/* compiled from: SidecarAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"Landroidx/window/sidecar/SidecarDisplayFeature;", "", "a", "(Landroidx/window/sidecar/SidecarDisplayFeature;)Ljava/lang/Boolean;"}, k = 3, mv = {1, 6, 0})
/* renamed from: androidx.window.layout.SidecarAdapter$translate$checkedFeature$2, reason: from Kotlin metadata and case insensitive filesystem */
/* loaded from: classes.dex */
final class C0129SidecarAdapter$translate$checkedFeature$2 extends Lambda implements l<SidecarDisplayFeature, Boolean> {

    /* renamed from: e, reason: collision with root package name */
    public static final C0129SidecarAdapter$translate$checkedFeature$2 f4455e = new C0129SidecarAdapter$translate$checkedFeature$2();

    C0129SidecarAdapter$translate$checkedFeature$2() {
        super(1);
    }

    @Override // ya.l
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Boolean invoke(SidecarDisplayFeature sidecarDisplayFeature) {
        k.e(sidecarDisplayFeature, "$this$require");
        return Boolean.valueOf((sidecarDisplayFeature.getRect().width() == 0 && sidecarDisplayFeature.getRect().height() == 0) ? false : true);
    }
}
