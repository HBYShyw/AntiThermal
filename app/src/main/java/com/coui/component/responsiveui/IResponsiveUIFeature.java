package com.coui.component.responsiveui;

import androidx.lifecycle.MutableLiveData;
import com.coui.component.responsiveui.status.WindowFeature;
import kotlin.Metadata;

/* compiled from: IResponsiveUIFeature.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0002H&¨\u0006\u0005"}, d2 = {"Lcom/coui/component/responsiveui/IResponsiveUIFeature;", "", "Landroidx/lifecycle/u;", "Lcom/coui/component/responsiveui/status/WindowFeature;", "getWindowFeatureLiveData", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public interface IResponsiveUIFeature {
    MutableLiveData<WindowFeature> getWindowFeatureLiveData();
}
