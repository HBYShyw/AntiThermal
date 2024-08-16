package com.coui.appcompat.expandable;

import java.util.ArrayList;

/* compiled from: ExpandableRecyclerPosition.java */
/* renamed from: com.coui.appcompat.expandable.b, reason: use source file name */
/* loaded from: classes.dex */
class ExpandableRecyclerPosition {

    /* renamed from: e, reason: collision with root package name */
    private static ArrayList<ExpandableRecyclerPosition> f5962e = new ArrayList<>(5);

    /* renamed from: a, reason: collision with root package name */
    public int f5963a;

    /* renamed from: b, reason: collision with root package name */
    public int f5964b;

    /* renamed from: c, reason: collision with root package name */
    int f5965c;

    /* renamed from: d, reason: collision with root package name */
    public int f5966d;

    private ExpandableRecyclerPosition() {
    }

    private static ExpandableRecyclerPosition a() {
        synchronized (f5962e) {
            if (f5962e.size() > 0) {
                ExpandableRecyclerPosition remove = f5962e.remove(0);
                remove.d();
                return remove;
            }
            return new ExpandableRecyclerPosition();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ExpandableRecyclerPosition b(int i10, int i11, int i12, int i13) {
        ExpandableRecyclerPosition a10 = a();
        a10.f5966d = i10;
        a10.f5963a = i11;
        a10.f5964b = i12;
        a10.f5965c = i13;
        return a10;
    }

    private void d() {
        this.f5963a = 0;
        this.f5964b = 0;
        this.f5965c = 0;
        this.f5966d = 0;
    }

    public void c() {
        synchronized (f5962e) {
            if (f5962e.size() < 5) {
                f5962e.add(this);
            }
        }
    }
}
