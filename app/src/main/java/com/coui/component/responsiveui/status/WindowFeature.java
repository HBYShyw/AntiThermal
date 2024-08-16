package com.coui.component.responsiveui.status;

import androidx.window.sidecar.DisplayFeature;
import androidx.window.sidecar.FoldingFeature;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.r;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: WindowFeature.kt */
@Metadata(bv = {}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\n\b\u0086\b\u0018\u00002\u00020\u0001B'\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004¢\u0006\u0004\b\u0017\u0010\u0018J\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u000f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004HÆ\u0003J\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004HÆ\u0003J)\u0010\u000b\u001a\u00020\u00002\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004HÆ\u0001J\t\u0010\r\u001a\u00020\fHÖ\u0001J\u0013\u0010\u0010\u001a\u00020\u000f2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001HÖ\u0003R\u001d\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u00048\u0006¢\u0006\f\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u0013\u0010\u0014R\u001d\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u00048\u0006¢\u0006\f\n\u0004\b\u0015\u0010\u0012\u001a\u0004\b\u0016\u0010\u0014¨\u0006\u0019"}, d2 = {"Lcom/coui/component/responsiveui/status/WindowFeature;", "", "", "toString", "", "Landroidx/window/layout/DisplayFeature;", "component1", "Landroidx/window/layout/FoldingFeature;", "component2", "displayFeatureList", "foldingFeatureList", "copy", "", "hashCode", "other", "", "equals", "a", "Ljava/util/List;", "getDisplayFeatureList", "()Ljava/util/List;", "b", "getFoldingFeatureList", "<init>", "(Ljava/util/List;Ljava/util/List;)V", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final /* data */ class WindowFeature {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final List<DisplayFeature> displayFeatureList;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final List<FoldingFeature> foldingFeatureList;

    public WindowFeature() {
        this(null, 0 == true ? 1 : 0, 3, 0 == true ? 1 : 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public WindowFeature(List<? extends DisplayFeature> list, List<? extends FoldingFeature> list2) {
        k.e(list, "displayFeatureList");
        k.e(list2, "foldingFeatureList");
        this.displayFeatureList = list;
        this.foldingFeatureList = list2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ WindowFeature copy$default(WindowFeature windowFeature, List list, List list2, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            list = windowFeature.displayFeatureList;
        }
        if ((i10 & 2) != 0) {
            list2 = windowFeature.foldingFeatureList;
        }
        return windowFeature.copy(list, list2);
    }

    public final List<DisplayFeature> component1() {
        return this.displayFeatureList;
    }

    public final List<FoldingFeature> component2() {
        return this.foldingFeatureList;
    }

    public final WindowFeature copy(List<? extends DisplayFeature> displayFeatureList, List<? extends FoldingFeature> foldingFeatureList) {
        k.e(displayFeatureList, "displayFeatureList");
        k.e(foldingFeatureList, "foldingFeatureList");
        return new WindowFeature(displayFeatureList, foldingFeatureList);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof WindowFeature)) {
            return false;
        }
        WindowFeature windowFeature = (WindowFeature) other;
        return k.a(this.displayFeatureList, windowFeature.displayFeatureList) && k.a(this.foldingFeatureList, windowFeature.foldingFeatureList);
    }

    public final List<DisplayFeature> getDisplayFeatureList() {
        return this.displayFeatureList;
    }

    public final List<FoldingFeature> getFoldingFeatureList() {
        return this.foldingFeatureList;
    }

    public int hashCode() {
        return (this.displayFeatureList.hashCode() * 31) + this.foldingFeatureList.hashCode();
    }

    public String toString() {
        return "WindowFeature { displayFeature = " + this.displayFeatureList + ", foldingFeature = " + this.foldingFeatureList + " }";
    }

    public /* synthetic */ WindowFeature(List list, List list2, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? r.j() : list, (i10 & 2) != 0 ? r.j() : list2);
    }
}
