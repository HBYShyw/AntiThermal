package androidx.core.widget;

import android.widget.ListView;

/* compiled from: ListViewCompat.java */
/* renamed from: androidx.core.widget.i, reason: use source file name */
/* loaded from: classes.dex */
public final class ListViewCompat {

    /* compiled from: ListViewCompat.java */
    /* renamed from: androidx.core.widget.i$a */
    /* loaded from: classes.dex */
    static class a {
        static boolean a(ListView listView, int i10) {
            return listView.canScrollList(i10);
        }

        static void b(ListView listView, int i10) {
            listView.scrollListBy(i10);
        }
    }

    public static void a(ListView listView, int i10) {
        a.b(listView, i10);
    }
}
