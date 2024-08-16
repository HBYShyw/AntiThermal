package androidx.recyclerview.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes.dex */
public class COUIPanelPreferenceLinearLayoutManager extends LinearLayoutManager {
    public COUIPanelPreferenceLinearLayoutManager(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
    public int u(RecyclerView.z zVar) {
        return super.u(zVar) + this.f3487b.getScrollY();
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
    public int v(RecyclerView.z zVar) {
        return super.v(zVar) + this.f3487b.getScrollY();
    }
}
