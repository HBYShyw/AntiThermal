package androidx.preference;

import android.R;
import android.util.SparseArray;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: PreferenceViewHolder.java */
/* renamed from: androidx.preference.l, reason: use source file name */
/* loaded from: classes.dex */
public class PreferenceViewHolder extends RecyclerView.c0 {

    /* renamed from: a, reason: collision with root package name */
    private final SparseArray<View> f3361a;

    /* renamed from: b, reason: collision with root package name */
    private boolean f3362b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f3363c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PreferenceViewHolder(View view) {
        super(view);
        SparseArray<View> sparseArray = new SparseArray<>(4);
        this.f3361a = sparseArray;
        sparseArray.put(R.id.title, view.findViewById(R.id.title));
        sparseArray.put(R.id.summary, view.findViewById(R.id.summary));
        sparseArray.put(R.id.icon, view.findViewById(R.id.icon));
        int i10 = R$id.icon_frame;
        sparseArray.put(i10, view.findViewById(i10));
        sparseArray.put(R.id.icon_frame, view.findViewById(R.id.icon_frame));
    }

    public View a(int i10) {
        View view = this.f3361a.get(i10);
        if (view != null) {
            return view;
        }
        View findViewById = this.itemView.findViewById(i10);
        if (findViewById != null) {
            this.f3361a.put(i10, findViewById);
        }
        return findViewById;
    }

    public boolean b() {
        return this.f3362b;
    }

    public boolean c() {
        return this.f3363c;
    }

    public void d(boolean z10) {
        this.f3362b = z10;
    }

    public void e(boolean z10) {
        this.f3363c = z10;
    }
}
