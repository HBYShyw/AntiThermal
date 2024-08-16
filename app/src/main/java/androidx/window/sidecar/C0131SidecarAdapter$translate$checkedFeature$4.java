package androidx.window.sidecar;

import androidx.window.sidecar.SidecarDisplayFeature;
import kotlin.Metadata;
import ya.l;
import za.Lambda;
import za.k;

/* compiled from: SidecarAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"Landroidx/window/sidecar/SidecarDisplayFeature;", "", "a", "(Landroidx/window/sidecar/SidecarDisplayFeature;)Ljava/lang/Boolean;"}, k = 3, mv = {1, 6, 0})
/* renamed from: androidx.window.layout.SidecarAdapter$translate$checkedFeature$4, reason: from Kotlin metadata and case insensitive filesystem */
/* loaded from: classes.dex */
final class C0131SidecarAdapter$translate$checkedFeature$4 extends Lambda implements l<SidecarDisplayFeature, Boolean> {

    /* renamed from: e, reason: collision with root package name */
    public static final C0131SidecarAdapter$translate$checkedFeature$4 f4457e = new C0131SidecarAdapter$translate$checkedFeature$4();

    C0131SidecarAdapter$translate$checkedFeature$4() {
        super(1);
    }

    @Override // ya.l
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Boolean invoke(SidecarDisplayFeature sidecarDisplayFeature) {
        k.e(sidecarDisplayFeature, "$this$require");
        return Boolean.valueOf(sidecarDisplayFeature.getRect().left == 0 || sidecarDisplayFeature.getRect().top == 0);
    }
}
